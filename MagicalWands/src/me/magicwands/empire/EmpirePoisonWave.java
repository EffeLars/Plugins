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
package me.magicwands.empire;

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
import me.magicwands.utils.ChatUtil;
import me.magicwands.utils.CooldownManager;
import me.magicwands.utils.ParticleColor;
import me.magicwands.utils.SpawnFirework;
import me.magicwands.wands.WandItems;

public class EmpirePoisonWave implements Listener {

    
	private CooldownManager cooldownManager;

	public EmpirePoisonWave() {
	    long cooldownTimeInSeconds = Main.getPlugin(Main.class).getConfig().getLong("spells.EmpirePoisonWave.cooldown", 30L); // default 30 seconds
	    this.cooldownManager = new CooldownManager(cooldownTimeInSeconds);
	    this.cooldownManager.setDefaultCooldownIfNotExist(cooldownTimeInSeconds, "EmpirePoisonWave");
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
	    Player player = event.getPlayer();
	    if (player.getItemInHand().getItemMeta() != null && 
	            player.getItemInHand().getItemMeta().getDisplayName() != null && 
	            player.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(WandItems.EmpireWand.getItemMeta().getDisplayName()) && 
	            event.getAction() == Action.LEFT_CLICK_AIR && 
	            me.magicwands.events.EmpireWand.EmpireSpell.getOrDefault(player, 0) == 7) {

	        boolean isStaff = player.hasPermission("magicwands.*");

	        
	        if (player.hasPermission("magicwands.empire.empirepoisonwave.bypasscooldown") || player.hasPermission("magicwands.empire.bypasscooldown") || isStaff) {
	        	LaunchEmpirePoisonWave(player); // Updated method name
	            return;
	        }

	        long cooldownTimeInMillis = cooldownManager.getCooldownDuration(player, "EmpirePoisonWave");
	        if (this.cooldownManager.hasCooldown(player, "EmpirePoisonWave")) {
	            long secondsRemaining = this.cooldownManager.getCooldownRemaining(player, "EmpirePoisonWave");
	            String color = Main.getPlugin(Main.class).getConfig().getString("wands.empire.color");
	            player.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&',
	                    ChatUtil.color("&7[&" + color + "MagicWands&7] &fThis spell is still on a &" + color + "cooldown&c&l! &fYou have to wait &" + color +
	                            secondsRemaining + " &fseconds")));
	            return;
	        }
	  
	        this.cooldownManager.startCooldown(player, cooldownTimeInMillis, "EmpirePoisonWave");
	        LaunchEmpirePoisonWave(player); 
	        }
	}


    private void LaunchEmpirePoisonWave(Player player) {


        Location initialLocation = player.getLocation().add(player.getLocation().getDirection().normalize().multiply(6));

       
        BukkitTask fireTrailTask = Bukkit.getScheduler().runTaskTimer(Main.getPlugin(Main.class), new Runnable() {
            Location currentLocation = initialLocation;
            Vector direction = currentLocation.getDirection().normalize().multiply(2.5); // Direction of the wave

            @Override
            public void run() {
                SpawnSparkWave(currentLocation);

                Block blockInFront = currentLocation.clone().add(direction).getBlock();
                Block blockAbove = currentLocation.clone().add(0, 1, 0).getBlock();

              
                if (!blockInFront.getType().isAir()) {
                  
                    direction = direction.multiply(-1);

           
                    if (blockAbove.getType().isAir()) {
                        direction = direction.multiply(-1);
                    }
                }

                currentLocation = currentLocation.add(direction);

                for (Entity entity : currentLocation.getWorld().getNearbyEntities(currentLocation, 1, 1, 1)) {
                    if (entity instanceof Player) {
                        Player nearbyPlayer = (Player) entity;
                        Location location = nearbyPlayer.getLocation();
                        nearbyPlayer.getWorld().spawnParticle(Particle.REDSTONE, location, 5, 1.0, 1.0, 1.0, 0.03, ParticleColor.PURPLE);
                        nearbyPlayer.getWorld().spawnParticle(Particle.REDSTONE, location, 5, 1.0, 1.0, 1.0, 0.03, ParticleColor.DARK_PURPLE);
                        nearbyPlayer.getWorld().spawnParticle(Particle.SMOKE_NORMAL, location, 20, 1.9, 1.9, 1.9, 0.01);
                        nearbyPlayer.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, location, 50, 0.9, 0.9, 0.9, 0.01);
                        nearbyPlayer.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 2));
                        nearbyPlayer.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 200, 2));
                        nearbyPlayer.getWorld().spawnParticle(Particle.BLOCK_CRACK, location, 20, 0.0, 0.0, 0.0, 0.03, Material.NETHER_PORTAL.createBlockData());
                    }
                }
            }
        }, 4L, 3L); 

        BukkitTask fireWaveSound = Bukkit.getScheduler().runTaskTimer(Main.getPlugin(Main.class), new Runnable() {
            Location currentLocation = player.getLocation();

            @Override
            public void run() {
                SpawnSparkWaveSound(currentLocation);
                currentLocation = getNextLocation(currentLocation);
            }
        }, 0L, 1L); 

        new BukkitRunnable() {
            @Override
            public void run() {
                fireTrailTask.cancel();
                fireWaveSound.cancel();
            }
        }.runTaskLater(Main.getPlugin(Main.class), 40L); 
    }

    private void SpawnSparkWave(Location location) {
        Color color = Color.GREEN;
        Color color2 = Color.fromRGB(0, 100, 0);
        FireworkEffect.Type type = FireworkEffect.Type.BURST;
        int power = 1;

        SpawnFirework.spawnFirework(location, color, type, power);
        SpawnFirework.spawnFirework(location, color2, type, power);
        location.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, location, 150, 3.0, 2.0, 1.0, 0.8);
        location.getWorld().spawnParticle(Particle.REDSTONE, location, 5, 1.0, 1.0, 1.0, 0.03, ParticleColor.PURPLE);
        location.getWorld().spawnParticle(Particle.REDSTONE, location, 5, 1.0, 1.0, 1.0, 0.03, ParticleColor.DARK_GREEN);
        location.getWorld().spawnParticle(Particle.REDSTONE, location, 5, 1.0, 1.0, 1.0, 0.03, ParticleColor.DARK_PURPLE);
        location.getWorld().spawnParticle(Particle.SMOKE_LARGE, location, 30, 1.9, 1.9, 1.9, 0.01);
        location.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, location, 50, 0.9, 0.9, 0.9, 0.01);
    }

    private void SpawnSparkWaveSound(Location location) {
        location.getWorld().playSound(location, Sound.ENTITY_ENDER_DRAGON_FLAP, 1, 1);
    }

    private Location getNextLocation(Location currentLocation) {
        Vector direction = currentLocation.getDirection().normalize().multiply(2.5);
        return currentLocation.add(direction);
    }

}