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
package me.magicwands.nature;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import me.magicwands.main.Main;
import me.magicwands.utils.ChatUtil;
import me.magicwands.wands.NatureWand;

public class NatureHeal implements Listener {
    private static final Set<Material> FLOWERS = new HashSet<Material>();
    static {
        FLOWERS.add(Material.DANDELION);
        FLOWERS.add(Material.POPPY);
        FLOWERS.add(Material.BLUE_ORCHID);
        FLOWERS.add(Material.ALLIUM);
        FLOWERS.add(Material.AZURE_BLUET);
        FLOWERS.add(Material.RED_TULIP);
        FLOWERS.add(Material.ORANGE_TULIP);
        FLOWERS.add(Material.WHITE_TULIP);
        FLOWERS.add(Material.PINK_TULIP);
        FLOWERS.add(Material.OXEYE_DAISY);
        FLOWERS.add(Material.CORNFLOWER);
        FLOWERS.add(Material.LILY_OF_THE_VALLEY);
        FLOWERS.add(Material.WITHER_ROSE);
    }

    
    // NIET AF FIXEN DAT HET NIET MEER MET SCOREBOARD TAGS WERKT
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (player.getItemInHand().getItemMeta() != null && 
        	    player.getItemInHand().getItemMeta().getDisplayName() != null && 
        	    player.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(NatureWand.NatureWand.getItemMeta().getDisplayName()) && 
        	    event.getAction() == Action.LEFT_CLICK_AIR && 
        	    me.magicwands.events.NatureWand.NatureSpell.getOrDefault(player, 0) == 4) {
            boolean isStaff = player.hasPermission("staff.wand");
            
            if (player.getScoreboardTags().contains("NatureHealEnabled")) {
                player.removeScoreboardTag("NatureHealEnabled");
                String color = Main.getPlugin(Main.class).getConfig().getString("wands.nature.color");
                player.sendMessage(ChatUtil.color("&7[&" + color + "Magic&7] &" + color + "Stopped the Nature Absorb.."));
            } else {
                player.addScoreboardTag("NatureHealEnabled");
                String color = Main.getPlugin(Main.class).getConfig().getString("wands.nature.color");
                player.sendMessage(ChatUtil.color("&7[&" + color + "Magic&7] &" + color + "Started the Nature Absorb.."));
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            NatureHealListener.checkAndHealPlayer(player);
                        }
                    }
                }.runTaskTimer(Main.getPlugin(Main.class), 0L, 20L);
            }
            }
            }
    
    

    @EventHandler
    public void RemoveTrail(PlayerJoinEvent event) {
    	Player p = event.getPlayer();
    	if(p.getScoreboardTags().contains("NatureHealEnabled")) {
    		p.removeScoreboardTag("NatureHealEnabled");
    	}
    }
    
    
}
