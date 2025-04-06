package me.magicwands.utils;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class DrawLine {

	public static void drawParticleLine(Player player) {
	    Block targetBlock = player.getTargetBlockExact(250);
	    if (targetBlock == null) return; 

	    Location start = player.getEyeLocation();
	    Location end = targetBlock.getLocation().add(0.5, 0.5, 0.5);
	    
	    double distance = start.distance(end);
	    Vector direction = end.toVector().subtract(start.toVector()).normalize();

	   
	    for (double i = 0; i <= distance; i += 0.5) { 
	        Location point = start.clone().add(direction.clone().multiply(i));
	        spawnParticles(point);
	    }

	 
	    Location groundCheck = end.clone();
	    World world = player.getWorld();

	    while (groundCheck.getBlock().getType() == Material.AIR && groundCheck.getY() > world.getMinHeight()) {
	        spawnParticles(groundCheck);
	        groundCheck.subtract(0, 0.5, 0); 
	    }
	}


	private static void spawnParticles(Location point) {
	    World world = point.getWorld();
	    if (world == null) return;

	    world.spawnParticle(Particle.CLOUD, point, 1, 0.9, 0.9, 0.9, 0.009);
	    world.spawnParticle(Particle.WHITE_ASH, point, 20, 0.9, 0.9, 0.9, 0.01);
	    world.spawnParticle(Particle.ENCHANTMENT_TABLE, point, 50, 0.9, 0.9, 0.9, 0.01);
	}
}
