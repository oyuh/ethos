package law.ethos.methods;

import law.ethos.DatabaseManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class Ranks {
    private static Map<String, Rank> ranks = new HashMap<>();
    private static Map<String, List<String>> rankPermissions = new HashMap<>();

    public static class Rank {
        private String name;
        private ChatColor color;
        private String prefix;
        private int weight;

        public Rank(String name, ChatColor color, String prefix, int weight) {
            this.name = name;
            this.color = color;
            this.prefix = prefix;
            this.weight = weight;
        }

        public String getName() {
            return name;
        }

        public ChatColor getColor() {
            return color;
        }

        public String getPrefix() {
            return prefix;
        }

        public int getWeight() {
            return weight;
        }

        public void setColor(ChatColor color) {
            this.color = color;
        }

        public void setPrefix(String prefix) {
            this.prefix = prefix;
        }

        public void setWeight(int weight) {
            this.weight = weight;
        }
    }

    static {
        loadRanksFromDatabase();
        loadRankPermissionsFromDatabase();
    }

    public static void createRank(String name, ChatColor color, String prefix, int weight) {
        ranks.put(name, new Rank(name, color, prefix, weight));
        saveRankToDatabase(name, color, prefix, weight);
        System.out.println("Rank created: " + name);
    }

    public static void editRank(String name, ChatColor color, String prefix, int weight) {
        if (ranks.containsKey(name)) {
            Rank rank = ranks.get(name);
            rank.setColor(color);
            rank.setPrefix(prefix);
            rank.setWeight(weight);
            updateRankInDatabase(name, color, prefix, weight);
            System.out.println("Rank edited: " + name);
        }
    }

    public static void deleteRank(String name) {
        ranks.remove(name);
        deleteRankFromDatabase(name);
        System.out.println("Rank deleted: " + name);
    }

    public static void grantRank(Player player, String rankName) {
        Rank rank = ranks.get(rankName);
        if (rank != null) {
            savePlayerRankToDatabase(player.getUniqueId().toString(), rankName);
            System.out.println("Granted rank " + rankName + " to player " + player.getName());
        } else {
            System.out.println("Rank " + rankName + " not found.");
        }
    }

    public static Rank getRank(String rankName) {
        System.out.println("Fetching rank for: " + rankName);
        return ranks.get(rankName);
    }

    public static Rank getPlayerRank(String playerName) {
        String rankName = fetchPlayerRankFromDatabase(playerName);
        System.out.println("Fetching player rank for: " + playerName + " - Rank: " + rankName);
        return ranks.get(rankName);
    }

    public static List<Rank> getAllRanks() {
        return new ArrayList<>(ranks.values());
    }

    public static void addPermissionToRank(String rankName, String permission) {
        List<String> permissions = rankPermissions.getOrDefault(rankName, new ArrayList<>());
        permissions.add(permission);
        rankPermissions.put(rankName, permissions);
        saveRankPermissionToDatabase(rankName, permission);
    }

    private static void saveRankToDatabase(String name, ChatColor color, String prefix, int weight) {
        String sql = "INSERT INTO ranks (name, color, prefix, weight) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, color.name());
            ps.setString(3, prefix);
            ps.setInt(4, weight);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void updateRankInDatabase(String name, ChatColor color, String prefix, int weight) {
        String sql = "UPDATE ranks SET color = ?, prefix = ?, weight = ? WHERE name = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, color.name());
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

    private static void loadRanksFromDatabase() {
        String sql = "SELECT name, color, prefix, weight FROM ranks";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String name = rs.getString("name");
                ChatColor color = ChatColor.valueOf(rs.getString("color"));
                String prefix = rs.getString("prefix");
                int weight = rs.getInt("weight");
                ranks.put(name, new Rank(name, color, prefix, weight));
                System.out.println("Rank loaded: " + name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void savePlayerRankToDatabase(String uuid, String rankName) {
        String sql = "INSERT OR REPLACE INTO player_ranks (uuid, rank) VALUES (?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, uuid);
            ps.setString(2, rankName);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static String fetchPlayerRankFromDatabase(String playerName) {
        String sql = "SELECT rank FROM player_ranks WHERE uuid = (SELECT uuid FROM players WHERE name = ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, playerName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("rank");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
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

    private static void loadRankPermissionsFromDatabase() {
        String sql = "SELECT rank, permission FROM rank_permissions";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String rankName = rs.getString("rank");
                String permission = rs.getString("permission");
                List<String> permissions = rankPermissions.getOrDefault(rankName, new ArrayList<>());
                permissions.add(permission);
                rankPermissions.put(rankName, permissions);
                System.out.println("Permission loaded: " + permission + " for rank: " + rankName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
