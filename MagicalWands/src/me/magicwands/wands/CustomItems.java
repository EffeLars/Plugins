package me.magicwands.wands;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.magicwands.main.Main;
import me.magicwands.utils.ChatUtil;

public class CustomItems
extends ItemStack {


    static Material HornOfValere = Material.valueOf(Main.getPlugin(Main.class).getConfig().getString("items.horn.item").toUpperCase());;
	String HornOfValereName = Main.getPlugin(Main.class).getConfig().getString("items.horn.customname");
    
	public static ItemStack HornOfValereItem = new ItemStack(HornOfValere, 1);
	
    public CustomItems() {
        ItemMeta meta = HornOfValereItem.getItemMeta();
        ArrayList<String> HornLore = new ArrayList<String>();
        HornLore.add(ChatUtil.color("&7"));
        HornLore.add(ChatUtil.color("&eMagicWand Item"));
        meta.setLore(HornLore);
        meta.setDisplayName(ChatUtil.color(HornOfValereName));
        meta.addEnchant(Enchantment.ARROW_FIRE, 2, true);
        meta.addEnchant(Enchantment.ARROW_DAMAGE, 6, true);
        meta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
        meta.addEnchant(Enchantment.DURABILITY, 5, true);
        meta.setUnbreakable(true);
        HornOfValereItem.setItemMeta(meta);
        
        
    }
}

