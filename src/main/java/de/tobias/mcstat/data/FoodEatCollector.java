package de.tobias.mcstat.data;

import de.tobias.mcstat.util.PlayerCache;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;

public class FoodEatCollector implements Listener {

    @EventHandler
    public void onEat(PlayerItemConsumeEvent e) {
        if(e.getItem().getType().isEdible()) {
            PlayerCache.getCache(e.getPlayer().getUniqueId()).getWorldCache(e.getPlayer().getWorld()).eaten(e.getItem().getType());
        }
    }
}
