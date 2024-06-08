package law.ethos.commands;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GameModeCommand implements CommandExecutor {

    private final GameMode gameMode;

    public GameModeCommand(GameMode gameMode) {
        this.gameMode = gameMode;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command.");
            return false;
        }

        Player player = (Player) sender;
        player.setGameMode(gameMode);
        player.sendMessage(ChatColor.GREEN + "Game mode set to " + gameMode.name().toLowerCase() + ".");
        return true;
    }
}