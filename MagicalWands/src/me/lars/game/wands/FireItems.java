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

public class FireItems
extends ItemStack {
    public static ItemStack IgnatiusBow = new ItemStack(Material.BOW, 1);

    public FireItems() {
        ItemMeta meta = IgnatiusBow.getItemMeta();
        ArrayList<String> IgnatiusBowLore = new ArrayList<String>();
        IgnatiusBowLore.add(ChatUtil.color("&7"));
        IgnatiusBowLore.add(ChatUtil.color("&6&lItem Description:"));
        IgnatiusBowLore.add(ChatUtil.color("&7Ignatius' Fire Bow is a weapon forged"));
        IgnatiusBowLore.add(ChatUtil.color("&7by Ignatius, a legendary blacksmith"));
        IgnatiusBowLore.add(ChatUtil.color("&7known for his exceptional skills, This bow was"));
        IgnatiusBowLore.add(ChatUtil.color("&7used in many fights, But forgotten in time."));
        IgnatiusBowLore.add(ChatUtil.color("&7"));
        IgnatiusBowLore.add(ChatUtil.color("&6&lItem Statistics:"));
        IgnatiusBowLore.add(ChatUtil.color("&7Damage: &c+4"));
        IgnatiusBowLore.add(ChatUtil.color("&7Speed: &c+2"));
        IgnatiusBowLore.add(ChatUtil.color("&7Fire Aspect: &c+1"));
        IgnatiusBowLore.add(ChatUtil.color("&7"));
        IgnatiusBowLore.add(ChatUtil.color("&6&lSpecial Ability:"));
        IgnatiusBowLore.add(ChatUtil.color("&8Flaming Arrows &f- &7Use this ability"));
        IgnatiusBowLore.add(ChatUtil.color("&7to shoot arrows that ignite enemies"));
        IgnatiusBowLore.add(ChatUtil.color("&7and set them on fire."));
        meta.setLore(IgnatiusBowLore);
        meta.setDisplayName(ChatColor.GOLD + "Ignatius' Fire Bow");
        meta.addEnchant(Enchantment.ARROW_DAMAGE, 4, true);
        meta.addEnchant(Enchantment.DURABILITY, 5, true);
        meta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
        meta.addEnchant(Enchantment.ARROW_FIRE, 1, true);
        meta.setUnbreakable(true);
        NamespacedKey itemIDKey = new NamespacedKey((Plugin)Main.getPlugin(Main.class), "ItemId");
        meta.getPersistentDataContainer().set(itemIDKey, PersistentDataType.STRING, "IGNATIUSBOW");
        IgnatiusBow.setItemMeta(meta);
    }
}

