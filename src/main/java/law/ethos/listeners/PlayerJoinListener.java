package law.ethos.listeners;

import law.ethos.DatabaseManager;
import law.ethos.methods.Ranks;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String playerName = player.getName();
        String playerUUID = player.getUniqueId().toString();

        if (!isPlayerInDatabase(playerUUID)) {
            savePlayerToDatabase(playerUUID, playerName);
            // Assign default rank
            Ranks.grantRank(player.getUniqueId(), "Player");
        }

        // Update last login time
        updatePlayerLastLogin(playerUUID);

        Ranks.Rank rank = Ranks.getPlayerRank(player.getUniqueId());
        if (rank != null) {
            player.setDisplayName(rank.getColor() + player.getName() + ChatColor.RESET);
        }
    }

    private boolean isPlayerInDatabase(String uuid) {
        String sql = "SELECT uuid FROM players WHERE uuid = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, uuid);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void savePlayerToDatabase(String uuid, String name) {
        String sql = "INSERT INTO players (uuid, name) VALUES (?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, uuid);
            ps.setString(2, name);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updatePlayerLastLogin(String uuid) {
        String sql = "UPDATE players SET last_login = CURRENT_TIMESTAMP WHERE uuid = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, uuid);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
