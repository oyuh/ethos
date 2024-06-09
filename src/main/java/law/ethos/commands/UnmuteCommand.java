package law.ethos.commands;

import law.ethos.methods.Punishments;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class UnmuteCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("ethos.unmute")) {
            sender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
            return true;
        }

        if (args.length != 1) {
            sender.sendMessage(ChatColor.RED + "Usage: /unmute <player>");
            return false;
        }

        String playerName = args[0];
        Punishments.unmutePlayer(playerName);
        sender.sendMessage(ChatColor.GREEN + "Player " + playerName + " has been unmuted.");
        return true;
    }
}