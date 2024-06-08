package law.ethos.commands;

import law.ethos.methods.Punishments;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KickCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("ethos.kick")) {
            sender.sendMessage("You do not have permission to use this command.");
            return true;
        }

        if (args.length < 2) {
            sender.sendMessage("Usage: /kick <player> <reason>");
            return false;
        }

        Player targetPlayer = Bukkit.getPlayer(args[0]);
        if (targetPlayer == null) {
            sender.sendMessage("Player not found.");
            return false;
        }

        String reason = String.join(" ", args).substring(args[0].length() + 1); // Combine all args after player name for reason
        Punishments.kickPlayer(targetPlayer, reason);
        sender.sendMessage("Player " + targetPlayer.getName() + " has been kicked for " + reason);
        return true;
    }
}
