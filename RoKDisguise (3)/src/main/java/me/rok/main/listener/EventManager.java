/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.PlayerPickupItemEvent
 *  org.bukkit.inventory.ItemStack
 */
package me.rok.main.listener;

import java.util.ArrayList;
import me.rok.main.util.ChatUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

public class EventManager
implements Listener,
CommandExecutor {
    @EventHandler
    public void onItemPickup(PlayerPickupItemEvent event) {
        ItemStack item = event.getItem().getItemStack();
        ArrayList<String> higherEnchantments = new ArrayList<String>();
        for (Enchantment enchantment : item.getEnchantments().keySet()) {
            int level = (Integer)item.getEnchantments().get(enchantment);
            if (level <= enchantment.getMaxLevel()) continue;
            String enchantmentName = enchantment.getKey().getKey();
            higherEnchantments.add(String.valueOf(enchantmentName) + " " + level);
            item.addUnsafeEnchantment(enchantment, enchantment.getMaxLevel());
        }
        if (!higherEnchantments.isEmpty()) {
            String message = ChatUtil.color("&4&lITEM MODIFIER! &cYou picked up an item with special powers, Unfortunatly only people with Devine power can handle this, So the power has been reduced      &c&lADMIN NOTE: &cThe enchantments that have been changed - ");
            message = String.valueOf(message) + String.join((CharSequence)", ", higherEnchantments);
            event.getPlayer().sendMessage(message);
        }
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }
        Player player = (Player)sender;
        player.sendMessage("Flower particles disabled.");
        return true;
    }
}

