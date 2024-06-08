package law.ethos.commands;

import law.ethos.methods.Ranks;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetPermissionCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("ethos.setpermission")) {
                if (args.length != 2) {
                    player.sendMessage("Usage: /setpermission <rank> <permission>");
                    return false;
                }
                String rankName = args[0];
                String permission = args[1];

                if (Ranks.getRank(rankName) != null) {
                    Ranks.addPermissionToRank(rankName, permission);
                    player.sendMessage("Added permission " + permission + " to rank " + rankName);
                } else {
                    player.sendMessage("Rank not found.");
                }
            } else {
                player.sendMessage("You do not have permission to use this command.");
            }
        }
        return true;
    }
}
