/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Color
 *  org.bukkit.Location
 *  org.bukkit.Particle
 *  org.bukkit.Particle$DustOptions
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 */
package me.lars.game.event;

import java.util.Random;
import me.lars.game.Main;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class LightWandEffect
extends BukkitRunnable {
    private static final double MAX_DURATION = 3.0;
    private static final double PARTICLE_INTERVAL = 0.1;
    private Player player;
    private Location wandLocation;
    private int iterations;
    private int taskId;
    private long startTime;
    private Random random;

    public LightWandEffect(Player player) {
        this.player = player;
        this.wandLocation = player.getLocation().add(0.0, 1.0, 0.0);
        this.iterations = 30;
        this.startTime = System.currentTimeMillis();
        this.random = new Random();
    }

    public void start() {
        this.taskId = this.runTaskTimer((Plugin)Main.getPlugin(Main.class), 0L, 5L).getTaskId();
    }

    public void run() {
        long currentTime = System.currentTimeMillis();
        double elapsedTime = (double)(currentTime - this.startTime) / 1000.0;
        if (elapsedTime >= 3.0) {
            Bukkit.getScheduler().cancelTask(this.taskId);
            return;
        }
        if (this.iterations > 0) {
            this.spawnParticles();
            --this.iterations;
        }
    }

    private void spawnParticles() {
        double radius = 1.0;
        double height = 1.0;
        double angle = this.random.nextDouble() * 2.0 * Math.PI;
        double x = radius * Math.cos(angle);
        double y = this.random.nextDouble() * height;
        double z = radius * Math.sin(angle);
        Location particleLocation = this.wandLocation.clone().add(x, y, z);
        int particleCount = this.random.nextInt(20) + 50;
        Particle.DustOptions dustOptions = new Particle.DustOptions(Color.WHITE, 1.0f);
        this.wandLocation.getWorld().spawnParticle(Particle.REDSTONE, particleLocation, particleCount, 0.0, 0.0, 0.0, (Object)dustOptions);
        this.wandLocation.getWorld().spawnParticle(Particle.SPELL_INSTANT, this.wandLocation, 2, 0.5, 0.5, 0.5, 0.03);
        this.wandLocation.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, this.wandLocation, 50, 0.5, 0.5, 0.5, 0.03);
        this.wandLocation.getWorld().spawnParticle(Particle.WHITE_ASH, this.wandLocation, 3, 0.5, 0.5, 0.5, 0.03);
    }
}

