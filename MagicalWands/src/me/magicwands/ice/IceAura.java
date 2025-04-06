
package me.magicwands.ice;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
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
import me.magicwands.wands.IceWand;

public class IceAura implements Listener {
	private Map<UUID, Boolean> iceAuraPlayers = new HashMap<>();
	private Map<UUID, BukkitTask> iceAuraTasks = new HashMap<>();

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
	    Player player = event.getPlayer();
	    
	    if (player.getItemInHand().getItemMeta() != null && 
	        player.getItemInHand().getItemMeta().getDisplayName() != null && 
	        player.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(IceWand.IceWand.getItemMeta().getDisplayName()) && 
	        event.getAction() == Action.LEFT_CLICK_AIR && 
	        me.magicwands.events.IceWand.IceSpell.getOrDefault(player, 0) == 9) {

	        UUID playerUUID = player.getUniqueId();
	        boolean hasAura = iceAuraPlayers.getOrDefault(playerUUID, false);

	        if (hasAura) {
	            iceAuraPlayers.put(playerUUID, false);
	            if (iceAuraTasks.containsKey(playerUUID)) {
	                iceAuraTasks.get(playerUUID).cancel();
	                iceAuraTasks.remove(playerUUID);
	            }

	            String color = Main.getPlugin(Main.class).getConfig().getString("wands.ice.color");
	            player.sendMessage(ChatUtil.color("&7[&" + color + "Magic&7] &" + color + "Stopped the ice aura.."));
	        } else {
	            iceAuraPlayers.put(playerUUID, true);
	            String color = Main.getPlugin(Main.class).getConfig().getString("wands.ice.color");
	            player.sendMessage(ChatUtil.color("&7[&" + color + "Magic&7] &" + color + "Started the ice aura.."));

	            BukkitTask task = Bukkit.getScheduler().runTaskTimer(Main.getPlugin(Main.class), () -> {
	                SummonIceAura(player, player.getLocation().add(0, 1, 0), 4);
	                for (Entity entity : player.getLocation().getWorld().getNearbyEntities(player.getLocation(), 4, 4, 4)) {
	                    if (!(entity instanceof Player) || entity == player) continue;
	                    Player nearbyPlayer = (Player) entity;
	                    nearbyPlayer.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 2));
	                    nearbyPlayer.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 100, 2));
	                    nearbyPlayer.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 700, 2));
	                    nearbyPlayer.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 300, 2));
	                    Vector direction = nearbyPlayer.getLocation().getDirection().normalize().multiply(-1);
	                    nearbyPlayer.setVelocity(direction.multiply(2));
	                    nearbyPlayer.playSound(nearbyPlayer.getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 1, -3);
	                }
	            }, 0L, 10L);

	            iceAuraTasks.put(playerUUID, task);
	        }
	    }
	}


    @EventHandler
    public void RemoveAura(PlayerJoinEvent event) {
        Player p = event.getPlayer();
        iceAuraPlayers.remove(p.getUniqueId());  
    }
    
    @EventHandler
    public void RemoveAuraQuit(PlayerQuitEvent event) {
        Player p = event.getPlayer();

        UUID playerUUID = p.getUniqueId();
        if (iceAuraTasks.containsKey(playerUUID)) {
            iceAuraTasks.get(playerUUID).cancel();
            iceAuraTasks.remove(playerUUID);
        }
    }

    public void SummonIceAura(Player player, Location location, int size) {
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
                
                location.getWorld().spawnParticle(Particle.REDSTONE, particleLoc.add(randomX, randomY, randomZ), 1, new Particle.DustOptions(Color.fromRGB(173, 216, 230), 1));
                location.getWorld().spawnParticle(Particle.REDSTONE, particleLoc.add(randomX, randomY, randomZ), 1, new Particle.DustOptions(Color.fromRGB(0, 255, 255), 1));
                location.getWorld().spawnParticle(Particle.REDSTONE, particleLoc.add(randomX, randomY, randomZ), 1, new Particle.DustOptions(Color.WHITE, 1));
            }

            location.getWorld().spawnParticle(Particle.CLOUD, particleLoc, 1, 1.0, 1.0, 1.0, 0.003);
            location.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, particleLoc, 4, 1.0, 1.0, 1.0, 0.1);
            location.getWorld().spawnParticle(Particle.BLOCK_CRACK, particleLoc, 2, 0.0, 0.0, 0.0, 0.05, Material.ICE.createBlockData());
        }
    }


}