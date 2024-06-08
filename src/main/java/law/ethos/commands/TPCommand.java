package law.ethos.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TPCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command can only be run by a player.");
            return false;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("ethos.teleport")) {
            player.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
            return false;
        }

        if (args.length != 1 && args.length != 2) {
            player.sendMessage(ChatColor.RED + "Usage: /teleport <player> [target]");
            return false;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(ChatColor.RED + "Player not found.");
            return false;
        }

        if (args.length == 1) {
            player.teleport(target.getLocation());
            player.sendMessage(ChatColor.GREEN + "Teleported to " + target.getName() + ".");
        } else {
            Player destination = Bukkit.getPlayer(args[1]);
            if (destination == null) {
                player.sendMessage(ChatColor.RED + "Target player not found.");
                return false;
            }
            target.teleport(destination.getLocation());
            player.sendMessage(ChatColor.GREEN + "Teleported " + target.getName() + " to " + destination.getName() + ".");
        }

        return true;
    }
}
