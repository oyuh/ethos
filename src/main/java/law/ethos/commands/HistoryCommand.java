package law.ethos.commands;

import law.ethos.gui.PunishmentHistoryGUI;
import law.ethos.methods.Punishments;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class HistoryCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player) && args.length != 1) {
            sender.sendMessage("Usage: /history <player>");
            return false;
        }

        Player targetPlayer = args.length == 1 ? Bukkit.getPlayer(args[0]) : (Player) sender;
        if (targetPlayer == null) {
            sender.sendMessage("Player not found.");
            return false;
        }

        List<Punishments.Punishment> punishments = Punishments.getPunishments(targetPlayer.getUniqueId());
        if (sender instanceof Player) {
            PunishmentHistoryGUI.openPunishmentHistory((Player) sender, punishments);
        } else {
            // Handle console output for punishment history
            sender.sendMessage("Punishment history for " + targetPlayer.getName() + ":");
            for (Punishments.Punishment punishment : punishments) {
                sender.sendMessage(punishment.getType() + ": " + punishment.getReason() + " (" + (punishment.getDuration() == -1 ? "Permanent" : punishment.getDuration() + " seconds") + ")");
            }
        }
        return true;
    }
}
