package me.magicwands.ice;

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

import me.magicwands.main.Main;
import me.magicwands.utils.ChatUtil;
import me.magicwands.utils.LogManager;
import me.magicwands.wands.IceWand;

public class IceDisappear implements Listener {
    private Map<UUID, Boolean> hiddenPlayers = new HashMap<>();
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (player.getItemInHand().getItemMeta() != null && 
        	    player.getItemInHand().getItemMeta().getDisplayName() != null && 
        	    player.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(IceWand.IceWand.getItemMeta().getDisplayName()) && 
        	    event.getAction() == Action.LEFT_CLICK_AIR && 
        	    me.magicwands.events.IceWand.IceSpell.getOrDefault(player, 0) == 4) {

            boolean isHidden = hiddenPlayers.getOrDefault(player.getUniqueId(), false);
            String color = Main.getPlugin(Main.class).getConfig().getString("wands.ice.color");
 

            BukkitTask iceTrailTask = Bukkit.getScheduler().runTaskTimer(Main.getPlugin(Main.class), new Runnable() {
                @Override
                public void run() {
                    spawnIceDisappearParticles(player);
                }
            }, 0L, 1L);

            new BukkitRunnable() {
                @Override
                public void run() {
                    iceTrailTask.cancel();

                    if (isHidden) {
                        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                            onlinePlayer.showPlayer(Main.getPlugin(Main.class), player);
                        }
                        hiddenPlayers.put(player.getUniqueId(), false);  // Mark as visible
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
    
    private void spawnIceDisappearParticles(Player player) {
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
