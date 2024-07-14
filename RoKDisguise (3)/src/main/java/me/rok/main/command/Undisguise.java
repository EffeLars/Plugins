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
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.craftbukkit.v1_16_R3.CraftWorld
 *  org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.PlayerJoinEvent
 */
package me.rok.main.command;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import java.lang.reflect.Field;
import me.rok.main.util.ChatUtil;
import net.minecraft.server.v1_16_R3.EntityPlayer;
import net.minecraft.server.v1_16_R3.Packet;
import net.minecraft.server.v1_16_R3.PacketPlayOutPlayerInfo;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class Undisguise
implements CommandExecutor,
Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (player.getScoreboardTags().contains("Haldirion") || player.getScoreboardTags().contains("Mortiferum") || player.getScoreboardTags().contains("Ignatius") || player.getScoreboardTags().contains("Nyxara") || player.getScoreboardTags().contains("Eldarion") || player.getScoreboardTags().contains("Disguised")) {
            player.removeScoreboardTag("Haldirion");
            player.removeScoreboardTag("Mortiferum");
            player.removeScoreboardTag("Ignatius");
            player.removeScoreboardTag("Nyxara");
            player.removeScoreboardTag("Eldarion");
            player.removeScoreboardTag("Disguised");
            player.setMaxHealth(20.0);
            player.setWalkSpeed(0.2f);
            player.sendMessage(ChatUtil.color("&c&lRoK &2You were still disguised when you last logged out on the server. We have reset your player stats and undisguised you."));
        }
    }

    public void ValeriaDisguise(Player player, String name) {
        GameProfile gameProfile = ((CraftPlayer)player).getHandle().getProfile();
        try {
            Field field = gameProfile.getClass().getDeclaredField("name");
            PropertyMap propertyMap = gameProfile.getProperties();
            field.setAccessible(true);
            EntityPlayer ePlayer = ((CraftPlayer)player).getHandle();
            GameProfile profile = ePlayer.getProfile();
            PropertyMap pMap = profile.getProperties();
            Property property = (Property)pMap.get((Object)"textures").iterator().next();
            String signature = "ewogICJ0aW1lc3RhbXAiIDogMTU5MjQ3NzUxOTYwMiwKICAicHJvZmlsZUlkIiA6ICJjMDQ5YzExNDBlMTE0NDhiOWI3Yzk2YTI1ZDQ3MGM1NCIsCiAgInByb2ZpbGVOYW1lIiA6ICJSZWFsTW9qYW5nXyIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9mNWJhZTc3ZmJjMjE5YTA2MjI0MmM0OWM4MjMwZWU2ZWU4ZTMzNDQ0ZDdjZWJiN2Y5OTllOTVlYTZmNGZlNjVkIiwKICAgICAgIm1ldGFkYXRhIiA6IHsKICAgICAgICAibW9kZWwiIDogInNsaW0iCiAgICAgIH0KICAgIH0KICB9Cn0=";
            String data = "YNk26/aNtcho9S7rJYNbmgkG07jfCA1d8VZ76hCQw87/HJItvWy3lHy5SMa4NRNyskLhin4QFBiYcqnrGQ60uVmyZSZ09SbMfgkT5y+OK6FrJU8mgowdPbSwXc7U94JDEGI3k2RPnOEB47unOYfetLdI76SsGBF7sosmJ5+rQIaHRzl8ZuSbgCZiLPXME7lwXUlJEub3AD3AaVmAQz+V2/VjrGM0es+FnbPb9HZqIGkq71aXhsqV65zd2U7V3Hjbf0izGJsn4v29wQurkkf6o7eELdxIsUMuMpRoBezMrluU9xHkvnGVnTDy4kr1S+HslBBWrGP0NJ6kekqkOeEwk0bCDEHV68cLany/tbV/YePO13bpBc34FewrfJUlX0ygvT3CxKy9AGcXKboBAsBEuL98to1iHP6mhoi9+U8PUO17Yl5+OxLH+2HBXMiIBr5XhfmDeeeva+TKt8EN3iGh90SdWv25HwJ3Em2nfXMPqvq/KLvUTKzN8Gk/ITkCWcRjkN5+XLvsohfB+LiZX6XQRNLjI8O1eH1kPadahzTTf+KbnE79qW6YbohuZst5wEEc/qC34BF+LyvHe5KypZuPUbMB5Wj0X1K8kc+ymXMuVaQ8FAOfNQprNx8N7/P/sJvyG8NW4SIq5aSteaZX/MJEEHQOxfPl05TSfe20GhGqxLw=";
            pMap.remove((Object)"textures", (Object)property);
            pMap.put((Object)"textures", (Object)new Property("textures", data, signature));
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
            field.set(gameProfile, "Valeria");
        }
        catch (Exception e2) {
            e2.printStackTrace();
        }
        for (Player player2 : Bukkit.getServer().getOnlinePlayers()) {
            player2.hidePlayer(player);
            player2.showPlayer(player);
        }
        player.sendMessage("Yur");
        player.sendMessage("Nieuwe naam:" + gameProfile.getName());
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player p2 = (Player)sender;
            this.ValeriaDisguise(p2, label);
        }
        return true;
    }
}

