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
package me.magicwands.wands;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.magicwands.main.Main;
import me.magicwands.utils.ChatUtil;

public class NatureWand
extends ItemStack {
	static Material nature = Material.valueOf(Main.getPlugin(Main.class).getConfig().getString("wands.nature.item").toUpperCase());;
    public static ItemStack NatureWand = new ItemStack(nature, 1);
    public static ItemStack NyxaraWand = new ItemStack(Material.IRON_HOE, 1);
    public static ItemStack MortiferumWand = new ItemStack(Material.POTION, 1);
    public static ItemStack HaldirionWand = new ItemStack(Material.POPPY, 1);

    public NatureWand() {
    	String naturename = Main.getPlugin(Main.class).getConfig().getString("wands.nature.customname");
        ItemMeta meta = NatureWand.getItemMeta();
        ArrayList<String> IceWandLore = new ArrayList<String>();
        meta.setDisplayName(ChatUtil.color(naturename));
        meta.setLore(IceWandLore);
        NatureWand.setItemMeta(meta);
        meta = NatureWand.getItemMeta();
    }
}

