package me.magicwands.commands;

import java.util.List;

import org.bukkit.entity.Player;

import me.magicwands.utils.ChatUtil;
import me.magicwands.utils.LogManager;
import me.magicwands.wands.CustomItems;
import me.magicwands.wands.FireItems;
import me.magicwands.wands.FireWand;
import me.magicwands.wands.IceItems;
import me.magicwands.wands.IceWand;
import me.magicwands.wands.LightItems;
import me.magicwands.wands.NatureItems;
import me.magicwands.wands.NatureWand;
import me.magicwands.wands.WitchItems;

public class ItemCommand {

    private final List<String> itemNames;

    public ItemCommand(List<String> itemNames) {
        this.itemNames = itemNames;
    }

    public void handle(Player player, String[] args) {
        if (args.length == 1) {
            listItems(player);
        } else {
            String itemName = args[1].toLowerCase();
            handleItemCommand(player, itemName);
        }
    }

    private void listItems(Player player) {
        player.sendMessage(ChatUtil.color("&eList of available items:"));
        for (String itemName : itemNames) {
            player.sendMessage(ChatUtil.color("&e- " + itemName));
        }
    }

    private void handleItemCommand(Player player, String itemName) {
        switch (itemName) {
            case "haldirionbow":
                player.getInventory().addItem(LightItems.HaldirionBow);
                LogManager.sendLog(ChatUtil.color(player.getName() + " &7has spawned a &e" + LightItems.HaldirionBow.getItemMeta().getDisplayName() + " &7bow."));
                player.sendMessage(ChatUtil.color("&7[&e&lMagic&6&lWands&7] &fYou have received the &e" + LightItems.HaldirionBow.getItemMeta().getDisplayName() + " &fbow in your inventory."));
                break;
            case "lightsword":
                player.getInventory().addItem(LightItems.HaldirionSword);
                LogManager.sendLog(ChatUtil.color(player.getName() + " &7has spawned a &e" + LightItems.HaldirionSword.getItemMeta().getDisplayName() + " &e."));
                player.sendMessage(ChatUtil.color("&7[&e&lMagic&6&lWands&7] &fYou have received the &e" + LightItems.HaldirionSword.getItemMeta().getDisplayName() + " &fbow in your inventory."));
                break;
            case "icebow":
                player.getInventory().addItem(IceItems.IceBow);
                LogManager.sendLog(ChatUtil.color(player.getName() + " &7has spawned a &b" + IceItems.IceBow.getItemMeta().getDisplayName() + " &7bow."));
                player.sendMessage(ChatUtil.color("&7[&e&lMagic&6&lWands&7] &fYou have received the &b" + IceItems.IceBow.getItemMeta().getDisplayName() + " &fbow in your inventory."));
                break;
            case "firebow":
                player.getInventory().addItem(FireItems.FireBow);
                LogManager.sendLog(ChatUtil.color(player.getName() + " &7has spawned a &c" + FireItems.FireBow.getItemMeta().getDisplayName() + " &7bow."));
                player.sendMessage(ChatUtil.color("&7[&e&lMagic&6&lWands&7] &fYou have received the &c" + FireItems.FireBow.getItemMeta().getDisplayName() + " &fbow in your inventory."));
                break;
            case "naturebow":
                player.getInventory().addItem(NatureItems.NatureBow);
                LogManager.sendLog(ChatUtil.color(player.getName() + " &7has spawned a &2" + NatureItems.NatureBow.getItemMeta().getDisplayName() + " &7bow."));
                player.sendMessage(ChatUtil.color("&7[&e&lMagic&6&lWands&7] &fYou have received the &2" + NatureItems.NatureBow.getItemMeta().getDisplayName() + " &fbow in your inventory."));
                break;
            case "horn":
                player.getInventory().addItem(CustomItems.HornOfValereItem);
                LogManager.sendLog(ChatUtil.color(player.getName() + " &7has spawned a &2" + CustomItems.HornOfValereItem.getItemMeta().getDisplayName() + " &7bow."));
                player.sendMessage(ChatUtil.color("&7[&e&lMagic&6&lWands&7] &fYou have received the &2" + CustomItems.HornOfValereItem.getItemMeta().getDisplayName() + " &fin your inventory."));
                break;
            default:
                player.sendMessage(ChatUtil.color("&cThis MagicWands item does not exist."));
        }
    }


    public void giveAllItems(Player player) {
        player.getInventory().addItem(FireWand.IgnatiusWand);
        player.getInventory().addItem(FireItems.FireBow);
        player.getInventory().addItem(IceWand.IceWand);
        player.getInventory().addItem(IceItems.IceBow);
        player.getInventory().addItem(NatureWand.NatureWand);
        player.getInventory().addItem(NatureItems.NatureBow);
        player.getInventory().addItem(me.magicwands.wands.LightWand.LightWand);
        player.getInventory().addItem(LightItems.HaldirionBow);
        player.getInventory().addItem(me.magicwands.wands.WitchWand.WitchWand);
        player.getInventory().addItem(WitchItems.NyxaraBow);
        player.getInventory().addItem(LightItems.HaldirionSword);
        LogManager.sendLog(ChatUtil.color(player.getName() + " &7has spawned all MagicalWands items."));
        player.sendMessage(ChatUtil.color("&7[&e&lMagic&6&lWands&7] &fYou have received all the MagicalWands items."));
    }

    
}
