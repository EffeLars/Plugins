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

public class Valerian
implements CommandExecutor,
Listener {
    private static BossBar disguiseBossBar;

    public static void addDisguiseBossBar(Player player) {
        disguiseBossBar = Bukkit.createBossBar((String)ChatUtil.color("&fYou are currently disguised as &cValerian"), (BarColor)BarColor.WHITE, (BarStyle)BarStyle.SOLID, (BarFlag[])new BarFlag[0]);
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

    public static void ValerianDisguise(Player player) {
        String disguiseName = "Valerian";
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (!onlinePlayer.getName().equalsIgnoreCase(disguiseName)) continue;
            player.sendMessage(ChatUtil.color("&c&lERROR! &cYou cannot use this disguise as there is already a staff member disguised as Valerian. If you think this is not true or an issue, please contact a Developer+ ASAP."));
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
            String signature = "DL9tBgCLLp+2RVGG6Ov7xaCT2pyFXKkdYXzl2Y3+5opB0LhUfJeZoEbWpewQGSJGxtfWwAlz4fAhSlwOwynjWVfnfqBzzYKFjl9r9UrFr9BKmnPJK8YSwQE1itKF2kDSG4/Q1TchdOuoSBWcTYraaGeEgx0NvSA3uP56KnYbeZgD1K9+KGtbF+F8+s6a1wZH3fThKMhQL2NXrduOqMbs8XqrckB8wM6o0qLCaOssmKJTtQ2mbBSHauzaLO1H9VTOBYAFH06Rfr/zTFl7MKBAza4crCI8xYVRDBygVYrU4GuBBuTJ/MOqC1I1zXRo59Lr8ko2lLVmg65MJhBZ+O+/Dt1MOXQBO8AyVHYl7IBV5y+rBnOFfxOuaCIhsw/cK+E16lp1KFglnSHhorpdz1BUpmtHuD3JnLoIo/0sEFmExF9rqlY3ri/j2ZlBHA5IhzmSvLCgtxygVOsWppKJMl24YK41ht/sgiDys+rKp0xrJuX+eaXiywSrryq4U+KxSb258MP1r3CeaqKdWJQLVRJPUWYVEBqKn1s0ZWZ8uJzVMn28aqbpYdXpnfRANfp7GdlxtkRdgTP2T3wW/jHXpDv7q429t8QC/QTth6JEmg86x+VClgiSBk59VLFRBR0E3tiv6PyuWlvioZEzZWTeWVS3vbqFwiPHzbtmvp8Y1hYJoy0=";
            String data = "ewogICJ0aW1lc3RhbXAiIDogMTU4ODE1MDEzNTQzMywKICAicHJvZmlsZUlkIiA6ICIyM2YxYTU5ZjQ2OWI0M2RkYmRiNTM3YmZlYzEwNDcxZiIsCiAgInByb2ZpbGVOYW1lIiA6ICIyODA3IiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzU3Yjc5MjE5YTU1MmUwMTA0MzQyMGMzYWZkNGI3MzM1MTZiMWQzNDIzYTNmODE5YjFjYjliZjg5ZmFkNTcwNWEiCiAgICB9CiAgfQp9";
            pMap.remove((Object)"textures", (Object)property);
            player.setCustomName("Valerian");
            player.setDisplayName("Valerian");
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
            field.set(gameProfile, "Valerian");
            player.updateCommands();
        }
        catch (Exception e2) {
            e2.printStackTrace();
        }
        for (Player player2 : Bukkit.getServer().getOnlinePlayers()) {
            player2.hidePlayer(player);
            player2.showPlayer(player);
        }
        player.sendMessage("Valerian");
        player.addScoreboardTag("Valerian");
        Valerian.sendPlayerLevelsToOnlinePlayers(player);
        player.addScoreboardTag("Disguised");
        player.sendMessage(ChatUtil.color("&c&lRoK &2You are currently disguised as character: &a" + gameProfile.getName()));
    }

    public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
        return false;
    }
}

