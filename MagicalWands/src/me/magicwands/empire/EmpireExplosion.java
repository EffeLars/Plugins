package me.magicwands.empire;

import java.util.Random;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
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
import me.magicwands.wands.WandItems;

public class EmpireExplosion implements Listener {
private CooldownManager cooldownManager;
    
    private long cooldownTimeInSeconds;
    public EmpireExplosion() {
        this.cooldownTimeInSeconds = Main.getPlugin(Main.class).getConfig().getLong("spells.EmpireExplosive.cooldown", 10L);
        this.cooldownManager = new CooldownManager(cooldownTimeInSeconds);
        this.cooldownManager.setDefaultCooldownIfNotExist(cooldownTimeInSeconds, "EmpireExplosive");
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (player.getItemInHand() != null && player.getItemInHand().getItemMeta() != null) {
            String itemDisplayName = player.getItemInHand().getItemMeta().getDisplayName();
            
            if (player.getItemInHand().getItemMeta() != null && 
                    player.getItemInHand().getItemMeta().getDisplayName() != null && 
                    player.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(WandItems.EmpireWand.getItemMeta().getDisplayName()) && 
                    event.getAction() == Action.LEFT_CLICK_AIR && 
                    me.magicwands.events.EmpireWand.EmpireSpell.getOrDefault(player, 0) == 11) { 
                if (player.hasPermission("magicwands.empire.empireexplosion.bypasscooldown") || player.hasPermission("magicwands.empire.*")) {
                    launchEmpireExplosive(player);
                    return;
                }
    
                long cooldownTimeInMillis = cooldownManager.getCooldownDuration(player, "EmpireExplosive");
                if (this.cooldownManager.hasCooldown(player, "EmpireExplosive")) {
                    long secondsRemaining = this.cooldownManager.getCooldownRemaining(player, "EmpireExplosive");
                    String color = Main.getPlugin(Main.class).getConfig().getString("wands.empire.color");
                    player.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes((char) '&',
                            (ChatUtil.color("&7[&" + color + "MagicWands&7] &fThis spell is still on a &" + color + "cooldown&c&l! &fYou have to wait &" + color +
                                    + secondsRemaining + " &fseconds"))));
                    return;
                }
                this.cooldownManager.startCooldown(player, cooldownTimeInMillis, "EmpireExplosive");
    
                launchEmpireExplosive(player);
            }
        }
    }
    

    public static double getRandomDouble(double min, double max) {
        Random ran = new Random();
        return min + (max - min) * ran.nextDouble();
    }
    
    private void launchEmpireExplosive(Player player) {
        Location location = player.getEyeLocation();
        Vector direction = location.getDirection().normalize();
        
        Location projectileSpawnLocation = location.clone().add(direction.multiply(2));
        
        final Fireball projectile = (Fireball) player.launchProjectile(Fireball.class, direction);
        projectile.teleport(projectileSpawnLocation);
        projectile.setVelocity(direction.multiply(1)); 
        
        player.playSound(location, Sound.ENTITY_BLAZE_SHOOT, 1.0f, 1.0f);
        
        projectile.setYield(0.0f);
        projectile.setMetadata("EmpireExplosiveRadius", new FixedMetadataValue(Main.getPlugin(Main.class), 5.0));
        projectile.setFireTicks(0);
        projectile.setMetadata("EmpireExplosiveTrail", new FixedMetadataValue(Main.getPlugin(Main.class), true));

        new BukkitRunnable() {
            public void run() {
                if (!projectile.isDead()) {
                    Location trailLocation = projectile.getLocation();
                    Location particleLocation = trailLocation.clone().add(direction).add(0, 1, 0);
                    SpawnFirework.spawnFirework(trailLocation.multiply(1), Color.BLACK, org.bukkit.FireworkEffect.Type.BURST, 1);
                    SpawnFirework.spawnFirework(trailLocation.multiply(1), Color.PURPLE, org.bukkit.FireworkEffect.Type.BURST, 1);
                    trailLocation.getWorld().spawnParticle(Particle.PORTAL, trailLocation, 30, 1.0, 1.0, 1.0, 0.03);
                    trailLocation.getWorld().spawnParticle(Particle.SPELL_WITCH, particleLocation, 20, 1.0, 1.0, 1.0, 0.05);
                } else {
                    this.cancel();
                }
            }
        }.runTaskTimer(Main.getPlugin(Main.class), 3L, 4L);
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        if (event.getEntityType() == EntityType.FIREBALL && event.getEntity().hasMetadata("EmpireExplosiveRadius")) {
            double radius = event.getEntity().getMetadata("EmpireExplosiveRadius").get(0).asDouble();
            Location impactLocation = event.getEntity().getLocation();
            double theta = 0.0;
                Location fireLocation = new Location(impactLocation.getWorld(), impactLocation.getX(), impactLocation.getY(), impactLocation.getZ());
                fireLocation.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, impactLocation, 50, 3.0, 5.0, 1.0, 4.0);
                event.getEntity().getWorld().playSound(fireLocation, Sound.BLOCK_FIRE_AMBIENT, 0.1f, -1.0f);
                event.getEntity().getWorld().playSound(fireLocation, Sound.BLOCK_FIRE_EXTINGUISH, 0.1f, -2.0f);
                event.getEntity().getWorld().createExplosion(fireLocation, 4, true);
                theta += 0.39269908169872414;
            
                Block blockAtExplosion = null;
                int radius2 = 3;

                for (int x = -radius2; x <= radius; x++) {
                    for (int y = -radius2; y <= radius; y++) {
                        for (int z = -radius2; z <= radius; z++) {
                            Block tempBlock = fireLocation.clone().add(x, y, z).getBlock();
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
                    Material fallbackBlock = Material.TNT;
                    if (blockname != Material.BEDROCK && blockname != Material.OBSIDIAN && blockAtExplosion.getType().isSolid()) {
                        for (int i = 0; i < 8; i++) {
                        	fireLocation.getWorld().spawnFallingBlock(fireLocation, blockname, (byte) 0).setVelocity(new Vector(
                                    getRandomDouble(-0.2, 0.5),
                                    getRandomDouble(0.4, 1.5),
                                    getRandomDouble(-0.2, 0.5)
                            ));
                        }
                    }
                } else {        
                }
            double launchRadius = 4.0;
            for (Entity entity : impactLocation.getWorld().getNearbyEntities(impactLocation, launchRadius, launchRadius,
                    launchRadius)) {
                if (!(entity instanceof Player) || entity == event.getEntity().getShooter()) continue;
                Player nearbyPlayer = (Player) entity;
                nearbyPlayer.setFireTicks(500);
                int pulsedamage = Main.getPlugin(Main.class).getConfig().getInt("spells.explosion.damage");
                nearbyPlayer.damage(pulsedamage);
            }
        }
    }
}