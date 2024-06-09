package law.ethos.commands;

import law.ethos.Ethos;
import law.ethos.util.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TPCommand implements CommandExecutor {

    private final Ethos plugin = Ethos.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission(plugin.getPermission("tp"))) {
            sender.sendMessage(ChatUtil.colorize(plugin.getMessage("messages.no_permission")));
            return true;
        }

        if (args.length != 2) {
            sender.sendMessage(ChatUtil.colorize(plugin.getMessage("messages.usage.tp")));
            return false;
        }

        Player player = Bukkit.getPlayer(args[0]);
        Player target = Bukkit.getPlayer(args[1]);

        if (player == null || target == null) {
            sender.sendMessage(ChatUtil.colorize(plugin.getMessage("messages.player_not_found")));
            return true;
        }

        player.teleport(target);
        sender.sendMessage(ChatUtil.colorize(plugin.getMessage("messages.tp.success").replace("{player}", player.getName()).replace("{target}", target.getName())));
        return true;
    }
}
