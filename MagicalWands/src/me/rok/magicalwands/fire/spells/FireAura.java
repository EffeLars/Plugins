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
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import me.lars.game.Main;
import me.lars.game.event.FlameWand;
import me.lars.game.utils.ChatUtil;
import me.lars.game.wands.Wands;

public class FireAura implements Listener {
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
            event.getAction() == Action.LEFT_CLICK_AIR && FlameWand.Spell.get(player) == 10) {
            boolean isStaff = player.hasPermission("staff.wand");
            
            if (player.getScoreboardTags().contains("fireaura")) {
                player.removeScoreboardTag("fireaura");
                if (fireTrailTask != null) {
                    fireTrailTask.cancel();
                    fireTrailTask = null;
                }
                String color = Main.getPlugin(Main.class).getConfig().getString("wands.fire.color");
                player.sendMessage(ChatUtil.color("&7[&" + color + "Magic&7] &" + color + "Stopped the fire aura.."));
            } else {
                player.addScoreboardTag("fireaura");
                String color = Main.getPlugin(Main.class).getConfig().getString("wands.fire.color");
                player.sendMessage(ChatUtil.color("&7[&" + color + "Magic&7] &" + color + "Started the fire aura.."));

                fireTrailTask = Bukkit.getScheduler().runTaskTimer(Main.getPlugin(Main.class), new Runnable() {
                    @Override
                    public void run() {
                    	SummonFireAura(player.getLocation(), 4);
                    	for (Entity entity : player.getLocation().getWorld().getNearbyEntities(player.getLocation(), 4, 4, 4)) {
                            if (!(entity instanceof Player) || entity == player) continue;
                            Player nearbyPlayer = (Player)entity;
                            nearbyPlayer.setFireTicks(300);
                            Vector direction = nearbyPlayer.getLocation().getDirection().normalize().multiply(-1); // Reverse direction
                            nearbyPlayer.setVelocity(direction.multiply(2));
                            nearbyPlayer.playSound(nearbyPlayer.getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 1, 1);
                    	}
                    }
                }, 0L, 10L); // 0L delay, 10L period (10 ticks = 0.5 seconds)
            }
        }
    }

    @EventHandler
    public void RemoveAura(PlayerJoinEvent event) {
    	Player p = event.getPlayer();
    	if(p.getScoreboardTags().contains("fireaura")) {
    		p.removeScoreboardTag("fireaura");
    	}
    }
    
    public void SummonFireAura(Location location, int size) {
        for (int d = 0; d <= 90; d += 1) {
            Location particleLoc = new Location(location.getWorld(), location.getX(), location.getY(), location.getZ());
            particleLoc.setX(location.getX() + Math.cos(d) * size);
            particleLoc.setZ(location.getZ() + Math.sin(d) * size);
            location.getWorld().spawnParticle(Particle.REDSTONE, particleLoc.add(0, 4.2, 0), 1, new Particle.DustOptions(Color.BLACK, 1));
            location.getWorld().spawnParticle(Particle.REDSTONE, particleLoc, 1, new Particle.DustOptions(Color.GRAY, 1));
            location.getWorld().spawnParticle(Particle.REDSTONE, particleLoc, 1, new Particle.DustOptions(Color.ORANGE, 1));
            location.getWorld().spawnParticle(Particle.FLAME, particleLoc, 1, 0.5, 0.5, 0.5, 0.02);
            location.getWorld().spawnParticle(Particle.SMOKE_LARGE, particleLoc, 1, 0.5, 0.5, 0.5, 0.02);
            location.getWorld().spawnParticle(Particle.SMOKE_NORMAL, particleLoc, 1, 0.5, 0.5, 0.5, 0.02);
            location.getWorld().spawnParticle(Particle.ASH, particleLoc, 1, 1.0, 1.0, 1.0, 0.02);
            location.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, particleLoc, 1, 1.0, 1.0, 1.0, 0.08);
        }
    }
}
