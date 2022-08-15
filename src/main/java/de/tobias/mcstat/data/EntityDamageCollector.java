package de.tobias.mcstat.data;

import de.tobias.mcstat.util.PlayerCache;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class EntityDamageCollector implements Listener {

    @EventHandler
    public void onHit(EntityDamageByEntityEvent e) {
        Player p = null;
        Material item;
        if(e.getDamager().getType() == EntityType.PLAYER) {
            p = (Player) e.getDamager();
        }

        if(e.getDamager() instanceof Projectile) {
            Projectile projectile = (Projectile) e.getDamager();
            if(projectile.getShooter() instanceof Player) {
                p = (Player) projectile.getShooter();
            }
        }

        if(p != null) {
            item = p.getInventory().getItemInMainHand().getType();
            PlayerCache.getCache(p.getUniqueId()).getWorldCache(p.getWorld()).damageEntity(e.getEntity().getType(), e.getDamage(), item);
        }
    }
}
