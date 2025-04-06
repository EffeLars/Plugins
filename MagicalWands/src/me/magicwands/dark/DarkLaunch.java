package me.magicwands.dark;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import me.magicwands.events.DarkWand;
import me.magicwands.main.Main;
import me.magicwands.utils.ChatUtil;
import me.magicwands.utils.CooldownManager;
import me.magicwands.utils.GetTargetEntity;

public class DarkLaunch implements Listener {
    private CooldownManager cooldownManager;
    private long cooldownTimeInSeconds;

    public DarkLaunch() {
        this.cooldownTimeInSeconds = Main.getPlugin(Main.class).getConfig().getLong("spells.DarkLaunch.cooldown", 10L);
        this.cooldownManager = new CooldownManager(cooldownTimeInSeconds);
        this.cooldownManager.setDefaultCooldownIfNotExist(cooldownTimeInSeconds, "DarkLaunch");
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (player.getItemInHand() != null && player.getItemInHand().getItemMeta() != null) {
            String itemDisplayName = player.getItemInHand().getItemMeta().getDisplayName();

            if (itemDisplayName != null && itemDisplayName.equalsIgnoreCase(me.magicwands.wands.DarkWand.DarkWand.getItemMeta().getDisplayName())
                && event.getAction() == Action.LEFT_CLICK_AIR
                && DarkWand.DarkSpell.get(player) == 9) {

                boolean isStaff = player.hasPermission("magicwands.dark.darklaunch.bypasscooldown") || player.hasPermission("magicwands.dark.*");
                this.launchDarkSpell(player, isStaff);
            }
        }
    }

    private void launchDarkSpell(final Player player, final boolean isStaff) {
        long remainingTime;
        long cooldownDuration = isStaff ? 0L : this.cooldownManager.getCooldownDuration(player, "DarkLaunch");

        if (this.cooldownManager.hasCooldown(player, "DarkLaunch") && (remainingTime = this.cooldownManager.getCooldownRemaining(player, "DarkLaunch")) > 0L) {
            String color = Main.getPlugin(Main.class).getConfig().getString("wands.dark.color");
            player.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes((char) '&',
                    (ChatUtil.color("&7[&" + color + "MagicWands&7] &fThis spell is still on a &" + color + "cooldown&c&l! &fYou have to wait &" + color +
                            remainingTime + " &fseconds"))));
            return;
        }

        this.cooldownManager.startCooldown(player, cooldownDuration, "DarkLaunch");

        Entity targetEntity = GetTargetEntity.getTargetEntity(player);

        if (targetEntity != null) {
        	targetEntity.getWorld().playSound(targetEntity, Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, (float) 0.4, 1);
            targetEntity.getWorld().playSound(targetEntity, Sound.ENTITY_PHANTOM_HURT, 1, -3);
            targetEntity.getLocation().getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, targetEntity.getLocation(), 150, 3.0, 2.0, 1.0, 0.8);
            targetEntity.getLocation().getWorld().spawnParticle(Particle.SMOKE_NORMAL, targetEntity.getLocation(), 20, 1.0, 2.0, 1.0, 0.03);
            targetEntity.getLocation().getWorld().spawnParticle(Particle.SMOKE_LARGE, targetEntity.getLocation(), 40, 1.0, 2.0, 1.0, 0.03);
            targetEntity.setVelocity(new Vector(0, 1.8, 0));

        } else {
            Block block = player.getTargetBlockExact(100);

            if (block != null) {
                Location spawnLocation = block.getLocation().add(0, 1, 0);   
                spawnLocation.getWorld().playSound(spawnLocation, Sound.BLOCK_DISPENSER_LAUNCH, 1, 1);
                spawnLocation.getWorld().playSound(spawnLocation, Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, (float) 0.4, 1);
                spawnLocation.getWorld().playSound(spawnLocation, Sound.ENTITY_PHANTOM_HURT, 1, -3);
                spawnLocation.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, spawnLocation, 150, 3.0, 2.0, 0.0, 0.8);
                spawnLocation.getWorld().spawnParticle(Particle.SMOKE_NORMAL, spawnLocation, 20, 1.0, 1.0, 1.0, 0.03);
                spawnLocation.getWorld().spawnParticle(Particle.SMOKE_LARGE, spawnLocation, 40, 1.0, 1.0, 1.0, 0.03);

            } else {
            	Location playerLocation = player.getLocation();
                Location spawnLocation = player.getEyeLocation().add(player.getEyeLocation().getDirection().multiply(25));
                spawnLocation.getWorld().playSound(spawnLocation, Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, (float) 0.4, 1);
                spawnLocation.getWorld().playSound(playerLocation, Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, (float) 0.4, 1);  
                spawnLocation.getWorld().playSound(spawnLocation, Sound.ENTITY_PHANTOM_HURT, 1, -3);
                spawnLocation.getWorld().playSound(playerLocation, Sound.ENTITY_PHANTOM_HURT, 1, -3); 
                spawnLocation.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, spawnLocation, 150, 3.0, 2.0, 0.0, 0.8);
                spawnLocation.getWorld().spawnParticle(Particle.SMOKE_NORMAL, spawnLocation, 20, 1.0, 1.0, 1.0, 0.03);
                spawnLocation.getWorld().spawnParticle(Particle.SMOKE_LARGE, spawnLocation, 40, 1.0, 1.0, 1.0, 0.03);
            }
        }
    }
}
