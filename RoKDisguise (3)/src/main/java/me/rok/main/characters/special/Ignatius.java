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
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.block.Block
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
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
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

public class Ignatius
implements CommandExecutor,
Listener {
    private static BossBar disguiseBossBar;

    public static void addDisguiseBossBar(Player player) {
        disguiseBossBar = Bukkit.createBossBar((String)ChatUtil.color("&fYou are currently disguised as &2Ignatius"), (BarColor)BarColor.WHITE, (BarStyle)BarStyle.SOLID, (BarFlag[])new BarFlag[0]);
        disguiseBossBar.addPlayer(player);
        Ignatius.updateDisguiseBossBar(player);
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
            int fireBlockCount = Ignatius.countFireBlocksNearby(player, 20);
            float percentage = Ignatius.calculatePercentage(fireBlockCount);
            disguiseBossBar.setProgress((double)percentage);
            disguiseBossBar.setColor(Ignatius.getBossBarColor(percentage));
        }
    }

    private static int countFireBlocksNearby(Player player, int radius) {
        Location playerLocation = player.getLocation();
        int fireBlockCount = 0;
        int x2 = -radius;
        while (x2 <= radius) {
            int y2 = -radius;
            while (y2 <= radius) {
                int z2 = -radius;
                while (z2 <= radius) {
                    Location blockLocation = playerLocation.clone().add((double)x2, (double)y2, (double)z2);
                    Block block = blockLocation.getBlock();
                    if (block.getType() == Material.FIRE) {
                        ++fireBlockCount;
                    }
                    ++z2;
                }
                ++y2;
            }
            ++x2;
        }
        return fireBlockCount;
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

    private static float calculatePercentage(int fireBlockCount) {
        int maxFireBlockCount = 8000;
        boolean minFireBlockCount = false;
        float percentage = (float)fireBlockCount / (float)maxFireBlockCount;
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

    public static void IgnatiusDisguise(Player player) {
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
            String signature = "J4MLe1oWezPz6jINxNNZnpl6Fh/oB54qzCaa5VKcmo8PZwsA205UkQ+eNd7mPtbUpX7mP8Ah+h1ebYNZLzRQQvXtQaQTu40cQItYgbpv3nCkldhiczageRvDnTTpmkcolXZOEwcgSZlCuWvAk5X0+zpyMA4XJKmLH+D5IHydx+U13KS9y2VxAI29QAJ+gpLnoiN3t2l/anJDhcNOVCiZU9MQuFDwvMc/1dk2seDeUV7/RTnlU0sgxEQOe+tr1EhISSAuPGIYnSPsvFXBXReFsGSZyb7PszFcMYYqGqKcZEOSEnPHGIC++yTBdjwyrBalli4YkMc+HIrAArlIF0zb+nmm8Wo2Ai55/GTqD5uVVk3U05OgkDSKPlY/lRCACWWJUARBVcYoQgCpvrqZ9nYic0DI2tGNg3a96GfGWBCYPi6sheyTuJlr2KiXOJHgOIBo+ghJ0E43FENKH7jt8UER+ALS/RQnt+ZFJq2QoxwwORNCvqPUNOLH//YzLPfSkahRSO1SsP3mrT/nXmNHCVutcF8URhhlWB1mL62BB2mV6dAibl+4xfZTS4m84KMDyVLYU/Rb3g2UkVMBciK7Pkzl6xONFvG6AxYuTLxE8tMVE8EMQe3eTWIMYAPeZMunomEqQEXWWSVsLPwV9BB1+rQ+HvyfwM0P0nRkT/u1N+nFB/E=";
            String data = "ewogICJ0aW1lc3RhbXAiIDogMTU5MjQyNjQ1NTIyMywKICAicHJvZmlsZUlkIiA6ICJiMGQ3MzJmZTAwZjc0MDdlOWU3Zjc0NjMwMWNkOThjYSIsCiAgInByb2ZpbGVOYW1lIiA6ICJPUHBscyIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9lMmUxMzA4NjdlZjVkM2VlNzk1N2I3ZGIxYzBjZmM5Y2NjYmUwNjgwMDYwOWUwMTU1MzBjMTQzNzNiY2VmNmM4IgogICAgfQogIH0KfQ==";
            pMap.remove((Object)"textures", (Object)property);
            player.setCustomName("Ignatius");
            player.setDisplayName("Ignatius");
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
            field.set(gameProfile, "Ignatius");
            player.updateCommands();
        }
        catch (Exception e2) {
            e2.printStackTrace();
        }
        for (Player player2 : Bukkit.getServer().getOnlinePlayers()) {
            player2.hidePlayer(player);
            player2.showPlayer(player);
        }
        player.sendMessage("Ignatius");
        player.addScoreboardTag("Ignatius");
        Ignatius.sendPlayerLevelsToOnlinePlayers(player);
        player.sendMessage(ChatUtil.color("&c&lRoK &2You are currently disguised as character: &a" + gameProfile.getName()));
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player)sender;
        }
        return true;
    }
}

