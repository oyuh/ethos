package law.ethos.commands;

import law.ethos.methods.Ranks;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class ListCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("ethos.list")) {
                List<Player> players = Bukkit.getOnlinePlayers().stream()
                        .sorted((p1, p2) -> {
                            Ranks.Rank rank1 = Ranks.getPlayerRank(p1.getName());
                            Ranks.Rank rank2 = Ranks.getPlayerRank(p2.getName());
                            if (rank1 == null) return 1;
                            if (rank2 == null) return -1;
                            return Integer.compare(rank2.getWeight(), rank1.getWeight());
                        })
                        .collect(Collectors.toList());

                player.sendMessage("Online Players:");
                for (Player p : players) {
                    Ranks.Rank rank = Ranks.getPlayerRank(p.getName());
                    String displayName = rank != null ? rank.getColor() + p.getName() + org.bukkit.ChatColor.RESET : p.getName();
                    player.sendMessage(displayName);
                }
            } else {
                player.sendMessage("You do not have permission to use this command.");
            }
        }
        return true;
    }
}
