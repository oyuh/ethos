package law.ethos.commands;

import law.ethos.Ethos;
import law.ethos.methods.Ranks;
import law.ethos.util.ChatUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class EditRankCommand implements CommandExecutor {

    private final Ethos plugin = Ethos.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission(plugin.getPermission("editrank"))) {
            sender.sendMessage(ChatUtil.colorize(plugin.getMessage("messages.no_permission")));
            return true;
        }

        if (args.length != 4) {
            sender.sendMessage(ChatUtil.colorize(plugin.getMessage("messages.usage.editrank")));
            return false;
        }

        String rankName = args[0];
        String color = args[1];
        String prefix = args[2];
        int weight;

        try {
            weight = Integer.parseInt(args[3]);
        } catch (NumberFormatException e) {
            sender.sendMessage(ChatUtil.colorize(plugin.getMessage("messages.invalid_number")));
            return false;
        }

        if (Ranks.editRank(rankName, ChatColor.valueOf(color), prefix, weight)) {
            sender.sendMessage(ChatUtil.colorize(plugin.getMessage("messages.editrank.success").replace("{rank}", rankName)));
        } else {
            sender.sendMessage(ChatUtil.colorize(plugin.getMessage("messages.editrank.rank_not_found")));
        }
        return true;
    }
}
