package law.ethos.commands;

import law.ethos.Ethos;
import law.ethos.gui.GrantRankGUI;
import law.ethos.util.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

public class GrantRankCommand implements CommandExecutor {
    private final Ethos plugin = Ethos.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatUtil.colorize(plugin.getMessage("messages.no_permission")));
            return true;
        }

        Player player = (Player) sender;
        if (!player.hasPermission(plugin.getPermission("grantrank"))) {
            sender.sendMessage(ChatUtil.colorize(plugin.getMessage("messages.no_permission")));
            return true;
        }

        if (args.length != 1) {
            sender.sendMessage(ChatUtil.colorize(plugin.getMessage("messages.usage.grantrank")));
            return false;
        }

        Player targetPlayer = Bukkit.getPlayer(args[0]);
        if (targetPlayer == null) {
            sender.sendMessage(ChatUtil.colorize(plugin.getMessage("messages.player_not_found")));
            return false;
        }

        player.setMetadata("targetPlayer", new FixedMetadataValue(plugin, targetPlayer));
        GrantRankGUI.openGrantRankGUI(player, targetPlayer);
        return true;
    }
}
