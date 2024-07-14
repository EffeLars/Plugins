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
package me.rok.magicalwands.nature.spells;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.Validate;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import me.lars.game.Main;
import me.lars.game.utils.ParticleColor;

public class NatureHealListener implements Listener {
	private static final double CIRCLE_RADIUS = 1.5;
	private static final int PARTICLE_COUNT = 50;
	private static final long PARTICLE_COOLDOWN = 1000L;
	private final static Set<Player> cooldownPlayers = new HashSet<Player>();
	private final static Set<Player> disabledPlayers = new HashSet<Player>();
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

	public static void drawLine(Location point1, Location point2, double space) {
		World world = point1.getWorld();
		Validate.isTrue(point2.getWorld().equals(world), "Lines cannot be in different worlds!");
		double distance = point1.distance(point2);
		Vector p1 = point1.toVector();
		Vector p2 = point2.toVector();
		Vector vector = p2.clone().subtract(p1).normalize().multiply(space);
		double length = 0;
		for (; length < distance; p1.add(vector)) {
			world.spawnParticle(Particle.REDSTONE, p1.getX(), p1.getY(), p1.getZ(), 1);
			length += space;
		}
	}

    public static void checkAndHealPlayer(Player player) {
        if (disabledPlayers.contains(player)) {
            return;
        }
        if (player.getScoreboardTags().contains("NatureHealEnabled")) {
            int radius = 6;
            if (cooldownPlayers.contains(player)) {
                return;
            }
            int x2 = -radius;
            while (x2 <= radius) {
                int y2 = -radius;
                while (y2 <= radius) {
                    int z2 = -radius;
                    while (z2 <= radius) {
                        Block block = player.getLocation().add((double) x2, (double) y2, (double) z2).getBlock();
                        if (FLOWERS.contains(block.getType())) {
                            if (player.getHealth() != player.getMaxHealth()) {
                                PotionEffect regenEffect = new PotionEffect(PotionEffectType.REGENERATION, 20, 3);
                                player.addPotionEffect(regenEffect);
                                drawLineWithHearts(block.getLocation(), player.getLocation(), 1);
                                Location location = player.getLocation();
                                player.getWorld().spawnParticle(Particle.HEART, player.getLocation().add(0.5, 0.5, 0.5), 1, 0.2, 0.2, 0.2, 0.0);
                                player.getWorld().spawnParticle(Particle.SPELL_INSTANT, block.getLocation().add(0.5, 0.5, 0.5), 7, 0.2, 0.2, 0.2, 0.0);
                                player.getWorld().spawnParticle(Particle.HEART, block.getLocation().add(0.5, 0.5, 0.5), 1, 0.2, 0.2, 0.2, 0.0);
                                player.getWorld().spawnParticle(Particle.REDSTONE, location, 1, 1.0, 1.0, 1.0, 0.03, ParticleColor.LIGHT_GREEN);
                                player.getWorld().spawnParticle(Particle.REDSTONE, location, 1, 1.0, 1.0, 1.0, 0.03, ParticleColor.LIME_GREEN);
                                player.getWorld().spawnParticle(Particle.REDSTONE, location, 1, 1.0, 1.0, 1.0, 0.03, ParticleColor.DARK_GREEN);
                                player.getWorld().spawnParticle(Particle.SPELL_INSTANT, block.getLocation().add(0.5, 0.5, 0.5), 10, 0.2, 0.2, 0.2, 0.0);
                                player.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, block.getLocation().add(0.5, 0.5, 0.5), 2, 0.2, 0.2, 0.2, 0.0);
                            }
                            Location location = block.getLocation();
                            player.getWorld().spawnParticle(Particle.REDSTONE, location, 1, 1.0, 1.0, 1.0, 0.03, ParticleColor.LIGHT_GREEN);
                            player.getWorld().spawnParticle(Particle.REDSTONE, location, 1, 1.0, 1.0, 1.0, 0.03, ParticleColor.LIME_GREEN);
                            player.getWorld().spawnParticle(Particle.REDSTONE, location, 1, 1.0, 1.0, 1.0, 0.03, ParticleColor.DARK_GREEN);
                            player.getWorld().spawnParticle(Particle.SPELL_INSTANT, block.getLocation().add(0.5, 0.5, 0.5), 10, 0.2, 0.2, 0.2, 0.0);
                            player.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, block.getLocation().add(0.5, 0.5, 0.5), 2, 0.2, 0.2, 0.2, 0.0);
                            cooldownPlayers.add(player);
                            new BukkitRunnable() {
                                public void run() {
                                    cooldownPlayers.remove(player);
                                }
                            }.runTaskLater(Main.getPlugin(Main.class), 20L);
                        }
                        ++z2;
                    }
                    ++y2;
                }
                ++x2;
            }
        }
    }

    private static void drawLineWithHearts(Location point1, Location point2, double space) {
        World world = point1.getWorld();
        Validate.isTrue(point2.getWorld().equals(world), "Lines cannot be in different worlds!");
        double distance = point1.distance(point2);
        Vector p1 = point1.toVector();
        Vector p2 = point2.toVector();
        Vector vector = p2.clone().subtract(p1).normalize().multiply(space);
        double length = 0;
        for (; length < distance; p1.add(vector)) {
            world.spawnParticle(Particle.ENCHANTMENT_TABLE, p1.getX(), p1.getY(), p1.getZ(), 10, 0.2, 0.2, 0.2, 0.03);
            Particle.DustOptions lightGreenColor = new Particle.DustOptions(Color.fromRGB(144, 238, 144), 1.0f);
            Particle.DustOptions darkGreenColor = new Particle.DustOptions(Color.fromRGB(0, 100, 0), 1.0f);
            Particle.DustOptions limeGreenColor = new Particle.DustOptions(Color.fromRGB(50, 205, 50), 1.0f);
            world.spawnParticle(Particle.REDSTONE, p1.getX(), p1.getY(), p1.getZ(), 1, 0.1, 0.1, 0.1, 0.03, lightGreenColor);
            world.spawnParticle(Particle.REDSTONE, p1.getX(), p1.getY(), p1.getZ(), 1, 0.1, 0.1, 0.1, 0.03, darkGreenColor);
            world.spawnParticle(Particle.REDSTONE, p1.getX(), p1.getY(), p1.getZ(), 1, 0.1, 0.1, 0.1, 0.03, limeGreenColor);
            length += space;
        }
    }
}