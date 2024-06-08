package law.ethos.commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class TopCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command can only be run by a player.");
            return false;
        }

        Player player = (Player) sender;
        Location loc = player.getLocation();
        loc.setY(player.getWorld().getHighestBlockYAt(loc));
        player.teleport(loc);
        player.sendMessage(ChatColor.GREEN + "Teleported to the top.");
        return true;
    }
}
