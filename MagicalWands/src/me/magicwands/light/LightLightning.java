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

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.magicwands.utils.DrawLine;
import me.magicwands.utils.GetTargetEntity;
import me.magicwands.utils.SpawnFirework;

public class LightLightning implements Listener {


    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (player.getItemInHand().getItemMeta() != null && 
        	    player.getItemInHand().getItemMeta().getDisplayName() != null && 
        	    player.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(me.magicwands.wands.LightWand.LightWand.getItemMeta().getDisplayName()) && 
        	    event.getAction() == Action.LEFT_CLICK_AIR && 
        	    me.magicwands.events.LightWand.LightSpell.getOrDefault(player, 0) == 1) { 
            boolean isStaff = player.hasPermission("magicwands.*");
            this.LightningShoot(player, isStaff);
        }
        }
    

    @SuppressWarnings({ "unused", "null" })
	private void LightningShoot(final Player player, final boolean isStaff) {
        long remainingTime;

        Entity targetEntity = GetTargetEntity.getTargetEntity(player); 
       
        if (targetEntity != null) {
            Color color = Color.BLACK; 
            Color color2 = Color.WHITE; 
            FireworkEffect.Type type = FireworkEffect.Type.BURST; 
            int power = 1; 
            SpawnFirework.spawnFirework(targetEntity.getLocation(), color2, type, power);
            for (int i = 0; i < 5; i++) { 
        	    double randomX = (Math.random() - 0.5) * 7.6; 
        	    double randomZ = (Math.random() - 0.5) * 7.6;
        	    targetEntity.getLocation().getWorld().playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
        	    targetEntity.getLocation().getWorld().playSound(player.getLocation(), Sound.ENTITY_WITHER_SHOOT, 1, 1);
        	    targetEntity.getLocation().getWorld().strikeLightning(targetEntity.getLocation().add(randomX, 0, randomZ));
        	    targetEntity.getLocation().getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, targetEntity.getLocation(), 50, 3.0, 2.0, 1.0, 0.8);
                targetEntity.getLocation().getWorld().spawnParticle(Particle.CLOUD, targetEntity.getLocation(), 20, 1.0, 1.0, 1.0, 0.03);
                DrawLine.drawParticleLine(player);
        	}
            
            if (targetEntity != null && targetEntity instanceof LivingEntity) {
                LivingEntity livingEntity = (LivingEntity) targetEntity;
                livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 2));
                livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 100, 2));
                livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 700, 2));   
            }
            
        } else {
           
            Block block = player.getTargetBlockExact(100);

         
            if (block != null) {
                Location spawnLocation = block.getLocation().add(0, 1, 0);

                Color color = Color.WHITE;
                Color color2 = Color.BLACK; 
                FireworkEffect.Type type = FireworkEffect.Type.BURST; 
                int power = 1; 

                SpawnFirework.spawnFirework(spawnLocation, color, type, power);
                SpawnFirework.spawnFirework(spawnLocation, color2, type, power);
                for (int i = 0; i < 7; i++) { 
                    double randomX = (Math.random() - 0.5) * 7.6; 
                    double randomZ = (Math.random() - 0.5) * 7.6; 

                    spawnLocation.getWorld().strikeLightning(spawnLocation.clone().add(randomX, 0, randomZ));
                    spawnLocation.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_SHOOT, 1, 1);
                    spawnLocation.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, spawnLocation, 150, 3.0, 2.0, 1.0, 0.8);
                    spawnLocation.getWorld().spawnParticle(Particle.CLOUD, spawnLocation, 20, 1.0, 1.0, 1.0, 0.03);
                    DrawLine.drawParticleLine(player);
                }

            } else {
            	
            	 Location spawnLocation = player.getEyeLocation().add(player.getEyeLocation().getDirection().multiply(25));

                 Color color = Color.WHITE; 
                 Color color2 = Color.BLACK;
                 FireworkEffect.Type type = FireworkEffect.Type.BURST; 
                 int power = 1; 
                 SpawnFirework.spawnFirework(spawnLocation, color, type, power);
                 SpawnFirework.spawnFirework(spawnLocation, color2, type, power);
                 for (int i = 0; i < 5; i++) {
                	    double randomX = (Math.random() - 0.5) * 7.6; 
                	    double randomZ = (Math.random() - 0.5) * 7.6; 

                	    spawnLocation.getWorld().strikeLightning(spawnLocation);
                	    spawnLocation.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, spawnLocation, 150, 3.0, 2.0, 1.0, 0.8);
                	    spawnLocation.getWorld().playSound(player.getLocation(), Sound.ENTITY_WITHER_SHOOT, 1, 1);
                        spawnLocation.getWorld().spawnParticle(Particle.CLOUD, spawnLocation, 50, 1.0, 1.0, 1.0, 0.03);
                        spawnLocation.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, spawnLocation, 150, 3.0, 2.0, 0.0, 0.8);
                        DrawLine.drawParticleLine(player);
                	}
                 
            }
        
    }
    }
	


}