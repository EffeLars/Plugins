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

public class FireItems
extends ItemStack implements Listener {
    public static ItemStack FireBow = new ItemStack(Material.BOW, 1);

    public FireItems() {
        ItemMeta meta = FireBow.getItemMeta();
        ArrayList<String> FireBowLore = new ArrayList<String>();
        FireBowLore.add(ChatUtil.color("&7"));
        FireBowLore.add(ChatUtil.color("&6&lItem Description:"));
        FireBowLore.add(ChatUtil.color("&7Ignatius' Fire Bow is a weapon forged"));
        FireBowLore.add(ChatUtil.color("&7by Ignatius, a legendary blacksmith"));
        FireBowLore.add(ChatUtil.color("&7known for his exceptional skills, This bow was"));
        FireBowLore.add(ChatUtil.color("&7used in many fights, But forgotten in time."));
        FireBowLore.add(ChatUtil.color("&7"));
        FireBowLore.add(ChatUtil.color("&6&lItem Statistics:"));
        FireBowLore.add(ChatUtil.color("&7Damage: &c+4"));
        FireBowLore.add(ChatUtil.color("&7Speed: &c+2"));
        FireBowLore.add(ChatUtil.color("&7Fire Aspect: &c+1"));
        FireBowLore.add(ChatUtil.color("&7"));
        FireBowLore.add(ChatUtil.color("&6&lSpecial Ability:"));
        FireBowLore.add(ChatUtil.color("&8Flaming Arrows &f- &7Use this ability"));
        FireBowLore.add(ChatUtil.color("&7to shoot arrows that ignite enemies"));
        FireBowLore.add(ChatUtil.color("&7and set them on fire."));
        meta.setLore(FireBowLore);
        meta.setDisplayName(ChatColor.GOLD + "Fire Bow");
        meta.addEnchant(Enchantment.ARROW_DAMAGE, 4, true);
        meta.addEnchant(Enchantment.DURABILITY, 5, true);
        meta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
        meta.addEnchant(Enchantment.ARROW_FIRE, 1, true);
        meta.setUnbreakable(true);
        NamespacedKey itemIDKey = new NamespacedKey((Plugin)Main.getPlugin(Main.class), "ItemId");
        meta.getPersistentDataContainer().set(itemIDKey, PersistentDataType.STRING, "FIREBOW");
        FireBow.setItemMeta(meta);
    }
}

