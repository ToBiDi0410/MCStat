package de.tobias.mcstat.data;

import de.tobias.mcstat.util.PlayerCache;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class EntityKillCollector implements Listener {

    @EventHandler
    public void onKill(EntityDeathEvent e) {
        Player p = e.getEntity().getKiller();
        if(p != null) PlayerCache.getCache(p.getUniqueId()).getWorldCache(e.getEntity().getWorld()).killMob(e.getEntity().getType());
    }
}
