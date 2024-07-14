/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Particle
 *  org.bukkit.Sound
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.block.Action
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.event.player.PlayerMoveEvent
 *  org.bukkit.inventory.ItemStack
 */
package me.rok.main.listener.Ignatius;

import me.rok.main.characters.special.Ignatius;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

public class IgnatiusSword
implements Listener {
    @EventHandler
    public void UpdateBar(PlayerMoveEvent e2) {
        Player p2 = e2.getPlayer();
        Ignatius.updateDisguiseBossBar(p2);
    }

    @EventHandler
    public void SwordHit(PlayerInteractEvent e2) {
        Player p2 = e2.getPlayer();
        ItemStack item = me.rok.main.items.Ignatius.IgnatiusSword;
        if (e2.getAction() == Action.LEFT_CLICK_AIR && p2.getItemInHand().equals((Object)item)) {
            Bukkit.broadcastMessage((String)"FIRE");
            p2.getWorld().spawnParticle(Particle.FLAME, p2.getLocation(), 5, 0.5, 0.5, 0.5, 0.0);
            p2.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, p2.getLocation(), 5, 0.5, 0.5, 0.5, 0.0);
            p2.getPlayer().playSound(p2.getLocation(), Sound.ITEM_FIRECHARGE_USE, 1.0f, 1.0f);
        }
    }
}

