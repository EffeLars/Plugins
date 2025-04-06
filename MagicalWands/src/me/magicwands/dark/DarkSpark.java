package me.magicwands.dark;

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
import me.magicwands.wands.DarkWand;

public class DarkSpark implements Listener {
    private CooldownManager cooldownManager;
    private long cooldownTimeInSeconds;
    
    public DarkSpark() {
        this.cooldownTimeInSeconds = Main.getPlugin(Main.class).getConfig().getLong("spells.DarkSpark.cooldown", 10L);
        this.cooldownManager = new CooldownManager(cooldownTimeInSeconds);
        this.cooldownManager.setDefaultCooldownIfNotExist(cooldownTimeInSeconds, "DarkSpark");
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (player.getItemInHand() != null && player.getItemInHand().getItemMeta() != null) {
            String itemDisplayName = player.getItemInHand().getItemMeta().getDisplayName();
            if (player.getItemInHand().getItemMeta().getDisplayName() != null &&
                player.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(DarkWand.DarkWand.getItemMeta().getDisplayName()) &&
                event.getAction() == Action.LEFT_CLICK_AIR && me.magicwands.events.DarkWand.DarkSpell.get(player) == 8) {
                boolean isStaff = player.hasPermission("magicwands.dark.darkspark.bypasscooldown") || player.hasPermission("magicwands.dark.*");
                this.SparkPlayer(player, isStaff);
            }
        }
    }

    private void SparkPlayer(final Player player, final boolean isStaff) {
        long remainingTime;
        long cooldownDuration = isStaff ? 0L : this.cooldownManager.getCooldownDuration(player, "DarkSpark");

        if (this.cooldownManager.hasCooldown(player, "DarkSpark") && (remainingTime = this.cooldownManager.getCooldownRemaining(player, "DarkSpark")) > 0L) {
            String color = Main.getPlugin(Main.class).getConfig().getString("wands.dark.color");
            player.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes((char) '&',
                    (ChatUtil.color("&7[&" + color + "MagicWands&7] &fThis spell is still on a &" + color + "cooldown&c&l! &fYou have to wait &" + color +
                            + remainingTime + " &fseconds"))));
            return;
        }

        this.cooldownManager.startCooldown(player, cooldownDuration, "DarkSpark");

        Entity targetEntity = GetTargetEntity.getTargetEntity(player);

        if (targetEntity != null) {
            Color color = Color.BLACK;
            Color color2 = Color.GRAY;
            FireworkEffect.Type type = FireworkEffect.Type.BALL;
            int power = 1;
            long sparkDamage = Main.getPlugin(Main.class).getConfig().getInt("spells.darkspark.damage");

            SpawnFirework.spawnFirework(targetEntity.getLocation(), color, type, power);
            SpawnFirework.spawnFirework(targetEntity.getLocation(), color, type, power);
            targetEntity.getLocation().getWorld().spawnParticle(Particle.SMOKE_LARGE, targetEntity.getLocation(), 150, 1.0, 1.0, 1.0, 0.01);
            targetEntity.getLocation().getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, targetEntity.getLocation(), 50, 1.0, 1.0, 1.0, 0.03);
            targetEntity.getLocation().getWorld().spawnParticle(Particle.ASH, targetEntity.getLocation(), 20, 1.0, 2.0, 1.0, 0.03);
            ((LivingEntity) targetEntity).addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 100, 2));

        } else {
            Block block = player.getTargetBlockExact(100);

            if (block != null) {
                Location spawnLocation = block.getLocation().add(0, 1, 0);
                Color color = Color.BLACK;
                FireworkEffect.Type type = FireworkEffect.Type.BALL;
                int power = 1;

                SpawnFirework.spawnFirework(spawnLocation, color, type, power);
                SpawnFirework.spawnFirework(spawnLocation, color, type, power);
                spawnLocation.getWorld().spawnParticle(Particle.SMOKE_LARGE, spawnLocation, 150, 1.0, 1.0, 1.0, 0.01);
                spawnLocation.getWorld().spawnParticle(Particle.ASH, spawnLocation, 50, 0.5, 0.5, 0.0, 0.03);
                spawnLocation.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, spawnLocation, 50, 1.0, 1.0, 1.0, 0.03);

            } else {
                Location spawnLocation = player.getEyeLocation().add(player.getEyeLocation().getDirection().multiply(25));
                Color color = Color.BLACK;
                FireworkEffect.Type type = FireworkEffect.Type.BALL;
                int power = 1;

                SpawnFirework.spawnFirework(spawnLocation, color, type, power);
                SpawnFirework.spawnFirework(spawnLocation, color, type, power);
                spawnLocation.getWorld().spawnParticle(Particle.SMOKE_LARGE, spawnLocation, 150, 1.0, 1.0, 1.0, 0.01);
                spawnLocation.getWorld().spawnParticle(Particle.ASH, spawnLocation, 50, 0.5, 0.5, 0.0, 0.03);
                spawnLocation.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, spawnLocation, 50, 1.0, 1.0, 1.0, 0.03);
            }
        }
    }
}
