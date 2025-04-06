package me.magicwands.dark;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
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
import me.magicwands.wands.DarkWand;

public class DarkLeap implements Listener {
    private CooldownManager cooldownManager;
    private long cooldownTimeInSeconds;
    private Map<UUID, Boolean> leapingPlayers = new HashMap<>();
    private Map<UUID, BukkitRunnable> particleTasks = new HashMap<>();
    private Map<UUID, Boolean> hasActiveParticles = new HashMap<>();

    public DarkLeap() {
        this.cooldownTimeInSeconds = Main.getPlugin(Main.class).getConfig().getLong("spells.DarkLeap.cooldown", 10L);
        this.cooldownManager = new CooldownManager(cooldownTimeInSeconds);
        this.cooldownManager.setDefaultCooldownIfNotExist(cooldownTimeInSeconds, "DarkLeap");
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (player.getItemInHand() != null && player.getItemInHand().getItemMeta() != null) {
            String itemDisplayName = player.getItemInHand().getItemMeta().getDisplayName();
            if (player.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(DarkWand.DarkWand.getItemMeta().getDisplayName())
                    && event.getAction() == Action.LEFT_CLICK_AIR && me.magicwands.events.DarkWand.DarkSpell.get(player) == 1) {
                boolean isStaff = player.hasPermission("magicwands.dark.darkleap.bypasscooldown") || player.hasPermission("magicwands.dark.*");
                this.leapPlayer(player, isStaff);
            }
        }
    }

    private void leapPlayer(final Player player, final boolean isStaff) {
        long remainingTime;
        long cooldownDuration = isStaff ? 0L : this.cooldownManager.getCooldownDuration(player, "DarkLeap");

        if (this.cooldownManager.hasCooldown(player, "DarkLeap") && (remainingTime = this.cooldownManager.getCooldownRemaining(player, "DarkLeap")) > 0L) {
            String color = Main.getPlugin(Main.class).getConfig().getString("wands.dark.color");
            player.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes((char) '&',
                    (ChatUtil.color("&7[&" + color + "MagicWands&7] &fThis spell is still on a &" + color + "cooldown&c&l! &fYou have to wait &" + color +
                            + remainingTime + " &fseconds"))));
            return;
        }

        this.cooldownManager.startCooldown(player, cooldownDuration, "DarkLeap");
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
                            playDarkParticles(player.getLocation());
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
    public void LeapEffect(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (player.isOnGround() && leapingPlayers.getOrDefault(player.getUniqueId(), false)) {
            Vector velocity = player.getVelocity();
            if (velocity.getY() == 0) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        leapingPlayers.put(player.getUniqueId(), false);
                        cancelParticleTask(player.getUniqueId(), false);
                    }
                }.runTaskLater(Main.getPlugin(Main.class), 5L);
            }
        }
    }

    private void playDarkParticles(Location location) {
        World world = location.getWorld();
        if (world != null) {
            world.spawnParticle(Particle.SMOKE_LARGE, location, 4, 0.6, 0.6, 0.6, 0.004);
            world.spawnParticle(Particle.ASH, location, 17, 0.5, 0.8, 0.7, 0.004);
            world.playEffect(location, Effect.ENDER_SIGNAL, 1);
        }
    }

    private void playLeapSound(World world, Location location) {
        if (world != null) {
            world.playSound(location, Sound.BLOCK_LAVA_POP, 1.0f, 3.0f);
            world.playSound(location, Sound.ENTITY_ENDERMAN_TELEPORT, 1.0F, 1.0F);
        }
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
