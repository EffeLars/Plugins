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

public class IceWand extends ItemStack {
    static Material ice = Material.valueOf(Main.getPlugin(Main.class).getConfig().getString("wands.ice.item").toUpperCase());
    public static ItemStack IceWand = new ItemStack(ice, 1);

    public IceWand() {
        String Icename = Main.getPlugin(Main.class).getConfig().getString("wands.ice.customname");
        ItemMeta meta = IceWand.getItemMeta();
        ArrayList<String> IceWandLore = new ArrayList<>();
        meta.setDisplayName(ChatUtil.color(Icename));
        meta.setLore(IceWandLore);
        IceWand.setItemMeta(meta);
    }
}
