/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.Particle
 *  org.bukkit.Sound
 *  org.bukkit.World
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.block.Action
 *  org.bukkit.event.entity.EntityDamageEvent
 *  org.bukkit.event.entity.EntityDamageEvent$DamageCause
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 *  org.bukkit.util.Vector
 */
package me.magicwands.nature;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
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
import me.magicwands.wands.NatureWand;

public class NatureLeap implements Listener {
    private Map<UUID, BukkitRunnable> particleTasks = new HashMap<>();
    private Map<UUID, Boolean> leapingPlayers = new HashMap<>();
    private Map<UUID, Boolean> hasActiveParticles = new HashMap<>();
   

    private CooldownManager cooldownManager;

    public NatureLeap() {
        long cooldownTimeInSeconds = Main.getPlugin(Main.class).getConfig().getLong("spells.NatureLeap.cooldown", 30L); // default 30 seconds
        this.cooldownManager = new CooldownManager(cooldownTimeInSeconds);
        this.cooldownManager.setDefaultCooldownIfNotExist(cooldownTimeInSeconds, "NatureLeap");
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (player.getItemInHand().getItemMeta() != null && 
        	    player.getItemInHand().getItemMeta().getDisplayName() != null && 
        	    player.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(NatureWand.NatureWand.getItemMeta().getDisplayName()) && 
        	    event.getAction() == Action.LEFT_CLICK_AIR && 
        	    me.magicwands.events.NatureWand.NatureSpell.getOrDefault(player, 0) == 1) {
            boolean isStaff = player.hasPermission("magicwands.*");
            
        
            if (player.hasPermission("magicwands.nature.leap.bypasscooldown") || isStaff) {
                leapPlayer(player, isStaff);
                return;
            }
            
            long cooldownTimeInMillis = cooldownManager.getCooldownDuration(player, "NatureLeap");
            if (cooldownManager.hasCooldown(player, "NatureLeap")) {
                long secondsRemaining = cooldownManager.getCooldownRemaining(player, "NatureLeap");
                String color = Main.getPlugin(Main.class).getConfig().getString("wands.nature.color");
                player.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&',
                        ChatUtil.color("&7[&" + color + "MagicWands&7] &fThis spell is still on a &" + color + "cooldown&c&l! &fYou have to wait &" + color +
                                secondsRemaining + " &fseconds")));
                return;
            }
            cooldownManager.startCooldown(player, cooldownTimeInMillis, "NatureLeap");
            leapPlayer(player, isStaff);
        }
        }
    

    @SuppressWarnings("deprecation")
	private void leapPlayer(final Player player, final boolean isStaff) {
        long cooldownDuration = isStaff ? 0L : this.cooldownManager.getCooldownDuration(player, "NatureLeap");

        this.cooldownManager.startCooldown(player, cooldownDuration, "NatureLeap");
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
                            playNatureParticles(player.getLocation()); // Use Ice particles
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

 
    private void playNatureParticles(Location location) {
        World world = location.getWorld();
        if (world != null) {
            world.spawnParticle(Particle.VILLAGER_HAPPY, location, 2, 0.3, 0.5, 0.3, 0.001);
            world.spawnParticle(Particle.ENCHANTMENT_TABLE, location, 8, 0.3, 0.5, 0.3, 0.001);
            world.spawnParticle(Particle.TOTEM, location, 2, 0.3, 0.5, 0.3, 0.001);
            world.spawnParticle(Particle.SLIME, location, 2, 0.3, 0.5, 0.3, 0.001);
            world.spawnParticle(Particle.BLOCK_CRACK, location, 3, 0.3, 0.5, 0.3, 0.03, Material.OAK_LEAVES.createBlockData());
        }
    }

    private void playLeapSound(World world, Location location) {
        if (world != null) {
            world.playSound(location, Sound.ITEM_TRIDENT_THROW, 1.0f, -3.0f);
            world.playSound(location, Sound.BLOCK_BEACON_ACTIVATE, 1.0f, 3.0f);
        }
    }

    private void cancelParticleTask(UUID playerId, boolean isStaff) {
        if (particleTasks.containsKey(playerId)) {
            BukkitRunnable particleTask = particleTasks.get(playerId);
            particleTask.cancel();
            particleTasks.remove(playerId);
            hasActiveParticles.put(playerId, false);
            if (isStaff) {
                leapingPlayers.put(playerId, false);
            }
        }
    }}
