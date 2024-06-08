package law.ethos.commands;

import law.ethos.methods.Ranks;
import law.ethos.methods.Colors;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CreateRankCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("ethos.createrank")) {
                if (args.length != 4) {
                    player.sendMessage("Usage: /createrank <name> <color> <prefix> <weight>");
                    return false;
                }
                String rankName = args[0];
                ChatColor rankColor = Colors.getColor(args[1]);
                String rankPrefix = args[2];
                int rankWeight = Integer.parseInt(args[3]);

                Ranks.createRank(rankName, rankColor, rankPrefix, rankWeight);
                player.sendMessage("Rank " + rankName + " created successfully!");
            } else {
                player.sendMessage("You do not have permission to use this command.");
            }
        }
        return true;
    }
}
