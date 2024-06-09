package law.ethos.commands;

import law.ethos.Ethos;
import law.ethos.methods.Ranks;
import law.ethos.methods.Ranks.Rank;
import law.ethos.util.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

public class ListCommand implements CommandExecutor {

    private final Ethos plugin = Ethos.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission(plugin.getPermission("list"))) {
            sender.sendMessage(ChatUtil.colorize(plugin.getMessage("messages.no_permission")));
            return true;
        }

        Map<UUID, Rank> playerRanks = new HashMap<>();
        for (Player player : Bukkit.getOnlinePlayers()) {
            Rank rank = Ranks.getPlayerRank(player.getUniqueId());
            playerRanks.put(player.getUniqueId(), rank);
        }

        List<Map.Entry<UUID, Rank>> sortedPlayers = new ArrayList<>(playerRanks.entrySet());
        sortedPlayers.sort((a, b) -> {
            Rank rankA = a.getValue();
            Rank rankB = b.getValue();
            return Integer.compare(rankB.getWeight(), rankA != null ? rankA.getWeight() : 0);
        });

        sender.sendMessage(ChatUtil.colorize(plugin.getMessage("messages.list.header")));
        for (Map.Entry<UUID, Rank> entry : sortedPlayers) {
            Player player = Bukkit.getPlayer(entry.getKey());
            Rank rank = entry.getValue();
            if (player != null && rank != null) {
                sender.sendMessage(ChatUtil.colorize(rank.getColor() + player.getName() + " - " + rank.getName()));
            } else if (player != null) {
                sender.sendMessage(ChatUtil.colorize(player.getName() + " - " + ChatColor.GRAY + "No Rank"));
            }
        }

        return true;
    }
}
