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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import me.rok.main.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TabCommand
implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be executed by a player.");
            return true;
        }
        Player player = (Player)sender;
        String playerName = player.getName();
        String fileName = "/home/container/plugins/_RoKCore/PlayerData/" + playerName + ".yml";
        File playerDataFile = new File(((Main)Main.getPlugin(Main.class)).getDataFolder(), fileName);
        if (!playerDataFile.exists()) {
            player.sendMessage("Player data file not found.");
            player.sendMessage(fileName.toString());
            return true;
        }
        try {
            String line;
            BufferedReader reader = new BufferedReader(new FileReader(playerDataFile));
            StringBuilder content = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            reader.close();
            player.sendMessage(content.toString());
        }
        catch (IOException e2) {
            e2.printStackTrace();
            player.sendMessage("An error occurred while reading the player data file.");
        }
        return true;
    }
}

