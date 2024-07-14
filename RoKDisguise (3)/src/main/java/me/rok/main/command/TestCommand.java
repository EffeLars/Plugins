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
 *  org.bukkit.Location
 *  org.bukkit.World
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.craftbukkit.v1_16_R3.CraftWorld
 *  org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer
 *  org.bukkit.entity.EntityType
 *  org.bukkit.entity.Parrot
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 */
package me.rok.main.command;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import me.rok.main.Main;
import me.rok.main.characters.special.Eldarion;
import me.rok.main.characters.special.Haldirion;
import me.rok.main.characters.special.Mortiferum;
import me.rok.main.characters.special.Nyxara;
import me.rok.main.characters.special.Undisguise;
import me.rok.main.items.Ignatius;
import net.minecraft.server.v1_16_R3.EntityPlayer;
import net.minecraft.server.v1_16_R3.Packet;
import net.minecraft.server.v1_16_R3.PacketPlayOutPlayerInfo;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Parrot;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class TestCommand
implements CommandExecutor {
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
            String signature = "YNk26/aNtcho9S7rJYNbmgkG07jfCA1d8VZ76hCQw87/HJItvWy3lHy5SMa4NRNyskLhin4QFBiYcqnrGQ60uVmyZSZ09SbMfgkT5y+OK6FrJU8mgowdPbSwXc7U94JDEGI3k2RPnOEB47unOYfetLdI76SsGBF7sosmJ5+rQIaHRzl8ZuSbgCZiLPXME7lwXUlJEub3AD3AaVmAQz+V2/VjrGM0es+FnbPb9HZqIGkq71aXhsqV65zd2U7V3Hjbf0izGJsn4v29wQurkkf6o7eELdxIsUMuMpRoBezMrluU9xHkvnGVnTDy4kr1S+HslBBWrGP0NJ6kekqkOeEwk0bCDEHV68cLany/tbV/YePO13bpBc34FewrfJUlX0ygvT3CxKy9AGcXKboBAsBEuL98to1iHP6mhoi9+U8PUO17Yl5+OxLH+2HBXMiIBr5XhfmDeeeva+TKt8EN3iGh90SdWv25HwJ3Em2nfXMPqvq/KLvUTKzN8Gk/ITkCWcRjkN5+XLvsohfB+LiZX6XQRNLjI8O1eH1kPadahzTTf+KbnE79qW6YbohuZst5wEEc/qC34BF+LyvHe5KypZuPUbMB5Wj0X1K8kc+ymXMuVaQ8FAOfNQprNx8N7/P/sJvyG8NW4SIq5aSteaZX/MJEEHQOxfPl05TSfe20GhGqxLw=";
            String data = "ewogICJ0aW1lc3RhbXAiIDogMTU5MjQ3NzUxOTYwMiwKICAicHJvZmlsZUlkIiA6ICJjMDQ5YzExNDBlMTE0NDhiOWI3Yzk2YTI1ZDQ3MGM1NCIsCiAgInByb2ZpbGVOYW1lIiA6ICJSZWFsTW9qYW5nXyIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9mNWJhZTc3ZmJjMjE5YTA2MjI0MmM0OWM4MjMwZWU2ZWU4ZTMzNDQ0ZDdjZWJiN2Y5OTllOTVlYTZmNGZlNjVkIiwKICAgICAgIm1ldGFkYXRhIiA6IHsKICAgICAgICAibW9kZWwiIDogInNsaW0iCiAgICAgIH0KICAgIH0KICB9Cn0=";
            pMap.remove((Object)"textures", (Object)property);
            player.setCustomName("Valeria");
            player.setDisplayName("Valeria");
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
            player.updateCommands();
        }
        catch (Exception e2) {
            e2.printStackTrace();
        }
        for (Player player2 : Bukkit.getServer().getOnlinePlayers()) {
            player2.hidePlayer(player);
            player2.showPlayer(player);
        }
        player.sendMessage("Yurfds");
        player.sendMessage("Nieuwe naam:" + gameProfile.getName());
    }

    public static void RespawnPlayer(final Player player) {
        player.spigot().respawn();
        final Location originalLocation = player.getLocation();
        World netherWorld = Bukkit.getWorld((String)"world_nether");
        Location netherLocation = new Location(netherWorld, originalLocation.getX(), originalLocation.getY(), originalLocation.getZ());
        player.teleport(netherLocation);
        player.sendMessage("Player Name: " + player.getName());
        new BukkitRunnable(){

            public void run() {
                player.teleport(originalLocation);
            }
        }.runTaskLater((Plugin)Main.getPlugin(Main.class), 20L);
    }

    public void spawnFlyingParrot(Player player) {
        Location playerLocation = player.getLocation();
        World world = player.getWorld();
        Location spawnLocation = playerLocation.add(0.0, 1.0, 0.0);
        Parrot parrot = (Parrot)world.spawnEntity(spawnLocation, EntityType.PARROT);
        Location targetLocation = this.getRandomLocationAwayFromPlayer(player, 40.0);
        parrot.setAI(false);
        Bukkit.getScheduler().runTaskTimer((Plugin)Main.getPlugin(Main.class), () -> {
            if (parrot.getLocation().distanceSquared(targetLocation) > 3.0) {
                Location currentLocation = parrot.getLocation();
                Location newLocation = currentLocation.add(targetLocation.toVector().subtract(currentLocation.toVector()).normalize().multiply(0.2));
                parrot.teleport(newLocation);
            } else {
                parrot.setAI(true);
                player.sendMessage("The parrot has reached its destination!");
                parrot.remove();
            }
        }, 0L, 1L);
    }

    private Location getRandomLocationAwayFromPlayer(Player player, double distance) {
        Location playerLocation = player.getLocation();
        World world = player.getWorld();
        Random random = new Random();
        double angle = random.nextDouble() * 2.0 * Math.PI;
        double x2 = playerLocation.getX() + distance * Math.cos(angle);
        double y2 = playerLocation.getY() + distance * Math.cos(angle);
        double z2 = playerLocation.getZ() + distance * Math.sin(angle);
        return new Location(world, x2, y2, z2);
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player)sender;
            if (args.length > 0) {
                String argument = args[0].toLowerCase();
                List<String> subArguments = Arrays.asList("haldirion", "test", "eldarion", "ignatius", "mortiferum", "nyxara", "undisguise");
                if (subArguments.contains(argument)) {
                    if (argument.equals("haldirion")) {
                        Haldirion.HaldirionDisguise(player);
                        return true;
                    }
                    if (argument.equals("ignatius")) {
                        me.rok.main.characters.special.Ignatius.addDisguiseBossBar(player);
                        me.rok.main.characters.special.Ignatius.IgnatiusDisguise(player);
                        TestCommand.RespawnPlayer(player);
                        player.getInventory().addItem(new ItemStack[]{Ignatius.IgnatiusSword});
                        return true;
                    }
                    if (argument.equals("eldarion")) {
                        Eldarion.EldarionDisguise(player);
                        Eldarion.addDisguiseBossBar(player);
                        TestCommand.RespawnPlayer(player);
                        player.getInventory().addItem(new ItemStack[]{Ignatius.IgnatiusSword});
                        return true;
                    }
                    if (argument.equals("nyxara")) {
                        Nyxara.addDisguiseBossBar(player);
                        Nyxara.NyxaraDisguise(player);
                        TestCommand.RespawnPlayer(player);
                        return true;
                    }
                    if (argument.equals("mortiferum")) {
                        Mortiferum.addDisguiseBossBar(player);
                        Mortiferum.MortiferumDisguise(player);
                        TestCommand.RespawnPlayer(player);
                        return true;
                    }
                    if (argument.equals("test")) {
                        this.spawnFlyingParrot(player);
                        player.sendMessage("tt");
                        return true;
                    }
                    if (argument.equals("undisguise")) {
                        Undisguise.Undisguise(player);
                        TestCommand.RespawnPlayer(player);
                        return true;
                    }
                    if (argument.equals("menu")) {
                        Undisguise.Undisguise(player);
                        TestCommand.RespawnPlayer(player);
                        return true;
                    }
                }
            }
        }
        return true;
    }
}

