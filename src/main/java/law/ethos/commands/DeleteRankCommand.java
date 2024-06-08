package law.ethos.commands;

import law.ethos.methods.Ranks;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DeleteRankCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("ethos.deleterank")) {
                if (args.length != 1) {
                    player.sendMessage("Usage: /deleterank <name>");
                    return false;
                }
                String rankName = args[0];

                Ranks.deleteRank(rankName);
                player.sendMessage("Rank " + rankName + " deleted successfully!");
            } else {
                player.sendMessage("You do not have permission to use this command.");
            }
        }
        return true;
    }
}
