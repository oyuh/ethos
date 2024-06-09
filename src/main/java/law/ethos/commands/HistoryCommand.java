package law.ethos.commands;

import law.ethos.Ethos;
import law.ethos.gui.PunishmentHistoryGUI;
import law.ethos.methods.Punishments;
import law.ethos.util.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class HistoryCommand implements CommandExecutor {

    private final Ethos plugin = Ethos.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission(plugin.getPermission("history"))) {
            sender.sendMessage(ChatUtil.colorize(plugin.getMessage("messages.no_permission")));
            return true;
        }

        if (args.length != 1) {
            sender.sendMessage(ChatUtil.colorize(plugin.getMessage("messages.usage.history")));
            return false;
        }

        Player targetPlayer = Bukkit.getPlayer(args[0]);
        if (targetPlayer == null) {
            sender.sendMessage(ChatUtil.colorize(plugin.getMessage("messages.player_not_found")));
            return false;
        }

        List<Punishments.Punishment> punishments = Punishments.getPunishments(targetPlayer.getUniqueId());
        PunishmentHistoryGUI.openPunishmentHistory((Player) sender, punishments);
        return true;
    }
}
