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
package me.magicwands.commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.magicwands.main.Main;
import me.magicwands.utils.ChatUtil;

public class MagicWandsCommand implements CommandExecutor {
    public static String prefix = Main.getPlugin(Main.class).getConfig().getString("wands.plugin.prefix");
    private final WandCommand wandCommand;
    private final ItemCommand itemCommand;
    private final ConfigureCommand configureCommand;
    private final List<String> wandNames = List.of("Fire", "Ice", "Nature", "Light", "Dark", "Empire");
    private final List<String> itemNames = List.of("haldirionbow", "FireBow", "ignatiusbow", "haldirionsword", "mortiferumbow");

    public MagicWandsCommand() {
        this.wandCommand = new WandCommand(wandNames);
        this.itemCommand = new ItemCommand(itemNames);
        this.configureCommand = new ConfigureCommand();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be used by players.");
            return true;
        }

        Player player = (Player) sender;
        if (!(cmd.getName().equalsIgnoreCase("magicwands") || cmd.getName().equalsIgnoreCase("mw"))) {
            return false;
        }

        boolean hasAllPermissions = player.hasPermission("magicwands.*");

        if (!hasAllPermissions) {
            if (args.length < 1) {
                sendUsageMessage(player);
                return true;
            }
        }

        if (args.length < 1) {
            sendUsageMessage(player);
            return true;
        }

        String subcommand = args[0].toLowerCase();

        switch (subcommand) {
            case "wand":
                if (hasAllPermissions || player.hasPermission("magicwands.command.wand")) {
                    wandCommand.handle(player, args);
                } else {
                    player.sendMessage(ChatUtil.color("&cYou do not have permission to use this command."));
                }
                break;

            case "item":
                if (hasAllPermissions || player.hasPermission("magicwands.command.item")) {
                    itemCommand.handle(player, args);
                } else {
                    player.sendMessage(ChatUtil.color("&cYou do not have permission to use this command."));
                }
                break;
            case "configure":
                if (hasAllPermissions || player.hasPermission("magicwands.command.configure")) {
                    configureCommand.handle(player, args);
                } else {
                    player.sendMessage(ChatUtil.color("&cYou do not have permission to use this command."));
                }
                break;

            case "getall":
                if (hasAllPermissions || player.hasPermission("magicwands.command.getall")) {
                    itemCommand.giveAllItems(player);
                } else {
                    player.sendMessage(ChatUtil.color("&cYou do not have permission to use this command."));
                }
                break;

            case "reload":
                if (hasAllPermissions || player.hasPermission("magicwands.command.reload")) {
                    Main.getPlugin(Main.class).reloadConfig();
                    Main.getPlugin(Main.class).saveConfig();
                    player.sendMessage(ChatUtil.color("&7[&e&lMagic&6&lWands&7] &eThe config has been reloaded successfully."));
                } else {
                    player.sendMessage(ChatUtil.color("&cYou do not have permission to use this command."));
                }
                break;

            case "toggleparticles":
                if (hasAllPermissions || player.hasPermission("magicwands.command.toggleparticles")) {
                    StaffCommands.toggleParticles(player);
                } else {
                    player.sendMessage(ChatUtil.color("&cYou do not have permission to use this command."));
                }
                break;
            case "togglesounds":
                if (hasAllPermissions || player.hasPermission("magicwands.command.toggleparticles")) {
                    StaffCommands.toggleSounds(player);;
                } else {
                    player.sendMessage(ChatUtil.color("&cYou do not have permission to use this command."));
                }
                break;

            case "togglelogs":
                if (hasAllPermissions || player.hasPermission("magicwands.command.togglelogs")) {
                    me.magicwands.utils.LogManager.toggleLogs(player);
                } else {
                    player.sendMessage(ChatUtil.color("&cYou do not have permission to use this command."));
                }
                break;

            default:
                sendUsageMessage(player);
                return false;
        }

        return true;
    }

    

    private void sendUsageMessage(Player player) {
        player.sendMessage(ChatUtil.color("&6&m---&r &e&lMagic&6&lWands&r &6&m---"));
        player.sendMessage(ChatUtil.color("&6Author: &eEffeLars &f| &6Version: &e0.1"));
        player.sendMessage(ChatUtil.color("&6"));
        player.sendMessage(ChatUtil.color("&e&lCommands:"));
        if (!player.hasPermission("magicwands.command.wand")) {
            player.sendMessage(ChatUtil.color("&6/nothing &e:( &f- &eYou dont have any permissions to do this... sad times.."));
        } else {
        if (player.hasPermission("magicwands.command.wand")) {
            player.sendMessage(ChatUtil.color("&6/magicwands &ewand &f- &eGet a list of available wands"));
        }
        if (player.hasPermission("magicwands.command.item")) {
            player.sendMessage(ChatUtil.color("&6/magicwands &eitem &f- &eGet a list of available items"));
        }
        if (player.hasPermission("magicwands.command.getall")) {
            player.sendMessage(ChatUtil.color("&6/magicwands &egetall &f- &eGet all current items."));
        }
        if (player.hasPermission("magicwands.command.toggleparticles")) {
            player.sendMessage(ChatUtil.color("&6/magicwands &etoggleparticles &f- &eToggle particles when switching spells"));
        }
        if (player.hasPermission("magicwands.command.togglesounds")) {
            player.sendMessage(ChatUtil.color("&6/magicwands &etogglesounds &f- &eToggle sounds when switching spells"));
        }
        if (player.hasPermission("magicwands.command.togglelogs")) {
            player.sendMessage(ChatUtil.color("&6/magicwands &etogglelogs &f- &eToggle logs in chat"));
        }
        if (player.hasPermission("magicwands.command.configure")) {
            player.sendMessage(ChatUtil.color("&6/magicwands &econfigure &f- &eConfigure wand settings"));
        }
    }
    }
}

