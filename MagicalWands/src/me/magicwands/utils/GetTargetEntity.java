package me.magicwands.utils;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.RayTraceResult;

public class GetTargetEntity {

	public static Entity getTargetEntity(Player player) {
	    RayTraceResult rayTraceResult = player.getWorld().rayTraceEntities(
	            player.getEyeLocation(),
	            player.getEyeLocation().getDirection(),
	            100, 
	            1,   
	            entity -> entity instanceof LivingEntity && entity != player 
	    );

	
	    if (rayTraceResult != null && rayTraceResult.getHitEntity() != null) {
	        return rayTraceResult.getHitEntity();
	    }

	    return null;
	}
}