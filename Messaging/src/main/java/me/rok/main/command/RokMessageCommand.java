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

import java.util.UUID;
import me.rok.main.listener.PlayerPaperData;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RokMessageCommand
implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be executed by players.");
            return true;
        }
        if (args.length < 3) {
            sender.sendMessage("Usage: /rokmessage paper give <player> <amount>");
            sender.sendMessage("Usage: /rokmessage paper remove <player> <amount>");
            sender.sendMessage("Usage: /rokmessage paper set <player> <amount>");
            return true;
        }
        Player player = (Player)sender;
        UUID playerUUID = player.getUniqueId();
        String action = args[1];
        if (action.equalsIgnoreCase("give")) {
            if (args.length < 4) {
                player.sendMessage("Usage: /rokmessage paper give <player> <amount>");
                return true;
            }
            String targetPlayerName = args[2];
            Player targetPlayer = Bukkit.getPlayer((String)targetPlayerName);
            if (targetPlayer == null) {
                player.sendMessage("The specified player is not online.");
                return true;
            }
            int amount = Integer.parseInt(args[3]);
            int currentAmount = PlayerPaperData.getPlayerMessageAmount(targetPlayer.getUniqueId());
            PlayerPaperData.setPlayerMessageAmount(targetPlayer.getUniqueId(), currentAmount + amount);
            player.sendMessage("You have given " + amount + " message(s) to " + targetPlayer.getName() + ". Total: " + (currentAmount + amount));
            targetPlayer.sendMessage("You have received " + amount + " message(s) from " + player.getName() + ". Total: " + (currentAmount + amount));
        } else if (action.equalsIgnoreCase("remove")) {
            if (args.length < 4) {
                player.sendMessage("Usage: /rokmessage paper remove <player> <amount>");
                return true;
            }
            String targetPlayerName = args[2];
            Player targetPlayer = Bukkit.getPlayer((String)targetPlayerName);
            if (targetPlayer == null) {
                player.sendMessage("The specified player is not online.");
                return true;
            }
            int amount = Integer.parseInt(args[3]);
            int currentAmount = PlayerPaperData.getPlayerMessageAmount(targetPlayer.getUniqueId());
            if (currentAmount >= amount) {
                PlayerPaperData.setPlayerMessageAmount(targetPlayer.getUniqueId(), currentAmount - amount);
                player.sendMessage("You have removed " + amount + " message(s) from " + targetPlayer.getName() + ". Total: " + (currentAmount - amount));
                targetPlayer.sendMessage("You have lost " + amount + " message(s) removed by " + player.getName() + ". Total: " + (currentAmount - amount));
            } else {
                player.sendMessage(String.valueOf(targetPlayer.getName()) + " doesn't have enough messages to remove.");
            }
        } else if (action.equalsIgnoreCase("set")) {
            if (args.length < 4) {
                player.sendMessage("Usage: /rokmessage paper set <player> <amount>");
                return true;
            }
            String targetPlayerName = args[2];
            Player targetPlayer = Bukkit.getPlayer((String)targetPlayerName);
            if (targetPlayer == null) {
                player.sendMessage("The specified player is not online.");
                return true;
            }
            int amount = Integer.parseInt(args[3]);
            PlayerPaperData.setPlayerMessageAmount(targetPlayer.getUniqueId(), amount);
            player.sendMessage("You have set " + targetPlayer.getName() + "'s message count to: " + amount);
            targetPlayer.sendMessage(String.valueOf(player.getName()) + " has set your message count to: " + amount);
        } else {
            player.sendMessage("Invalid action. Usage: /rokmessage paper (give/remove/set) <player> <amount>");
        }
        return true;
    }
}

