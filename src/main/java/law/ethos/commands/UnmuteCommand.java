package law.ethos.commands;

import law.ethos.methods.Punishments;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class UnmuteCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("ethos.unmute")) {
            sender.sendMessage("You do not have permission to use this command.");
            return true;
        }

        if (args.length != 1) {
            sender.sendMessage("Usage: /unmute <player>");
            return false;
        }

        String playerName = args[0];
        Punishments.unmutePlayer(playerName);
        sender.sendMessage("Player " + playerName + " has been unmuted.");
        return true;
    }
}
