/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.NamespacedKey
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.persistence.PersistentDataType
 *  org.bukkit.plugin.Plugin
 */
package me.rok.main.items;

import java.util.ArrayList;
import me.rok.main.Main;
import me.rok.main.util.ChatUtil;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

public class Ignatius
extends ItemStack {
    public static ItemStack IgnatiusSword = new ItemStack(Material.GOLDEN_SWORD, 1);

    static {
        ItemMeta meta = IgnatiusSword.getItemMeta();
        ArrayList<String> IgnatiusSwordLore = new ArrayList<String>();
        IgnatiusSwordLore.add(ChatUtil.color("&6"));
        IgnatiusSwordLore.add(ChatUtil.color("&6&lItem Description"));
        IgnatiusSwordLore.add(ChatUtil.color("&e--- The Ignatius Sword ---"));
        IgnatiusSwordLore.add("");
        IgnatiusSwordLore.add(ChatUtil.color("&7Forged deep within the heart of the"));
        IgnatiusSwordLore.add(ChatUtil.color("&7volcanic mountains, the Ignatius Sword"));
        IgnatiusSwordLore.add(ChatUtil.color("&7is a legendary weapon imbued with the essence of pure fire."));
        IgnatiusSwordLore.add("");
        IgnatiusSwordLore.add(ChatUtil.color("&7Crafted by the blacksmith, Ignatius,"));
        IgnatiusSwordLore.add(ChatUtil.color("&7using his unparalleled mastery of fire"));
        IgnatiusSwordLore.add(ChatUtil.color("&7and metal, this sword is a testament to"));
        IgnatiusSwordLore.add(ChatUtil.color("&7his extraordinary skill."));
        IgnatiusSwordLore.add("");
        IgnatiusSwordLore.add(ChatUtil.color("&7Legend speaks of the intense heat that"));
        IgnatiusSwordLore.add(ChatUtil.color("&7engulfed the forge as Ignatius poured"));
        IgnatiusSwordLore.add(ChatUtil.color("&7his own fiery essence into the molten"));
        IgnatiusSwordLore.add(ChatUtil.color("&7gold, fusing his spirit with the metal."));
        IgnatiusSwordLore.add("");
        meta.setDisplayName(ChatUtil.color("&6Ignatius Fire Blade"));
        meta.setLore(IgnatiusSwordLore);
        meta.addEnchant(Enchantment.DAMAGE_ALL, 7, true);
        meta.addEnchant(Enchantment.FIRE_ASPECT, 6, true);
        meta.setUnbreakable(true);
        NamespacedKey itemIDKey = new NamespacedKey((Plugin)Main.getPlugin(Main.class), "ItemId");
        meta.getPersistentDataContainer().set(itemIDKey, PersistentDataType.STRING, (Object)"IGNATIUSSWORD");
        IgnatiusSword.setItemMeta(meta);
    }
}

