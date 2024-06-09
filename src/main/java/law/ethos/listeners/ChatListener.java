package law.ethos.listeners;

import law.ethos.methods.Ranks;
import law.ethos.methods.Ranks.Rank;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        Rank rank = Ranks.getPlayerRank(player.getUniqueId());

        if (rank != null) {
            String prefix = ChatColor.translateAlternateColorCodes('&', rank.getPrefix());
            ChatColor color = rank.getColor();
            event.setFormat(prefix + color + player.getName() + ChatColor.RESET + ": " + event.getMessage());
        } else {
            event.setFormat(player.getName() + ": " + event.getMessage());
        }

        // Debug message
        System.out.println("Player: " + player.getName() + ", Rank: " + (rank != null ? rank.getName() : "None"));
    }
}
