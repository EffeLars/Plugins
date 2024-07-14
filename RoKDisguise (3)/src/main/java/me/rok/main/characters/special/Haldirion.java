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

public class Haldirion
implements CommandExecutor,
Listener {
    private static BossBar disguiseBossBar;

    public static void addDisguiseBossBar(Player player) {
        disguiseBossBar = Bukkit.createBossBar((String)ChatUtil.color("&fYou are currently disguised as &cHaldirion"), (BarColor)BarColor.WHITE, (BarStyle)BarStyle.SOLID, (BarFlag[])new BarFlag[0]);
        disguiseBossBar.addPlayer(player);
        Haldirion.updateDisguiseBossBar(player);
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
            float percentage = Haldirion.calculatePercentage(lightLevel);
            disguiseBossBar.setProgress((double)percentage);
            disguiseBossBar.setColor(Haldirion.getBossBarColor(percentage));
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

    public static void HaldirionDisguise(Player player) {
        String disguiseName = "Haldirion";
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (onlinePlayer.getName().equalsIgnoreCase(disguiseName)) {
                player.sendMessage(ChatUtil.color("&c&lERROR! &cYou cannot use this disguise as there is already a staffmember disguised as Haldirion, If you think this is not true or an issue, Please contact an Developer+ ASAP."));
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
                String signature = "YNk26/aNtcho9S7rJYNbmgkG07jfCA1d8VZ76hCQw87/HJItvWy3lHy5SMa4NRNyskLhin4QFBiYcqnrGQ60uVmyZSZ09SbMfgkT5y+OK6FrJU8mgowdPbSwXc7U94JDEGI3k2RPnOEB47unOYfetLdI76SsGBF7sosmJ5+rQIaHRzl8ZuSbgCZiLPXME7lwXUlJEub3AD3AaVmAQz+V2/VjrGM0es+FnbPb9HZqIGkq71aXhsqV65zd2U7V3Hjbf0izGJsn4v29wQurkkf6o7eELdxIsUMuMpRoBezMrluU9xHkvnGVnTDy4kr1S+HslBBWrGP0NJ6kekqkOeEwk0bCDEHV68cLany/tbV/YePO13bpBc34FewrfJUlX0ygvT3CxKy9AGcXKboBAsBEuL98to1iHP6mhoi9+U8PUO17Yl5+OxLH+2HBXMiIBr5XhfmDeeeva+TKt8EN3iGh90SdWv25HwJ3Em2nfXMPqvq/KLvUTKzN8Gk/ITkCWcRjkN5+XLvsohfB+LiZX6XQRNLjI8O1eH1kPadahzTTf+KbnE79qW6YbohuZst5wEEc/qC34BF+LyvHe5KypZuPUbMB5Wj0X1K8kc+ymXMuVaQ8FAOfNQprNx8N7/P/sJvyG8NW4SIq5aSteaZX/MJEEHQOxfPl05TSfe20GhGqxLw=";
                String data = "ewogICJ0aW1lc3RhbXAiIDogMTU5MjQ3NzUxOTYwMiwKICAicHJvZmlsZUlkIiA6ICJjMDQ5YzExNDBlMTE0NDhiOWI3Yzk2YTI1ZDQ3MGM1NCIsCiAgInByb2ZpbGVOYW1lIiA6ICJSZWFsTW9qYW5nXyIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9mNWJhZTc3ZmJjMjE5YTA2MjI0MmM0OWM4MjMwZWU2ZWU4ZTMzNDQ0ZDdjZWJiN2Y5OTllOTVlYTZmNGZlNjVkIiwKICAgICAgIm1ldGFkYXRhIiA6IHsKICAgICAgICAibW9kZWwiIDogInNsaW0iCiAgICAgIH0KICAgIH0KICB9Cn0=";
                pMap.remove((Object)"textures", (Object)property);
                player.setCustomName("Haldirion");
                player.setDisplayName("Haldirion");
                pMap.put((Object)"textures", (Object)new Property("textures", data, signature));
                Haldirion.addDisguiseBossBar(player);
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
                field.set(gameProfile, "Haldirion");
                player.updateCommands();
            }
            catch (Exception e2) {
                e2.printStackTrace();
            }
            for (Player player2 : Bukkit.getServer().getOnlinePlayers()) {
                player2.hidePlayer(player);
                player2.showPlayer(player);
            }
            player.sendMessage("Haldirion");
            player.addScoreboardTag("Haldirion");
            Haldirion.sendPlayerLevelsToOnlinePlayers(player);
            player.addScoreboardTag("Disguised");
            player.sendMessage(ChatUtil.color("&c&lRoK &2You are currently disguised as character: &a" + gameProfile.getName()));
        }
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player)sender;
        }
        return true;
    }
}

