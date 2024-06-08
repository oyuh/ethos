package law.ethos.commands;

import law.ethos.methods.Ranks;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ListRanksCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("ethos.listranks")) {
                player.sendMessage("Ranks:");
                for (Ranks.Rank rank : Ranks.getAllRanks()) {
                    player.sendMessage(rank.getName() + " (Weight: " + rank.getWeight() + ")");
                }
            } else {
                player.sendMessage("You do not have permission to use this command.");
            }
        }
        return true;
    }
}
