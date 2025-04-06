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
package me.magicwands.light;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
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

import me.magicwands.main.Main;
import me.magicwands.utils.ChatUtil;
import me.magicwands.utils.LogManager;
import me.magicwands.utils.ParticleColor;
import me.magicwands.wands.LightWand;

public class LightDisappear implements Listener {
   
    private Map<UUID, Boolean> hiddenPlayers = new HashMap<>();  

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (player.getItemInHand().getItemMeta() != null && 
        	    player.getItemInHand().getItemMeta().getDisplayName() != null && 
        	    player.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(LightWand.LightWand.getItemMeta().getDisplayName()) && 
        	    event.getAction() == Action.LEFT_CLICK_AIR && 
        	    me.magicwands.events.LightWand.LightSpell.getOrDefault(player, 0) == 2) { 

            boolean isHidden = hiddenPlayers.getOrDefault(player.getUniqueId(), false); 
            String color = Main.getPlugin(Main.class).getConfig().getString("wands.light.color");

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
                        hiddenPlayers.put(player.getUniqueId(), false);  
                        for (int i = 0; i < 6; i++) {
                            double randomX = (Math.random() - 0.5) * 7.6; 
                            double randomZ = (Math.random() - 0.5) * 7.6; 
                            player.getLocation().getWorld().strikeLightningEffect(player.getLocation().add(randomX, 0, randomZ));
                        }
                        player.getLocation().getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, player.getLocation(), 40, 1.0, 1.0, 1.0, 0.01);
                        player.getLocation().getWorld().spawnParticle(Particle.EXPLOSION_LARGE, player.getLocation(), 40, 1.0, 1.0, 1.0, 0.01);
                        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1.0F, 1.0F);
                        player.sendMessage(ChatUtil.color("&7[&" + color + "Magic&7] &" + color + "You are now visible for players."));
                        
                        LogManager.sendLog(ChatUtil.color(player.getName() + " has become visible using their ice wand."));
                        
                    } else {
                        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                            if (!onlinePlayer.hasPermission("magicwands.disappear.see")) {
                                onlinePlayer.hidePlayer(Main.getPlugin(Main.class), player);
                                
                            } else {
                                onlinePlayer.showPlayer(Main.getPlugin(Main.class), player);
                                
                            }
                        }
                        hiddenPlayers.put(player.getUniqueId(), true); 
                        LogManager.sendLog(ChatUtil.color( player.getName() + " is now invisible for other players."));
                        for (int i = 0; i < 6; i++) {
                            double randomX = (Math.random() - 0.5) * 7.6; 
                            double randomZ = (Math.random() - 0.5) * 7.6; 
                            player.getLocation().getWorld().strikeLightningEffect(player.getLocation().add(randomX, 0, randomZ));
                        }
                        player.getLocation().getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, player.getLocation(), 40, 1.0, 1.0, 1.0, 0.01);
                        player.getLocation().getWorld().spawnParticle(Particle.EXPLOSION_LARGE, player.getLocation(), 40, 1.0, 1.0, 1.0, 0.01);
                        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1.0F, 1.0F);
                        player.sendMessage(ChatUtil.color("&7[&" + color + "Magic&7] &" + color + "You are now hidden for players."));
                    }
                }
            }.runTaskLater(Main.getPlugin(Main.class), 100L);  
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        hiddenPlayers.remove(e.getPlayer().getUniqueId());  
    }

    @EventHandler
    public void RemoveTrail(PlayerJoinEvent event) {
        Player p = event.getPlayer();
        hiddenPlayers.remove(p.getUniqueId());  
    }
    
    private void spawnFireDisappearParticles(Player player) {
        player.getLocation().getWorld().spawnParticle(Particle.REDSTONE, player.getLocation(), 5, 1.0, 1.0, 1.0, 0.03, ParticleColor.LIGHT_GRAY);
        player.getLocation().getWorld().spawnParticle(Particle.BLOCK_CRACK, player.getLocation(), 20, 0.0, 0.0, 0.0, 0.03, Material.QUARTZ_BLOCK.createBlockData());
        player.getLocation().getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, player.getLocation(), 35, 1.0, 1.0, 1.0, 0.01);
        player.getLocation().getWorld().spawnParticle(Particle.WHITE_ASH, player.getLocation(), 35, 1.0, 1.0, 1.0, 0.01);
        player.getLocation().getWorld().spawnParticle(Particle.CLOUD, player.getLocation(), 2, 1.0, 1.0, 1.0, 0.01);

        for (int i = 0; i < 10; i++) {
            double offsetX = Math.random() * 0.6 - 0.3;
            double offsetY = Math.random() * 0.6 - 0.3;
            double offsetZ = Math.random() * 0.6 - 0.3;
            player.getLocation().getWorld().spawnParticle(Particle.REDSTONE, player.getLocation().getX() + offsetX, player.getLocation().getY() + offsetY, player.getLocation().getZ() + offsetZ, 1, ParticleColor.WHITE);
        }
    }
}
