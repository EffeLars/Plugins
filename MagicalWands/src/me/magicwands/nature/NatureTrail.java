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
package me.magicwands.nature;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Particle;
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
import me.magicwands.utils.ParticleColor;
import me.magicwands.wands.NatureWand;

public class NatureTrail implements Listener {
    private final Map<UUID, BukkitTask> natureTrailTasks = new HashMap<>();
    private final Map<UUID, Boolean> playerNatureTrailStatus = new HashMap<>();

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (player.getItemInHand().getItemMeta() != null &&
            player.getItemInHand().getItemMeta().getDisplayName() != null &&
            player.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(NatureWand.NatureWand.getItemMeta().getDisplayName()) &&
            event.getAction() == Action.LEFT_CLICK_AIR &&
            me.magicwands.events.NatureWand.NatureSpell.getOrDefault(player, 0) == 5) {

            UUID playerUUID = player.getUniqueId();
            boolean isActive = playerNatureTrailStatus.getOrDefault(playerUUID, false);
            String color = Main.getPlugin(Main.class).getConfig().getString("wands.nature.color");

            if (isActive) {
                playerNatureTrailStatus.put(playerUUID, false);
                
                if (natureTrailTasks.containsKey(playerUUID)) {
                    natureTrailTasks.get(playerUUID).cancel();
                    natureTrailTasks.remove(playerUUID);
                }

                player.sendMessage(ChatUtil.color("&7[&" + color + "Magic&7] &" + color + "Stopped the Nature trail.."));
            } else {
                playerNatureTrailStatus.put(playerUUID, true);
                player.sendMessage(ChatUtil.color("&7[&" + color + "Magic&7] &" + color + "Started the Nature trail.."));

                BukkitTask task = Bukkit.getScheduler().runTaskTimer(Main.getPlugin(Main.class), new Runnable() {
                    @Override
                    public void run() {
                        spawnNatureTrailParticles(player);
                    }
                }, 0L, 1L);

                natureTrailTasks.put(playerUUID, task);
            }
        }
    }

    

    @EventHandler
    public void RemoveNatureTrail(PlayerJoinEvent event) {
        Player p = event.getPlayer();

        playerNatureTrailStatus.remove(p.getUniqueId());
    }
    
    @EventHandler
    public void RemoveTrail(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        
        if (playerNatureTrailStatus.containsKey(player.getUniqueId()) && playerNatureTrailStatus.get(player.getUniqueId())) {
        	playerNatureTrailStatus.put(player.getUniqueId(), false);
        	if (natureTrailTasks.containsKey(player.getUniqueId())) {
                natureTrailTasks.get(player.getUniqueId()).cancel();
                natureTrailTasks.remove(player.getUniqueId());
            }
        }
    }
    
    private void spawnNatureTrailParticles(Player player) {
        Particle.DustOptions cyanColor = new Particle.DustOptions(Color.AQUA, 1.0f);
        Particle.DustOptions blueColor = new Particle.DustOptions(Color.fromRGB(0, 0, 255), 1.0f);
        Particle.DustOptions iceShardColor = new Particle.DustOptions(Color.fromRGB(170, 255, 255), 1.0f);
        
        player.getWorld().spawnParticle(Particle.REDSTONE, player.getLocation(), 1, 1.0, 1.0, 1.0, 0.03, ParticleColor.GREEN);
        player.getWorld().spawnParticle(Particle.REDSTONE, player.getLocation(), 1, 1.0, 1.0, 1.0, 0.03, ParticleColor.LIME_GREEN);
        player.getWorld().spawnParticle(Particle.REDSTONE, player.getLocation(), 1, 1.0, 1.0, 1.0, 0.03, ParticleColor.DARK_GREEN);
        player.getWorld().spawnParticle(Particle.BLOCK_CRACK, player.getLocation(), 2, 0.0, 0.0, 0.0, 0.03, Material.BAMBOO.createBlockData());
        player.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, player.getLocation(), 5, 1.0, 1.0, 1.0, 0.01);
        
        for (int i = 0; i < 10; i++) {
            double offsetX = Math.random() * 0.6 - 0.3;
            double offsetY = Math.random() * 0.6 - 0.3;
            double offsetZ = Math.random() * 0.6 - 0.3;
            player.getWorld().spawnParticle(Particle.REDSTONE, player.getLocation().getX() + offsetX, player.getLocation().getY() + offsetY, player.getLocation().getZ() + offsetZ, 1, ParticleColor.LIGHT_GREEN);
        }
    }
}
