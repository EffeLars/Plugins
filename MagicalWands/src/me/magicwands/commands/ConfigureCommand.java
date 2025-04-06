package me.magicwands.commands;

import java.util.Arrays;

import org.bukkit.entity.Player;

import me.magicwands.main.Main;
import me.magicwands.utils.ChatUtil;
import me.magicwands.utils.LogManager;

public class ConfigureCommand {

	public void handle(Player player, String[] args) {

	    System.out.println("Arguments: " + Arrays.toString(args));

	    if (args.length < 4) {
	        player.sendMessage(ChatUtil.color("&cUsage: /magicwands configure <wandname/itemname> <setname/setcolor/setitem> <arg>"));
	        return;
	    }

	    String type = args[1].toLowerCase(); 
	    String name = args[2].toLowerCase(); 
	    String action = args[3].toLowerCase(); 
	    String argument = args[4];

	    
	    System.out.println("Type: " + type + ", Name: " + name + ", Action: " + action);

	    if (type.equals("wand") || type.equals("item")) {
	        switch (action) {
	            case "setname":
	                String newName = String.join(" ", Arrays.copyOfRange(args, 4, args.length)); 
	                if (type.equals("wand")) {
	                    setWandName(name, newName);
	                    LogManager.sendLog(ChatUtil.color(player.getName() + " &7has set the wand name of &e" + name + " &7to &f" + newName + "."));
	                    player.sendMessage(ChatUtil.color(MagicWandsCommand.prefix + " &fYou have set the wand name of " + name + " &fto " + newName));
	                } else if (type.equals("item")) {
	                    setItemName(name, newName);
	                    LogManager.sendLog(ChatUtil.color(player.getName() + " &7has set the item name of &e" + name + " &7to &f" + newName + "."));
	                    player.sendMessage(ChatUtil.color(MagicWandsCommand.prefix + " &fYou have set the item name of " + name + " &fto " + newName));
	                }
	                break;

	            case "setcolor":
	                if (type.equals("wand")) {
	                    setWandColor(name, argument);
	                    LogManager.sendLog(ChatUtil.color(player.getName() + " &7has set the wand color of &e" + name + " &7to &f" + argument + "."));
	                    player.sendMessage(ChatUtil.color(MagicWandsCommand.prefix + " &fYou have set the wand color of " + name + " &fto " + argument));
	                } else if (type.equals("item")) {
	                    setItemColor(name, argument);
	                    LogManager.sendLog(ChatUtil.color(player.getName() + " &7has set the item color of &e" + name + " &7to &f" + argument + "."));
	                    player.sendMessage(ChatUtil.color(MagicWandsCommand.prefix + " &fYou have set the item color of " + name + " &fto " + argument));
	                }
	                break;

	            case "setitem":
	                if (type.equals("wand")) {
	                    setWandItem(name, argument);
	                    LogManager.sendLog(ChatUtil.color(player.getName() + " &7has set the wand item of &e" + name + " &7to &f" + argument + "."));
	                    player.sendMessage(ChatUtil.color(MagicWandsCommand.prefix + " &fYou have set the wand item of " + name + " &fto " + argument));
	                } else if (type.equals("item")) {
	                    setItemItem(name, argument);
	                    LogManager.sendLog(ChatUtil.color(player.getName() + " &7has set the item of &e" + name + " &7to &f" + argument + "."));
	                    player.sendMessage(ChatUtil.color(MagicWandsCommand.prefix + " &fYou have set the item of " + name + " &fto " + argument));
	                }
	                break;

	            default:
	                player.sendMessage(ChatUtil.color("&cInvalid configuration action."));
	        }
	    } else {
	        player.sendMessage(ChatUtil.color("&cInvalid type. Please specify 'wand' or 'item'."));
	    }
	}


	
	private void setWandName(String wandName, String newName) {
	    if (newName != null && !newName.isEmpty()) {
	        Main.getPlugin(Main.class).getConfig().set("wands." + wandName + ".customname", newName);
	        Main.getPlugin(Main.class).saveConfig();
	        Main.getPlugin(Main.class).reloadConfig();
	    } else {
	        System.out.println("Invalid name provided for the wand: " + newName);
	    }
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

	private void setItemName(String itemName, String newName) {
	    if (newName != null && !newName.isEmpty()) {
	        Main.getPlugin(Main.class).getConfig().set("items." + itemName + ".customname", newName);
	        Main.getPlugin(Main.class).saveConfig();
	        Main.getPlugin(Main.class).reloadConfig();
	    } else {
	        System.out.println("Invalid name provided for the item: " + newName);
	    }
	}

	private void setItemColor(String itemName, String newColor) {
	    Main.getPlugin(Main.class).getConfig().set("items." + itemName + ".color", newColor);
	    Main.getPlugin(Main.class).saveConfig();
	    Main.getPlugin(Main.class).reloadConfig();
	}

	private void setItemItem(String itemName, String newItem) {
	    Main.getPlugin(Main.class).getConfig().set("items." + itemName + ".item", newItem);
	    Main.getPlugin(Main.class).saveConfig();
	    Main.getPlugin(Main.class).reloadConfig();
	}
}