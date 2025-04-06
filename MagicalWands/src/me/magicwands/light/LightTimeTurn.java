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

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import me.magicwands.main.Main;
import me.magicwands.utils.ChatUtil;
import me.magicwands.wands.LightWand;

public class LightTimeTurn implements Listener {
    private BukkitTask TimeTurnTask = null;
    private final Map<Player, Boolean> TimeTurnMap = new HashMap<>();

    

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (player.getItemInHand().getItemMeta() != null && 
        	    player.getItemInHand().getItemMeta().getDisplayName() != null && 
        	    player.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(LightWand.LightWand.getItemMeta().getDisplayName()) && 
        	    event.getAction() == Action.LEFT_CLICK_AIR && 
        	    me.magicwands.events.LightWand.LightSpell.getOrDefault(player, 0) == 8) { 

            if (TimeTurnTask != null && !TimeTurnTask.isCancelled()) {
            	String color = Main.getPlugin(Main.class).getConfig().getString("wands.light.color");
            	player.sendMessage(ChatUtil.color("&7[&" + color + "Magic&7] &" + color + "&cThe time is already changing, if you keep doing this the world will explode."));
                return;
            }

            boolean isActive = TimeTurnMap.getOrDefault(player, false);
            
            String color = Main.getPlugin(Main.class).getConfig().getString("wands.light.color");
            player.sendMessage(ChatUtil.color("&7[&" + color + "Magic&7] &" + color + "Started the Light Time Turner.."));
            castTimeJump(player);

            final long[] totalAdded = {0}; 

            TimeTurnTask = new BukkitRunnable() {
                @Override
                public void run() {
                    totalAdded[0] += 125;
                    long currentTime = player.getWorld().getTime();
                    currentTime += 125;
                    currentTime = currentTime % 24000;
                    player.getWorld().setTime(currentTime);

                    if (totalAdded[0] >= 12000) {
                        TimeTurnTask.cancel();
                        TimeTurnTask = null;
                    }
                }
            }.runTaskTimer(Main.getPlugin(Main.class), 0L, 1L);
        }
    }

   
    public void castTimeJump(Player player) {
        World world = player.getWorld();
        Location playerLoc = player.getLocation();
        new BukkitRunnable() {
            int repetitions = 0;

            @Override
            public void run() {
                if (repetitions >= 13) {
                    cancel(); 
                    return;
                }
                for (double y = 0; y < 2; y += 0.1) {
                    Location particleLoc = playerLoc.clone().add(0, y, 0);
                   
                    world.spawnParticle(Particle.ENCHANTMENT_TABLE, particleLoc, 250, 15.3, 15, 15.3, 0.05);

                    
                }
                world.playSound(playerLoc, Sound.ENTITY_ENDER_DRAGON_FLAP, 1.5f, 1.0f);
                world.playSound(playerLoc, Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1.5f, 1.0f);
                world.playSound(playerLoc, Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1.5f, 2.0f);
                world.playSound(playerLoc, Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1.5f, 3.0f);
                world.playSound(playerLoc, Sound.BLOCK_NOTE_BLOCK_BELL, 1.5f, 3.0f);
                

                repetitions++; 
            }
        }.runTaskTimer(Main.getPlugin(Main.class), 0L, 5L); 
    }

        
    
}
