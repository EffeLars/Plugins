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
    	player.playSound(wandLocation, Sound.BLOCK_LEVER_CLICK, 1.0F, 1.0F);
        player.playSound(wandLocation, Sound.BLOCK_LAVA_POP, 1.0F, 1.0F);
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
        this.player.getWorld().spawnParticle(Particle.REDSTONE, this.wandLocation, 5, 1.0, 1.0, 1.0, 0.03, (Object)orangeColor);
        this.player.getWorld().spawnParticle(Particle.REDSTONE, this.wandLocation, 5, 1.0, 1.0, 1.0, 0.03, (Object)yellowColor);
        double red = 0.7372549019607844;
        double green = 0.44313725490196076;
        double blue = 0.21176470588235294;
        double red2 = 0.9803921568627451;
        double green2 = 0.9450980392156862;
        double blue2 = 0.1568627450980392;
        this.player.spawnParticle(Particle.SPELL_MOB, this.player.getLocation(), 1, red, green, blue, 2.0);
        this.player.spawnParticle(Particle.SPELL_MOB, this.player.getLocation(), 1, red2, green2, blue2, 3.0);
        this.wandLocation.getWorld().spawnParticle(Particle.FLAME, this.wandLocation, 20, 0.0, 0.0, 0.0, 0.03);
        this.wandLocation.getWorld().spawnParticle(Particle.LAVA, this.wandLocation, 1, 0.0, 0.0, 0.0, 0.01);
        this.wandLocation.getWorld().spawnParticle(Particle.ASH, this.wandLocation, 5, 0.0, 0.0, 0.0, 0.03);
        this.wandLocation.getWorld().spawnParticle(Particle.SMOKE_NORMAL, this.wandLocation, 5, 0.0, 0.0, 0.0, 0.02);
    }
}

