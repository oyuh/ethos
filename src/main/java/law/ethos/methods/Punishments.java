package law.ethos.methods;

import law.ethos.DatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Punishments {

    private static final Logger logger = Bukkit.getLogger();

    public enum PunishmentType {
        BAN, MUTE, KICK
    }

    public static class Punishment {
        private final PunishmentType type;
        private final String reason;
        private final Date date;
        private final long duration;

        public Punishment(PunishmentType type, String reason, Date date, long duration) {
            this.type = type;
            this.reason = reason;
            this.date = date;
            this.duration = duration;
        }

        public PunishmentType getType() {
            return type;
        }

        public String getReason() {
            return reason;
        }

        public Date getDate() {
            return date;
        }

        public long getDuration() {
            return duration;
        }
    }

    public static void banPlayer(String playerName, String reason, String durationString) {
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerName);
        long duration = parseDuration(durationString);
        recordPunishment(offlinePlayer.getUniqueId(), PunishmentType.BAN, reason, duration);
        if (offlinePlayer.isOnline()) {
            ((Player) offlinePlayer).kickPlayer(ChatColor.RED + "You have been banned for: " + reason);
        }
    }

    public static void kickPlayer(Player player, String reason) {
        recordPunishment(player.getUniqueId(), PunishmentType.KICK, reason, 0);
        player.kickPlayer(ChatColor.RED + "You have been kicked for: " + reason);
    }

    public static void mutePlayer(String playerName, String reason, String durationString) {
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerName);
        long duration = parseDuration(durationString);
        recordPunishment(offlinePlayer.getUniqueId(), PunishmentType.MUTE, reason, duration);
    }

    public static boolean unbanPlayer(String playerName) {
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerName);
        return removePunishment(offlinePlayer.getUniqueId(), PunishmentType.BAN);
    }

    public static void unmutePlayer(String playerName) {
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerName);
        removePunishment(offlinePlayer.getUniqueId(), PunishmentType.MUTE);
    }

    public static List<Punishment> getPunishments(UUID uuid) {
        List<Punishment> punishments = new ArrayList<>();
        String sql = "SELECT type, reason, expiration FROM punishments WHERE uuid = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                PunishmentType type = PunishmentType.valueOf(rs.getString("type").toUpperCase());
                String reason = rs.getString("reason");
                long expiration = rs.getLong("expiration");
                Date date = new Date(expiration > 0 ? expiration : System.currentTimeMillis());
                long duration = expiration > 0 ? (expiration - System.currentTimeMillis()) / 1000 : -1;
                punishments.add(new Punishment(type, reason, date, duration));
            }
            logger.info("Retrieved " + punishments.size() + " punishments for UUID " + uuid.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return punishments;
    }

    private static void recordPunishment(UUID uuid, PunishmentType type, String reason, long duration) {
        String sql = "INSERT INTO punishments (uuid, type, reason, expiration) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, uuid.toString());
            ps.setString(2, type.toString().toLowerCase());
            ps.setString(3, reason);
            ps.setLong(4, duration > 0 ? System.currentTimeMillis() + duration * 1000 : -1);
            ps.executeUpdate();
            logger.info("Recorded punishment: " + type + " for UUID " + uuid.toString() + " with reason: " + reason);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static boolean removePunishment(UUID uuid, PunishmentType type) {
        String sql = "DELETE FROM punishments WHERE uuid = ? AND type = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, uuid.toString());
            ps.setString(2, type.toString().toLowerCase());
            int rowsAffected = ps.executeUpdate();
            logger.info("Removed " + rowsAffected + " punishments of type " + type + " for UUID " + uuid.toString());
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static long parseDuration(String durationString) {
        long duration = 0;
        Pattern pattern = Pattern.compile("(\\d+)([smhd])");
        Matcher matcher = pattern.matcher(durationString);
        while (matcher.find()) {
            int value = Integer.parseInt(matcher.group(1));
            String unit = matcher.group(2);
            switch (unit) {
                case "s":
                    duration += value;
                    break;
                case "m":
                    duration += value * 60;
                    break;
                case "h":
                    duration += value * 3600;
                    break;
                case "d":
                    duration += value * 86400;
                    break;
            }
        }
        return duration;
    }

    public static String formatDuration(long seconds) {
        if (seconds == -1) {
            return "Permanent";
        }

        Duration duration = Duration.of(seconds, ChronoUnit.SECONDS);
        long days = duration.toDays();
        long hours = duration.toHours() % 24;
        long minutes = duration.toMinutes() % 60;
        long secs = duration.getSeconds() % 60;

        StringBuilder formatted = new StringBuilder();
        if (days > 0) {
            formatted.append(days).append(" day(s) ");
        }
        if (hours > 0) {
            formatted.append(hours).append(" hour(s) ");
        }
        if (minutes > 0) {
            formatted.append(minutes).append(" minute(s) ");
        }
        if (secs > 0) {
            formatted.append(secs).append(" second(s) ");
        }

        return formatted.toString().trim();
    }
}
