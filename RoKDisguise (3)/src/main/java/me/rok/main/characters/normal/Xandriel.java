/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.GameProfile
 *  com.mojang.authlib.properties.Property
 *  com.mojang.authlib.properties.PropertyMap
 *  com.nametagedit.plugin.NametagEdit
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
package me.rok.main.characters.normal;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import com.nametagedit.plugin.NametagEdit;
import java.io.File;
import java.lang.reflect.Field;
import java.util.UUID;
import me.rok.main.command.TestCommand;
import me.rok.main.util.ChatUtil;
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

public class Xandriel
implements CommandExecutor,
Listener {
    private static BossBar disguiseBossBar;

    public static void addDisguiseBossBar(Player player) {
        disguiseBossBar = Bukkit.createBossBar((String)ChatUtil.color("&fYou are currently disguised as &cXandriel"), (BarColor)BarColor.WHITE, (BarStyle)BarStyle.SOLID, (BarFlag[])new BarFlag[0]);
        disguiseBossBar.addPlayer(player);
    }

    public static void removeDisguiseBossBar(Player player) {
        if (disguiseBossBar != null) {
            disguiseBossBar.removePlayer(player);
            disguiseBossBar.setVisible(false);
            disguiseBossBar.removeAll();
        }
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

    public static void XandrielDisguise(Player player) {
        String disguiseName = "Xandriel";
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (!onlinePlayer.getName().equalsIgnoreCase(disguiseName)) continue;
            player.sendMessage(ChatUtil.color("&c&lERROR! &cYou cannot use this disguise as there is already a staff member disguised as Xandriel. If you think this is not true or an issue, please contact a Developer+ ASAP."));
            return;
        }
        GameProfile gameProfile = ((CraftPlayer)player).getHandle().getProfile();
        try {
            Field field = gameProfile.getClass().getDeclaredField("name");
            PropertyMap propertyMap = gameProfile.getProperties();
            field.setAccessible(true);
            EntityPlayer ePlayer = ((CraftPlayer)player).getHandle();
            GameProfile profile = ePlayer.getProfile();
            PropertyMap pMap = profile.getProperties();
            gameProfile.getId();
            UUID.randomUUID();
            Property property = (Property)pMap.get((Object)"textures").iterator().next();
            String signature = "N7yTyXeVSSdHZcTCg94GsSugxc31g5EjJuhHBgR8HZ5ysby0LjWql2B+U9ExSIG3VXMIwoz8OTbekTXbkgg98fLK1FKM54tUuCAgCjIxJnltc1rvXl7mdINRjFdNv3Pki4BIG9ByRHlLRJ5VR6c/Tpk90bgwkY2UfnYAXAxZyTnJgE0HfTTWsI6J/cC/6bGMB0Tqk6XwwOlvcjatdCOEaQqrlu/nO7ZBxyvHMIM9RtE3JESx1urjHAhp0EA3SF9lpVqnlmE8smqOyldu9EgVfxn/di0WnSAs+ydRfv2+Q4xGO8AMmGk/nN+H1gX3+XTipd0HlF/2C4ZexFasGX5X+sRn2PBxLj1aUbcqEheW1bnsT2Ii6YYaSKUlLQYh8dXQOXZvuT1vAN/aTafFr/k4IvzBkTtqly0/j7I1AJacARUV40jJM8uofygZKD4xzqgDdfh7ax2JI0QD0MYnk+Hza/JSodFVwsl4wLE8f7JSy4KsBr2Jj9afVDpxa6VgQlXe7CjlTWYBTbHGn6BfJl1DNfQg2CMpvj0/vTUFtWwjsodG02+6nxg1PEUtJVLYhqg6MxoA4w5BJzdrPDByb8NtqqqqQmCOBs8+JQRlVdCNmVBpNjX6/q1IR5TKprRkUTAAe+lAyytCV5a7o1m7XiZBqsF9DTYoPVK2Z4cekuMb0oA=";
            String data = "ewogICJ0aW1lc3RhbXAiIDogMTU5ODIwODQzOTE3MSwKICAicHJvZmlsZUlkIiA6ICJjMjY1MDY1NjBmZmI0ZDY5YTg2ODM1NTM3ZjczYTIyOSIsCiAgInByb2ZpbGVOYW1lIiA6ICJFbmRlcm1hbk82IiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzY4Y2VjZWUxYjE2YzI2MTYyNWIzM2E3NTViZWE1Nzg1ODBiY2YzZjZkMjI2NWZiOGQ2YzJkNWE5NTVhNWZiYWQiCiAgICB9CiAgfQp9";
            pMap.remove((Object)"textures", (Object)property);
            player.setCustomName("Xandriel");
            player.setDisplayName("Xandriel");
            pMap.put((Object)"textures", (Object)new Property("textures", data, signature));
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
            field.set(gameProfile, "Xandriel");
            player.updateCommands();
        }
        catch (Exception e2) {
            e2.printStackTrace();
        }
        for (Player player2 : Bukkit.getServer().getOnlinePlayers()) {
            player2.hidePlayer(player);
            player2.showPlayer(player);
        }
        player.sendMessage("Xandriel");
        player.addScoreboardTag("Xandriel");
        Xandriel.sendPlayerLevelsToOnlinePlayers(player);
        player.addScoreboardTag("Disguised");
        player.sendMessage(ChatUtil.color("&c&lRoK &2You are currently disguised as character: &a" + gameProfile.getName()));
    }

    public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
        return false;
    }
}

