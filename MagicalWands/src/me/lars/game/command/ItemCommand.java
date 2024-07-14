/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 */
package me.lars.game.command;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.lars.game.Main;
import me.lars.game.utils.ChatUtil;
import me.lars.game.wands.FireItems;
import me.lars.game.wands.FlowerWand;
import me.lars.game.wands.HaldirionItems;
import me.lars.game.wands.IceWand;
import me.lars.game.wands.MortiferumItems;
import me.lars.game.wands.NatureWand;
import me.lars.game.wands.NyxaraItems;
import me.lars.game.wands.Wands;

public class ItemCommand
implements CommandExecutor {
    
    public static String getUUIDName(UUID uuid) {
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer((UUID)uuid);
        if (offlinePlayer.hasPlayedBefore()) {
            return offlinePlayer.getName();
        }
        return null;
    }

    private final List<String> wandNames = List.of("ignatiusWand", "nyxaraWand", "haldirionWand", "mortiferumWand");
    private final List<String> itemNames = List.of("haldirionbow", "nyxarabow", "ignatiusbow", "haldirionsword", "mortiferumbow");

    private void sendUsageMessage(CommandSender sender) {
        sender.sendMessage(ChatUtil.color("&6&m---&r &e&lMagic&6&lWands&r &6&m---"));
        sender.sendMessage(ChatUtil.color("&6"));
        sender.sendMessage(ChatUtil.color("&6Usage&e: &6/magicwands &ewand &f- &eGet a list of available wands"));
        sender.sendMessage(ChatUtil.color("&6Usage&e: &6/magicwands &eitem &f- &eGet a list of available items"));
        sender.sendMessage(ChatUtil.color("&6Usage&e: &6/magicwands &egetall &f- &eGet all curent items."));
        sender.sendMessage(ChatUtil.color("&6Usage&e: &6/magicwands &etoggleparticles &f- &eToggle particles when switching spells"));
        sender.sendMessage(ChatUtil.color("&6Usage&e: &6/magicwands &econfigure &f- &eConfigure wand settings"));
    }

    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be used by players.");
            return true;
        }

        Player player = (Player) sender;


        if (!cmd.getName().equalsIgnoreCase("magicwands")) {
            return true;
        }

        if (args.length < 1) {
            sendUsageMessage(sender);
            return true;
        }

        // Handle subcommands
        String subcommand = args[0].toLowerCase();
        switch (subcommand) {
            case "wand":
                handleWandSubcommand(player, sender, args);
                break;
            case "item":
                handleItemSubcommand(player, sender, args);
                break;
            case "getall":
                giveAllItems(player);
                break;
            case "toggleparticles":
                toggleParticles(player);
                break;
            case "configure":
                if (args.length < 4) {
                    sender.sendMessage(ChatUtil.color("&cUsage: /magicwands configure <wandname> <setname/setcolor/setitem> <arg>"));
                    return true;
                }
                handleConfigureCommand(player, sender, args);
                break;
            default:
                return true;
        }

        return true;
    }

    private void handleWandSubcommand(Player player, CommandSender sender, String[] args) {
        if (args.length == 1) {
            player.sendMessage(ChatUtil.color("&eList of available wands:"));
            for (String wandName : wandNames) {
                player.sendMessage(ChatUtil.color("&e- " + wandName));
            }
        } else {
            String wandName = args[1].toLowerCase();
            handleWandCommand(player, sender, wandName);
        }
    }

    private void handleItemSubcommand(Player player, CommandSender sender, String[] args) {
        if (args.length == 1) {
            player.sendMessage(ChatUtil.color("&eList of available items:"));
            for (String itemName : itemNames) {
                player.sendMessage(ChatUtil.color("&e- " + itemName));
            }
        } else {
            String itemName = args[1].toLowerCase();
            handleItemCommand(player, sender, itemName);
        }
    }

    private void giveAllItems(Player player) {
        player.getInventory().addItem(Wands.IgnatiusWand);
        player.getInventory().addItem(IceWand.IceWand);
        player.getInventory().addItem(NatureWand.NatureWand);
        player.getInventory().addItem(FlowerWand.FlowerWand);
        player.getInventory().addItem(Wands.MortiferumWand);
        player.getInventory().addItem(HaldirionItems.HaldirionBow);
        player.getInventory().addItem(NyxaraItems.NyxaraBow);
        player.getInventory().addItem(FireItems.IgnatiusBow);
        player.getInventory().addItem(HaldirionItems.HaldirionSword);
        player.getInventory().addItem(MortiferumItems.MortiferumBow);
    }

    private void toggleParticles(Player player) {
        if (player.getScoreboardTags().contains("particleon")) {
            player.sendMessage(ChatUtil.color("&eTurned particles off"));
            player.removeScoreboardTag("particleon");
        } else {
            player.sendMessage(ChatUtil.color("&eTurned particles on"));
            player.addScoreboardTag("particleon");
        }
    }

    private void handleConfigureCommand(Player player, CommandSender sender, String[] args) {
        String wandName = args[1].toLowerCase();
        String action = args[2].toLowerCase();
        String argument = args[3];

        switch (action) {
            case "setname":
                setWandName(wandName, argument);
                sender.sendMessage(ChatUtil.color("&aWand name has been set to " + argument));
                break;
            case "setcolor":
                setWandColor(wandName, argument);
                sender.sendMessage(ChatUtil.color("&aWand color has been set to " + argument));
                break;
            case "setitem":
                setWandItem(wandName, argument);
                sender.sendMessage(ChatUtil.color("&aWand item has been set to " + argument));
                break;
            default:
                sender.sendMessage(ChatUtil.color("&cInvalid configuration action. Use setname, setcolor, or setitem."));
        }
    }

    private void handleWandCommand(Player player, CommandSender sender, String itemName) {
        switch (itemName.toLowerCase()) {
            case "fire":
                player.getInventory().addItem(Wands.IgnatiusWand);
                sender.sendMessage(ChatUtil.color("&c&lWANDS &cYou have received the &4Fire &cwand. Do &c&lNOT &cmisuse this item, or you will be &4&lBanned."));
                break;
            case "ice":
                player.getInventory().addItem(IceWand.IceWand);
                sender.sendMessage(ChatUtil.color("&c&lWANDS &cYou have received the &bIce Wand&c. Do &c&lNOT &cmisuse this item, or you will be &4&lBanned."));
                break;
            case "nature":
                player.getInventory().addItem(NatureWand.NatureWand);
                sender.sendMessage(ChatUtil.color("&c&lWANDS &cYou have received the &aNature Wand&c. Do &c&lNOT &cmisuse this item, or you will be &4&lBanned."));
                break;
            case "flower":
                player.getInventory().addItem(me.lars.game.wands.FlowerWand.FlowerWand);
                sender.sendMessage(ChatUtil.color("&c&lWANDS &cYou have received the &dFlower Wand&c. Do &c&lNOT &cmisuse this item, or you will be &4&lBanned."));
                break;
            case "mortiferumwand":
                player.getInventory().addItem(Wands.MortiferumWand);
                sender.sendMessage(ChatUtil.color("&c&lWANDS &cYou have received the &4MortiferumWand&c. Do &c&lNOT &cmisuse this item, or you will be &4&lBanned."));
                break;
            default:
                sender.sendMessage("Invalid wand name. Available wand names: IgnatiusWand, NyxaraWand, HaldirionWand, MortiferumWand.");
        }
    }

    private void handleItemCommand(Player player, CommandSender sender, String itemName) {
        switch (itemName.toLowerCase()) {
            case "haldirionbow":
                player.getInventory().addItem(HaldirionItems.HaldirionBow);
                sender.sendMessage(ChatUtil.color("&c&lWANDS &cYou have received the &4HaldirionBow&c. Do &c&lNOT &cmisuse this item, or you will be &4&lBanned."));
                break;
            case "nyxarabow":
                player.getInventory().addItem(NyxaraItems.NyxaraBow);
                sender.sendMessage(ChatUtil.color("&c&lWANDS &cYou have received the &4Nyxara's Celestial Bow&c. Do &c&lNOT &cmisuse this item, or you will be &4&lBanned."));
                break;
            case "ignatiusbow":
                player.getInventory().addItem(FireItems.IgnatiusBow);
                sender.sendMessage(ChatUtil.color("&c&lWANDS &cYou have received the &4Ignatius's Celestial Bow&c. Do &c&lNOT &cmisuse this item, or you will be &4&lBanned."));
                break;
            case "haldirionsword":
                player.getInventory().addItem(HaldirionItems.HaldirionSword);
                sender.sendMessage(ChatUtil.color("&c&lWANDS &cYou have received the &eHaldirion's Sword&c. Do &c&lNOT &cmisuse this item, or you will be &4&lBanned."));
                break;
            case "mortiferumbow":
                player.getInventory().addItem(MortiferumItems.MortiferumBow);
                sender.sendMessage(ChatUtil.color("&c&lWANDS &cYou have received the &4Mortiferum's Celestial Bow&c. Do &c&lNOT &cmisuse this item, or you will be &4&lBanned."));
                break;
            default:
                sender.sendMessage("Invalid item name. Available item names: HaldirionBow, NyxaraBow, IgnatiusBow, MortiferumBow.");
        }
    }


    private void setWandName(String wandName, String newName) {
    	Main.getPlugin(Main.class).getConfig().set("wands." + wandName + ".customname", newName);
    	Main.getPlugin(Main.class).saveConfig();
    	Main.getPlugin(Main.class).reloadConfig();
    }

    private void setWandColor(String wandName, String newColor) {
    	Main.getPlugin(Main.class).getConfig().set("wands." + wandName + ".color", newColor);
    	Main.getPlugin(Main.class).saveConfig();
    	Main.getPlugin(Main.class).reloadConfig();
    }

    private void setWandItem(String wandName, String newItem) {
    	Main.getPlugin(Main.class).getConfig().set("wands." + wandName + ".item", newItem);
    	Main.getPlugin(Main.class).saveConfig();
    	Main.getPlugin(Main.class).reloadConfig();
    }
}