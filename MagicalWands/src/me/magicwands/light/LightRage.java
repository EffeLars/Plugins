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

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import me.magicwands.main.Main;
import me.magicwands.utils.ChatUtil;
import me.magicwands.utils.GetTargetEntity;
import me.magicwands.wands.LightWand;

public class LightRage implements Listener {
    private final Map<Player, BukkitTask> LightRageTask = new HashMap<>();
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (player.getItemInHand().getItemMeta() != null && 
        	    player.getItemInHand().getItemMeta().getDisplayName() != null && 
        	    player.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(LightWand.LightWand.getItemMeta().getDisplayName()) && 
        	    event.getAction() == Action.LEFT_CLICK_AIR && 
        	    me.magicwands.events.LightWand.LightSpell.getOrDefault(player, 0) == 5) { 
            
            if (LightRageTask.containsKey(player)) {
                stopLightRage(player);
            } else {
                startLightRage(player);
            }
        }
    }
    
    private void startLightRage(Player player) {
        String color = Main.getPlugin(Main.class).getConfig().getString("wands.light.color");
        player.sendMessage(ChatUtil.color("&7[&" + color + "Magic&7] &" + color + "Started the light rage.."));
        
        BukkitTask task = new BukkitRunnable() {
            @Override
            public void run() {
                SpawnLightRageParticles(player);
            }
        }.runTaskTimer(Main.getPlugin(Main.class), 0L, 22L);
        
        LightRageTask.put(player, task);
    }
    
    private void stopLightRage(Player player) {
        if (LightRageTask.containsKey(player)) {
            LightRageTask.get(player).cancel();
            LightRageTask.remove(player);
        }
        
        String color = Main.getPlugin(Main.class).getConfig().getString("wands.light.color");
        player.sendMessage(ChatUtil.color("&7[&" + color + "Magic&7] &" + color + "Stopped the light rage.."));
    }
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        stopLightRage(player); 
    }
    
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        stopLightRage(player); 
    }
    
    private void SpawnLightRageParticles(Player player) {
        for (int i = 0; i < 4; i++) {
            double randomX = (Math.random() - 0.8) * 17.6;
            double randomZ = (Math.random() - 0.8) * 17.6;
            int x = (int) (player.getLocation().getX() + randomX + 12);
            int z = (int) (player.getLocation().getZ() + randomZ + 12);
            int groundY = player.getWorld().getHighestBlockYAt(x, z);
            player.getWorld().strikeLightning(new Location(player.getWorld(), x, groundY, z));
            player.getWorld().strikeLightning(new Location(player.getWorld(), x, groundY, z));

            Entity target = GetTargetEntity.getTargetEntity(player);
            if (target instanceof LivingEntity) {
                ((LivingEntity) target).damage(2.0, player); 
            }
        }
        player.getWorld().spawnParticle(Particle.CLOUD, player.getLocation(), 65, 3.0, 3.0, 3.0, 0.001);
        player.getWorld().spawnParticle(Particle.ASH, player.getLocation(), 50, 3.0, 3.0, 3.0, 0.01);
        player.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, player.getLocation(), 50, 3.0, 3.0, 3.0, 0.01);
        player.getWorld().spawnParticle(Particle.SMOKE_LARGE, player.getLocation(), 10, 1.0, 1.0, 1.0, 0.001);
    }
}