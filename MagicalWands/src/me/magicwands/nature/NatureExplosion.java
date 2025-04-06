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

import java.util.Random;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
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
import org.bukkit.util.Vector;

import me.magicwands.main.Main;
import me.magicwands.utils.GetTargetEntity;
import me.magicwands.utils.SpawnFirework;
import me.magicwands.wands.NatureWand;

public class NatureExplosion implements Listener {
 

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (player.getItemInHand().getItemMeta() != null && 
        	    player.getItemInHand().getItemMeta().getDisplayName() != null && 
        	    player.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(NatureWand.NatureWand.getItemMeta().getDisplayName()) && 
        	    event.getAction() == Action.LEFT_CLICK_AIR && 
        	    me.magicwands.events.NatureWand.NatureSpell.getOrDefault(player, 0) == 7) {
            boolean isStaff = player.hasPermission("magicwands.nature.*");
            this.SparkPlayer(player, isStaff);
        }
    
    }

    public static double getRandomDouble(double min, double max) {
        Random ran = new Random();
        return min + (max - min) * ran.nextDouble();
    }

    private void SparkPlayer(final Player player, final boolean isStaff) {
      

        Entity targetEntity = GetTargetEntity.getTargetEntity(player); 

        if (targetEntity != null) {
            Color color = Color.GREEN; 
            Color color2 = Color.LIME; 
            FireworkEffect.Type type = FireworkEffect.Type.BURST; 
            int power = 1; 
            long sparkdamage = Main.getPlugin(Main.class).getConfig().getInt("spells.naturespark.damage");
            SpawnFirework.spawnFirework(targetEntity.getLocation(), color, type, power);
            SpawnFirework.spawnFirework(targetEntity.getLocation(), color2, type, power);
            targetEntity.getWorld().createExplosion(targetEntity.getLocation(), 3);
            targetEntity.setVelocity(new Vector(0, 1.8, 0));
            targetEntity.getLocation().getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, targetEntity.getLocation(), 150, 3.0, 2.0, 1.0, 0.8);
            targetEntity.getLocation().getWorld().spawnParticle(Particle.TOTEM, targetEntity.getLocation(), 50, 1.0, 1.0, 1.0, 0.03);
            if (targetEntity instanceof LivingEntity) {
                LivingEntity livingEntity = (LivingEntity) targetEntity;
                livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 100, 2));
            }

        } else {

           
        	 Block block = player.getTargetBlockExact(100);

            if (block != null) {
            	
            	 Material blockname = player.getTargetBlockExact(100).getType();
                Location spawnLocation = block.getLocation().add(0, 1, 0);

                Color color = Color.GREEN; 
                Color color2 = Color.BLACK; 
                FireworkEffect.Type type = FireworkEffect.Type.BALL; 
                int power = 1; 
                
                double upper = getRandomDouble(0.5, 10.0);
                SpawnFirework.spawnFirework(spawnLocation, color, type, power);
                SpawnFirework.spawnFirework(spawnLocation, color2, type, power);
                spawnLocation.getWorld().createExplosion(spawnLocation, 3);
                spawnLocation.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, spawnLocation, 150, 3.0, 2.0, 1.0, 0.8);
                spawnLocation.getWorld().spawnParticle(Particle.TOTEM, spawnLocation, 50, 1.0, 1.0, 1.0, 0.03);
                spawnLocation.getWorld().spawnFallingBlock(spawnLocation, blockname, (byte) 2).setVelocity(new Vector(getRandomDouble(-0.2, 0.5), getRandomDouble(0.4, 1.5), getRandomDouble(-0.2, 0.5)));
                spawnLocation.getWorld().spawnFallingBlock(spawnLocation, blockname, (byte) 2).setVelocity(new Vector(getRandomDouble(-0.2, 0.5), getRandomDouble(0.4, 1.5), getRandomDouble(-0.2, 0.5)));
                spawnLocation.getWorld().spawnFallingBlock(spawnLocation, blockname, (byte) 2).setVelocity(new Vector(getRandomDouble(-0.2, 0.5), getRandomDouble(0.4, 1.5), getRandomDouble(-0.2, 0.5)));
                spawnLocation.getWorld().spawnFallingBlock(spawnLocation, blockname, (byte) 2).setVelocity(new Vector(getRandomDouble(-0.2, 0.5), getRandomDouble(0.4, 1.5), getRandomDouble(-0.2, 0.5)));
                spawnLocation.getWorld().spawnFallingBlock(spawnLocation, blockname, (byte) 2).setVelocity(new Vector(getRandomDouble(-0.2, 0.5), getRandomDouble(0.4, 1.5), getRandomDouble(-0.2, 0.5)));
            } else {
                Location spawnLocation = player.getEyeLocation().add(player.getEyeLocation().getDirection().multiply(25));

                Color color = Color.GREEN; 
                Color color2 = Color.BLACK; 
                FireworkEffect.Type type = FireworkEffect.Type.BURST; 
                int power = 1; 
                SpawnFirework.spawnFirework(spawnLocation, color, type, power);
                SpawnFirework.spawnFirework(spawnLocation, color2, type, power);
                spawnLocation.getWorld().createExplosion(spawnLocation, 3);
                spawnLocation.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, spawnLocation, 150, 3.0, 2.0, 1.0, 0.8);
                spawnLocation.getWorld().spawnParticle(Particle.TOTEM, spawnLocation, 50, 1.0, 1.0, 1.0, 0.03);
                spawnLocation.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, spawnLocation, 150, 3.0, 2.0, 0.0, 0.8);
            }
        }
    }}

   