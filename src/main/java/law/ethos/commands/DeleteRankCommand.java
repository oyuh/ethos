package law.ethos.commands;

import law.ethos.Ethos;
import law.ethos.methods.Ranks;
import law.ethos.util.ChatUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class DeleteRankCommand implements CommandExecutor {

    private final Ethos plugin = Ethos.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission(plugin.getPermission("deleterank"))) {
            sender.sendMessage(ChatUtil.colorize(plugin.getMessage("messages.no_permission")));
            return true;
        }

        if (args.length != 1) {
            sender.sendMessage(ChatUtil.colorize(plugin.getMessage("messages.usage.deleterank")));
            return false;
        }

        String rankName = args[0];

        if (Ranks.deleteRank(rankName)) {
            sender.sendMessage(ChatUtil.colorize(plugin.getMessage("messages.deleterank.success").replace("{rank}", rankName)));
        } else {
            sender.sendMessage(ChatUtil.colorize(plugin.getMessage("messages.deleterank.rank_not_found")));
        }
        return true;
    }
}
