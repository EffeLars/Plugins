package me.magicwands.wands;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.magicwands.main.Main;
import me.magicwands.utils.ChatUtil;

public class LightWand
extends ItemStack implements Listener {
	static Material light = Material.valueOf(Main.getPlugin(Main.class).getConfig().getString("wands.light.item").toUpperCase());;
	
    public static ItemStack LightWand = new ItemStack(light, 1);
    public static ItemStack NyxaraWand = new ItemStack(Material.IRON_HOE, 1);
    public static ItemStack MortiferumWand = new ItemStack(Material.POTION, 1);
    public static ItemStack HaldirionWand = new ItemStack(Material.POPPY, 1);

    public LightWand() {
    	String Icename = Main.getPlugin(Main.class).getConfig().getString("wands.light.customname");
        ItemMeta meta = LightWand.getItemMeta();
        ArrayList<String> LightWandLore = new ArrayList<String>();
        meta.setDisplayName(ChatUtil.color(Icename));
        meta.setLore(LightWandLore);
        LightWand.setItemMeta(meta);
        meta = LightWand.getItemMeta();
    }
}

