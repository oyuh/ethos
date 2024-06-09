package law.ethos.commands;

import law.ethos.methods.Punishments;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class MuteCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("ethos.mute")) {
            sender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
            return true;
        }

        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Usage: /mute <player> <reason> [duration]");
            return false;
        }

        String playerName = args[0];
        String reason = args[1];
        String durationString = args.length == 3 ? args[2] : "-1";

        Punishments.mutePlayer(playerName, reason, durationString);
        sender.sendMessage(ChatColor.GREEN + "Player " + playerName + " has been muted for " + reason + " (Duration: " + durationString + ")");
        return true;
    }
}