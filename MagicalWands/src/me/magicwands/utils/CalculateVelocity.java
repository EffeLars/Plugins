package me.magicwands.utils;

import org.bukkit.util.Vector;

public class CalculateVelocity {

	public static Vector calculateVelocity(Vector direction, boolean isStaff) {
        double forwardPowerModifier = 1.5D;  
        double upwardPowerModifier = forwardPowerModifier * 2.0D;  
        double fwv = 2.0D; 
        double uwv = 0.7D;  

        Vector velocity = direction.normalize().multiply(fwv * forwardPowerModifier);
        velocity.setY(uwv * upwardPowerModifier);

        return velocity;
    }
	
}
