package me.magicwands.utils;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import me.magicwands.main.Main;

public class WandEffects extends BukkitRunnable {

    public enum EffectType {
        DARK,
        EMPIRE,
        FLAME,
        ICE,
        LIGHT,
        NATURE,
        WITCH
    }

    private Player player;
    private Location wandLocation;
    private int iterations;
    private int taskId;
    private EffectType effectType;
    private long startTime;
    private Random random = new Random();

    public WandEffects(Player player, EffectType effectType) {
        this.player = player;
        this.effectType = effectType;
        this.wandLocation = player.getLocation().add(0.0, 1.0, 0.0);
        this.iterations = effectType == EffectType.LIGHT ? 30 : 5;
        this.startTime = System.currentTimeMillis();
    }

    public void start() {
        long delay = 0L;
        long period = effectType == EffectType.LIGHT ? 5L : 2L;
        this.taskId = this.runTaskTimer((Plugin) Main.getPlugin(Main.class), delay, period).getTaskId();
    }

    @Override
    public void run() {
        if (effectType == EffectType.LIGHT) {
            double elapsedTime = (System.currentTimeMillis() - startTime) / 1000.0;
            if (elapsedTime >= 3.0) {
                Bukkit.getScheduler().cancelTask(this.taskId);
                return;
            }
        }

        if (this.iterations > 0) {
            spawnParticles();
            --this.iterations;
        } else if (effectType != EffectType.LIGHT) {
            Bukkit.getScheduler().cancelTask(this.taskId);
        }
    }

    private void spawnParticles() {
        switch (effectType) {
            case DARK -> spawnDarkParticles();
            case EMPIRE -> spawnEmpireParticles();
            case FLAME -> spawnFlameParticles();
            case ICE -> spawnIceParticles();
            case LIGHT -> spawnLightParticles();
            case NATURE -> spawnNatureParticles();
            case WITCH -> spawnWitchParticles();
        }
    }

    private void spawnDarkParticles() {
        wandLocation.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, wandLocation, 50, 0.5, 0.5, 0.5, 0.03);
        wandLocation.getWorld().spawnParticle(Particle.WHITE_ASH, wandLocation, 50, 0.5, 0.5, 0.5, 0.03);
        wandLocation.getWorld().spawnParticle(Particle.FLAME, wandLocation, 10, 0.5, 0.5, 0.5, 0.03);
        wandLocation.getWorld().spawnParticle(Particle.SMOKE_NORMAL, wandLocation, 10, 0.5, 0.5, 0.5, 0.03);
    }

    private void spawnEmpireParticles() {
        Particle.DustOptions purple = new Particle.DustOptions(Color.fromRGB(75, 0, 130), 1.5f);
        for (int i = 0; i < 15; i++) {
            double offsetX = Math.random() * 0.8 - 0.4;
            double offsetY = Math.random() * 0.8 - 0.4;
            double offsetZ = Math.random() * 0.8 - 0.4;
            wandLocation.getWorld().spawnParticle(Particle.REDSTONE, wandLocation.clone().add(offsetX, offsetY, offsetZ), 1, purple);
        }

        wandLocation.getWorld().spawnParticle(Particle.SMOKE_NORMAL, wandLocation, 10, 0.5, 0.5, 0.5, 0.05);
        wandLocation.getWorld().spawnParticle(Particle.SPELL_WITCH, wandLocation, 15, 0.7, 0.7, 0.7, 0.02);
        wandLocation.getWorld().spawnParticle(Particle.PORTAL, wandLocation, 40, 1.0, 1.0, 1.0, 0.03);

        for (int i = 0; i < 5; i++) {
            double angle = Math.toRadians(i * 72);
            double x = Math.cos(angle) * 1.2;
            double z = Math.sin(angle) * 1.2;
            wandLocation.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, wandLocation.clone().add(x, 0, z), 3, 0.1, 0.1, 0.1, 0.02);
        }
    }

    private void spawnFlameParticles() {
        var orange = new Particle.DustOptions(Color.ORANGE, 1.0f);
        var yellow = new Particle.DustOptions(Color.YELLOW, 1.0f);

        player.getWorld().spawnParticle(Particle.REDSTONE, wandLocation, 5, orange);
        player.getWorld().spawnParticle(Particle.REDSTONE, wandLocation, 5, yellow);
        wandLocation.getWorld().spawnParticle(Particle.FLAME, wandLocation, 20, 0, 0, 0, 0.03);
        wandLocation.getWorld().spawnParticle(Particle.LAVA, wandLocation, 1, 0, 0, 0, 0.01);
        wandLocation.getWorld().spawnParticle(Particle.ASH, wandLocation, 5, 0, 0, 0, 0.03);
        wandLocation.getWorld().spawnParticle(Particle.SMOKE_NORMAL, wandLocation, 5, 0, 0, 0, 0.02);
    }

    private void spawnIceParticles() {
        var iceColor = new Particle.DustOptions(Color.fromRGB(170, 255, 255), 1.0f);
        wandLocation.getWorld().spawnParticle(Particle.REDSTONE, wandLocation, 5, 1, 1, 1, 0.03, iceColor);
        wandLocation.getWorld().spawnParticle(Particle.BLOCK_CRACK, wandLocation, 20, 0, 0, 0, 0.03, Material.ICE.createBlockData());
        wandLocation.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, wandLocation, 35, 1, 1, 1, 0.01);

        for (int i = 0; i < 10; i++) {
            double x = Math.random() * 0.6 - 0.3;
            double y = Math.random() * 0.6 - 0.3;
            double z = Math.random() * 0.6 - 0.3;
            wandLocation.getWorld().spawnParticle(Particle.REDSTONE, wandLocation.clone().add(x, y, z), 1, iceColor);
        }
    }

    private void spawnLightParticles() {
        double radius = 1.0;
        double height = 1.0;
        double angle = random.nextDouble() * 2 * Math.PI;
        double x = radius * Math.cos(angle);
        double y = random.nextDouble() * height;
        double z = radius * Math.sin(angle);
        Location loc = wandLocation.clone().add(x, y, z);
        int count = random.nextInt(20) + 50;
        Particle.DustOptions white = new Particle.DustOptions(Color.WHITE, 1.0f);

        wandLocation.getWorld().spawnParticle(Particle.REDSTONE, loc, count, 0, 0, 0, white);
        wandLocation.getWorld().spawnParticle(Particle.SPELL_INSTANT, wandLocation, 2, 0.5, 0.5, 0.5, 0.03);
        wandLocation.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, wandLocation, 50, 0.5, 0.5, 0.5, 0.03);
        wandLocation.getWorld().spawnParticle(Particle.WHITE_ASH, wandLocation, 3, 0.5, 0.5, 0.5, 0.03);
    }

    private void spawnNatureParticles() {
        var green = new Particle.DustOptions(Color.GREEN, 1.0f);
        var yellow = new Particle.DustOptions(Color.YELLOW, 1.0f);
        var white = new Particle.DustOptions(Color.WHITE, 1.0f);

        wandLocation.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, wandLocation, 5, 0.5, 0.5, 0.5, 0.1);
        wandLocation.getWorld().spawnParticle(Particle.TOTEM, wandLocation, 5, 0, 0, 0, 0.1);

        for (int i = 0; i < 10; i++) {
            double x = Math.random() * 2 - 1;
            double y = Math.random() * 0.5 + 1;
            double z = Math.random() * 2 - 1;
            wandLocation.getWorld().spawnParticle(Particle.REDSTONE, wandLocation.clone().add(x, y, z), 1, green);
        }

        for (int i = 0; i < 8; i++) {
            double angle = Math.PI / 4 * i;
            double x = 0.5 * Math.cos(angle);
            double z = 0.5 * Math.sin(angle);
            wandLocation.getWorld().spawnParticle(Particle.REDSTONE, wandLocation.clone().add(x, 0.8, z), 1, yellow);
        }

        wandLocation.getWorld().spawnParticle(Particle.REDSTONE, wandLocation, 5, 0.5, 0.5, 0.5, 0.1, yellow);
        wandLocation.getWorld().spawnParticle(Particle.REDSTONE, wandLocation, 5, 0.5, 0.5, 0.5, 0.1, green);
        wandLocation.getWorld().spawnParticle(Particle.REDSTONE, wandLocation, 5, 0.5, 0.5, 0.5, 0.1, white);
    }

    private void spawnWitchParticles() {
        var purple = new Particle.DustOptions(Color.fromRGB(153, 50, 204), 1.0f);
        var lightPurple = new Particle.DustOptions(Color.fromRGB(221, 160, 221), 1.0f); 

        wandLocation.getWorld().spawnParticle(Particle.REDSTONE, wandLocation, 35, 1, 1, 1, 0.03, purple);
        wandLocation.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, wandLocation, 35, 1, 1, 1, 0.01);
        wandLocation.getWorld().spawnParticle(Particle.SPELL_WITCH, wandLocation, 35, 1, 1, 1, 0.01);

        for (int i = 0; i < 10; i++) {
            double x = Math.random() * 0.6 - 0.3;
            double y = Math.random() * 0.6 - 0.3;
            double z = Math.random() * 0.6 - 0.3;
            wandLocation.getWorld().spawnParticle(Particle.REDSTONE, wandLocation.clone().add(x, y, z), 1, lightPurple);
        }
    }
}
