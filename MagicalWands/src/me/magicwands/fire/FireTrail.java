
package me.magicwands.fire;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitTask;

import me.magicwands.events.FlameWand;
import me.magicwands.main.Main;
import me.magicwands.utils.ChatUtil;

public class FireTrail implements Listener {
    
	private Map<UUID, Boolean> fireTrailStatus = new HashMap<>();
	private Map<UUID, BukkitTask> fireTrailTasks = new HashMap<>();

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
	    Player player = event.getPlayer();

	    if (player.getItemInHand().getItemMeta() != null && 
	        player.getItemInHand().getItemMeta().getDisplayName() != null && 
	        player.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(me.magicwands.wands.FireWand.IgnatiusWand.getItemMeta().getDisplayName()) && 
	        event.getAction() == Action.LEFT_CLICK_AIR && 
	        FlameWand.Spell.getOrDefault(player, 0) == 5) { 

	        UUID playerUUID = player.getUniqueId();
	        boolean hasTrail = fireTrailStatus.getOrDefault(playerUUID, false);

	        if (hasTrail) {
	            fireTrailStatus.put(playerUUID, false);
	            if (fireTrailTasks.containsKey(playerUUID)) {
	                fireTrailTasks.get(playerUUID).cancel();
	                fireTrailTasks.remove(playerUUID);
	            }

	            String color = Main.getPlugin(Main.class).getConfig().getString("wands.fire.color");
	            player.sendMessage(ChatUtil.color("&7[&" + color + "Magic&7] &" + color + "Stopped the fire trail.."));
	        } else {
	            fireTrailStatus.put(playerUUID, true);
	            String color = Main.getPlugin(Main.class).getConfig().getString("wands.fire.color");
	            player.sendMessage(ChatUtil.color("&7[&" + color + "Magic&7] &" + color + "Started the fire trail.."));

	            BukkitTask task = Bukkit.getScheduler().runTaskTimer(Main.getPlugin(Main.class), () -> {
	                spawnFireTrailParticles(player);
	            }, 0L, 1L);

	            fireTrailTasks.put(playerUUID, task);
	        }
	    }
	}

    @EventHandler
    public void RemoveTrail(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        
        if (fireTrailStatus.containsKey(player.getUniqueId()) && fireTrailStatus.get(player.getUniqueId())) {
            fireTrailStatus.put(player.getUniqueId(), false);
            UUID playerUUID = player.getUniqueId();
            if (fireTrailTasks.containsKey(playerUUID)) {
                fireTrailTasks.get(playerUUID).cancel();
                fireTrailTasks.remove(playerUUID);
            }
        }
    }
    
    @EventHandler
    public void RemoveTrail(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        
        if (fireTrailStatus.containsKey(player.getUniqueId()) && fireTrailStatus.get(player.getUniqueId())) {
            fireTrailStatus.put(player.getUniqueId(), false);
            UUID playerUUID = player.getUniqueId();
            if (fireTrailTasks.containsKey(playerUUID)) {
                fireTrailTasks.get(playerUUID).cancel();
                fireTrailTasks.remove(playerUUID);
            }
        }
    }
    
    private void spawnFireTrailParticles(Player player) {
        if (player.isOnGround()) {
            player.getWorld().spawnParticle(Particle.WHITE_ASH, player.getLocation(), 5, 0.5, 0.5, 0.5, 0.0);
            player.getWorld().spawnParticle(Particle.FLAME, player.getLocation(), 1, 0.5, 0.0, 0.5, 0.03);
            player.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, player.getLocation(), 5, 0.5, 0.0, 0.5, 0.02);
            player.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, player.getLocation(), 1, 0.5, 0.0, 0.5, 0.0);
        } else {
            player.getWorld().spawnParticle(Particle.WHITE_ASH, player.getLocation(), 5, 0.5, 0.5, 0.5, 0.0);
            player.getWorld().spawnParticle(Particle.FLAME, player.getLocation(), 1, 0.5, 0.0, 0.5, 0.03);
            player.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, player.getLocation(), 5, 0.5, 0.0, 0.5, 0.02);
            player.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, player.getLocation(), 1, 0.5, 0.0, 0.5, 0.0);
            player.getWorld().spawnParticle(Particle.SMOKE_LARGE, player.getLocation(), 1, 0.5, 0.0, 0.5, 0.003);
            player.getWorld().spawnParticle(Particle.SMOKE_NORMAL, player.getLocation(), 1, 0.5, 0.0, 0.5, 0.003);
        }
    }
}
