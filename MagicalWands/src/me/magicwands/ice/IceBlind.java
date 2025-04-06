
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
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import me.magicwands.main.Main;
import me.magicwands.utils.ChatUtil;
import me.magicwands.utils.CooldownManager;
import me.magicwands.utils.GetTargetEntity;
import me.magicwands.utils.SpawnFirework;
import me.magicwands.wands.IceWand;

public class IceBlind implements Listener {
    private CooldownManager cooldownManager;
    private long cooldownTimeInSeconds;

    public IceBlind() {
        this.cooldownTimeInSeconds = Main.getPlugin(Main.class).getConfig().getLong("spells.IceBlind.cooldown", 10L);
        this.cooldownManager = new CooldownManager(cooldownTimeInSeconds);
        this.cooldownManager.setDefaultCooldownIfNotExist(cooldownTimeInSeconds, "IceBlind");
    }

    @SuppressWarnings("deprecation")
	@EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (player.getItemInHand().getItemMeta() != null && 
        	    player.getItemInHand().getItemMeta().getDisplayName() != null && 
        	    player.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(IceWand.IceWand.getItemMeta().getDisplayName()) && 
        	    event.getAction() == Action.LEFT_CLICK_AIR && 
        	    me.magicwands.events.IceWand.IceSpell.getOrDefault(player, 0) == 10) {

            if (player.hasPermission("magicwands.ice.coldblindness.bypasscooldown") || player.hasPermission("magicwands.ice.bypasscooldown")) {
                SparkPlayer(player, true);
                return;
            }

            if (this.cooldownManager.hasCooldown(player, "IceBlind")) {
                long secondsRemaining = this.cooldownManager.getCooldownRemaining(player, "IceBlind");
                String color = Main.getPlugin(Main.class).getConfig().getString("wands.ice.color");
                player.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes((char) '&',
                        (ChatUtil.color("&7[&" + color + "MagicWands&7] &fThis spell is still on a &" + color + "cooldown&c&l! &fYou have to wait &" + color +
                                secondsRemaining + " &fseconds"))));
                return;
            }

            this.cooldownManager.startCooldown(player, cooldownTimeInSeconds, "IceBlind");
            SparkPlayer(player, false);
        }
        
    }

    private void SparkPlayer(final Player player, final boolean isStaff) {
        Entity targetEntity = GetTargetEntity.getTargetEntity(player); 
        
        if (targetEntity != null) {
            Color color = Color.AQUA;
            Color color2 = Color.WHITE;
            FireworkEffect.Type type = FireworkEffect.Type.BURST;
            int power = 1;

            SpawnFirework.spawnFirework(targetEntity.getLocation(), color, type, power);
            SpawnFirework.spawnFirework(targetEntity.getLocation(), color2, type, power);
            targetEntity.getLocation().getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, targetEntity.getLocation(), 150, 3.0, 2.0, 1.0, 0.8);
            targetEntity.getLocation().getWorld().spawnParticle(Particle.CLOUD, targetEntity.getLocation(), 50, 1.0, 1.0, 1.0, 0.03);
            Particle.DustOptions iceShardColor = new Particle.DustOptions(Color.fromRGB(170, 255, 255), 1.0f);
            targetEntity.getWorld().spawnParticle(Particle.REDSTONE, targetEntity.getLocation(), 1, 1.0, 1.0, 1.0, 0.03, iceShardColor);
            if (targetEntity instanceof LivingEntity) {
                LivingEntity livingEntity = (LivingEntity) targetEntity;
                livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 2));
                livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 100, 2));
                livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 700, 2));

                new BukkitRunnable() {
                    private int count = 0;

                    public void run() {
                        ++this.count;
                        player.getWorld().spawnParticle(Particle.FLASH, livingEntity.getLocation().add(0.0, 0.0, 0.0), 20, 0.5, 2.5, 0.5, 0.01);
                        if (this.count >= 12) {
                            this.cancel();
                        }
                    }
                }.runTaskTimer((Plugin) Main.getPlugin(Main.class), 0L, 4L);
                livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 100, 0));
            }
        } else {
            Block block = player.getTargetBlockExact(100);

            if (block != null) {
                Location spawnLocation = block.getLocation().add(0, 1, 0);
                Color color = Color.WHITE;
                Color color2 = Color.AQUA;
                FireworkEffect.Type type = FireworkEffect.Type.BURST;
                int power = 1;
                new BukkitRunnable() {
                    private int count = 0;

                    public void run() {
                        ++this.count;
                        player.getWorld().spawnParticle(Particle.FLASH, spawnLocation.add(0.0, 0.0, 0.0), 2, 0.5, 2.5, 0.5, 0.01);
                        if (this.count >= 12) {
                            this.cancel();
                        }
                    }
                }.runTaskTimer((Plugin) Main.getPlugin(Main.class), 0L, 4L);

                SpawnFirework.spawnFirework(spawnLocation, color, type, power);
                SpawnFirework.spawnFirework(spawnLocation, color2, type, power);
                Particle.DustOptions iceShardColor = new Particle.DustOptions(Color.fromRGB(170, 255, 255), 1.0f);
                spawnLocation.getWorld().spawnParticle(Particle.REDSTONE, spawnLocation, 1, 1.0, 1.0, 1.0, 0.03, iceShardColor);
                spawnLocation.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, spawnLocation, 150, 3.0, 2.0, 1.0, 0.8);
                spawnLocation.getWorld().spawnParticle(Particle.CLOUD, spawnLocation, 50, 1.0, 1.0, 1.0, 0.03);
            } else {
                Location spawnLocation = player.getEyeLocation().add(player.getEyeLocation().getDirection().multiply(25));
                new BukkitRunnable() {
                    private int count = 0;

                    public void run() {
                        ++this.count;
                        player.getWorld().spawnParticle(Particle.FLASH, spawnLocation.add(0.0, 0.0, 0.0), 2, 0.5, 2.5, 0.5, 0.01);
                        if (this.count >= 12) {
                            this.cancel();
                        }
                    }
                }.runTaskTimer((Plugin) Main.getPlugin(Main.class), 0L, 4L);
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
