package me.magicwands.dark;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
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
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import me.magicwands.main.Main;
import me.magicwands.utils.ChatUtil;
import me.magicwands.utils.CooldownManager;
import me.magicwands.wands.DarkWand;

public class DarkWave implements Listener {
    private CooldownManager cooldownManager;
    private long cooldownTimeInSeconds;

    public DarkWave() {
        this.cooldownTimeInSeconds = Main.getPlugin(Main.class).getConfig().getLong("spells.DarkWave.cooldown", 10L);
        this.cooldownManager = new CooldownManager(cooldownTimeInSeconds);
        this.cooldownManager.setDefaultCooldownIfNotExist(cooldownTimeInSeconds, "DarkWave");
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (player.getItemInHand().getItemMeta() != null &&
                player.getItemInHand().getItemMeta().getDisplayName() != null &&
                player.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(DarkWand.DarkWand.getItemMeta().getDisplayName()) &&
                event.getAction() == Action.LEFT_CLICK_AIR && me.magicwands.events.DarkWand.DarkSpell.get(player) == 2) {

            if (player.hasPermission("magicwands.dark.darkwave.bypasscooldown") || player.hasPermission("magicwands.dark.*")) {
                castDarkWave(player);
                return;
            }

            long cooldownTimeInMillis = cooldownManager.getCooldownDuration(player, "DarkWave");
            if (this.cooldownManager.hasCooldown(player, "DarkWave")) {
                long secondsRemaining = this.cooldownManager.getCooldownRemaining(player, "DarkWave");
                String color = Main.getPlugin(Main.class).getConfig().getString("wands.dark.color");
                player.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes((char) '&',
                        (ChatUtil.color("&7[&" + color + "MagicWands&7] &fThis spell is still on a &" + color + "cooldown&c&l! &fYou have to wait &" + color + secondsRemaining + " &fseconds"))));
                return;
            }
            this.cooldownManager.startCooldown(player, cooldownTimeInMillis, "DarkWave");

            castDarkWave(player);
        }
    }

    private void castDarkWave(Player player) {
        Location initialLocation = player.getLocation().add(player.getLocation().getDirection().normalize().multiply(2));
        Vector direction = initialLocation.getDirection().normalize().multiply(0.5); 

        BukkitTask darkTrailTask = Bukkit.getScheduler().runTaskTimer(Main.getPlugin(Main.class), new Runnable() {
            Location currentLocation = initialLocation;

            @Override
            public void run() {
                Block blockInFront = currentLocation.clone().add(direction).getBlock();
                Block blockAbove = currentLocation.clone().add(0, 1, 0).getBlock();

                Vector updatedDirection = direction.clone();

                if (!blockInFront.getType().isAir()) {
                    currentLocation.getWorld().createExplosion(blockInFront.getLocation(), 0);
                    updatedDirection = updatedDirection.multiply(-1);

                    if (blockAbove.getType().isAir()) {
                        updatedDirection = new Vector(0, 1, 0);
                    }
                }

                currentLocation = currentLocation.add(updatedDirection);

                spawnDarkDisappearParticles(currentLocation);
                

                for (Entity entity : currentLocation.getWorld().getNearbyEntities(currentLocation, 1, 1, 1)) {
                    if (entity instanceof Player) {
                        Player nearbyPlayer = (Player) entity;
                        Vector direction = nearbyPlayer.getLocation().getDirection().normalize().multiply(-1);
                        nearbyPlayer.setVelocity(direction.multiply(2));
                        nearbyPlayer.getWorld().playSound(nearbyPlayer.getLocation(), Sound.BLOCK_LAVA_EXTINGUISH, 1, 1);
                        nearbyPlayer.getWorld().spawnParticle(Particle.ASH, nearbyPlayer.getLocation(), 30, 0.9, 0.9, 0.9, 0.01);
                        nearbyPlayer.getWorld().spawnParticle(Particle.SMOKE_NORMAL, nearbyPlayer.getLocation(), 20, 0.9, 0.9, 0.9, 0.01);
                        nearbyPlayer.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, nearbyPlayer.getLocation(), 50, 0.9, 0.9, 0.9, 0.01);
                        nearbyPlayer.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 100, 2));
                        nearbyPlayer.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 100, 2));
                    }
                }
            }
        }, 0L, 1L);

        BukkitTask darkWaveSound = Bukkit.getScheduler().runTaskTimer(Main.getPlugin(Main.class), new Runnable() {
            Location currentLocation = player.getLocation();

            @Override
            public void run() {
                spawnDarkWaveSound(currentLocation);
                currentLocation = getNextLocation(currentLocation);

            }
        }, 0L, 2L);

        new BukkitRunnable() {
            @Override
            public void run() {
                darkTrailTask.cancel();
                darkWaveSound.cancel();
            }
        }.runTaskLater(Main.getPlugin(Main.class), 60L);
    }

    private void spawnDarkDisappearParticles(Location location) {
        location.getWorld().spawnParticle(Particle.ASH, location, 10, 0.9, 0.9, 0.9, 0.01);
        location.getWorld().spawnParticle(Particle.SMOKE_NORMAL, location, 20, 0.9, 0.9, 0.9, 0.01);
        location.getWorld().spawnParticle(Particle.SMOKE_LARGE, location, 5, 0.9, 0.9, 0.9, 0.004);
        location.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, location, 50, 0.9, 0.9, 0.9, 0.01);
    }

   

    private void spawnDarkWaveSound(Location location) {
        location.getWorld().playSound(location, Sound.ENTITY_ENDER_DRAGON_FLAP, 1, 1);
    }

    private Location getNextLocation(Location currentLocation) {
        Vector direction = currentLocation.getDirection().normalize().multiply(0.5);
        return currentLocation.add(direction);
    }
}
