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

public class InfoGUI {

    private static final int INVENTORY_SIZE = 9;
    private static final String INVENTORY_NAME = "Info";

    public static void openPlayerInfo(Player player, String name, String uuid, String lastLocation, String lastLogin) {
        Inventory inv = Bukkit.createInventory(null, INVENTORY_SIZE, INVENTORY_NAME);

        inv.setItem(0, createItem(Material.PAPER, "Name", List.of(ChatColor.GRAY + name)));
        inv.setItem(1, createItem(Material.PAPER, "UUID", List.of(ChatColor.GRAY + uuid)));
        inv.setItem(2, createItem(Material.PAPER, "Last Location", List.of(ChatColor.GRAY + lastLocation)));
        inv.setItem(3, createItem(Material.PAPER, "Last Login", List.of(ChatColor.GRAY + lastLogin)));

        player.openInventory(inv);
    }

    public static void openRankInfo(Player player, Ranks.Rank rank) {
        Inventory inv = Bukkit.createInventory(null, INVENTORY_SIZE, INVENTORY_NAME);

        inv.setItem(0, createItem(Material.PAPER, "Name", List.of(ChatColor.GRAY + rank.getName())));
        inv.setItem(1, createItem(Material.PAPER, "Color", List.of(ChatColor.GRAY + rank.getColor().toString())));
        inv.setItem(2, createItem(Material.PAPER, "Prefix", List.of(ChatColor.GRAY + rank.getPrefix())));
        inv.setItem(3, createItem(Material.PAPER, "Weight", List.of(ChatColor.GRAY + Integer.toString(rank.getWeight()))));

        player.openInventory(inv);
    }

    private static ItemStack createItem(Material material, String name, List<String> lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.YELLOW + name);
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public static void handleInventoryClick(InventoryClickEvent event) {
        if (!event.getView().getTitle().equals(INVENTORY_NAME)) return;
        event.setCancelled(true);
    }
}
