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
package me.rok.magicalwands.flower.spells;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import me.lars.game.Main;
import me.lars.game.utils.ChatUtil;
import me.lars.game.utils.ParticleColor;
import me.lars.game.wands.FlowerWand;

public class FlowerShockwave implements Listener {
    private Map<UUID, BukkitRunnable> FlowerWaveTasks = new HashMap<>();
    private Map<UUID, Long> cooldowns = new HashMap<>();

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (player.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(FlowerWand.FlowerWand.getItemMeta().getDisplayName())
                && event.getAction() == Action.LEFT_CLICK_AIR && me.lars.game.event.FlowerWand.FlowerSpell.get(player) == 2) {

            String color = Main.getPlugin(Main.class).getConfig().getString("wands.flower.color");
            player.sendMessage(ChatUtil.color("&7[&" + color + "Magic&7] &" + color + "Using spell.."));

            Location initialLocation = player.getLocation().add(player.getLocation().getDirection().normalize().multiply(2));
            
            BukkitTask NatureTrailTask = Bukkit.getScheduler().runTaskTimer(Main.getPlugin(Main.class), new Runnable() {
                Location currentLocation = initialLocation; 
                final double[] radius = {1.0}; 
                final double stepSize = 0.1;
                
                @Override
                public void run() {
                	  summonCircle(player.getLocation(), Particle.ENCHANTMENT_TABLE, 50, radius[0], 0);
                	  summonCircle(player.getLocation(), Particle.TOTEM, 45, radius[0], 0);
                	  summonCircle(player.getLocation(), Particle.SMOKE_NORMAL, 40, radius[0], 0);
                      radius[0] += stepSize; 
                   
                }
            }, 0L, 1L); 
        
           BukkitTask FlowerShockwaveSound = Bukkit.getScheduler().runTaskTimer(Main.getPlugin(Main.class), new Runnable() {
                Location currentLocation = player.getLocation(); 

                @Override
                public void run() {
                	spawnNatureWaveSound(currentLocation);
                    currentLocation = getNextLocation(currentLocation);
                    
                    
                }
            }, 0L, 2L); 

            new BukkitRunnable() {
                @Override
                public void run() {
                    NatureTrailTask.cancel();
                    FlowerShockwaveSound.cancel();
                }
            }.runTaskLater(Main.getPlugin(Main.class), 60L); 
        }
    }

    private void spawnNatureDisappearParticles(Location location) {
    	location.getWorld().spawnParticle(Particle.REDSTONE, location, 5, 1.0, 1.0, 1.0, 0.03, ParticleColor.DARK_GREEN);
    	location.getWorld().spawnParticle(Particle.REDSTONE, location, 5, 1.0, 1.0, 1.0, 0.03, ParticleColor.LIME_GREEN);
    	location.getWorld().spawnParticle(Particle.REDSTONE, location, 5, 1.0, 1.0, 1.0, 0.03, ParticleColor.GREEN);
    	location.getWorld().spawnParticle(Particle.TOTEM, location, 2, 0.9, 0.9, 0.9, 0.01);
    	location.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, location, 30, 0.9, 0.9, 0.9, 0.01);;
         location.getWorld().spawnParticle(Particle.BLOCK_CRACK, location, 15, 0.7, 0.7, 0.7, 0.03, Material.OAK_LEAVES.createBlockData());
         location.getWorld().spawnParticle(Particle.BLOCK_CRACK, location, 15, 0.7, 0.7, 0.7, 0.03, Material.SUNFLOWER.createBlockData());
         location.getWorld().spawnParticle(Particle.BLOCK_CRACK, location, 5, 0.7, 0.7, 0.7, 0.03, Material.POPPY.createBlockData());
    }
    
    private void summonCircle(Location center, Particle particle, int points, double radius, double height) {
        World world = center.getWorld();
        for (int i = 0; i < points; i++) {
            double angle = 2 * Math.PI * i / points;
            double x = center.getX() + radius * Math.cos(angle);
            double z = center.getZ() + radius * Math.sin(angle);
            Location particleLocation = new Location(world, x, center.getY() + height, z);
            world.spawnParticle(particle, particleLocation, 1, 0, 0, 0, 0);
        }
    }
   

    private void spawnNatureWaveSound(Location location) {

        location.getWorld().playSound(location, Sound.ENTITY_ENDER_DRAGON_FLAP, 1, 1);
        location.getWorld().playSound(location, Sound.BLOCK_WOOD_BREAK, 1, 1);
    }

    private Location getNextLocation(Location currentLocation) {
        Vector direction = currentLocation.getDirection().normalize().multiply(0.5); // Move 0.5 blocks forward
        return currentLocation.add(direction);
    }
}	
	
