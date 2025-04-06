package me.magicwands.wands;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import me.magicwands.main.Main;
import me.magicwands.utils.ChatUtil;

public class LightItems
extends ItemStack {


    static Material LightBow = Material.valueOf(Main.getPlugin(Main.class).getConfig().getString("items.lightbow.item").toUpperCase());;
	String LightBowName = Main.getPlugin(Main.class).getConfig().getString("items.lightbow.customname");
	
	static Material LightSword = Material.valueOf(Main.getPlugin(Main.class).getConfig().getString("items.lightsword.item").toUpperCase());;
	String LightSwordName = Main.getPlugin(Main.class).getConfig().getString("items.lightsword.customname");
    
	public static ItemStack HaldirionBow = new ItemStack(LightBow, 1);
	public static ItemStack HaldirionSword = new ItemStack(LightSword, 1);
    public LightItems() {
        ItemMeta meta = HaldirionBow.getItemMeta();
        ArrayList<String> HaldirionBowLore = new ArrayList<String>();
        HaldirionBowLore.add(ChatUtil.color("&7"));
        HaldirionBowLore.add(ChatUtil.color("&"));
        meta.setLore(HaldirionBowLore);
        meta.setDisplayName(ChatUtil.color(LightBowName));
        meta.addEnchant(Enchantment.ARROW_FIRE, 2, true);
        meta.addEnchant(Enchantment.ARROW_DAMAGE, 6, true);
        meta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
        meta.addEnchant(Enchantment.DURABILITY, 5, true);
        meta.setUnbreakable(true);
        NamespacedKey itemIDKey = new NamespacedKey((Plugin)Main.getPlugin(Main.class), "ItemId");
        meta.getPersistentDataContainer().set(itemIDKey, PersistentDataType.STRING, "HALDIRIONBOW");
        HaldirionBow.setItemMeta(meta);
        
        
        meta = HaldirionSword.getItemMeta();
        ArrayList<String> HaldirionSwordLore = new ArrayList<String>();
        HaldirionSwordLore.add(ChatUtil.color("&7"));
        HaldirionSwordLore.add(ChatUtil.color("&e&nMagicWand Item"));
        meta.setLore(HaldirionSwordLore);
        meta.setDisplayName(ChatUtil.color(LightSwordName));
        meta.addEnchant(Enchantment.DAMAGE_ALL, 6, true);
        meta.addEnchant(Enchantment.DURABILITY, 5, true);
        meta.setUnbreakable(true);
        itemIDKey = new NamespacedKey((Plugin)Main.getPlugin(Main.class), "ItemId");
        meta.getPersistentDataContainer().set(itemIDKey, PersistentDataType.STRING, "HALDIRIONSWORD");
        HaldirionSword.setItemMeta(meta);
    }
}

