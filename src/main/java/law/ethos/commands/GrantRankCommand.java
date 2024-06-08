package law.ethos.commands;

import law.ethos.Ethos;
import law.ethos.gui.GrantRankGUI;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

public class GrantRankCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be executed by a player.");
            return false;
        }

        Player player = (Player) sender;
        if (!player.hasPermission("ethos.grantrank")) {
            player.sendMessage("You do not have permission to use this command.");
            return true;
        }

        if (args.length != 1) {
            player.sendMessage("Usage: /grantrank <player>");
            return false;
        }

        Player targetPlayer = Bukkit.getPlayer(args[0]);
        if (targetPlayer == null) {
            player.sendMessage("Player not found.");
            return false;
        }

        player.setMetadata("targetPlayer", new FixedMetadataValue(Ethos.getInstance(), targetPlayer));
        GrantRankGUI.openGrantRankGUI(player, targetPlayer);
        return true;
    }
}
