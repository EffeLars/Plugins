
package me.magicwands.witch;

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

public class WitchTeleport implements Listener {
    private CooldownManager cooldownManager;
    private long cooldownTimeInSeconds;

    public WitchTeleport() {
        this.cooldownTimeInSeconds = Main.getPlugin(Main.class).getConfig().getLong("spells.WitchTeleport.cooldown", 10L);
        this.cooldownManager = new CooldownManager(cooldownTimeInSeconds);
        this.cooldownManager.setDefaultCooldownIfNotExist(cooldownTimeInSeconds, "WitchTeleport");
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (player.getItemInHand() != null && player.getItemInHand().getItemMeta() != null) {
            String itemDisplayName = player.getItemInHand().getItemMeta().getDisplayName();
            if (player.getItemInHand().getItemMeta() != null && 
            	    player.getItemInHand().getItemMeta().getDisplayName() != null && 
            	    player.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(me.magicwands.wands.WitchWand.WitchWand.getItemMeta().getDisplayName()) && 
            	    event.getAction() == Action.LEFT_CLICK_AIR && 
            	    (me.magicwands.events.WitchWand.WitchSpell.getOrDefault(player, 0) != 0 && me.magicwands.events.WitchWand.WitchSpell.get(player) == 5)) { 

                if (player.hasPermission("magicwands.witch.witchteleport.bypasscooldown") || player.hasPermission("magicwands.witch.*")) {
                    Location targetLocation = player.getTargetBlock(null, 300).getLocation();
                    teleportPlayer(player);
                    return;
                }

                long cooldownTimeInMillis = cooldownManager.getCooldownDuration(player, "WitchTeleport");
                if (this.cooldownManager.hasCooldown(player, "WitchTeleport")) {
                    long secondsRemaining = this.cooldownManager.getCooldownRemaining(player, "WitchTeleport");
                    String color = Main.getPlugin(Main.class).getConfig().getString("wands.witch.color");
                    player.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes((char) '&',
                            (ChatUtil.color("&7[&" + color + "MagicWands&7] &fThis spell is still on a &" + color + "cooldown&c&l! &fYou have to wait &" + color +
                                    + secondsRemaining + " &fseconds"))));
                    return;
                }
                this.cooldownManager.startCooldown(player, cooldownTimeInMillis, "WitchTeleport");

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

            this.createWitchTeleport(player, targetLocation);
        } else {
        }
    }

    private boolean isInsideBlock(Location location) {
        Block block = location.getBlock();
        return !block.getType().isAir();
    }

    private void createWitchTeleport(final Player player, final Location targetLocation) {
        final Location departureLocation = player.getLocation();
        Vector dep2 = player.getLocation().getDirection();
        final World world = departureLocation.getWorld();
        final int teleportDelay = 120;
        final int explosionRadius = 0;

        world.playSound(departureLocation, Sound.ENTITY_WITCH_CELEBRATE, 1.0f, 1.0f);
        world.spawnParticle(Particle.SPELL_WITCH, departureLocation, 50, 1.0, 1.0, 1.0, 0.03);
        world.spawnParticle(Particle.SPELL, departureLocation, 40, 0.9, 0.9, 0.9, 0.001);
        world.spawnParticle(Particle.ENCHANTMENT_TABLE, departureLocation, 50, 0.9, 0.9, 0.9, 0.01);

        createExplosion(departureLocation, explosionRadius);

        new BukkitRunnable() {
            public void run() {
                player.teleport(targetLocation.setDirection(dep2));

                world.playSound(targetLocation, Sound.ENTITY_WITCH_CELEBRATE, 1.0f, 1.0f);
                player.getWorld().spawnParticle(Particle.SPELL_WITCH, targetLocation, 50, 1.0, 1.0, 1.0, 0.03);
                Particle.DustOptions Purple = new Particle.DustOptions(Color.fromRGB(138, 43, 226), 1.0f);
                Particle.DustOptions Purple2 = new Particle.DustOptions(Color.PURPLE, 1.0f);
                world.spawnParticle(Particle.REDSTONE, targetLocation, 8, 0.9, 0.9, 0.9, 0.03, Purple2);
                world.spawnParticle(Particle.REDSTONE, targetLocation, 8, 0.9, 0.9, 0.9, 0.03, Purple);
                world.spawnParticle(Particle.ENCHANTMENT_TABLE, targetLocation, 50, 0.9, 0.9, 0.9, 0.01);
                createExplosion(targetLocation, explosionRadius);

                for (Entity entity : targetLocation.getWorld().getNearbyEntities(targetLocation, 4, 4, 4)) {
                    if (!(entity instanceof Player) || entity == player.getPlayer()) continue;
                    Player nearbyPlayer = (Player) entity;
                    nearbyPlayer.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 100, 2));
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

        world.spawnParticle(Particle.SPELL_WITCH, location, 50, 1.0, 1.0, 1.0, 0.03);
        world.spawnParticle(Particle.ENCHANTMENT_TABLE, location, 50, 0.9, 0.9, 0.9, 0.01);
        world.spawnParticle(Particle.EXPLOSION_LARGE, location, 5, 0.9, 0.9, 0.9, 0.001);
        world.playSound(location, Sound.ENTITY_WITCH_CELEBRATE, 1, -3);
        world.playSound(location, Sound.ENTITY_WITCH_CELEBRATE, 1, 1);
        world.playSound(location, Sound.ENTITY_GENERIC_EXPLODE, 1, 1);
        Particle.DustOptions Purple = new Particle.DustOptions(Color.fromRGB(138, 43, 226), 1.0f);
        Particle.DustOptions Purple2 = new Particle.DustOptions(Color.PURPLE, 1.0f);
        location.getWorld().spawnParticle(Particle.REDSTONE, location, 8, 0.9, 0.9, 0.9, 0.03, Purple2);
        location.getWorld().spawnParticle(Particle.REDSTONE, location, 8, 0.9, 0.9, 0.9, 0.03, Purple);

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
