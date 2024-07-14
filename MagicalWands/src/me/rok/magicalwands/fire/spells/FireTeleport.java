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
package me.rok.magicalwands.fire.spells;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import me.lars.game.Main;
import me.lars.game.event.FlameWand;
import me.lars.game.utils.ChatUtil;
import me.lars.game.wands.Wands;

public class FireTeleport
implements Listener {
    private Map<Player, Long> cooldownMap = new HashMap<Player, Long>();
    private int cooldownDuration = 0;

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (player.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(Wands.IgnatiusWand.getItemMeta().getDisplayName()) && event.getAction() == Action.LEFT_CLICK_AIR && FlameWand.Spell.get(player) == 2) {
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
        final int teleportDelay = 20;
        final int explosionRadius = 0;
        
        // Play teleportation effects at departure location
        world.playSound(departureLocation, Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
        world.spawnParticle(Particle.SMOKE_LARGE, departureLocation, 30, 0.5, 0.5, 0.5);
        world.spawnParticle(Particle.FLAME, departureLocation, 20, 0.5, 0.5, 0.5);
        world.spawnParticle(Particle.LAVA, departureLocation, 20, 0.5, 0.5, 0.5);
        createExplosion(departureLocation, explosionRadius);

        new BukkitRunnable() {
            public void run() {
               
                player.teleport(targetLocation.setDirection(dep2));	
               
                world.playSound(targetLocation, Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
                world.spawnParticle(Particle.SMOKE_LARGE, targetLocation, 50, 0.5, 0.5, 0.5);
                world.spawnParticle(Particle.FLAME, targetLocation, 20, 0.5, 0.5, 0.5);
                world.spawnParticle(Particle.LAVA, targetLocation, 20, 0.5, 0.5, 0.5);
                createExplosion(targetLocation, explosionRadius);
                for (Entity entity : targetLocation.getWorld().getNearbyEntities(targetLocation, 4, 4, 4)) {
                    if (!(entity instanceof Player) || entity == player.getPlayer()) continue;
                    Player nearbyPlayer = (Player)entity;
                    nearbyPlayer.setVelocity(new Vector(-1.3, 0.8, 0));
                    nearbyPlayer.setFireTicks(200);
                }
            }
        }.runTaskLater(Main.getPlugin(Main.class), 10L); 
    }


    private void createExplosion(Location location, int radius) {
        World world = location.getWorld();
        int particleAmount = 50;
        double particleSpeed = 0.2;
        world.playSound(location, Sound.ENTITY_GENERIC_EXPLODE, 1.0f, 1.0f);
        world.spawnParticle(Particle.EXPLOSION_LARGE, location, 1);
        world.spawnParticle(Particle.FLAME, location, particleAmount, 0.5, 0.5, 0.5, particleSpeed);
        world.spawnParticle(Particle.LAVA, location, particleAmount, 0.5, 0.5, 0.5, particleSpeed);
        world.spawnParticle(Particle.SMOKE_LARGE, location, particleAmount, 0.5, 0.5, 0.5, particleSpeed);
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

