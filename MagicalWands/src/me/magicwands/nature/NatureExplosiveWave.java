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
package me.magicwands.nature;

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
import me.magicwands.wands.NatureWand;

public class NatureExplosiveWave implements Listener {

    
    private CooldownManager cooldownManager;

    public NatureExplosiveWave() {
        long cooldownTimeInSeconds = Main.getPlugin(Main.class).getConfig().getLong("spells.NaturePoisonWave.cooldown", 30L); // default 30 seconds
        this.cooldownManager = new CooldownManager(cooldownTimeInSeconds);
        this.cooldownManager.setDefaultCooldownIfNotExist(cooldownTimeInSeconds, "NaturePoisonWave");
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (player.getItemInHand().getItemMeta() != null && 
        	    player.getItemInHand().getItemMeta().getDisplayName() != null && 
        	    player.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(NatureWand.NatureWand.getItemMeta().getDisplayName()) && 
        	    event.getAction() == Action.LEFT_CLICK_AIR && 
        	    me.magicwands.events.NatureWand.NatureSpell.getOrDefault(player, 0) == 11) {

            boolean isStaff = player.hasPermission("magicwands.*");

            
            if (player.hasPermission("magicwands.nature.naturepoisonwave.bypasscooldown") || player.hasPermission("magicwands.nature.bypasscooldown") || isStaff) {
                this.launchNatureSparkWave(player);
                return;
            }

            long cooldownTimeInMillis = cooldownManager.getCooldownDuration(player, "NaturePoisonWave");
            if (this.cooldownManager.hasCooldown(player, "NaturePoisonWave")) {
                long secondsRemaining = this.cooldownManager.getCooldownRemaining(player, "NaturePoisonWave");
                String color = Main.getPlugin(Main.class).getConfig().getString("wands.nature.color");
                player.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&',
                        ChatUtil.color("&7[&" + color + "MagicWands&7] &fThis spell is still on a &" + color + "cooldown&c&l! &fYou have to wait &" + color +
                                secondsRemaining + " &fseconds")));
                return;
            }
         
            this.cooldownManager.startCooldown(player, cooldownTimeInMillis, "NaturePoisonWave");
            this.launchNatureSparkWave(player);
        }
    }
    
    public static double getRandomDouble(double min, double max) {
        Random ran = new Random();
        return min + (max - min) * ran.nextDouble();
    }
    
    public void SpawnExplosion(Location location) {
        location.getWorld().createExplosion(location, 3);
        location.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, location, 150, 3.0, 2.0, 1.0, 0.8);
        location.getWorld().spawnParticle(Particle.TOTEM, location, 50, 1.0, 1.0, 1.0, 0.03);
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



    
    private void launchNatureSparkWave(Player player) {

        Location initialLocation = player.getLocation().add(player.getLocation().getDirection().normalize().multiply(6));

        BukkitTask fireTrailTask = Bukkit.getScheduler().runTaskTimer(Main.getPlugin(Main.class), new Runnable() {
            Location currentLocation = initialLocation;
            Vector direction = currentLocation.getDirection().normalize().multiply(2.5); // Direction of the wave

            @Override
            public void run() {
                SpawnSparkWave(currentLocation);
                SpawnExplosion(currentLocation);

                Block blockInFront = currentLocation.clone().add(direction).getBlock();

              
                if (!blockInFront.getType().isAir()) {
                    return; 
                }

                currentLocation = currentLocation.add(direction);

                for (Entity entity : currentLocation.getWorld().getNearbyEntities(currentLocation, 1, 1, 1)) {
                    if (entity instanceof Player) {
                        Player nearbyPlayer = (Player) entity;
                        Particle.DustOptions iceShardColor = new Particle.DustOptions(Color.fromRGB(170, 255, 255), 1.0f);
                        Particle.DustOptions cyanColor = new Particle.DustOptions(Color.AQUA, 1.0f);
                        Particle.DustOptions blueColor = new Particle.DustOptions(Color.fromRGB(0, 0, 255), 1.0f);
                        Location location = nearbyPlayer.getLocation();
                        nearbyPlayer.getWorld().spawnParticle(Particle.REDSTONE, location, 5, 1.0, 1.0, 1.0, 0.03, ParticleColor.GREEN);
                        nearbyPlayer.getWorld().spawnParticle(Particle.REDSTONE, location, 5, 1.0, 1.0, 1.0, 0.03, ParticleColor.DARK_GREEN);
                        nearbyPlayer.getWorld().spawnParticle(Particle.REDSTONE, location, 5, 1.0, 1.0, 1.0, 0.03, ParticleColor.LIME_GREEN);
                        nearbyPlayer.getWorld().spawnParticle(Particle.SMOKE_NORMAL, location, 20, 1.9, 1.9, 1.9, 0.01);
                        nearbyPlayer.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, location, 50, 0.9, 0.9, 0.9, 0.01);
                        nearbyPlayer.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 2));
                        nearbyPlayer.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 100, 2));
                        nearbyPlayer.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 700, 2));
                        nearbyPlayer.getWorld().spawnParticle(Particle.BLOCK_CRACK, location, 20, 0.0, 0.0, 0.0, 0.03, Material.OAK_LEAVES.createBlockData());
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
        Color color2 = Color.LIME;
        FireworkEffect.Type type = FireworkEffect.Type.BURST;
        int power = 1;

        SpawnFirework.spawnFirework(location, color, type, power);
        SpawnFirework.spawnFirework(location, color2, type, power);
        location.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, location, 150, 3.0, 2.0, 1.0, 0.8);
        Particle.DustOptions cyanColor = new Particle.DustOptions(Color.GREEN, 1.0f);
        Particle.DustOptions blueColor = new Particle.DustOptions(Color.LIME, 1.0f);
        location.getWorld().spawnParticle(Particle.REDSTONE, location, 5, 1.0, 1.0, 1.0, 0.03, ParticleColor.GREEN);
        location.getWorld().spawnParticle(Particle.REDSTONE, location, 5, 1.0, 1.0, 1.0, 0.03, ParticleColor.DARK_GREEN);
        location.getWorld().spawnParticle(Particle.REDSTONE, location, 5, 1.0, 1.0, 1.0, 0.03, ParticleColor.LIME_GREEN);
        location.getWorld().spawnParticle(Particle.SMOKE_LARGE, location, 30, 1.9, 1.9, 1.9, 0.01);
        location.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, location, 50, 0.9, 0.9, 0.9, 0.01);
        location.getWorld().spawnParticle(Particle.REDSTONE, location, 5, 1.0, 1.0, 1.0, 0.03, cyanColor);
        location.getWorld().spawnParticle(Particle.REDSTONE, location, 5, 1.0, 1.0, 1.0, 0.03, cyanColor);
        location.getWorld().spawnParticle(Particle.REDSTONE, location, 5, 1.0, 1.0, 1.0, 0.03, blueColor);
    }

    private void SpawnSparkWaveSound(Location location) {
        location.getWorld().playSound(location, Sound.ENTITY_ENDER_DRAGON_FLAP, 1, 1);
        location.getWorld().playSound(location, Sound.BLOCK_GLASS_BREAK, 1, 1);
    }

    private Location getNextLocation(Location currentLocation) {
        Vector direction = currentLocation.getDirection().normalize().multiply(2.5);
        return currentLocation.add(direction);
    }

}