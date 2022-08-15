package de.tobias.mcstat.util;

import de.tobias.mcstat.data.UserDB;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.advancement.Advancement;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WorldCache {

    public long lastFlush = System.currentTimeMillis();

    public World world;
    public HashMap<String, Double> travel = new HashMap<>();
    public HashMap<Material, Integer> placedBlocks = new HashMap<>();
    public HashMap<Material, Integer> brokenBlocks = new HashMap<>();
    public HashMap<EntityType, Integer> killedEntities = new HashMap<>();
    public HashMap<EntityType, Double> entityDamage = new HashMap<>();
    public HashMap<EntityType, Double> entityDamageTaken = new HashMap<>();
    public ArrayList<Integer> foodLevels = new ArrayList<>();
    public HashMap<Material, Integer> eatenItems = new HashMap<>();
    public HashMap<Material, Integer> craftedItems = new HashMap<>();
    public HashMap<Material, Integer> damagedItems = new HashMap<>();
    public HashMap<Material, Integer> brokenItems = new HashMap<>();
    public ArrayList<Advancement> advancementsDone = new ArrayList<>();

    public WorldCache(World w) {
        this.world = w;
    }

    public void addTravel(String type, Double distance) {
        type = type.toUpperCase();
        if(!travel.containsKey(type)) travel.put(type, 0.0);
        travel.replace(type, travel.get(type) + distance);
    }

    public void addBuild(String type, Block b) {
        type = type.toUpperCase();

        HashMap<Material, Integer> map = null;
        if(type.equalsIgnoreCase("PLACE")) {
            map = placedBlocks;
        } else if(type.equalsIgnoreCase("BREAK")) {
            map = brokenBlocks;
        } else {
            Logger.warn("Unknown Build Type: " + type);
            return;
        }

        if(!map.containsKey(b.getType())) map.put(b.getType(), 0);
        map.replace(b.getType(), map.get(b.getType()) + 1);
    }

    public void killMob(EntityType type) {
        if(!killedEntities.containsKey(type)) killedEntities.put(type, 0);
        killedEntities.replace(type, killedEntities.get(type) + 1);
    }

    public void damageEntity(EntityType type, Double damage, Material itemType) {
        if(!entityDamage.containsKey(type)) entityDamage.put(type, 0.0);
        entityDamage.replace(type, entityDamage.get(type) + damage);
    }

    public void damagedByEntity(EntityType type, Double damage) {
        if(!entityDamageTaken.containsKey(type)) entityDamageTaken.put(type, 0.0);
        entityDamageTaken.replace(type, entityDamageTaken.get(type) + damage);
    }

    public void foodLevelChange(Integer newLevel, Integer oldLevel) {
        if(newLevel > 20) newLevel = 20;
        if(oldLevel > 20) oldLevel = 20;
        foodLevels.add(newLevel - oldLevel);
    }

    public void eaten(Material item) {
        if(!eatenItems.containsKey(item)) eatenItems.put(item, 0);
        eatenItems.replace(item, eatenItems.get(item) + 1);
    }

    public void crafted(Material material) {
        if(!craftedItems.containsKey(material)) craftedItems.put(material, 0);
        craftedItems.replace(material, craftedItems.get(material) + 1);
    }

    public void damageItem(Material material, Integer damage) {
        if(!damagedItems.containsKey(material)) damagedItems.put(material, 0);
        damagedItems.replace(material, damagedItems.get(material) + damage);
    }

    public void breakItem(Material material) {
        if(!brokenItems.containsKey(material)) brokenItems.put(material, 0);
        brokenItems.replace(material, brokenItems.get(material) + 1);
    }

    public void advancementDone(Advancement a) {
        advancementsDone.add(a);
    }

    public Boolean flush(UserDB db) {
        for(Map.Entry<String, Double> travelEntry : travel.entrySet()) {
            db.execute("INSERT INTO `travel` (`TIME`, `DISTANCE`, `TYPE`, `WORLD`) VALUES (" + System.currentTimeMillis() + ", '" + travelEntry.getValue() + "', '" + travelEntry.getKey() + "', '" + world.getName() + "');", true);
        }

        for(Map.Entry<Material, Integer> placedBlockEntry : placedBlocks.entrySet()) {
            db.execute("INSERT INTO `build` (`TIME`, `COUNT`, `BLOCKTYPE`, `TYPE`, `WORLD`) VALUES (" + System.currentTimeMillis() + ", '" + placedBlockEntry.getValue() + "', '" + placedBlockEntry.getKey().name() + "', 'PLACE', '" + world.getName() + "');", true);
        }

        for(Map.Entry<Material, Integer> breakBlockEntry : brokenBlocks.entrySet()) {
            db.execute("INSERT INTO `build` (`TIME`, `COUNT`, `BLOCKTYPE`, `TYPE`, `WORLD`) VALUES (" + System.currentTimeMillis() + ", '" + breakBlockEntry.getValue() + "', '" + breakBlockEntry.getKey().name() + "', 'BREAK', '" + world.getName() + "');", true);
        }

        for(Map.Entry<EntityType, Integer> killEntityEntry : killedEntities.entrySet()) {
            db.execute("INSERT INTO `entityKills` (`TIME`, `COUNT`, `ENTITYTYPE`, `WORLD`) VALUES (" + System.currentTimeMillis() + ", " + killEntityEntry.getValue() + ", '" + killEntityEntry.getKey().name() + "', '" + world.getName() + "');", true);
        }

        for(Map.Entry<EntityType, Double> damageEntityEntry : entityDamage.entrySet()) {
            db.execute("INSERT INTO `entityDamage` (`TIME`, `DAMAGE`, `ENTITYTYPE`, `WORLD`) VALUES (" + System.currentTimeMillis() + ", " + damageEntityEntry.getValue() + ", '" + damageEntityEntry.getKey().name() + "', '" + world.getName() + "');", true);
        }

        for(Map.Entry<EntityType, Double> damagedByEntityEntry : entityDamageTaken.entrySet()) {
            db.execute("INSERT INTO `entityDamageTaken` (`TIME`, `DAMAGE`, `ENTITYTYPE`, `WORLD`) VALUES (" + System.currentTimeMillis() + ", " + damagedByEntityEntry.getValue() + ", '" + damagedByEntityEntry.getKey().name() + "', '" + world.getName() + "');", true);
        }

        for(Map.Entry<Material, Integer> eatenEntry : eatenItems.entrySet()) {
            db.execute("INSERT INTO `foodEaten` (`TIME`, `COUNT`, `ITEMTYPE`, `WORLD`) VALUES (" + System.currentTimeMillis() + ", " + eatenEntry.getValue() + ", '" + eatenEntry.getKey().name() + "', '" + world.getName() + "');", true);
        }

        for(Map.Entry<Material, Integer> craftedEntry : craftedItems.entrySet()) {
            db.execute("INSERT INTO `craftedItems` (`TIME`, `COUNT`, `ITEMTYPE`, `WORLD`) VALUES (" + System.currentTimeMillis() + ", " + craftedEntry.getValue() + ", '" + craftedEntry.getKey().name() + "', '" + world.getName() + "');", true);
        }

        for(Map.Entry<Material, Integer> brokenEntry : brokenItems.entrySet()) {
            db.execute("INSERT INTO `brokenItems` (`TIME`, `COUNT`, `ITEMTYPE`, `WORLD`) VALUES (" + System.currentTimeMillis() + ", " + brokenEntry.getValue() + ", '" + brokenEntry.getKey().name() + "', '" + world.getName() + "');", true);
        }

        for(Map.Entry<Material, Integer> damagedEntry : damagedItems.entrySet()) {
            db.execute("INSERT INTO `damagedItems` (`TIME`, `DAMAGE`, `ITEMTYPE`, `WORLD`) VALUES (" + System.currentTimeMillis() + ", " + damagedEntry.getValue() + ", '" + damagedEntry.getKey().name() + "', '" + world.getName() + "');", true);
        }

        for(Advancement advancementEntry : advancementsDone) {
            db.execute("INSERT INTO `advancementsDone` (`TIME`, `NAME`, `CRITERIA`, `WORLD`) VALUES (" + System.currentTimeMillis() + ", '" + advancementEntry.getDisplay().getTitle() + "', '" + String.join(",",advancementEntry.getCriteria()) + "', '" + world.getName() + "');", true);
        }

        Integer totalFoodLost = 0;
        Integer totalFoodAdded = 0;
        for(int foodDiff : foodLevels) {
            if(foodDiff > 0) totalFoodAdded += foodDiff;
            else totalFoodLost -= foodDiff;
        }

        if(totalFoodLost != 0 || totalFoodAdded != 0) {
            db.execute("INSERT INTO `foodLevel` (`TIME`, `VALUE`, `ADDED`, `REMOVED`, `WORLD`) VALUES (" + System.currentTimeMillis() + ", " + (totalFoodAdded - totalFoodLost) + ", " + totalFoodAdded + ", " + totalFoodLost + ", '" + world.getName() + "');", true);
        }

        Boolean result = db.flushExecuteBatches();

        long playedTime = System.currentTimeMillis() - lastFlush;
        db.execute("INSERT INTO `playTime` (`TIME`, `VALUE`, `WORLD`) VALUES (" + System.currentTimeMillis() + ", " + playedTime + ", '" + world.getName() + "')", false);
        lastFlush = System.currentTimeMillis();

        travel.clear();
        placedBlocks.clear();
        brokenBlocks.clear();
        killedEntities.clear();
        entityDamage.clear();
        entityDamageTaken.clear();
        foodLevels.clear();
        eatenItems.clear();
        craftedItems.clear();
        brokenItems.clear();
        damagedItems.clear();
        advancementsDone.clear();
        return result;
    }


}
