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

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import me.lars.game.Main;

public class NatureWandEffect
extends BukkitRunnable {
    private Player player;
    private Location wandLocation;
    private int iterations;
    private int taskId;

    public NatureWandEffect(Player player) {
        this.player = player;
        this.wandLocation = player.getLocation().add(0.0, 1.0, 0.0);
        this.iterations = 5;
    	player.playSound(wandLocation, Sound.BLOCK_LEVER_CLICK, 1.0F, 1.0F);
        player.playSound(wandLocation, Sound.BLOCK_CROP_BREAK, 1.0F, 1.0F);
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

        Particle.DustOptions greenColor = new Particle.DustOptions(Color.GREEN, 1.0f);
        Particle.DustOptions yellowColor = new Particle.DustOptions(Color.YELLOW, 1.0f);
        Particle.DustOptions whiteColor = new Particle.DustOptions(Color.WHITE, 1.0f);


        this.wandLocation.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, this.wandLocation, 5, 0.5, 0.5, 0.5, 0.1); // Sparkles
        this.wandLocation.getWorld().spawnParticle(Particle.TOTEM, this.wandLocation, 5, 0.0, 0.0, 0.0, 0.1); // Magical essence
        

        for (int i = 0; i < 10; i++) {
            double offsetX = Math.random() * 2 - 1;
            double offsetY = Math.random() * 0.5 + 1; 
            double offsetZ = Math.random() * 2 - 1;
            this.wandLocation.getWorld().spawnParticle(Particle.REDSTONE, this.wandLocation.getX() + offsetX, this.wandLocation.getY() + 0.8, this.wandLocation.getZ() + offsetZ, 1, 0, 0, 0, greenColor);
        }


        for (int i = 0; i < 8; i++) {
            double radius = 0.5;
            double angle = Math.PI / 4 * i;
            double offsetX = radius * Math.cos(angle);
            double offsetZ = radius * Math.sin(angle);
            this.wandLocation.getWorld().spawnParticle(Particle.REDSTONE, this.wandLocation.getX() + offsetX, this.wandLocation.getY() + 0.8, this.wandLocation.getZ() + offsetZ, 1, 0, 0, 0, yellowColor);
        }

        this.wandLocation.getWorld().spawnParticle(Particle.REDSTONE, this.wandLocation, 5, 0.5, 0.5, 0.5, 0.1, yellowColor);
        this.wandLocation.getWorld().spawnParticle(Particle.REDSTONE, this.wandLocation, 5, 0.5, 0.5, 0.5, 0.1, greenColor);
        this.wandLocation.getWorld().spawnParticle(Particle.REDSTONE, this.wandLocation, 5, 0.5, 0.5, 0.5, 0.1, whiteColor);
    }


}

