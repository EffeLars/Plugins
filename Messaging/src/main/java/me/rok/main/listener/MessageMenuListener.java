/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.AsyncPlayerChatEvent
 */
package me.rok.main.listener;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class MessageMenuListener
implements Listener {
    private Map<Player, String> recipientMap = new HashMap<Player, String>();

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();
        if (this.recipientMap.containsKey(player)) {
            event.setCancelled(true);
            if (message.equalsIgnoreCase("cancel")) {
                player.sendMessage(ChatColor.RED + "Letter creation canceled.");
                this.recipientMap.remove(player);
                return;
            }
            String recipientName = this.recipientMap.get(player);
            Player recipient = Bukkit.getPlayerExact((String)recipientName);
            if (recipient != null) {
                player.sendMessage(ChatColor.GREEN + "Letter sent successfully to " + recipientName + "!");
            } else {
                player.sendMessage(ChatColor.RED + "Player not found or not online.");
            }
            this.recipientMap.remove(player);
        }
    }

    private int getCurrentPage(String title) {
        return 0;
    }

    private void displayMessageMenu(Player player, int page) {
    }
}

