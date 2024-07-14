/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.PlayerMoveEvent
 */
package me.rok.main.listener.Haldirion;

import me.rok.main.characters.special.Haldirion;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class HaldirionSpeed
implements Listener {
    @EventHandler
    public void onPlayerMove2(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Haldirion.updateDisguiseBossBar(player);
        byte lightLevel = player.getLocation().getBlock().getLightLevel();
        if (player.getName().equalsIgnoreCase("Haldirion")) {
            double walkSpeed = this.calculateWalkSpeed(lightLevel);
            player.setWalkSpeed((float)walkSpeed);
        }
    }

    private double calculateWalkSpeed(int lightLevel) {
        int minLightLevel = 0;
        int maxLightLevel = 15;
        double minWalkSpeed = 0.09;
        double maxWalkSpeed = 0.3;
        double walkSpeedRange = maxWalkSpeed - minWalkSpeed;
        double lightLevelRange = maxLightLevel - minLightLevel;
        double walkSpeedIncrement = walkSpeedRange / lightLevelRange;
        double walkSpeed = minWalkSpeed + (double)(lightLevel - minLightLevel) * walkSpeedIncrement;
        walkSpeed = Math.max(minWalkSpeed, Math.min(maxWalkSpeed, walkSpeed));
        return walkSpeed;
    }
}

