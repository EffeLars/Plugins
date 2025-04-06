package me.magicwands.fire;

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
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import me.magicwands.events.FlameWand;
import me.magicwands.main.Main;
import me.magicwands.utils.ChatUtil;
import me.magicwands.utils.CooldownManager;
import me.magicwands.utils.SpawnFirework;

public class FireComet implements Listener {
    private CooldownManager cooldownManager;

    private long cooldownTimeInSeconds;
    public FireComet() {
        this.cooldownTimeInSeconds = Main.getPlugin(Main.class).getConfig().getLong("spells.FireComet.cooldown", 10L);
        this.cooldownManager = new CooldownManager(cooldownTimeInSeconds);
        this.cooldownManager.setDefaultCooldownIfNotExist(cooldownTimeInSeconds, "FireComet");
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (player.getItemInHand() != null && player.getItemInHand().getItemMeta() != null) {
            String itemDisplayName = player.getItemInHand().getItemMeta().getDisplayName();
            
            if (player.getItemInHand().getItemMeta() != null && 
            	    player.getItemInHand().getItemMeta().getDisplayName() != null && 
            	    player.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(me.magicwands.wands.FireWand.IgnatiusWand.getItemMeta().getDisplayName()) && 
            	    event.getAction() == Action.LEFT_CLICK_AIR && 
            	    FlameWand.Spell.getOrDefault(player, 0) == 7) { 

        	if (player.hasPermission("magicwands.fire.firecomet.bypasscooldown") || player.hasPermission("magicwands.fire.*")) {
                launchFireComet(player);
                return;
            }

            long cooldownTimeInMillis = cooldownManager.getCooldownDuration(player, "FireComet");
            if (this.cooldownManager.hasCooldown(player, "FireComet")) {
                long secondsRemaining = this.cooldownManager.getCooldownRemaining(player, "FireComet");
                String color = Main.getPlugin(Main.class).getConfig().getString("wands.fire.color");
                player.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes((char) '&',
                        (ChatUtil.color("&7[&" + color + "MagicWands&7] &fThis spell is still on a &" + color + "cooldown&c&l! &fYou have to wait &" + color +
                                + secondsRemaining + " &fseconds"))));
                return;
            }
            this.cooldownManager.startCooldown(player, cooldownTimeInMillis, "FireComet");

            launchFireComet(player);
        
    }}
    }

    private void launchFireComet(Player player) {
        Location location = player.getEyeLocation();
        Vector direction = location.getDirection();
        final Fireball fireball = (Fireball) player.launchProjectile(Fireball.class, direction);
        player.playSound(location, Sound.ENTITY_PIGLIN_JEALOUS, 1.0f, 1.0f);
        fireball.setYield(0.0f);
        fireball.setMetadata("Fireball2Radius", new FixedMetadataValue(Main.getPlugin(Main.class), 5.0));
        fireball.setFireTicks(0);
        fireball.setMetadata("Fireball2Trail", new FixedMetadataValue(Main.getPlugin(Main.class), true));

        new BukkitRunnable() {
            public void run() {
                if (!fireball.isDead()) {
                    Location trailLocation = fireball.getLocation();
                    Location fireworkLocation = trailLocation.clone().add(0, 1, 0); 
                    SpawnFirework.spawnFirework(fireworkLocation, Color.ORANGE, Type.BURST, 1);
                    SpawnFirework.spawnFirework(fireworkLocation, Color.YELLOW, Type.BURST, 1);
      
                    trailLocation.getWorld().spawnParticle(Particle.FLAME, trailLocation, 30, 1.0, 1.0, 1.0, 0.03);
                } else {
                    this.cancel();
                }
            }
        }.runTaskTimer(Main.getPlugin(Main.class), 8L, 5L);
    }


    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        if (event.getEntityType() == EntityType.FIREBALL && event.getEntity().hasMetadata("Fireball2Radius")) {
            double radius = event.getEntity().getMetadata("Fireball2Radius").get(0).asDouble();
            Location impactLocation = event.getEntity().getLocation();
            double theta = 0.0;

            while (theta < Math.PI * 2) {
                double x = impactLocation.getX() + radius * Math.cos(theta);
                double z = impactLocation.getZ() + radius * Math.sin(theta);
                Location fireLocation = new Location(impactLocation.getWorld(), x, impactLocation.getY(), z);
                fireLocation.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, impactLocation, 50, 3.0, 5.0, 1.0, 4.0);
                event.getEntity().getWorld().playSound(fireLocation, Sound.BLOCK_FIRE_AMBIENT, 0.1f, -1.0f);
                event.getEntity().getWorld().playSound(fireLocation, Sound.BLOCK_FIRE_EXTINGUISH, 0.1f, -2.0f);
                theta += 0.39269908169872414;
            }

            Random random = new Random();
            for (int x = -5; x <= 5; x++) {
                for (int z = -5; z <= 5; z++) {
                    Location blockLocation = impactLocation.clone().add(x, 0, z);
                    if (blockLocation.distance(impactLocation) <= 5) {
                        Location highestBlockLocation = blockLocation.getWorld().getHighestBlockAt(blockLocation).getLocation().add(0, 1, 0);

                        if (random.nextDouble() < 0.3) {
                            highestBlockLocation.getBlock().setType(Material.FIRE);
                        } else {
                           //
                        }
                    }
                }
            }

            double launchRadius = 4.0;
            for (Entity entity : impactLocation.getWorld().getNearbyEntities(impactLocation, launchRadius, launchRadius,
                    launchRadius)) {
                if (!(entity instanceof Player) || entity == event.getEntity().getShooter()) continue;
                Player nearbyPlayer = (Player) entity;
                nearbyPlayer.setFireTicks(500);
                int pulsedamage = Main.getPlugin(Main.class).getConfig().getInt("spells.comet.damage");
                nearbyPlayer.damage(pulsedamage);
            }
        }
    }
}
