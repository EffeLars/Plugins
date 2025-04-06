
package me.magicwands.dark;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import me.magicwands.events.DarkWand;
import me.magicwands.main.Main;
import me.magicwands.utils.ChatUtil;

public class DarkAura implements Listener {
    private final Map<UUID, BukkitTask> darkAuraTasks = new HashMap<>(); // Store tasks per player
    private final Map<UUID, Boolean> darkAuraPlayers = new HashMap<>(); // Track aura status per player

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();

        if (player.getItemInHand().getItemMeta() != null &&
                player.getItemInHand().getItemMeta().getDisplayName() != null &&
                player.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(me.magicwands.wands.DarkWand.DarkWand.getItemMeta().getDisplayName()) &&
                event.getAction() == Action.LEFT_CLICK_AIR &&
                DarkWand.DarkSpell.get(player) == 7) {

            boolean hasAura = darkAuraPlayers.getOrDefault(playerId, false);

            if (hasAura) {
              
                darkAuraPlayers.put(playerId, false);

                if (darkAuraTasks.containsKey(playerId)) {
                    BukkitTask task = darkAuraTasks.get(playerId);
                    if (task != null) {
                        task.cancel();
                    }
                    darkAuraTasks.remove(playerId);
                }

                String color = Main.getPlugin(Main.class).getConfig().getString("wands.dark.color");
                player.sendMessage(ChatUtil.color("&7[&" + color + "Magic&7] &" + color + "Stopped the dark aura.."));
            } else {
               
                darkAuraPlayers.put(playerId, true);
                String color = Main.getPlugin(Main.class).getConfig().getString("wands.dark.color");
                player.sendMessage(ChatUtil.color("&7[&" + color + "Magic&7] &" + color + "Started the dark aura.."));

                BukkitTask task = new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (!darkAuraPlayers.getOrDefault(playerId, false)) {
                            this.cancel();
                            darkAuraTasks.remove(playerId);
                            return;
                        }

                        SummonDarkAura(player.getLocation(), 4);
                        for (Entity entity : player.getLocation().getWorld().getNearbyEntities(player.getLocation(), 4, 4, 4)) {
                            if (!(entity instanceof Player) || entity == player) continue;
                            Player nearbyPlayer = (Player) entity;

                            Vector direction = nearbyPlayer.getLocation().getDirection().normalize().multiply(-1);
                            nearbyPlayer.setVelocity(direction.multiply(-2));
                            nearbyPlayer.playSound(nearbyPlayer.getLocation(), Sound.ENTITY_WITHER_HURT, 1, 1);
                            nearbyPlayer.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 2));
                            nearbyPlayer.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 100, 2));
                            nearbyPlayer.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 300, 2));
                        }
                    }
                }.runTaskTimer(Main.getPlugin(Main.class), 0L, 10L); 

                darkAuraTasks.put(playerId, task);
            }
        }
    }

    @EventHandler
    public void RemoveAura(PlayerJoinEvent event) {
        darkAuraPlayers.remove(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void RemoveAuraQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();

        if (darkAuraTasks.containsKey(playerId)) {
            BukkitTask task = darkAuraTasks.get(playerId);
            if (task != null) {
                task.cancel();
            }
            darkAuraTasks.remove(playerId);
        }

        darkAuraPlayers.remove(playerId);
    }

    public void SummonDarkAura(Location location, int size) {
        for (int d = 0; d <= 90; d += 1) {
            Location particleLoc = new Location(location.getWorld(), location.getX(), location.getY(), location.getZ());
            particleLoc.setX(location.getX() + Math.cos(d) * size);
            particleLoc.setZ(location.getZ() + Math.sin(d) * size);
            
            location.getWorld().spawnParticle(Particle.REDSTONE, particleLoc.add(0, 4.2, 0), 1, new Particle.DustOptions(Color.BLACK, 1));
            location.getWorld().spawnParticle(Particle.REDSTONE, particleLoc, 1, new Particle.DustOptions(Color.BLACK, 1));
            location.getWorld().spawnParticle(Particle.REDSTONE, particleLoc, 1, new Particle.DustOptions(Color.GRAY, 1));
            location.getWorld().spawnParticle(Particle.SMOKE_LARGE, particleLoc, 1, 0.5, 0.5, 0.5, 0.02);
            location.getWorld().spawnParticle(Particle.SMOKE_NORMAL, particleLoc, 1, 0.5, 0.5, 0.5, 0.02);
            location.getWorld().spawnParticle(Particle.ASH, particleLoc, 15, 0.3, 0.3, 0.3, 0.02);
            location.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, particleLoc, 1, 1.0, 1.0, 1.0, 0.08);
        }
    }
}
