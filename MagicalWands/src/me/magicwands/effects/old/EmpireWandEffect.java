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

public class EmpireWandEffect
extends BukkitRunnable {
    private Player player;
    private Location wandLocation;
    private int iterations;
    private int taskId;

    public EmpireWandEffect(Player player) {
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
        Particle.DustOptions EmpireDarkPurple = new Particle.DustOptions(Color.fromRGB(75, 0, 130), 1.5f);
        
        for (int i = 0; i < 15; i++) {
            double offsetX = Math.random() * 0.8 - 0.4;
            double offsetY = Math.random() * 0.8 - 0.4;
            double offsetZ = Math.random() * 0.8 - 0.4;
            
            this.wandLocation.getWorld().spawnParticle(Particle.REDSTONE, this.wandLocation.getX() + offsetX, this.wandLocation.getY() + offsetY, this.wandLocation.getZ() + offsetZ, 1, EmpireDarkPurple);
        }
        
        this.player.getWorld().spawnParticle(Particle.SMOKE_NORMAL, this.wandLocation, 10, 0.5, 0.5, 0.5, 0.05);
        this.wandLocation.getWorld().spawnParticle(Particle.SPELL_WITCH, this.wandLocation, 15, 0.7, 0.7, 0.7, 0.02);
        this.wandLocation.getWorld().spawnParticle(Particle.PORTAL, this.wandLocation, 40, 1.0, 1.0, 1.0, 0.03);
        
        for (int i = 0; i < 5; i++) {
            double angle = Math.toRadians(i * 72);
            double x = Math.cos(angle) * 1.2;
            double z = Math.sin(angle) * 1.2;
            this.wandLocation.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, this.wandLocation.getX() + x, this.wandLocation.getY(), this.wandLocation.getZ() + z, 3, 0.1, 0.1, 0.1, 0.02);
        }
    }


}

