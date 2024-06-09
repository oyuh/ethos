package law.ethos.methods;

import law.ethos.DatabaseManager;
import org.bukkit.ChatColor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class Ranks {

    private static final Map<String, Rank> ranks = new HashMap<>();
    private static final Map<UUID, String> playerRanks = new HashMap<>();
    private static final Map<String, List<String>> rankPermissions = new HashMap<>();

    public static boolean createRank(String name, ChatColor color, String prefix, int weight) {
        if (ranks.containsKey(name)) {
            System.out.println("Rank already exists in memory: " + name);
            return false; // Rank already exists in memory
        }

        // Attempt to create rank only if not already in database
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT name FROM ranks WHERE name = ?")) {
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                System.out.println("Rank already exists in database: " + name);
                return false; // Rank exists in the database
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        ranks.put(name, new Rank(name, color.toString(), prefix, weight));
        saveRankToDatabase(name, color.toString(), prefix, weight);
        return true;
    }

    public static boolean editRank(String name, ChatColor color, String prefix, int weight) {
        if (!ranks.containsKey(name)) return false;
        ranks.put(name, new Rank(name, color.name(), prefix, weight));
        updateRankInDatabase(name, color.name(), prefix, weight);
        return true;
    }

    public static boolean deleteRank(String name) {
        if (!ranks.containsKey(name)) return false;
        ranks.remove(name);
        deleteRankFromDatabase(name);
        return true;
    }

    public static Rank getPlayerRank(UUID uuid) {
        String rankName = playerRanks.get(uuid);
        return rankName != null ? ranks.get(rankName) : null;
    }

    public static Rank getRank(String name) {
        return ranks.get(name);
    }

    public static void grantRank(UUID uuid, String rankName) {
        setRank(uuid, rankName);
    }

    public static void setRank(UUID uuid, String rankName) {
        playerRanks.put(uuid, rankName);
        savePlayerRankToDatabase(uuid, rankName);
    }

    public static Iterable<Rank> getAllRanks() {
        return ranks.values();
    }

    public static List<Rank> getAllRanksList() {
        return new ArrayList<>(ranks.values());
    }

    public static void addPermissionToRank(String rankName, String permission) {
        List<String> permissions = rankPermissions.getOrDefault(rankName, new ArrayList<>());
        permissions.add(permission);
        rankPermissions.put(rankName, permissions);
        saveRankPermissionToDatabase(rankName, permission);
    }

    public static void saveRankToDatabase(String name, String color, String prefix, int weight) {
        String sql = "INSERT INTO ranks (name, color, prefix, weight) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, color);
            ps.setString(3, prefix);
            ps.setInt(4, weight);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void updateRankInDatabase(String name, String color, String prefix, int weight) {
        String sql = "UPDATE ranks SET color = ?, prefix = ?, weight = ? WHERE name = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, color);
            ps.setString(2, prefix);
            ps.setInt(3, weight);
            ps.setString(4, name);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void deleteRankFromDatabase(String name) {
        String sql = "DELETE FROM ranks WHERE name = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void savePlayerRankToDatabase(UUID uuid, String rankName) {
        String sql = "INSERT OR REPLACE INTO player_ranks (uuid, rank) VALUES (?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, uuid.toString());
            ps.setString(2, rankName);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void saveRankPermissionToDatabase(String rankName, String permission) {
        String sql = "INSERT INTO rank_permissions (rank, permission) VALUES (?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, rankName);
            ps.setString(2, permission);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void loadRanksFromDatabase() {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT name, color, prefix, weight FROM ranks");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String name = rs.getString("name");
                String color = rs.getString("color");
                String prefix = rs.getString("prefix");
                int weight = rs.getInt("weight");
                ranks.put(name, new Rank(name, color, prefix, weight));
                System.out.println("Rank loaded: " + name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static class Rank {
        private final String name;
        private final String color;
        private final String prefix;
        private final int weight;

        public Rank(String name, String color, String prefix, int weight) {
            this.name = name;
            this.color = color;
            this.prefix = prefix;
            this.weight = weight;
        }

        public String getName() {
            return name;
        }

        public ChatColor getColor() {
            try {
                return ChatColor.valueOf(color.toUpperCase().replace('&', '§'));
            } catch (IllegalArgumentException e) {
                return ChatColor.WHITE; // default color if the provided color is invalid
            }
        }

        public String getPrefix() {
            return prefix;
        }

        public int getWeight() {
            return weight;
        }

        @Override
        public String toString() {
            return ChatColor.translateAlternateColorCodes('&', color + prefix + " " + name);
        }
    }
}
