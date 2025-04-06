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
package me.magicwands.light;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import me.magicwands.main.Main;
import me.magicwands.utils.ChatUtil;
import me.magicwands.wands.LightWand;

public class LightExecutioner implements Listener {
    private final Map<UUID, BukkitTask> fireIceTasks = new HashMap<>();
    private final Map<UUID, Boolean> firestrikeMap = new HashMap<>();

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (player.getItemInHand().getItemMeta() != null &&
            player.getItemInHand().getItemMeta().getDisplayName() != null &&
            player.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(LightWand.LightWand.getItemMeta().getDisplayName()) &&
            event.getAction() == Action.LEFT_CLICK_AIR &&
            me.magicwands.events.LightWand.LightSpell.getOrDefault(player, 0) == 7) {

            UUID playerUUID = player.getUniqueId();
            boolean isActive = firestrikeMap.getOrDefault(playerUUID, false);
            String color = Main.getPlugin(Main.class).getConfig().getString("wands.light.color");

            if (isActive) {
                firestrikeMap.put(playerUUID, false);
                
                if (fireIceTasks.containsKey(playerUUID)) {
                    fireIceTasks.get(playerUUID).cancel();
                    fireIceTasks.remove(playerUUID);
                }

                player.sendMessage(ChatUtil.color("&7[&" + color + "Magic&7] &" + color + "Stopped the Light Executioner.."));
            } else {
                firestrikeMap.put(playerUUID, true);
                player.sendMessage(ChatUtil.color("&7[&" + color + "Magic&7] &" + color + "Started the Light Executioner.."));

                BukkitTask task = new BukkitRunnable() {
                    @Override
                    public void run() {
                        Block targetBlock = player.getTargetBlockExact(50);
                        if (targetBlock == null) return; 

                        castDivineJudgement(player, 10, 2);
                        for (Entity entity : player.getLocation().getWorld().getNearbyEntities(targetBlock.getLocation(), 4, 4, 4)) {
                            if (!(entity instanceof Player) || entity == player) continue;

                            Player nearbyPlayer = (Player) entity;
                            nearbyPlayer.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 100, 2));
                            Vector direction = nearbyPlayer.getLocation().getDirection().normalize().multiply(-1);
                            nearbyPlayer.setVelocity(direction.multiply(0.2));
                            nearbyPlayer.damage(30);
                            nearbyPlayer.playSound(nearbyPlayer.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1, 1);
                            nearbyPlayer.playSound(nearbyPlayer.getLocation(), Sound.ENTITY_WITHER_AMBIENT, 1, 1);
                        }
                    }
                }.runTaskTimer(Main.getPlugin(Main.class), 0L, 20L);

                fireIceTasks.put(playerUUID, task);
            }
        }
    }



    @EventHandler
    public void RemoveAura(PlayerJoinEvent event) {
        firestrikeMap.remove(event.getPlayer());
    }
    
    @EventHandler
    public void RemoveSpell(PlayerQuitEvent event) {
        firestrikeMap.remove(event.getPlayer());
        if (fireIceTasks.containsKey(event.getPlayer().getUniqueId())) {
            fireIceTasks.get(event.getPlayer().getUniqueId()).cancel();
            fireIceTasks.remove(event.getPlayer().getUniqueId());
        }
    }

    public void castDivineJudgement(Player caster, int radius, int damage) {
        Block targetBlock = caster.getTargetBlockExact(50);
        if (targetBlock == null) {
           
            return;
        } else {
        Location center = targetBlock.getLocation().add(0.5, 1, 0.5);
        World world = center.getWorld();

        for (double y = 0; y < 6; y += 0.5) {
            Location particleLoc = center.clone().add(0, y, 0);
            world.spawnParticle(Particle.END_ROD, particleLoc, 10, 0.5, 0, 0.5, 0.1);
            world.spawnParticle(Particle.CLOUD, particleLoc, 10, 0.5, 0, 0.5, 0.1);
            world.spawnParticle(Particle.ENCHANTMENT_TABLE, particleLoc, 10, 0.5, 0, 0.5, 0.1);
            world.spawnParticle(Particle.FLAME, particleLoc, 5, 0.3, 0, 0.3, 0.05);
            world.spawnParticle(Particle.LAVA, particleLoc, 2, 0.2, 0, 0.2, 0.02);
        }

        world.playSound(center, Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 2.0f, 1.2f);
        world.strikeLightningEffect(center);
        world.strikeLightningEffect(center);
        world.strikeLightningEffect(center);
        for (Entity entity : world.getNearbyEntities(center, radius, radius, radius)) {
            if (entity instanceof LivingEntity && entity != caster) {
                LivingEntity target = (LivingEntity) entity;
                target.damage(damage, caster);
                target.setFireTicks(100);
                world.spawnParticle(Particle.SPELL_INSTANT, target.getLocation().add(0, 1, 0), 20, 0.5, 0.5, 0.5, 0.1);
            }
        }
        }
    }
}
