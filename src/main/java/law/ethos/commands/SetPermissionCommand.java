package law.ethos.commands;

import law.ethos.Ethos;
import law.ethos.methods.Ranks;
import law.ethos.util.ChatUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class SetPermissionCommand implements CommandExecutor {

    private final Ethos plugin = Ethos.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission(plugin.getPermission("setpermission"))) {
            sender.sendMessage(ChatUtil.colorize(plugin.getMessage("messages.no_permission")));
            return true;
        }

        if (args.length != 2) {
            sender.sendMessage(ChatUtil.colorize(plugin.getMessage("messages.usage.setpermission")));
            return false;
        }

        String rankName = args[0];
        String permission = args[1];

        if (Ranks.getRank(rankName) != null) {
            Ranks.addPermissionToRank(rankName, permission);
            sender.sendMessage(ChatUtil.colorize(plugin.getMessage("messages.setpermission.success").replace("{permission}", permission).replace("{rank}", rankName)));
        } else {
            sender.sendMessage(ChatUtil.colorize(plugin.getMessage("messages.setpermission.rank_not_found")));
        }
        return true;
    }
}
