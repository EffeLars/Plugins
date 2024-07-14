/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 */
package me.rok.main.command;

import me.rok.main.characters.special.Eldarion;
import me.rok.main.characters.special.Haldirion;
import me.rok.main.characters.special.Ignatius;
import me.rok.main.characters.special.Mortiferum;
import me.rok.main.characters.special.Nyxara;
import me.rok.main.gui.RPDisguiseGUI;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RPCommand
implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String subCommand;
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be executed by a player.");
            return true;
        }
        Player player = (Player)sender;
        if (args.length < 1) {
            this.displayHelpMenu(player);
            return true;
        }
        switch (subCommand = args[0].toLowerCase()) {
            case "menu": {
                this.executeMenu(player);
                break;
            }
            case "forcedisguise": {
                this.executeForceDisguise(player, args);
                break;
            }
            case "check": {
                this.executeCheck(player);
                break;
            }
            case "list": {
                this.executeList(player);
                break;
            }
            default: {
                this.displayHelpMenu(player);
            }
        }
        return true;
    }

    private void displayHelpMenu(Player player) {
        player.sendMessage("=== RP Command Help ===");
        player.sendMessage("/rp menu - Open the RP menu");
        player.sendMessage("/rp forcedisguise - Force a disguise for your character");
        player.sendMessage("/rp check - Check your RP status");
        player.sendMessage("/rp list - List available RP options");
    }

    private void executeMenu(Player player) {
        player.sendMessage("Opening RP menu...");
        RPDisguiseGUI.openInventory(RPDisguiseGUI.Menu, player);
    }

    private void executeForceDisguise(Player player, String[] args) {
        String[] characterNames;
        if (args.length < 2) {
            player.sendMessage("Usage: /rp forcedisguise <playername>");
            return;
        }
        String playerName = args[1];
        Player target = Bukkit.getPlayer((String)playerName);
        if (target == null || !target.isOnline()) {
            player.sendMessage("Player '" + playerName + "' not found or not online.");
            return;
        }
        String[] stringArray = characterNames = new String[]{"Ignatius", "Haldirion", "Nyxara", "Mortiferum", "Eldarion"};
        int n2 = characterNames.length;
        int n3 = 0;
        while (n3 < n2) {
            block22: {
                String character = stringArray[n3];
                if (!player.getName().equalsIgnoreCase(character)) break block22;
                switch (character) {
                    case "Ignatius": {
                        Ignatius.IgnatiusDisguise(target);
                        Ignatius.addDisguiseBossBar(target);
                        break;
                    }
                    case "Haldirion": {
                        Haldirion.HaldirionDisguise(target);
                        Haldirion.addDisguiseBossBar(target);
                        break;
                    }
                    case "Nyxara": {
                        Nyxara.NyxaraDisguise(target);
                        Nyxara.addDisguiseBossBar(target);
                        break;
                    }
                    case "Mortiferum": {
                        Mortiferum.MortiferumDisguise(target);
                        Mortiferum.addDisguiseBossBar(target);
                        break;
                    }
                    case "Eldarion": {
                        Eldarion.EldarionDisguise(target);
                        Eldarion.addDisguiseBossBar(target);
                    }
                }
                player.sendMessage("Forcing disguise as " + character + " for player " + target.getName() + "...");
                target.sendMessage("You have been forced into a disguise as " + character + ".");
                return;
            }
            ++n3;
        }
        player.sendMessage("Character not found.");
    }

    private void executeCheck(Player player) {
        player.sendMessage("Checking RP status...");
    }

    private void executeList(Player player) {
        player.sendMessage("Listing available RP options...");
    }
}

