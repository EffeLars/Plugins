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
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import me.lars.game.Main;
import me.lars.game.event.FlameWand;
import me.lars.game.utils.ChatUtil;
import me.lars.game.wands.IceWand;

public class IceDisappear implements Listener {
    private static final long STAFF_COOLDOWN = 0000L;
    private static final long PLAYER_COOLDOWN = 30000L;
    private Map<UUID, BukkitRunnable> particleTasks = new HashMap<>();
    private Map<UUID, Long> cooldowns = new HashMap<>();
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (player.getItemInHand().getItemMeta() != null &&
                player.getItemInHand().getItemMeta().getDisplayName() != null &&
                player.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(IceWand.IceWand.getItemMeta().getDisplayName()) &&
                event.getAction() == Action.LEFT_CLICK_AIR && me.lars.game.event.IceWand.IceSpell.get(player) == 4) {

            boolean isHidden = player.getScoreboardTags().contains("icedisappear");
            String color = Main.getPlugin(Main.class).getConfig().getString("wands.ice.color");
            player.sendMessage(ChatUtil.color("&7[&" + color + "Magic&7] &" + color + "Using spell..."));

            BukkitTask fireTrailTask = Bukkit.getScheduler().runTaskTimer(Main.getPlugin(Main.class), new Runnable() {
                @Override
                public void run() {
                    spawnFireDisappearParticles(player);
                }
            }, 0L, 1L);

            new BukkitRunnable() {
                @Override
                public void run() {
                    fireTrailTask.cancel();

                    if (isHidden) {
                        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                            onlinePlayer.showPlayer(Main.getPlugin(Main.class), player);
                        }
                        player.removeScoreboardTag("icedisappear");
                        player.getLocation().getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, player.getLocation(), 40, 1.0, 1.0, 1.0, 0.01);
                        player.getLocation().getWorld().spawnParticle(Particle.EXPLOSION_LARGE, player.getLocation(), 40, 1.0, 1.0, 1.0, 0.01);
                        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1.0F, 1.0F);
                        player.sendMessage(ChatUtil.color("&7[&" + color + "Magic&7] &" + color + "You are now visible for players."));
                        Bukkit.broadcast(ChatUtil.color("&7[&" + color + "Magic&7] &" + color + player.getName() + " has become visible again using their ice wand."), "staff.wand");
                    } else {
                        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                            if (!onlinePlayer.hasPermission("staff.wand")) {
                                onlinePlayer.hidePlayer(Main.getPlugin(Main.class), player);
                                
                            } else {
                                onlinePlayer.showPlayer(Main.getPlugin(Main.class), player);
                            }
                        }
                        player.addScoreboardTag("icedisappear");
                        player.getLocation().getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, player.getLocation(), 40, 1.0, 1.0, 1.0, 0.01);
                        player.getLocation().getWorld().spawnParticle(Particle.EXPLOSION_LARGE, player.getLocation(), 40, 1.0, 1.0, 1.0, 0.01);
                        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1.0F, 1.0F);
                        player.sendMessage(ChatUtil.color("&7[&" + color + "Magic&7] &" + color + "You are now hidden for players."));
                        Bukkit.broadcast(ChatUtil.color("&7[&" + color + "Magic&7] &" + color + player.getName() + " is now invisible for players."), "staff.wand");
                    }
                }
            }.runTaskLater(Main.getPlugin(Main.class), 100L); // 100L = 5 seconds
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
    	e.getPlayer().removeScoreboardTag("icedisappear");
    }

    @EventHandler
    public void RemoveTrail(PlayerJoinEvent event) {
    	Player p = event.getPlayer();
    	if(p.getScoreboardTags().contains("icedisappear")) {
    		p.removeScoreboardTag("icedisappear");
    	}
    }
    
    private void spawnFireDisappearParticles(Player player) {
    	Particle.DustOptions cyanColor = new Particle.DustOptions(Color.AQUA, 1.0f);
        Particle.DustOptions blueColor = new Particle.DustOptions(Color.fromRGB(0, 0, 255), 1.0f);

  
        double blueRed = 0.0;  
        double blueGreen = 0.0;
        double blueBlue = 1.0;
        double cyanRed = 0.0;
        double cyanGreen = 1.0;
        double cyanBlue = 1.0;


        Particle.DustOptions iceShardColor = new Particle.DustOptions(Color.fromRGB(170, 255, 255), 1.0f);


        player.getLocation().getWorld().spawnParticle(Particle.REDSTONE, player.getLocation(), 5, 1.0, 1.0, 1.0, 0.03, iceShardColor);
        player.getLocation().getWorld().spawnParticle(Particle.BLOCK_CRACK, player.getLocation(), 20, 0.0, 0.0, 0.0, 0.03, Material.ICE.createBlockData());
        player.getLocation().getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, player.getLocation(), 35, 1.0, 1.0, 1.0, 0.01);
        

        for (int i = 0; i < 10; i++) {
            double offsetX = Math.random() * 0.6 - 0.3;
            double offsetY = Math.random() * 0.6 - 0.3;
            double offsetZ = Math.random() * 0.6 - 0.3;
            player.getLocation().getWorld().spawnParticle(Particle.REDSTONE, player.getLocation().getX() + offsetX, player.getLocation().getY() + offsetY, player.getLocation().getZ() + offsetZ, 1, iceShardColor);
    }
    }
}
