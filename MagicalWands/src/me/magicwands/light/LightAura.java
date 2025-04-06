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
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import me.magicwands.main.Main;
import me.magicwands.utils.ChatUtil;
import me.magicwands.wands.LightWand;

public class LightAura implements Listener {

	private Map<UUID, Boolean> lightAuraMap = new HashMap<>();
	private Map<UUID, BukkitTask> lightAuraTasks = new HashMap<>();

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
	    Player player = event.getPlayer();
	    
	    if (player.getItemInHand().getItemMeta() != null && 
	        player.getItemInHand().getItemMeta().getDisplayName() != null && 
	        player.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(LightWand.LightWand.getItemMeta().getDisplayName()) && 
	        event.getAction() == Action.LEFT_CLICK_AIR && 
	        me.magicwands.events.LightWand.LightSpell.getOrDefault(player, 0) == 6) { 

	        UUID playerUUID = player.getUniqueId();
	        boolean hasAura = lightAuraMap.getOrDefault(playerUUID, false);

	        if (hasAura) {
	            lightAuraMap.put(playerUUID, false);
	            
	            if (lightAuraTasks.containsKey(playerUUID)) {
	                lightAuraTasks.get(playerUUID).cancel();
	                lightAuraTasks.remove(playerUUID);
	            }

	            String color = Main.getPlugin(Main.class).getConfig().getString("wands.light.color");
	            player.sendMessage(ChatUtil.color("&7[&" + color + "Magic&7] &" + color + "Stopped the light aura.."));
	        } else {
	            lightAuraMap.put(playerUUID, true);
	            String color = Main.getPlugin(Main.class).getConfig().getString("wands.light.color");
	            player.sendMessage(ChatUtil.color("&7[&" + color + "Magic&7] &" + color + "Started the light aura.."));

	            BukkitTask task = Bukkit.getScheduler().runTaskTimer(Main.getPlugin(Main.class), () -> {
	                SummonLightAura(player.getLocation(), 6);
	                for (Entity entity : player.getLocation().getWorld().getNearbyEntities(player.getLocation(), 4, 4, 4)) {
	                    if (!(entity instanceof Player) || entity == player) continue;
	                    Player nearbyPlayer = (Player) entity;
	                    nearbyPlayer.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 2));
	                    nearbyPlayer.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 100, 2));
	                    nearbyPlayer.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 700, 2));
	                    Vector direction = nearbyPlayer.getLocation().getDirection().normalize().multiply(-1);
	                    nearbyPlayer.setVelocity(direction.multiply(3));
	                    nearbyPlayer.getWorld().playSound(nearbyPlayer.getLocation(), Sound.ENTITY_ENDER_DRAGON_FLAP, 1, 1);
	                    nearbyPlayer.playSound(nearbyPlayer.getLocation(), Sound.ITEM_TRIDENT_THUNDER, 1, -3);
	                }
	            }, 4L, 10L);

	            lightAuraTasks.put(playerUUID, task);
	        }
	    }
	}


    @EventHandler
    public void RemoveAura(PlayerJoinEvent event) {
        Player p = event.getPlayer();
        lightAuraMap.remove(p.getUniqueId());
    }

    @EventHandler
    public void RemoveAuraQuit(PlayerQuitEvent event) {
        Player p = event.getPlayer();

      
        UUID playerUUID = p.getUniqueId();
        if (lightAuraTasks.containsKey(playerUUID)) {
            lightAuraTasks.get(playerUUID).cancel();
            lightAuraTasks.remove(playerUUID);
        }
    }
    public void SummonLightAura(Location location, int size) {
        for (int y = 0; y <= size; y++) {
            double radius = Math.sqrt(size * size - y * y);

            for (int d = 0; d < 360; d += 15) { 
                double radians = Math.toRadians(d);
                double x = Math.cos(radians) * radius;
                double z = Math.sin(radians) * radius;
                Location particleLoc = location.clone().add(x, y, z);

                double angle = Math.random() * 2.0 * Math.PI;  
                double randomX = Math.cos(angle) * 1.6; 
                double randomY = Math.random() * 0.3;  
                double randomZ = Math.sin(angle) * 0.2;  
                Location randomParticleLocation = particleLoc.clone().add(randomX, randomY, randomZ);

                int particleCount = (int)(Math.random() * 6) + 8;  
                Particle.DustOptions dustOptions = new Particle.DustOptions(Color.WHITE, 1.0f);
                Particle.DustOptions dustOptions2 = new Particle.DustOptions(Color.GRAY, 1.0f);
                location.getWorld().spawnParticle(Particle.REDSTONE, randomParticleLocation, 1, 0.7, 0.3, 0.7, dustOptions);
                location.getWorld().spawnParticle(Particle.REDSTONE, randomParticleLocation, 1, 0.7, 0.3, 0.7, dustOptions2);
                location.getWorld().spawnParticle(Particle.SPELL_INSTANT, randomParticleLocation, 1, 0.5, 0.5, 0.5, 0.002);
                location.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, randomParticleLocation, 15, 0.5, 0.5, 0.5, 0.004);
                location.getWorld().spawnParticle(Particle.CLOUD, randomParticleLocation, 1, 0.1, 0.2, 0.3, 0.004);
                location.getWorld().spawnParticle(Particle.WHITE_ASH, randomParticleLocation, 5, 0.5, 0.5, 0.5, 0.004);
            }
        }
    }


}
