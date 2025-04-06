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
package me.magicwands.light;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitTask;

import me.magicwands.main.Main;
import me.magicwands.utils.ChatUtil;
import me.magicwands.wands.LightWand;

public class LightCloud implements Listener {
    private final Map<UUID, BukkitTask> LightWingsTasks = new HashMap<>();
    private final Map<UUID, Boolean> playerLightWingsStatus = new HashMap<>();

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (player.getItemInHand().getItemMeta() != null && 
        	    player.getItemInHand().getItemMeta().getDisplayName() != null && 
        	    player.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(LightWand.LightWand.getItemMeta().getDisplayName()) && 
        	    event.getAction() == Action.LEFT_CLICK_AIR && 
        	    me.magicwands.events.LightWand.LightSpell.getOrDefault(player, 0) == 10) { 

            UUID playerUUID = player.getUniqueId();
            boolean isActive = playerLightWingsStatus.getOrDefault(playerUUID, false);
            String color = Main.getPlugin(Main.class).getConfig().getString("wands.light.color");

            if (isActive) {
                playerLightWingsStatus.put(playerUUID, false);
                
                if (LightWingsTasks.containsKey(playerUUID)) {
                    LightWingsTasks.get(playerUUID).cancel();
                    LightWingsTasks.remove(playerUUID);
                }

                player.sendMessage(ChatUtil.color("&7[&" + color + "Magic&7] &" + color + "Stopped the Light Cloud.."));
            } else {
                playerLightWingsStatus.put(playerUUID, true);
                player.sendMessage(ChatUtil.color("&7[&" + color + "Magic&7] &" + color + "Started the Light Cloud.."));

                BukkitTask task = Bukkit.getScheduler().runTaskTimer(Main.getPlugin(Main.class), new Runnable() {
                    @Override
                    public void run() {
                        spawnCelestialWandParticles(player);
                    }
                }, 0L, 3L);

                LightWingsTasks.put(playerUUID, task);
            }
        }
    }

    

    @EventHandler
    public void RemoveNatureTrail(PlayerJoinEvent event) {
        Player p = event.getPlayer();

        playerLightWingsStatus.remove(p.getUniqueId());
    }
    
    @EventHandler
    public void RemoveTrail(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        
        if (playerLightWingsStatus.containsKey(player.getUniqueId()) && playerLightWingsStatus.get(player.getUniqueId())) {
        	playerLightWingsStatus.put(player.getUniqueId(), false);
        	if (LightWingsTasks.containsKey(player.getUniqueId())) {
                LightWingsTasks.get(player.getUniqueId()).cancel();
                LightWingsTasks.remove(player.getUniqueId());
            }
        }
    }
    
    private void spawnCelestialWandParticles(Player player) {
        if(player.isOnGround() ) {
        	return;
        } else {
        	player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_FLAP, (float) 0.1, 1);
        Particle.DustOptions starColor = new Particle.DustOptions(Color.WHITE, 1.0f);
        Particle.DustOptions cosmicDust = new Particle.DustOptions(Color.fromRGB(0, 0, 255), 1.0f);
        Particle.DustOptions nebulaColor = new Particle.DustOptions(Color.fromRGB(255, 0, 255), 1.0f);

        for (int i = 0; i < 10; i++) {
            double angle = Math.random() * Math.PI * 2;
            double offsetX = Math.cos(angle) * 0.5;
            double offsetY = Math.random() * 1.0;  
            double offsetZ = Math.sin(angle) * 0.5;
            
            player.getWorld().spawnParticle(Particle.REDSTONE, player.getLocation().add(offsetX, offsetY, offsetZ), 1, 0.0, 0.0, 0.0, 0.05, starColor);
            player.getWorld().spawnParticle(Particle.CLOUD, player.getLocation().add(offsetX, -0.1, offsetZ), 1, 0.2, -0.2, 0.2, 0.001);
            player.getWorld().spawnParticle(Particle.WHITE_ASH, player.getLocation().add(offsetX, offsetY, offsetZ), 1, 0.0, 0.0, 0.0, 0.001);
        }
        for (int i = 0; i < 15; i++) {
            double angle = Math.random() * Math.PI * 2;
            double offsetX = Math.cos(angle) * i * 0.1;
            double offsetY = Math.sin(angle) * i * 0.1;
            double offsetZ = Math.random() * 0.2 - 0.1;
            
            player.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, player.getLocation().add(offsetX, offsetY, offsetZ), 1, 0.0, 0.0, 0.0, 0.05);
        }
        for (int i = 0; i < 4; i++) {
            double offsetX = Math.random() * 0.4; 
            double offsetY = Math.random() * 0.1; 
            double offsetZ = Math.random() * 0.5; 

            player.getWorld().spawnParticle(Particle.END_ROD, player.getLocation().add(offsetX, offsetY, offsetZ), 0, -0.4, 0.1, 0.1, 0.05);
        }

        for (int i = 0; i < 4; i++) {
            double radius = 0.4 + i * 0.3;
            for (double t = 0; t < Math.PI * 2; t += Math.PI / 3) {
                double offsetX = Math.cos(t) * radius;
                double offsetY = 0.3 + i * 0.1;  
                double offsetZ = Math.sin(t) * radius;

                player.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, player.getLocation().add(offsetX, offsetY, offsetZ), 1, 0.3, 0.3, 0.3, 0.005);
                player.getWorld().spawnParticle(Particle.WHITE_ASH, player.getLocation().add(offsetX, offsetY, offsetZ), 1, 0.3, 0.3, 0.3, 0.005);
            }
        }
        for (int i = 0; i < 10; i++) {
            double offsetX = Math.random() * 1.0 - 0.5;
            double offsetY = Math.random() * 1.0 - 0.5;
            double offsetZ = Math.random() * 1.0 - 0.5;

            player.getWorld().spawnParticle(Particle.REDSTONE, player.getLocation().add(offsetX, offsetY, offsetZ), 1, 0.2, 0.2, 0.3, 0.1, starColor);
        }
        }
    }

}