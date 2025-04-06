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

public class WitchWand
extends ItemStack {
	static Material Witch = Material.valueOf(Main.getPlugin(Main.class).getConfig().getString("wands.witch.item").toUpperCase());;
    public static ItemStack WitchWand = new ItemStack(Witch, 1);
    
    public WitchWand() {
    	String witchname = Main.getPlugin(Main.class).getConfig().getString("wands.witch.customname");
        ItemMeta meta = WitchWand.getItemMeta();
        ArrayList<String> IceWandLore = new ArrayList<String>();
        meta.setDisplayName(ChatUtil.color(witchname));
        meta.setLore(IceWandLore);
        WitchWand.setItemMeta(meta);
        meta = WitchWand.getItemMeta();
    }
}

