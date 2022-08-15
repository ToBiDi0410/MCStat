package de.tobias.mcstat.data;

import de.tobias.mcstat.util.PlayerCache;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.sql.ResultSet;
import java.util.*;

public class TravelCollector implements Listener {

    public static HashMap<UUID, HashMap<World, HashMap<String, Double>>> movementData = new HashMap<>();

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        if(!movementData.containsKey(p.getUniqueId())) {
            movementData.put(p.getUniqueId(), new HashMap<>());
        }

        if(!movementData.get(p.getUniqueId()).containsKey(p.getWorld())) {
            movementData.get(p.getUniqueId()).put(p.getWorld(), new HashMap<>());
        }

        String type = "WALK";
        Double distance = e.getTo().distance(e.getFrom());
        HashMap<String, Double> data = movementData.get(p.getUniqueId()).get(p.getWorld());
        if(p.isFlying()) type = "FLY";
        if(p.isSwimming()) type = "CROUCH";
        if(p.isInWater()) type = "SWIM_SLOW";
        if(p.isSwimming() && p.isInWater()) type = "SWIM_FAST";
        if(p.isInsideVehicle()) type = "VEHICLE_" + p.getVehicle().getType();
        if(p.isGliding()) type = "ELTRYA";

        if(p.isSprinting()) type += "_SPRINT";
        if(p.isSneaking()) type += "_SNEAK";

        if(p.getVelocity().getY() < -0.5) type = "FALL";
        PlayerCache.getCache(p.getUniqueId()).getWorldCache(p.getWorld()).addTravel(type, distance);
    }

    public static HashMap<String, HashMap<String, Double>> getMovementsForAllWorlds(UUID uuid, Long maxAgeSeconds) {
        HashMap<String, HashMap<String, Double>> values = new HashMap<>();

        for(World w : Bukkit.getWorlds())
            values.put(w.getName(), getMovementsForWorld(uuid, w, maxAgeSeconds));

        return values;
    }

    public static HashMap<String, Double> getMovementsForWorld(UUID uuid, World w, Long maxAgeSeconds) {
        HashMap<String, Double> values = new HashMap<>();

        try {
            UserDB db = UserDB.getDB(uuid);
            ResultSet results = db.query("SELECT * FROM 'travel' WHERE TIME >= " + (System.currentTimeMillis() - (maxAgeSeconds * 1000)) + ";");
            while(results.next()) {
                String type = results.getString("TYPE");
                Double distance = results.getDouble("DISTANCE");
                String world = results.getString("WORLD");

                if(world.equalsIgnoreCase(w.getName().toLowerCase())) {
                    if(values.containsKey(type)) {
                        distance += values.get(type);
                        values.remove(type);
                    }

                    values.put(type, distance);
                }
            }
            results.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return values;
    }
}
