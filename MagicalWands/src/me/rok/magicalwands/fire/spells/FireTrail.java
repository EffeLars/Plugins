/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.Particle
 *  org.bukkit.Sound
 *  org.bukkit.World
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.block.Action
 *  org.bukkit.event.entity.EntityDamageEvent
 *  org.bukkit.event.entity.EntityDamageEvent$DamageCause
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 *  org.bukkit.util.Vector
 */
package me.rok.magicalwands.fire.spells;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import me.lars.game.Main;
import me.lars.game.event.FlameWand;
import me.lars.game.utils.ChatUtil;
import me.lars.game.wands.Wands;

public class FireTrail implements Listener {
    private static final long STAFF_COOLDOWN = 0000L;
    private static final long PLAYER_COOLDOWN = 30000L;
    private Map<UUID, BukkitRunnable> particleTasks = new HashMap<>();
    private Map<UUID, Long> cooldowns = new HashMap<>();
    private BukkitTask fireTrailTask = null;
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (player.getItemInHand().getItemMeta() != null &&
            player.getItemInHand().getItemMeta().getDisplayName() != null &&
            player.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(Wands.IgnatiusWand.getItemMeta().getDisplayName()) &&
            event.getAction() == Action.LEFT_CLICK_AIR && FlameWand.Spell.get(player) == 5) {
            boolean isStaff = player.hasPermission("staff.wand");
            
            if (player.getScoreboardTags().contains("firetrail")) {
                player.removeScoreboardTag("firetrail");
                if (fireTrailTask != null) {
                    fireTrailTask.cancel();
                    fireTrailTask = null;
                }
                String color = Main.getPlugin(Main.class).getConfig().getString("wands.fire.color");
                player.sendMessage(ChatUtil.color("&7[&" + color + "Magic&7] &" + color + "Stopped the fire trail.."));
            } else {
                player.addScoreboardTag("firetrail");
                String color = Main.getPlugin(Main.class).getConfig().getString("wands.fire.color");
                player.sendMessage(ChatUtil.color("&7[&" + color + "Magic&7] &" + color + "Started the fire trail.."));

                fireTrailTask = Bukkit.getScheduler().runTaskTimer(Main.getPlugin(Main.class), new Runnable() {
                    @Override
                    public void run() {
                        spawnFireTrailParticles(player);
                    }
                }, 0L, 1L); // 0L delay, 10L period (10 ticks = 0.5 seconds)
            }
        }
    }

    @EventHandler
    public void RemoveTrail(PlayerJoinEvent event) {
    	Player p = event.getPlayer();
    	if(p.getScoreboardTags().contains("firetrail")) {
    		p.removeScoreboardTag("firetrail");
    	}
    }
    
    private void spawnFireTrailParticles(Player player) {
    	if(player.isOnGround()) {
        player.getWorld().spawnParticle(Particle.WHITE_ASH, player.getLocation(), 5, 0.5, 0.5, 0.5, 0.0);
        player.getWorld().spawnParticle(Particle.FLAME, player.getLocation(), 1, 0.5, 0.0, 0.5, 0.03);
        player.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, player.getLocation(), 5, 0.5, 0.0, 0.5, 0.02);
        player.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, player.getLocation(), 1, 0.5, 0.0, 0.5, 0.0);
    } else {
    	player.getWorld().spawnParticle(Particle.WHITE_ASH, player.getLocation(), 5, 0.5, 0.5, 0.5, 0.0);
        player.getWorld().spawnParticle(Particle.FLAME, player.getLocation(), 1, 0.5, 0.0, 0.5, 0.03);
        player.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, player.getLocation(), 5, 0.5, 0.0, 0.5, 0.02);
        player.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, player.getLocation(), 1, 0.5, 0.0, 0.5, 0.0);
        player.getWorld().spawnParticle(Particle.SMOKE_LARGE, player.getLocation(), 1, 0.5, 0.0, 0.5, 0.003);
        player.getWorld().spawnParticle(Particle.SMOKE_NORMAL, player.getLocation(), 1, 0.5, 0.0, 0.5, 0.003);
    }
}
}
