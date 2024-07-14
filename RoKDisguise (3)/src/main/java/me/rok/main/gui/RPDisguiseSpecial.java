/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  net.md_5.bungee.api.ChatColor
 *  org.bukkit.Bukkit
 *  org.bukkit.Material
 *  org.bukkit.entity.HumanEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.inventory.InventoryClickEvent
 *  org.bukkit.event.player.PlayerQuitEvent
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 */
package me.rok.main.gui;

import java.util.Arrays;
import me.rok.main.characters.special.Eldarion;
import me.rok.main.characters.special.Haldirion;
import me.rok.main.characters.special.Ignatius;
import me.rok.main.characters.special.Mortiferum;
import me.rok.main.characters.special.Nyxara;
import me.rok.main.gui.RPDisguiseGUI;
import me.rok.main.util.ChatUtil;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class RPDisguiseSpecial
implements Listener {
    public static Inventory Menu = Bukkit.createInventory(null, (int)27, (String)"Special Characters");

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

    public static void openNormalInventory(Inventory inv, Player player) {
        ItemStack haldirionItem = RPDisguiseSpecial.createCustomItem(Material.GOLDEN_SWORD, ChatColor.RED + "Haldirion", " ", "Custom lore for Haldirion");
        ItemStack ignatiusItem = RPDisguiseSpecial.createCustomItem(Material.BOW, ChatColor.RED + "Ignatius", " ", "Custom lore for Ignatius");
        ItemStack eldarionItem = RPDisguiseSpecial.createCustomItem(Material.DIAMOND_CHESTPLATE, ChatColor.RED + "Eldarion", " ", "Custom lore for Eldarion");
        ItemStack mortiferumItem = RPDisguiseSpecial.createCustomItem(Material.IRON_AXE, ChatColor.RED + "Mortiferum", " ", "Custom lore for Mortiferum");
        ItemStack nyxaraItem = RPDisguiseSpecial.createCustomItem(Material.BLAZE_ROD, ChatColor.RED + "Nyxara", " ", "Custom lore for Nyxara");
        ItemStack Undisguise2 = RPDisguiseSpecial.createCustomItem(Material.RED_DYE, ChatColor.RED + "Undisguise", " ", "Click here undisguise as your character.");
        ItemStack Error = RPDisguiseSpecial.createCustomItem(Material.GREEN_STAINED_GLASS_PANE, ChatColor.RED + "ERROR404", " ", "Unknown Error :)");
        ItemStack Back = RPDisguiseSpecial.createCustomItem(Material.ARROW, ChatColor.RED + "Go Back", " ", "Go Back to the Previous Menu");
        int i2 = 0;
        while (i2 < inv.getSize()) {
            if (i2 != 10 && i2 != 12 && i2 != 14 && i2 != 16 && i2 != 20) {
                inv.setItem(i2, Error);
            }
            ++i2;
        }
        inv.setItem(10, haldirionItem);
        inv.setItem(12, ignatiusItem);
        inv.setItem(14, eldarionItem);
        inv.setItem(16, mortiferumItem);
        inv.setItem(20, nyxaraItem);
        if (player.getScoreboardTags().contains("Disguised")) {
            inv.setItem(26, Undisguise2);
        } else {
            inv.setItem(26, Back);
        }
        player.openInventory(inv);
    }

    @EventHandler
    public void MainMenu2(InventoryClickEvent e2) {
        HumanEntity p2 = e2.getWhoClicked();
        if (e2.getView().getTitle().equals("Special Characters")) {
            if (p2.getScoreboardTags().contains("Disguised")) {
                p2.sendMessage(ChatUtil.color("&4&lRoK &cYou cannot open this menu while disguised as an character, Please undisguise first."));
                e2.setCancelled(true);
                return;
            }
            if (e2.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.RED + "Haldirion")) {
                p2.sendMessage(ChatUtil.color("&4&lRoK &cHaldirion"));
                e2.setCancelled(true);
                Haldirion.HaldirionDisguise((Player)p2);
                Haldirion.addDisguiseBossBar((Player)p2);
                return;
            }
            if (e2.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.RED + "Ignatius")) {
                e2.setCancelled(true);
                p2.sendMessage(ChatUtil.color("&4&lRoK &cIgnatius"));
                Ignatius.IgnatiusDisguise((Player)p2);
                Ignatius.addDisguiseBossBar((Player)p2);
            }
            if (e2.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.RED + "Nyxara")) {
                e2.setCancelled(true);
                p2.sendMessage(ChatUtil.color("&4&lRoK &cNyxara"));
                Nyxara.NyxaraDisguise((Player)p2);
                Nyxara.addDisguiseBossBar((Player)p2);
            }
            if (e2.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.RED + "Mortiferum")) {
                e2.setCancelled(true);
                p2.sendMessage(ChatUtil.color("&4&lRoK &cMortiferum"));
                Mortiferum.MortiferumDisguise((Player)p2);
                Mortiferum.addDisguiseBossBar((Player)p2);
            }
            if (e2.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.RED + "Eldarion")) {
                e2.setCancelled(true);
                p2.sendMessage(ChatUtil.color("&4&lRoK &cEldarion"));
                Eldarion.EldarionDisguise((Player)p2);
                Eldarion.addDisguiseBossBar((Player)p2);
            }
            if (e2.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.RED + "Undisguise") && p2.getScoreboardTags().contains("Disguised")) {
                p2.sendMessage(ChatUtil.color("&4&lRoK &cUndisguising you.."));
                e2.setCancelled(true);
                String kickReason = "You are currently being undisguised...";
                ((Player)p2).kickPlayer(kickReason);
                return;
            }
            if (e2.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.RED + "Go Back")) {
                e2.setCancelled(true);
                RPDisguiseGUI.openInventory(RPDisguiseGUI.Menu, (Player)p2);
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

