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

import org.bukkit.Bukkit;
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
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

import me.magicwands.main.Main;
import me.magicwands.utils.ChatUtil;
import me.magicwands.utils.CooldownManager;
import me.magicwands.wands.WandItems;

public class EmpireShockwave implements Listener {

    private CooldownManager cooldownManager;

    public EmpireShockwave() {
        long cooldownTimeInSeconds = Main.getPlugin(Main.class).getConfig().getLong("spells.EmpireShockwave.cooldown", 30L); 
        this.cooldownManager = new CooldownManager(cooldownTimeInSeconds);
        this.cooldownManager.setDefaultCooldownIfNotExist(cooldownTimeInSeconds, "EmpireShockwave");
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (player.getItemInHand().getItemMeta() != null && 
                player.getItemInHand().getItemMeta().getDisplayName() != null && 
                player.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(WandItems.EmpireWand.getItemMeta().getDisplayName()) && 
                event.getAction() == Action.LEFT_CLICK_AIR && 
                me.magicwands.events.EmpireWand.EmpireSpell.getOrDefault(player, 0) == 13) {

            if (player.hasPermission("magicwands.empire.empireshockwave.bypasscooldown")) {
                executeEmpireShockwave(player);
                return;
            }

            long cooldownTimeInMillis = cooldownManager.getCooldownDuration(player, "EmpireShockwave");
            if (this.cooldownManager.hasCooldown(player, "EmpireShockwave")) {
                long secondsRemaining = this.cooldownManager.getCooldownRemaining(player, "EmpireShockwave");
                String color = Main.getPlugin(Main.class).getConfig().getString("wands.empire.color");
                player.sendMessage(ChatUtil.color("&7[&" + color + "Magic&7] &fThis spell is still on a &" + color + "cooldown&c&l! &fYou have to wait &" + color + secondsRemaining + " &fseconds"));
                return;
            }

            this.cooldownManager.startCooldown(player, cooldownTimeInMillis, "EmpireShockwave");

            executeEmpireShockwave(player);
        }
    }

    private void executeEmpireShockwave(Player player) {
        String color = Main.getPlugin(Main.class).getConfig().getString("wands.nature.color");

        Location center = player.getLocation();
        double maxRadius = 10.0; 
        double expansionRate = 0.5; 
        int duration = 10; 

        BukkitTask natureFreezeTask = Bukkit.getScheduler().runTaskTimer(Main.getPlugin(Main.class), new Runnable() {
            double currentRadius = 1.0;

            @Override
            public void run() {
                if (currentRadius > maxRadius) {
                    this.cancel();
                    return;
                }

                SpawnEmpireShockwaveParticles(center, currentRadius);

                for (Entity entity : center.getWorld().getNearbyEntities(center, currentRadius, 2, currentRadius)) {
                    if (entity instanceof Player) {
                        Player nearbyPlayer = (Player) entity;
                        ApplyEffects(nearbyPlayer, player);
                    }
                }

                currentRadius += expansionRate;
            }

            private void cancel() {
                Bukkit.getScheduler().cancelTask(this.hashCode());
            }
        }, 0L, 2L); 
    }

    private void SpawnEmpireShockwaveParticles(Location center, double radius) {
        World world = center.getWorld();
        int points = 60; 
        world.playSound(center, Sound.ENTITY_ENDER_DRAGON_FLAP, (float) 0.3, 1);
        for (int i = 0; i < points; i++) {
            double angle = (2 * Math.PI * i) / points;
            double x = center.getX() + radius * Math.cos(angle);
            double z = center.getZ() + radius * Math.sin(angle);
            Location particleLocation = new Location(world, x, center.getY(), z);

            world.spawnParticle(Particle.PORTAL, particleLocation, 5, 0.3, 0.2, 0.3, 0.02);
            world.spawnParticle(Particle.REDSTONE, particleLocation, 2, 0.2, 0, 0.2, new Particle.DustOptions(Color.PURPLE, 1.0f));
            world.spawnParticle(Particle.REDSTONE, particleLocation, 2, 0.2, 0, 0.2, new Particle.DustOptions(Color.BLACK, 1.0f));
        }
    }

    private void ApplyEffects(Player target, Player caster) {
    	if(target.equals(caster)) {
    		return;
    	}
    	   target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 7));
    	   target.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 100, 1)); 
    	   target.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 10, 1)); 
    	   target.getWorld().playSound(target.getLocation(), Sound.ENTITY_PHANTOM_HURT, 1, 1);
       }


}