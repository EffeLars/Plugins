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

import me.rok.main.listener.MessagingSystem;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class configReloadCommand
implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player)sender;
            MessagingSystem.sendMessage(player, player, "This is what you need to do, So go and do it you fucking weirdo.");
            MessagingSystem.displayMessageMenu(player, 1);
            player.sendMessage("Yu222r");
        }
        return true;
    }
}

