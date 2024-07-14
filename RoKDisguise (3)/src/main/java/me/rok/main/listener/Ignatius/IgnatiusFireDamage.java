/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.Particle
 *  org.bukkit.World
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.EntityDamageEvent
 *  org.bukkit.event.entity.EntityDamageEvent$DamageCause
 */
package me.rok.main.listener.Ignatius;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class IgnatiusFireDamage
implements Listener {
    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player p2 = (Player)event.getEntity();
            Player player = (Player)event.getEntity();
            if (player.getScoreboardTags().contains("Ignatius") && (event.getCause() == EntityDamageEvent.DamageCause.FIRE || event.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK || event.getCause() == EntityDamageEvent.DamageCause.LAVA)) {
                double radius = 0.3;
                int particleCount = 8;
                Location playerLocation = p2.getLocation();
                World world = playerLocation.getWorld();
                int i2 = 0;
                while (i2 < particleCount) {
                    double angle = Math.PI * 2 * (double)i2 / (double)particleCount;
                    double xOffset = radius * Math.cos(angle);
                    double zOffset = radius * Math.sin(angle);
                    Location particleLocation = playerLocation.clone().add(xOffset, 2.0, zOffset);
                    world.spawnParticle(Particle.FLAME, particleLocation, 1, 0.0, 0.0, 0.0, 0.0);
                    ++i2;
                }
                event.setCancelled(true);
                player.sendMessage("no damage");
            }
        }
    }
}

