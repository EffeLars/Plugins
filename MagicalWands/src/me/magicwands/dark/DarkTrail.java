
package me.magicwands.dark;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitTask;

import me.magicwands.events.DarkWand;
import me.magicwands.main.Main;
import me.magicwands.utils.ChatUtil;

public class DarkTrail implements Listener {

	private Map<UUID, Boolean> darkTrailStatus = new HashMap<>();
	private Map<UUID, BukkitTask> darkTrailTasks = new HashMap<>();

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
	    Player player = event.getPlayer();

	    if (player.getItemInHand().getItemMeta() != null &&
	        player.getItemInHand().getItemMeta().getDisplayName() != null &&
	        player.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(me.magicwands.wands.DarkWand.DarkWand.getItemMeta().getDisplayName()) &&
	        event.getAction() == Action.LEFT_CLICK_AIR && DarkWand.DarkSpell.get(player) == 5) {

	        UUID playerUUID = player.getUniqueId();
	        boolean hasTrail = darkTrailStatus.getOrDefault(playerUUID, false);

	        if (hasTrail) {
	            darkTrailStatus.put(playerUUID, false);
	            if (darkTrailTasks.containsKey(playerUUID)) {
	                darkTrailTasks.get(playerUUID).cancel();
	                darkTrailTasks.remove(playerUUID);
	            }

	            String color = Main.getPlugin(Main.class).getConfig().getString("wands.dark.color");
	            player.sendMessage(ChatUtil.color("&7[&" + color + "Magic&7] &" + color + "Stopped the dark trail.."));
	        } else {
	            darkTrailStatus.put(playerUUID, true);
	            String color = Main.getPlugin(Main.class).getConfig().getString("wands.dark.color");
	            player.sendMessage(ChatUtil.color("&7[&" + color + "Magic&7] &" + color + "Started the dark trail.."));

	            BukkitTask task = Bukkit.getScheduler().runTaskTimer(Main.getPlugin(Main.class), () -> {
	                spawnDarkTrailParticles(player);
	            }, 0L, 1L);

	            darkTrailTasks.put(playerUUID, task);
	        }
	    }
	}

    @EventHandler
    public void RemoveTrail(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (darkTrailStatus.containsKey(player.getUniqueId()) && darkTrailStatus.get(player.getUniqueId())) {
            darkTrailStatus.put(player.getUniqueId(), false);
            if (darkTrailTasks.containsKey(player.getUniqueId())) {
                darkTrailTasks.get(player.getUniqueId()).cancel();
                darkTrailTasks.remove(player.getUniqueId());
            }
        }
    }

    @EventHandler
    public void RemoveTrail(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        if (darkTrailStatus.containsKey(player.getUniqueId()) && darkTrailStatus.get(player.getUniqueId())) {
            darkTrailStatus.put(player.getUniqueId(), false);
            if (darkTrailTasks.containsKey(player.getUniqueId())) {
                darkTrailTasks.get(player.getUniqueId()).cancel();
                darkTrailTasks.remove(player.getUniqueId());
            }
        }
    }

    private void spawnDarkTrailParticles(Player player) {

        if (player.isOnGround()) {
            player.getWorld().spawnParticle(Particle.SMOKE_LARGE, player.getLocation(), 1, 0.5, 0.5, 0.5, 0.0);
            player.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, player.getLocation(), 5, 0.5, 0.0, 0.5, 0.02);
            player.getWorld().spawnParticle(Particle.ASH, player.getLocation(), 1, 0.5, 0.0, 0.5, 0.0);
            player.getWorld().spawnParticle(Particle.SMOKE_NORMAL, player.getLocation(), 5, 0.5, 0.0, 0.5, 0.0);
        } else {
            Location loc = player.getLocation().clone().add(0, -0.2, 0); 

            for (int i = 0; i < 15; i++) { 
                double x = (Math.random() - 0.5) * 1.4; 
                double z = (Math.random() - 0.5) * 1.4;
                double y = (Math.random() - 0.3) * 0.3; 

                Location particleLoc = loc.clone().add(x, y, z);
                float size = 1.5f + (float) Math.random() * 0.3F; 

                player.getWorld().spawnParticle(Particle.REDSTONE, particleLoc, 1, new Particle.DustOptions(Color.fromRGB(30, 30, 30), size));
                player.getWorld().spawnParticle(Particle.REDSTONE, particleLoc, 1, new Particle.DustOptions(Color.fromRGB(50, 50, 50), size));
            }

            for (int i = 0; i < 8; i++) { 
                double x = (Math.random() - 0.5) * 1.0;
                double z = (Math.random() - 0.5) * 1.0;
                double y = (Math.random() - 0.2) * 0.2;

                Location particleLoc = loc.clone().add(x, y, z);
                float size = 1.2f + (float) Math.random() * 0.3f;

                player.getWorld().spawnParticle(Particle.REDSTONE, particleLoc, 1, new Particle.DustOptions(Color.fromRGB(139, 0, 0), size));
                player.getWorld().spawnParticle(Particle.REDSTONE, particleLoc, 1, new Particle.DustOptions(Color.fromRGB(150, 30, 30), size));
            }
            
            for (int i = 0; i < 15; i++) { 
                double x = (Math.random() - 0.5) * 0.6; 
                double z = (Math.random() - 0.5) * 0.6; 
                double y = (Math.random() - 0.2) * 0.15; 

                Location particleLoc = loc.clone().add(x, -0.3, z);
                float size = 1.4f + (float) Math.random() * 0.5f; 

                player.getWorld().spawnParticle(Particle.REDSTONE, particleLoc, 1, new Particle.DustOptions(Color.fromRGB(130, 30, 30), size));
                player.getWorld().spawnParticle(Particle.REDSTONE, particleLoc, 1, new Particle.DustOptions(Color.fromRGB(150, 50, 50), size));
                player.getWorld().spawnParticle(Particle.REDSTONE, particleLoc, 1, new Particle.DustOptions(Color.fromRGB(39, 0, 0), size));
                player.getWorld().spawnParticle(Particle.REDSTONE, particleLoc, 1, new Particle.DustOptions(Color.fromRGB(50, 30, 30), size));
            }


            player.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, loc, 8, 0.4, 0.1, 0.4, 0.002);
            player.getWorld().spawnParticle(Particle.SMOKE_NORMAL, loc, 5, 0.5, 0.2, 0.5, 0.01);
            player.getWorld().spawnParticle(Particle.ASH, loc, 6, 0.5, 0.2, 0.5, 0.005);
        }
    }

}