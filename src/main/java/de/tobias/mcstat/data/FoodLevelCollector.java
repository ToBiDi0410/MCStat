package de.tobias.mcstat.data;

import de.tobias.mcstat.util.PlayerCache;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class FoodLevelCollector implements Listener {

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent e) {
        if(e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            PlayerCache.getCache(p.getUniqueId()).getWorldCache(p.getWorld()).foodLevelChange(e.getFoodLevel(), p.getFoodLevel());
        }
    }
}
