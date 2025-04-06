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

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import me.magicwands.main.Main;
import me.magicwands.utils.ChatUtil;

public class IceItems
extends ItemStack implements Listener {
    public static ItemStack IceBow = new ItemStack(Material.BOW, 1);

    public IceItems() {
        ItemMeta meta = IceBow.getItemMeta();
        ArrayList<String> IceBowLore = new ArrayList<String>();
        IceBowLore.add(ChatUtil.color("&7"));
        IceBowLore.add(ChatUtil.color("&b&lItem Description:"));
        IceBowLore.add(ChatUtil.color("&7Frostborn's Ice Bow is a weapon"));
        IceBowLore.add(ChatUtil.color("&7crafted by Frostborn, a legendary"));
        IceBowLore.add(ChatUtil.color("&7blacksmith known for mastering"));
        IceBowLore.add(ChatUtil.color("&7the art of ice enchantments."));
        IceBowLore.add(ChatUtil.color("&7"));
        IceBowLore.add(ChatUtil.color("&b&lItem Statistics:"));
        IceBowLore.add(ChatUtil.color("&7Damage: &c+4"));
        IceBowLore.add(ChatUtil.color("&7Speed: &c+2"));
        IceBowLore.add(ChatUtil.color("&7Frost Aspect: &c+1"));
        IceBowLore.add(ChatUtil.color("&7"));
        IceBowLore.add(ChatUtil.color("&b&lSpecial Ability:"));
        IceBowLore.add(ChatUtil.color("&8Freezing Arrows &f- &7Use this ability"));
        IceBowLore.add(ChatUtil.color("&7to shoot arrows that slow enemies"));
        IceBowLore.add(ChatUtil.color("&7and freeze them in place."));
        meta.setLore(IceBowLore);
        meta.setDisplayName(ChatColor.AQUA + "Ice Bow");
        meta.addEnchant(Enchantment.ARROW_DAMAGE, 4, true);
        meta.addEnchant(Enchantment.DURABILITY, 5, true);
        meta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
        meta.addEnchant(Enchantment.FROST_WALKER, 1, true); // Simulates a freezing effect
        meta.setUnbreakable(true);
        NamespacedKey itemIDKey = new NamespacedKey((Plugin)Main.getPlugin(Main.class), "ItemId");
        meta.getPersistentDataContainer().set(itemIDKey, PersistentDataType.STRING, "ICEBOW");
        IceBow.setItemMeta(meta);
    }
}

