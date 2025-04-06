
package me.magicwands.fire;

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

import me.magicwands.events.FlameWand;
import me.magicwands.main.Main;
import me.magicwands.utils.ChatUtil;
import me.magicwands.utils.CooldownManager;

public class FireTeleport implements Listener {
    private CooldownManager cooldownManager;
    private long cooldownTimeInSeconds;
    
    public FireTeleport() {
        this.cooldownTimeInSeconds = Main.getPlugin(Main.class).getConfig().getLong("spells.FireTeleport.cooldown", 10L);
        this.cooldownManager = new CooldownManager(cooldownTimeInSeconds);
        this.cooldownManager.setDefaultCooldownIfNotExist(cooldownTimeInSeconds, "FireTeleport");
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (player.getItemInHand().getItemMeta() != null && 
        	    player.getItemInHand().getItemMeta().getDisplayName() != null && 
        	    player.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(me.magicwands.wands.FireWand.IgnatiusWand.getItemMeta().getDisplayName()) && 
        	    event.getAction() == Action.LEFT_CLICK_AIR && 
        	    FlameWand.Spell.getOrDefault(player, 0) == 2) { 
            
            if (player.hasPermission("magicwands.fire.fireteleport.bypasscooldown") || player.hasPermission("magicwands.fire.*")) {
            	Location targetLocation = player.getTargetBlock(null, 300).getLocation();
            	teleportPlayer(player);
                return;
            }
            
            long cooldownTimeInMillis = cooldownManager.getCooldownDuration(player, "FireTeleport");
            if (this.cooldownManager.hasCooldown(player, "FireTeleport")) {
                long secondsRemaining = this.cooldownManager.getCooldownRemaining(player, "FireTeleport");
                String color = Main.getPlugin(Main.class).getConfig().getString("wands.fire.color");
                player.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes((char) '&',
                        (ChatUtil.color("&7[&" + color + "MagicWands&7] &fThis spell is still on a &" + color + "cooldown&c&l! &fYou have to wait &" + color +
                                + secondsRemaining + " &fseconds"))));
                return;
            }
            this.cooldownManager.startCooldown(player, cooldownTimeInMillis, "FireTeleport");
            
            Location targetLocation = player.getTargetBlock(null, 300).getLocation();
            if (targetLocation.distanceSquared(player.getLocation()) <= 4500.0) {
                if (this.isInsideBlock(targetLocation)) {
                    targetLocation.setY(targetLocation.getY() + 1.0);
                }
                this.teleportPlayer(player);
            }
        }
        }
    

    private void teleportPlayer(final Player player) {
        Location targetLocation = player.getTargetBlock(null, 300).getLocation();
        
        if (targetLocation.distanceSquared(player.getLocation()) <= 4500.0) {
            if (this.isInsideBlock(targetLocation)) {
                targetLocation.setY(targetLocation.getY() + 1.0);
            }
            
            this.createInfernalTeleport(player, targetLocation);
        } else {
        }
    }

    private boolean isInsideBlock(Location location) {
        Block block = location.getBlock();
        return !block.getType().isAir();
    }

    private void createInfernalTeleport(final Player player, final Location targetLocation) {
        final Location departureLocation = player.getLocation();
        Vector dep2 = player.getLocation().getDirection();
        final World world = departureLocation.getWorld();
        final int teleportDelay = 120;
        final int explosionRadius = 0;

        world.playSound(departureLocation, Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
        Particle.DustOptions fireParticleColor = new Particle.DustOptions(Color.fromRGB(255, 69, 0), 1.0f);
        Particle.DustOptions orangeColor = new Particle.DustOptions(Color.fromRGB(255, 165, 0), 1.0f);
        Particle.DustOptions redColor = new Particle.DustOptions(Color.fromRGB(255, 0, 0), 1.0f);

        Location location = player.getLocation();
        
        world.spawnParticle(Particle.REDSTONE, location, 35, 1.0, 1.0, 1.0, 0.03, fireParticleColor);
        world.playSound(location, Sound.BLOCK_FIRE_EXTINGUISH, 1, -3);
        world.spawnParticle(Particle.REDSTONE, location, 35, 1.0, 1.0, 1.0, 0.03, orangeColor);
        world.spawnParticle(Particle.REDSTONE, location, 35, 1.0, 1.0, 1.0, 0.03, redColor);
        world.spawnParticle(Particle.FLAME, location, 40, 0.9, 0.9, 0.9, 0.001);
        world.spawnParticle(Particle.ENCHANTMENT_TABLE, location, 50, 0.9, 0.9, 0.9, 0.01);
        world.spawnParticle(Particle.BLOCK_CRACK, location, 80, 1.0, 1.0, 1.0, 1.03, Material.MAGMA_BLOCK.createBlockData());

        createExplosion(departureLocation, explosionRadius);

        new BukkitRunnable() {
            public void run() {
                player.teleport(targetLocation.setDirection(dep2));

                world.playSound(targetLocation, Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
                player.getWorld().spawnParticle(Particle.REDSTONE, targetLocation, 5, 1.0, 1.0, 1.0, 0.03, fireParticleColor);
                player.getWorld().spawnParticle(Particle.REDSTONE, targetLocation, 5, 1.0, 1.0, 1.0, 0.03, orangeColor);
                world.spawnParticle(Particle.REDSTONE, targetLocation, 35, 1.0, 1.0, 1.0, 0.03, fireParticleColor);
                world.playSound(targetLocation, Sound.BLOCK_FIRE_AMBIENT, 1, -3);
                player.playSound(targetLocation, Sound.BLOCK_FIRE_AMBIENT, 1, -3);
                world.spawnParticle(Particle.REDSTONE, targetLocation, 35, 1.0, 1.0, 1.0, 0.03, orangeColor);
                world.spawnParticle(Particle.REDSTONE, targetLocation, 35, 1.0, 1.0, 1.0, 0.03, redColor);
                world.spawnParticle(Particle.FLAME, targetLocation, 40, 0.9, 0.9, 0.9, 0.001);
                world.spawnParticle(Particle.ENCHANTMENT_TABLE, targetLocation, 50, 0.9, 0.9, 0.9, 0.01);
                world.spawnParticle(Particle.BLOCK_CRACK, targetLocation, 80, 1.0, 1.0, 1.0, 1.03, Material.MAGMA_BLOCK.createBlockData());
                player.getWorld().spawnParticle(Particle.REDSTONE, targetLocation, 5, 1.0, 1.0, 1.0, 0.03, redColor);
                createExplosion(targetLocation, explosionRadius);

                for (Entity entity : targetLocation.getWorld().getNearbyEntities(targetLocation, 4, 4, 4)) {
                    if (!(entity instanceof Player) || entity == player.getPlayer()) continue;
                    Player nearbyPlayer = (Player) entity;
                    nearbyPlayer.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 2));
                    nearbyPlayer.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 100, 2));
                    nearbyPlayer.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 700, 2));
                    nearbyPlayer.setFireTicks(300);
                    Location location = nearbyPlayer.getLocation();
                    Vector direction = location.getDirection();
                    Vector velocity = direction.multiply(-2); 
                    velocity.setY(1);
                    nearbyPlayer.setVelocity(velocity);
                }
            }
        }.runTaskLater(Main.getPlugin(Main.class), 10L); 
    }

    private void createExplosion(Location location, int radius) {
        World world = location.getWorld();
        Particle.DustOptions fireParticleColor = new Particle.DustOptions(Color.fromRGB(255, 69, 0), 1.0f);
        Particle.DustOptions orangeColor = new Particle.DustOptions(Color.fromRGB(255, 165, 0), 1.0f);
        Particle.DustOptions redColor = new Particle.DustOptions(Color.fromRGB(255, 0, 0), 1.0f);

        world.spawnParticle(Particle.REDSTONE, location, 35, 1.0, 1.0, 1.0, 0.03, fireParticleColor);
        world.playSound(location, Sound.BLOCK_FIRE_AMBIENT, 1, -3);
        world.spawnParticle(Particle.REDSTONE, location, 35, 1.0, 1.0, 1.0, 0.03, orangeColor);
        world.spawnParticle(Particle.REDSTONE, location, 35, 1.0, 1.0, 1.0, 0.03, redColor);
        world.spawnParticle(Particle.FLAME, location, 40, 0.9, 0.9, 0.9, 0.001);
        world.spawnParticle(Particle.ENCHANTMENT_TABLE, location, 50, 0.9, 0.9, 0.9, 0.01);
        world.spawnParticle(Particle.EXPLOSION_LARGE, location, 5, 0.9, 0.9, 0.9, 0.001);
        world.playSound(location, Sound.ENTITY_GENERIC_EXPLODE, 1, -3);
        world.spawnParticle(Particle.CAMPFIRE_SIGNAL_SMOKE, location, 10, 0.9, 0.9, 0.9, 0.01);
        world.spawnParticle(Particle.BLOCK_CRACK, location, 80, 1.0, 1.0, 1.0, 1.03, Material.MAGMA_BLOCK.createBlockData());

        int x = -radius;
        while (x <= radius) {
            int y = -radius;
            while (y <= radius) {
                int z = -radius;
                while (z <= radius) {
                    Location currentLocation = location.clone().add((double) x, (double) y, (double) z);
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
}
