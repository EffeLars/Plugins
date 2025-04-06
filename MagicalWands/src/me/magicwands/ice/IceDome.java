package me.magicwands.ice;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
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
import org.bukkit.scheduler.BukkitRunnable;

import me.magicwands.main.Main;
import me.magicwands.utils.GetTargetEntity;
import me.magicwands.wands.IceWand;

public class IceDome implements Listener {

   ;

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (player.getItemInHand().getItemMeta() != null && 
        	    player.getItemInHand().getItemMeta().getDisplayName() != null && 
        	    player.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(IceWand.IceWand.getItemMeta().getDisplayName()) && 
        	    event.getAction() == Action.LEFT_CLICK_AIR && 
        	    me.magicwands.events.IceWand.IceSpell.getOrDefault(player, 0) == 11) {
            boolean isStaff = player.hasPermission("staff.wand");
            this.IceDome(player, isStaff);
        }
        }
    

    private void IceDome(final Player player, final boolean isStaff) {
       
        
        Entity targetEntity = GetTargetEntity.getTargetEntity(player);
       
        if (targetEntity != null) {
            
            targetEntity.getLocation().getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, targetEntity.getLocation(), 150, 3.0, 2.0, 1.0, 0.8);
            targetEntity.getLocation().getWorld().spawnParticle(Particle.CLOUD, targetEntity.getLocation(), 50, 1.0, 1.0, 1.0, 0.03);
            
            if (targetEntity instanceof LivingEntity) {
                LivingEntity livingEntity = (LivingEntity) targetEntity;
                buildIceDome((Player) targetEntity);
                
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        removeIceDome();
                    }
                }.runTaskLater(Main.getPlugin(Main.class), 200L);

                livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 2));
                livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 100, 2));
                livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 700, 2));   
            }
        } else {
            Block block = player.getTargetBlockExact(100);

            if (block != null) {
                Location spawnLocation = block.getLocation().add(0, 1, 0);
                spawnLocation.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, spawnLocation, 150, 3.0, 2.0, 1.0, 0.8);
                spawnLocation.getWorld().spawnParticle(Particle.CLOUD, spawnLocation, 50, 1.0, 1.0, 1.0, 0.03);
            } else {
                Location spawnLocation = player.getEyeLocation().add(player.getEyeLocation().getDirection().multiply(25));
                buildIceDome((Player) targetEntity);
                
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        removeIceDome();
                    }
                }.runTaskLater(Main.getPlugin(Main.class), 200L);

                spawnLocation.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, spawnLocation, 150, 3.0, 2.0, 1.0, 0.8);
                spawnLocation.getWorld().spawnParticle(Particle.CLOUD, spawnLocation, 50, 1.0, 1.0, 1.0, 0.03);
                spawnLocation.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, spawnLocation, 150, 3.0, 2.0, 0.0, 0.8);
            }
        }
    }

    private static final int RADIUS = 3;
    private List<Block> iceBlocks = new ArrayList<>();

    private void buildIceDome(Player targetPlayer) {
        Location center = targetPlayer.getLocation().clone().add(0, 1, 0);

        final int[] delayCounter = {0};
        final int[] totalBlocks = {0};

        for (int x = -RADIUS; x <= RADIUS; x++) {
            for (int y = -RADIUS; y <= RADIUS; y++) {
                for (int z = -RADIUS; z <= RADIUS; z++) {
                   
                    double distance = Math.sqrt(x * x + y * y + z * z);
                    if (distance >= RADIUS - 0.5 && distance <= RADIUS + 0.5) {
                        totalBlocks[0]++;
                    }
                }
            }
        }

        for (int x = -RADIUS; x <= RADIUS; x++) {
            for (int y = -RADIUS; y <= RADIUS; y++) {
                for (int z = -RADIUS; z <= RADIUS; z++) {
                    Location blockLocation = center.clone().add(x, y, z);
                    double distance = Math.sqrt(x * x + y * y + z * z);
                    if (distance >= RADIUS - 0.5 && distance <= RADIUS + 0.5) {
                        Block block = blockLocation.getBlock();
                        if (block.getType() == Material.AIR) {
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    Random rand = new Random();
                                    Material[] materials = {Material.ICE, Material.BLUE_ICE, Material.PACKED_ICE, Material.SNOW_BLOCK};
                                    Material blockMaterial = materials[rand.nextInt(materials.length)];
                                    block.setType(blockMaterial);
                                    block.getLocation().getWorld().playSound(block.getLocation(), Sound.BLOCK_GLASS_BREAK, 1, 1);
                                    block.getLocation().getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, block.getLocation(), 50, 0.5, 0.5, 0.5, 0.008);
                                    block.getLocation().getWorld().spawnParticle(Particle.CLOUD, block.getLocation(), 20, 1.0, 1.0, 0.0, 0.08);
                                    block.getLocation().getWorld().spawnParticle(Particle.BLOCK_CRACK, block.getLocation(), 60, 0.0, 0.0, 0.0, 0.03, Material.ICE.createBlockData());
                                    targetPlayer.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 5));
                                    targetPlayer.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 100, 2));
                                    targetPlayer.setVelocity(targetPlayer.getVelocity().setY(0));
                                    targetPlayer.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 700, 2));
                                    iceBlocks.add(block); 
                                    if (delayCounter[0] >= totalBlocks[0]) {
                                        targetPlayer.teleport(center);
                                    }
                                }
                            }.runTaskLater(Main.getPlugin(Main.class), delayCounter[0] * 1L);
                            delayCounter[0]++;
                        }
                    }
                }
            }
        }
    }

    private void removeIceDome() {
        if (iceBlocks.isEmpty()) {

            return;
        }

        for (Block block : iceBlocks) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    Material[] materials = {Material.ICE, Material.BLUE_ICE, Material.PACKED_ICE, Material.SNOW_BLOCK};
                    for (Material material : materials) {
                        if (block.getType() == material) {
                            block.setType(Material.AIR);
                            block.getLocation().getWorld().playSound(block.getLocation(), Sound.BLOCK_GLASS_BREAK, 1, 1);
                            block.getLocation().getWorld().spawnParticle(Particle.BLOCK_CRACK, block.getLocation(), 20, 0.0, 0.0, 0.0, 0.03, material.createBlockData());
                            break;
                        }
                    }
                }
            }.runTask(Main.getPlugin(Main.class));
        }

        iceBlocks.clear();
    }
}