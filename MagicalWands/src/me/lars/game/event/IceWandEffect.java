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
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import me.lars.game.Main;

public class IceWandEffect
extends BukkitRunnable {
    private Player player;
    private Location wandLocation;
    private int iterations;
    private int taskId;

    public IceWandEffect(Player player) {
        this.player = player;
        this.wandLocation = player.getLocation().add(0.0, 1.0, 0.0);
        this.iterations = 5;
    	player.playSound(wandLocation, Sound.BLOCK_LEVER_CLICK, 1.0F, 1.0F);
        player.playSound(wandLocation, Sound.BLOCK_GLASS_BREAK, 1.0F, 1.0F);
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
        
        Particle.DustOptions cyanColor = new Particle.DustOptions(Color.AQUA, 1.0f);
        Particle.DustOptions blueColor = new Particle.DustOptions(Color.fromRGB(0, 0, 255), 1.0f);
       double blueRed = 0.0; 
        double blueGreen = 0.0;
        double blueBlue = 1.0;
        double cyanRed = 0.0;
        double cyanGreen = 1.0;
        double cyanBlue = 1.0;
  Particle.DustOptions iceShardColor = new Particle.DustOptions(Color.fromRGB(170, 255, 255), 1.0f);

     
        this.player.getWorld().spawnParticle(Particle.REDSTONE, this.wandLocation, 5, 1.0, 1.0, 1.0, 0.03, iceShardColor);
        this.wandLocation.getWorld().spawnParticle(Particle.BLOCK_CRACK, this.wandLocation, 20, 0.0, 0.0, 0.0, 0.03, Material.ICE.createBlockData());
        this.wandLocation.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, this.wandLocation, 35, 1.0, 1.0, 1.0, 0.01);
        
      
        for (int i = 0; i < 10; i++) {
            double offsetX = Math.random() * 0.6 - 0.3;
            double offsetY = Math.random() * 0.6 - 0.3;
            double offsetZ = Math.random() * 0.6 - 0.3;
            this.wandLocation.getWorld().spawnParticle(Particle.REDSTONE, this.wandLocation.getX() + offsetX, this.wandLocation.getY() + offsetY, this.wandLocation.getZ() + offsetZ, 1, iceShardColor);
        }
    }

}

