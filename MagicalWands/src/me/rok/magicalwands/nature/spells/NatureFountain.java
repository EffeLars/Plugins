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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Firework;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

import me.lars.game.Main;
import me.lars.game.utils.ChatUtil;
import me.lars.game.utils.ParticleColor;
import me.lars.game.wands.NatureWand;

public class NatureFountain implements Listener {
    private static final long STAFF_COOLDOWN = 0000L;
    private static final long PLAYER_COOLDOWN = 30000L;
    private Map<UUID, BukkitRunnable> particleTasks = new HashMap<>();
    private Map<UUID, Long> cooldowns = new HashMap<>();
    private static final List<Material> FLOWERS = new ArrayList<>();
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
        if (player.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(NatureWand.NatureWand.getItemMeta().getDisplayName())
                && event.getAction() == Action.LEFT_CLICK_AIR && me.lars.game.event.NatureWand.NatureSpell.get(player) == 8) {
            boolean isStaff = player.hasPermission("staff.wand");
            this.Fountain(player, isStaff);
        }
    }


    public static double getRandomDouble(double min, double max) {
        Random ran = new Random();
        return min + (max - min) * ran.nextDouble();
    }

    private List<Material> getRandomFlowers(int count) {
        List<Material> shuffledFlowers = new ArrayList<>(FLOWERS);
        Collections.shuffle(shuffledFlowers);
        return shuffledFlowers.subList(0, count);
    }
    
    private void spawnFlowers(Location location, Material material) {
        FallingBlock fallingBlock = location.getWorld().spawnFallingBlock(location, material.createBlockData());
        fallingBlock.setVelocity(new Vector(getRandomDouble(-0.2, 0.5), getRandomDouble(0.4, 1.5), getRandomDouble(-0.2, 0.5)));
        BukkitTask particleTask = Bukkit.getScheduler().runTaskTimer(Main.getPlugin(Main.class), () -> {
            fallingBlock.getLocation().getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, fallingBlock.getLocation(), 15, 0.2, 0.2, 0.2, 0.03);
            fallingBlock.getLocation().getWorld().spawnParticle(Particle.VILLAGER_HAPPY, fallingBlock.getLocation(), 1, 0.2, 0.2, 0.2, 0.03);
            fallingBlock.getLocation().getWorld().spawnParticle(Particle.REDSTONE, location, 1, 1.0, 1.0, 1.0, 0.03, ParticleColor.GREEN);
            fallingBlock.getLocation().getWorld().spawnParticle(Particle.REDSTONE, location, 1, 1.0, 1.0, 1.0, 0.03, ParticleColor.LIME_GREEN);
            fallingBlock.getLocation().getWorld().spawnParticle(Particle.REDSTONE, location, 1, 1.0, 1.0, 1.0, 0.03, ParticleColor.DARK_GREEN);
            fallingBlock.getLocation().getWorld().spawnParticle(Particle.REDSTONE, fallingBlock.getLocation(), 1, 1.0, 1.0, 1.0, 0.03, ParticleColor.GREEN);
            fallingBlock.getLocation().getWorld().spawnParticle(Particle.REDSTONE, fallingBlock.getLocation(), 1, 1.0, 1.0, 1.0, 0.03, ParticleColor.LIME_GREEN);
            fallingBlock.getLocation().getWorld().spawnParticle(Particle.REDSTONE, fallingBlock.getLocation(), 1, 1.0, 1.0, 1.0, 0.03, ParticleColor.DARK_GREEN);
        }, 0, 4);
        Bukkit.getScheduler().runTaskLater(Main.getPlugin(Main.class), particleTask::cancel, 100);
        Bukkit.getScheduler().runTaskLater(Main.getPlugin(Main.class), () -> {
            particleTask.cancel();
            fallingBlock.getLocation().getWorld().spawnParticle(Particle.REDSTONE, fallingBlock.getLocation(), 1, 2.0, 0.0, 2.0, 0.03, ParticleColor.GREEN);
            fallingBlock.getLocation().getWorld().spawnParticle(Particle.REDSTONE, fallingBlock.getLocation(), 1, 2.0, 0.0, 2.0, 0.03, ParticleColor.LIME_GREEN);
            fallingBlock.getLocation().getWorld().spawnParticle(Particle.REDSTONE, fallingBlock.getLocation(), 1, 2.0, 0.0, 2.0, 0.03, ParticleColor.DARK_GREEN);
            fallingBlock.getLocation().getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, fallingBlock.getLocation(), 50, 2.2, 0.2, 2.2, 0.03);
        }, 100);
    }

    
    @SuppressWarnings("unused")
	private void Fountain(final Player player, final boolean isStaff) {
        long remainingTime;
        long cooldownDuration = isStaff ? STAFF_COOLDOWN : PLAYER_COOLDOWN;

        if (this.cooldowns.containsKey(player.getUniqueId()) && (remainingTime = this.cooldowns.get(player.getUniqueId()) - System.currentTimeMillis()) > 0L) {
            player.sendMessage(ChatUtil.color("&7[&dMagic&7] &fThis spell is still on a &d&lCOOLDOWN&c&l! &fYou have to wait &d" + remainingTime / 1000L + " &fseconds"));
            return;
        }
        this.cooldowns.put(player.getUniqueId(), System.currentTimeMillis() + cooldownDuration);

        Entity targetEntity = getTargetEntity(player); 
       
        if (targetEntity != null) {
            targetEntity.getWorld().createExplosion(targetEntity.getLocation(), 3);
            targetEntity.setVelocity(new Vector(0, 1.8, 0));
            targetEntity.getLocation().getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, targetEntity.getLocation(), 150, 3.0, 2.0, 1.0, 0.8);
            targetEntity.getLocation().getWorld().spawnParticle(Particle.TOTEM, targetEntity.getLocation(), 50, 1.0, 1.0, 1.0, 0.03);
            if (targetEntity != null && targetEntity instanceof LivingEntity) {
                LivingEntity livingEntity = (LivingEntity) targetEntity;
            }
            
        } else {
        	 Random random = new Random();
            Block block = player.getTargetBlockExact(100);
            Material blockname = FLOWERS.get(random.nextInt(FLOWERS.size()));
            List<Material> randomFlowers = getRandomFlowers(5);
            if (block != null) {
                Location spawnLocation = block.getLocation().add(0, 1, 0);
                spawnLocation.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, spawnLocation, 150, 3.0, 2.0, 1.0, 0.8);
                spawnLocation.getWorld().spawnParticle(Particle.TOTEM, spawnLocation, 50, 1.0, 1.0, 1.0, 0.03);
                BukkitTask FlowerFountain = Bukkit.getScheduler().runTaskTimer(Main.getPlugin(Main.class), new Runnable() {
                    @Override
                    public void run() {
                    	 for (Material flower : randomFlowers) {
                             spawnFlowers(spawnLocation, flower);
                             player.playSound(player.getLocation(), Sound.BLOCK_CHORUS_FLOWER_GROW, 1.0F, 1.0F);
                             player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_XYLOPHONE, 1.0F, -2.0F);
                         }
                    }
                }, 0L, 4L); 

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        FlowerFountain.cancel();
                    }
                }.runTaskLater(Main.getPlugin(Main.class), 40L); 
            
        
               
                
            } else {
            	 Location spawnLocation = player.getEyeLocation().add(player.getEyeLocation().getDirection().multiply(25));
                 spawnLocation.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, spawnLocation, 150, 3.0, 2.0, 1.0, 0.8);
                 spawnLocation.getWorld().spawnParticle(Particle.TOTEM, spawnLocation, 50, 1.0, 1.0, 1.0, 0.03);
                 spawnLocation.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, spawnLocation, 150, 3.0, 2.0, 0.0, 0.8);
                 
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