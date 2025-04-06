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
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import me.magicwands.main.Main;
import me.magicwands.utils.ChatUtil;
import me.magicwands.utils.CooldownManager;
import me.magicwands.wands.NatureWand;

public class NatureWave implements Listener {


    private CooldownManager cooldownManager;

   
    public NatureWave() {
        long cooldownTimeInSeconds = Main.getPlugin(Main.class).getConfig().getLong("spells.NatureWave.cooldown", 30L); 
        this.cooldownManager = new CooldownManager(cooldownTimeInSeconds);
        this.cooldownManager.setDefaultCooldownIfNotExist(cooldownTimeInSeconds, "NatureWave");
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (player.getItemInHand().getItemMeta() != null && 
        	    player.getItemInHand().getItemMeta().getDisplayName() != null && 
        	    player.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(NatureWand.NatureWand.getItemMeta().getDisplayName()) && 
        	    event.getAction() == Action.LEFT_CLICK_AIR && 
        	    me.magicwands.events.NatureWand.NatureSpell.getOrDefault(player, 0) == 2) {

            if (player.hasPermission("magicwands.nature.naturewave.bypasscooldown")) {
                executeNatureWave(player);
                return;
            }

            long cooldownTimeInMillis = cooldownManager.getCooldownDuration(player, "NatureWave");
            if (this.cooldownManager.hasCooldown(player, "NatureWave")) {
                long secondsRemaining = this.cooldownManager.getCooldownRemaining(player, "NatureWave");
                String color = Main.getPlugin(Main.class).getConfig().getString("wands.nature.color");
                player.sendMessage(ChatUtil.color("&7[&" + color + "Magic&7] &fThis spell is still on a &" + color + "cooldown&c&l! &fYou have to wait &" + color + secondsRemaining + " &fseconds"));
                return;
            }

            this.cooldownManager.startCooldown(player, cooldownTimeInMillis, "NatureWave");

            executeNatureWave(player);
        }
    }

    private void executeNatureWave(Player player) {
        String color = Main.getPlugin(Main.class).getConfig().getString("wands.nature.color");

        Location initialLocation = player.getLocation().add(player.getLocation().getDirection().normalize().multiply(2));
        
        BukkitTask NatureTrailTask = Bukkit.getScheduler().runTaskTimer(Main.getPlugin(Main.class), new Runnable() {
            Location currentLocation = initialLocation;
            Vector direction = currentLocation.getDirection().normalize().multiply(0.5);

            @Override
            public void run() {
                spawnNatureDisappearParticles(currentLocation);

                Block blockInFront = currentLocation.clone().add(direction).getBlock();
                Block blockAbove = currentLocation.clone().add(0, 1, 0).getBlock();

                if (!blockInFront.getType().isAir()) {
                    player.getWorld().spawnParticle(Particle.CLOUD, blockInFront.getLocation(), 20, 0.9, 0.9, 0.9, 0.001);
                    player.getWorld().spawnParticle(Particle.TOTEM, blockInFront.getLocation(), 20, 0.9, 0.9, 0.9, 0.001);

                   
                    direction = direction.multiply(-1);

              
                    if (blockAbove.getType().isAir()) {
                        direction = direction.multiply(-1);
                    }
                }

                currentLocation = currentLocation.add(direction);

                for (Entity entity : currentLocation.getWorld().getNearbyEntities(currentLocation, 1, 1, 1)) {
                    if (entity instanceof Player) {
                        Player nearbyPlayer = (Player) entity;
                        Particle.DustOptions cyanColor = new Particle.DustOptions(Color.GREEN, 1.0f);
                        Particle.DustOptions blueColor = new Particle.DustOptions(Color.LIME, 1.0f);
                        Location location = nearbyPlayer.getLocation();
                        nearbyPlayer.getWorld().spawnParticle(Particle.REDSTONE, location, 5, 1.0, 1.0, 1.0, 0.03, cyanColor);
                        nearbyPlayer.playSound(location, Sound.BLOCK_FIRE_EXTINGUISH, 1, -3);
                        nearbyPlayer.getWorld().spawnParticle(Particle.REDSTONE, location, 5, 1.0, 1.0, 1.0, 0.03, cyanColor);
                        nearbyPlayer.getWorld().spawnParticle(Particle.REDSTONE, location, 5, 1.0, 1.0, 1.0, 0.03, blueColor);
                        nearbyPlayer.getWorld().spawnParticle(Particle.TOTEM, location, 20, 0.9, 0.9, 0.9, 0.01);
                        nearbyPlayer.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, location, 50, 0.9, 0.9, 0.9, 0.01);
                        nearbyPlayer.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 100, 2));
                        nearbyPlayer.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 300, 2));
                        nearbyPlayer.getWorld().spawnParticle(Particle.BLOCK_CRACK, location, 20, 0.0, 0.0, 0.0, 0.03, Material.OAK_LEAVES.createBlockData());
                    }
                }
            }
        }, 0L, 1L); 
        
        BukkitTask NatureWaveSound = Bukkit.getScheduler().runTaskTimer(Main.getPlugin(Main.class), new Runnable() {
            Location currentLocation = player.getLocation();

            @Override
            public void run() {
                spawnNatureWaveSound(currentLocation);
                currentLocation = getNextLocation(currentLocation);
            }
        }, 0L, 2L); 

        new BukkitRunnable() {
            @Override
            public void run() {
                NatureTrailTask.cancel();
                NatureWaveSound.cancel();
            }
        }.runTaskLater(Main.getPlugin(Main.class), 60L); 
    }

    private void spawnNatureWaveSound(Location location) {
        location.getWorld().playSound(location, Sound.ENTITY_ENDER_DRAGON_FLAP, 1, 1);
        location.getWorld().playSound(location, Sound.BLOCK_WOOD_BREAK, 1, 1);
    }

    private Location getNextLocation(Location currentLocation) {
        Vector direction = currentLocation.getDirection().normalize().multiply(0.5);
        return currentLocation.add(direction);
    }

    private void spawnNatureDisappearParticles(Location location) {
        Particle.DustOptions cyanColor = new Particle.DustOptions(Color.GREEN, 1.0f);
        Particle.DustOptions blueColor = new Particle.DustOptions(Color.LIME, 1.0f);
        location.getWorld().spawnParticle(Particle.REDSTONE, location, 5, 1.0, 1.0, 1.0, 0.03, cyanColor);
        location.getWorld().spawnParticle(Particle.REDSTONE, location, 5, 1.0, 1.0, 1.0, 0.03, blueColor);
        location.getWorld().spawnParticle(Particle.TOTEM, location, 20, 0.9, 0.9, 0.9, 0.01);
        location.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, location, 50, 0.9, 0.9, 0.9, 0.01);
        location.getWorld().spawnParticle(Particle.BLOCK_CRACK, location, 20, 0.0, 0.0, 0.0, 0.03, Material.OAK_LEAVES.createBlockData());
    }
}
