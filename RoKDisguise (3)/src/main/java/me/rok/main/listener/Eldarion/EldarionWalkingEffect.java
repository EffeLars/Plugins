/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  org.bukkit.Particle
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.PlayerMoveEvent
 */
package me.rok.main.listener.Eldarion;

import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class EldarionWalkingEffect
implements Listener {
    @EventHandler
    public void OnWalk(PlayerMoveEvent e2) {
        Player p2 = e2.getPlayer();
        if (p2.getScoreboardTags().contains("Eldarion")) {
            p2.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, p2.getLocation(), 5, 0.5, 0.5, 0.5, 0.0);
            p2.getWorld().spawnParticle(Particle.WHITE_ASH, p2.getLocation(), 5, 0.5, 0.5, 0.5, 0.0);
        }
    }
}

