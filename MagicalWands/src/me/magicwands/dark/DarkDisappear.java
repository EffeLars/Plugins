package me.magicwands.dark;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import me.magicwands.main.Main;
import me.magicwands.utils.ChatUtil;
import me.magicwands.utils.LogManager;
import me.magicwands.wands.DarkWand;

public class DarkDisappear implements Listener {
    private Map<UUID, Boolean> hiddenPlayers = new HashMap<>();

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (player.getItemInHand().getItemMeta() != null &&
                player.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(DarkWand.DarkWand.getItemMeta().getDisplayName()) &&
                event.getAction() == Action.LEFT_CLICK_AIR && me.magicwands.events.DarkWand.DarkSpell.get(player) == 6) {

            boolean isHidden = hiddenPlayers.getOrDefault(player.getUniqueId(), false);
            String color = Main.getPlugin(Main.class).getConfig().getString("wands.dark.color");

            BukkitTask darkTrailTask = Bukkit.getScheduler().runTaskTimer(Main.getPlugin(Main.class), new Runnable() {
                @Override
                public void run() {
                    spawnDisappearParticles(player);
                }
            }, 0L, 1L);

            new BukkitRunnable() {
                @Override
                public void run() {
                    darkTrailTask.cancel();

                    if (isHidden) {
                        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                            onlinePlayer.showPlayer(Main.getPlugin(Main.class), player);
                        }
                        hiddenPlayers.put(player.getUniqueId(), false);
                        player.getLocation().getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, player.getLocation(), 40, 1.0, 1.0, 1.0, 0.01);
                        player.getLocation().getWorld().spawnParticle(Particle.EXPLOSION_LARGE, player.getLocation(), 40, 1.0, 1.0, 1.0, 0.01);
                        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1.0F, 1.0F);
                        player.sendMessage(ChatUtil.color("&7[&" + color + "Magic&7] &" + color + "You are now visible for players."));
                        LogManager.sendLog(ChatUtil.color("&7[&" + color + "Magic&7] &" + color + player.getName() + " has become visible using their dark wand."));
                    } else {
                        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                            if (!onlinePlayer.hasPermission("magicwands.disappear.see")) {
                                onlinePlayer.hidePlayer(Main.getPlugin(Main.class), player);
                            } else {
                                onlinePlayer.showPlayer(Main.getPlugin(Main.class), player);
                            }
                        }
                        hiddenPlayers.put(player.getUniqueId(), true);
                        LogManager.sendLog(ChatUtil.color("&7[&" + color + "Magic&7] &" + color + player.getName() + " is now invisible for other players."));
                        player.getLocation().getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, player.getLocation(), 40, 1.0, 1.0, 1.0, 0.01);
                        player.getLocation().getWorld().spawnParticle(Particle.EXPLOSION_LARGE, player.getLocation(), 40, 1.0, 1.0, 1.0, 0.01);
                        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1.0F, 1.0F);
                        player.sendMessage(ChatUtil.color("&7[&" + color + "Magic&7] &" + color + "You are now hidden for players."));
                    }
                }
            }.runTaskLater(Main.getPlugin(Main.class), 100L);
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        hiddenPlayers.remove(e.getPlayer().getUniqueId());
    }

    @EventHandler
    public void Remove(PlayerJoinEvent event) {
        Player p = event.getPlayer();
        hiddenPlayers.remove(p.getUniqueId());
    }

    private void spawnDisappearParticles(Player player) {
        Particle.DustOptions darkColor = new Particle.DustOptions(Color.BLACK, 1.0f);
        Particle.DustOptions GrayColor = new Particle.DustOptions(Color.GRAY, 1.0f);
        player.getWorld().spawnParticle(Particle.REDSTONE, player.getLocation(), 10, 1.0, 1.0, 1.0, 0.02, (Object)darkColor);
        player.getWorld().spawnParticle(Particle.REDSTONE, player.getLocation(), 10, 1.0, 1.0, 1.0, 0.03, (Object)GrayColor);
        player.getLocation().getWorld().spawnParticle(Particle.ASH, player.getLocation(), 1, 0.0, 0.0, 0.0, 0.01);
        player.getLocation().getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, player.getLocation(), 40, 1.0, 1.0, 1.0, 0.01);
        player.getLocation().getWorld().spawnParticle(Particle.SMOKE_NORMAL, player.getLocation(), 5, 0.0, 0.0, 0.0, 0.02);
    }
}
