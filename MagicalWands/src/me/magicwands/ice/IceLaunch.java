package me.magicwands.ice;

import org.bukkit.Color;
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
import org.bukkit.util.Vector;

import me.magicwands.main.Main;
import me.magicwands.utils.ChatUtil;
import me.magicwands.utils.CooldownManager;
import me.magicwands.utils.GetTargetEntity;

public class IceLaunch implements Listener {
    private CooldownManager cooldownManager;

    public IceLaunch() {
        long cooldownTimeInSeconds = Main.getPlugin(Main.class).getConfig().getLong("spells.IceLaunch.cooldown", 30L); // default 30 seconds
        this.cooldownManager = new CooldownManager(cooldownTimeInSeconds);
        this.cooldownManager.setDefaultCooldownIfNotExist(cooldownTimeInSeconds, "IceLaunch");
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (player.getItemInHand().getItemMeta() != null && 
        	    player.getItemInHand().getItemMeta().getDisplayName() != null && 
        	    player.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(me.magicwands.wands.IceWand.IceWand.getItemMeta().getDisplayName()) && 
        	    event.getAction() == Action.LEFT_CLICK_AIR && 
        	    me.magicwands.events.IceWand.IceSpell.getOrDefault(player, 0) == 5) {
            
            boolean isStaff = player.hasPermission("magicwands.*");

          
            if (player.hasPermission("magicwands.ice.coldlaunch.bypasscooldown") || player.hasPermission("magicwands.ice.bypasscooldown") || isStaff) {
                this.launchIce(player);
                return;
            }

            long cooldownTimeInMillis = cooldownManager.getCooldownDuration(player, "IceLaunch");
            if (this.cooldownManager.hasCooldown(player, "IceLaunch")) {
                long secondsRemaining = this.cooldownManager.getCooldownRemaining(player, "IceLaunch");
                String color = Main.getPlugin(Main.class).getConfig().getString("wands.ice.color");
                player.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&',
                        ChatUtil.color("&7[&" + color + "MagicWands&7] &fThis spell is still on a &" + color + "cooldown&c&l! &fYou have to wait &" + color +
                                secondsRemaining + " &fseconds")));
                return;
            }
            this.cooldownManager.startCooldown(player, cooldownTimeInMillis, "IceLaunch");
            this.launchIce(player);
        }
        }
    

    private void launchIce(final Player player) {
        Entity targetEntity = GetTargetEntity.getTargetEntity(player);

        if (targetEntity != null) {
           
            targetEntity.setVelocity(new Vector(0, 1.8, 0));
            Particle.DustOptions iceShardColor = new Particle.DustOptions(Color.fromRGB(170, 255, 255), 1.0f);
            targetEntity.getWorld().spawnParticle(Particle.REDSTONE, targetEntity.getLocation(), 50, 1.0, 1.0, 1.0, 0.03, iceShardColor);
            targetEntity.getWorld().spawnParticle(Particle.BLOCK_CRACK, targetEntity.getLocation(), 25, 0.0, 0.0, 0.0, 0.03, Material.ICE.createBlockData());
            targetEntity.getLocation().getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, targetEntity.getLocation(), 150, 3.0, 2.0, 1.0, 0.8);
            targetEntity.getLocation().getWorld().spawnParticle(Particle.CLOUD, targetEntity.getLocation(), 50, 1.0, 1.0, 1.0, 0.03);
            targetEntity.getWorld().playSound(targetEntity.getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 1, 1); 
            if (targetEntity instanceof LivingEntity) {
                LivingEntity livingEntity = (LivingEntity) targetEntity;
                livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 2));
                livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 100, 2));
                livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 700, 2));
            }
        } else {
            Block block = player.getTargetBlockExact(100);

            if (block != null) {
                Location spawnLocation = block.getLocation().add(0, 1, 0);
                Particle.DustOptions iceShardColor = new Particle.DustOptions(Color.fromRGB(170, 255, 255), 1.0f);
                spawnLocation.getWorld().spawnParticle(Particle.REDSTONE, spawnLocation, 50, 1.0, 1.0, 1.0, 0.03, iceShardColor);
                spawnLocation.getWorld().spawnParticle(Particle.BLOCK_CRACK, spawnLocation, 25, 0.0, 0.0, 0.0, 0.03, Material.ICE.createBlockData());
                spawnLocation.getWorld().playSound(spawnLocation, Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 1, 1);  
                spawnLocation.getWorld().playSound(spawnLocation, Sound.BLOCK_FIRE_EXTINGUISH, 1, 1); 
                spawnLocation.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, spawnLocation, 150, 3.0, 2.0, 1.0, 0.8);
                spawnLocation.getWorld().spawnParticle(Particle.CLOUD, spawnLocation, 50, 1.0, 1.0, 1.0, 0.03);
            } else {
            	Location spawnLocation = player.getEyeLocation().add(player.getEyeLocation().getDirection().multiply(25));
                Location playerLocation = player.getLocation();
                
                spawnLocation.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, spawnLocation, 150, 3.0, 2.0, 0.0, 0.8);
                spawnLocation.getWorld().playSound(spawnLocation, Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 1, 1);  
                spawnLocation.getWorld().playSound(spawnLocation, Sound.BLOCK_FIRE_EXTINGUISH, 1, 1); 
                spawnLocation.getWorld().playSound(playerLocation, Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 1, 1);  
                spawnLocation.getWorld().playSound(playerLocation, Sound.BLOCK_FIRE_EXTINGUISH, 1, 1); 
                Particle.DustOptions iceShardColor = new Particle.DustOptions(Color.fromRGB(170, 255, 255), 1.0f);

                spawnLocation.getWorld().spawnParticle(Particle.REDSTONE, spawnLocation, 50, 1.0, 1.0, 1.0, 0.03, iceShardColor);
                spawnLocation.getWorld().spawnParticle(Particle.BLOCK_CRACK, spawnLocation, 25, 0.0, 0.0, 0.0, 0.03, Material.ICE.createBlockData());
               
                spawnLocation.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, spawnLocation, 150, 3.0, 2.0, 1.0, 0.8);
                spawnLocation.getWorld().spawnParticle(Particle.CLOUD, spawnLocation, 50, 1.0, 1.0, 1.0, 0.03);
            }
        }
    }

}
