package me.magicwands.commands;

import java.util.List;

import org.bukkit.entity.Player;

import me.magicwands.utils.ChatUtil;
import me.magicwands.utils.LogManager;
import me.magicwands.wands.DarkWand;
import me.magicwands.wands.FireWand;
import me.magicwands.wands.IceWand;
import me.magicwands.wands.NatureWand;
import me.magicwands.wands.WandItems;
import me.magicwands.wands.WitchWand;

public class WandCommand {

    private final List<String> wandNames;

    public WandCommand(List<String> wandNames) {
        this.wandNames = wandNames;
    }

    public void handle(Player player, String[] args) {
        if (args.length == 1) {
            listWands(player);
        } else {
            String wandName = args[1].toLowerCase();
            handleWandCommand(player, wandName);
        }
    }

    private void listWands(Player player) {
        player.sendMessage(ChatUtil.color("&eList of available wands:"));
        for (String wandName : wandNames) {
            player.sendMessage(ChatUtil.color("&e- " + wandName));
        }
    }

    private void handleWandCommand(Player player, String wandName) {
        switch (wandName.toLowerCase()) {
            case "fire":
                player.getInventory().addItem(FireWand.IgnatiusWand);
                LogManager.sendLog(ChatUtil.color(player.getName() + " &7has spawned a &c" + FireWand.IgnatiusWand.getItemMeta().getDisplayName() + " &7wand."));
                player.sendMessage(ChatUtil.color("&7[&e&lMagic&6&lWands&7] &fYou have received the &c" + FireWand.IgnatiusWand.getItemMeta().getDisplayName() + " &fwand in your inventory."));
                break;
            case "witch":
                player.getInventory().addItem(me.magicwands.wands.WitchWand.WitchWand);
                LogManager.sendLog(ChatUtil.color(player.getName() + " &7has spawned a &c" + WitchWand.WitchWand.getItemMeta().getDisplayName() + " &7wand."));
                player.sendMessage(ChatUtil.color("&7[&e&lMagic&6&lWands&7] &fYou have received the &c" + WitchWand.WitchWand.getItemMeta().getDisplayName() + " &fwand in your inventory."));
                break;
            case "ice":
                player.getInventory().addItem(IceWand.IceWand);
                LogManager.sendLog(ChatUtil.color(player.getName() + " &7has spawned a &b" + IceWand.IceWand.getItemMeta().getDisplayName() + " &7wand."));
                player.sendMessage(ChatUtil.color("&7[&e&lMagic&6&lWands&7] &fYou have received the &b" + IceWand.IceWand.getItemMeta().getDisplayName() + " &fwand in your inventory."));
                break;
            case "nature":
                player.getInventory().addItem(NatureWand.NatureWand);
                LogManager.sendLog(ChatUtil.color(player.getName() + " &7has spawned a &a" + NatureWand.NatureWand.getItemMeta().getDisplayName() + " &7wand."));
                player.sendMessage(ChatUtil.color("&7[&e&lMagic&6&lWands&7] &fYou have received the &a" + NatureWand.NatureWand.getItemMeta().getDisplayName() + " &fwand in your inventory."));
                break;
            case "empire":
                player.getInventory().addItem(WandItems.EmpireWand);
                LogManager.sendLog(ChatUtil.color(player.getName() + " &7has spawned a &a" + WandItems.EmpireWand.getItemMeta().getDisplayName() + " &7wand."));
                player.sendMessage(ChatUtil.color("&7[&e&lMagic&6&lWands&7] &fYou have received the &a" + WandItems.EmpireWand.getItemMeta().getDisplayName() + " &fwand in your inventory."));
                break;
            case "light":
                player.getInventory().addItem(me.magicwands.wands.LightWand.LightWand);
                LogManager.sendLog(ChatUtil.color(player.getName() + " &7has spawned a &f" + me.magicwands.wands.LightWand.LightWand.getItemMeta().getDisplayName() + " &7wand."));
                player.sendMessage(ChatUtil.color("&7[&e&lMagic&6&lWands&7] &fYou have received the &f" + me.magicwands.wands.LightWand.LightWand.getItemMeta().getDisplayName() + " &fwand in your inventory."));
                break;
            case "dark":
                player.getInventory().addItem(DarkWand.DarkWand);
                LogManager.sendLog(ChatUtil.color(player.getName() + " &7has spawned a &8" + DarkWand.DarkWand.getItemMeta().getDisplayName() + " &7wand."));
                player.sendMessage(ChatUtil.color("&7[&e&lMagic&6&lWands&7] &fYou have received the &8" + DarkWand.DarkWand.getItemMeta().getDisplayName() + " &fwand in your inventory."));
                break;
            default:
                player.sendMessage(ChatUtil.color("&cYeah no this does not exist, try again. :)"));
        }
    }

}
