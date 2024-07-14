/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.Sound
 *  org.bukkit.attribute.Attribute
 *  org.bukkit.attribute.AttributeModifier
 *  org.bukkit.attribute.AttributeModifier$Operation
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.block.Action
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.inventory.EquipmentSlot
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 */
package me.rok.magicalwands.haldirion.customitems;

import java.util.UUID;
import me.lars.game.event.HaldirionWandEffect;
import me.lars.game.utils.ChatUtil;
import me.lars.game.wands.HaldirionItems;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class HaldirionSword
implements Listener {
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();
        if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
            ItemMeta meta;
            if (!player.isSneaking()) {
                return;
            }
            ItemStack heldItem = player.getInventory().getItemInMainHand();
            if (heldItem != null && heldItem.getType() == Material.DIAMOND_SWORD && (meta = heldItem.getItemMeta()) != null && meta.getDisplayName().equals(HaldirionItems.HaldirionSword.getItemMeta().getDisplayName())) {
                String modeMessage;
                int currentLevel = meta.getEnchantLevel(Enchantment.DAMAGE_ALL);
                int nextLevel = (currentLevel + 1) % 3;
                switch (nextLevel) {
                    case 0: {
                        meta.removeEnchant(Enchantment.DAMAGE_ALL);
                        meta.addEnchant(Enchantment.DAMAGE_ALL, 0, true);
                        this.setDamage(heldItem, 1.0);
                        modeMessage = ChatUtil.color("&a&LNORMAL DAMAGE");
                        break;
                    }
                    case 1: {
                        meta.removeEnchant(Enchantment.DAMAGE_ALL);
                        this.setDamage(heldItem, 0.0);
                        meta.addEnchant(Enchantment.DAMAGE_ALL, 1, true);
                        modeMessage = ChatUtil.color("&6&lFAKE DAMAGE");
                        break;
                    }
                    case 2: {
                        meta.removeEnchant(Enchantment.DAMAGE_ALL);
                        meta.addEnchant(Enchantment.DAMAGE_ALL, 2, true);
                        this.setDamage(heldItem, 3.0);
                        modeMessage = ChatUtil.color("&C&LEXTRA DAMAGE");
                        break;
                    }
                    default: {
                        modeMessage = "Mode: Unknown";
                    }
                }
                heldItem.setItemMeta(meta);
                this.spawnparticles(player);
                player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, (float)nextLevel);
                player.sendMessage(ChatUtil.color("&7[&dMagic&7] &fYou have changed the mode of your sword to " + modeMessage));
            }
        }
    }

    public void setDamage(ItemStack sword, double damageAmount) {
        ItemMeta meta = sword.getItemMeta();
        meta.removeAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE);
        AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(), "generic.attackDamage", damageAmount, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
        meta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, modifier);
        sword.setItemMeta(meta);
    }

    private void spawnparticles(Player player) {
        HaldirionWandEffect wandAnimation = new HaldirionWandEffect(player);
        wandAnimation.start();
    }
}

