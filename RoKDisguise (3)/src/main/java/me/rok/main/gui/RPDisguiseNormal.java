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
import me.rok.main.characters.normal.Aurelia;
import me.rok.main.characters.normal.Azariel;
import me.rok.main.characters.normal.Gwendolyn;
import me.rok.main.characters.normal.Valerian;
import me.rok.main.characters.normal.Xandriel;
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

public class RPDisguiseNormal
implements Listener {
    public static Inventory Menu = Bukkit.createInventory(null, (int)27, (String)"Normal Characters");

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
        ItemStack aureliaItem = RPDisguiseNormal.createCustomItem(Material.GOLDEN_SWORD, ChatColor.RED + "Aurelia", " ", "Custom lore for Aurelia");
        ItemStack azarielItem = RPDisguiseNormal.createCustomItem(Material.BOW, ChatColor.RED + "Azariel", " ", "Custom lore for Azariel");
        ItemStack gwendolynItem = RPDisguiseNormal.createCustomItem(Material.DIAMOND_CHESTPLATE, ChatColor.RED + "Gwendolyn", " ", "Custom lore for Gwendolyn");
        ItemStack valerianItem = RPDisguiseNormal.createCustomItem(Material.IRON_AXE, ChatColor.RED + "Valerian", " ", "Custom lore for Valerian");
        ItemStack xandrielItem = RPDisguiseNormal.createCustomItem(Material.BLAZE_ROD, ChatColor.RED + "Xandriel", " ", "Custom lore for Xandriel");
        ItemStack undisguiseItem = RPDisguiseNormal.createCustomItem(Material.RED_DYE, ChatColor.RED + "Undisguise", " ", "Click here to undisguise as your character.");
        ItemStack errorItem = RPDisguiseNormal.createCustomItem(Material.GREEN_STAINED_GLASS_PANE, ChatColor.RED + "ERROR404", " ", "Unknown Error :)");
        ItemStack backButton = RPDisguiseNormal.createCustomItem(Material.ARROW, ChatColor.RED + "Go Back", " ", "Go back to the previous menu");
        int i2 = 0;
        while (i2 < inv.getSize()) {
            if (i2 != 10 && i2 != 12 && i2 != 14 && i2 != 16 && i2 != 20) {
                inv.setItem(i2, errorItem);
            }
            ++i2;
        }
        inv.setItem(10, aureliaItem);
        inv.setItem(12, azarielItem);
        inv.setItem(14, gwendolynItem);
        inv.setItem(16, valerianItem);
        inv.setItem(20, xandrielItem);
        if (player.getScoreboardTags().contains("Disguised")) {
            inv.setItem(26, undisguiseItem);
        } else {
            inv.setItem(26, backButton);
        }
        player.openInventory(inv);
    }

    @EventHandler
    public void MainMenu2(InventoryClickEvent e2) {
        HumanEntity p2 = e2.getWhoClicked();
        if (e2.getView().getTitle().equals("Normal Characters")) {
            if (p2.getScoreboardTags().contains("Disguised")) {
                p2.sendMessage(ChatUtil.color("&4&lRoK &cYou cannot open this menu while disguised as a character, Please undisguise first."));
                e2.setCancelled(true);
                return;
            }
            if (e2.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.RED + "Aurelia")) {
                p2.sendMessage(ChatUtil.color("&4&lRoK &cAurelia"));
                e2.setCancelled(true);
                Aurelia.AureliaDisguise((Player)p2);
                Aurelia.addDisguiseBossBar((Player)p2);
                return;
            }
            if (e2.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.RED + "Azariel")) {
                e2.setCancelled(true);
                p2.sendMessage(ChatUtil.color("&4&lRoK &cAzariel"));
                Azariel.AzarielDisguise((Player)p2);
                Azariel.addDisguiseBossBar((Player)p2);
            }
            if (e2.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.RED + "Gwendolyn")) {
                e2.setCancelled(true);
                p2.sendMessage(ChatUtil.color("&4&lRoK &cGwendolyn"));
                Gwendolyn.GwendolynDisguise((Player)p2);
                Gwendolyn.addDisguiseBossBar((Player)p2);
            }
            if (e2.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.RED + "Valerian")) {
                e2.setCancelled(true);
                p2.sendMessage(ChatUtil.color("&4&lRoK &cValerian"));
                Valerian.ValerianDisguise((Player)p2);
                Valerian.addDisguiseBossBar((Player)p2);
            }
            if (e2.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.RED + "Xandriel")) {
                e2.setCancelled(true);
                p2.sendMessage(ChatUtil.color("&4&lRoK &cXandriel"));
                Xandriel.XandrielDisguise((Player)p2);
                Xandriel.addDisguiseBossBar((Player)p2);
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

