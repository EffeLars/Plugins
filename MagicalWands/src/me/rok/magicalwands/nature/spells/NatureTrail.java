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
package me.rok.magicalwands.nature.spells;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import me.lars.game.Main;
import me.lars.game.utils.ChatUtil;
import me.lars.game.utils.ParticleColor;
import me.lars.game.wands.NatureWand;

public class NatureTrail implements Listener {
    private static final long STAFF_COOLDOWN = 0000L;
    private static final long PLAYER_COOLDOWN = 30000L;
    private Map<UUID, BukkitRunnable> particleTasks = new HashMap<>();
    private Map<UUID, Long> cooldowns = new HashMap<>();
    private BukkitTask NatureTrailTask = null;
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (player.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(NatureWand.NatureWand.getItemMeta().getDisplayName())
                && event.getAction() == Action.LEFT_CLICK_AIR && me.lars.game.event.NatureWand.NatureSpell.get(player) == 5) {
            boolean isStaff = player.hasPermission("staff.wand");
            
            if (player.getScoreboardTags().contains("naturetrail")) {
                player.removeScoreboardTag("naturetrail");
                if (NatureTrailTask != null) {
                    NatureTrailTask.cancel();
                    NatureTrailTask = null;
                }
                String color = Main.getPlugin(Main.class).getConfig().getString("wands.nature.color");
                player.sendMessage(ChatUtil.color("&7[&" + color + "Magic&7] &" + color + "Stopped the Nature trail.."));
            } else {
                player.addScoreboardTag("naturetrail");
                String color = Main.getPlugin(Main.class).getConfig().getString("wands.nature.color");
                player.sendMessage(ChatUtil.color("&7[&" + color + "Magic&7] &" + color + "Started the Nature trail.."));

                NatureTrailTask = Bukkit.getScheduler().runTaskTimer(Main.getPlugin(Main.class), new Runnable() {
                    @Override
                    public void run() {
                        spawnNatureTrailParticles(player);
                    }
                }, 0L, 1L); 
            }
        }
    }

    @EventHandler
    public void RemoveNatureTrail(PlayerJoinEvent event) {
    	Player p = event.getPlayer();
    	if(p.getScoreboardTags().contains("naturetrail")) {
    		p.removeScoreboardTag("naturetrail");
    	}
    }
    
    private void spawnNatureTrailParticles(Player player) {
    		Particle.DustOptions cyanColor = new Particle.DustOptions(Color.AQUA, 1.0f);
            Particle.DustOptions blueColor = new Particle.DustOptions(Color.fromRGB(0, 0, 255), 1.0f);
            Particle.DustOptions iceShardColor = new Particle.DustOptions(Color.fromRGB(170, 255, 255), 1.0f);
           player.getWorld().spawnParticle(Particle.REDSTONE, player.getLocation(), 1, 1.0, 1.0, 1.0, 0.03, ParticleColor.GREEN);
           player.getWorld().spawnParticle(Particle.REDSTONE, player.getLocation(), 1, 1.0, 1.0, 1.0, 0.03, ParticleColor.LIME_GREEN);
           player.getWorld().spawnParticle(Particle.REDSTONE, player.getLocation(), 1, 1.0, 1.0, 1.0, 0.03, ParticleColor.DARK_GREEN);
           player.getWorld().spawnParticle(Particle.BLOCK_CRACK, player.getLocation(), 2, 0.0, 0.0, 0.0, 0.03, Material.BAMBOO.createBlockData());
           player.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, player.getLocation(), 5, 1.0, 1.0, 1.0, 0.01);
            for (int i = 0; i < 10; i++) {
                double offsetX = Math.random() * 0.6 - 0.3;
                double offsetY = Math.random() * 0.6 - 0.3;
                double offsetZ = Math.random() * 0.6 - 0.3;
                player.getWorld().spawnParticle(Particle.REDSTONE, player.getLocation().getX() + offsetX, player.getLocation().getY() + offsetY, player.getLocation().getZ() + offsetZ, 1, ParticleColor.LIGHT_GREEN);
            }
}
}
