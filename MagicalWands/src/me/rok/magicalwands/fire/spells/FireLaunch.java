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

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Firework;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

import me.lars.game.event.FlameWand;
import me.lars.game.utils.ChatUtil;
import me.lars.game.wands.Wands;

public class FireLaunch implements Listener {
    private static final long STAFF_COOLDOWN = 0000L;
    private static final long PLAYER_COOLDOWN = 30000L;
    private Map<UUID, BukkitRunnable> particleTasks = new HashMap<>();
    private Map<UUID, Long> cooldowns = new HashMap<>();

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (player.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(Wands.IgnatiusWand.getItemMeta().getDisplayName())
                && event.getAction() == Action.LEFT_CLICK_AIR && FlameWand.Spell.get(player) == 6) {
            boolean isStaff = player.hasPermission("staff.wand");
            this.SparkPlayer(player, isStaff);
        }
    }

    @SuppressWarnings("unused")
	private void SparkPlayer(final Player player, final boolean isStaff) {
        long remainingTime;
        long cooldownDuration = isStaff ? STAFF_COOLDOWN : PLAYER_COOLDOWN;

        if (this.cooldowns.containsKey(player.getUniqueId()) && (remainingTime = this.cooldowns.get(player.getUniqueId()) - System.currentTimeMillis()) > 0L) {
            player.sendMessage(ChatUtil.color("&7[&dMagic&7] &fThis spell is still on a &d&lCOOLDOWN&c&l! &fYou have to wait &d" + remainingTime / 1000L + " &fseconds"));
            return;
        }
        this.cooldowns.put(player.getUniqueId(), System.currentTimeMillis() + cooldownDuration);

        Entity targetEntity = getTargetEntity(player); 
       
        if (targetEntity != null) {
            Color color = Color.ORANGE; 
            Color color2 = Color.BLACK; 
            FireworkEffect.Type type = FireworkEffect.Type.BURST; 
            int power = 1; 
            spawnFirework(targetEntity.getLocation(), color, type, power);
            spawnFirework(targetEntity.getLocation(), color2, type, power);
            targetEntity.getLocation().getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, targetEntity.getLocation(), 150, 3.0, 2.0, 1.0, 0.8);
            targetEntity.getLocation().getWorld().spawnParticle(Particle.FLAME, targetEntity.getLocation(), 50, 1.0, 1.0, 1.0, 0.03);
            targetEntity.getLocation().getWorld().spawnParticle(Particle.SMOKE_NORMAL, targetEntity.getLocation(), 20, 1.0, 2.0, 1.0, 0.03);
            targetEntity.getLocation().getWorld().spawnParticle(Particle.SMOKE_LARGE, targetEntity.getLocation(), 40, 1.0, 2.0, 1.0, 0.03);
            targetEntity.setVelocity(new Vector(0, 1.8, 0));
           
              
        } else {
           
            Block block = player.getTargetBlockExact(100);

         
            if (block != null) {
                Location spawnLocation = block.getLocation().add(0, 1, 0);

                Color color = Color.ORANGE; 
                Color color2 = Color.BLACK; 
                FireworkEffect.Type type = FireworkEffect.Type.BURST; 
                int power = 1; 

                spawnFirework(spawnLocation, color, type, power);
                spawnFirework(spawnLocation, color2, type, power);
                spawnLocation.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, spawnLocation, 150, 3.0, 2.0, 0.0, 0.8);
                spawnLocation.getWorld().spawnParticle(Particle.FLAME, spawnLocation, 50, 0.5, 0.5, 0.0, 0.03);
                spawnLocation.getWorld().spawnParticle(Particle.SMOKE_NORMAL, spawnLocation, 20, 1.0, 1.0, 1.0, 0.03);
                spawnLocation.getWorld().spawnParticle(Particle.SMOKE_LARGE, spawnLocation, 40, 1.0, 1.0, 1.0, 0.03);
            } else {
            	 Location spawnLocation = player.getEyeLocation().add(player.getEyeLocation().getDirection().multiply(25));

                 Color color = Color.ORANGE; 
                 Color color2 = Color.BLACK; 
                 FireworkEffect.Type type = FireworkEffect.Type.BURST; 
                 int power = 1; 
                 spawnFirework(spawnLocation, color, type, power);
                 spawnFirework(spawnLocation, color2, type, power);
                 spawnLocation.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, spawnLocation, 150, 3.0, 2.0, 0.0, 0.8);
                 spawnLocation.getWorld().spawnParticle(Particle.FLAME, spawnLocation, 50, 0.5, 0.5, 0.0, 0.03);
                 spawnLocation.getWorld().spawnParticle(Particle.SMOKE_NORMAL, spawnLocation, 20, 1.0, 1.0, 1.0, 0.03);
                 spawnLocation.getWorld().spawnParticle(Particle.SMOKE_LARGE, spawnLocation, 40, 1.0, 1.0, 1.0, 0.03);
                 
            }
        
    }
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

    
	
    public static void spawnFirework(Location location, Color color, FireworkEffect.Type type, int power) {
        Firework firework = location.getWorld().spawn(location, Firework.class);
        FireworkMeta fireworkMeta = firework.getFireworkMeta();

        FireworkEffect effect = FireworkEffect.builder()
                .withColor(color)
                .with(type)
                .flicker(true)
                .build();

        fireworkMeta.addEffect(effect);
        fireworkMeta.setPower(power);
        firework.setFireworkMeta(fireworkMeta);

      
        firework.detonate();
    }
}