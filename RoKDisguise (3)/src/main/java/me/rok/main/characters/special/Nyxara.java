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

public class Nyxara
implements CommandExecutor,
Listener {
    private static BossBar disguiseBossBar;

    public static void addDisguiseBossBar(Player player) {
        disguiseBossBar = Bukkit.createBossBar((String)ChatUtil.color("&fYou are currently disguised as &4Nyxara"), (BarColor)BarColor.WHITE, (BarStyle)BarStyle.SOLID, (BarFlag[])new BarFlag[0]);
        disguiseBossBar.addPlayer(player);
        Nyxara.updateDisguiseBossBar(player);
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
            float percentage = Nyxara.calculatePercentage(lightLevel);
            disguiseBossBar.setProgress((double)percentage);
            disguiseBossBar.setColor(Nyxara.getBossBarColor(percentage));
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

    public static void NyxaraDisguise(Player player) {
        String disguiseName = "Nyxara";
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
                player.addScoreboardTag("Disguised");
                Property property = (Property)pMap.get((Object)"textures").iterator().next();
                String signature = "r5jY0JgxpVMQOCjGDP6fGwiguwN/6RFrore2BrH7ni44diHb0DF7tqmZRTGLFskhc21VEzX5dMguDbLW1ER9V+7gOXf+0ZGfD9NZaJoqhy+tSi1Nlz+7bE+nuI39kBtc4MHIc4xaKfEZYqKzYCUX6PAX1bn6jNgkPKhHiVgpBFyRPdvOruihL2ij1KgU+gYcrXTVD+JuWXgUN97h25se19h/G2vS+lz+7H9W/fb10Es+Qby5qJ3ENHlj2rXsFUS/NFrPkh943CHjfSldugyFN3tn09h2jy2QKybCQcdizH9hLsXRn/Z1QgGFotUql3l8ns3foJGb4cYXpqVW8SqrdbOfsNwmMuRiGFDEujGpf5kItcuPXw1IoTQOoaWJifqnNpVtedD/nkhulmmU3iVxxMhsjTz3XNsGzaxwBoH2ZRuMk/ifjA88IMzR2rewY5TyohJLhLv3Mj7ti6nAZWaq0ttgpoSZW+Xv4bRgn/BSYihD0e2RqzraBZ83QPX/wmDa+rpkrtMggU5Iw8cvaAYsKw/cvFNYNMTz5SPBCYyg4mbzplcOoGlBx2SS+bCMTyWYxtKl2NFe9r0nnL6PMOc0LsdlJTLGSzNVkTlMQ4VdPKbDLGzlrY8kZ5l59UDMgaoC4wRz6ZljKot7rEP+3kmLsZsAOtyK6AQ0ZL3jskgvfqU=";
                String data = "eyJ0aW1lc3RhbXAiOjE1NjEyMzU4ODQwMzMsInByb2ZpbGVJZCI6ImIwZDRiMjhiYzFkNzQ4ODlhZjBlODY2MWNlZTk2YWFiIiwicHJvZmlsZU5hbWUiOiJ4RmFpaUxlUiIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTgxZGMwNjIxN2M0YzUyZTliYjdlYTY2M2I2OTU2MWFmN2M3ODg1ZDM3ZmExZjZlYTQwMDRhZTBiZjg4MDZlOCJ9fX0=";
                pMap.remove((Object)"textures", (Object)property);
                player.setCustomName("Nyxara");
                player.setDisplayName("Nyxara");
                pMap.put((Object)"textures", (Object)new Property("textures", data, signature));
                Nyxara.addDisguiseBossBar(player);
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
                field.set(gameProfile, "Nyxara");
                player.updateCommands();
            }
            catch (Exception e2) {
                e2.printStackTrace();
            }
            for (Player player2 : Bukkit.getServer().getOnlinePlayers()) {
                player2.hidePlayer(player);
                player2.showPlayer(player);
            }
            player.sendMessage("Nyxara");
            player.addScoreboardTag("Nyxara");
            Nyxara.sendPlayerLevelsToOnlinePlayers(player);
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

