/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.Sound
 *  org.bukkit.attribute.Attribute
 *  org.bukkit.attribute.AttributeModifier
 *  org.bukkit.attribute.AttributeModifier$Operation
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.block.Action
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.inventory.EquipmentSlot
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 */
package me.magicwands.customitems;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import me.magicwands.main.Main;
import me.magicwands.utils.ChatUtil;
import me.magicwands.utils.CooldownManager;
import me.magicwands.utils.GetTargetEntity;
import me.magicwands.wands.LightItems;

public class LightSword
implements Listener {
	
	private Map<UUID, Boolean> PlayerAbility = new HashMap<>();
	
	private CooldownManager cooldownManager;
    private long cooldownTimeInSeconds;

    public LightSword() {
        this.cooldownTimeInSeconds = Main.getPlugin(Main.class).getConfig().getLong("items.LightSword.cooldown", 30L);
        this.cooldownManager = new CooldownManager(cooldownTimeInSeconds);
        this.cooldownManager.setDefaultCooldownIfNotExist(cooldownTimeInSeconds, "LightSword");
    }

 
	
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();
        boolean isStaff = player.hasPermission("magicwands.light.sword.bypasscooldown") || player.hasPermission("magicwands.light.*");
        if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
            ItemMeta meta;
            ItemStack heldItem = player.getInventory().getItemInMainHand();
            if (heldItem != null && (meta = heldItem.getItemMeta()) != null && meta.getDisplayName().equals(LightItems.HaldirionSword.getItemMeta().getDisplayName())) {
                if(!player.hasPermission("magicwands.light.sword.bypasscooldown") || player.hasPermission("magicwands.light.*")) {
                	long remainingTime;
                    long cooldownDuration = isStaff ? 0L : this.cooldownManager.getCooldownDuration(player, "LightSword");

                    if (this.cooldownManager.hasCooldown(player, "LightSword") && (remainingTime = this.cooldownManager.getCooldownRemaining(player, "LightSword")) > 0L) {
                         String color = Main.getPlugin(Main.class).getConfig().getString("wands.light.color");
                         player.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes((char) '&',
                                 (ChatUtil.color("&7[&" + color + "MagicWands&7] &fThis ability is still on a &" + color + "cooldown&c&l! &fYou have to wait &" + color +
                                         + remainingTime + " &fseconds"))));
                        return;
                    }

                    this.cooldownManager.startCooldown(player, cooldownDuration, "LightSword");
                }
            	this.startParticles(player);
            	
            	  String color = Main.getPlugin(Main.class).getConfig().getString("wands.light.color");
                player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1);
                player.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes((char) '&',
                        (ChatUtil.color("&7[&" + color + "MagicWands&7] &fYou are now charging your ability..."))));
                
                    new BukkitRunnable() {
                        public void run() {
                           PlayerAbility.put(player.getUniqueId(), true);
                           player.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes((char) '&',
                                   (ChatUtil.color("&7[&" + color + "MagicWands&7] &fThe ability is now &aready&7!"))));
                           stopParticles();
                        }
                    }.runTaskLater(Main.getPlugin(Main.class), 200);
                }

            
        }
    }

    @EventHandler
    public void SwordHit(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();
        if (action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK) {
            ItemMeta meta;
            ItemStack heldItem = player.getInventory().getItemInMainHand();
            if (heldItem != null  && (meta = heldItem.getItemMeta()) != null && meta.getDisplayName().equals(LightItems.HaldirionSword.getItemMeta().getDisplayName())) {
                if(PlayerAbility.containsKey(player.getUniqueId())) {
                player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1);
                Block block = player.getTargetBlockExact(100);

                if (block != null) {
                	player.getWorld().strikeLightning(block.getLocation());
                	player.getWorld().strikeLightning(block.getLocation());
                	player.getWorld().strikeLightning(block.getLocation());
                    PlayerAbility.remove(player.getUniqueId());

                } else {
                	//
                }
                Entity target = GetTargetEntity.getTargetEntity(player);
                player.getWorld().strikeLightning(GetTargetEntity.getTargetEntity(player).getLocation());
            	player.getWorld().strikeLightning(GetTargetEntity.getTargetEntity(player).getLocation());
            	player.getWorld().strikeLightning(GetTargetEntity.getTargetEntity(player).getLocation());
                PlayerAbility.remove(player.getUniqueId());
                Location location1 = target.getLocation();
                Particle.DustOptions dustOptions = new Particle.DustOptions(Color.WHITE, 1.0f);
                player.getWorld().spawnParticle(Particle.REDSTONE, location1, 10, 1.0, 1.0, 1.0, dustOptions);
                player.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, location1, 30, 1, 1, 1, 0.05);
                player.getWorld().spawnParticle(Particle.END_ROD, location1, 10, 1, 1, 0, 0.005);
                player.getWorld().spawnParticle(Particle.WHITE_ASH, location1, 10, 1, 1, 0, 0.05);

                return;
                } else {
                	//
                }
                
            }
            
        }
    }
    
    public void setDamage(ItemStack sword, double damageAmount) {
        ItemMeta meta = sword.getItemMeta();
        meta.removeAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE);
        AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(), "generic.attackDamage", damageAmount, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
        meta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, modifier);
        sword.setItemMeta(meta);
    }

    private BukkitRunnable particleTask;

    private void startParticles(Player player) {
        particleTask = new BukkitRunnable() {
            double angle = 0;
            double radius = 1.5;
            double height1 = 0;
            double height2 = 0;
            boolean goingUp1 = true;
            boolean goingUp2 = false;

            @Override
            public void run() {
                angle += 0.1;

                if (goingUp1) {
                    height1 += 0.1;
                    if (height1 >= 3) {
                        goingUp1 = false;
                    }
                } else {
                    height1 -= 0.1;
                    if (height1 <= 0) {
                        goingUp1 = true;
                    }
                }

                if (goingUp2) {
                    height2 += 0.1;
                    if (height2 >= 3) {
                        goingUp2 = false;
                    }
                } else {
                    height2 -= 0.1;
                    if (height2 <= 0) {
                        goingUp2 = true;
                    }
                }

                double x1 = radius * Math.cos(angle);
                double z1 = radius * Math.sin(angle);
                double x2 = radius * Math.cos(angle + Math.PI);
                double z2 = radius * Math.sin(angle + Math.PI);

                Location location1 = player.getLocation().add(x1, height1, z1);
                Location location2 = player.getLocation().add(x2, height2, z2);

                Particle.DustOptions dustOptions = new Particle.DustOptions(Color.WHITE, 1.0f);
                player.getWorld().spawnParticle(Particle.REDSTONE, location1, 10, 0.0, 0.0, 0.0, dustOptions);
                player.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, location1, 3, 1, 1, 1, 0.05);
                player.getWorld().spawnParticle(Particle.END_ROD, location1, 1, 0, 0, 0, 0.005);
                player.getWorld().spawnParticle(Particle.WHITE_ASH, location1, 1, 0, 0, 0, 0.05);

                player.getWorld().spawnParticle(Particle.REDSTONE, location2, 10, 0.0, 0.0, 0.0, dustOptions);
                player.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, location2, 3, 1, 1, 1, 0.05);
                player.getWorld().spawnParticle(Particle.END_ROD, location2, 1, 0, 0, 0, 0.005);
                player.getWorld().spawnParticle(Particle.WHITE_ASH, location2, 1, 0, 0, 0, 0.05);
            }
        };
        particleTask.runTaskTimer(Main.getPlugin(Main.class), 0L, 1L);
    }

    private void stopParticles() {
        if (particleTask != null) {
            particleTask.cancel();
            particleTask = null;
        }
    }
}

