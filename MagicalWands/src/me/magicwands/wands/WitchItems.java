/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.NamespacedKey
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.persistence.PersistentDataType
 *  org.bukkit.plugin.Plugin
 */
package me.magicwands.wands;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import me.magicwands.main.Main;

public class WitchItems
extends ItemStack {
    public static ItemStack NyxaraBow = new ItemStack(Material.BOW, 1);

    public WitchItems() {
        ItemMeta meta = NyxaraBow.getItemMeta();	
        ArrayList<String> NyxaraBowLore = new ArrayList<>();
        NyxaraBowLore.add(ChatColor.GRAY.toString());
        NyxaraBowLore.add(ChatColor.GOLD + "" + ChatColor.BOLD + "Item Description:");
        NyxaraBowLore.add(ChatColor.GRAY + "Description about this amazing");
        NyxaraBowLore.add(ChatColor.GRAY + "and powerful divine bow goes here");
        NyxaraBowLore.add(ChatColor.GRAY + "with lots of cool information.");
        NyxaraBowLore.add(ChatColor.GRAY.toString());
        NyxaraBowLore.add(ChatColor.GOLD + "" + ChatColor.BOLD + "Item Statistics:");
        NyxaraBowLore.add(ChatColor.GRAY + "Damage: " + ChatColor.RED + "+5");
        NyxaraBowLore.add(ChatColor.GRAY + "Speed: " + ChatColor.RED + "+3");
        NyxaraBowLore.add(ChatColor.GRAY + "Strength: " + ChatColor.RED + "+7");
        NyxaraBowLore.add(ChatColor.GRAY.toString());
        NyxaraBowLore.add(ChatColor.GOLD + "" + ChatColor.BOLD + "Special Ability:");
        NyxaraBowLore.add(ChatColor.DARK_GRAY + "Divine Arrow " + ChatColor.WHITE + "- " + ChatColor.GRAY + "Use this ability");
        NyxaraBowLore.add(ChatColor.GRAY + "to unleash a divine arrow that");
        NyxaraBowLore.add(ChatColor.GRAY + "causes massive damage to enemies.");
        meta.setLore(NyxaraBowLore);
        meta.setDisplayName(ChatColor.DARK_PURPLE + "Nyxara's Divine Bow");
        meta.addEnchant(Enchantment.ARROW_DAMAGE, 5, true);
        meta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
        meta.addEnchant(Enchantment.ARROW_FIRE, 3, true);
        meta.addEnchant(Enchantment.ARROW_KNOCKBACK, 1, true);
        meta.setUnbreakable(true);

        NamespacedKey itemIDKey = new NamespacedKey(Main.getPlugin(Main.class), "ItemId");
        meta.getPersistentDataContainer().set(itemIDKey, PersistentDataType.STRING, "NYXARABOW");
        NyxaraBow.setItemMeta(meta);
    }
}