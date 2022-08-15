/*package de.tobias.mcstat.data;

import de.tobias.mcstat.util.Inventories;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;

public class SmeltCollector implements Listener {



    @EventHandler
    public void onInsertItem(InventoryClickEvent e) {
        if(e.getInventory().getType() == InventoryType.FURNACE || e.getInventory().getType() == InventoryType.SMOKER || e.getInventory().getType() == InventoryType.BLAST_FURNACE) {
            if(e.getSlotType() == InventoryType.SlotType.CRAFTING) {
                if(Inventories.actionIsInsertion(e.getAction())) {

                }
            }
        }
    }
}
NOT EASILY POSSIBLE DUE TO COOK EVENT NOT SERVING THE PLAYER WHICH INSERTED THE STACK!
*/