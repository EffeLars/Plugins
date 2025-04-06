package me.magicwands.empire;

import org.bukkit.Bukkit;
import org.bukkit.Color;
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

import me.magicwands.events.EmpireWand;
import me.magicwands.main.Main;
import me.magicwands.utils.ChatUtil;
import me.magicwands.utils.CooldownManager;
import me.magicwands.wands.WandItems;

public class EmpireConfusionWave implements Listener {
    private CooldownManager cooldownManager;
    private long cooldownTimeInSeconds;

    public EmpireConfusionWave() {
        this.cooldownTimeInSeconds = Main.getPlugin(Main.class).getConfig().getLong("spells.DarkWave.cooldown", 10L);
        this.cooldownManager = new CooldownManager(cooldownTimeInSeconds);
        this.cooldownManager.setDefaultCooldownIfNotExist(cooldownTimeInSeconds, "DarkWave");
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (player.getItemInHand().getItemMeta() != null && 
	            player.getItemInHand().getItemMeta().getDisplayName() != null && 
	            player.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(WandItems.EmpireWand.getItemMeta().getDisplayName()) && 
	            event.getAction() == Action.LEFT_CLICK_AIR && 
	            EmpireWand.EmpireSpell.getOrDefault(player, 0) == 5) {

            if (player.hasPermission("magicwands.empire.empireconfusionwave.bypasscooldown") || player.hasPermission("magicwands.empire.*")) {
            	EmpireConfusionWave(player); 
                return;
            }

            long cooldownTimeInMillis = cooldownManager.getCooldownDuration(player, "EmpireConfusionWave"); 
            if (this.cooldownManager.hasCooldown(player, "EmpireConfusionWave")) { 
                long secondsRemaining = this.cooldownManager.getCooldownRemaining(player, "EmpireConfusionWave"); 
                String color = Main.getPlugin(Main.class).getConfig().getString("wands.empire.color");
                player.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes((char) '&',
                        (ChatUtil.color("&7[&" + color + "MagicWands&7] &fThis spell is still on a &" + color + "cooldown&c&l! &fYou have to wait &" + color + secondsRemaining + " &fseconds"))));
                return;
            }
            this.cooldownManager.startCooldown(player, cooldownTimeInMillis, "EmpireConfusionWave"); 

            EmpireConfusionWave(player); 
        }
    }

    private void EmpireConfusionWave(Player player) {
        Location initialLocation = player.getLocation().add(player.getLocation().getDirection().normalize().multiply(2));
        Vector direction = initialLocation.getDirection().normalize().multiply(0.5); 

        BukkitTask EmpireConfusionWave = Bukkit.getScheduler().runTaskTimer(Main.getPlugin(Main.class), new Runnable() {
            Location currentLocation = initialLocation;

            @Override
            public void run() {
                Block blockInFront = currentLocation.clone().add(direction).getBlock();
                Block blockAbove = currentLocation.clone().add(0, 1, 0).getBlock();

                Vector updatedDirection = direction.clone();

                if (!blockInFront.getType().isAir()) {
                    updatedDirection = updatedDirection.multiply(-1);

                    if (blockAbove.getType().isAir()) {
                        updatedDirection = new Vector(0, 1, 0);
                    }
                }

                currentLocation = currentLocation.add(updatedDirection);

                SpawnEmpireParticles(currentLocation);
                

                for (Entity entity : currentLocation.getWorld().getNearbyEntities(currentLocation, 1, 1, 1)) {
                    if (entity instanceof Player) {
                        Player nearbyPlayer = (Player) entity;
                        Vector direction = nearbyPlayer.getLocation().getDirection().normalize().multiply(-1);
                        nearbyPlayer.getWorld().playSound(nearbyPlayer.getLocation(), Sound.ENTITY_WITCH_AMBIENT, 1, 1);
                        nearbyPlayer.getWorld().spawnParticle(Particle.ASH, nearbyPlayer.getLocation(), 30, 0.9, 0.9, 0.9, 0.01);
                        nearbyPlayer.getWorld().spawnParticle(Particle.SMOKE_NORMAL, nearbyPlayer.getLocation(), 20, 0.9, 0.9, 0.9, 0.01);
                        nearbyPlayer.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, nearbyPlayer.getLocation(), 50, 0.9, 0.9, 0.9, 0.01);
                        nearbyPlayer.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 320, 2));
                    }
                }
            }
        }, 0L, 1L);

        BukkitTask EmpireConfusionWaveSound = Bukkit.getScheduler().runTaskTimer(Main.getPlugin(Main.class), new Runnable() {
            Location currentLocation = player.getLocation();

            @Override
            public void run() {
                SpawnConfusionSound(currentLocation);
                currentLocation = getNextLocation(currentLocation);

            }
        }, 0L, 10L);

        new BukkitRunnable() {
            @Override
            public void run() {
                EmpireConfusionWave.cancel();
                EmpireConfusionWaveSound.cancel();
            }
        }.runTaskLater(Main.getPlugin(Main.class), 60L);
    }

    private void SpawnEmpireParticles(Location location) {
    	  Particle.DustOptions empireDarkPurple = new Particle.DustOptions(Color.fromRGB(75, 0, 130), 1.5f);
    	  location.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, location, 20, 2.0, 1.0, 1.0, 0.8);
    	  location.getWorld().spawnParticle(Particle.ASH, location, 20, 2.0, 1.0, 1.0, 0.8);
    	  location.getWorld().spawnParticle(Particle.REDSTONE, location, 10, 1, 1, 1, empireDarkPurple);
    	  location.getWorld().spawnParticle(Particle.SMOKE_NORMAL, location, 15, 1.0, 1.0, 1.0, 0.03);
    	  location.getWorld().spawnParticle(Particle.SMOKE_LARGE, location, 5, 1.0, 1.0, 1.0, 0.03);
    }

   

    private void SpawnConfusionSound(Location location) {
        location.getWorld().playSound(location, Sound.ENTITY_ENDER_DRAGON_FLAP, 1, 1);
        location.getWorld().playSound(location, Sound.ENTITY_HUSK_CONVERTED_TO_ZOMBIE, 1, 1);
    }

    private Location getNextLocation(Location currentLocation) {
        Vector direction = currentLocation.getDirection().normalize().multiply(0.5);
        return currentLocation.add(direction);
    }
}
