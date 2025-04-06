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

import me.magicwands.utils.GetTargetEntity;
import me.magicwands.utils.ParticleColor;
import me.magicwands.wands.NatureWand;

public class NatureHealPlayer implements Listener {
  
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
    

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (player.getItemInHand().getItemMeta() != null && 
        	    player.getItemInHand().getItemMeta().getDisplayName() != null && 
        	    player.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(NatureWand.NatureWand.getItemMeta().getDisplayName()) && 
        	    event.getAction() == Action.LEFT_CLICK_AIR && 
        	    me.magicwands.events.NatureWand.NatureSpell.getOrDefault(player, 0) == 9) {
            boolean isStaff = player.hasPermission("staff.wand");
            
            Entity targetEntity = GetTargetEntity.getTargetEntity(player); 
            
            if (targetEntity != null) {
            	Particle.DustOptions iceShardColor = new Particle.DustOptions(Color.fromRGB(170, 255, 255), 1.0f);
                Particle.DustOptions cyanColor = new Particle.DustOptions(Color.AQUA, 1.0f);
                Particle.DustOptions blueColor = new Particle.DustOptions(Color.fromRGB(0, 0, 255), 1.0f);
                Location location = targetEntity.getLocation();
                targetEntity.getWorld().spawnParticle(Particle.REDSTONE, location, 5, 1.0, 1.0, 1.0, 0.03, ParticleColor.GREEN);
                targetEntity.getWorld().spawnParticle(Particle.REDSTONE, location, 5, 1.0, 1.0, 1.0, 0.03, ParticleColor.DARK_GREEN);
                targetEntity.getWorld().spawnParticle(Particle.REDSTONE, location, 5, 1.0, 1.0, 1.0, 0.03, ParticleColor.LIME_GREEN);
                targetEntity.getWorld().spawnParticle(Particle.SMOKE_NORMAL, location, 20, 1.9, 1.9, 1.9, 0.01);
                targetEntity.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, location, 50, 0.9, 0.9, 0.9, 0.01);
                targetEntity.getWorld().spawnParticle(Particle.BLOCK_CRACK, location, 20, 0.0, 0.0, 0.0, 0.03, Material.OAK_LEAVES.createBlockData());
                targetEntity.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, location, 150, 3.0, 2.0, 1.0, 0.8);
                Particle.DustOptions greenDust = new Particle.DustOptions(Color.fromRGB(0, 255, 0), 1.0f); // Green
                Particle.DustOptions lightGreenDust = new Particle.DustOptions(Color.fromRGB(144, 238, 144), 1.0f); // Light Green
                Particle.DustOptions limeGreenDust = new Particle.DustOptions(Color.fromRGB(50, 205, 50), 1.0f); // Lime Green
                targetEntity.getWorld().spawnParticle(Particle.REDSTONE, location, 50, 1.0, 1.0, 1.0, 0.03, greenDust);
                targetEntity.getWorld().spawnParticle(Particle.REDSTONE, location, 50, 1.0, 1.0, 1.0, 0.03, lightGreenDust);
                targetEntity.getWorld().spawnParticle(Particle.REDSTONE, location, 50, 1.0, 1.0, 1.0, 0.03, limeGreenDust);
                targetEntity.getLocation().getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, targetEntity.getLocation(), 150, 3.0, 2.0, 1.0, 0.8);
                targetEntity.getLocation().getWorld().spawnParticle(Particle.HEART, targetEntity.getLocation(), 10, 1.0, 1.0, 1.0, 0.03);
                if (targetEntity != null && targetEntity instanceof LivingEntity) {
                    LivingEntity livingEntity = (LivingEntity) targetEntity;
                    livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 2));
                }
                
            } else {
               
                Block block = player.getTargetBlockExact(100);

             
                if (block != null) {
                    Location spawnLocation = block.getLocation().add(0, 1, 0);

                    Color color = Color.GREEN; 
                    Color color2 = Color.LIME; 
                    FireworkEffect.Type type = FireworkEffect.Type.BURST; 
                    int power = 1; 
                    Particle.DustOptions iceShardColor = new Particle.DustOptions(Color.fromRGB(170, 255, 255), 1.0f);
                    Particle.DustOptions cyanColor = new Particle.DustOptions(Color.AQUA, 1.0f);
                    Particle.DustOptions blueColor = new Particle.DustOptions(Color.fromRGB(0, 0, 255), 1.0f);
                    Location location = spawnLocation;
                    spawnLocation.getWorld().spawnParticle(Particle.REDSTONE, location, 5, 1.0, 1.0, 1.0, 0.03, ParticleColor.GREEN);
                    spawnLocation.getWorld().spawnParticle(Particle.REDSTONE, location, 5, 1.0, 1.0, 1.0, 0.03, ParticleColor.DARK_GREEN);
                    spawnLocation.getWorld().spawnParticle(Particle.REDSTONE, location, 5, 1.0, 1.0, 1.0, 0.03, ParticleColor.LIME_GREEN);
                    spawnLocation.getWorld().spawnParticle(Particle.SMOKE_NORMAL, location, 20, 1.9, 1.9, 1.9, 0.01);
                    Particle.DustOptions greenDust = new Particle.DustOptions(Color.fromRGB(0, 255, 0), 1.0f); // Green
                    Particle.DustOptions lightGreenDust = new Particle.DustOptions(Color.fromRGB(144, 238, 144), 1.0f); // Light Green
                    Particle.DustOptions limeGreenDust = new Particle.DustOptions(Color.fromRGB(50, 205, 50), 1.0f); // Lime Green
                    spawnLocation.getWorld().spawnParticle(Particle.REDSTONE, location, 50, 1.0, 1.0, 1.0, 0.03, greenDust);
                    spawnLocation.getWorld().spawnParticle(Particle.REDSTONE, location, 50, 1.0, 1.0, 1.0, 0.03, lightGreenDust);
                    spawnLocation.getWorld().spawnParticle(Particle.REDSTONE, location, 50, 1.0, 1.0, 1.0, 0.03, limeGreenDust);
                    spawnLocation.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, location, 50, 0.9, 0.9, 0.9, 0.01);
                    spawnLocation.getWorld().spawnParticle(Particle.BLOCK_CRACK, location, 20, 0.0, 0.0, 0.0, 0.03, Material.OAK_LEAVES.createBlockData());
                    spawnLocation.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, spawnLocation, 150, 3.0, 2.0, 1.0, 0.8);

                }
                  
            }}}
    

    
    
    
}
