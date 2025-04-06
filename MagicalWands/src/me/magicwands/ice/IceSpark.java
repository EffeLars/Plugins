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
package me.magicwands.ice;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
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

import me.magicwands.main.Main;
import me.magicwands.utils.ChatUtil;
import me.magicwands.utils.CooldownManager;
import me.magicwands.utils.GetTargetEntity;
import me.magicwands.utils.SpawnFirework;
import me.magicwands.wands.IceWand;

public class IceSpark implements Listener {
    private CooldownManager cooldownManager;

    public IceSpark() {
        long cooldownTimeInSeconds = Main.getPlugin(Main.class).getConfig().getLong("spells.IceSpark.cooldown", 30L); 
        this.cooldownManager = new CooldownManager(cooldownTimeInSeconds);
        this.cooldownManager.setDefaultCooldownIfNotExist(cooldownTimeInSeconds, "IceSpark");
    }

    @SuppressWarnings("deprecation")
	@EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (player.getItemInHand().getItemMeta() != null && 
        	    player.getItemInHand().getItemMeta().getDisplayName() != null && 
        	    player.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(IceWand.IceWand.getItemMeta().getDisplayName()) && 
        	    event.getAction() == Action.LEFT_CLICK_AIR && 
        	    me.magicwands.events.IceWand.IceSpell.getOrDefault(player, 0) == 3) {
            
            boolean isStaff = player.hasPermission("magicwands.*");

           
            if (player.hasPermission("magicwands.ice.coldspark.bypasscooldown") || player.hasPermission("magicwands.ice.bypasscooldown") || isStaff) {
                this.SparkPlayer(player, isStaff);
                return;
            }

            long cooldownTimeInMillis = cooldownManager.getCooldownDuration(player, "IceSpark");
            if (this.cooldownManager.hasCooldown(player, "IceSpark")) {
                long secondsRemaining = this.cooldownManager.getCooldownRemaining(player, "IceSpark");
                String color = Main.getPlugin(Main.class).getConfig().getString("wands.ice.color");
                player.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&',
                        ChatUtil.color("&7[&" + color + "MagicWands&7] &fThis spell is still on a &" + color + "cooldown&c&l! &fYou have to wait &" + color +
                                secondsRemaining + " &fseconds")));
                return;
            }
            this.cooldownManager.startCooldown(player, cooldownTimeInMillis, "IceSpark");
            this.SparkPlayer(player, isStaff);
        }
        }
    

    @SuppressWarnings("unused")
    private void SparkPlayer(final Player player, final boolean isStaff) {
  

        Entity targetEntity = GetTargetEntity.getTargetEntity(player); 

        if (targetEntity != null) {
            Color color = Color.AQUA; 
            Color color2 = Color.WHITE; 
            FireworkEffect.Type type = FireworkEffect.Type.BURST; 
            int power = 1; 
            long sparkdamage = Main.getPlugin(Main.class).getConfig().getInt("spells.firespark.damage");
            SpawnFirework.spawnFirework(targetEntity.getLocation(), color, type, power);
            SpawnFirework.spawnFirework(targetEntity.getLocation(), color2, type, power);
            targetEntity.getLocation().getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, targetEntity.getLocation(), 150, 3.0, 2.0, 1.0, 0.8);
            targetEntity.getLocation().getWorld().spawnParticle(Particle.CLOUD, targetEntity.getLocation(), 50, 1.0, 1.0, 1.0, 0.03);
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

                Color color = Color.WHITE;
                Color color2 = Color.AQUA; 
                FireworkEffect.Type type = FireworkEffect.Type.BURST; 
                int power = 1; 

                SpawnFirework.spawnFirework(spawnLocation, color, type, power);
                SpawnFirework.spawnFirework(spawnLocation, color2, type, power);
                spawnLocation.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, spawnLocation, 150, 3.0, 2.0, 1.0, 0.8);
                spawnLocation.getWorld().spawnParticle(Particle.CLOUD, spawnLocation, 50, 1.0, 1.0, 1.0, 0.03);

            } else {
                Location spawnLocation = player.getEyeLocation().add(player.getEyeLocation().getDirection().multiply(25));

                Color color = Color.WHITE; 
                Color color2 = Color.AQUA;
                FireworkEffect.Type type = FireworkEffect.Type.BURST; 
                int power = 1; 
                SpawnFirework.spawnFirework(spawnLocation, color, type, power);
                SpawnFirework.spawnFirework(spawnLocation, color2, type, power);
                spawnLocation.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, spawnLocation, 150, 3.0, 2.0, 1.0, 0.8);
                spawnLocation.getWorld().spawnParticle(Particle.CLOUD, spawnLocation, 50, 1.0, 1.0, 1.0, 0.03);
                spawnLocation.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, spawnLocation, 150, 3.0, 2.0, 0.0, 0.8);
            }
        }
    }
}

   
