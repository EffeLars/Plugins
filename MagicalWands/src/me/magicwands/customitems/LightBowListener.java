/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  org.bukkit.Sound
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.EntityShootBowEvent
 *  org.bukkit.event.entity.ProjectileLaunchEvent
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.event.player.PlayerQuitEvent
 *  org.bukkit.event.player.PlayerSwapHandItemsEvent
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 */
package me.magicwands.customitems;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import me.magicwands.effects.old.LightWandEffect;
import me.magicwands.main.Main;
import me.magicwands.utils.ChatUtil;
import me.magicwands.wands.LightItems;

public class LightBowListener
implements Listener {
    private final Map<UUID, BukkitRunnable> broadcastTasks = new HashMap<UUID, BukkitRunnable>();

    public LightBowListener() {
        ((Main)Main.getPlugin(Main.class)).getServer().getPluginManager().registerEvents((Listener)this, (Plugin)Main.getPlugin(Main.class));
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();
        if (event.getItem() != null && event.getItem().getType().toString().contains("BOW") && event.getItem().getItemMeta() != null && event.getItem().getItemMeta().getDisplayName() != null) {
            if (event.getItem().getItemMeta().getDisplayName().equals(LightItems.HaldirionBow.getItemMeta().getDisplayName())) {
                if (event.getAction().name().contains("RIGHT")) {
                    if (!this.broadcastTasks.containsKey(playerId)) {
                        this.startBroadcastTask(player, playerId);
                    }
                } else if (event.getAction().name().contains("LEFT") && this.broadcastTasks.containsKey(playerId)) {
                    player.getScoreboardTags().remove("HaldirionBow");
                    this.stopBroadcastTask(playerId);
                }
            }
        }
    }


    @EventHandler
    public void onEntityShootBow(EntityShootBowEvent event) {
        Player player;
        UUID playerId;
        if (event.getEntity() instanceof Player && this.broadcastTasks.containsKey(playerId = (player = (Player)event.getEntity()).getUniqueId())) {
            this.stopBroadcastTask(playerId);
        }
    }

    @EventHandler
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        Player player;
        UUID playerId;
        if (event.getEntity().getShooter() instanceof Player && this.broadcastTasks.containsKey(playerId = (player = (Player)event.getEntity().getShooter()).getUniqueId())) {
            this.stopBroadcastTask(playerId);
        }
    }

    @EventHandler
    public void onPlayerSwapHandItems(PlayerSwapHandItemsEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();
        if (this.broadcastTasks.containsKey(playerId)) {
            this.stopBroadcastTask(playerId);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();
        if (this.broadcastTasks.containsKey(playerId)) {
            this.stopBroadcastTask(playerId);
        }
    }

    private void spawnparticles(Player player) {
        LightWandEffect wandAnimation = new LightWandEffect(player);
        wandAnimation.start();
    }

    private void startBroadcastTask(final Player player, final UUID playerId) {
        this.stopBroadcastTask(playerId);
        BukkitRunnable task = new BukkitRunnable(){
            private final long startTime = System.currentTimeMillis();
            int time = Main.getPlugin(Main.class).getConfig().getInt("wands.light.bowtime");
            public void run() {
                if (player.isOnline()) {
                    if (player.getItemInHand().getItemMeta().getDisplayName().equals(LightItems.HaldirionBow.getItemMeta().getDisplayName())) {
                        player.getInventory().getItemInMainHand().getType().toString().contains("BOW");
                        String message = ChatUtil.color("&7[&dMagic&7] &eYou are charging your special ability...");
                        long duration = (System.currentTimeMillis() - this.startTime) / 1000L;
                        if (duration == time) {
                            LightBowListener.this.spawnparticles(player);
                            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.0f);
                            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 2.0f);
                            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, -1.0f);
                            player.getScoreboardTags().add("HaldirionBow");
                        }
                        if (duration > 3L && player.getItemInHand().getItemMeta().getDisplayName().equals(LightItems.HaldirionBow.getItemMeta().getDisplayName())) {
                            LightBowListener.this.spawnparticles(player);
                        }
                    } else {
                        LightBowListener.this.stopBroadcastTask(playerId);
                    }
                } else {
                    LightBowListener.this.stopBroadcastTask(playerId);
                }
            }
        };
        this.broadcastTasks.put(playerId, task);
        task.runTaskTimer((Plugin)Main.getPlugin(Main.class), 0L, 20L);
    }

    private void stopBroadcastTask(UUID playerId) {
        BukkitRunnable task = this.broadcastTasks.remove(playerId);
        if (task != null) {
            task.cancel();
        }
    }
}

