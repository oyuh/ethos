package law.ethos;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseManager {
    private static Connection connection;

    public static void connect() {
        try {
            File dataFolder = new File("plugins/Ethos");
            if (!dataFolder.exists()) {
                dataFolder.mkdirs();
            }

            connection = DriverManager.getConnection("jdbc:sqlite:" + dataFolder + "/ethos.db");
            createTables();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void disconnect() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void reconnect() {
        disconnect();
        connect();
    }

    private static void createTables() {
        String createRanksTable = "CREATE TABLE IF NOT EXISTS ranks (" +
                "name TEXT PRIMARY KEY, " +
                "color TEXT, " +
                "prefix TEXT, " +
                "weight INTEGER);";

        String createPlayerRanksTable = "CREATE TABLE IF NOT EXISTS player_ranks (" +
                "uuid TEXT PRIMARY KEY, " +
                "rank TEXT);";

        String createPlayersTable = "CREATE TABLE IF NOT EXISTS players (" +
                "uuid TEXT PRIMARY KEY, " +
                "name TEXT, " +
                "last_location TEXT, " +
                "last_login INTEGER);";

        String createPunishmentsTable = "CREATE TABLE IF NOT EXISTS punishments (" +
                "uuid TEXT, " +
                "type TEXT, " +
                "reason TEXT, " +
                "expiration INTEGER);";

        String createRankPermissionsTable = "CREATE TABLE IF NOT EXISTS rank_permissions (" +
                "rank TEXT, " +
                "permission TEXT);";

        try (PreparedStatement ps1 = connection.prepareStatement(createRanksTable);
             PreparedStatement ps2 = connection.prepareStatement(createPlayerRanksTable);
             PreparedStatement ps3 = connection.prepareStatement(createPlayersTable);
             PreparedStatement ps4 = connection.prepareStatement(createPunishmentsTable);
             PreparedStatement ps5 = connection.prepareStatement(createRankPermissionsTable)) {
            ps1.execute();
            ps2.execute();
            ps3.execute();
            ps4.execute();
            ps5.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                reconnect();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }
}
