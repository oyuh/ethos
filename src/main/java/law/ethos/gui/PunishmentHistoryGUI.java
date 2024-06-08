package law.ethos.gui;

import law.ethos.methods.Punishments;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

public class PunishmentHistoryGUI {

    private static final int INVENTORY_SIZE = 27;
    private static final String INVENTORY_NAME = "Punishment History";

    public static void openPunishmentHistory(Player player, List<Punishments.Punishment> punishments) {
        Inventory inv = Bukkit.createInventory(null, INVENTORY_SIZE, INVENTORY_NAME);

        for (int i = 0; i < punishments.size() && i < INVENTORY_SIZE; i++) {
            Punishments.Punishment punishment = punishments.get(i);
            ItemStack item = new ItemStack(getMaterialForPunishmentType(punishment.getType()));
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.YELLOW + punishment.getType().toString());
            meta.setLore(Arrays.asList(
                    ChatColor.GRAY + "Reason: " + punishment.getReason(),
                    ChatColor.GRAY + "Date: " + punishment.getDate().toString(),
                    ChatColor.GRAY + "Duration: " + (punishment.getDuration() == -1 ? "Permanent" : punishment.getDuration() + " seconds")
            ));
            item.setItemMeta(meta);
            inv.setItem(i, item);
        }

        player.openInventory(inv);
    }

    private static Material getMaterialForPunishmentType(Punishments.PunishmentType type) {
        switch (type) {
            case BAN:
                return Material.RED_WOOL;
            case MUTE:
                return Material.YELLOW_WOOL;
            case KICK:
                return Material.ORANGE_WOOL;
            default:
                return Material.WHITE_WOOL;
        }
    }

    public static void handleInventoryClick(InventoryClickEvent event) {
        if (!event.getView().getTitle().equals(INVENTORY_NAME)) return;
        event.setCancelled(true);
    }
}
