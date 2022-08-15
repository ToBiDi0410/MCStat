package de.tobias.mcstat.data;

import de.tobias.mcstat.util.PlayerCache;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;

public class ItemDamageCollector implements Listener {

    @EventHandler
    public void onDamage(PlayerItemDamageEvent e) {
        PlayerCache.getCache(e.getPlayer().getUniqueId()).getWorldCache(e.getPlayer().getWorld()).damageItem(e.getItem().getType(), e.getDamage());
    }

    @EventHandler
    public void onDestroy(PlayerItemBreakEvent e) {
        PlayerCache.getCache(e.getPlayer().getUniqueId()).getWorldCache(e.getPlayer().getWorld()).breakItem(e.getBrokenItem().getType());
    }
}
