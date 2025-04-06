
package me.magicwands.witch;

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

import me.magicwands.main.Main;
import me.magicwands.utils.ChatUtil;



	public class WitchTrail implements Listener {
	    private final Map<UUID, BukkitTask> witchTrailTasks = new HashMap<>();
	    private final Map<UUID, Boolean> witchTrailStatus = new HashMap<>();

	    @EventHandler
	    public void onPlayerInteract(PlayerInteractEvent event) {
	        Player player = event.getPlayer();

	        if (player.getItemInHand().getItemMeta() != null && 
	            player.getItemInHand().getItemMeta().getDisplayName() != null && 
	            player.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(me.magicwands.wands.WitchWand.WitchWand.getItemMeta().getDisplayName()) && 
	            event.getAction() == Action.LEFT_CLICK_AIR && 
	            me.magicwands.events.WitchWand.WitchSpell.getOrDefault(player, 0) == 6) { 

	            UUID playerUUID = player.getUniqueId();
	            boolean isActive = witchTrailStatus.getOrDefault(playerUUID, false);
	            String color = Main.getPlugin(Main.class).getConfig().getString("wands.witch.color");

	            if (isActive) {
	                witchTrailStatus.put(playerUUID, false);

	                if (witchTrailTasks.containsKey(playerUUID)) {
	                    witchTrailTasks.get(playerUUID).cancel();
	                    witchTrailTasks.remove(playerUUID);
	                }

	                player.sendMessage(ChatUtil.color("&7[&" + color + "Magic&7] &" + color + "Stopped the witch trail.."));
	            } else {
	                witchTrailStatus.put(playerUUID, true);
	                player.sendMessage(ChatUtil.color("&7[&" + color + "Magic&7] &" + color + "Started the witch trail.."));

	                BukkitTask task = Bukkit.getScheduler().runTaskTimer(Main.getPlugin(Main.class), new Runnable() {
	                    @Override
	                    public void run() {
	                        spawnWitchTrailParticles(player);
	                    }
	                }, 0L, 1L);

	                witchTrailTasks.put(playerUUID, task);
	            }
	        }
	    }
	

    @EventHandler
    public void RemoveTrail(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (witchTrailStatus.containsKey(player.getUniqueId()) && witchTrailStatus.get(player.getUniqueId())) {
            witchTrailStatus.put(player.getUniqueId(), false);
            if (witchTrailTasks.containsKey(player.getUniqueId())) {
                witchTrailTasks.get(player.getUniqueId()).cancel();
                witchTrailTasks.remove(player.getUniqueId());
            }

        }
    }

    @EventHandler
    public void RemoveTrail(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        if (witchTrailStatus.containsKey(player.getUniqueId()) && witchTrailStatus.get(player.getUniqueId())) {
            witchTrailStatus.put(player.getUniqueId(), false);
            if (witchTrailTasks.containsKey(player.getUniqueId())) {
                witchTrailTasks.get(player.getUniqueId()).cancel();
                witchTrailTasks.remove(player.getUniqueId());
            }
        }
    }

    private void spawnWitchTrailParticles(Player player) {
        if (player.isOnGround()) {
            player.getWorld().spawnParticle(Particle.SPELL_WITCH, player.getLocation(), 3, 0.5, 0.5, 0.5, 0.005);
            player.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, player.getLocation(), 8, 0.5, 0.0, 0.5, 0.04);
            player.getWorld().spawnParticle(Particle.PORTAL, player.getLocation(), 5, 0.5, 0.5, 0.5, 0.02);
            player.getWorld().spawnParticle(Particle.DRAGON_BREATH, player.getLocation(), 2, 0.5, -0.5, 0.5, 0.005);
        } else {
            player.getWorld().spawnParticle(Particle.SPELL_WITCH, player.getLocation(), 3, 0.5, 0.5, 0.5, 0.005);
            player.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, player.getLocation(), 8, 0.5, 0.0, 0.5, 0.04);
            player.getWorld().spawnParticle(Particle.PORTAL, player.getLocation(), 5, 0.5, 0.5, 0.5, 0.02);
            player.getWorld().spawnParticle(Particle.DRAGON_BREATH, player.getLocation(), 2, 0.5, 0.5, 0.5, 0.005);
        }
    }
}
