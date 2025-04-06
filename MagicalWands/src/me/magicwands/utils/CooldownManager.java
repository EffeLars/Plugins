package me.magicwands.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;

import me.magicwands.main.Main;

public class CooldownManager {
    private final Map<UUID, Long> cooldowns = new HashMap<>();
    private final long defaultCooldown; 

    public CooldownManager(long defaultCooldown) {
        this.defaultCooldown = defaultCooldown;
    }

   
    public long getCooldownDuration(Player player, String spell) {
       
        long cooldownDuration = Main.getPlugin(Main.class).getConfig().getLong("spells." + spell + ".cooldown", defaultCooldown);
        return cooldownDuration;
    }

   
    public boolean hasCooldown(Player player, String spell) {
        if (!cooldowns.containsKey(player.getUniqueId())) {
            return false;
        }

        long timeLeft = getCooldownRemaining(player, spell);
        return timeLeft > 0;
    }

    
    public long getCooldownRemaining(Player player, String spell) {
        long currentTime = System.currentTimeMillis() / 1000;  
        long cooldownEnd = cooldowns.get(player.getUniqueId());
        return cooldownEnd - currentTime; 
    }

   
    public void startCooldown(Player player, long cooldownTimeInSeconds, String spell) {
        long cooldownEnd = System.currentTimeMillis() / 1000 + cooldownTimeInSeconds;  
        cooldowns.put(player.getUniqueId(), cooldownEnd);
    }

   
    public void setDefaultCooldownIfNotExist(long defaultCooldown, String spell) {
        String path = "spells." + spell + ".cooldown";
        if (!Main.getPlugin(Main.class).getConfig().contains(path)) {
            Main.getPlugin(Main.class).getConfig().set(path, defaultCooldown);
            Main.getPlugin(Main.class).saveConfig();
        }
    }
}
