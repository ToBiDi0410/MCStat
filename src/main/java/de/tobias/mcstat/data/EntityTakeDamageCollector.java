package de.tobias.mcstat.data;

import de.tobias.mcstat.util.PlayerCache;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class EntityTakeDamageCollector implements Listener {

    @EventHandler
    public void onEntityGetDamageEvent(EntityDamageByEntityEvent e) {
        if(e.getEntity().getType() == EntityType.PLAYER) {
            Object cause = e.getDamager();
            if(cause instanceof Projectile) {
                cause = ((Projectile) cause).getShooter();
            }

            if(!(cause instanceof Entity)) return;
            Entity entityCause = (Entity) cause;

            Player p = (Player) e.getEntity();
            PlayerCache.getCache(p.getUniqueId()).getWorldCache(p.getWorld()).damagedByEntity(entityCause.getType(), e.getDamage());
        }
    }
}
