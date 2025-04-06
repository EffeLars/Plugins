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
package me.magicwands.empire;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

import me.magicwands.main.Main;
import me.magicwands.utils.ChatUtil;
import me.magicwands.utils.ParticleColor;
import me.magicwands.wands.WandItems;

public class EmpireHunt implements Listener {
    private final Map<UUID, BukkitTask> OreonHuntTask = new HashMap<>();
    private final Map<UUID, Boolean> OreonHuntPlayers = new HashMap<>();

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (player.getItemInHand().getItemMeta() != null && 
                player.getItemInHand().getItemMeta().getDisplayName() != null && 
                player.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(WandItems.EmpireWand.getItemMeta().getDisplayName()) && 
                event.getAction() == Action.LEFT_CLICK_AIR && 
                me.magicwands.events.EmpireWand.EmpireSpell.getOrDefault(player, 0) == 15) {

            UUID playerUUID = player.getUniqueId();
            boolean isActive = OreonHuntPlayers.getOrDefault(playerUUID, false);
            String color = Main.getPlugin(Main.class).getConfig().getString("wands.empire.color");

            if (isActive) {
                OreonHuntPlayers.put(playerUUID, false);
                
                if (OreonHuntTask.containsKey(playerUUID)) {
                    OreonHuntTask.get(playerUUID).cancel();
                    OreonHuntTask.remove(playerUUID);
                }

                player.sendMessage(ChatUtil.color("&7[&" + color + "Magic&7] &" + color + "Stopped the Empire Hunt Mode&c&l!"));
            } else {
                OreonHuntPlayers.put(playerUUID, true);
                player.sendMessage(ChatUtil.color("&7[&" + color + "Magic&7] &" + color + "Started the Empire Hunt Mode&c&l!"));

                BukkitTask task = Bukkit.getScheduler().runTaskTimer(Main.getPlugin(Main.class), new Runnable() {
                    @Override
                    public void run() {
                        CheckForNearbyEntities(player);
                        
                    }
                }, 200L, 400L);

                OreonHuntTask.put(playerUUID, task);
            }
        }
    }

    

    @EventHandler
    public void RemoveNatureTrail(PlayerJoinEvent event) {
        Player p = event.getPlayer();

        OreonHuntPlayers.remove(p.getUniqueId());
    }
    
    @EventHandler
    public void RemoveTrail(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        
        if (OreonHuntPlayers.containsKey(player.getUniqueId()) && OreonHuntPlayers.get(player.getUniqueId())) {
        	OreonHuntPlayers.put(player.getUniqueId(), false);
        	if (OreonHuntTask.containsKey(player.getUniqueId())) {
                OreonHuntTask.get(player.getUniqueId()).cancel();
                OreonHuntTask.remove(player.getUniqueId());
            }
        }
    }
    
    
    private void CheckForNearbyEntities(Player player) {
    	
        for (Entity entity : player.getWorld().getNearbyEntities(player.getLocation(), 30, 30, 30)) {
            spawnParticles(player);
                Player nearbyPlayer = (Player) entity;
                nearbyPlayer.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 100, 1)); // 100 ticks = 5 seconds
                spawnParticles(nearbyPlayer);
           
        }
    }

    private void spawnParticles(Player player) {
    	player.getWorld().playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 1, 3);
        player.getWorld().spawnParticle(Particle.REDSTONE, player.getLocation(), 1, 1.0, 1.0, 1.0, 0.003, ParticleColor.PURPLE);
        player.getWorld().spawnParticle(Particle.REDSTONE, player.getLocation(), 1, 1.0, 1.0, 1.0, 0.003, ParticleColor.DARK_PURPLE);
        player.getWorld().spawnParticle(Particle.REDSTONE, player.getLocation(), 1, 1.0, 1.0, 1.0, 0.003, ParticleColor.DARK_GRAY);
        player.getWorld().spawnParticle(Particle.BLOCK_CRACK, player.getLocation(), 3, 0.0, 0.0, 0.0, 0.03, Material.NETHER_PORTAL.createBlockData());
        player.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, player.getLocation(), 5, 1.0, 1.0, 1.0, 0.01);
        player.getWorld().spawnParticle(Particle.PORTAL, player.getLocation(), 5, 1.0, 1.0, 1.0, 0.01);
    }
}