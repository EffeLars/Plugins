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

import java.util.Random;

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
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import me.magicwands.main.Main;
import me.magicwands.utils.ChatUtil;
import me.magicwands.utils.CooldownManager;
import me.magicwands.utils.ParticleColor;
import me.magicwands.utils.SpawnFirework;
import me.magicwands.wands.WandItems;

public class EmpireExplosiveWave implements Listener {

    
	private CooldownManager cooldownManager;

	public EmpireExplosiveWave() {
	    long cooldownTimeInSeconds = Main.getPlugin(Main.class).getConfig().getLong("spells.EmpireExplosiveWave.cooldown", 30L); // default 30 seconds
	    this.cooldownManager = new CooldownManager(cooldownTimeInSeconds);
	    this.cooldownManager.setDefaultCooldownIfNotExist(cooldownTimeInSeconds, "EmpireExplosiveWave");
	}

	
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
	    Player player = event.getPlayer();
	    if (player.getItemInHand().getItemMeta() != null && 
                player.getItemInHand().getItemMeta().getDisplayName() != null && 
                player.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(WandItems.EmpireWand.getItemMeta().getDisplayName()) && 
                event.getAction() == Action.LEFT_CLICK_AIR && 
                me.magicwands.events.EmpireWand.EmpireSpell.getOrDefault(player, 0) == 12) { 
    

	        boolean isStaff = player.hasPermission("magicwands.*");

	        if (player.hasPermission("magicwands.empire.empireexplosivewave.bypasscooldown") || player.hasPermission("magicwands.empire.bypasscooldown") || isStaff) {
	            launchEmpireExplosiveWave(player);
	            return;
	        }

	        
	        long cooldownTimeInMillis = cooldownManager.getCooldownDuration(player, "EmpireExplosiveWave");
	        if (this.cooldownManager.hasCooldown(player, "EmpireExplosiveWave")) {
	            long secondsRemaining = this.cooldownManager.getCooldownRemaining(player, "EmpireExplosiveWave");
	            String color = Main.getPlugin(Main.class).getConfig().getString("wands.empire.color");
	            player.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&',
	                    ChatUtil.color("&7[&" + color + "MagicWands&7] &fThis spell is still on a &" + color + "cooldown&c&l! &fYou have to wait &" + color +
	                            secondsRemaining + " &fseconds")));
	            return;
	        }
	        
	        this.cooldownManager.startCooldown(player, cooldownTimeInMillis, "EmpireExplosiveWave");
	        launchEmpireExplosiveWave(player);
	    }
	}

    
    public static double getRandomDouble(double min, double max) {
        Random ran = new Random();
        return min + (max - min) * ran.nextDouble();
    }
    
    public void SpawnExplosion(Location location) {
        location.getWorld().createExplosion(location, 3);
        location.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, location, 150, 3.0, 2.0, 1.0, 0.8);
        location.getWorld().spawnParticle(Particle.PORTAL, location, 50, 1.0, 1.0, 1.0, 0.03);
        Block blockAtExplosion = null;
        int radius = 3;

        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    Block tempBlock = location.clone().add(x, y, z).getBlock();
                    if (!tempBlock.getType().isAir()) {
                        blockAtExplosion = tempBlock;
                        break;
                    }
                }
                if (blockAtExplosion != null) break;
            }
            if (blockAtExplosion != null) break;
        }
        if (blockAtExplosion != null) {
            Material blockname = blockAtExplosion.getType();
            if (blockname != Material.BEDROCK && blockname != Material.OBSIDIAN && blockAtExplosion.getType().isSolid()) {
                for (int i = 0; i < 5; i++) {
                    location.getWorld().spawnFallingBlock(location, blockname, (byte) 0).setVelocity(new Vector(
                            getRandomDouble(-0.2, 0.5),
                            getRandomDouble(0.4, 1.5),
                            getRandomDouble(-0.2, 0.5)
                    ));
                }
            }
        } else {
            Material fallbackBlock = Material.DIRT;
            //
        
        }
    }



    
    private void launchEmpireExplosiveWave(Player player) {

        Location initialLocation = player.getLocation().add(player.getLocation().getDirection().normalize().multiply(6));

        BukkitTask EmpireExplosiveWaveTask = Bukkit.getScheduler().runTaskTimer(Main.getPlugin(Main.class), new Runnable() {
            Location currentLocation = initialLocation;
            Vector direction = currentLocation.getDirection().normalize().multiply(2.5); // Direction of the wave

            @Override
            public void run() {
                SpawnExplosiveWaveEffect(currentLocation);
                SpawnExplosion(currentLocation);

                Block blockInFront = currentLocation.clone().add(direction).getBlock();

               
                if (!blockInFront.getType().isAir()) {
                    return; 
                }

                currentLocation = currentLocation.add(direction);

                for (Entity entity : currentLocation.getWorld().getNearbyEntities(currentLocation, 1, 1, 1)) {
                    if (entity instanceof Player) {
                        Player nearbyPlayer = (Player) entity;
                        Location location = nearbyPlayer.getLocation();
                        nearbyPlayer.getWorld().spawnParticle(Particle.REDSTONE, location, 5, 1.0, 1.0, 1.0, 0.03, ParticleColor.PURPLE);
                        nearbyPlayer.getWorld().spawnParticle(Particle.REDSTONE, location, 5, 1.0, 1.0, 1.0, 0.03, ParticleColor.DARK_PURPLE);
                        nearbyPlayer.getWorld().spawnParticle(Particle.REDSTONE, location, 5, 1.0, 1.0, 1.0, 0.03, ParticleColor.LIGHT_PURPLE);
                        nearbyPlayer.getWorld().spawnParticle(Particle.SMOKE_NORMAL, location, 20, 1.9, 1.9, 1.9, 0.01);
                        nearbyPlayer.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, location, 50, 0.9, 0.9, 0.9, 0.01);;
                        nearbyPlayer.getWorld().spawnParticle(Particle.BLOCK_CRACK, location, 20, 0.0, 0.0, 0.0, 0.03, Material.NETHER_PORTAL.createBlockData());
                    }
                }
            }
        }, 4L, 3L);

        BukkitTask EmpireExplosiveWaveSound = Bukkit.getScheduler().runTaskTimer(Main.getPlugin(Main.class), new Runnable() {
            Location currentLocation = player.getLocation();

            @Override
            public void run() {
                SpawnExplosiveWaveSounds(currentLocation);
                currentLocation = getNextLocation(currentLocation);
            }
        }, 0L, 1L);

        new BukkitRunnable() {
            @Override
            public void run() {
                EmpireExplosiveWaveTask.cancel();
                EmpireExplosiveWaveSound.cancel();
            }
        }.runTaskLater(Main.getPlugin(Main.class), 40L);
    }


    private void SpawnExplosiveWaveEffect(Location location) {
        Color color = Color.PURPLE;
        Color color2 = Color.BLACK;
        FireworkEffect.Type type = FireworkEffect.Type.BURST;
        int power = 1;

        SpawnFirework.spawnFirework(location, color, type, power);
        SpawnFirework.spawnFirework(location, color2, type, power);
        location.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, location, 150, 3.0, 2.0, 1.0, 0.8);
        location.getWorld().spawnParticle(Particle.REDSTONE, location, 5, 1.0, 1.0, 1.0, 0.03, ParticleColor.PURPLE);
        location.getWorld().spawnParticle(Particle.REDSTONE, location, 5, 1.0, 1.0, 1.0, 0.03, ParticleColor.DARK_PURPLE);
        location.getWorld().spawnParticle(Particle.REDSTONE, location, 5, 1.0, 1.0, 1.0, 0.03, ParticleColor.LIGHT_PURPLE);
        location.getWorld().spawnParticle(Particle.SMOKE_LARGE, location, 30, 1.9, 1.9, 1.9, 0.01);
        location.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, location, 50, 0.9, 0.9, 0.9, 0.01);
    }

    private void SpawnExplosiveWaveSounds(Location location) {
        location.getWorld().playSound(location, Sound.ENTITY_ENDER_DRAGON_FLAP, 1, 1);
        location.getWorld().playSound(location, Sound.ENTITY_PHANTOM_HURT, 1, 1);
    }

    private Location getNextLocation(Location currentLocation) {
        Vector direction = currentLocation.getDirection().normalize().multiply(2.5);
        return currentLocation.add(direction);
    }

}