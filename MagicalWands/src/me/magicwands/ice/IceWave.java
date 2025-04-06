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
package me.magicwands.ice;

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

public class IceWave implements Listener {
    private CooldownManager cooldownManager;

    // Initialize CooldownManager with a default cooldown time
    public IceWave() {
        long cooldownTimeInSeconds = Main.getPlugin(Main.class).getConfig().getLong("spells.IceWave.cooldown", 30L); // default 30 seconds
        this.cooldownManager = new CooldownManager(cooldownTimeInSeconds);
        this.cooldownManager.setDefaultCooldownIfNotExist(cooldownTimeInSeconds, "IceWave");
    }

    @SuppressWarnings("deprecation")
	@EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        
        if (player.getItemInHand().getItemMeta() != null && 
        	    player.getItemInHand().getItemMeta().getDisplayName() != null && 
        	    player.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(me.magicwands.wands.IceWand.IceWand.getItemMeta().getDisplayName()) && 
        	    event.getAction() == Action.LEFT_CLICK_AIR && 
        	    me.magicwands.events.IceWand.IceSpell.getOrDefault(player, 0) == 2) {
            
            if (player.hasPermission("magicwands.ice.coldwave.bypasscooldown") || player.hasPermission("magicwands.ice.bypasscooldown")) {
                executeIceWave(player);
                return;
            }

            long cooldownTimeInMillis = cooldownManager.getCooldownDuration(player, "IceWave");
            if (this.cooldownManager.hasCooldown(player, "IceWave")) {
                long secondsRemaining = this.cooldownManager.getCooldownRemaining(player, "IceWave");
                String color = Main.getPlugin(Main.class).getConfig().getString("wands.ice.color");
                player.sendMessage(ChatUtil.color("&7[&" + color + "Magic&7] &fThis spell is still on a &" + color + "cooldown&c&l! &fYou have to wait &" + color + secondsRemaining + " &fseconds"));
                return;
            }

            this.cooldownManager.startCooldown(player, cooldownTimeInMillis, "IceWave");

            executeIceWave(player);
        }
    }

    private void executeIceWave(final Player player) {
       

        Location initialLocation = player.getLocation().add(player.getLocation().getDirection().normalize().multiply(2));

        BukkitTask iceWaveTrailTask = Bukkit.getScheduler().runTaskTimer(Main.getPlugin(Main.class), new Runnable() {
            Location currentLocation = initialLocation;
            Vector direction = currentLocation.getDirection().normalize().multiply(0.5); 

            @Override
            public void run() {
                Block blockInFront = currentLocation.clone().add(direction).getBlock();
                Block blockAbove = currentLocation.clone().add(0, 1, 0).getBlock();

                Vector updatedDirection = direction.clone();

                if (!blockInFront.getType().isAir()) {
                    currentLocation.getWorld().createExplosion(blockInFront.getLocation(), 0);
                    updatedDirection = updatedDirection.multiply(-1);

                    if (blockAbove.getType().isAir()) {
                        updatedDirection = new Vector(0, 1, 0);
                    }
                }

                currentLocation = currentLocation.add(updatedDirection);

                currentLocation = currentLocation.add(direction);
                spawnIceWaveParticles(currentLocation);

                for (Entity entity : currentLocation.getWorld().getNearbyEntities(currentLocation, 1, 1, 1)) {
                    if (entity instanceof Player) {
                        Player nearbyPlayer = (Player) entity;
                        applyEffectsToNearbyPlayer(nearbyPlayer);
                    }
                }
            }
        }, 0L, 1L);

        BukkitTask iceWaveSoundTask = Bukkit.getScheduler().runTaskTimer(Main.getPlugin(Main.class), new Runnable() {
            Location currentLocation = player.getLocation();

            @Override
            public void run() {
                spawnIceWaveSound(currentLocation);
                currentLocation = getNextLocation(currentLocation);
            }
        }, 0L, 2L);

        new BukkitRunnable() {
            @Override
            public void run() {
                iceWaveTrailTask.cancel();
                iceWaveSoundTask.cancel();
            }
        }.runTaskLater(Main.getPlugin(Main.class), 60L);
    }

    private void spawnIceWaveParticles(Location location) {
        Particle.DustOptions iceShardColor = new Particle.DustOptions(Color.fromRGB(170, 255, 255), 1.0f);
        Particle.DustOptions cyanColor = new Particle.DustOptions(Color.AQUA, 1.0f);
        location.getWorld().spawnParticle(Particle.REDSTONE, location, 5, 1.0, 1.0, 1.0, 0.03, iceShardColor);
        location.getWorld().spawnParticle(Particle.REDSTONE, location, 5, 1.0, 1.0, 1.0, 0.03, cyanColor);
        location.getWorld().spawnParticle(Particle.CLOUD, location, 10, 0.9, 0.9, 0.9, 0.01);
        location.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, location, 50, 0.9, 0.9, 0.9, 0.01);
        location.getWorld().spawnParticle(Particle.BLOCK_CRACK, location, 20, 0.0, 0.0, 0.0, 0.03, Material.ICE.createBlockData());
    }

    private void applyEffectsToNearbyPlayer(Player nearbyPlayer) {
        Particle.DustOptions iceShardColor = new Particle.DustOptions(Color.fromRGB(170, 255, 255), 1.0f);
        Particle.DustOptions cyanColor = new Particle.DustOptions(Color.AQUA, 1.0f);
        Location location = nearbyPlayer.getLocation();
        nearbyPlayer.getWorld().spawnParticle(Particle.REDSTONE, location, 5, 1.0, 1.0, 1.0, 0.03, iceShardColor);
        nearbyPlayer.getWorld().spawnParticle(Particle.REDSTONE, location, 5, 1.0, 1.0, 1.0, 0.03, cyanColor);
        nearbyPlayer.getWorld().spawnParticle(Particle.CLOUD, location, 20, 0.9, 0.9, 0.9, 0.001);
        nearbyPlayer.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, location, 50, 0.9, 0.9, 0.9, 0.01);
        nearbyPlayer.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 2));
        nearbyPlayer.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 100, 2));
        nearbyPlayer.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 700, 2));
        nearbyPlayer.getWorld().spawnParticle(Particle.BLOCK_CRACK, location, 20, 1.0, 1.0, 1.0, 0.003, Material.ICE.createBlockData());
        nearbyPlayer.getWorld().spawnParticle(Particle.BLOCK_CRACK, location, 20, 1.0, 1.0, 1.0, 0.003, Material.PACKED_ICE.createBlockData());
        nearbyPlayer.getWorld().spawnParticle(Particle.BLOCK_CRACK, location, 20, 1.0, 1.0, 1.0, 0.003, Material.BLUE_ICE.createBlockData());
    }

    private void spawnIceWaveSound(Location location) {
        location.getWorld().playSound(location, Sound.ENTITY_ENDER_DRAGON_FLAP, 1, 1);
        location.getWorld().playSound(location, Sound.BLOCK_GLASS_BREAK, 1, 1);
    }

    private Location getNextLocation(Location currentLocation) {
        Vector direction = currentLocation.getDirection().normalize().multiply(0.5); 
        return currentLocation.add(direction);
    }
}