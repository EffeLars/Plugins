/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.Particle
 *  org.bukkit.block.Block
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.PlayerMoveEvent
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 *  org.bukkit.scheduler.BukkitRunnable
 */
package me.rok.main.listener.Haldirion;

import java.util.HashSet;
import java.util.Set;
import me.rok.main.Main;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class HaldirionHealing
implements Listener {
    private static final double CIRCLE_RADIUS = 1.5;
    private static final int PARTICLE_COUNT = 50;
    private static final long PARTICLE_COOLDOWN = 1000L;
    private final Set<Player> cooldownPlayers = new HashSet<Player>();
    private final Set<Player> disabledPlayers = new HashSet<Player>();
    private static final Set<Material> FLOWERS = new HashSet<Material>();
    static {
        FLOWERS.add(Material.DANDELION);
        FLOWERS.add(Material.POPPY);
        FLOWERS.add(Material.BLUE_ORCHID);
        FLOWERS.add(Material.ALLIUM);
        FLOWERS.add(Material.AZURE_BLUET);
        FLOWERS.add(Material.RED_TULIP);
        FLOWERS.add(Material.ORANGE_TULIP);
        FLOWERS.add(Material.WHITE_TULIP);
        FLOWERS.add(Material.PINK_TULIP);
        FLOWERS.add(Material.OXEYE_DAISY);
        FLOWERS.add(Material.CORNFLOWER);
        FLOWERS.add(Material.LILY_OF_THE_VALLEY);
        FLOWERS.add(Material.WITHER_ROSE);
    }

    @EventHandler
    public void Move(PlayerMoveEvent event) {
        final Player player = event.getPlayer();
        if (this.disabledPlayers.contains(player)) {
            return;
        }
        if (player.getName().equalsIgnoreCase("Haldirion")) {
            int radius = 6;
            if (this.cooldownPlayers.contains(player)) {
                return;
            }
            int x2 = -radius;
            while (x2 <= radius) {
                int y2 = -radius;
                while (y2 <= radius) {
                    int z2 = -radius;
                    while (z2 <= radius) {
                        Block block = player.getLocation().add((double)x2, (double)y2, (double)z2).getBlock();
                        if (FLOWERS.contains(block.getType())) {
                            if (player.getHealth() != player.getMaxHealth()) {
                                PotionEffect regenEffect = new PotionEffect(PotionEffectType.REGENERATION, 20, 3);
                                player.addPotionEffect(regenEffect);
                                player.getWorld().spawnParticle(Particle.HEART, player.getLocation().add(0.5, 0.5, 0.5), 1, 0.2, 0.2, 0.2, 0.0);
                                player.getWorld().spawnParticle(Particle.SPELL_INSTANT, block.getLocation().add(0.5, 0.5, 0.5), 7, 0.2, 0.2, 0.2, 0.0);
                                player.getWorld().spawnParticle(Particle.HEART, block.getLocation().add(0.5, 0.5, 0.5), 1, 0.2, 0.2, 0.2, 0.0);
                            }
                            player.getWorld().spawnParticle(Particle.SPELL_INSTANT, block.getLocation().add(0.5, 0.5, 0.5), 3, 0.2, 0.2, 0.2, 0.0);
                            this.cooldownPlayers.add(player);
                            new BukkitRunnable(){

                                public void run() {
                                    HaldirionHealing.this.cooldownPlayers.remove(player);
                                }
                            }.runTaskLater((Plugin)Main.getPlugin(Main.class), 20L);
                        }
                        ++z2;
                    }
                    ++y2;
                }
                ++x2;
            }
        }
    }
}

