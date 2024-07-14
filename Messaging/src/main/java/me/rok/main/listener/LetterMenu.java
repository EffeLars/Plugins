/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  net.md_5.bungee.api.ChatColor
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.Sound
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.block.Action
 *  org.bukkit.event.inventory.InventoryClickEvent
 *  org.bukkit.event.player.AsyncPlayerChatEvent
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.java.JavaPlugin
 */
package me.rok.main.listener;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import me.rok.main.Main;
import me.rok.main.listener.MessagingSystem;
import me.rok.main.listener.PlayerPaperData;
import me.rok.main.util.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class LetterMenu
implements Listener {
    public static Map<Player, LetterData> letterDataMap;
    private final Random random;
    private Random random1 = new Random();

    public LetterMenu() {
        letterDataMap = new HashMap<Player, LetterData>();
        this.random = new Random();
    }

    public void registerEvents(JavaPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents((Listener)this, (Plugin)plugin);
    }

    @EventHandler
    public void MorePaper(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getAction() == Action.RIGHT_CLICK_AIR && player.getItemInHand().getType() == Material.PAPER && player.getItemInHand().getItemMeta().getDisplayName().equals(net.md_5.bungee.api.ChatColor.GOLD + "Writing Paper")) {
            PlayerPaperData.setPlayerMessageAmount(player.getUniqueId(), PlayerPaperData.getPlayerMessageAmount(player.getUniqueId()) + event.getPlayer().getItemInHand().getAmount());
            player.sendMessage(ChatUtil.color("&4&lRoK &cYou have added &4" + player.getItemInHand().getAmount() + " &cpaper to your mailbox, You can now write &4" + PlayerPaperData.getPlayerMessageAmount(player.getUniqueId()) + " &cletters&4&l!"));
            player.getItemInHand().setAmount(0);
            player.playSound(player.getLocation(), Sound.ITEM_BOOK_PAGE_TURN, 1.0f, 1.0f);
            player.playSound(player.getLocation(), Sound.ITEM_BOOK_PUT, 1.0f, 1.0f);
            player.playSound(player.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1.0f, 1.0f);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player)event.getWhoClicked();
        Inventory inventory = event.getClickedInventory();
        ItemStack clickedItem = event.getCurrentItem();
        if (inventory == null || clickedItem == null) {
            return;
        }
        if (event.getView().getTitle().equals(ChatColor.GOLD + "Letter Menu")) {
            ItemMeta clickedItemMeta;
            event.setCancelled(true);
            if (clickedItem.getType() == Material.PAPER && (clickedItemMeta = clickedItem.getItemMeta()) != null) {
                String displayName = clickedItemMeta.getDisplayName();
                if (displayName.equals(ChatColor.YELLOW + "Send Letter")) {
                    if (PlayerPaperData.getPlayerMessageAmount(player.getUniqueId()) >= 1) {
                        player.sendMessage(ChatUtil.color("&4&lRoK &cYou start writing your letter.. Write on the letter &c(&7Chat&c) the message you want to write on your letter. If you want to stop writing you can type &4'cancel' &con the letter."));
                        letterDataMap.put(player, new LetterData(LetterAction.SEND_LETTER));
                        player.closeInventory();
                        return;
                    }
                } else if (displayName.equals(ChatColor.GREEN + "Read Letters")) {
                    MessagingSystem.displayMessageMenu(player, 1);
                } else if (displayName.equals(ChatColor.BLUE + "Buy Letters")) {
                    player.sendMessage(ChatColor.BLUE + "This feature is still in progress.");
                }
            }
        }
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();
        if (letterDataMap.containsKey(player)) {
            event.setCancelled(true);
            if (message.equalsIgnoreCase("cancel")) {
                player.sendMessage(ChatUtil.color("&4"));
                player.sendMessage(ChatUtil.color("&4&lRoK &cCancelled writing the letter.. We have cleaned the paper and put it back in the mailbox!"));
                player.sendMessage(ChatUtil.color("&4"));
                letterDataMap.remove(player);
                return;
            }
            LetterData letterData = letterDataMap.get(player);
            switch (letterData.getAction()) {
                case SEND_LETTER: {
                    player.sendMessage(ChatUtil.color("&4"));
                    player.sendMessage(ChatUtil.color("&4&lRoK &cYou finished writing your letter, Now put in the chat the name of the individual you are trying to reach with this letter. &c&lKeep in mind: &cThis individual has to be online."));
                    player.sendMessage(ChatUtil.color("&4"));
                    letterData.setAction(LetterAction.SET_RECIPIENT);
                    letterData.setMessage(message);
                    break;
                }
                case SET_RECIPIENT: {
                    Player recipient = Bukkit.getPlayerExact((String)message);
                    if (recipient != null) {
                        String formattedMessage = this.formatMessage(letterData.getMessage());
                        PlayerPaperData.setPlayerMessageAmount(player.getUniqueId(), PlayerPaperData.getPlayerMessageAmount(player.getUniqueId()) - 1);
                        player.sendMessage(ChatUtil.color("&4"));
                        player.sendMessage(ChatUtil.color("&4&lRoK &cYou see your bird fly off with the letter, We will send confirmation when your bird has arrived and gave the letter to the individual."));
                        player.sendMessage(ChatUtil.color("&4"));
                        double distance = player.getLocation().distance(recipient.getLocation());
                        int delaySeconds = (int)(distance * 2.0);
                        delaySeconds = Math.max(delaySeconds, 10);
                        delaySeconds = Math.min(delaySeconds, 30);
                        player.sendMessage(ChatUtil.color("&4"));
                        player.sendMessage(ChatUtil.color("&4&lRoK &cIt will take your bird approximately " + delaySeconds + " seconds to reach the recipient's location."));
                        player.sendMessage(ChatUtil.color("&4"));
                        Bukkit.getScheduler().runTaskLater((Plugin)Main.getPlugin(Main.class), () -> {
                            MessagingSystem.sendMessage(player, recipient, formattedMessage);
                            player.sendMessage(ChatUtil.color("&4"));
                            player.sendMessage(ChatUtil.color("&4&lRoK &cYour bird has arrived at the correct location and gave your letter to the individual&c&l!"));
                            player.sendMessage(ChatUtil.color("&4"));
                        }, (long)(delaySeconds * 20));
                    } else {
                        player.sendMessage(ChatUtil.color("&4"));
                        player.sendMessage(ChatUtil.color("&4&lRoK &cWe cannot find this individual in this realm, Are you we are talking about the same person&c&l?"));
                        player.sendMessage(ChatUtil.color("&4"));
                    }
                    letterDataMap.remove(player);
                }
            }
        }
    }

    public static void openLetterMenu(Player player) {
        Inventory menu = Bukkit.createInventory(null, (int)27, (String)(ChatColor.GOLD + "Letter Menu"));
        ItemStack sendLetter = LetterMenu.createItem(Material.PAPER, ChatColor.YELLOW + "Send Letter", null);
        ItemStack readLetters = LetterMenu.createItem(Material.PAPER, ChatColor.GREEN + "Read Letters", null);
        ItemStack buyLetters = LetterMenu.createItem(Material.PAPER, ChatColor.BLUE + "Your amount of Letters you can write", net.md_5.bungee.api.ChatColor.YELLOW + "You currently have enough paper to write " + net.md_5.bungee.api.ChatColor.GOLD + PlayerPaperData.getPlayerMessageAmount(player.getUniqueId()) + net.md_5.bungee.api.ChatColor.YELLOW + " letters!");
        menu.setItem(10, sendLetter);
        menu.setItem(13, readLetters);
        menu.setItem(16, buyLetters);
        player.openInventory(menu);
    }

    private static ItemStack createItem(Material material, String displayName, String string) {
        ItemStack item = new ItemStack(material);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(displayName);
        if (string != null) {
            itemMeta.setLore(Arrays.asList(string));
        }
        item.setItemMeta(itemMeta);
        return item;
    }

    private String formatMessage(String message) {
        StringBuilder formattedMessage = new StringBuilder();
        int maxLength = 60;
        int index = 0;
        while (index < message.length()) {
            if (index + maxLength < message.length()) {
                int spaceIndex = message.lastIndexOf(32, index + maxLength);
                int commaIndex = message.lastIndexOf(44, index + maxLength);
                int dotIndex = message.lastIndexOf(46, index + maxLength);
                int splitIndex = Math.max(Math.max(spaceIndex, commaIndex), dotIndex);
                if (splitIndex == -1 || splitIndex < index) {
                    splitIndex = index + maxLength;
                }
                formattedMessage.append(message.substring(index, splitIndex)).append("[n]");
                index = splitIndex + 1;
                continue;
            }
            formattedMessage.append(message.substring(index));
            break;
        }
        return formattedMessage.toString();
    }

    private static enum LetterAction {
        SEND_LETTER,
        SET_RECIPIENT;

    }

    private static class LetterData {
        private LetterAction action;
        private String message;

        public LetterData(LetterAction action) {
            this.action = action;
        }

        public LetterAction getAction() {
            return this.action;
        }

        public void setAction(LetterAction action) {
            this.action = action;
        }

        public String getMessage() {
            return this.message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}

