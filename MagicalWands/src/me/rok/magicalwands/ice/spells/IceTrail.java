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
package me.rok.magicalwands.ice.spells;

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
import me.lars.game.wands.IceWand;

public class IceTrail implements Listener {
    private static final long STAFF_COOLDOWN = 0000L;
    private static final long PLAYER_COOLDOWN = 30000L;
    private Map<UUID, BukkitRunnable> particleTasks = new HashMap<>();
    private Map<UUID, Long> cooldowns = new HashMap<>();
    private BukkitTask fireTrailTask = null;
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (player.getItemInHand().getItemMeta() != null &&
            player.getItemInHand().getItemMeta().getDisplayName() != null &&
            player.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(IceWand.IceWand.getItemMeta().getDisplayName()) &&
            event.getAction() == Action.LEFT_CLICK_AIR && me.lars.game.event.IceWand.IceSpell.get(player) == 8) {
            boolean isStaff = player.hasPermission("staff.wand");
            
            if (player.getScoreboardTags().contains("icetrail")) {
                player.removeScoreboardTag("icetrail");
                if (fireTrailTask != null) {
                    fireTrailTask.cancel();
                    fireTrailTask = null;
                }
                String color = Main.getPlugin(Main.class).getConfig().getString("wands.ice.color");
                player.sendMessage(ChatUtil.color("&7[&" + color + "Magic&7] &" + color + "Stopped the ice trail.."));
            } else {
                player.addScoreboardTag("icetrail");
                String color = Main.getPlugin(Main.class).getConfig().getString("wands.ice.color");
                player.sendMessage(ChatUtil.color("&7[&" + color + "Magic&7] &" + color + "Started the ice trail.."));

                fireTrailTask = Bukkit.getScheduler().runTaskTimer(Main.getPlugin(Main.class), new Runnable() {
                    @Override
                    public void run() {
                        spawnIceTrailParticles(player);
                    }
                }, 0L, 1L); 
            }
        }
    }

    @EventHandler
    public void RemoveTrail(PlayerJoinEvent event) {
    	Player p = event.getPlayer();
    	if(p.getScoreboardTags().contains("icetrail")) {
    		p.removeScoreboardTag("icetrail");
    	}
    }
    
    private void spawnIceTrailParticles(Player player) {
    		Particle.DustOptions cyanColor = new Particle.DustOptions(Color.AQUA, 1.0f);
            Particle.DustOptions blueColor = new Particle.DustOptions(Color.fromRGB(0, 0, 255), 1.0f);

            double blueRed = 0.0; 
            double blueGreen = 0.0;
            double blueBlue = 1.0;
            double cyanRed = 0.0;
            double cyanGreen = 1.0;
            double cyanBlue = 1.0;

            Particle.DustOptions iceShardColor = new Particle.DustOptions(Color.fromRGB(170, 255, 255), 1.0f);

         
           player.getWorld().spawnParticle(Particle.REDSTONE, player.getLocation(), 1, 1.0, 1.0, 1.0, 0.03, iceShardColor);
           player.getWorld().spawnParticle(Particle.BLOCK_CRACK, player.getLocation(), 2, 0.0, 0.0, 0.0, 0.03, Material.ICE.createBlockData());
           player.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, player.getLocation(), 5, 1.0, 1.0, 1.0, 0.01);

            for (int i = 0; i < 10; i++) {
                double offsetX = Math.random() * 0.6 - 0.3;
                double offsetY = Math.random() * 0.6 - 0.3;
                double offsetZ = Math.random() * 0.6 - 0.3;
                player.getWorld().spawnParticle(Particle.REDSTONE, player.getLocation().getX() + offsetX, player.getLocation().getY() + offsetY, player.getLocation().getZ() + offsetZ, 1, iceShardColor);
            }
}
}
