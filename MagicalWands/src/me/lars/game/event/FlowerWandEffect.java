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
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import me.lars.game.Main;
import me.lars.game.utils.ParticleColor;

public class FlowerWandEffect
extends BukkitRunnable {
    private Player player;
    private Location wandLocation;
    private int iterations;
    private int taskId;

    public FlowerWandEffect(Player player) {
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
        this.wandLocation.getWorld().spawnParticle(Particle.SPELL_WITCH, this.wandLocation, 5, 0.5, 0.5, 0.5, 0.1);
        this.wandLocation.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, this.wandLocation, 20, 0.0, 0.0, 0.0, 0.1);
        this.wandLocation.getWorld().spawnParticle(Particle.REDSTONE, this.wandLocation, 5, 0.5, 0.5, 0.5, 0.1, ParticleColor.GREEN);
        this.wandLocation.getWorld().spawnParticle(Particle.REDSTONE, this.wandLocation, 5, 0.5, 0.5, 0.5, 0.1, ParticleColor.DARK_GREEN);
        this.wandLocation.getWorld().spawnParticle(Particle.REDSTONE, this.wandLocation, 5, 0.5, 0.5, 0.5, 0.1, ParticleColor.LIME_GREEN);
    }


}

