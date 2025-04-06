
package me.magicwands.dark;

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

import me.magicwands.main.Main;
import me.magicwands.utils.ChatUtil;
import me.magicwands.utils.CooldownManager;
import me.magicwands.utils.ParticleColor;
import me.magicwands.wands.DarkWand;

public class DarkTeleport implements Listener {
    private CooldownManager cooldownManager;
    private long cooldownTimeInSeconds;

    public DarkTeleport() {
        this.cooldownTimeInSeconds = Main.getPlugin(Main.class).getConfig().getLong("spells.DarkTeleport.cooldown", 10L);
        this.cooldownManager = new CooldownManager(cooldownTimeInSeconds);
        this.cooldownManager.setDefaultCooldownIfNotExist(cooldownTimeInSeconds, "DarkTeleport");
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (player.getItemInHand() != null && player.getItemInHand().getItemMeta() != null) {
            String itemDisplayName = player.getItemInHand().getItemMeta().getDisplayName();
            if (itemDisplayName.equalsIgnoreCase(DarkWand.DarkWand.getItemMeta().getDisplayName())
                && event.getAction() == Action.LEFT_CLICK_AIR && me.magicwands.events.DarkWand.DarkSpell.get(player) == 4) {

                if (player.hasPermission("magicwands.dark.darkteleport.bypasscooldown") || player.hasPermission("magicwands.dark.*")) {
                    Location targetLocation = player.getTargetBlock(null, 300).getLocation();
                    teleportPlayer(player);
                    return;
                }

                long cooldownTimeInMillis = cooldownManager.getCooldownDuration(player, "DarkTeleport");
                if (this.cooldownManager.hasCooldown(player, "DarkTeleport")) {
                    long secondsRemaining = this.cooldownManager.getCooldownRemaining(player, "DarkTeleport");
                    String color = Main.getPlugin(Main.class).getConfig().getString("wands.dark.color");
                    player.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes((char) '&',
                            (ChatUtil.color("&7[&" + color + "MagicWands&7] &fThis spell is still on a &" + color + "cooldown&c&l! &fYou have to wait &" + color +
                                    + secondsRemaining + " &fseconds"))));
                    return;
                }
                this.cooldownManager.startCooldown(player, cooldownTimeInMillis, "DarkTeleport");

                Location targetLocation = player.getTargetBlock(null, 300).getLocation();
                if (targetLocation.distanceSquared(player.getLocation()) <= 4500.0) {
                    if (this.isInsideBlock(targetLocation)) {
                        targetLocation.setY(targetLocation.getY() + 1.0);
                    }
                    this.teleportPlayer(player);
                }
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

        world.spawnParticle(Particle.REDSTONE, location, 35, 1.0, 1.0, 1.0, 0.03, ParticleColor.DARK_GRAY);
        world.spawnParticle(Particle.REDSTONE, location, 35, 1.0, 1.0, 1.0, 0.03, ParticleColor.BLACK);
        world.playSound(location, Sound.ENTITY_WITHER_AMBIENT, 1, -3);

        world.spawnParticle(Particle.SMOKE_LARGE, location, 40, 0.9, 0.9, 0.9, 0.001);
        world.spawnParticle(Particle.ENCHANTMENT_TABLE, location, 50, 0.9, 0.9, 0.9, 0.01);
        world.spawnParticle(Particle.ASH, location, 50, 0.9, 0.9, 0.9, 0.01);

        createExplosion(departureLocation, explosionRadius);

        new BukkitRunnable() {
            public void run() {
                player.teleport(targetLocation.setDirection(dep2));
                world.spawnParticle(Particle.REDSTONE, location, 35, 1.0, 1.0, 1.0, 0.03, ParticleColor.DARK_GRAY);
                world.spawnParticle(Particle.REDSTONE, location, 35, 1.0, 1.0, 1.0, 0.03, ParticleColor.BLACK);
                world.playSound(targetLocation, Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
                world.playSound(location, Sound.ENTITY_WITHER_AMBIENT, 1, -3);

                world.spawnParticle(Particle.SMOKE_LARGE, location, 40, 0.9, 0.9, 0.9, 0.001);
                world.spawnParticle(Particle.ENCHANTMENT_TABLE, location, 50, 0.9, 0.9, 0.9, 0.01);
                world.spawnParticle(Particle.ASH, location, 50, 0.9, 0.9, 0.9, 0.01);
                createExplosion(targetLocation, explosionRadius);

                for (Entity entity : targetLocation.getWorld().getNearbyEntities(targetLocation, 4, 4, 4)) {
                    if (!(entity instanceof Player) || entity == player.getPlayer()) continue;
                    Player nearbyPlayer = (Player) entity;
                    nearbyPlayer.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 100, 2));
                    nearbyPlayer.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 100, 2));
                    nearbyPlayer.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 100, 2));
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
        world.spawnParticle(Particle.REDSTONE, location, 35, 1.0, 1.0, 1.0, 0.03, ParticleColor.DARK_GRAY);
        world.spawnParticle(Particle.REDSTONE, location, 35, 1.0, 1.0, 1.0, 0.03, ParticleColor.BLACK);
        world.playSound(location, Sound.ENTITY_WITHER_AMBIENT, 1, -3);
        world.spawnParticle(Particle.REDSTONE, location, 35, 1.0, 1.0, 1.0, 0.03, ParticleColor.DARK_GRAY);
        world.spawnParticle(Particle.SMOKE_LARGE, location, 40, 0.9, 0.9, 0.9, 0.001);
        world.spawnParticle(Particle.ENCHANTMENT_TABLE, location, 50, 0.9, 0.9, 0.9, 0.01);
        world.spawnParticle(Particle.ASH, location, 50, 0.9, 0.9, 0.9, 0.01);	

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
