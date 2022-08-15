package de.tobias.mcstat.util;

import de.tobias.mcstat.data.UserDB;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.HashMap;
import java.util.UUID;

public class PlayerCache {

    public static HashMap<UUID, PlayerCache> caches = new HashMap<>();

    public static PlayerCache getCache(UUID uuid) {
        if(caches.containsKey(uuid)) return caches.get(uuid);
        PlayerCache c =  new PlayerCache(uuid);
        caches.put(uuid, c);
        return c;
    }

    UUID uuid;
    HashMap<World, WorldCache> worldCaches = new HashMap<>();
    UserDB database;

    public PlayerCache(UUID uuid) {
        this.uuid = uuid;
        this.database = UserDB.getDB(uuid);
    }

    public WorldCache getWorldCache(World w) {
        if(worldCaches.containsKey(w)) return worldCaches.get(w);
        WorldCache wc = new WorldCache(w);
        worldCaches.put(w, wc);
        return wc;
    }

    public void flush() {
        for(WorldCache w : worldCaches.values()) {
            w.flush(database);
        }
    }
}
