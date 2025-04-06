package me.magicwands.wands;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.magicwands.main.Main;
import me.magicwands.utils.ChatUtil;

public class FireWand extends ItemStack {
    static Material fire = Material.valueOf(Main.getPlugin(Main.class).getConfig().getString("wands.fire.item").toUpperCase());
    public static ItemStack IgnatiusWand = new ItemStack(fire, 1);

    public FireWand() {
        String name = Main.getPlugin(Main.class).getConfig().getString("wands.fire.customname");
        ItemMeta meta = IgnatiusWand.getItemMeta();
        ArrayList<String> IgnatiusWandLore = new ArrayList<>();
        meta.setDisplayName(ChatUtil.color(name));
        IgnatiusWandLore.add(ChatColor.RED + "???");
        IgnatiusWandLore.add("Leap");
        meta.setLore(IgnatiusWandLore);
        IgnatiusWand.setItemMeta(meta);
    }
}
