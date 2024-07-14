/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.Particle
 *  org.bukkit.Sound
 *  org.bukkit.World
 *  org.bukkit.block.Block
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.block.Action
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 */
package me.rok.magicalwands.ice.spells;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
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
import org.bukkit.util.Vector;

import me.lars.game.Main;
import me.lars.game.utils.ChatUtil;
import me.lars.game.wands.IceWand;

public class IceTeleport
implements Listener {
    private Map<Player, Long> cooldownMap = new HashMap<Player, Long>();
    private int cooldownDuration = 0;

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (player.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(IceWand.IceWand.getItemMeta().getDisplayName()) && event.getAction() == Action.LEFT_CLICK_AIR && me.lars.game.event.IceWand.IceSpell.get(player) == 1) {
            if (this.isOnCooldown(player)) {
                long secondsRemaining = this.getCooldownRemaining(player);
                player.sendMessage(ChatUtil.color("&7[&dMagic&7] &fThis spell is stil on a &d&lCOOLDOWN&c&l! &fYou have to wait &d" + secondsRemaining + " &fseconds"));
                return;
            }
            Location targetLocation = player.getTargetBlock(null, 300).getLocation();
            if (targetLocation.distanceSquared(player.getLocation()) <= 4500.0) {
                if (this.isInsideBlock(targetLocation)) {
                    targetLocation.setY(targetLocation.getY() + 1.0);
                }
                this.createInfernalTeleport(player, targetLocation);
                this.startCooldown(player);
            }
        }
    }

    private boolean isOnCooldown(Player player) {
        return this.cooldownMap.containsKey(player) && System.currentTimeMillis() - this.cooldownMap.get(player) < (long)(this.cooldownDuration * 1000);
    }

    private long getCooldownRemaining(Player player) {
        long cooldownEnd = this.cooldownMap.getOrDefault(player, 0L) + (long)(this.cooldownDuration * 1000);
        long currentTime = System.currentTimeMillis();
        return TimeUnit.MILLISECONDS.toSeconds(cooldownEnd - currentTime);
    }

    private void startCooldown(Player player) {
        this.cooldownMap.put(player, System.currentTimeMillis());
    }

    private void createInfernalTeleport(final Player player, final Location targetLocation) {
        final Location departureLocation = player.getLocation(); 
        Vector dep2 = player.getLocation().getDirection();
        final World world = departureLocation.getWorld();
        final int teleportDelay = 120;
        final int explosionRadius = 0;
        

        world.playSound(departureLocation, Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
        Particle.DustOptions iceShardColor = new Particle.DustOptions(Color.fromRGB(170, 255, 255), 1.0f);
        Particle.DustOptions cyanColor = new Particle.DustOptions(Color.AQUA, 1.0f);
        Particle.DustOptions blueColor = new Particle.DustOptions(Color.fromRGB(0, 0, 255), 1.0f);
        Location location = player.getLocation();
        world.spawnParticle(Particle.REDSTONE, location, 35, 1.0, 1.0, 1.0, 0.03, iceShardColor);
        world.playSound(location, Sound.BLOCK_FIRE_EXTINGUISH, 1, -3);
        world.spawnParticle(Particle.REDSTONE, location, 35, 1.0, 1.0, 1.0, 0.03, cyanColor);
        world.spawnParticle(Particle.REDSTONE, location, 35, 1.0, 1.0, 1.0, 0.03, blueColor);
        world.spawnParticle(Particle.CLOUD, location, 40, 0.9, 0.9, 0.9, 0.001);
        world.spawnParticle(Particle.ENCHANTMENT_TABLE, location, 50, 0.9, 0.9, 0.9, 0.01);
        world.spawnParticle(Particle.BLOCK_CRACK, location, 80, 1.0, 1.0, 1.0, 1.03, Material.ICE.createBlockData());
        createExplosion(departureLocation, explosionRadius);

        new BukkitRunnable() {
            public void run() {
                // Teleport the player to the target location with the same yaw
                player.teleport(targetLocation.setDirection(dep2));	
                // Play teleportation effects at target location
                world.playSound(targetLocation, Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
                Particle.DustOptions iceShardColor = new Particle.DustOptions(Color.fromRGB(170, 255, 255), 1.0f);
                Particle.DustOptions cyanColor = new Particle.DustOptions(Color.AQUA, 1.0f);
                Particle.DustOptions blueColor = new Particle.DustOptions(Color.fromRGB(0, 0, 255), 1.0f);
                Location location = player.getLocation();
                player.getWorld().spawnParticle(Particle.REDSTONE, location, 5, 1.0, 1.0, 1.0, 0.03, iceShardColor);
                player.playSound(location, Sound.BLOCK_FIRE_EXTINGUISH, 1, -3);
                player.getWorld().spawnParticle(Particle.REDSTONE, location, 5, 1.0, 1.0, 1.0, 0.03, cyanColor);
                world.spawnParticle(Particle.REDSTONE, location, 35, 1.0, 1.0, 1.0, 0.03, iceShardColor);
                world.playSound(location, Sound.BLOCK_FIRE_EXTINGUISH, 1, -3);
                world.spawnParticle(Particle.REDSTONE, location, 35, 1.0, 1.0, 1.0, 0.03, cyanColor);
                world.spawnParticle(Particle.REDSTONE, location, 35, 1.0, 1.0, 1.0, 0.03, blueColor);
                world.spawnParticle(Particle.CLOUD, location, 40, 0.9, 0.9, 0.9, 0.001);
                world.spawnParticle(Particle.ENCHANTMENT_TABLE, location, 50, 0.9, 0.9, 0.9, 0.01);
                world.spawnParticle(Particle.BLOCK_CRACK, location, 80, 1.0, 1.0, 1.0, 1.03, Material.ICE.createBlockData());
                player.getWorld().spawnParticle(Particle.REDSTONE, location, 5, 1.0, 1.0, 1.0, 0.03, blueColor);
                createExplosion(targetLocation, explosionRadius);
                for (Entity entity : targetLocation.getWorld().getNearbyEntities(targetLocation, 4, 4, 4)) {
                    if (!(entity instanceof Player) || entity == player.getPlayer()) continue;
                    Player nearbyPlayer = (Player)entity;
                    nearbyPlayer.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 2));
                    nearbyPlayer.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 100, 2));
                    nearbyPlayer.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 700, 2));
                }
            }
        }.runTaskLater(Main.getPlugin(Main.class), 10L); // Use your plugin instance to schedule the task
    }


    private void createExplosion(Location location, int radius) {
        World world = location.getWorld();
        int particleAmount = 50;
        double particleSpeed = 0.02;
        Particle.DustOptions iceShardColor = new Particle.DustOptions(Color.fromRGB(170, 255, 255), 1.0f);
        Particle.DustOptions cyanColor = new Particle.DustOptions(Color.AQUA, 1.0f);
        Particle.DustOptions blueColor = new Particle.DustOptions(Color.fromRGB(0, 0, 255), 1.0f);
        world.spawnParticle(Particle.REDSTONE, location, 35, 1.0, 1.0, 1.0, 0.03, iceShardColor);
        world.playSound(location, Sound.BLOCK_FIRE_EXTINGUISH, 1, -3);
        world.spawnParticle(Particle.REDSTONE, location, 35, 1.0, 1.0, 1.0, 0.03, cyanColor);
        world.spawnParticle(Particle.REDSTONE, location, 35, 1.0, 1.0, 1.0, 0.03, blueColor);
        world.spawnParticle(Particle.CLOUD, location, 40, 0.9, 0.9, 0.9, 0.001);
        world.spawnParticle(Particle.ENCHANTMENT_TABLE, location, 50, 0.9, 0.9, 0.9, 0.01);
        world.spawnParticle(Particle.BLOCK_CRACK, location, 80, 1.0, 1.0, 1.0, 1.03, Material.ICE.createBlockData());
        int x = -radius;
        while (x <= radius) {
            int y = -radius;
            while (y <= radius) {
                int z = -radius;
                while (z <= radius) {
                    Location currentLocation = location.clone().add((double)x, (double)y, (double)z);
                    Block block = currentLocation.getBlock();
                    if (block.getType() != Material.AIR) {
                        block.setType(Material.AIR);
                    }
                    ++z;
                }
                ++y;
            }
            ++x;
        }
    }

    private boolean isInsideBlock(Location location) {
        Block block = location.getBlock();
        return !block.getType().isAir();
    }
}

