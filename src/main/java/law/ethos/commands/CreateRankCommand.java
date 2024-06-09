package law.ethos.commands;

import law.ethos.Ethos;
import law.ethos.methods.Ranks;
import law.ethos.util.ChatUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CreateRankCommand implements CommandExecutor {

    private final Ethos plugin = Ethos.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission(plugin.getPermission("createrank"))) {
            sender.sendMessage(ChatUtil.colorize(plugin.getMessage("messages.no_permission")));
            return true;
        }

        if (args.length != 4) {
            sender.sendMessage(ChatUtil.colorize(plugin.getMessage("messages.usage.createrank")));
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

        boolean created = Ranks.createRank(rankName, ChatColor.valueOf(color), prefix, weight);
        if (created) {
            sender.sendMessage(ChatUtil.colorize(plugin.getMessage("messages.createrank.success").replace("{rank}", rankName)));
        } else {
            sender.sendMessage(ChatUtil.colorize(plugin.getMessage("messages.createrank.rank_exists")));
        }

        return true;
    }
}
