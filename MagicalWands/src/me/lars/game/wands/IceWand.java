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

public class IceWand
extends ItemStack {
	static Material ice = Material.valueOf(Main.getPlugin(Main.class).getConfig().getString("wands.ice.item").toUpperCase());;
    public static ItemStack IceWand = new ItemStack(ice, 1);
    public static ItemStack NyxaraWand = new ItemStack(Material.IRON_HOE, 1);
    public static ItemStack MortiferumWand = new ItemStack(Material.POTION, 1);
    public static ItemStack HaldirionWand = new ItemStack(Material.POPPY, 1);

    public IceWand() {
    	String Icename = Main.getPlugin(Main.class).getConfig().getString("wands.ice.customname");
        ItemMeta meta = IceWand.getItemMeta();
        ArrayList<String> IceWandLore = new ArrayList<String>();
        meta.setDisplayName(ChatUtil.color(Icename));
        meta.setLore(IceWandLore);
        IceWand.setItemMeta(meta);
        meta = NyxaraWand.getItemMeta();
    }
}

