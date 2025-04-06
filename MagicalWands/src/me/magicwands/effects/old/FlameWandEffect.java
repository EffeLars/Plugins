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
package me.magicwands.effects.old;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import me.magicwands.main.Main;

public class FlameWandEffect
extends BukkitRunnable {
    private Player player;
    private Location wandLocation;
    private int iterations;
    private int taskId;

    public FlameWandEffect(Player player) {
        this.player = player;
        this.wandLocation = player.getLocation().add(0.0, 1.0, 0.0);
        this.iterations = 5;
    }

    public void start() {
        this.taskId = this.runTaskTimer((Plugin)Main.getPlugin(Main.class), 0L, 2L).getTaskId();
    }

    public void run() {
        if (this.iterations > 0) {
            this.spawnParticles();
            --this.iterations;
        } else {
            Bukkit.getScheduler().cancelTask(this.taskId);
        }
    }

    private void spawnParticles() {	
        Particle.DustOptions orangeColor = new Particle.DustOptions(Color.ORANGE, 1.0f);
        Particle.DustOptions yellowColor = new Particle.DustOptions(Color.YELLOW, 1.0f);
        
        this.player.getWorld().spawnParticle(Particle.REDSTONE, this.wandLocation, 5, orangeColor);
        this.player.getWorld().spawnParticle(Particle.REDSTONE, this.wandLocation, 5, yellowColor);

        this.wandLocation.getWorld().spawnParticle(Particle.FLAME, this.wandLocation, 20, 0.0, 0.0, 0.0, 0.03);
        this.wandLocation.getWorld().spawnParticle(Particle.LAVA, this.wandLocation, 1, 0.0, 0.0, 0.0, 0.01);
        this.wandLocation.getWorld().spawnParticle(Particle.ASH, this.wandLocation, 5, 0.0, 0.0, 0.0, 0.03);
        this.wandLocation.getWorld().spawnParticle(Particle.SMOKE_NORMAL, this.wandLocation, 5, 0.0, 0.0, 0.0, 0.02);
    }
}

