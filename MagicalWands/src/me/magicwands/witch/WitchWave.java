package me.magicwands.witch;

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

import me.magicwands.main.Main;
import me.magicwands.utils.ChatUtil;
import me.magicwands.utils.CooldownManager;

public class WitchWave implements Listener {
    private CooldownManager cooldownManager;
    private long cooldownTimeInSeconds;

    public WitchWave() {
        this.cooldownTimeInSeconds = Main.getPlugin(Main.class).getConfig().getLong("spells.WitchWave.cooldown", 10L);
        this.cooldownManager = new CooldownManager(cooldownTimeInSeconds);
        this.cooldownManager.setDefaultCooldownIfNotExist(cooldownTimeInSeconds, "WitchWave");
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (player.getItemInHand().getItemMeta() != null && 
        	    player.getItemInHand().getItemMeta().getDisplayName() != null && 
        	    player.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(me.magicwands.wands.WitchWand.WitchWand.getItemMeta().getDisplayName()) && 
        	    event.getAction() == Action.LEFT_CLICK_AIR && 
        	    (me.magicwands.events.WitchWand.WitchSpell.getOrDefault(player, 0) != 0 && me.magicwands.events.WitchWand.WitchSpell.get(player) == 4)) { 

            if (player.hasPermission("magicwands.witch.witchwave.bypasscooldown") || player.hasPermission("magicwands.witch.*")) {
                castWitchWave(player, player);
                return;
            }

            long cooldownTimeInMillis = cooldownManager.getCooldownDuration(player, "WitchWave");
            if (this.cooldownManager.hasCooldown(player, "WitchWave")) {
                long secondsRemaining = this.cooldownManager.getCooldownRemaining(player, "WitchWave");
                String color = Main.getPlugin(Main.class).getConfig().getString("wands.witch.color");
                player.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes((char) '&',
                        (ChatUtil.color("&7[&" + color + "MagicWands&7] &fThis spell is still on a &" + color + "cooldown&c&l! &fYou have to wait &" + color + secondsRemaining + " &fseconds"))));
                return;
            }
            this.cooldownManager.startCooldown(player, cooldownTimeInMillis, "WitchWave");

            castWitchWave(player, player);
        }
    }

    private void castWitchWave(Player caster, Player player) {
        Location initialLocation = player.getLocation().add(player.getLocation().getDirection().normalize().multiply(2));
        Vector direction = initialLocation.getDirection().normalize().multiply(0.5); 

        BukkitTask witchTrailTask = Bukkit.getScheduler().runTaskTimer(Main.getPlugin(Main.class), new Runnable() {
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

                spawnWitchDisappearParticles(currentLocation)	;
                
               
                for (Entity entity : currentLocation.getWorld().getNearbyEntities(currentLocation, 1, 1, 1)) {
                    if (entity instanceof Player) {

                        Player nearbyPlayer = (Player) entity;
                   	 if(!nearbyPlayer.equals(caster)) {
                        Vector direction = nearbyPlayer.getLocation().getDirection().normalize().multiply(-1);
                        Location playerloc = player.getLocation();
                    	Location targetloc = nearbyPlayer.getLocation();
                    	player.teleport(targetloc);
                    	nearbyPlayer.teleport(playerloc);
                        nearbyPlayer.setVelocity(direction.multiply(2));
                        nearbyPlayer.getWorld().playSound(nearbyPlayer.getLocation(), Sound.BLOCK_LAVA_EXTINGUISH, 1, 1);
                        Particle.DustOptions Purple = new Particle.DustOptions(Color.fromRGB(138, 43, 226), 1.0f);
                        nearbyPlayer.getWorld().spawnParticle(Particle.REDSTONE, nearbyPlayer.getLocation(), 8, 0.9, 0.9, 0.9, 0.03, Purple);
                        nearbyPlayer.getWorld().spawnParticle(Particle.SPELL_WITCH, nearbyPlayer.getLocation(), 20, 0.9, 0.9, 0.9, 0.01);
                        nearbyPlayer.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, nearbyPlayer.getLocation(), 50, 0.9, 0.9, 0.9, 0.01);
                        Particle.DustOptions Purple2 = new Particle.DustOptions(Color.PURPLE, 1.0f);
                        nearbyPlayer.getWorld().spawnParticle(Particle.REDSTONE, nearbyPlayer.getLocation(), 8, 0.9, 0.9, 0.9, 0.03, Purple2);
                        nearbyPlayer.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 100, 2));
                        nearbyPlayer.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 100, 2));
                    } else {
                    	return;
                    }
                }
                }
            }
        }, 0L, 1L);
        
        BukkitTask witchWaveSound = Bukkit.getScheduler().runTaskTimer(Main.getPlugin(Main.class), new Runnable() {
            Location currentLocation = player.getLocation();

            @Override
            public void run() {
                spawnWitchWaveSound(currentLocation);
                currentLocation = getNextLocation(currentLocation);

            }
        }, 0L, 2L);

        new BukkitRunnable() {
            @Override
            public void run() {
                witchTrailTask.cancel();
                witchWaveSound.cancel();
            }
        }.runTaskLater(Main.getPlugin(Main.class), 60L);
    }

    private void spawnWitchDisappearParticles(Location location) {
        location.getWorld().spawnParticle(Particle.SPELL_WITCH, location, 10, 0.9, 0.9, 0.9, 0.01);
        location.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, location, 20, 0.9, 0.9, 0.9, 0.01);
        Particle.DustOptions Purple = new Particle.DustOptions(Color.fromRGB(138, 43, 226), 1.0f);
        Particle.DustOptions Purple2 = new Particle.DustOptions(Color.PURPLE, 1.0f);
        location.getWorld().spawnParticle(Particle.REDSTONE, location, 8, 0.9, 0.9, 0.9, 0.03, Purple2);
        location.getWorld().spawnParticle(Particle.REDSTONE, location, 8, 0.9, 0.9, 0.9, 0.03, Purple);
    }

    private void spawnWitchWaveSound(Location location) {
        location.getWorld().playSound(location, Sound.ENTITY_WITCH_AMBIENT, 1, 1);
    }

    private Location getNextLocation(Location currentLocation) {
        Vector direction = currentLocation.getDirection().normalize().multiply(0.5);
        return currentLocation.add(direction);
    }
}
