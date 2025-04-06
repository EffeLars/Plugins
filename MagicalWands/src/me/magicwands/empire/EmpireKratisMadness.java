package me.magicwands.empire;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
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
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import me.magicwands.events.EmpireWand;
import me.magicwands.main.Main;
import me.magicwands.utils.ChatUtil;
import me.magicwands.utils.CooldownManager;
import me.magicwands.utils.GetTargetEntity;
import me.magicwands.wands.WandItems;

public class EmpireKratisMadness implements Listener {

	 private CooldownManager cooldownManager;

	    public EmpireKratisMadness() {
	        long cooldownTimeInSeconds = Main.getPlugin(Main.class).getConfig().getLong("spells.kratisempiremadness.cooldown", 30L);
	        this.cooldownManager = new CooldownManager(cooldownTimeInSeconds);
	        this.cooldownManager.setDefaultCooldownIfNotExist(cooldownTimeInSeconds, "KratisEmpireMadness");
	    }

	    @EventHandler
	    public void onPlayerInteract(PlayerInteractEvent event) {
	        Player player = event.getPlayer();
	        if (player.getItemInHand().getItemMeta() != null && 
	            player.getItemInHand().getItemMeta().getDisplayName() != null && 
	            player.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(WandItems.EmpireWand.getItemMeta().getDisplayName()) && 
	            event.getAction() == Action.LEFT_CLICK_AIR && 
	            EmpireWand.EmpireSpell.getOrDefault(player, 0) == 16) {

	            boolean isStaff = player.hasPermission("magicwands.*");

	            if (player.hasPermission("magicwands.empire.kratisempiremadness.bypasscooldown") || player.hasPermission("magicwands.kratisempiremadness.*") || isStaff) {
	                this.EmpireConfuse(player);
	                return;
	            }

	            if (this.cooldownManager.hasCooldown(player, "KratisEmpireMadness")) {
	                long secondsRemaining = this.cooldownManager.getCooldownRemaining(player, "KratisEmpireMadness");
	                String color = Main.getPlugin(Main.class).getConfig().getString("wands.empire.color");
	                player.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&',
	                        ChatUtil.color("&7[&" + color + "MagicWands&7] &fThis spell is still on a &" + color + "cooldown&c&l! &fYou have to wait &" + color +
	                                secondsRemaining + " &fseconds")));
	                return;
	            }

	            long cooldownTimeInMillis = cooldownManager.getCooldownDuration(player, "KratisEmpireMadness");
	            this.cooldownManager.startCooldown(player, cooldownTimeInMillis, "KratisEmpireMadness");
	            this.EmpireConfuse(player);
	        }
	    }

	    private void EmpireConfuse(Player player) {
	        Entity targetEntity = GetTargetEntity.getTargetEntity(player);
	        Location spawnLocation = targetEntity != null ? targetEntity.getLocation() 
	                : player.getTargetBlockExact(100) != null ? player.getTargetBlockExact(100).getLocation().add(0, 1, 0) 
	                : player.getEyeLocation().add(player.getEyeLocation().getDirection().multiply(25));
	        
	        spawnLocation.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, spawnLocation, 150, 3.0, 2.0, 1.0, 0.8);
	        spawnLocation.getWorld().spawnParticle(Particle.SMOKE_LARGE, spawnLocation, 50, 1.0, 1.0, 1.0, 0.03);
	        
	        if (targetEntity instanceof LivingEntity) {
	        	KratisMadness((Player)targetEntity);
	            ((LivingEntity) targetEntity).addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 100, 2));
	        }
	    }
	    
	    private BukkitTask KratisPlayerTask = null;

	    private void KratisMadness(Player player) {
	        final long[] totalTime = {0}; 
	        final Random random = new Random();
	        final Sound madnessSound = Sound.ENTITY_PHANTOM_HURT;

	        KratisPlayerTask = new BukkitRunnable() {
	            @Override
	            public void run() {
	                totalTime[0]++;

	                if (totalTime[0] >= 20) {
	                    KratisPlayerTask.cancel();
	                    KratisPlayerTask = null;
	                    return;
	                }

	                int action = random.nextInt(6); 

	                switch (action) {
	                    case 0:
	                        player.setVelocity(player.getLocation().getDirection().multiply(0.7)); 
	                        break;

	                    case 1:
	                        player.setVelocity(player.getLocation().getDirection().multiply(-0.7)); 
	                        break;

	                    case 2:
	                        if (player.isOnGround()) {
	                            player.setVelocity(new Vector(0, 0.8, 0)); 
	                        }
	                        break;      
	                    case 3:
	                        Vector strafeVector = player.getLocation().getDirection().crossProduct(new Vector(0, 1.2, 0)); 
	                        strafeVector = strafeVector.multiply(random.nextBoolean() ? 1.2 : -1.2); 
	                        player.setVelocity(strafeVector); 
	                        break;
	                    case 4:
	                        if (player.isOnGround()) {
	                            player.setVelocity(new Vector(0, -0.4, 0)); 
	                        }
	                        break;  
	                    default:
	                        break;
	                }

	                player.playSound(player.getLocation(), madnessSound, 1.0f, 1.0f);
	            }
	        }.runTaskTimer(Main.getPlugin(Main.class), 0L, 7L); 
	    }
}