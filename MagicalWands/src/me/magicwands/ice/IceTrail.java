package me.magicwands.ice;

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
import me.magicwands.wands.IceWand;

public class IceTrail implements Listener {
	private Map<UUID, Boolean> playerIceTrailStatus = new HashMap<>();
	private Map<UUID, BukkitTask> iceTrailTasks = new HashMap<>();

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
	    Player player = event.getPlayer();
	    
	    if (player.getItemInHand().getItemMeta() != null && 
	        player.getItemInHand().getItemMeta().getDisplayName() != null && 
	        player.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(IceWand.IceWand.getItemMeta().getDisplayName()) && 
	        event.getAction() == Action.LEFT_CLICK_AIR && 
	        me.magicwands.events.IceWand.IceSpell.getOrDefault(player, 0) == 8) {

	        UUID playerUUID = player.getUniqueId();
	        boolean hasTrail = playerIceTrailStatus.getOrDefault(playerUUID, false);

	        if (hasTrail) {
	            playerIceTrailStatus.put(playerUUID, false);
	            if (iceTrailTasks.containsKey(playerUUID)) {
	                iceTrailTasks.get(playerUUID).cancel();
	                iceTrailTasks.remove(playerUUID);
	            }

	            String color = Main.getPlugin(Main.class).getConfig().getString("wands.ice.color");
	            player.sendMessage(ChatUtil.color("&7[&" + color + "Magic&7] &" + color + "Stopped the ice trail.."));
	        } else {
	            playerIceTrailStatus.put(playerUUID, true);
	            String color = Main.getPlugin(Main.class).getConfig().getString("wands.ice.color");
	            player.sendMessage(ChatUtil.color("&7[&" + color + "Magic&7] &" + color + "Started the ice trail.."));

	            BukkitTask task = Bukkit.getScheduler().runTaskTimer(Main.getPlugin(Main.class), () -> {
	                spawnIceTrailParticles(player);
	            }, 0L, 1L);

	            iceTrailTasks.put(playerUUID, task);
	        }
	    }
	}


    @EventHandler
    public void RemoveTrail(PlayerJoinEvent event) {
        Player p = event.getPlayer();
        playerIceTrailStatus.remove(p.getUniqueId());
    }
    
    @EventHandler
    public void RemoveTrail(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        
        if (playerIceTrailStatus.containsKey(player.getUniqueId()) && playerIceTrailStatus.get(player.getUniqueId())) {
        	playerIceTrailStatus.put(player.getUniqueId(), false);
        	if (iceTrailTasks.containsKey(player.getUniqueId())) {
                iceTrailTasks.get(player.getUniqueId()).cancel();
                iceTrailTasks.remove(player.getUniqueId());
            }
        }
    }

    private void spawnIceTrailParticles(Player player) {
        Particle.DustOptions iceShardColor = new Particle.DustOptions(Color.fromRGB(170, 255, 255), 1.0f);

        player.getWorld().spawnParticle(Particle.REDSTONE, player.getLocation(), 1, 1.0, 1.0, 1.0, 0.03, iceShardColor);
        player.getWorld().spawnParticle(Particle.BLOCK_CRACK, player.getLocation(), 2, 0.0, 0.0, 0.0, 0.03, Material.ICE.createBlockData());
        player.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, player.getLocation(), 5, 1.0, 1.0, 1.0, 0.01);

        for (int i = 0; i < 10; i++) {
            double offsetX = Math.random() * 0.6 - 0.3;
            double offsetY = Math.random() * 0.6 - 0.3;
            double offsetZ = Math.random() * 0.6 - 0.3;
            player.getWorld().spawnParticle(Particle.REDSTONE, player.getLocation().getX() + offsetX, player.getLocation().getY() + offsetY, player.getLocation().getZ() + offsetZ, 1, iceShardColor);
        }
    }
}
