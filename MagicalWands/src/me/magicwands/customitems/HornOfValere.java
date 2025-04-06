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

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.magicwands.main.Main;
import me.magicwands.utils.ChatUtil;
import me.magicwands.utils.CooldownManager;
import me.magicwands.wands.CustomItems;

public class HornOfValere
implements Listener {

	private CooldownManager cooldownManager;
    private long cooldownTimeInSeconds;

    public HornOfValere() {
        this.cooldownTimeInSeconds = Main.getPlugin(Main.class).getConfig().getLong("items.Horn.cooldown", 30L);
        this.cooldownManager = new CooldownManager(cooldownTimeInSeconds);
        this.cooldownManager.setDefaultCooldownIfNotExist(cooldownTimeInSeconds, "Horn");
    }

 
	
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();
        boolean isStaff = player.hasPermission("magicwands.customitems.horn.bypasscooldown") || player.hasPermission("magicwands.customitems.*");
        if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
            ItemMeta meta;
            ItemStack heldItem = player.getInventory().getItemInMainHand();
            if (heldItem != null && (meta = heldItem.getItemMeta()) != null && meta.getDisplayName().equals(CustomItems.HornOfValereItem.getItemMeta().getDisplayName())) {
            	event.setCancelled(true);
                if(!player.hasPermission("magicwands.customitems.horn.bypasscooldown") || player.hasPermission("magicwands.customitems.*")) {
                	long remainingTime;
                    long cooldownDuration = isStaff ? 0L : this.cooldownManager.getCooldownDuration(player, "Horn");

                    if (this.cooldownManager.hasCooldown(player, "Horn") && (remainingTime = this.cooldownManager.getCooldownRemaining(player, "Horn")) > 0L) {
                         String color = Main.getPlugin(Main.class).getConfig().getString("wands.light.color");
                         player.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes((char) '&',
                                 (ChatUtil.color("&7[&" + color + "MagicWands&7] &fThis ability is still on a &" + color + "cooldown&c&l! &fYou have to wait &" + color +
                                         + remainingTime + " &fseconds"))));
                        return;
                    }

                    this.cooldownManager.startCooldown(player, cooldownDuration, "Horn");
                }
            	PlayAlarm(player);
            	
            }}
    }

    

    private void PlayAlarm(Player player) {
       
                
                Particle.DustOptions dustOptions = new Particle.DustOptions(Color.WHITE, 1.0f);
                Location location1 = player.getLocation();
                player.getWorld().spawnParticle(Particle.REDSTONE, location1, 10, 0.0, 0.0, 0.0, dustOptions);
                player.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, location1, 3, 1, 1, 1, 0.05);
                player.getWorld().spawnParticle(Particle.END_ROD, location1, 1, 0, 0, 0, 0.005);
                player.getWorld().spawnParticle(Particle.WHITE_ASH, location1, 1, 0, 0, 0, 0.05);
                for (Entity entity : player.getWorld().getNearbyEntities(location1, 20, 20, 20)) {
                    if (entity instanceof Player) {
                        Player nearbyPlayer = (Player) entity;
                        nearbyPlayer.getWorld().playSound(nearbyPlayer.getLocation(), Sound.ITEM_GOAT_HORN_SOUND_7, Integer.MAX_VALUE, -3);
                        Bukkit.getScheduler().runTaskLater(Main.getPlugin(Main.class), () -> 
                        nearbyPlayer.getWorld().playSound(nearbyPlayer.getLocation(), Sound.ENTITY_PIGLIN_CELEBRATE, (float) 0.3, -1), 100L);
                        Bukkit.getScheduler().runTaskLater(Main.getPlugin(Main.class), () -> 
                        nearbyPlayer.getWorld().playSound(nearbyPlayer.getLocation(), Sound.ENTITY_PIGLIN_ANGRY, (float) 0.3, -2), 120L);
                        Bukkit.getScheduler().runTaskLater(Main.getPlugin(Main.class), () -> 
                        nearbyPlayer.getWorld().playSound(nearbyPlayer.getLocation(), Sound.ENTITY_PIGLIN_ANGRY, (float) 0.3, -2), 120L);
                        Bukkit.getScheduler().runTaskLater(Main.getPlugin(Main.class), () -> 
                        nearbyPlayer.getWorld().playSound(nearbyPlayer.getLocation(), Sound.BLOCK_BELL_USE, Integer.MAX_VALUE, -2), 130L);
                        Bukkit.getScheduler().runTaskLater(Main.getPlugin(Main.class), () -> 
                        nearbyPlayer.getWorld().playSound(nearbyPlayer.getLocation(), Sound.BLOCK_BELL_USE, Integer.MAX_VALUE, -2), 150L);
                        Bukkit.getScheduler().runTaskLater(Main.getPlugin(Main.class), () -> 
                        nearbyPlayer.getWorld().playSound(nearbyPlayer.getLocation(), Sound.BLOCK_BELL_USE, Integer.MAX_VALUE, -2), 180L);
                        Bukkit.getScheduler().runTaskLater(Main.getPlugin(Main.class), () -> 
                        nearbyPlayer.getWorld().playSound(nearbyPlayer.getLocation(), Sound.BLOCK_BELL_USE, Integer.MAX_VALUE, -2), 200L);
                        Bukkit.getScheduler().runTaskLater(Main.getPlugin(Main.class), () -> 
                        nearbyPlayer.getWorld().playSound(nearbyPlayer.getLocation(), Sound.BLOCK_BELL_USE, Integer.MAX_VALUE, -2), 230L);
                        Bukkit.getScheduler().runTaskLater(Main.getPlugin(Main.class), () -> 
                        nearbyPlayer.getWorld().playSound(nearbyPlayer.getLocation(), Sound.BLOCK_BELL_USE, Integer.MAX_VALUE, -2), 270L);
                        Bukkit.getScheduler().runTaskLater(Main.getPlugin(Main.class), () -> 
                        nearbyPlayer.getWorld().playSound(nearbyPlayer.getLocation(), Sound.ENTITY_PIGLIN_ANGRY, Integer.MAX_VALUE, -2), 300L);
                        Bukkit.getScheduler().runTaskLater(Main.getPlugin(Main.class), () -> 
                        nearbyPlayer.getWorld().playSound(nearbyPlayer.getLocation(), Sound.ENTITY_PIGLIN_ANGRY, Integer.MAX_VALUE, -2), 300L);
                        Bukkit.getScheduler().runTaskLater(Main.getPlugin(Main.class), () -> 
                        nearbyPlayer.getWorld().playSound(nearbyPlayer.getLocation(), Sound.ENTITY_PIGLIN_BRUTE_ANGRY, Integer.MAX_VALUE, -3), 320L);
                        
                    }
                }

    }


   
}

