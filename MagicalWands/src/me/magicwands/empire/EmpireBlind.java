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
package me.magicwands.empire;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.magicwands.events.EmpireWand;
import me.magicwands.main.Main;
import me.magicwands.utils.ChatUtil;
import me.magicwands.utils.CooldownManager;
import me.magicwands.utils.GetTargetEntity;
import me.magicwands.utils.SpawnFirework;
import me.magicwands.wands.WandItems;

public class EmpireBlind implements Listener {

	private CooldownManager cooldownManager;

	public EmpireBlind() {
	    long cooldownTimeInSeconds = Main.getPlugin(Main.class).getConfig().getLong("spells.empireblind.cooldown", 30L);
	    this.cooldownManager = new CooldownManager(cooldownTimeInSeconds);
	    this.cooldownManager.setDefaultCooldownIfNotExist(cooldownTimeInSeconds, "EmpireBlind");
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
	    Player player = event.getPlayer();
	    if (player.getItemInHand().getItemMeta() != null && 
	        player.getItemInHand().getItemMeta().getDisplayName() != null && 
	        player.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(WandItems.EmpireWand.getItemMeta().getDisplayName()) && 
	        event.getAction() == Action.LEFT_CLICK_AIR && 
	        EmpireWand.EmpireSpell.getOrDefault(player, 0) == 6) {

	        boolean isStaff = player.hasPermission("magicwands.*");

	        if (player.hasPermission("magicwands.empire.empireblind.bypasscooldown") || player.hasPermission("magicwands.empireblind.*") || isStaff) {
	            EmpireBlind(player); 
	            return;
	        }

	        if (this.cooldownManager.hasCooldown(player, "EmpireBlind")) { 
	            long secondsRemaining = this.cooldownManager.getCooldownRemaining(player, "EmpireBlind"); 
	            String color = Main.getPlugin(Main.class).getConfig().getString("wands.empire.color");
	            player.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&',
	                    ChatUtil.color("&7[&" + color + "MagicWands&7] &fThis spell is still on a &" + color + "cooldown&c&l! &fYou have to wait &" + color +
	                            secondsRemaining + " &fseconds")));
	            return;
	        }

	        long cooldownTimeInMillis = cooldownManager.getCooldownDuration(player, "EmpireBlind"); 
	        this.cooldownManager.startCooldown(player, cooldownTimeInMillis, "EmpireBlind"); 
	        EmpireBlind(player); 
	    }
	}

	    private void EmpireBlind(Player player) {
	        Entity targetEntity = GetTargetEntity.getTargetEntity(player);
	        Location spawnLocation = targetEntity != null ? targetEntity.getLocation() 
	                : player.getTargetBlockExact(100) != null ? player.getTargetBlockExact(100).getLocation().add(0, 1, 0) 
	                : player.getEyeLocation().add(player.getEyeLocation().getDirection().multiply(25));

	        Color color1 = Color.BLACK, color2 = Color.PURPLE;
	        FireworkEffect.Type type = FireworkEffect.Type.BURST;
	        int power = 1;

	        SpawnFirework.spawnFirework(spawnLocation, color1, type, power);
	        SpawnFirework.spawnFirework(spawnLocation, color2, type, power);
	        spawnLocation.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, spawnLocation, 150, 3.0, 2.0, 1.0, 0.8);
	        spawnLocation.getWorld().spawnParticle(Particle.SMOKE_LARGE, spawnLocation, 50, 1.0, 1.0, 1.0, 0.03);
	        spawnLocation.getWorld().spawnParticle(Particle.CAMPFIRE_SIGNAL_SMOKE, spawnLocation, 10, 3.0, 3.0, 3.0, 0.03);

	        if (targetEntity instanceof LivingEntity) {
	            ((LivingEntity) targetEntity).addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 200, 2));
	        }
	    }
}