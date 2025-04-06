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

import org.bukkit.Bukkit;
import org.bukkit.Color;
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
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import me.magicwands.main.Main;
import me.magicwands.wands.LightWand;

public class LightVortex implements Listener {
// USE THE SAME PARTICLES AS LIGHTSWORD TO SUCK YOU IN (ATLEAST SOMEONE IS GETTING SUCKED)
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (player.getItemInHand().getItemMeta() != null && 
        	    player.getItemInHand().getItemMeta().getDisplayName() != null && 
        	    player.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(LightWand.LightWand.getItemMeta().getDisplayName()) && 
        	    event.getAction() == Action.LEFT_CLICK_AIR && 
        	    me.magicwands.events.LightWand.LightSpell.getOrDefault(player, 0) == 9) { 

        	  String color = Main.getPlugin(Main.class).getConfig().getString("wands.light.color");

              Location initialLocation = player.getLocation().add(player.getLocation().getDirection().normalize().multiply(2));

              BukkitTask lightWaveTask = Bukkit.getScheduler().runTaskTimer(Main.getPlugin(Main.class), new Runnable() {
                  Location currentLocation = initialLocation;
                  Vector direction = currentLocation.getDirection().normalize().multiply(0.5);

                  @Override
                  public void run() {
                      Block blockInFront = currentLocation.clone().add(direction).getBlock();
                      Block blockAbove = currentLocation.clone().add(0, 1, 0).getBlock();

                     
                      Vector updatedDirection = direction.clone();

                      
                      if (!blockInFront.getType().isAir()) {
                          updatedDirection = updatedDirection.multiply(-1);  

                          Material[] materials = {Material.GRASS, Material.FERN, Material.LARGE_FERN, Material.TALL_GRASS};
                          if (blockAbove.getType().isAir() || blockAbove.getType().equals(materials)) {
                              updatedDirection = new Vector(0, 1, 0); 
                          }
                      }

                      currentLocation = currentLocation.add(updatedDirection);  


                      currentLocation = currentLocation.add(direction);
                      spawnCelestialWave(currentLocation);

                      for (Entity entity : currentLocation.getWorld().getNearbyEntities(currentLocation, 1, 1, 1)) {
                          if (entity instanceof Player) {
                              Player nearbyPlayer = (Player) entity;
                              applyEffectsToNearbyPlayer(nearbyPlayer, player);

                          }
                      }
                  }
              }, 0L, 1L);

              BukkitTask lightWaveSoundTask = Bukkit.getScheduler().runTaskTimer(Main.getPlugin(Main.class), new Runnable() {
                  Location currentLocation = player.getLocation();

                  @Override
                  public void run() {
                      spawnFireWaveSound(currentLocation);
                      currentLocation = getNextLocation(currentLocation);
                  }
              }, 0L, 5L);

              new BukkitRunnable() {
                  @Override
                  public void run() {
                      lightWaveTask.cancel();
                      lightWaveSoundTask.cancel();
                  }
              }.runTaskLater(Main.getPlugin(Main.class), 60L);
          }
      }

    private void spawnCelestialWave(Location location) {    	
    	location.getWorld().spawnParticle(Particle.REDSTONE, location, 34, 1, 1, 1, 0.01, new Particle.DustOptions(Color.WHITE, 1.5f));
    	location.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, location, 35, 1, 1, 1, 0.01);
    	location.getWorld().spawnParticle(Particle.END_ROD, location, 36, 1, 1, 1, 0.02);
                  
    }







    private void applyEffectsToNearbyPlayer(Player nearbyPlayer, Player caster) {
    	Location playerLocation = nearbyPlayer.getLocation();
    	Location casterLocation = caster.getLocation();
        Vector toCaster = casterLocation.toVector().subtract(playerLocation.toVector()).normalize();
        Vector pullVelocity = toCaster.multiply(2.5); 
        nearbyPlayer.setVelocity(pullVelocity);
        nearbyPlayer.getWorld().spawnParticle(Particle.REDSTONE, playerLocation, 5, 1.0, 1.0, 1.0, 0.03, new Particle.DustOptions(Color.WHITE, 1.0f));
        nearbyPlayer.getWorld().spawnParticle(Particle.REDSTONE, playerLocation, 5, 1.0, 1.0, 1.0, 0.03, new Particle.DustOptions(Color.GRAY, 1.0f));
        nearbyPlayer.getWorld().spawnParticle(Particle.CLOUD, playerLocation, 20, 0.9, 0.9, 0.9, 0.01);
        nearbyPlayer.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, playerLocation, 50, 0.9, 0.9, 0.9, 0.01);
        nearbyPlayer.getWorld().spawnParticle(Particle.BLOCK_CRACK, playerLocation, 20, 0.0, 0.0, 0.0, 0.03, Material.QUARTZ_BLOCK.createBlockData());
    }

      private void spawnFireWaveSound(Location location) {
          location.getWorld().playSound(location, Sound.ENTITY_ENDER_DRAGON_FLAP, 1, 1);
          location.getWorld().playSound(location, Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1, 1);
          location.getWorld().playSound(location, Sound.BLOCK_NOTE_BLOCK_CHIME, (float) 0.5, 1);
      }

      private Location getNextLocation(Location currentLocation) {
          Vector direction = currentLocation.getDirection().normalize().multiply(0.5);
          return currentLocation.add(direction);
      }
  }
