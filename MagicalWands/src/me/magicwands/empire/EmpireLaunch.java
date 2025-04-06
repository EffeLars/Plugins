package me.magicwands.empire;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import me.magicwands.events.EmpireWand;
import me.magicwands.main.Main;
import me.magicwands.utils.ChatUtil;
import me.magicwands.utils.CooldownManager;
import me.magicwands.utils.GetTargetEntity;
import me.magicwands.wands.WandItems;

public class EmpireLaunch implements Listener {
	  private CooldownManager cooldownManager;
	    private long cooldownTimeInSeconds;

	    public EmpireLaunch() {
	        this.cooldownTimeInSeconds = Main.getPlugin(Main.class).getConfig().getLong("spells.EmpireLaunch.cooldown", 10L);
	        this.cooldownManager = new CooldownManager(cooldownTimeInSeconds);
	        this.cooldownManager.setDefaultCooldownIfNotExist(cooldownTimeInSeconds, "EmpireLaunch");
	    }

	    @EventHandler
	    public void onPlayerInteract(PlayerInteractEvent event) {
	        Player player = event.getPlayer();
	        
	        if (player.getItemInHand() != null && player.getItemInHand().getItemMeta() != null) {
	            String itemDisplayName = player.getItemInHand().getItemMeta().getDisplayName();
	            
	            if (player.getItemInHand().getItemMeta() != null && 
	            	    player.getItemInHand().getItemMeta().getDisplayName() != null && 
	            	    player.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(WandItems.EmpireWand.getItemMeta().getDisplayName()) && 
	            	    event.getAction() == Action.LEFT_CLICK_AIR && 
	            	    EmpireWand.EmpireSpell.getOrDefault(player, 0) == 2) { 
	                
	                boolean isStaff = player.hasPermission("magicwands.empire.empirelaunch.bypasscooldown") || player.hasPermission("magicwands.empire.*");
	                this.launchEmpireSpell(player, isStaff);
	            }
	        }
	    }


	    private void launchEmpireSpell(final Player player, final boolean isStaff) {
	        long remainingTime, cooldownDuration = isStaff ? 0L : cooldownManager.getCooldownDuration(player, "EmpireLaunch");

	        if (cooldownManager.hasCooldown(player, "EmpireLaunch") &&
	            (remainingTime = cooldownManager.getCooldownRemaining(player, "EmpireLaunch")) > 0L) {
	            String color = Main.getPlugin(Main.class).getConfig().getString("wands.empire.color");
	            long secondsRemaining = this.cooldownManager.getCooldownRemaining(player, "EmpireSpark");
                player.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&',
                        ChatUtil.color("&7[&" + color + "MagicWands&7] &fThis spell is still on a &" + color + "cooldown&c&l! &fYou have to wait &" + color +
                                secondsRemaining + " &fseconds")));
	            return;
	        }

	        cooldownManager.startCooldown(player, cooldownDuration, "EmpireLaunch");
	        Entity targetEntity = GetTargetEntity.getTargetEntity(player);
	        Location loc = (targetEntity != null) ? targetEntity.getLocation() : player.getTargetBlockExact(100) != null
	                        ? player.getTargetBlockExact(100).getLocation().add(0, 1, 0)
	                        : player.getEyeLocation().add(player.getEyeLocation().getDirection().multiply(25));

	        playEmpireSpellEffects(player, loc);

	        if (targetEntity != null) targetEntity.setVelocity(new Vector(0, 1.8, 0));
	    }

	    private void playEmpireSpellEffects(Player player, Location location) {
	        World world = location.getWorld();
	        Particle.DustOptions empireDarkPurple = new Particle.DustOptions(Color.fromRGB(75, 0, 130), 1.5f);

	        world.playSound(location, Sound.BLOCK_DISPENSER_LAUNCH, 1, 1);
	        world.playSound(location, Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 1, 1);
	        world.playSound(location, Sound.ITEM_FIRECHARGE_USE, 1, 1);
	        
	        world.playSound(player.getLocation(), Sound.BLOCK_DISPENSER_LAUNCH, 1, 1);
	        world.playSound(player.getLocation(), Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 1, 1);
	        world.playSound(player.getLocation(), Sound.ITEM_FIRECHARGE_USE, 1, 1);

	        world.spawnParticle(Particle.ENCHANTMENT_TABLE, location, 150, 3.0, 2.0, 1.0, 0.8);
	        world.spawnParticle(Particle.REDSTONE, location, 40, 1, 1, 1, empireDarkPurple);
	        world.spawnParticle(Particle.SMOKE_NORMAL, location, 20, 1.0, 2.0, 1.0, 0.03);
	        world.spawnParticle(Particle.SMOKE_LARGE, location, 40, 1.0, 2.0, 1.0, 0.03);
	    }
}