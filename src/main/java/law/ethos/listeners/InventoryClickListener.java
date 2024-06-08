package law.ethos.listeners;

import law.ethos.gui.GrantRankGUI;
import law.ethos.gui.InfoGUI;
import law.ethos.gui.PunishmentHistoryGUI;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryClickListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        InfoGUI.handleInventoryClick(event);
        PunishmentHistoryGUI.handleInventoryClick(event);
        GrantRankGUI.handleInventoryClick(event);
    }
}
