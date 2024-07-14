/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.World
 *  org.bukkit.block.Block
 *  org.bukkit.entity.EntityType
 *  org.bukkit.entity.Parrot
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.PlayerChatEvent
 *  org.bukkit.plugin.Plugin
 */
package me.rok.main.listener;

import java.util.Random;
import me.rok.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Parrot;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.plugin.Plugin;

public class ParrotFlyEvent
implements Listener {
    @EventHandler
    public void onPlayerChat(PlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();
        if (message.equalsIgnoreCase("12345")) {
            event.setCancelled(true);
            ParrotFlyEvent.spawnFlyingParrot(player);
        }
    }

    public static Location getRandomLocationAwayFromPlayer(Player player, double distance) {
        Location playerLocation = player.getLocation();
        World world = player.getWorld();
        Random random = new Random();
        double angle = random.nextDouble() * 2.0 * Math.PI;
        double x2 = playerLocation.getX() + distance * Math.cos(angle);
        double y2 = playerLocation.getY() + distance;
        double z2 = playerLocation.getZ() + distance * Math.sin(angle);
        return new Location(world, x2, y2, z2);
    }

    public static void spawnFlyingParrot(Player player) {
        Location playerLocation = player.getLocation();
        World world = player.getWorld();
        Location spawnLocation = playerLocation.clone().add(0.0, 1.0, 0.0);
        Parrot parrot = (Parrot)world.spawnEntity(spawnLocation, EntityType.PARROT);
        parrot.setAI(false);
        Location targetLocation = ParrotFlyEvent.getRandomLocationAwayFromPlayer(player, 40.0);
        Bukkit.getScheduler().runTaskTimer((Plugin)Main.getPlugin(Main.class), () -> {
            if (parrot.getLocation().distanceSquared(targetLocation) > 3.0) {
                Location currentLocation = parrot.getLocation();
                Location newLocation = currentLocation.add(targetLocation.toVector().subtract(currentLocation.toVector()).normalize().multiply(0.2));
                Block block = newLocation.getBlock();
                if (block.getType().isSolid() && !block.getType().equals((Object)Material.AIR)) {
                    parrot.remove();
                    return;
                }
                Block currentBlock = parrot.getLocation().getBlock();
                if (currentBlock.getType().isSolid() && !currentBlock.getType().equals((Object)Material.AIR)) {
                    parrot.remove();
                    return;
                }
                parrot.teleport(newLocation);
            } else if (!parrot.isDead()) {
                parrot.setAI(true);
                player.sendMessage("The parrot has reached its destination!");
                parrot.remove();
            }
        }, 0L, 1L);
    }
}

