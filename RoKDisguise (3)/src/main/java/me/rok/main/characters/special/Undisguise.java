/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.GameProfile
 *  com.mojang.authlib.properties.Property
 *  com.mojang.authlib.properties.PropertyMap
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
 *  org.bukkit.craftbukkit.v1_16_R3.CraftWorld
 *  org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Listener
 */
package me.rok.main.characters.special;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import java.lang.reflect.Field;
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
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class Undisguise
implements CommandExecutor,
Listener {
    private static BossBar disguiseBossBar;

    public static void addDisguiseBossBar(Player player) {
        disguiseBossBar = Bukkit.createBossBar((String)ChatUtil.color("&fYou are currently disguised as &cHaldirion"), (BarColor)BarColor.WHITE, (BarStyle)BarStyle.SOLID, (BarFlag[])new BarFlag[0]);
        disguiseBossBar.addPlayer(player);
        Undisguise.updateDisguiseBossBar(player);
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
            float percentage = Undisguise.calculatePercentage(lightLevel);
            disguiseBossBar.setProgress((double)percentage);
            disguiseBossBar.setColor(Undisguise.getBossBarColor(percentage));
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

    public static void Undisguise(Player player) {
        GameProfile gameProfile = ((CraftPlayer)player).getHandle().getProfile();
        try {
            Field field = gameProfile.getClass().getDeclaredField("name");
            PropertyMap propertyMap = gameProfile.getProperties();
            field.setAccessible(true);
            player.setCustomName(player.getName());
            player.setDisplayName(player.getName());
            Property property = (Property)propertyMap.get((Object)"textures").iterator().next();
            propertyMap.remove((Object)"textures", (Object)property);
            player.updateCommands();
            CraftWorld world = (CraftWorld)player.getWorld();
            CraftPlayer craftPlayer = (CraftPlayer)player;
            EntityPlayer entityPlayer = craftPlayer.getHandle();
            entityPlayer.playerConnection.sendPacket((Packet)new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, new EntityPlayer[]{craftPlayer.getHandle()}));
            entityPlayer.playerConnection.sendPacket((Packet)new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, new EntityPlayer[]{craftPlayer.getHandle()}));
            player.teleport(player.getLocation());
            for (Player player2 : Bukkit.getOnlinePlayers()) {
                player.showPlayer(player2);
            }
            field.set(gameProfile, player.getName());
        }
        catch (Exception e2) {
            e2.printStackTrace();
        }
        for (Player player2 : Bukkit.getServer().getOnlinePlayers()) {
            player2.showPlayer(player);
        }
        player.sendMessage("Undisguised");
        player.removeScoreboardTag("Haldirion");
        player.removeScoreboardTag("Mortiferum");
        player.removeScoreboardTag("Ignatius");
        player.sendMessage(ChatUtil.color("&c&lRoK &2You are no longer disguised."));
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player)sender;
        }
        return true;
    }
}

