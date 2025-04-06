package me.magicwands.commands;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import me.magicwands.utils.ChatUtil;
import me.magicwands.utils.LogManager;

public class StaffCommands implements Listener {
	
	public static final Map<UUID, Boolean> playerParticles = new HashMap<>();
	public static final Map<UUID, Boolean> playerSounds = new HashMap<>();

	

	
	public static void toggleParticles(Player player) {
	    UUID playerId = player.getUniqueId();

	    if (playerParticles.containsKey(playerId) && playerParticles.get(playerId)) {
	        playerParticles.put(playerId, false);
	        LogManager.sendLog(ChatUtil.color(player.getName() + " &7has toggled particles &coff &7for their wand."));
	        player.sendMessage(ChatUtil.color("&7[&e&lMagic&6&lWands&7] &fYou have turned off the particle effect."));
	    } else {
	        playerParticles.put(playerId, true);
	        LogManager.sendLog(ChatUtil.color(player.getName() + " &7has toggled particles &aon &7for their wand."));
	        player.sendMessage(ChatUtil.color("&7[&e&lMagic&6&lWands&7] &fYou have turned on the particle effect."));
	    }
	}

	// Toggle Sounds
	public static void toggleSounds(Player player) {
	    UUID playerId = player.getUniqueId();

	    if (playerSounds.containsKey(playerId) && playerSounds.get(playerId)) {
	        playerSounds.put(playerId, false);
	        LogManager.sendLog(ChatUtil.color(player.getName() + " &7has toggled sounds &coff &7for their wand."));
	        player.sendMessage(ChatUtil.color("&7[&e&lMagic&6&lWands&7] &fYou have turned off the sound effect."));
	    } else {
	        playerSounds.put(playerId, true);
	        LogManager.sendLog(ChatUtil.color(player.getName() + " &7has toggled sounds &aon &7for their wand."));
	        player.sendMessage(ChatUtil.color("&7[&e&lMagic&6&lWands&7] &fYou have turned on the sound effect."));
	    }
	}


    
	
}
