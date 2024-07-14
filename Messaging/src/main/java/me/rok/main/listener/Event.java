/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.block.Action
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 */
package me.rok.main.listener;

import java.util.Arrays;
import me.rok.main.listener.LetterMenu;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Event
implements Listener,
CommandExecutor {
    public static ItemStack createCustomItem(Material material, String itemName, String ... lore) {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(itemName);
        if (lore.length > 0) {
            itemMeta.setLore(Arrays.asList(lore));
        }
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    @EventHandler
    public void PlayerInteract(PlayerInteractEvent e2) {
        if (e2.getAction().equals((Object)Action.RIGHT_CLICK_BLOCK) && e2.getClickedBlock().getType().equals((Object)Material.LIGHT_BLUE_WOOL)) {
            LetterMenu.openLetterMenu(e2.getPlayer());
        }
    }

    public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
        return false;
    }
}

