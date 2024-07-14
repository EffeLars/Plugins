/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.block.Block
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 */
package me.rok.main.listener.Haldirion;

import java.util.Arrays;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class HaldirionDamage
implements Listener {
    public static ItemStack createCustomItem(Material material, String itemName, String ... lore) {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(itemName);
        if (lore.length > 0) {
            itemMeta.setLore(Arrays.asList(lore));
        }
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public int getPlayerLightLevel(Player player) {
        Block block = player.getLocation().getBlock();
        byte lightLevel = block.getLightLevel();
        return lightLevel;
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            Player attacker = (Player)event.getDamager();
            Player victim = (Player)event.getEntity();
            if (attacker.getName().equalsIgnoreCase("Haldirion")) {
                byte lightLevel = attacker.getLocation().getBlock().getLightLevel();
                double baseDamageModifier = 1.0;
                double damageModifierPerLightLevel = 0.4;
                double damageModifier = baseDamageModifier + (double)lightLevel * damageModifierPerLightLevel;
                double originalDamage = event.getDamage();
                double modifiedDamage = originalDamage * damageModifier;
                event.setDamage(modifiedDamage);
            }
        }
    }
}

