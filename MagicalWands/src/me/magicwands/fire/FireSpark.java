package me.magicwands.fire;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import me.magicwands.events.FlameWand;
import me.magicwands.main.Main;
import me.magicwands.utils.ChatUtil;
import me.magicwands.utils.CooldownManager;
import me.magicwands.utils.GetTargetEntity;
import me.magicwands.utils.SpawnFirework;

public class FireSpark implements Listener {
    private CooldownManager cooldownManager;
    private long cooldownTimeInSeconds;
    	
    public FireSpark() {
        this.cooldownTimeInSeconds = Main.getPlugin(Main.class).getConfig().getLong("spells.FireSpark.cooldown", 10L);
        this.cooldownManager = new CooldownManager(cooldownTimeInSeconds);
        this.cooldownManager.setDefaultCooldownIfNotExist(cooldownTimeInSeconds, "FireSpark");
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (player.getItemInHand().getItemMeta() != null && 
        	    player.getItemInHand().getItemMeta().getDisplayName() != null && 
        	    player.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(me.magicwands.wands.FireWand.IgnatiusWand.getItemMeta().getDisplayName()) && 
        	    event.getAction() == Action.LEFT_CLICK_AIR && 
        	    FlameWand.Spell.getOrDefault(player, 0) == 4) { 
        	boolean isStaff = player.hasPermission("magicwands.fire.firespark.bypasscooldown") || player.hasPermission("magicwands.fire.*");
            this.SparkPlayer(player, isStaff);
        }
    }

    private void SparkPlayer(final Player player, final boolean isStaff) {
        long remainingTime;
        long cooldownDuration = isStaff ? 0L : this.cooldownManager.getCooldownDuration(player, "FireSpark");

        if (this.cooldownManager.hasCooldown(player, "FireSpark") && (remainingTime = this.cooldownManager.getCooldownRemaining(player, "FireSpark")) > 0L) {
            String color = Main.getPlugin(Main.class).getConfig().getString("wands.fire.color");
            player.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes((char) '&',
                    (ChatUtil.color("&7[&" + color + "MagicWands&7] &fThis spell is still on a &" + color + "cooldown&c&l! &fYou have to wait &" + color +
                            + remainingTime + " &fseconds"))));
            return;
        }

        this.cooldownManager.startCooldown(player, cooldownDuration, "FireSpark");

        Entity targetEntity = GetTargetEntity.getTargetEntity(player);

        if (targetEntity != null) {
            Color color = Color.ORANGE;
            Color color2 = Color.YELLOW;
            FireworkEffect.Type type = FireworkEffect.Type.BURST;
            int power = 1;
            long sparkDamage = Main.getPlugin(Main.class).getConfig().getInt("spells.firespark.damage");

            SpawnFirework.spawnFirework(targetEntity.getLocation(), color, type, power);
            SpawnFirework.spawnFirework(targetEntity.getLocation(), color, type, power);
            targetEntity.getLocation().getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, targetEntity.getLocation(), 150, 3.0, 2.0, 1.0, 0.8);
            targetEntity.getLocation().getWorld().spawnParticle(Particle.FLAME, targetEntity.getLocation(), 50, 1.0, 1.0, 1.0, 0.03);
            targetEntity.getLocation().getWorld().spawnParticle(Particle.SMOKE_NORMAL, targetEntity.getLocation(), 20, 1.0, 2.0, 1.0, 0.03);
            targetEntity.getLocation().getWorld().spawnParticle(Particle.SMOKE_LARGE, targetEntity.getLocation(), 40, 1.0, 2.0, 1.0, 0.03);
            targetEntity.setFireTicks(500);

        } else {
            Block block = player.getTargetBlockExact(100);

            if (block != null) {
                Location spawnLocation = block.getLocation().add(0, 1, 0);
                Color color = Color.ORANGE;
                FireworkEffect.Type type = FireworkEffect.Type.BURST;
                int power = 1;

                SpawnFirework.spawnFirework(spawnLocation, color, type, power);
                SpawnFirework.spawnFirework(spawnLocation, color, type, power);
                spawnLocation.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, spawnLocation, 150, 3.0, 2.0, 0.0, 0.8);
                spawnLocation.getWorld().spawnParticle(Particle.FLAME, spawnLocation, 50, 0.5, 0.5, 0.0, 0.03);
                spawnLocation.getWorld().spawnParticle(Particle.SMOKE_NORMAL, spawnLocation, 20, 1.0, 1.0, 1.0, 0.03);
                spawnLocation.getWorld().spawnParticle(Particle.SMOKE_LARGE, spawnLocation, 40, 1.0, 1.0, 1.0, 0.03);

            } else {
                Location spawnLocation = player.getEyeLocation().add(player.getEyeLocation().getDirection().multiply(25));
                Color color = Color.ORANGE;
                FireworkEffect.Type type = FireworkEffect.Type.BURST;
                int power = 1;

                SpawnFirework.spawnFirework(spawnLocation, color, type, power);
                SpawnFirework.spawnFirework(spawnLocation, color, type, power);
                spawnLocation.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, spawnLocation, 150, 3.0, 2.0, 0.0, 0.8);
                spawnLocation.getWorld().spawnParticle(Particle.FLAME, spawnLocation, 50, 0.5, 0.5, 0.0, 0.03);
                spawnLocation.getWorld().spawnParticle(Particle.SMOKE_NORMAL, spawnLocation, 20, 1.0, 1.0, 1.0, 0.03);
                spawnLocation.getWorld().spawnParticle(Particle.SMOKE_LARGE, spawnLocation, 40, 1.0, 1.0, 1.0, 0.03);
            }
        }
    }
}

