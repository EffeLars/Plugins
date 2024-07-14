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

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import me.lars.game.Main;
import me.lars.game.utils.ChatUtil;

public class HaldirionItems
extends ItemStack {
    public static ItemStack HaldirionBow = new ItemStack(Material.BOW, 1);
    public static ItemStack HaldirionSword = new ItemStack(Material.DIAMOND_SWORD, 1);

    public HaldirionItems() {
        ItemMeta meta = HaldirionBow.getItemMeta();
        ArrayList<String> HaldirionBowLore = new ArrayList<String>();
        HaldirionBowLore.add(ChatUtil.color("&7"));
        HaldirionBowLore.add(ChatUtil.color("&6&lItem Description:"));
        HaldirionBowLore.add(ChatUtil.color("&7Description about this very cool and"));
        HaldirionBowLore.add(ChatUtil.color("&7Special unique item Comes here"));
        HaldirionBowLore.add(ChatUtil.color("&7With alot of cool information about the item"));
        HaldirionBowLore.add(ChatUtil.color("&7I guess lol kekw hehe haha :)"));
        HaldirionBowLore.add(ChatUtil.color("&7"));
        HaldirionBowLore.add(ChatUtil.color("&6&lItem Statistics:"));
        HaldirionBowLore.add(ChatUtil.color("&7Damage: &c+3"));
        HaldirionBowLore.add(ChatUtil.color("&7Speed: &c+2"));
        HaldirionBowLore.add(ChatUtil.color("&7Strenght: &c+5"));
        HaldirionBowLore.add(ChatUtil.color("&7"));
        HaldirionBowLore.add(ChatUtil.color("&6&lSpecial Ability:"));
        HaldirionBowLore.add(ChatUtil.color("&8Pure Light Arrow &f- &7Use this ability to blind people with"));
        HaldirionBowLore.add(ChatUtil.color("&7a pure burst of light and disorient them."));
        meta.setLore(HaldirionBowLore);
        meta.setDisplayName(ChatColor.RED + "Haldirion's Celestial Bow");
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
        HaldirionSwordLore.add(ChatUtil.color("&6&lItem Description:"));
        HaldirionSwordLore.add(ChatUtil.color("&7Once upon a time, in the realm of Eldoria,"));
        HaldirionSwordLore.add(ChatUtil.color("&7there lived Haldirion, a half-god born from the"));
        HaldirionSwordLore.add(ChatUtil.color("&7union of a mortal elf and a celestial deity."));
        HaldirionSwordLore.add(ChatUtil.color("&7He possessed the grace of the elves and the"));
        HaldirionSwordLore.add(ChatUtil.color("&7divine powers of his celestial heritage."));
        HaldirionSwordLore.add(ChatUtil.color("&7"));
        HaldirionSwordLore.add(ChatUtil.color("&6&lItem Statistics:"));
        HaldirionSwordLore.add(ChatUtil.color("&7Damage: &c+3"));
        HaldirionSwordLore.add(ChatUtil.color("&7Speed: &c+2"));
        HaldirionSwordLore.add(ChatUtil.color("&7Strength: &c+5"));
        HaldirionSwordLore.add(ChatUtil.color("&7"));
        HaldirionSwordLore.add(ChatUtil.color("&6&lSpecial Ability:"));
        HaldirionSwordLore.add(ChatUtil.color("&8Celestial Light &f- &7Harness the power of celestial"));
        HaldirionSwordLore.add(ChatUtil.color("&7energy to strike your enemies with blinding light."));
        meta.setLore(HaldirionSwordLore);
        meta.setDisplayName(ChatColor.RED + "Haldirion's Divine Sword");
        meta.addEnchant(Enchantment.DAMAGE_ALL, 6, true);
        meta.addEnchant(Enchantment.DURABILITY, 5, true);
        meta.setUnbreakable(true);
        itemIDKey = new NamespacedKey((Plugin)Main.getPlugin(Main.class), "ItemId");
        meta.getPersistentDataContainer().set(itemIDKey, PersistentDataType.STRING, "HALDIRIONSWORD");
        HaldirionSword.setItemMeta(meta);
    }
}

