package me.magicwands.empire;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
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
import org.bukkit.scheduler.BukkitRunnable;

import me.magicwands.main.Main;
import me.magicwands.utils.CalculateVelocity;
import me.magicwands.utils.ChatUtil;
import me.magicwands.utils.CooldownManager;
import me.magicwands.utils.ParticleColor;
import me.magicwands.wands.WandItems;

public class EmpireLeap implements Listener {
    private CooldownManager cooldownManager;
    private long cooldownTimeInSeconds;
    private Map<UUID, Boolean> leapingPlayers = new HashMap<>();
    private Map<UUID, BukkitRunnable> particleTasks = new HashMap<>();
    private Map<UUID, Boolean> hasActiveParticles = new HashMap<>();

    public EmpireLeap() {
        this.cooldownTimeInSeconds = Main.getPlugin(Main.class).getConfig().getLong("spells.EmpireLeap.cooldown", 10L);
        this.cooldownManager = new CooldownManager(cooldownTimeInSeconds);
        this.cooldownManager.setDefaultCooldownIfNotExist(cooldownTimeInSeconds, "EmpireLeap");
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (player.getItemInHand().getItemMeta() != null && 
                player.getItemInHand().getItemMeta().getDisplayName() != null && 
                player.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(WandItems.EmpireWand.getItemMeta().getDisplayName()) && 
                event.getAction() == Action.LEFT_CLICK_AIR && 
                me.magicwands.events.EmpireWand.EmpireSpell.getOrDefault(player, 0) == 1) { 
            boolean isStaff = player.hasPermission("magicwands.empire.empireleap.bypasscooldown") || player.hasPermission("magicwands.empire.*");
            this.leapPlayer(player, isStaff);
        }
    }

    private void leapPlayer(final Player player, final boolean isStaff) {
        long remainingTime;
        long cooldownDuration = isStaff ? 0L : this.cooldownManager.getCooldownDuration(player, "EmpireLeap");

        if (this.cooldownManager.hasCooldown(player, "EmpireLeap") && (remainingTime = this.cooldownManager.getCooldownRemaining(player, "EmpireLeap")) > 0L) {
            String color = Main.getPlugin(Main.class).getConfig().getString("wands.empire.color");
            player.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&',
                    ChatUtil.color("&7[&" + color + "MagicWands&7] &fThis spell is still on a &" + color + "cooldown&c&l! &fYou have to wait &" + color +
                            remainingTime + " &fseconds")));
            return;
        }

        this.cooldownManager.startCooldown(player, cooldownDuration, "EmpireLeap");
        leapingPlayers.put(player.getUniqueId(), true);

        player.setVelocity(CalculateVelocity.calculateVelocity(player.getLocation().getDirection(), isStaff));
        this.playLeapSound(player.getLocation().getWorld(), player.getLocation(), player);

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
                            playFireParticles(player.getLocation());
                        }
                    }
                };
                particleTask.runTaskTimer(Main.getPlugin(Main.class), 1L, 1L);
                this.particleTasks.put(player.getUniqueId(), particleTask);
                hasActiveParticles.put(player.getUniqueId(), true);
            }
        }, 8L); 
    }

    private void playFireParticles(Location location) {
        World world = location.getWorld();
        if (world != null) {
            world.spawnParticle(Particle.ASH, location, 2, 0.3, 0.5, 0.3, 0.001);
            world.spawnParticle(Particle.ENCHANTMENT_TABLE, location, 8, 0.3, 0.5, 0.3, 0.001);
            world.spawnParticle(Particle.SMOKE_NORMAL, location, 2, 0.3, 0.5, 0.3, 0.001);
            world.spawnParticle(Particle.BLOCK_CRACK, location, 3, 0.3, 0.5, 0.3, 0.03, Material.NETHER_PORTAL.createBlockData());
            world.spawnParticle(Particle.REDSTONE, location, 2, 1.0, 1.0, 1.0, 0.03, ParticleColor.PURPLE);
            world.spawnParticle(Particle.REDSTONE, location, 2, 1.0, 1.0, 1.0, 0.03, ParticleColor.DARK_PURPLE);
            world.playEffect(location, Effect.ENDER_SIGNAL, 1);
        }
    }

    private void playLeapSound(World world, Location location, Player player) {
        if (world != null) {
            world.playSound(location, Sound.ENTITY_WITHER_AMBIENT, 1.0f, 1.0f);
            world.playSound(location, Sound.ENTITY_ENDERMAN_TELEPORT, 1.0F, 1.0F);
            player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, Integer.MAX_VALUE, -1.0F);
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
