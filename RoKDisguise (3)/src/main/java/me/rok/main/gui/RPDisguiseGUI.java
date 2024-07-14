/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.GameProfile
 *  com.mojang.authlib.properties.Property
 *  net.md_5.bungee.api.ChatColor
 *  org.bukkit.Bukkit
 *  org.bukkit.Material
 *  org.bukkit.entity.HumanEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.inventory.InventoryClickEvent
 *  org.bukkit.event.player.PlayerJoinEvent
 *  org.bukkit.event.player.PlayerQuitEvent
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.inventory.meta.SkullMeta
 */
package me.rok.main.gui;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.UUID;
import me.rok.main.gui.RPDisguiseNormal;
import me.rok.main.gui.RPDisguiseSpecial;
import me.rok.main.util.ChatUtil;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class RPDisguiseGUI
implements Listener {
    public static Inventory Menu = Bukkit.createInventory(null, (int)27, (String)"Roleplay Disguise Menu");

    public static ItemStack createCustomItem(Material material, String itemName, String ... lore) {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(itemName);
        if (lore.length > 0) {
            itemMeta.setLore(Arrays.asList(lore));
        }
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static ItemStack createSkullItem(String skinSignature) {
        ItemStack skullItem = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta skullMeta = (SkullMeta)skullItem.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        profile.getProperties().put((Object)"textures", (Object)new Property("textures", skinSignature));
        try {
            Field profileField = skullMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(skullMeta, profile);
        }
        catch (IllegalAccessException | NoSuchFieldException e2) {
            e2.printStackTrace();
        }
        skullItem.setItemMeta((ItemMeta)skullMeta);
        return skullItem;
    }

    public static void openInventory(Inventory inv, Player player) {
        ItemStack NormalChar = RPDisguiseGUI.createCustomItem(Material.GREEN_DYE, ChatColor.RED + "Normal Characters", " ", "Click here to open the Normal Characters Menu");
        ItemStack SpecialChar = RPDisguiseGUI.createCustomItem(Material.BARRIER, ChatColor.RED + "Special Characters", " ", "Click here to open the Special Characters Menu");
        ItemStack Undisguise2 = RPDisguiseGUI.createCustomItem(Material.RED_DYE, ChatColor.RED + "Undisguise", " ", "Click here undisguise as your character.");
        ItemStack Error = RPDisguiseGUI.createCustomItem(Material.BLACK_STAINED_GLASS_PANE, ChatColor.RED + "ERROR404", " ", "Unknown Error :)");
        ItemStack Test = RPDisguiseGUI.createSkullItem("Tvn4exxEkyoDZ/9tYODoRMs5goSJLXYK/ewUPT4dmC9kyQQX6Y1jQ85laWObzYyHmBLjOQjqjaBpD/DtPVUC6PcuuCmmTb6a6/lFrMIGhCC0ifDVHmqzPKRqcRr8vq5r4YqU//rm8x7rLJSdPrbPRjkREdmZr7krcETBSUJlTX/hSDkX2vm8kJGaU3USSQGAskfJQrx7u1ewznTEXpW8aHpsjV3pnpGrwf8S2ycgGTd8ZXpfYlr9d1/mzZdu8xHdCHcKxA4ulPULteINtCBz1xjqgdlziSmqh0nH9AjkXMC+81paVwk22n0cG9FGB6z+TeO4re+o5bclg1/h5Zs3ues5usFvIGZe1sVd0b1PZh1jq8FIFK4y+kts+8a3W+bdqBqDoEPJj17/oQDyICs2modQUZN0iB4CgUB9qy4CTTKJNrGa0Yf6mBGp5eobVZdvexbb382djTCq4ZIHuI3t9wY1ZYT47MZw8MyeY14qtgzPkSv2p6v5X74VCvrb1Z1c28rve1RXQ9pH1ZMi5+jpwAfzeUOH8oGC0pFYFLkxW3N28YpDVr6K7/1tnodoFxn3RSVgtwEyHw7yOxsvB5Ikp55gdo8UMkVU2ndiW9wIbTG6jlYOm3w7OzTXdIHkgUtJ0AtswLya72fm1IKPBTRD+2Cy8obz2aKo3yh7d2uxWLk=");
        int i2 = 0;
        while (i2 < inv.getSize()) {
            if (i2 != 10 && i2 != 16) {
                inv.setItem(i2, Error);
            }
            ++i2;
        }
        inv.setItem(10, NormalChar);
        inv.setItem(16, SpecialChar);
        inv.setItem(26, Undisguise2);
        player.openInventory(inv);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e2) {
        Player player = e2.getPlayer();
        RPDisguiseGUI.openInventory(Menu, player);
    }

    @EventHandler
    public void MainMenu2(InventoryClickEvent e2) {
        HumanEntity p2 = e2.getWhoClicked();
        if (e2.getView().getTitle().equals("Roleplay Disguise Menu")) {
            if (e2.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.RED + "Normal Characters")) {
                p2.sendMessage(ChatUtil.color("&4&lRoK &cOpening Normal Character Menu"));
                e2.setCancelled(true);
                RPDisguiseNormal.openNormalInventory(RPDisguiseNormal.Menu, (Player)p2);
                return;
            }
            if (e2.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.RED + "Undisguise")) {
                p2.sendMessage(ChatUtil.color("&4&lRoK &cUndisguising you.."));
                e2.setCancelled(true);
                String kickReason = "You are currently being undisguised...";
                ((Player)p2).kickPlayer(kickReason);
                return;
            }
            if (e2.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.RED + "Special Characters")) {
                e2.setCancelled(true);
                RPDisguiseGUI.openInventory(RPDisguiseSpecial.Menu, (Player)p2);
                p2.sendMessage(ChatUtil.color("&4&lRoK &cOpening Special Character Menu"));
                RPDisguiseSpecial.openNormalInventory(RPDisguiseSpecial.Menu, (Player)p2);
            }
            if (e2.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.RED + "ERROR404")) {
                e2.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e2) {
        Player player = e2.getPlayer();
    }
}

