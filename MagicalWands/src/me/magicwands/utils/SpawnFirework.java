package me.magicwands.utils;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;

public class SpawnFirework {

	 public static void spawnFirework(Location location, Color color, FireworkEffect.Type type, int power) {
	        Firework firework = location.getWorld().spawn(location, Firework.class);
	        FireworkMeta fireworkMeta = firework.getFireworkMeta();

	        FireworkEffect effect = FireworkEffect.builder()
	                .withColor(color)
	                .with(type)
	                .flicker(true)
	                .build();

	        fireworkMeta.addEffect(effect);
	        fireworkMeta.setPower(power);
	        firework.setFireworkMeta(fireworkMeta);


	        firework.detonate();
	    }
	
	
}
