/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.PlayerInteractEntityEvent
 */
package me.rok.main.listener.Haldirion;

import me.rok.main.listener.CooldownManager;
import me.rok.main.util.ChatUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class HaldirionHeal
implements Listener {
    private final CooldownManager cooldownManager = new CooldownManager();

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        if (!event.isCancelled() && event.getRightClicked() instanceof Player) {
            Player targetPlayer = (Player)event.getRightClicked();
            if (player.getName().equals("Haldirion") && player.getInventory().getItemInMainHand().getType() == Material.POPPY) {
                event.setCancelled(true);
                if (!this.cooldownManager.hasCooldown(player)) {
                    double targetHealth = targetPlayer.getHealth();
                    byte lightLevel = player.getLocation().getBlock().getLightLevel();
                    double healAmount = 0.0;
                    healAmount = lightLevel >= 10 ? targetHealth * 0.2 : (lightLevel >= 5 ? targetHealth * 0.1 : targetHealth * 0.05);
                    targetPlayer.setHealth(Math.min(targetHealth + healAmount, targetPlayer.getMaxHealth()));
                    targetPlayer.sendMessage(ChatUtil.color("&4&lRoK &cHaldirion used his magical nature powers to slightly heal your wounds... You feel a little bit better."));
                    player.sendMessage(ChatUtil.color("&4&lRoK &cYou used your magical nature powers to slightly heal " + targetPlayer.getName() + "'s wounds.. &4(&d" + (int)healAmount + "&4)"));
                    this.cooldownManager.setCooldown(player, 10);
                } else {
                    int cooldownTime = this.cooldownManager.getRemainingCooldown(player);
                    player.sendMessage(ChatUtil.color("&4&lRoK &cYour powers need to rest. Wait " + cooldownTime + " seconds until you can heal someone again."));
                }
            }
        }
    }
}

