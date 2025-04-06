package me.magicwands.ice;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import me.magicwands.main.Main;
import me.magicwands.utils.CalculateVelocity;
import me.magicwands.utils.ChatUtil;
import me.magicwands.utils.CooldownManager;

public class IceLeap implements Listener {

    private CooldownManager cooldownManager;
    private Map<UUID, BukkitRunnable> particleTasks = new HashMap<>();
    private Map<UUID, Boolean> leapingPlayers = new HashMap<>();
    private Map<UUID, Boolean> hasActiveParticles = new HashMap<>(); 

    public IceLeap() {
        long cooldownTimeInSeconds = Main.getPlugin(Main.class).getConfig().getLong("spells.IceLeap.cooldown", 30L); 
        this.cooldownManager = new CooldownManager(cooldownTimeInSeconds);
        this.cooldownManager.setDefaultCooldownIfNotExist(cooldownTimeInSeconds, "IceLeap");
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (player.getItemInHand().getItemMeta() != null && 
        	    player.getItemInHand().getItemMeta().getDisplayName() != null && 
        	    player.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(me.magicwands.wands.IceWand.IceWand.getItemMeta().getDisplayName()) && 
        	    event.getAction() == Action.LEFT_CLICK_AIR && 
        	    me.magicwands.events.IceWand.IceSpell.getOrDefault(player, 0) == 6) {
            boolean isStaff = player.hasPermission("magicwands.*");
    	
            if (player.hasPermission("magicwands.ice.coldleap.bypasscooldown") || player.hasPermission("magicwands.ice.bypasscooldown") || isStaff) {
                this.leapPlayer(player, isStaff);
                return;
            }

            long cooldownTimeInMillis = cooldownManager.getCooldownDuration(player, "IceLeap");
            if (this.cooldownManager.hasCooldown(player, "IceLeap")) {
                long secondsRemaining = this.cooldownManager.getCooldownRemaining(player, "IceLeap");
                String color = Main.getPlugin(Main.class).getConfig().getString("wands.ice.color");
                player.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&',
                        ChatUtil.color("&7[&" + color + "MagicWands&7] &fThis spell is still on a &" + color + "cooldown&c&l! &fYou have to wait &" + color +
                                secondsRemaining + " &fseconds")));
                return;
            }
            this.cooldownManager.startCooldown(player, cooldownTimeInMillis, "IceLeap");
            this.leapPlayer(player, isStaff);
        }
        }
    

    @SuppressWarnings("deprecation")
	private void leapPlayer(final Player player, final boolean isStaff) {
        long cooldownDuration = isStaff ? 0L : this.cooldownManager.getCooldownDuration(player, "IceLeap");

        this.cooldownManager.startCooldown(player, cooldownDuration, "IceLeap");
        leapingPlayers.put(player.getUniqueId(), true);
       

        player.setVelocity(CalculateVelocity.calculateVelocity(player.getLocation().getDirection(), isStaff));
        this.playLeapSound(player.getLocation().getWorld(), player.getLocation());
        Bukkit.getScheduler().runTaskLater(Main.getPlugin(Main.class), () -> {
            if (!player.isOnline() || player.isOnGround()) {
                return; 
            }
	
            if (!hasActiveParticles.getOrDefault(player.getUniqueId(), false)) {
                BukkitRunnable particleTask = new BukkitRunnable() {
                    public void run() {
                        if (!player.isOnline()) {
                            leapingPlayers.put(player.getUniqueId(), false);
                            cancelParticleTask(player.getUniqueId(), false);
                            this.cancel();
                            return;
                        }

                        if (player.isOnGround()) {
                            leapingPlayers.put(player.getUniqueId(), false);
                            cancelParticleTask(player.getUniqueId(), true);
                            this.cancel();
                        } else {
                            playIceParticles(player.getLocation()); // Use Ice particles
                        }
                    }
                };
                particleTask.runTaskTimer(Main.getPlugin(Main.class), 1L, 1L);
                this.particleTasks.put(player.getUniqueId(), particleTask);
                hasActiveParticles.put(player.getUniqueId(), true);
            }
        }, 8L); 

        
        BukkitRunnable groundCheckTask = new BukkitRunnable() {
            public void run() {
                if (!player.isOnline() || player.isOnGround()) {
                    leapingPlayers.put(player.getUniqueId(), false);
                    cancelParticleTask(player.getUniqueId(), true);
                    this.cancel();
                }
            }
        };
        groundCheckTask.runTaskTimer(Main.getPlugin(Main.class), 40L, 100L); 
        

    }


    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            
           
            if (leapingPlayers.getOrDefault(player.getUniqueId(), false) && event.getCause() == DamageCause.FALL) {
                event.setCancelled(true);
                cancelParticleTask(player.getUniqueId(), false);
            }
        }
    }

    @SuppressWarnings("deprecation")
	@EventHandler
    public void LeapEffect(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (player.isOnGround() && leapingPlayers.getOrDefault(player.getUniqueId(), false)) {
            Vector velocity = player.getVelocity();
            if (velocity.getY() == 0) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        leapingPlayers.put(player.getUniqueId(), false);
                        cancelParticleTask(player.getUniqueId(), true);
                    }
                }.runTaskLater(Main.getPlugin(Main.class), 5L);
            }
       
        
        }
        
    }

 

    private void playIceParticles(Location location) {
        World world = location.getWorld();
        if (world != null) {
            Particle.DustOptions iceShardColor = new Particle.DustOptions(Color.fromRGB(170, 255, 255), 1.0f);
            Particle.DustOptions cyanColor = new Particle.DustOptions(Color.AQUA, 1.0f);
            location.getWorld().spawnParticle(Particle.REDSTONE, location, 2, 0.4, 0.4, 0.4, 0.03, iceShardColor);
            location.getWorld().spawnParticle(Particle.REDSTONE, location, 2, 0.4, 0.4, 0.4, 0.03, cyanColor);
            location.getWorld().spawnParticle(Particle.CLOUD, location, 2, 0.2, 0.3, 0.2, 0.001);
            location.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, location, 30, 0.3, 0.5, 0.3, 0.01);
            location.getWorld().spawnParticle(Particle.BLOCK_CRACK, location, 2, 0.4, 0.4, 0.4, 0.03, Material.ICE.createBlockData());
        }
    }

    private void playLeapSound(World world, Location location) {
        if (world != null) {
            world.playSound(location, Sound.ITEM_TRIDENT_THROW, 1.0f, -3.0f);
            world.playSound(location, Sound.BLOCK_FIRE_EXTINGUISH, 1.0f, 3.0f);
        }
    }

    private void cancelParticleTask(UUID playerId, boolean isStaff) {
        if (this.particleTasks.containsKey(playerId)) {
            BukkitRunnable particleTask = this.particleTasks.get(playerId);
            particleTask.cancel();
            this.particleTasks.remove(playerId);
            hasActiveParticles.put(playerId, false);
            if (isStaff) {
                leapingPlayers.put(playerId, false);
            }
        }
    }
}
