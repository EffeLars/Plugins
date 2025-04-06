package me.magicwands.ice;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import me.magicwands.main.Main;
import me.magicwands.utils.SpawnFirework;

public class IceSparkWave implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (player.getItemInHand().getItemMeta() != null && 
        	    player.getItemInHand().getItemMeta().getDisplayName() != null && 
        	    player.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(me.magicwands.wands.IceWand.IceWand.getItemMeta().getDisplayName()) && 
        	    event.getAction() == Action.LEFT_CLICK_AIR && 
        	    me.magicwands.events.IceWand.IceSpell.getOrDefault(player, 0) == 7) {

            Location initialLocation = player.getLocation().add(player.getLocation().getDirection().normalize().multiply(6));

            BukkitTask fireTrailTask = Bukkit.getScheduler().runTaskTimer(Main.getPlugin(Main.class), new Runnable() {
                Location currentLocation = initialLocation;
                Vector direction = currentLocation.getDirection().normalize().multiply(2.5);

                @Override
                public void run() {
                    Block blockInFront = currentLocation.clone().add(direction).getBlock();
                    Block blockAbove = currentLocation.clone().add(0, 1, 0).getBlock();

                    if (!blockInFront.getType().isAir()) {
                        player.getWorld().spawnParticle(Particle.CLOUD, blockInFront.getLocation(), 20, 0.9, 0.9, 0.9, 0.001);
                        player.getWorld().spawnParticle(Particle.END_ROD, blockInFront.getLocation(), 20, 0.9, 0.9, 0.9, 0.001);

                        direction = direction.multiply(-1);

                        if (blockAbove.getType().isAir()) {
                        	direction = direction.multiply(-1);
                        }
                    }


                    currentLocation = currentLocation.add(direction);
                    SpawnSparkWave(currentLocation);

                    for (Entity entity : currentLocation.getWorld().getNearbyEntities(currentLocation, 1, 1, 1)) {
                        if (entity instanceof Player) {
                            Player nearbyPlayer = (Player) entity;

                            Particle.DustOptions iceShardColor = new Particle.DustOptions(Color.fromRGB(170, 255, 255), 1.0f);
                            Particle.DustOptions cyanColor = new Particle.DustOptions(Color.AQUA, 1.0f);
                            Particle.DustOptions blueColor = new Particle.DustOptions(Color.fromRGB(0, 0, 255), 1.0f);
                            Location location = nearbyPlayer.getLocation();
                            nearbyPlayer.getWorld().spawnParticle(Particle.REDSTONE, location, 5, 1.0, 1.0, 1.0, 0.03, iceShardColor);
                            nearbyPlayer.playSound(location, Sound.BLOCK_FIRE_EXTINGUISH, 1, -3);
                            nearbyPlayer.getWorld().spawnParticle(Particle.REDSTONE, location, 5, 1.0, 1.0, 1.0, 0.03, cyanColor);
                            nearbyPlayer.getWorld().spawnParticle(Particle.REDSTONE, location, 5, 1.0, 1.0, 1.0, 0.03, blueColor);
                            nearbyPlayer.getWorld().spawnParticle(Particle.CLOUD, location, 20, 0.9, 0.9, 0.9, 0.001);
                            nearbyPlayer.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, location, 50, 0.9, 0.9, 0.9, 0.01);
                            nearbyPlayer.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 2));
                            nearbyPlayer.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 100, 2));
                            nearbyPlayer.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 700, 2));
                            nearbyPlayer.getWorld().spawnParticle(Particle.BLOCK_CRACK, location, 20, 0.0, 0.0, 0.0, 0.03, Material.ICE.createBlockData());
                        }
                    }
                }
            }, 4L, 5L); 

            new BukkitRunnable() {
                @Override
                public void run() {
                    fireTrailTask.cancel();
                }
            }.runTaskLater(Main.getPlugin(Main.class), 60L);
        }
    }


    private void SpawnSparkWave(Location location) {
        SpawnFirework.spawnFirework(location, Color.fromRGB(0, 255, 255), FireworkEffect.Type.BURST, 1); // Light Cyan
        SpawnFirework.spawnFirework(location, Color.fromRGB(170, 255, 255), FireworkEffect.Type.STAR, 2); // Icy Blue
        location.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, location, 150, 3.0, 2.0, 1.0, 0.8);
        location.getWorld().spawnParticle(Particle.CLOUD, location, 50, 1.0, 1.0, 1.0, 0.03);
        Particle.DustOptions iceShardColor = new Particle.DustOptions(Color.fromRGB(170, 255, 255), 1.0f);
        location.getWorld().spawnParticle(Particle.REDSTONE, location, 5, 1.0, 1.0, 1.0, 0.03, iceShardColor);
        location.getWorld().playSound(location, Sound.BLOCK_FIRE_EXTINGUISH, 1, -3);
    }
}
