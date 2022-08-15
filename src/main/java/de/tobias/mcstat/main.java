package de.tobias.mcstat;

import de.tobias.mcstat.data.*;
import de.tobias.mcstat.util.PlayerCache;
import de.tobias.mcstat.server.WebServer;
import de.tobias.mcstat.util.Logger;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class main extends JavaPlugin {

    public static WebServer server = new WebServer(80);
    public static Plugin pl;

    @Override
    public void onEnable() {
        Logger.info("Starting Plugin...");
        pl = this;
        server.start();
        Bukkit.getPluginManager().registerEvents(new TravelCollector(), pl);
        Bukkit.getPluginManager().registerEvents(new BuildCollector(), pl);
        Bukkit.getPluginManager().registerEvents(new EntityKillCollector(), pl);
        Bukkit.getPluginManager().registerEvents(new EntityDamageCollector(), pl);
        Bukkit.getPluginManager().registerEvents(new FoodLevelCollector(), pl);
        Bukkit.getPluginManager().registerEvents(new FoodEatCollector(), pl);
        Bukkit.getPluginManager().registerEvents(new EntityTakeDamageCollector(), pl);
        Bukkit.getPluginManager().registerEvents(new CraftingCollector(), pl);
        Bukkit.getPluginManager().registerEvents(new ItemDamageCollector(), pl);
        Bukkit.getPluginManager().registerEvents(new AdvancementCollector(), pl);

        Bukkit.getScheduler().scheduleSyncRepeatingTask(pl,  () -> {
            for(PlayerCache cache : PlayerCache.caches.values()) {
                cache.flush();
            }
        }, 0L, 20L * 10);
    }

    @Override
    public void onDisable() {
        UserDB.closeAll();
        server.stop();
    }
}
