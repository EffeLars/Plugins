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
package me.rok.magicalwands.fire.spells;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
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

import me.lars.game.Main;
import me.lars.game.event.FlameWand;
import me.lars.game.utils.ChatUtil;
import me.lars.game.wands.Wands;

public class FireLeap implements Listener {
    private static final double FORWARD_POWER_MODIFIER = 1.5;
    private static final double UPWARD_POWER_MODIFIER = 3.0;
    private static final double FORWARD_VELOCITY = 2.0;
    private static final double UPWARD_VELOCITY = 0.7;
    private static final int PARTICLE_DELAY = 1;
    static long leapcooldownp = Main.getPlugin(Main.class).getConfig().getInt("spells.fireleap.playercooldown");
    private static final long PLAYER_COOLDOWN_SECONDS = leapcooldownp; // cooldown in seconds
    private static final long PLAYER_COOLDOWN_MS = PLAYER_COOLDOWN_SECONDS * 1000; // cooldown in milliseconds
    static long leapcooldowns = Main.getPlugin(Main.class).getConfig().getInt("spells.fireleap.staffcooldown");
    private static final long STAFF_COOLDOWN_SECONDS = leapcooldowns; // cooldown in seconds
    private static final long STAFF_COOLDOWN_MS = STAFF_COOLDOWN_SECONDS * 1000; // cooldown in milliseconds
    
    private Map<UUID, BukkitRunnable> particleTasks = new HashMap<>();
    private Map<UUID, Long> cooldowns = new HashMap<>();

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (player.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(Wands.IgnatiusWand.getItemMeta().getDisplayName())
                && event.getAction() == Action.LEFT_CLICK_AIR && FlameWand.Spell.get(player) == 1) {
            boolean isStaff = player.hasPermission("staff.wand");
            this.cancelParticleTask(player.getUniqueId(), isStaff);
            this.leapPlayer(player, isStaff);
        }
    }

    private void leapPlayer(final Player player, final boolean isStaff) {
        long remainingTime;
        long cooldownDuration = isStaff ? STAFF_COOLDOWN_MS : PLAYER_COOLDOWN_MS;

        if (this.cooldowns.containsKey(player.getUniqueId()) && (remainingTime = this.cooldowns.get(player.getUniqueId()) - System.currentTimeMillis()) > 0L) {
            player.sendMessage(ChatUtil.color("&7[&dMagic&7] &fThis spell is still on a &d&lCOOLDOWN&c&l! &fYou have to wait &d" + remainingTime / 1000L + " &fseconds"));
            return;
        }
        this.cooldowns.put(player.getUniqueId(), System.currentTimeMillis() + cooldownDuration);

        player.setVelocity(this.calculateVelocity(player.getLocation().getDirection(), isStaff));
        this.playLeapSound(player.getLocation().getWorld(), player.getLocation());
        player.addScoreboardTag("IceLeapingd");
        player.addScoreboardTag("IceLeaping");

        BukkitRunnable particleTask = new BukkitRunnable() {
            public void run() {
                if (!player.isOnline()) {
                    player.getScoreboardTags().remove("IceLeaping");
                    FireLeap.this.cancelParticleTask(player.getUniqueId(), isStaff);
                    return;
                }
                FireLeap.this.playFireParticles(player.getLocation());
            }
        };
        particleTask.runTaskTimer(Main.getPlugin(Main.class), 1L, 1L);
        this.particleTasks.put(player.getUniqueId(), particleTask);
    }

    @EventHandler
    public void LeapEffect(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (player.isOnGround()) {
            if (player.getScoreboardTags().contains("IceLeaping")) {
                player.removeScoreboardTag("IceLeaping");
                this.cancelParticleTask(player.getUniqueId(), true);
            }
        }
    }
    
    private Vector calculateVelocity(Vector direction, boolean isStaff) {
        double forwardPowerModifier = isStaff ? 1.5 : 0.75;
        double upwardPowerModifier = isStaff ? 3.0 : 1.5;
        Vector velocity = direction.normalize().multiply(2.0 * forwardPowerModifier);
        velocity.setY(0.7 * upwardPowerModifier);
        return velocity;
    }

    private void playFireParticles(Location location) {
        World world = location.getWorld();
        if (world != null) {
            world.spawnParticle(Particle.FLAME, location, 2, 0.0, 0.0, 0.0, 0.04);
            world.spawnParticle(Particle.SMOKE_LARGE, location, 1, 0.0, 0.0, 0.0, 0.03);
            world.spawnParticle(Particle.REVERSE_PORTAL, location, 1, 0.0, 0.0, 0.0, 0.1);
        }
    }

    private void playLeapSound(World world, Location location) {
        if (world != null) {
            world.playSound(location, Sound.ITEM_TRIDENT_THROW, 1.0f, -3.0f);
            world.playSound(location, Sound.BLOCK_FIRE_EXTINGUISH, 1.0f, 3.0f);
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if ((player.getScoreboardTags().contains("IceLeapingd")) 
                    && event.getCause() == DamageCause.FALL) {;
                event.setCancelled(true);
                player.removeScoreboardTag("IceLeapingd");
            }
        }
    }


    private void cancelParticleTask(UUID playerId, boolean isStaff) {
        if (this.particleTasks.containsKey(playerId)) {
            Player player;
            BukkitRunnable particleTask = this.particleTasks.get(playerId);
            particleTask.cancel();
            this.particleTasks.remove(playerId);
            if (isStaff && (player = Bukkit.getPlayer((UUID)playerId)) != null) {
                player.getScoreboardTags().remove("IceLeaping");
            }
        }
    }
}

