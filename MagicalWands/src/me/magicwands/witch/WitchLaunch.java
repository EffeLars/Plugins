package me.magicwands.witch;

import org.bukkit.Color;
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

import me.magicwands.main.Main;
import me.magicwands.utils.ChatUtil;
import me.magicwands.utils.CooldownManager;
import me.magicwands.utils.GetTargetEntity;

public class WitchLaunch implements Listener {
    private CooldownManager cooldownManager;
    private long cooldownTimeInSeconds;

    public WitchLaunch() {
        this.cooldownTimeInSeconds = Main.getPlugin(Main.class).getConfig().getLong("spells.WitchLaunch.cooldown", 10L);
        this.cooldownManager = new CooldownManager(cooldownTimeInSeconds);
        this.cooldownManager.setDefaultCooldownIfNotExist(cooldownTimeInSeconds, "WitchLaunch");
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (player.getItemInHand() != null && player.getItemInHand().getItemMeta() != null) {
            String itemDisplayName = player.getItemInHand().getItemMeta().getDisplayName();

            if (itemDisplayName != null 
                    && itemDisplayName.equalsIgnoreCase(me.magicwands.wands.WitchWand.WitchWand.getItemMeta().getDisplayName()) 
                    && event.getAction() == Action.LEFT_CLICK_AIR) {

                int spell = me.magicwands.events.WitchWand.WitchSpell.getOrDefault(player, 0);

                if (spell == 0) {
                    String color = Main.getPlugin(Main.class).getConfig().getString("wands.witch.color");
                    player.sendMessage(ChatUtil.color("&7[&" + color + "MagicWands&7] &fYou do not have a spell selected."));
                } else if (spell == 2) {
                    boolean isStaff = player.hasPermission("magicwands.witch.witchlaunch.bypasscooldown") || player.hasPermission("magicwands.witch.*");
                    this.launchWitchSpell(player, isStaff);
                }
            }}
    }

    private void launchWitchSpell(final Player player, final boolean isStaff) {
        long remainingTime;
        long cooldownDuration = isStaff ? 0L : this.cooldownManager.getCooldownDuration(player, "WitchLaunch");

        if (this.cooldownManager.hasCooldown(player, "WitchLaunch") && (remainingTime = this.cooldownManager.getCooldownRemaining(player, "WitchLaunch")) > 0L) {
            String color = Main.getPlugin(Main.class).getConfig().getString("wands.witch.color");
            player.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes((char) '&',
                    (ChatUtil.color("&7[&" + color + "MagicWands&7] &fThis spell is still on a &" + color + "cooldown&c&l! &fYou have to wait &" + color +
                            remainingTime + " &fseconds"))));
            return;
        }

        this.cooldownManager.startCooldown(player, cooldownDuration, "WitchLaunch");

        Entity targetEntity = GetTargetEntity.getTargetEntity(player);

        if (targetEntity != null) {
            Particle.DustOptions iceShardColor = new Particle.DustOptions(Color.fromRGB(138, 43, 226), 1.0f);
            Particle.DustOptions cyanColor = new Particle.DustOptions(Color.PURPLE, 1.0f);      
            targetEntity.getLocation().getWorld().spawnParticle(Particle.SPELL_WITCH, targetEntity.getLocation(), 150, 3.0, 2.0, 1.0, 0.8);
            targetEntity.getLocation().getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, targetEntity.getLocation(), 50, 1.0, 1.0, 1.0, 0.03);
            targetEntity.getLocation().getWorld().spawnParticle(Particle.SMOKE_LARGE, targetEntity.getLocation(), 20, 1.0, 2.0, 1.0, 0.03);
            targetEntity.getWorld().spawnParticle(Particle.REDSTONE, targetEntity.getLocation(), 8, 0.4, 0.4, 0.4, 0.03, iceShardColor);
            targetEntity.getWorld().spawnParticle(Particle.REDSTONE, targetEntity.getLocation(), 8, 0.4, 0.4, 0.4, 0.03, cyanColor);
            targetEntity.setVelocity(new Vector(0, 1.8, 0));
            targetEntity.getWorld().playSound(targetEntity.getLocation(), Sound.BLOCK_SLIME_BLOCK_HIT, 1, 1);
            targetEntity.getWorld().playSound(targetEntity.getLocation(), Sound.ITEM_TRIDENT_THROW, 1, 1);
            targetEntity.getWorld().playSound(targetEntity.getLocation(), Sound.ENTITY_WITCH_AMBIENT, 1, 1);

        } else {
            Block block = player.getTargetBlockExact(100);

            if (block != null) {
                Location spawnLocation = block.getLocation().add(0, 1, 0);
                Particle.DustOptions iceShardColor = new Particle.DustOptions(Color.fromRGB(138, 43, 226), 1.0f);
                Particle.DustOptions cyanColor = new Particle.DustOptions(Color.PURPLE, 1.0f);         
                spawnLocation.getWorld().spawnParticle(Particle.SPELL_WITCH, spawnLocation, 150, 3.0, 2.0, 1.0, 0.8);
                spawnLocation.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, spawnLocation, 50, 1.0, 1.0, 1.0, 0.03);
                spawnLocation.getWorld().spawnParticle(Particle.SMOKE_LARGE, spawnLocation, 20, 1.0, 2.0, 1.0, 0.03);
                spawnLocation.getWorld().spawnParticle(Particle.REDSTONE, spawnLocation, 8, 0.4, 0.4, 0.4, 0.03, iceShardColor);
                spawnLocation.getWorld().spawnParticle(Particle.REDSTONE, spawnLocation, 8, 0.4, 0.4, 0.4, 0.03, cyanColor);
                spawnLocation.getWorld().playSound(spawnLocation, Sound.BLOCK_SLIME_BLOCK_HIT, 1, 1);
                spawnLocation.getWorld().playSound(spawnLocation, Sound.ITEM_TRIDENT_THROW, 1, 1);
                spawnLocation.getWorld().playSound(spawnLocation, Sound.ENTITY_WITCH_AMBIENT, 1, 1);

            } else {
                Location spawnLocation = player.getEyeLocation().add(player.getEyeLocation().getDirection().multiply(25));               
                Particle.DustOptions iceShardColor = new Particle.DustOptions(Color.fromRGB(138, 43, 226), 1.0f);
                Particle.DustOptions cyanColor = new Particle.DustOptions(Color.PURPLE, 1.0f);           
                spawnLocation.getWorld().spawnParticle(Particle.SPELL_WITCH, spawnLocation, 150, 3.0, 2.0, 1.0, 0.8);
                spawnLocation.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, spawnLocation, 50, 1.0, 1.0, 1.0, 0.03);
                spawnLocation.getWorld().spawnParticle(Particle.SMOKE_LARGE, spawnLocation, 20, 1.0, 2.0, 1.0, 0.03);
                spawnLocation.getWorld().spawnParticle(Particle.REDSTONE, spawnLocation, 8, 0.4, 0.4, 0.4, 0.03, iceShardColor);
                spawnLocation.getWorld().spawnParticle(Particle.REDSTONE, spawnLocation, 8, 0.4, 0.4, 0.4, 0.03, cyanColor);
                spawnLocation.getWorld().playSound(spawnLocation, Sound.BLOCK_SLIME_BLOCK_HIT, 1, 1);
                spawnLocation.getWorld().playSound(spawnLocation, Sound.ITEM_TRIDENT_THROW, 1, 1);
                spawnLocation.getWorld().playSound(spawnLocation, Sound.ENTITY_WITCH_AMBIENT, 1, 1);
            }
        }
    }
}
