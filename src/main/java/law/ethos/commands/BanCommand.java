package law.ethos.commands;

import law.ethos.methods.Punishments;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BanCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("ethos.ban")) {
            sender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
            return true;
        }

        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Usage: /ban <player> <reason> [duration]");
            return false;
        }

        String playerName = args[0];
        String reason = args[1];
        String durationString = args.length == 3 ? args[2] : "-1";

        Punishments.banPlayer(playerName, reason, durationString);
        sender.sendMessage(ChatColor.GREEN + "Player " + playerName + " has been banned for " + reason + " (Duration: " + durationString + ")");
        return true;
    }
}