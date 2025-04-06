package me.magicwands.fire;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import me.magicwands.events.FlameWand;
import me.magicwands.main.Main;
import me.magicwands.utils.ChatUtil;
import me.magicwands.utils.CooldownManager;

public class FireWave implements Listener {
    private CooldownManager cooldownManager;
    private long cooldownTimeInSeconds;


    public FireWave() {
        this.cooldownTimeInSeconds = Main.getPlugin(Main.class).getConfig().getLong("spells.FireWave.cooldown", 10L);
        this.cooldownManager = new CooldownManager(cooldownTimeInSeconds);
        this.cooldownManager.setDefaultCooldownIfNotExist(cooldownTimeInSeconds, "FireWave");
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (player.getItemInHand().getItemMeta() != null && 
        	    player.getItemInHand().getItemMeta().getDisplayName() != null && 
        	    player.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(me.magicwands.wands.FireWand.IgnatiusWand.getItemMeta().getDisplayName()) && 
        	    event.getAction() == Action.LEFT_CLICK_AIR && 
        	    FlameWand.Spell.getOrDefault(player, 0) == 9) { 

            if (player.hasPermission("magicwands.fire.firewave.bypasscooldown") || player.hasPermission("magicwands.fire.*")) {
                castFireWave(player);
                return;
            }

            long cooldownTimeInMillis = cooldownManager.getCooldownDuration(player, "FireWave");
            if (this.cooldownManager.hasCooldown(player, "FireWave")) {
                long secondsRemaining = this.cooldownManager.getCooldownRemaining(player, "FireWave");
                String color = Main.getPlugin(Main.class).getConfig().getString("wands.fire.color");
                player.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes((char) '&',
                        (ChatUtil.color("&7[&" + color + "MagicWands&7] &fThis spell is still on a &" + color + "cooldown&c&l! &fYou have to wait &" + color + secondsRemaining + " &fseconds"))));
                return;
            }
            this.cooldownManager.startCooldown(player, cooldownTimeInMillis, "FireWave");

            castFireWave(player);
        }
    }

    private void castFireWave(Player player) {
        Location initialLocation = player.getLocation().add(player.getLocation().getDirection().normalize().multiply(2));
        Vector direction = initialLocation.getDirection().normalize().multiply(0.5); 

        BukkitTask fireTrailTask = Bukkit.getScheduler().runTaskTimer(Main.getPlugin(Main.class), new Runnable() {
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

               
                spawnFireDisappearParticles(currentLocation);
                spawnFire(currentLocation);

                
                for (Entity entity : currentLocation.getWorld().getNearbyEntities(currentLocation, 1, 1, 1)) {
                    if (entity instanceof Player) {
                        Player nearbyPlayer = (Player) entity;
                        nearbyPlayer.setFireTicks(300);
                        nearbyPlayer.getWorld().playSound(nearbyPlayer.getLocation(), Sound.BLOCK_LAVA_EXTINGUISH, 1, 1);
                        nearbyPlayer.getWorld().spawnParticle(Particle.FLAME, nearbyPlayer.getLocation(), 30, 0.9, 0.9, 0.9, 0.01);
                        nearbyPlayer.getWorld().spawnParticle(Particle.SMOKE_NORMAL, nearbyPlayer.getLocation(), 20, 0.9, 0.9, 0.9, 0.01);
                        nearbyPlayer.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, nearbyPlayer.getLocation(), 50, 0.9, 0.9, 0.9, 0.01);
                    }
                }
            }
        }, 0L, 1L);

        BukkitTask fireWaveSound = Bukkit.getScheduler().runTaskTimer(Main.getPlugin(Main.class), new Runnable() {
            Location currentLocation = player.getLocation();

            @Override
            public void run() {
                spawnFireWaveSound(currentLocation);
                currentLocation = getNextLocation(currentLocation);
                spawnFire(currentLocation);
            }
        }, 0L, 2L);

        new BukkitRunnable() {
            @Override
            public void run() {
                fireTrailTask.cancel();
                fireWaveSound.cancel();
            }
        }.runTaskLater(Main.getPlugin(Main.class), 60L);
    }

    private void spawnFireDisappearParticles(Location location) {
        location.getWorld().spawnParticle(Particle.FLAME, location, 10, 0.9, 0.9, 0.9, 0.01);
        location.getWorld().spawnParticle(Particle.SMOKE_NORMAL, location, 20, 0.9, 0.9, 0.9, 0.01);
        location.getWorld().spawnParticle(Particle.SMOKE_LARGE, location, 5, 0.9, 0.9, 0.9, 0.004);
        location.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, location, 50, 0.9, 0.9, 0.9, 0.01);
    }

    private void spawnFire(Location location) {
        World world = location.getWorld();
        if (world == null) return;

        Block blockBelow = location.getBlock().getRelative(BlockFace.DOWN);
        if (!blockBelow.getType().isSolid()) {
            return; 
        }

        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                Location fireLocation = location.clone().add(x + 0.5, 1, z + 0.5);
                if (fireLocation.getBlock().getType() == Material.AIR) {
                    fireLocation.getBlock().setType(Material.FIRE);
                    fireLocation.getWorld().playSound(fireLocation, Sound.ENTITY_WOLF_STEP, 1, 1);
                    fireLocation.getWorld().spawnParticle(Particle.FLAME, fireLocation, 30, 0.5, 0.5, 0.5, 0.02);
                    fireLocation.getWorld().spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, fireLocation, 3, 0.5, 0.5, 0.5, 0.02);
                }
            }
        }
    }

    private void spawnFireWaveSound(Location location) {
        location.getWorld().playSound(location, Sound.ENTITY_ENDER_DRAGON_FLAP, 1, 1);
    }

    private Location getNextLocation(Location currentLocation) {
        Vector direction = currentLocation.getDirection().normalize().multiply(0.5); 
        return currentLocation.add(direction);
    }
}