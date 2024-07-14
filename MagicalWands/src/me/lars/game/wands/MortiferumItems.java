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
package me.lars.game.wands;

import java.util.ArrayList;
import me.lars.game.Main;
import me.lars.game.utils.ChatUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

public class MortiferumItems
extends ItemStack {
    public static ItemStack MortiferumBow = new ItemStack(Material.BOW, 1);

    public MortiferumItems() {
        ItemMeta meta = MortiferumBow.getItemMeta();
        ArrayList<String> MortiferumBowLore = new ArrayList<String>();
        MortiferumBowLore.add(ChatUtil.color("&7"));
        MortiferumBowLore.add(ChatUtil.color("&6&lItem Description:"));
        MortiferumBowLore.add(ChatUtil.color("&7Description about this deadly"));
        MortiferumBowLore.add(ChatUtil.color("&7and venomous bow goes here"));
        MortiferumBowLore.add(ChatUtil.color("&7with lots of cool information."));
        MortiferumBowLore.add(ChatUtil.color("&7"));
        MortiferumBowLore.add(ChatUtil.color("&6&lItem Statistics:"));
        MortiferumBowLore.add(ChatUtil.color("&7Damage: &c+3"));
        MortiferumBowLore.add(ChatUtil.color("&7Speed: &c+2"));
        MortiferumBowLore.add(ChatUtil.color("&7Poison Aspect: &c+1"));
        MortiferumBowLore.add(ChatUtil.color("&7"));
        MortiferumBowLore.add(ChatUtil.color("&6&lSpecial Ability:"));
        MortiferumBowLore.add(ChatUtil.color("&8Toxic Arrows &f- &7Use this ability"));
        MortiferumBowLore.add(ChatUtil.color("&7to shoot arrows that poison enemies"));
        MortiferumBowLore.add(ChatUtil.color("&7and inflict them with deadly toxins."));
        meta.setLore(MortiferumBowLore);
        meta.setDisplayName(ChatColor.DARK_PURPLE+ "Mortiferum's Poison Bow");
        meta.addEnchant(Enchantment.ARROW_DAMAGE, 3, true);
        meta.addEnchant(Enchantment.ARROW_FIRE, 2, true);
        meta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
        meta.setUnbreakable(true);
        NamespacedKey itemIDKey = new NamespacedKey((Plugin)Main.getPlugin(Main.class), "ItemId");
        meta.getPersistentDataContainer().set(itemIDKey, PersistentDataType.STRING, "MORTIFERUMBOW");
        MortiferumBow.setItemMeta(meta);
    }
}

