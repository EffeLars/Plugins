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
import org.bukkit.Color;
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
import me.lars.game.wands.Wands;

public class FireDisappear implements Listener {
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
                player.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(Wands.IgnatiusWand.getItemMeta().getDisplayName()) &&
                event.getAction() == Action.LEFT_CLICK_AIR && FlameWand.Spell.get(player) == 8) {

            boolean isHidden = player.getScoreboardTags().contains("firedisappear");
            String color = Main.getPlugin(Main.class).getConfig().getString("wands.fire.color");
            player.sendMessage(ChatUtil.color("&7[&" + color + "Magic&7] &" + color + "Using spell..."));

            BukkitTask fireTrailTask = Bukkit.getScheduler().runTaskTimer(Main.getPlugin(Main.class), new Runnable() {
                @Override
                public void run() {
                    spawnFireDisappearParticles(player);
                }
            }, 0L, 1L); // 1 tick = 0.05 seconds

            new BukkitRunnable() {
                @Override
                public void run() {
                    fireTrailTask.cancel();

                    if (isHidden) {
                        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                            onlinePlayer.showPlayer(Main.getPlugin(Main.class), player);
                        }
                        player.removeScoreboardTag("firedisappear");
                        player.getLocation().getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, player.getLocation(), 40, 1.0, 1.0, 1.0, 0.01);
                        player.getLocation().getWorld().spawnParticle(Particle.EXPLOSION_LARGE, player.getLocation(), 40, 1.0, 1.0, 1.0, 0.01);
                        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1.0F, 1.0F);
                        player.sendMessage(ChatUtil.color("&7[&" + color + "Magic&7] &" + color + "You are now visible for players."));
                        Bukkit.broadcast(ChatUtil.color("&7[&" + color + "Magic&7] &" + color + player.getName() + " has become visible again using their fire wand."), "staff.wand");
                    } else {
                        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                            if (!onlinePlayer.hasPermission("staff.wand")) {
                                onlinePlayer.hidePlayer(Main.getPlugin(Main.class), player);
                                
                            } else {
                                onlinePlayer.showPlayer(Main.getPlugin(Main.class), player);
                            }
                        }
                        player.addScoreboardTag("firedisappear");
                        player.getLocation().getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, player.getLocation(), 40, 1.0, 1.0, 1.0, 0.01);
                        player.getLocation().getWorld().spawnParticle(Particle.EXPLOSION_LARGE, player.getLocation(), 40, 1.0, 1.0, 1.0, 0.01);
                        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1.0F, 1.0F);
                        player.sendMessage(ChatUtil.color("&7[&" + color + "Magic&7] &" + color + "You are now hidden for players."));
                        Bukkit.broadcast(ChatUtil.color("&7[&" + color + "Magic&7] &" + color + player.getName() + " is now invisible for players."), "staff.wand");
                    }
                }
            }.runTaskLater(Main.getPlugin(Main.class), 100L); 
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
    	e.getPlayer().removeScoreboardTag("firedisappear");
    }

    @EventHandler
    public void RemoveTrail(PlayerJoinEvent event) {
    	Player p = event.getPlayer();
    	if(p.getScoreboardTags().contains("firetrail")) {
    		p.removeScoreboardTag("firetrail");
    	}
    }
    
    private void spawnFireDisappearParticles(Player player) {
    	Particle.DustOptions orangeColor = new Particle.DustOptions(Color.ORANGE, 1.0f);
        Particle.DustOptions yellowColor = new Particle.DustOptions(Color.YELLOW, 1.0f);
        player.getWorld().spawnParticle(Particle.REDSTONE, player.getLocation(), 10, 1.0, 1.0, 1.0, 0.02, (Object)orangeColor);
        player.getWorld().spawnParticle(Particle.REDSTONE, player.getLocation(), 10, 1.0, 1.0, 1.0, 0.03, (Object)yellowColor);
        double red = 0.7372549019607844;
        double green = 0.44313725490196076;
        double blue = 0.21176470588235294;
        double red2 = 0.9803921568627451;
        double green2 = 0.9450980392156862;
        double blue2 = 0.1568627450980392;
       player.spawnParticle(Particle.SPELL_MOB, player.getLocation(), 1, red, green, blue, 2.0);
        player.spawnParticle(Particle.SPELL_MOB, player.getLocation(), 1, red2, green2, blue2, 3.0);
        player.getLocation().getWorld().spawnParticle(Particle.FLAME, player.getLocation(), 10, 1.0, 1.0, 1.0, 0.03);
        player.getLocation().getWorld().spawnParticle(Particle.LAVA, player.getLocation(), 1, 0.0, 0.0, 0.0, 0.01);
        player.getLocation().getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, player.getLocation(), 40, 1.0, 1.0, 1.0, 0.01);
        player.getLocation().getWorld().spawnParticle(Particle.ASH, player.getLocation(), 5, 0.0, 0.0, 0.0, 0.03);
        player.getLocation().getWorld().spawnParticle(Particle.SMOKE_NORMAL, player.getLocation(), 5, 0.0, 0.0, 0.0, 0.02);
    }

}
