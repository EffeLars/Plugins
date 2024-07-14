/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.GameProfile
 *  com.mojang.authlib.properties.Property
 *  com.mojang.authlib.properties.PropertyMap
 *  com.nametagedit.plugin.NametagEdit
 *  net.md_5.bungee.api.ChatColor
 *  net.minecraft.server.v1_16_R3.EntityPlayer
 *  net.minecraft.server.v1_16_R3.Packet
 *  net.minecraft.server.v1_16_R3.PacketPlayOutPlayerInfo
 *  net.minecraft.server.v1_16_R3.PacketPlayOutPlayerInfo$EnumPlayerInfoAction
 *  org.bukkit.Bukkit
 *  org.bukkit.boss.BarColor
 *  org.bukkit.boss.BarFlag
 *  org.bukkit.boss.BarStyle
 *  org.bukkit.boss.BossBar
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.craftbukkit.v1_16_R3.CraftWorld
 *  org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Listener
 */
package me.rok.main.characters.special;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import com.nametagedit.plugin.NametagEdit;
import java.io.File;
import java.lang.reflect.Field;
import java.util.UUID;
import me.rok.main.command.TestCommand;
import me.rok.main.util.ChatUtil;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_16_R3.EntityPlayer;
import net.minecraft.server.v1_16_R3.Packet;
import net.minecraft.server.v1_16_R3.PacketPlayOutPlayerInfo;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class Eldarion
implements CommandExecutor,
Listener {
    private static BossBar disguiseBossBar;

    public static void addDisguiseBossBar(Player player) {
        disguiseBossBar = Bukkit.createBossBar((String)ChatUtil.color("&fYou are currently disguised as &4Eldarion"), (BarColor)BarColor.WHITE, (BarStyle)BarStyle.SOLID, (BarFlag[])new BarFlag[0]);
        disguiseBossBar.addPlayer(player);
        Eldarion.updateDisguiseBossBar(player);
    }

    public static void removeDisguiseBossBar(Player player) {
        if (disguiseBossBar != null) {
            disguiseBossBar.removePlayer(player);
            disguiseBossBar.setVisible(false);
            disguiseBossBar.removeAll();
        }
    }

    public static void updateDisguiseBossBar(Player player) {
        if (disguiseBossBar != null) {
            byte lightLevel = player.getLocation().getBlock().getLightLevel();
            float percentage = Eldarion.calculatePercentage(lightLevel);
            disguiseBossBar.setProgress((double)percentage);
            disguiseBossBar.setColor(Eldarion.getBossBarColor(percentage));
        }
    }

    private static BarColor getBossBarColor(float percentage) {
        if ((double)percentage >= 0.7) {
            return BarColor.GREEN;
        }
        if ((double)percentage >= 0.3) {
            return BarColor.YELLOW;
        }
        return BarColor.RED;
    }

    private static float calculatePercentage(int lightLevel) {
        float maxLightLevel = 15.0f;
        float minLightLevel = 0.0f;
        float percentage = ((float)lightLevel - minLightLevel) / (maxLightLevel - minLightLevel);
        return Math.max(0.0f, Math.min(percentage, 1.0f));
    }

    public static void sendPlayerLevelsToOnlinePlayers(Player player) {
        File pluginFolder = new File("plugins");
        File targetFolder = new File(pluginFolder, "_RoKCore/PlayerData");
        File playerFileByName = new File(targetFolder, String.valueOf(player.getName()) + ".yml");
        if (playerFileByName.exists()) {
            YamlConfiguration config = YamlConfiguration.loadConfiguration((File)playerFileByName);
            String playerLevel = config.getString("Level");
            String nameColor = config.getString("NameColor");
            if (playerLevel != null && nameColor != null) {
                String formattedLevel = "&7[&3" + playerLevel + "&7] ";
                String formattedNameColor = ChatUtil.color(nameColor.replace("\u00a7", "&"));
                String nameTag = ChatUtil.color(String.valueOf(formattedLevel) + formattedNameColor);
                NametagEdit.getApi().setPrefix(player, nameTag);
                NametagEdit.getApi().setNametag(player, nameTag, "");
            }
        }
    }

    public static void EldarionDisguise(Player player) {
        String disguiseName = "Eldarion";
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (!onlinePlayer.getName().equalsIgnoreCase(disguiseName)) continue;
            player.sendMessage(ChatUtil.color("&c&lERROR! &cYou cannot use this disguise as there is already a staff member disguised as Eldarion. If you think this is not true or an issue, please contact a Developer+ ASAP."));
            return;
        }
        GameProfile gameProfile = ((CraftPlayer)player).getHandle().getProfile();
        try {
            player.addScoreboardTag("Eldarion");
            player.addScoreboardTag("Disguised");
            Field field = gameProfile.getClass().getDeclaredField("name");
            PropertyMap propertyMap = gameProfile.getProperties();
            field.setAccessible(true);
            EntityPlayer ePlayer = ((CraftPlayer)player).getHandle();
            GameProfile profile = ePlayer.getProfile();
            PropertyMap pMap = profile.getProperties();
            gameProfile.getId();
            UUID.randomUUID();
            Property property = (Property)pMap.get((Object)"textures").iterator().next();
            String signature = "Tvn4exxEkyoDZ/9tYODoRMs5goSJLXYK/ewUPT4dmC9kyQQX6Y1jQ85laWObzYyHmBLjOQjqjaBpD/DtPVUC6PcuuCmmTb6a6/lFrMIGhCC0ifDVHmqzPKRqcRr8vq5r4YqU//rm8x7rLJSdPrbPRjkREdmZr7krcETBSUJlTX/hSDkX2vm8kJGaU3USSQGAskfJQrx7u1ewznTEXpW8aHpsjV3pnpGrwf8S2ycgGTd8ZXpfYlr9d1/mzZdu8xHdCHcKxA4ulPULteINtCBz1xjqgdlziSmqh0nH9AjkXMC+81paVwk22n0cG9FGB6z+TeO4re+o5bclg1/h5Zs3ues5usFvIGZe1sVd0b1PZh1jq8FIFK4y+kts+8a3W+bdqBqDoEPJj17/oQDyICs2modQUZN0iB4CgUB9qy4CTTKJNrGa0Yf6mBGp5eobVZdvexbb382djTCq4ZIHuI3t9wY1ZYT47MZw8MyeY14qtgzPkSv2p6v5X74VCvrb1Z1c28rve1RXQ9pH1ZMi5+jpwAfzeUOH8oGC0pFYFLkxW3N28YpDVr6K7/1tnodoFxn3RSVgtwEyHw7yOxsvB5Ikp55gdo8UMkVU2ndiW9wIbTG6jlYOm3w7OzTXdIHkgUtJ0AtswLya72fm1IKPBTRD+2Cy8obz2aKo3yh7d2uxWLk=";
            String data = "ewogICJ0aW1lc3RhbXAiIDogMTY3Njk5NTkyODY0OCwKICAicHJvZmlsZUlkIiA6ICJkZjAyYTMxZWYzYTY0ZmNlOWZmMTQyNmMzZDA5Zjg2ZCIsCiAgInByb2ZpbGVOYW1lIiA6ICJSZXphYm95ODEiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzIwNWUwYzk2NTM0MTMzYTQ1OGEyNjFiMTVlNzRmMTkyZjg3YmMwZWJmYmZkMDE2MjYwMWM0YTA4NGJlYjVmIgogICAgfQogIH0KfQ==";
            pMap.remove((Object)"textures", (Object)property);
            player.setCustomName("Eldarion");
            player.setDisplayName("Eldarion");
            pMap.put((Object)"textures", (Object)new Property("textures", data, signature));
            Eldarion.addDisguiseBossBar(player);
            TestCommand.RespawnPlayer(player);
            CraftWorld world = (CraftWorld)player.getWorld();
            CraftPlayer craftPlayer = (CraftPlayer)player;
            EntityPlayer entityPlayer = craftPlayer.getHandle();
            entityPlayer.playerConnection.sendPacket((Packet)new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, new EntityPlayer[]{craftPlayer.getHandle()}));
            entityPlayer.playerConnection.sendPacket((Packet)new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, new EntityPlayer[]{craftPlayer.getHandle()}));
            player.teleport(player.getLocation());
            for (Player player2 : Bukkit.getOnlinePlayers()) {
                player.hidePlayer(player2);
                player.showPlayer(player2);
            }
            field.set(gameProfile, "Eldarion");
            player.updateCommands();
        }
        catch (Exception e2) {
            e2.printStackTrace();
        }
        for (Player player2 : Bukkit.getServer().getOnlinePlayers()) {
            player2.hidePlayer(player);
            player2.showPlayer(player);
        }
        player.sendMessage("Eldarion");
        player.addScoreboardTag("Eldarion");
        Eldarion.sendPlayerLevelsToOnlinePlayers(player);
        player.sendMessage(ChatUtil.color("&c&lRoK &2You are currently disguised as character: &a" + gameProfile.getName()));
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player p2 = (Player)sender;
            if (command.getName().equalsIgnoreCase("eldarion")) {
                Eldarion.EldarionDisguise(p2);
            }
        } else {
            sender.sendMessage(ChatColor.RED + "You must be a player to use this command!");
        }
        return true;
    }
}

