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
package me.magicwands.light;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import me.magicwands.main.Main;
import me.magicwands.utils.ParticleColor;
import me.magicwands.wands.LightWand;

public class LightTeleport implements Listener {
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (player.getItemInHand().getItemMeta() != null && 
        	    player.getItemInHand().getItemMeta().getDisplayName() != null && 
        	    player.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(LightWand.LightWand.getItemMeta().getDisplayName()) && 
        	    event.getAction() == Action.LEFT_CLICK_AIR && 
        	    me.magicwands.events.LightWand.LightSpell.getOrDefault(player, 0) == 4) { 
            Location targetLocation = player.getTargetBlock(null, 300).getLocation();
            if (targetLocation.distanceSquared(player.getLocation()) <= 4500.0) {
                if (this.isInsideBlock(targetLocation)) {
                    targetLocation.setY(targetLocation.getY() + 1.0);
                }
                this.CelestialTeleport(player, targetLocation);
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(EntityDamageEvent event) {
        if (event.getEntityType() == EntityType.PLAYER) {
            
        }
    }

    private final Map<UUID, Long> noLightningDamageMap = new HashMap<>();

    private void CelestialTeleport(final Player player, final Location targetLocation) {
        final Location departureLocation = player.getLocation(); 
        Vector dep2 = player.getLocation().getDirection();
        final World world = departureLocation.getWorld();
        final int explosionRadius = 0;

        world.playSound(departureLocation, Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
        Location location = player.getLocation();
        world.spawnParticle(Particle.REDSTONE, location, 35, 1.0, 1.0, 1.0, 0.03, ParticleColor.WHITE);
        world.playSound(location, Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1, -3);
        world.spawnParticle(Particle.REDSTONE, location, 35, 1.0, 1.0, 1.0, 0.03, ParticleColor.GRAY);
        world.spawnParticle(Particle.REDSTONE, location, 35, 1.0, 1.0, 1.0, 0.03, ParticleColor.LIGHT_GRAY);
        world.spawnParticle(Particle.CLOUD, location, 40, 0.9, 0.9, 0.9, 0.001);
        world.spawnParticle(Particle.WHITE_ASH, location, 40, 0.9, 0.9, 0.9, 0.001);
        world.spawnParticle(Particle.ENCHANTMENT_TABLE, location, 50, 0.9, 0.9, 0.9, 0.01);
        createExplosion(departureLocation, explosionRadius);

        noLightningDamageMap.put(player.getUniqueId(), System.currentTimeMillis());

        new BukkitRunnable() {
            public void run() {       
                player.teleport(targetLocation.setDirection(dep2));                
                world.playSound(targetLocation, Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
                Location location = player.getLocation();
                player.getWorld().spawnParticle(Particle.REDSTONE, location, 5, 1.0, 1.0, 1.0, 0.03, ParticleColor.WHITE);
                player.playSound(location, Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1, -3);
                player.getWorld().spawnParticle(Particle.REDSTONE, location, 5, 1.0, 1.0, 1.0, 0.03, ParticleColor.LIGHT_GRAY);
                world.spawnParticle(Particle.REDSTONE, location, 35, 1.0, 1.0, 1.0, 0.03, ParticleColor.GRAY);
                world.playSound(location, Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1, -3);
                world.spawnParticle(Particle.REDSTONE, location, 35, 1.0, 1.0, 1.0, 0.03, ParticleColor.WHITE);
                
                for (int i = 0; i < 5; i++) { 
                    double randomX = (Math.random() - 0.5) * 7.6; 
                    double randomZ = (Math.random() - 0.5) * 7.6;
                    world.strikeLightning(location.add(randomX, 0, randomZ));
                }
                world.spawnParticle(Particle.REDSTONE, location, 35, 1.0, 1.0, 1.0, 0.03, ParticleColor.GRAY);
                world.spawnParticle(Particle.CLOUD, location, 40, 0.9, 0.9, 0.9, 0.001);
                world.spawnParticle(Particle.WHITE_ASH, location, 40, 0.9, 0.9, 0.9, 0.001);
                world.spawnParticle(Particle.ENCHANTMENT_TABLE, location, 50, 0.9, 0.9, 0.9, 0.01);
                player.getWorld().spawnParticle(Particle.REDSTONE, location, 5, 1.0, 1.0, 1.0, 0.03, ParticleColor.WHITE);
                createExplosion(targetLocation, explosionRadius);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        noLightningDamageMap.remove(player.getUniqueId());
                    }
                }.runTaskLater(Main.getPlugin(Main.class), 60L); 
            }
        }.runTaskLater(Main.getPlugin(Main.class), 10L); 
    }

    @SuppressWarnings("deprecation")
	@EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (player.getItemInHand() != null && player.getItemInHand().hasItemMeta()) {
                if (player.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(LightWand.LightWand.getItemMeta().getDisplayName())) {
                    if (event.getCause() == DamageCause.LIGHTNING) {
                        event.setCancelled(true);  
                    }
                }
            }
        }
    }


    private void createExplosion(Location location, int radius) {
        World world = location.getWorld();
        int particleAmount = 50;
        double particleSpeed = 0.02;
        Particle.DustOptions iceShardColor = new Particle.DustOptions(Color.fromRGB(170, 255, 255), 1.0f);
        Particle.DustOptions cyanColor = new Particle.DustOptions(Color.AQUA, 1.0f);
        Particle.DustOptions blueColor = new Particle.DustOptions(Color.fromRGB(0, 0, 255), 1.0f);
        world.spawnParticle(Particle.REDSTONE, location, 35, 1.0, 1.0, 1.0, 0.03, ParticleColor.WHITE);
        world.playSound(location, Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1, -3);
        world.spawnParticle(Particle.REDSTONE, location, 35, 1.0, 1.0, 1.0, 0.03, ParticleColor.GRAY);
        world.spawnParticle(Particle.REDSTONE, location, 35, 1.0, 1.0, 1.0, 0.03, ParticleColor.LIGHT_GRAY);
        world.spawnParticle(Particle.CLOUD, location, 40, 0.9, 0.9, 0.9, 0.001);
        world.spawnParticle(Particle.WHITE_ASH, location, 40, 0.9, 0.9, 0.9, 0.001);
        world.spawnParticle(Particle.ENCHANTMENT_TABLE, location, 50, 0.9, 0.9, 0.9, 0.01);
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

