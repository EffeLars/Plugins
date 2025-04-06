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
package me.magicwands.dark;

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
import me.magicwands.wands.DarkWand;

public class DarkCloud implements Listener {
    private final Map<UUID, BukkitTask> DarkWingsTasks = new HashMap<>();
    private final Map<UUID, Boolean> playerDarkWingsStatus = new HashMap<>();

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (player.getItemInHand().getItemMeta() != null && 
        	    player.getItemInHand().getItemMeta().getDisplayName() != null && 
        	    player.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(DarkWand.DarkWand.getItemMeta().getDisplayName()) && 
        	    event.getAction() == Action.LEFT_CLICK_AIR && 
        	    me.magicwands.events.DarkWand.DarkSpell.getOrDefault(player, 0) == 10) { 

            UUID playerUUID = player.getUniqueId();
            boolean isActive = playerDarkWingsStatus.getOrDefault(playerUUID, false);
            String color = Main.getPlugin(Main.class).getConfig().getString("wands.dark.color");

            if (isActive) {
                playerDarkWingsStatus.put(playerUUID, false);
                
                if (DarkWingsTasks.containsKey(playerUUID)) {
                    DarkWingsTasks.get(playerUUID).cancel();
                    DarkWingsTasks.remove(playerUUID);
                }

                player.sendMessage(ChatUtil.color("&7[&" + color + "Magic&7] &" + color + "Stopped the Dark Cloud.."));
            } else {
                playerDarkWingsStatus.put(playerUUID, true);
                player.sendMessage(ChatUtil.color("&7[&" + color + "Magic&7] &" + color + "Started the Dark Cloud.."));

                BukkitTask task = Bukkit.getScheduler().runTaskTimer(Main.getPlugin(Main.class), new Runnable() {
                    @Override
                    public void run() {
                        spawnDarkWandParticles(player);
                    }
                }, 0L, 3L);

                DarkWingsTasks.put(playerUUID, task);
            }
        }
    }

    

    @EventHandler
    public void RemoveNatureTrail(PlayerJoinEvent event) {
        Player p = event.getPlayer();

        playerDarkWingsStatus.remove(p.getUniqueId());
    }
    
    @EventHandler
    public void RemoveTrail(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        
        if (playerDarkWingsStatus.containsKey(player.getUniqueId()) && playerDarkWingsStatus.get(player.getUniqueId())) {
        	playerDarkWingsStatus.put(player.getUniqueId(), false);
        	if (DarkWingsTasks.containsKey(player.getUniqueId())) {
                DarkWingsTasks.get(player.getUniqueId()).cancel();
                DarkWingsTasks.remove(player.getUniqueId());
            }
        }
    }
    
    private void spawnDarkWandParticles(Player player) {
        if(player.isOnGround() ) {
        	return;
        } else {
        	player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_FLAP, (float) 0.1, 1);

        Particle.DustOptions Black = new Particle.DustOptions(Color.BLACK, 1.0f);
        Particle.DustOptions Gray = new Particle.DustOptions(Color.GRAY, 1.0f);


        for (int i = 0; i < 10; i++) {
            double angle = Math.random() * Math.PI * 2;
            double offsetX = Math.cos(angle) * 0.5;
            double offsetY = Math.random() * 0.1;
            double offsetZ = Math.sin(angle) * 0.5;
            player.getWorld().spawnParticle(Particle.REDSTONE, player.getLocation().add(offsetX, offsetY, offsetZ), 1, 0.0, 0.0, 0.0, 0.05, Gray);
            player.getWorld().spawnParticle(Particle.REDSTONE, player.getLocation().add(offsetX, offsetY, offsetZ), 1, 0.0, 0.0, 0.0, 0.05, Black);
            player.getWorld().spawnParticle(Particle.SMOKE_NORMAL, player.getLocation().add(offsetX, -0.1, offsetZ), 1, 0.2, -0.2, 0.2, 0.001);
            player.getWorld().spawnParticle(Particle.ASH, player.getLocation().add(offsetX, offsetY, offsetZ), 1, 0.0, 0.0, 0.0, 0.001);
        }
        for (int i = 0; i < 15; i++) {
            double angle = Math.random() * Math.PI * 2;
            double offsetX = Math.cos(angle) * i * 0.1;
            double offsetY = Math.sin(angle) * i * 0.1;
            double offsetZ = Math.random() * 0.2 - 0.1;
            
            player.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, player.getLocation().add(offsetX, offsetY, offsetZ), 1, 0.0, 0.0, 0.0, 0.05);
        }
        for (int i = 0; i < 4; i++) {
        	double angle = Math.random() * Math.PI * 2;
        	 double offsetX = Math.cos(angle) * 0.5;
             double offsetY = Math.random() * 0.1;  
             double offsetZ = Math.sin(angle) * 0.5;

            Color darkGray = Color.fromRGB(50, 50, 50); 
            Particle.DustOptions dustOptions = new Particle.DustOptions(darkGray, 3.0f); 
            Particle.DustOptions Gray2 = new Particle.DustOptions(Color.GRAY, 2.0f);
            player.getWorld().spawnParticle(Particle.REDSTONE, player.getLocation().add(offsetX, offsetY, offsetZ), 1, 0, -0.4, 0, 0.05, Gray2); 
            player.getWorld().spawnParticle(Particle.REDSTONE, player.getLocation().add(offsetX, offsetY, offsetZ), 1, 0, -0.4, 0, 0.05, dustOptions);       }

        for (int i = 0; i < 4; i++) {
            double radius = 0.4 + i * 0.3;
            for (double t = 0; t < Math.PI * 2; t += Math.PI / 3) {
                double offsetX = Math.cos(t) * radius;
                double offsetY = 0.3 + i * 0.1;  
                double offsetZ = Math.sin(t) * radius;

                player.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, player.getLocation().add(offsetX, offsetY, offsetZ), 1, 0.1, 0.2, 0.1, 0.005);
                player.getWorld().spawnParticle(Particle.ASH, player.getLocation().add(offsetX, offsetY, offsetZ), 1, 0.3, 0.3, 0.3, 0.005);
            }
        }
        for (int i = 0; i < 10; i++) {
            double offsetX = Math.random() * 1.0 - 0.5;
            double offsetY = Math.random() * 0.5 - 0.2;
            double offsetZ = Math.random() * 1.0 - 0.5;

            player.getWorld().spawnParticle(Particle.REDSTONE, player.getLocation().add(offsetX, offsetY, offsetZ), 1, 0.2, 0.2, 0.3, 0.1, Black);
        }
        }
    }

}