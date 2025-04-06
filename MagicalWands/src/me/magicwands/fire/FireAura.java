
package me.magicwands.fire;

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
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import me.magicwands.events.FlameWand;
import me.magicwands.main.Main;
import me.magicwands.utils.ChatUtil;

public class FireAura implements Listener {
	private Map<UUID, BukkitTask> fireAuraTasks = new HashMap<>();
	private Map<UUID, Boolean> fireAuraPlayers = new HashMap<>();

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
	    Player player = event.getPlayer();
	    
	    if (player.getItemInHand().getItemMeta() != null && 
	        player.getItemInHand().getItemMeta().getDisplayName() != null && 
	        player.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(me.magicwands.wands.FireWand.IgnatiusWand.getItemMeta().getDisplayName()) && 
	        event.getAction() == Action.LEFT_CLICK_AIR && 
	        FlameWand.Spell.getOrDefault(player, 0) == 10) { 

	        UUID playerUUID = player.getUniqueId();
	        boolean hasAura = fireAuraPlayers.getOrDefault(playerUUID, false);  

	        if (hasAura) {
	            fireAuraPlayers.put(playerUUID, false);  

	            if (fireAuraTasks.containsKey(playerUUID)) {
	                fireAuraTasks.get(playerUUID).cancel();
	                fireAuraTasks.remove(playerUUID);
	            }

	            String color = Main.getPlugin(Main.class).getConfig().getString("wands.fire.color");
	            player.sendMessage(ChatUtil.color("&7[&" + color + "Magic&7] &" + color + "Stopped the fire aura.."));
	        } else {
	            fireAuraPlayers.put(playerUUID, true);  
	            String color = Main.getPlugin(Main.class).getConfig().getString("wands.fire.color");
	            player.sendMessage(ChatUtil.color("&7[&" + color + "Magic&7] &" + color + "Started the fire aura.."));

	            BukkitTask task = Bukkit.getScheduler().runTaskTimer(Main.getPlugin(Main.class), () -> {
	                SummonFireAura(player.getLocation(), 4);
	                for (Entity entity : player.getLocation().getWorld().getNearbyEntities(player.getLocation(), 4, 4, 4)) {
	                    if (!(entity instanceof Player) || entity == player) continue;
	                    Player nearbyPlayer = (Player) entity;
	                    nearbyPlayer.setFireTicks(300);
	                    Vector direction = nearbyPlayer.getLocation().getDirection().normalize().multiply(-1);
	                    nearbyPlayer.setVelocity(direction.multiply(2));
	                    nearbyPlayer.playSound(nearbyPlayer.getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 1, 1);
	                }
	            }, 0L, 10L);

	            fireAuraTasks.put(playerUUID, task);
	        }
	    }
	}


    @EventHandler
    public void RemoveAura(PlayerJoinEvent event) {
        Player p = event.getPlayer();
        fireAuraPlayers.remove(p.getUniqueId());  
    }
    
    @EventHandler
    public void RemoveAuraQuit(PlayerQuitEvent event) {
        Player p = event.getPlayer();
        UUID playerUUID = p.getUniqueId();
      
        if (fireAuraTasks.containsKey(playerUUID)) {
            fireAuraTasks.get(playerUUID).cancel();
            fireAuraTasks.remove(playerUUID);
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
