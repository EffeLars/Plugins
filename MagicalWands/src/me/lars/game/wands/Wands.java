/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  net.md_5.bungee.api.ChatColor
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 */
package me.lars.game.wands;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.lars.game.Main;
import me.lars.game.utils.ChatUtil;

public class Wands
extends ItemStack {
	static Material fire = Material.valueOf(Main.getPlugin(Main.class).getConfig().getString("wands.fire.item").toUpperCase());;
    public static ItemStack IgnatiusWand = new ItemStack(fire, 1);
    public static ItemStack NyxaraWand = new ItemStack(Material.IRON_HOE, 1);
    public static ItemStack MortiferumWand = new ItemStack(Material.POTION, 1);
    public static ItemStack HaldirionWand = new ItemStack(Material.POPPY, 1);

    public Wands() {
    	String name = Main.getPlugin(Main.class).getConfig().getString("wands.fire.customname");
        ItemMeta meta = IgnatiusWand.getItemMeta();
        ArrayList<String> IgnatiusWandLore = new ArrayList<String>();
        meta.setDisplayName(ChatUtil.color(name));
        meta.setLore(IgnatiusWandLore);
        IgnatiusWandLore.add(net.md_5.bungee.api.ChatColor.MAGIC + "???");
        IgnatiusWandLore.add("Leap");
        IgnatiusWand.setItemMeta(meta);
        meta = NyxaraWand.getItemMeta();
        ArrayList<String> NyxaraWandLore = new ArrayList<String>();
        meta.setDisplayName(ChatColor.DARK_PURPLE + "Nyxara Wand");
        meta.setLore(NyxaraWandLore);
        NyxaraWandLore.add(ChatColor.MAGIC + "???");
        NyxaraWandLore.add("Leap");
        NyxaraWand.setItemMeta(meta);
        meta = MortiferumWand.getItemMeta();
        ArrayList<String> MortiferumWandLore = new ArrayList<String>();
        meta.setDisplayName(ChatColor.RED + "Mortiferum Wand");
        meta.setLore(MortiferumWandLore);
        MortiferumWandLore.add(ChatColor.MAGIC + "???");
        MortiferumWandLore.add("Enchantment 1");
        MortiferumWandLore.add("Enchantment 2");
        MortiferumWand.setItemMeta(meta);
        meta = HaldirionWand.getItemMeta();
        ArrayList<String> HaldirionWandLore = new ArrayList<String>();
        meta.setDisplayName(ChatColor.GREEN + "Haldirion Wand");
        meta.setLore(HaldirionWandLore);
        HaldirionWandLore.add(ChatColor.MAGIC + "???");
        HaldirionWandLore.add("Enchantment 1");
        HaldirionWandLore.add("Enchantment 2");
        HaldirionWand.setItemMeta(meta);
    }
}

