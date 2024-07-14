/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  org.bukkit.Color
 *  org.bukkit.Particle
 *  org.bukkit.Particle$DustOptions
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.PlayerMoveEvent
 */
package me.rok.main.listener.Mortiferum;

import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class WalkingEffect
implements Listener {
    @EventHandler
    public void OnWalk(PlayerMoveEvent e2) {
        Player p2 = e2.getPlayer();
        if (p2.getScoreboardTags().contains("Mortiferum")) {
            p2.getWorld().spawnParticle(Particle.REDSTONE, p2.getLocation(), 15, 0.5, 0.5, 0.5, 0.0, (Object)new Particle.DustOptions(Color.GREEN, 1.0f));
        }
    }
}

