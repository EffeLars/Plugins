package me.magicwands.dark;

import java.util.Random;

import org.bukkit.Color;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
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
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import me.magicwands.main.Main;
import me.magicwands.utils.ChatUtil;
import me.magicwands.utils.CooldownManager;
import me.magicwands.utils.SpawnFirework;

public class DarkPulse implements Listener {
    private CooldownManager cooldownManager;
    private long cooldownTimeInSeconds;

    public DarkPulse() {
        this.cooldownTimeInSeconds = Main.getPlugin(Main.class).getConfig().getLong("spells.DarkPulse.cooldown", 10L);
        this.cooldownManager = new CooldownManager(cooldownTimeInSeconds);
        this.cooldownManager.setDefaultCooldownIfNotExist(cooldownTimeInSeconds, "DarkPulse");
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (player.getItemInHand() != null && player.getItemInHand().getItemMeta() != null) {
            String itemDisplayName = player.getItemInHand().getItemMeta().getDisplayName();
            if (itemDisplayName != null &&
                itemDisplayName.equalsIgnoreCase(me.magicwands.wands.DarkWand.DarkWand.getItemMeta().getDisplayName()) &&
                event.getAction() == Action.LEFT_CLICK_AIR && me.magicwands.events.DarkWand.DarkSpell.get(player) == 3) {

                if (player.hasPermission("magicwands.dark.darkpulse.bypasscooldown") || player.hasPermission("magicwands.dark.*")) {
                    launchDarkPulse(player);
                    return;
                }

                if (this.cooldownManager.hasCooldown(player, "DarkPulse")) {
                    long secondsRemaining = this.cooldownManager.getCooldownRemaining(player, "DarkPulse");
                    String color = Main.getPlugin(Main.class).getConfig().getString("wands.dark.color");
                    player.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes((char) '&',
                            (ChatUtil.color("&7[&" + color + "MagicWands&7] &fThis spell is still on a &" + color + "cooldown&c&l! &fYou have to wait &" + color +
                                    + secondsRemaining + " &fseconds"))));
                    return;
                }

                this.cooldownManager.startCooldown(player, cooldownTimeInSeconds, "DarkPulse");
                launchDarkPulse(player);
            }
        }
    }

    private void launchDarkPulse(Player player) {
        Location location = player.getEyeLocation();
        Vector direction = location.getDirection();
        final Fireball fireball = (Fireball) player.launchProjectile(Fireball.class, direction);
        player.playSound(location, Sound.ENTITY_WITHER_SHOOT, 1.0f, 1.0f);
        fireball.setYield(0.0f);
        fireball.setMetadata("DarkFireballRadius", new FixedMetadataValue(Main.getPlugin(Main.class), 5.0));
        fireball.setFireTicks(0);
        fireball.setMetadata("DarkFireballTrail", new FixedMetadataValue(Main.getPlugin(Main.class), true));

        new BukkitRunnable() {
            public void run() {
                if (!fireball.isDead()) {
                    Location trailLocation = fireball.getLocation();
                    trailLocation.getWorld().spawnParticle(Particle.SMOKE_LARGE, trailLocation, 10, 0.5, 0.5, 0.5, 0.02);
                    trailLocation.getWorld().spawnParticle(Particle.SMOKE_NORMAL, trailLocation, 10, 0.5, 0.5, 0.5, 0.02);
                    trailLocation.getWorld().spawnParticle(Particle.ASH, trailLocation, 50, 1.0, 1.0, 1.0, 0.02);
                    trailLocation.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, trailLocation, 60, 1.0, 1.0, 1.0, 0.08);
                } else {
                    this.cancel();
                }
            }
        }.runTaskTimer(Main.getPlugin(Main.class), 0L, 0L);
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        if (event.getEntityType() == EntityType.FIREBALL && event.getEntity().hasMetadata("DarkFireballRadius")) {
            double radius = event.getEntity().getMetadata("DarkFireballRadius").get(0).asDouble();
            Location impactLocation = event.getEntity().getLocation();
            
            SpawnFirework.spawnFirework(impactLocation.add(0, -1, 0), Color.BLACK, Type.BURST, 1);
            SpawnFirework.spawnFirework(impactLocation.add(0, -1, 0), Color.BLACK, Type.BURST, 1);
            impactLocation.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, impactLocation, 1, 0.2, 0.2, 0.2, 0.1);
            double theta = 0.0;
            while (theta < Math.PI * 1) {
                double x = impactLocation.getX() + radius * Math.cos(theta);
                double z = impactLocation.getZ() + radius * Math.sin(theta);
                Location darkLocation = new Location(impactLocation.getWorld(), impactLocation.getX(), impactLocation.getY(), impactLocation.getZ());
                darkLocation.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, darkLocation, 2, 3.0, 5.0, 1.0, 4.0);
                event.getEntity().getWorld().playSound(darkLocation, Sound.ENTITY_WITHER_HURT, 0.1f, -1.0f);
                event.getEntity().getWorld().playSound(darkLocation, Sound.ENTITY_WITHER_HURT, 0.1f, -2.0f);
                theta += 0.39269908169872414;
            }

            double launchRadius = 4.0;
            for (Entity entity : impactLocation.getWorld().getNearbyEntities(impactLocation, launchRadius, launchRadius, launchRadius)) {
                if (!(entity instanceof Player) || entity == event.getEntity().getShooter()) continue;
                Player nearbyPlayer = (Player) entity;
                SpawnFirework.spawnFirework(impactLocation, Color.BLACK, Type.BURST, 1);
                SpawnFirework.spawnFirework(nearbyPlayer.getLocation(), Color.BLACK, Type.BURST, 1);
                nearbyPlayer.setVelocity(new Vector(0, 2, 0));
                nearbyPlayer.setFireTicks(500);
                int pulseDamage = Main.getPlugin(Main.class).getConfig().getInt("spells.darkpulse.damage");
                nearbyPlayer.damage(pulseDamage);
                Random random = new Random();
                
                
            }
        }
    }
}
