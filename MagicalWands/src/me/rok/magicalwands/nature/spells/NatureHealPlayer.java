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
package me.rok.magicalwands.nature.spells;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

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
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.BlockIterator;

import me.lars.game.wands.NatureWand;

public class NatureHealPlayer implements Listener {
    private static final long STAFF_COOLDOWN = 0000L;
    private static final long PLAYER_COOLDOWN = 30000L;
    private Map<UUID, BukkitRunnable> particleTasks = new HashMap<>();
    private Map<UUID, Long> cooldowns = new HashMap<>();
    private BukkitTask fireTrailTask = null;
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
    
    private Entity getTargetEntity(Player player) {
        BlockIterator iterator = new BlockIterator(player.getWorld(), player.getEyeLocation().toVector(), player.getEyeLocation().getDirection(), 0, 100);
        Entity targetEntity = null;

        while (iterator.hasNext()) {
            Block block = iterator.next();
            for (Entity entity : block.getChunk().getEntities()) {
                if (entity instanceof LivingEntity && entity.getLocation().getBlock().equals(block)) {
                    targetEntity = entity;
                    
                    break;
                }
            }
        }
        return targetEntity;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (player.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(NatureWand.NatureWand.getItemMeta().getDisplayName())
                && event.getAction() == Action.LEFT_CLICK_AIR && me.lars.game.event.NatureWand.NatureSpell.get(player) == 9) {
            boolean isStaff = player.hasPermission("staff.wand");
            
            Entity targetEntity = getTargetEntity(player); 
            
            if (targetEntity != null) {
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

                    spawnLocation.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, spawnLocation, 150, 3.0, 2.0, 1.0, 0.8);
                    spawnLocation.getWorld().spawnParticle(Particle.SPELL_INSTANT, spawnLocation, 50, 1.0, 1.0, 1.0, 0.03);

                }
                  
            }}
            }
    

    @EventHandler
    public void RemoveTrail(PlayerJoinEvent event) {
    	Player p = event.getPlayer();
    	if(p.getScoreboardTags().contains("NatureHealEnabled")) {
    		p.removeScoreboardTag("NatureHealEnabled");
    	}
    }
    
    
}
