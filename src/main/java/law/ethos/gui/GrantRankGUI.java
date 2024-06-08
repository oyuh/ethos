package law.ethos.gui;

import law.ethos.methods.Ranks;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class GrantRankGUI {

    private static final int INVENTORY_SIZE = 27;
    private static final String INVENTORY_NAME = "Grant Rank";

    public static void openGrantRankGUI(Player player, Player targetPlayer) {
        Inventory inv = Bukkit.createInventory(null, INVENTORY_SIZE, INVENTORY_NAME);

        List<Ranks.Rank> ranks = Ranks.getAllRanks();
        for (int i = 0; i < ranks.size() && i < INVENTORY_SIZE; i++) {
            Ranks.Rank rank = ranks.get(i);
            ItemStack item = new ItemStack(Material.NAME_TAG);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(rank.getColor() + rank.getName());
            meta.setLore(List.of(
                    ChatColor.GRAY + "Prefix: " + rank.getPrefix(),
                    ChatColor.GRAY + "Weight: " + rank.getWeight()
            ));
            item.setItemMeta(meta);
            inv.setItem(i, item);
        }

        player.openInventory(inv);
    }

    public static void handleInventoryClick(InventoryClickEvent event) {
        if (!event.getView().getTitle().equals(INVENTORY_NAME)) return;
        event.setCancelled(true);

        Player player = (Player) event.getWhoClicked();
        ItemStack item = event.getCurrentItem();
        if (item == null || !item.hasItemMeta()) return;

        String rankName = ChatColor.stripColor(item.getItemMeta().getDisplayName());
        Ranks.Rank rank = Ranks.getRank(rankName);
        if (rank != null) {
            Player targetPlayer = (Player) player.getMetadata("targetPlayer").get(0).value();
            Ranks.grantRank(targetPlayer, rankName);
            player.sendMessage("Granted rank " + rank.getName() + " to " + targetPlayer.getName());
            player.closeInventory();
        }
    }
}
