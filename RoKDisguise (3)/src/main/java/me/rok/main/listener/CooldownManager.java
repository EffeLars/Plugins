/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 */
package me.rok.main.listener;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.entity.Player;

public class CooldownManager {
    private final Map<Player, Long> cooldowns = new HashMap<Player, Long>();

    public void setCooldown(Player player, int seconds) {
        long currentTime = System.currentTimeMillis();
        long cooldownTime = currentTime + (long)seconds * 1000L;
        this.cooldowns.put(player, cooldownTime);
    }

    public boolean hasCooldown(Player player) {
        return this.cooldowns.containsKey(player) && this.cooldowns.get(player) > System.currentTimeMillis();
    }

    public int getRemainingCooldown(Player player) {
        if (this.hasCooldown(player)) {
            long remainingTime = this.cooldowns.get(player) - System.currentTimeMillis();
            return (int)Math.ceil((double)remainingTime / 1000.0);
        }
        return 0;
    }

    public void removeCooldown(Player player) {
        this.cooldowns.remove(player);
    }
}

