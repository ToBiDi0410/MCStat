package de.tobias.mcstat.util;

import org.bukkit.event.inventory.InventoryAction;

public class Helpers {

    public static boolean actionIsInsertion(InventoryAction act) {
        return act == InventoryAction.PLACE_ALL ||
                act == InventoryAction.PLACE_ONE ||
                act == InventoryAction.PLACE_SOME ||
                act == InventoryAction.SWAP_WITH_CURSOR;
    }

    public static boolean actionIsRemoval(InventoryAction act) {
        return act == InventoryAction.PICKUP_ALL ||
                act == InventoryAction.PICKUP_HALF ||
                act == InventoryAction.PICKUP_SOME ||
                act == InventoryAction.PICKUP_ONE ||
                act == InventoryAction.DROP_ALL_CURSOR ||
                act == InventoryAction.DROP_ONE_CURSOR ||
                act == InventoryAction.DROP_ALL_SLOT ||
                act == InventoryAction.DROP_ONE_SLOT;
    }
}
