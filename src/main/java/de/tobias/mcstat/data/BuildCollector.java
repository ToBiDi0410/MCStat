package de.tobias.mcstat.data;

import de.tobias.mcstat.util.PlayerCache;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class BuildCollector implements Listener {

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        PlayerCache.getCache(e.getPlayer().getUniqueId()).getWorldCache(e.getBlockPlaced().getWorld()).addBuild("PLACE", e.getBlockPlaced());
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        PlayerCache.getCache(e.getPlayer().getUniqueId()).getWorldCache(e.getBlock().getWorld()).addBuild("BREAK", e.getBlock());
    }
}
