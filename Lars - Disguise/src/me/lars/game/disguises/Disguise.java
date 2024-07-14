package me.lars.game.disguises;

import java.util.Iterator;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;

import me.lars.game.utils.ChatUtil;
import net.minecraft.server.v1_16_R3.EntityPlayer;
import net.minecraft.server.v1_16_R3.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_16_R3.PacketPlayOutPlayerInfo.EnumPlayerInfoAction;

public class Disguise extends ItemStack {
	 
	 public static void RespawnPlayer(Player player) {
	      player.spigot().respawn();
	      Location originalLocation = player.getLocation();
	      org.bukkit.World netherWorld = Bukkit.getWorld("world_nether");
	      Location netherLocation = new Location(netherWorld, originalLocation.getX(), originalLocation.getY(), originalLocation.getZ());
	      player.teleport(netherLocation);
	      
	   }

	
	 public static void ValerianDisguise(Player player) {
      String disguiseName = "Valerian";
      Iterator var3 = Bukkit.getOnlinePlayers().iterator();

      while(var3.hasNext()) {
         Player onlinePlayer = (Player)var3.next();
         if (onlinePlayer.getName().equalsIgnoreCase(disguiseName)) {
            player.sendMessage(ChatUtil.color("&c&lERROR! &cYou cannot use this disguise as there is already a staff member disguised as Valerian. If you think this is not true or an issue, please contact a Developer+ ASAP."));
            return;
         }
      }

      GameProfile gameProfile = ((CraftPlayer)player).getHandle().getProfile();

      try {
         java.lang.reflect.Field field = gameProfile.getClass().getDeclaredField("name");
         PropertyMap propertyMap = gameProfile.getProperties();
         field.setAccessible(true);
         EntityPlayer ePlayer = ((CraftPlayer)player).getHandle();
         GameProfile profile = ePlayer.getProfile();
         PropertyMap pMap = profile.getProperties();
         gameProfile.getId();
         UUID.randomUUID();
         Property property = (Property)pMap.get("textures").iterator().next();
         String signature = "DL9tBgCLLp+2RVGG6Ov7xaCT2pyFXKkdYXzl2Y3+5opB0LhUfJeZoEbWpewQGSJGxtfWwAlz4fAhSlwOwynjWVfnfqBzzYKFjl9r9UrFr9BKmnPJK8YSwQE1itKF2kDSG4/Q1TchdOuoSBWcTYraaGeEgx0NvSA3uP56KnYbeZgD1K9+KGtbF+F8+s6a1wZH3fThKMhQL2NXrduOqMbs8XqrckB8wM6o0qLCaOssmKJTtQ2mbBSHauzaLO1H9VTOBYAFH06Rfr/zTFl7MKBAza4crCI8xYVRDBygVYrU4GuBBuTJ/MOqC1I1zXRo59Lr8ko2lLVmg65MJhBZ+O+/Dt1MOXQBO8AyVHYl7IBV5y+rBnOFfxOuaCIhsw/cK+E16lp1KFglnSHhorpdz1BUpmtHuD3JnLoIo/0sEFmExF9rqlY3ri/j2ZlBHA5IhzmSvLCgtxygVOsWppKJMl24YK41ht/sgiDys+rKp0xrJuX+eaXiywSrryq4U+KxSb258MP1r3CeaqKdWJQLVRJPUWYVEBqKn1s0ZWZ8uJzVMn28aqbpYdXpnfRANfp7GdlxtkRdgTP2T3wW/jHXpDv7q429t8QC/QTth6JEmg86x+VClgiSBk59VLFRBR0E3tiv6PyuWlvioZEzZWTeWVS3vbqFwiPHzbtmvp8Y1hYJoy0=";
         String data = "ewogICJ0aW1lc3RhbXAiIDogMTU4ODE1MDEzNTQzMywKICAicHJvZmlsZUlkIiA6ICIyM2YxYTU5ZjQ2OWI0M2RkYmRiNTM3YmZlYzEwNDcxZiIsCiAgInByb2ZpbGVOYW1lIiA6ICIyODA3IiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzU3Yjc5MjE5YTU1MmUwMTA0MzQyMGMzYWZkNGI3MzM1MTZiMWQzNDIzYTNmODE5YjFjYjliZjg5ZmFkNTcwNWEiCiAgICB9CiAgfQp9";
         pMap.remove("textures", property);
         player.setCustomName("Valerian");
         player.setDisplayName("Valerian");
         pMap.put("textures", new Property("textures", data, signature));
         CraftWorld world = (CraftWorld)player.getWorld();
         CraftPlayer craftPlayer = (CraftPlayer)player;
         EntityPlayer entityPlayer = craftPlayer.getHandle();
         entityPlayer.playerConnection.sendPacket(new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.REMOVE_PLAYER, new EntityPlayer[]{craftPlayer.getHandle()}));
         entityPlayer.playerConnection.sendPacket(new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.ADD_PLAYER, new EntityPlayer[]{craftPlayer.getHandle()}));
         player.teleport(player.getLocation());
         Iterator var15 = Bukkit.getOnlinePlayers().iterator();

         while(var15.hasNext()) {
            Player player2 = (Player)var15.next();
            player.hidePlayer(player2);
            player.showPlayer(player2);
         }

         field.set(gameProfile, "Valerian");
         player.updateCommands();
      } catch (Exception var16) {
         var16.printStackTrace();
      }

      Iterator var20 = Bukkit.getServer().getOnlinePlayers().iterator();

      while(var20.hasNext()) {
         Player player2 = (Player)var20.next();
         player2.hidePlayer(player);
         player2.showPlayer(player);
      }

      player.sendMessage("Valerian");
      player.addScoreboardTag("Valerian");
      player.addScoreboardTag("Disguised");
      player.sendMessage(ChatUtil.color("&c&lRoK &2You are currently disguised as character: &a" + gameProfile.getName()));
   }
	}