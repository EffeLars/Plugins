/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.NamespacedKey
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.persistence.PersistentDataType
 *  org.bukkit.plugin.Plugin
 */
package me.magicwands.wands;

import java.util.ArrayList;

import me.magicwands.main.Main;
import me.magicwands.utils.ChatUtil;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

public class NatureItems
extends ItemStack {
    public static ItemStack NatureBow = new ItemStack(Material.BOW, 1);

    public NatureItems() {
        ItemMeta meta = NatureBow.getItemMeta();
        ArrayList<String> NatureBowLore = new ArrayList<String>();
        NatureBowLore.add(ChatUtil.color("&7"));
        NatureBowLore.add(ChatUtil.color("&6&lItem Description:"));
        NatureBowLore.add(ChatUtil.color("&7Description about this deadly"));
        NatureBowLore.add(ChatUtil.color("&7and venomous bow goes here"));
        NatureBowLore.add(ChatUtil.color("&7with lots of cool information."));
        NatureBowLore.add(ChatUtil.color("&7"));
        NatureBowLore.add(ChatUtil.color("&6&lItem Statistics:"));
        NatureBowLore.add(ChatUtil.color("&7Damage: &c+3"));
        NatureBowLore.add(ChatUtil.color("&7Speed: &c+2"));
        NatureBowLore.add(ChatUtil.color("&7Poison Aspect: &c+1"));
        NatureBowLore.add(ChatUtil.color("&7"));
        NatureBowLore.add(ChatUtil.color("&6&lSpecial Ability:"));
        NatureBowLore.add(ChatUtil.color("&8Toxic Arrows &f- &7Use this ability"));
        NatureBowLore.add(ChatUtil.color("&7to shoot arrows that poison enemies"));
        NatureBowLore.add(ChatUtil.color("&7and inflict them with deadly toxins."));
        meta.setLore(NatureBowLore);
        meta.setDisplayName(ChatColor.DARK_PURPLE+ "Nature Bow");
        meta.addEnchant(Enchantment.ARROW_DAMAGE, 3, true);
        meta.addEnchant(Enchantment.ARROW_FIRE, 2, true);
        meta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
        meta.setUnbreakable(true);
        NamespacedKey itemIDKey = new NamespacedKey((Plugin)Main.getPlugin(Main.class), "ItemId");
        meta.getPersistentDataContainer().set(itemIDKey, PersistentDataType.STRING, "MORTIFERUMBOW");
        NatureBow.setItemMeta(meta);
    }
}

