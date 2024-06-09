package law.ethos.gui;

import law.ethos.Ethos;
import law.ethos.methods.Ranks;
import law.ethos.methods.Ranks.Rank;
import law.ethos.util.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GrantRankGUI {

    private static final int INVENTORY_SIZE = 27;
    private static final String INVENTORY_NAME = "Grant Rank";

    public static void openGrantRankGUI(Player player, Player targetPlayer) {
        Inventory inv = Bukkit.createInventory(null, INVENTORY_SIZE, INVENTORY_NAME);

        List<Rank> ranks = new ArrayList<>((Collection) Ranks.getAllRanks());
        for (int i = 0; i < ranks.size() && i < INVENTORY_SIZE; i++) {
            Rank rank = ranks.get(i);
            ItemStack item = new ItemStack(org.bukkit.Material.NAME_TAG);
            ItemMeta meta = item.getItemMeta();

            if (meta != null) {
                meta.setDisplayName(rank.getColor() + rank.getName());
                List<String> lore = new ArrayList<>();
                lore.add(ChatColor.GRAY + "Prefix: " + rank.getPrefix());
                lore.add(ChatColor.GRAY + "Weight: " + rank.getWeight());
                meta.setLore(lore);
                item.setItemMeta(meta);
            }

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
        Rank rank = Ranks.getRank(rankName);

        if (rank != null) {
            Player targetPlayer = (Player) player.getMetadata("targetPlayer").get(0).value();
            Ranks.grantRank(targetPlayer.getUniqueId(), rankName);
            player.sendMessage(ChatUtil.colorize(Ethos.getInstance().getMessage("messages.grantrank.success")
                    .replace("{player}", targetPlayer.getName())
                    .replace("{rank}", rankName)));
            player.closeInventory();
        }
    }
}
