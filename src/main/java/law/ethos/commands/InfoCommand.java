package law.ethos.commands;

import law.ethos.gui.InfoGUI;
import law.ethos.methods.Ranks;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InfoCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("ethos.info")) {
            sender.sendMessage("You do not have permission to use this command.");
            return true;
        }

        if (args.length != 1) {
            sender.sendMessage("Usage: /info <rank|player>");
            return false;
        }
        String target = args[0];

        // Check if the target is a rank
        Ranks.Rank rank = Ranks.getRank(target);
        if (rank != null) {
            InfoGUI.openRankInfo((Player) sender, rank);
            return true;
        }

        // Check if the target is a player
        Player targetPlayer = Bukkit.getPlayer(target);
        if (targetPlayer != null) {
            String lastLocation = targetPlayer.getLocation().toString();
            String lastLogin = fetchPlayerLastLoginFromDatabase(targetPlayer.getUniqueId().toString());
            InfoGUI.openPlayerInfo((Player) sender, targetPlayer.getName(), targetPlayer.getUniqueId().toString(), lastLocation, lastLogin);
            return true;
        } else {
            // Fetch player info from the database
            String[] playerInfo = fetchPlayerInfoFromDatabase(target);
            if (playerInfo != null) {
                InfoGUI.openPlayerInfo((Player) sender, playerInfo[0], playerInfo[1], playerInfo[2], playerInfo[3]);
            } else {
                sender.sendMessage("No rank or player found with the name: " + target);
            }
        }

        return true;
    }

    private String[] fetchPlayerInfoFromDatabase(String playerName) {
        String sql = "SELECT * FROM players WHERE name = ?";
        try (Connection conn = law.ethos.DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, playerName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String uuid = rs.getString("uuid");
                String lastLocation = rs.getString("last_location");
                String lastLogin = rs.getString("last_login");
                return new String[]{playerName, uuid, lastLocation, lastLogin};
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String fetchPlayerLastLoginFromDatabase(String uuid) {
        String sql = "SELECT last_login FROM players WHERE uuid = ?";
        try (Connection conn = law.ethos.DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, uuid);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("last_login");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "N/A";
    }
}
