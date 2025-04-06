package me.magicwands.wands;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.magicwands.main.Main;
import me.magicwands.utils.ChatUtil;

public class DarkWand extends ItemStack {
    static Material darkMaterial = Material.valueOf(Main.getPlugin(Main.class).getConfig().getString("wands.dark.item").toUpperCase());
    public static ItemStack DarkWand = new ItemStack(darkMaterial, 1);

    public DarkWand() {
        String darkName = Main.getPlugin(Main.class).getConfig().getString("wands.dark.customname");
        ItemMeta meta = DarkWand.getItemMeta();
        ArrayList<String> darkWandLore = new ArrayList<>();
        meta.setDisplayName(ChatUtil.color(darkName));
        meta.setLore(darkWandLore);
        DarkWand.setItemMeta(meta);
    }
}