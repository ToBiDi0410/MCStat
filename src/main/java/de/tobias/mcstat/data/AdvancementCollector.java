package de.tobias.mcstat.data;

import de.tobias.mcstat.util.PlayerCache;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;

public class AdvancementCollector implements Listener {

    @EventHandler
    public void onAdvance(PlayerAdvancementDoneEvent e) {
        PlayerCache.getCache(e.getPlayer().getUniqueId()).getWorldCache(e.getPlayer().getWorld()).advancementDone(e.getAdvancement());
    }
}
