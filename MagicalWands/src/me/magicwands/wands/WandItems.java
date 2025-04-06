package me.magicwands.wands;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.magicwands.main.Main;
import me.magicwands.utils.ChatUtil;

public class WandItems implements Listener {
    public static ItemStack WitchWand;
    public static ItemStack NatureWand;
    public static ItemStack LightWand;
    public static ItemStack IceWand;
    public static ItemStack IgnatiusWand;
    public static ItemStack EmpireWand;
    public static ItemStack DarkWand;

    static {
        WitchWand = createWand("witch");
        NatureWand = createWand("nature");
        LightWand = createWand("light");
        IceWand = createWand("ice");
        IgnatiusWand = createWand("fire");
        EmpireWand = createWand("empire");
        DarkWand = createWand("dark");
    }

    private static ItemStack createWand(String type) {
        Material material = Material.valueOf(Main.getPlugin(Main.class).getConfig().getString("wands." + type + ".item").toUpperCase());
        String name = Main.getPlugin(Main.class).getConfig().getString("wands." + type + ".customname");
        
        ItemStack wand = new ItemStack(material, 1);
        ItemMeta meta = wand.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatUtil.color(name));
            meta.setLore(new ArrayList<>());
            wand.setItemMeta(meta);
        }
        return wand;
    }
}
