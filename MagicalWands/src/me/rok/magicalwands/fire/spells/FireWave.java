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
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import me.lars.game.Main;
import me.lars.game.event.FlameWand;
import me.lars.game.utils.ChatUtil;
import me.lars.game.wands.Wands;

public class FireWave implements Listener {
    private static final double FORWARD_POWER_MODIFIER = 1.5;
    private static final double UPWARD_POWER_MODIFIER = 3.0;
    private static final double FORWARD_VELOCITY = 2.0;
    private static final double UPWARD_VELOCITY = 0.7;
    private static final int PARTICLE_DELAY = 1;
    static long leapcooldownp = Main.getPlugin(Main.class).getConfig().getInt("spells.fireleap.playercooldown");
    private static final long PLAYER_COOLDOWN_SECONDS = leapcooldownp;
    private static final long PLAYER_COOLDOWN_MS = PLAYER_COOLDOWN_SECONDS * 1000; 
    static long leapcooldowns = Main.getPlugin(Main.class).getConfig().getInt("spells.fireleap.staffcooldown");
    private static final long STAFF_COOLDOWN_SECONDS = leapcooldowns; 
    private static final long STAFF_COOLDOWN_MS = STAFF_COOLDOWN_SECONDS * 1000;
    
    private Map<UUID, BukkitRunnable> particleTasks = new HashMap<>();
    private Map<UUID, Long> cooldowns = new HashMap<>();

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (player.getItemInHand().getItemMeta() != null &&
                player.getItemInHand().getItemMeta().getDisplayName() != null &&
                player.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(Wands.IgnatiusWand.getItemMeta().getDisplayName()) &&
                event.getAction() == Action.LEFT_CLICK_AIR && FlameWand.Spell.get(player) == 9) {

            String color = Main.getPlugin(Main.class).getConfig().getString("wands.fire.color");
            player.sendMessage(ChatUtil.color("&7[&" + color + "Magic&7] &" + color + "Using spell..."));

            Location initialLocation = player.getLocation().add(player.getLocation().getDirection().normalize().multiply(2));
            
            BukkitTask fireTrailTask = Bukkit.getScheduler().runTaskTimer(Main.getPlugin(Main.class), new Runnable() {
                Location currentLocation = initialLocation;

                @Override
                public void run() {
                    spawnFireDisappearParticles(currentLocation);
                    currentLocation = getNextLocation(currentLocation);
                    spawnFire(currentLocation);
                    for (Entity entity : currentLocation.getWorld().getNearbyEntities(currentLocation, 1, 1, 1)) {
                        if (entity instanceof Player) {
                            Player nearbyPlayer = (Player) entity;
                            nearbyPlayer.setFireTicks(300);
                            nearbyPlayer.getWorld().playSound(nearbyPlayer.getLocation(), Sound.BLOCK_LAVA_EXTINGUISH, 1, 1);
                            nearbyPlayer.getWorld().spawnParticle(Particle.FLAME, nearbyPlayer.getLocation(), 30, 0.9, 0.9, 0.9, 0.01);
                            nearbyPlayer.getWorld().spawnParticle(Particle.SMOKE_NORMAL, nearbyPlayer.getLocation(), 20, 0.9, 0.9, 0.9, 0.01);
                            nearbyPlayer.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, nearbyPlayer.getLocation(), 50, 0.9, 0.9, 0.9, 0.01);
                            
                        } else {
                           //
                        }
                    }
                
                }
            }, 0L, 1L); 
        
           BukkitTask fireWaveSound = Bukkit.getScheduler().runTaskTimer(Main.getPlugin(Main.class), new Runnable() {
                Location currentLocation = player.getLocation(); 

                @Override
                public void run() {
                	spawnFireWaveSound(currentLocation);
                    currentLocation = getNextLocation(currentLocation);
                    spawnFire(currentLocation);
                    
                }
            }, 0L, 2L); 

            new BukkitRunnable() {
                @Override
                public void run() {
                    fireTrailTask.cancel();
                    fireWaveSound.cancel();
                }
            }.runTaskLater(Main.getPlugin(Main.class), 60L);
        }
    }

    private void spawnFireDisappearParticles(Location location) {
        location.getWorld().spawnParticle(Particle.FLAME, location, 10, 0.9, 0.9, 0.9, 0.01);
        location.getWorld().spawnParticle(Particle.SMOKE_NORMAL, location, 20, 0.9, 0.9, 0.9, 0.01);
        location.getWorld().spawnParticle(Particle.SMOKE_LARGE, location, 5, 0.9, 0.9, 0.9, 0.004);
        location.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, location, 50, 0.9, 0.9, 0.9, 0.01);
    }
    
    private void spawnFire(Location location) {
        World world = location.getWorld();
        if (world == null) return;

        Block blockBelow = location.getBlock().getRelative(BlockFace.DOWN);
        if (!blockBelow.getType().isSolid()) {
            return; 
        }

        
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                Location fireLocation = location.clone().add(x + 0.5, 1, z + 0.5); 
                if (fireLocation.getBlock().getType() == Material.AIR) {
                    fireLocation.getBlock().setType(Material.FIRE);
                    fireLocation.getWorld().playSound(fireLocation, Sound.ENTITY_WOLF_STEP, 1, 1);
                    fireLocation.getWorld().spawnParticle(Particle.FLAME, fireLocation, 30, 0.5, 0.5, 0.5, 0.02);
                    fireLocation.getWorld().spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, fireLocation, 3, 0.5, 0.5, 0.5, 0.02);
                }

               
            }
        }
    }

   

    private void spawnFireWaveSound(Location location) {
        location.getWorld().playSound(location, Sound.ENTITY_ENDER_DRAGON_FLAP, 1, 1);
    }

    private Location getNextLocation(Location currentLocation) {
        Vector direction = currentLocation.getDirection().normalize().multiply(0.5); 
        return currentLocation.add(direction);
    }
}	
	
