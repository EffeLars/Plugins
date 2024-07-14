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

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.lars.game.Main;
import me.lars.game.utils.ChatUtil;

public class FlowerWand
extends ItemStack {
	static Material flower = Material.valueOf(Main.getPlugin(Main.class).getConfig().getString("wands.flower.item").toUpperCase());;
    public static ItemStack FlowerWand = new ItemStack(flower, 1);

    public FlowerWand() {
    	String Flowername = Main.getPlugin(Main.class).getConfig().getString("wands.flower.customname");
        ItemMeta meta = FlowerWand.getItemMeta();
        ArrayList<String> IceWandLore = new ArrayList<String>();
        meta.setDisplayName(ChatUtil.color(Flowername));
        meta.setLore(IceWandLore);
        FlowerWand.setItemMeta(meta);
    }
}

