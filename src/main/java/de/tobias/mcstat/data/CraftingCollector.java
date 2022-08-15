package de.tobias.mcstat.data;

import de.tobias.mcstat.util.PlayerCache;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;

public class CraftingCollector implements Listener {

    @EventHandler
    public void onCraft(CraftItemEvent e) {
        Player p = (Player) e.getView().getPlayer();
        if(p != null) PlayerCache.getCache(p.getUniqueId()).getWorldCache(p.getWorld()).crafted(e.getRecipe().getResult().getType());
    }
}
