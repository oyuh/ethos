package law.ethos.commands;

import law.ethos.methods.Punishments;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class UnbanCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("ethos.unban")) {
            sender.sendMessage("You do not have permission to use this command.");
            return true;
        }

        if (args.length != 1) {
            sender.sendMessage("Usage: /unban <player>");
            return false;
        }

        String playerName = args[0];
        if (Punishments.unbanPlayer(playerName)) {
            sender.sendMessage("Player " + playerName + " has been unbanned.");
        } else {
            sender.sendMessage("Player " + playerName + " is not banned.");
        }
        return true;
    }
}
