/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 */
package me.rok.main.command;

import me.rok.main.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class configReloadCommand
implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player)sender;
            ((Main)Main.getPlugin(Main.class)).getConfig().set("message.plugin.permissioncolor", (Object)"&6");
            ((Main)Main.getPlugin(Main.class)).saveConfig();
            player.sendMessage("Yur");
        }
        return true;
    }
}

