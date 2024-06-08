package law.ethos;

import law.ethos.commands.*;
import law.ethos.listeners.*;
import law.ethos.methods.*;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class Ethos extends JavaPlugin {

    private static Ethos instance;

    public static Ethos getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        // Plugin startup logic
        getLogger().info("Ethos plugin enabled!");

        // Connect to the database
        DatabaseManager.connect();

        // Register commands
        registerCommands();

        // Register listeners
        registerListeners();

        // Initialize default rank
        initializeDefaultRank();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("Ethos plugin disabled!");

        // Disconnect from the database
        DatabaseManager.disconnect();
    }

    private void initializeDefaultRank() {
        String defaultRankName = "Player";
        if (Ranks.getRank(defaultRankName) == null) {
            Ranks.createRank(defaultRankName, ChatColor.WHITE, "&7[Player]", 0);
            getLogger().info("Default rank 'Player' created.");
        }
    }

    private void registerCommands() {
        // Register all commands here
        getCommand("createrank").setExecutor(new CreateRankCommand());
        getCommand("grantrank").setExecutor(new GrantRankCommand());
        getCommand("editrank").setExecutor(new EditRankCommand());
        getCommand("deleterank").setExecutor(new DeleteRankCommand());
        getCommand("setpermission").setExecutor(new SetPermissionCommand());
        getCommand("listranks").setExecutor(new ListRanksCommand());
        getCommand("list").setExecutor(new ListCommand());
        getCommand("info").setExecutor(new InfoCommand());
        getCommand("ban").setExecutor(new BanCommand());
        getCommand("kick").setExecutor(new KickCommand());
        getCommand("mute").setExecutor(new MuteCommand());
        getCommand("unban").setExecutor(new UnbanCommand());
        getCommand("unmute").setExecutor(new UnmuteCommand());
        getCommand("fly").setExecutor(new FlyCommand());
        getCommand("gmc").setExecutor(new GMCCommand());
        getCommand("gms").setExecutor(new GMSCommand());
        getCommand("tp").setExecutor(new TPCommand());
        getCommand("history").setExecutor(new HistoryCommand());
    }

    private void registerListeners() {
        // Register all listeners here
        getServer().getPluginManager().registerEvents(new ChatListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(), this);
        getServer().getPluginManager().registerEvents(new InventoryClickListener(), this);
    }
}
