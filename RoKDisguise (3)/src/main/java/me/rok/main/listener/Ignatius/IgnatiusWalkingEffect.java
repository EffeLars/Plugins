/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.Particle
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.PlayerMoveEvent
 */
package me.rok.main.listener.Ignatius;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class IgnatiusWalkingEffect
implements Listener {
    @EventHandler
    public void onWalk(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (player.getScoreboardTags().contains("Ignatius")) {
            Location location = player.getLocation();
            location.setY(location.getY() - 0.05);
            player.getWorld().spawnParticle(Particle.FLAME, location, 1, 0.5, 0.0, 0.5, 0.0);
            player.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, location, 5, 0.5, 0.0, 0.5, 0.0);
            player.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, location, 1, 0.5, 0.0, 0.5, 0.0);
        }
    }
}

