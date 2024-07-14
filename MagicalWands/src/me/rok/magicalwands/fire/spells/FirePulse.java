/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  net.md_5.bungee.api.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.Particle
 *  org.bukkit.Sound
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.EntityType
 *  org.bukkit.entity.Fireball
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.block.Action
 *  org.bukkit.event.entity.ProjectileHitEvent
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 *  org.bukkit.util.Vector
 */
package me.rok.magicalwands.fire.spells;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.bukkit.Color;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import me.lars.game.Main;
import me.lars.game.event.FlameWand;
import me.lars.game.wands.Wands;
import net.md_5.bungee.api.ChatColor;

public class FirePulse
implements Listener {
    private Map<Player, Long> cooldowns = new HashMap<Player, Long>();
    private int cooldownTimeInSeconds = 0;

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (player.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(Wands.IgnatiusWand.getItemMeta().getDisplayName()) && event.getAction() == Action.LEFT_CLICK_AIR && FlameWand.Spell.get(player) == 3) {
            if (this.hasCooldown(player)) {
                long secondsRemaining = this.getCooldownRemaining(player);
                player.sendMessage(ChatColor.translateAlternateColorCodes((char)'&', (String)("&7[&dMagic&7] &fThis spell is still on a &d&lCOOLDOWN&c&l! &fYou have to wait &d" + secondsRemaining + " &fseconds")));
                return;
            }
            this.startCooldown(player);
            Location location = player.getEyeLocation();
            Vector direction = location.getDirection();
            final Fireball fireball = (Fireball)player.launchProjectile(Fireball.class, direction);
            player.playSound(location, Sound.ENTITY_WITHER_SHOOT, 1.0f, 1.0f);
            fireball.setYield(0.0f);
            fireball.setMetadata("FireballRadius", (MetadataValue)new FixedMetadataValue((Plugin)Main.getPlugin(Main.class), (Object)5.0));
            fireball.setFireTicks(0);
            fireball.setMetadata("FireballTrail", (MetadataValue)new FixedMetadataValue((Plugin)Main.getPlugin(Main.class), (Object)true));
            new BukkitRunnable(){

                public void run() {
                    if (!fireball.isDead()) {
                        Location trailLocation = fireball.getLocation();
                        trailLocation.getWorld().spawnParticle(Particle.FLAME, trailLocation, 6, 0.5, 0.5, 0.5, 0.02);
                        trailLocation.getWorld().spawnParticle(Particle.SMOKE_LARGE, trailLocation, 10, 0.5, 0.5, 0.5, 0.02);
                        trailLocation.getWorld().spawnParticle(Particle.SMOKE_NORMAL, trailLocation, 10, 0.5, 0.5, 0.5, 0.02);
                        trailLocation.getWorld().spawnParticle(Particle.ASH, trailLocation, 50, 1.0, 1.0, 1.0, 0.02);
                        trailLocation.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, trailLocation, 60, 1.0, 1.0, 1.0, 0.08);
                    } else {
                        this.cancel();
                    }
                }
            }.runTaskTimer((Plugin)Main.getPlugin(Main.class), 0L, 0L);
        }
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        if (event.getEntityType() == EntityType.FIREBALL && event.getEntity().hasMetadata("FireballRadius")) {
            double radius = ((MetadataValue)event.getEntity().getMetadata("FireballRadius").get(0)).asDouble();
            Location impactLocation = event.getEntity().getLocation();
            double theta = 0.0;
            while (theta < Math.PI * 2) {
                double x = impactLocation.getX() + radius * Math.cos(theta);
                double z = impactLocation.getZ() + radius * Math.sin(theta);
                Location fireLocation = new Location(impactLocation.getWorld(), x, impactLocation.getY(), z);
                fireLocation.getWorld().spawnParticle(Particle.FLAME, fireLocation, 1, 0.0, 0.0, 0.0, 0.0);
                fireLocation.getWorld().spawnParticle(Particle.FLAME, fireLocation, 10, 0.0, 0.0, 0.0, 10.0);
                fireLocation.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, impactLocation, 2, 1.0, 1.0, 1.0, 1.0);
                fireLocation.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, impactLocation, 2, 3.0, 5.0, 1.0, 4.0);
                event.getEntity().getWorld().playSound(fireLocation, Sound.ENTITY_WITHER_HURT, 0.1f, -1.0f);
                event.getEntity().getWorld().playSound(fireLocation, Sound.ENTITY_WITHER_HURT, 0.1f, -2.0f);
                theta += 0.39269908169872414;
            }
            double launchRadius = 4.0;
            for (Entity entity : impactLocation.getWorld().getNearbyEntities(impactLocation, launchRadius, launchRadius, launchRadius)) {
                if (!(entity instanceof Player) || entity == event.getEntity().getShooter()) continue;
                Player nearbyPlayer = (Player)entity;
                FireSpark.spawnFirework(impactLocation, Color.BLACK, Type.BURST, 1);
                FireSpark.spawnFirework(nearbyPlayer.getLocation(), Color.NAVY, Type.BURST, 1);
                nearbyPlayer.setVelocity(new Vector(0, 2, 0));
                nearbyPlayer.setFireTicks(500);
                int pulsedamage = Main.getPlugin(Main.class).getConfig().getInt("spells.firepulse.damage");
                nearbyPlayer.damage(pulsedamage);
                Random random = new Random();
    			for (int x = -5; x <= 5; x++) {
    	            for (int z = -5; z <= 5; z++) {
    	                Location blockLocation = impactLocation.clone().add(x, 0, z);
    	                if (blockLocation.distance(impactLocation) <= 5) {
    	                   
    	                    Location highestBlockLocation = blockLocation.getWorld().getHighestBlockAt(blockLocation).getLocation().add(0, 1, 0);

    	            
    	                    if (random.nextDouble() < 0.3) {
    	                        highestBlockLocation.getBlock().setType(Material.FIRE);
    	                    } else {
    	                        highestBlockLocation.getBlock().setType(Material.AIR);
    	                    }
    	                }
    	            }
    	        }
            }
        }
    }

    private void startCooldown(Player player) {
        this.cooldowns.put(player, System.currentTimeMillis());
    }

    private boolean hasCooldown(Player player) {
        if (!this.cooldowns.containsKey(player)) {
            return false;
        }
        long cooldown = this.cooldowns.get(player);
        long currentTime = System.currentTimeMillis();
        long elapsedSeconds = (currentTime - cooldown) / 1000L;
        return elapsedSeconds < (long)this.cooldownTimeInSeconds;
    }

    private long getCooldownRemaining(Player player) {
        if (!this.cooldowns.containsKey(player)) {
            return 0L;
        }
        long cooldown = this.cooldowns.get(player);
        long currentTime = System.currentTimeMillis();
        long elapsedSeconds = (currentTime - cooldown) / 1000L;
        return (long)this.cooldownTimeInSeconds - elapsedSeconds;
    }
}

