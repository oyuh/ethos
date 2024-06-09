package law.ethos.commands;

import law.ethos.Ethos;
import law.ethos.methods.Ranks;
import law.ethos.util.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class InfoCommand implements CommandExecutor {

    private final Ethos plugin = Ethos.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission(plugin.getPermission("info"))) {
            sender.sendMessage(ChatUtil.colorize(plugin.getMessage("messages.no_permission")));
            return true;
        }

        if (args.length != 1) {
            sender.sendMessage(ChatUtil.colorize(plugin.getMessage("messages.usage.info")));
            return false;
        }

        String target = args[0];
        OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(target);

        if (targetPlayer.hasPlayedBefore() || targetPlayer.isOnline()) {
            String playerInfo = String.format(
                    "%s\nName: %s\nUUID: %s\nLocation: %s\nLast Login: %s",
                    ChatUtil.colorize("&aPlayer Info:"),
                    targetPlayer.getName(),
                    targetPlayer.getUniqueId().toString(),
                    targetPlayer.isOnline() ? ((Player) targetPlayer).getLocation().toString() : "N/A",
                    targetPlayer.getLastPlayed()
            );
            sender.sendMessage(ChatUtil.colorize(playerInfo));
        } else {
            sender.sendMessage(ChatUtil.colorize(plugin.getMessage("messages.player_not_found")));
        }
        return true;
    }
}
