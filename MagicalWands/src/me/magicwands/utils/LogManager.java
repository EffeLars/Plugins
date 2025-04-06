package me.magicwands.utils;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

public class LogManager {

    private static final HashMap<UUID, Boolean> playerLoggingStatus = new HashMap<>();

    public static void toggleLogs(Player player) {
        if (!player.hasPermission("magicwands.command.togglelogs")) {
            player.sendMessage(ChatUtil.color("&cYou do not have permission to toggle the MagicWands logs."));
            return;
        }

        UUID playerId = player.getUniqueId();

        if (!playerLoggingStatus.containsKey(playerId)) {
            playerLoggingStatus.put(playerId, false);  // Default logging status is off
        }

        boolean currentStatus = playerLoggingStatus.get(playerId);
        boolean newStatus = !currentStatus;

        playerLoggingStatus.put(playerId, newStatus);

        if (newStatus) {
            player.sendMessage(ChatUtil.color("&7[&e&lMagic&6&lWands &6&lLogs&7] You have enabled the Magical Wands Admin logs."));
        } else {
            player.sendMessage(ChatUtil.color("&7[&e&lMagic&6&lWands &6&lLogs&7] You have disabled the Magical Wands Admin logs."));
        }
    }


    public static void sendLog(String message) {
        for (Player onlinePlayer : Bukkit.getServer().getOnlinePlayers()) {
            if (onlinePlayer.hasPermission("magicwands.command.togglelogs") && 
                playerLoggingStatus.getOrDefault(onlinePlayer.getUniqueId(), false)) {
                onlinePlayer.sendMessage(ChatUtil.color("&7[&e&lMagic&6&lWands &6&lLogs&7] &7" + message));
            }
        }
    }



    public static void onPlayerJoin(Player player) {
        UUID playerId = player.getUniqueId();

 
        if (playerLoggingStatus.getOrDefault(playerId, false)) {
            player.sendMessage(ChatUtil.color("&7[&6MagicWands Logs&7] &fLogging is still enabled. All log messages will be sent."));
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        LogManager.onPlayerJoin(player);
    }
}
