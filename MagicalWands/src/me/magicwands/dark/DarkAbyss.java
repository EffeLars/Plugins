
package me.magicwands.dark;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
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
import org.bukkit.util.Vector;

import me.magicwands.main.Main;
import me.magicwands.utils.ChatUtil;
import me.magicwands.wands.DarkWand;

public class DarkAbyss implements Listener {
	private Map<UUID, Boolean> DarkAbyssPlayers = new HashMap<>();
	private Map<UUID, BukkitTask> DarkAbyssTask = new HashMap<>();

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
	    Player player = event.getPlayer();
	    
	    if (player.getItemInHand().getItemMeta() != null && 
        	    player.getItemInHand().getItemMeta().getDisplayName() != null && 
        	    player.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(DarkWand.DarkWand.getItemMeta().getDisplayName()) && 
        	    event.getAction() == Action.LEFT_CLICK_AIR && 
        	    me.magicwands.events.DarkWand.DarkSpell.getOrDefault(player, 0) == 11) { 


	        UUID playerUUID = player.getUniqueId();
	        boolean hasAura = DarkAbyssPlayers.getOrDefault(playerUUID, false);

	        if (hasAura) {
	            DarkAbyssPlayers.put(playerUUID, false);
	            if (DarkAbyssTask.containsKey(playerUUID)) {
	                DarkAbyssTask.get(playerUUID).cancel();
	                DarkAbyssTask.remove(playerUUID);
	            }

	            String color = Main.getPlugin(Main.class).getConfig().getString("wands.dark.color");
	            player.sendMessage(ChatUtil.color("&7[&" + color + "Magic&7] &" + color + "Stopped the Dark Abyss.."));
	        } else {
	            DarkAbyssPlayers.put(playerUUID, true);
	            String color = Main.getPlugin(Main.class).getConfig().getString("wands.dark.color");
	            player.sendMessage(ChatUtil.color("&7[&" + color + "Magic&7] &" + color + "Started the Dark Abyss.."));

	            BukkitTask task = Bukkit.getScheduler().runTaskTimer(Main.getPlugin(Main.class), () -> {            	
	                DarkAbyssAura(player, player.getLocation().add(0, 1, 0), 10);

	                double radius = 9; 

	                for (Entity entity : player.getLocation().getWorld().getNearbyEntities(player.getLocation(), radius + 2, radius + 2, radius + 2)) {
	                    if (!(entity instanceof Player) || entity == player) continue;
	                    
	                    Player nearbyPlayer = (Player) entity;
	                    Location center = player.getLocation(); 
	                    Location playerLoc = nearbyPlayer.getLocation();

	                    double distance = playerLoc.distance(center);

	                   
	                    if (distance <= radius) {
	                        nearbyPlayer.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 2));
	                        nearbyPlayer.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 100, 2));
	                        nearbyPlayer.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 100, 2));
	                        nearbyPlayer.getWorld().spawnParticle(Particle.SMOKE_LARGE, nearbyPlayer.getLocation(), 10, 1.0, 1.0, 1.0, 0.003);
	                        nearbyPlayer.getWorld().spawnParticle(Particle.ASH, nearbyPlayer.getLocation(), 10, 1.0, 1.0, 1.0, 0.003);
	                        nearbyPlayer.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, nearbyPlayer.getLocation(), 40, 1.0, 1.0, 1.0, 0.1);
	                        nearbyPlayer.getWorld().spawnParticle(Particle.BLOCK_CRACK, nearbyPlayer.getLocation(), 20, 0.0, 0.0, 0.0, 0.05, Material.OBSIDIAN.createBlockData());
	                    } 
	                    
	                    else {
	                        Vector backvec = center.toVector().subtract(playerLoc.toVector()).normalize().multiply(1); 
	                        nearbyPlayer.setVelocity(backvec);
	                        nearbyPlayer.spawnParticle(Particle.REDSTONE, nearbyPlayer.getLocation(), 1, new Particle.DustOptions(Color.fromRGB(20, 20, 20), 1));
	                        nearbyPlayer.playSound(nearbyPlayer.getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 1, -3);
	                        nearbyPlayer.damage(8);
	                        nearbyPlayer.sendMessage(ChatColor.RED + "Hahah.. trying to leave? I will send cookie monster after you. (pls dont forget to chhange this)"); 
	                    }
	                }
	            }, 0L, 10L);

	            DarkAbyssTask.put(playerUUID, task);
	        }
	    }
	}


    @EventHandler
    public void RemoveAura(PlayerJoinEvent event) {
        Player p = event.getPlayer();
        DarkAbyssPlayers.remove(p.getUniqueId());  
    }
    
    @EventHandler
    public void RemoveAuraQuit(PlayerQuitEvent event) {
        Player p = event.getPlayer();

        UUID playerUUID = p.getUniqueId();
        if (DarkAbyssTask.containsKey(playerUUID)) {
            DarkAbyssTask.get(playerUUID).cancel();
            DarkAbyssTask.remove(playerUUID);
        }
    }

    public void DarkAbyssAura(Player player, Location location, int size) {
    	player.playSound(player.getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, (float) 0.1, -2);
    	player.playSound(player.getLocation(), Sound.BLOCK_SNOW_BREAK, 1, -2);
        for (int d = 0; d < 360; d += 10) {
            Location particleLoc = new Location(location.getWorld(), location.getX(), location.getY(), location.getZ());
            
            double offsetX = Math.cos(Math.toRadians(d)) * size;
            double offsetZ = Math.sin(Math.toRadians(d)) * size;
            
            particleLoc.setX(location.getX() + offsetX);
            particleLoc.setZ(location.getZ() + offsetZ);
            
            for (int i = 0; i < 3; i++) {
                double randomX = Math.random() * 0.5 - 0.25;
                double randomY = Math.random() * 0.5 - 0.25;
                double randomZ = Math.random() * 0.5 - 0.25;
                
                location.getWorld().spawnParticle(Particle.REDSTONE, particleLoc.add(randomX, randomY, randomZ), 1, new Particle.DustOptions(Color.fromRGB(20, 20, 20), 1));
                location.getWorld().spawnParticle(Particle.REDSTONE, particleLoc.add(randomX, randomY, randomZ), 1, new Particle.DustOptions(Color.fromRGB(60, 60, 60), 2));
                location.getWorld().spawnParticle(Particle.REDSTONE, particleLoc.add(randomX, randomY, randomZ), 1, new Particle.DustOptions(Color.fromRGB(90, 90, 90), 3));
            }

            location.getWorld().spawnParticle(Particle.SMOKE_LARGE, particleLoc, 1, 1.0, 1.0, 1.0, 0.003);
            location.getWorld().spawnParticle(Particle.ASH, particleLoc, 1, 1.0, 1.0, 1.0, 0.003);
            location.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, particleLoc, 4, 1.0, 1.0, 1.0, 0.1);
            location.getWorld().spawnParticle(Particle.BLOCK_CRACK, particleLoc, 2, 0.0, 0.0, 0.0, 0.05, Material.OBSIDIAN.createBlockData());
        }
    }


}