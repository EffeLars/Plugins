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
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
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
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Mortiferum
implements CommandExecutor,
Listener {
    private static BossBar disguiseBossBar;

    public static void addDisguiseBossBar(Player player) {
        disguiseBossBar = Bukkit.createBossBar((String)ChatUtil.color("&fYou are currently disguised as &2Mortiferum"), (BarColor)BarColor.WHITE, (BarStyle)BarStyle.SOLID, (BarFlag[])new BarFlag[0]);
        disguiseBossBar.addPlayer(player);
        Mortiferum.updateDisguiseBossBar(player);
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
            float percentage = Mortiferum.calculatePercentage(lightLevel);
            disguiseBossBar.setProgress((double)percentage);
            disguiseBossBar.setColor(Mortiferum.getBossBarColor(percentage));
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

    public static void MortiferumDisguise(Player player) {
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
            String signature = "hgd4C+iv7mcz6wkopIqtGsMjwnoYVI3Z41n2uaBHCIx6QNqsKQM72OKXUZcY2agVlcjqPhDIARkn9xGX6lowVwIHJCxz+cMvdDMoiPTEP2pEYX1PZ8evNFt5dFg2wYut7Qk34zYSJMj4AYWvIQzKgA4b0wpoXdNavwi4SbuR89Vz01/vuOuNMI+BQvdVvpC6D08Rhr5zK0XLnJJfFXbhVtr6jJznjd4Mn9ape3+K7MB4Gq2BlvxR2StgWNEXaqSZoCGNZGX0wnypg3+dLHBNant4MpS20O9W+7U9fyfUbsyXiXRbYENzGXwUUwHVMSd3hNIAAwKTA/zu/If9RtudkznpWg74O3yq2hpCkDVeyZssuvf2sUWgj+6n0fgZPPgo8MJkGauW4/IpILoeXeT5v5uKuhlWY1Bu81DUq8C28QFoEikeiYbmyPvugUs/+BEFMQ+c7uEj4ioaIQQ7leBwgL9tt0T+WjYGDoCLEThW4UMTdXCO2T9JA5j0hK3ksgYM5Wrkt1ILl7fA5mroWpdrkQWaWSgMyGBWU8kZn0XIsh05N81Qyal3ara7OMiQw2M3KPyuLgvtYjUVdMKtGjS92TuiHXhHU2aCqYt/QM7ZpC5HV7Zm2Ec3+qOBXhYIjoyK31FlPXsDZ65p0kJLvS+kTYGyMdyPQGgH+E6ePYnMgR8=";
            String data = "ewogICJ0aW1lc3RhbXAiIDogMTY0ODM2NTM1ODk1NSwKICAicHJvZmlsZUlkIiA6ICJjMTNkYzkxZjg1YjA0ZWM4OGU2NDk5YzdjZDc4Zjk3MSIsCiAgInByb2ZpbGVOYW1lIiA6ICJjcnlwdGljX2Zyb2dfIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2M0Mzc2YjBkMzliZGQ1ZTc5YTEwYWFlZmJlZGQ5MjRlMjQ3N2E4YzQwNWU5ZDljZDJiMDFkMTg3ZDhiYWMxZjYiCiAgICB9CiAgfQp9";
            pMap.remove((Object)"textures", (Object)property);
            player.setCustomName("Mortiferum");
            player.setDisplayName("Mortiferum");
            player.addScoreboardTag("Disguised");
            TestCommand.RespawnPlayer(player);
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
            field.set(gameProfile, "Mortiferum");
            player.updateCommands();
        }
        catch (Exception e2) {
            e2.printStackTrace();
        }
        for (Player player2 : Bukkit.getServer().getOnlinePlayers()) {
            player2.hidePlayer(player);
            player2.showPlayer(player);
        }
        player.sendMessage("Mortiferum");
        player.addScoreboardTag("Mortiferum");
        Mortiferum.sendPlayerLevelsToOnlinePlayers(player);
        PotionEffect regenEffect = new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 99999, 3);
        player.addPotionEffect(regenEffect);
        player.sendMessage(ChatUtil.color("&c&lRoK &2You are currently disguised as character: &a" + gameProfile.getName()));
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player)sender;
        }
        return true;
    }
}

