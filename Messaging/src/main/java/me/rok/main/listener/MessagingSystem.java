/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Material
 *  org.bukkit.configuration.file.FileConfiguration
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.inventory.InventoryClickEvent
 *  org.bukkit.event.player.PlayerJoinEvent
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 */
package me.rok.main.listener;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import me.rok.main.Main;
import me.rok.main.listener.PlayerPaperData;
import me.rok.main.util.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class MessagingSystem
implements Listener {
    private static File messagesFile;
    private static FileConfiguration messagesConfig;
    private static FileConfiguration offlineMessagesConfig;
    private static FileConfiguration dataConfig;
    private static File dataFile;

    public static void setup() {
        dataFile = new File("plugins/Messaging/messages.yml");
        if (!dataFile.exists()) {
            try {
                dataFile.createNewFile();
            }
            catch (IOException e2) {
                e2.printStackTrace();
            }
        }
        dataConfig = YamlConfiguration.loadConfiguration((File)dataFile);
    }

    public MessagingSystem() {
        messagesFile = new File(((Main)Main.getPlugin(Main.class)).getDataFolder(), "messages.yml");
        if (!messagesFile.exists()) {
            ((Main)Main.getPlugin(Main.class)).saveResource("messages.yml", false);
        }
        messagesConfig = YamlConfiguration.loadConfiguration((File)messagesFile);
        File offlineMessagesFile = new File(((Main)Main.getPlugin(Main.class)).getDataFolder(), "offline_messages.yml");
        if (!offlineMessagesFile.exists()) {
            try {
                offlineMessagesFile.createNewFile();
            }
            catch (IOException e2) {
                e2.printStackTrace();
            }
        }
        offlineMessagesConfig = YamlConfiguration.loadConfiguration((File)offlineMessagesFile);
    }

    public static void sendMessage(Player sender, Player recipient, String message) {
        String formattedMessage = "[" + sender.getName() + "]: " + message;
        recipient.sendMessage(ChatUtil.color("&4&lRoK &cYou have received mail, Go to the nearest city and check your new messages. &f- &6&l+1 PAPER"));
        PlayerPaperData.setPlayerMessageAmount(recipient.getUniqueId(), PlayerPaperData.getPlayerMessageAmount(recipient.getUniqueId()) + 1);
        String recipientUUID = recipient.getUniqueId().toString();
        List recipientMessages = messagesConfig.getStringList(recipientUUID);
        recipientMessages.add(formattedMessage);
        messagesConfig.set(recipientUUID, (Object)recipientMessages);
        MessagingSystem.saveMessagesConfig();
        if (!recipient.isOnline()) {
            MessagingSystem.saveOfflineMessage(recipientUUID, formattedMessage);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        List offlineMessages;
        Player player = event.getPlayer();
        String playerUUID = player.getUniqueId().toString();
        if (MessagingSystem.hasUnreadMail(playerUUID)) {
            player.sendMessage(ChatUtil.color("&aYou have unread mail! Check your messages with /mailbox."));
        }
        if (!(offlineMessages = offlineMessagesConfig.getStringList(playerUUID)).isEmpty()) {
            for (String message : offlineMessages) {
                messagesConfig.getStringList(playerUUID).add(message);
            }
            offlineMessagesConfig.set(playerUUID, new ArrayList());
            MessagingSystem.saveMessagesConfig();
            MessagingSystem.saveOfflineMessagesConfig();
        }
    }

    public static void displayMessageMenu(Player player, int page) {
        int pageSize = 27;
        int maxPages = (messagesConfig.getStringList(player.getUniqueId().toString()).size() - 1) / pageSize + 1;
        if (page < 1 || page > maxPages) {
            player.sendMessage(ChatUtil.color("&cInvalid page number!"));
            return;
        }
        Inventory menu = Bukkit.createInventory(null, (int)27, (String)ChatUtil.color("&6Message Menu (Page " + page + ")"));
        ItemStack glassPane = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta glassPaneMeta = glassPane.getItemMeta();
        glassPaneMeta.setDisplayName(" ");
        glassPane.setItemMeta(glassPaneMeta);
        int i2 = 0;
        while (i2 < 27) {
            menu.setItem(i2, glassPane);
            ++i2;
        }
        String playerUUID = player.getUniqueId().toString();
        List playerMessages = messagesConfig.getStringList(playerUUID);
        int startIndex = (page - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, playerMessages.size());
        if (!playerMessages.isEmpty()) {
            int i3 = startIndex;
            while (i3 < endIndex) {
                String[] messageLines1;
                String message = (String)playerMessages.get(i3);
                ItemStack messageItem = new ItemStack(Material.PAPER);
                ItemMeta messageItemMeta = messageItem.getItemMeta();
                String[] messageLines = message.split("\\[n\\]");
                StringBuilder formattedMessage = new StringBuilder();
                int j2 = 0;
                while (j2 < messageLines.length) {
                    if (j2 != 0) {
                        formattedMessage.append("\n");
                    }
                    formattedMessage.append(ChatUtil.color("&8\"")).append(ChatUtil.color("&o")).append(messageLines[j2]).append(ChatUtil.color("&8\""));
                    ++j2;
                }
                ArrayList<String> lore = new ArrayList<String>();
                lore.add("");
                String[] stringArray = messageLines1 = message.split("\\[n\\]");
                int n2 = messageLines1.length;
                int n3 = 0;
                while (n3 < n2) {
                    String line = stringArray[n3];
                    lore.add(ChatUtil.color("&7&o" + line));
                    ++n3;
                }
                lore.add("");
                lore.add(ChatUtil.color("&a[READ]"));
                messageItemMeta.setLore(lore);
                Player sender = MessagingSystem.getPlayerFromMessage(message);
                String senderName = sender != null ? sender.getName() : "Unknown";
                messageItemMeta.setDisplayName(ChatUtil.color("&6Letter from " + senderName));
                messageItem.setItemMeta(messageItemMeta);
                menu.setItem(i3 % pageSize, messageItem);
                ++i3;
            }
        } else {
            ItemStack noMessagesItem = new ItemStack(Material.BARRIER);
            ItemMeta noMessagesMeta = noMessagesItem.getItemMeta();
            noMessagesMeta.setDisplayName(ChatUtil.color("&cNo messages"));
            noMessagesItem.setItemMeta(noMessagesMeta);
            menu.setItem(13, noMessagesItem);
        }
        if (page < maxPages) {
            ItemStack nextPageArrow = new ItemStack(Material.ARROW);
            ItemMeta arrowMeta = nextPageArrow.getItemMeta();
            arrowMeta.setDisplayName(ChatUtil.color("&aNext Page"));
            nextPageArrow.setItemMeta(arrowMeta);
            menu.setItem(26, nextPageArrow);
        }
        player.openInventory(menu);
    }

    public static int getMaxPages(Player player) {
        String playerUUID = player.getUniqueId().toString();
        List playerMessages = messagesConfig.getStringList(playerUUID);
        int pageSize = 27;
        return (playerMessages.size() - 1) / pageSize + 1;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().startsWith(ChatUtil.color("&6Message Menu"))) {
            String title;
            int currentPage;
            int nextPage;
            ItemStack clickedItem;
            event.setCancelled(true);
            Player player = (Player)event.getWhoClicked();
            Inventory clickedInventory = event.getClickedInventory();
            if (clickedInventory != null && clickedInventory.equals(player.getOpenInventory().getTopInventory()) && (clickedItem = event.getCurrentItem()) != null && clickedItem.getType() == Material.ARROW && (nextPage = (currentPage = MessagingSystem.getCurrentPage(title = event.getView().getTitle())) + 1) <= MessagingSystem.getMaxPages(player)) {
                MessagingSystem.displayMessageMenu(player, nextPage);
            }
        }
    }

    private static Player getPlayerFromMessage(String message) {
        int startIndex = message.indexOf("[") + 1;
        int endIndex = message.indexOf("]");
        if (startIndex != -1 && endIndex != -1 && startIndex < endIndex) {
            String senderName = message.substring(startIndex, endIndex);
            return Bukkit.getPlayerExact((String)senderName);
        }
        return null;
    }

    private static boolean hasUnreadMail(String playerUUID) {
        List playerMessages = messagesConfig.getStringList(playerUUID);
        return !playerMessages.isEmpty();
    }

    private static void saveMessagesConfig() {
        try {
            messagesConfig.save(messagesFile);
        }
        catch (IOException e2) {
            e2.printStackTrace();
        }
    }

    private static void saveOfflineMessagesConfig() {
        File offlineMessagesFile = new File(((Main)Main.getPlugin(Main.class)).getDataFolder(), "offline_messages.yml");
        try {
            offlineMessagesConfig.save(offlineMessagesFile);
        }
        catch (IOException e2) {
            e2.printStackTrace();
        }
    }

    private static void saveOfflineMessage(String playerUUID, String message) {
        List offlineMessages = offlineMessagesConfig.getStringList(playerUUID);
        offlineMessages.add(message);
        offlineMessagesConfig.set(playerUUID, (Object)offlineMessages);
        MessagingSystem.saveOfflineMessagesConfig();
    }

    private static int getCurrentPage(String title) {
        String pageString = title.substring(title.lastIndexOf(" ") + 1, title.length() - 1);
        return Integer.parseInt(pageString);
    }
}

