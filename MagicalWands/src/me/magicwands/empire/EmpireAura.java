
package me.magicwands.empire;

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
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import me.magicwands.events.EmpireWand;
import me.magicwands.main.Main;
import me.magicwands.utils.ChatUtil;
import me.magicwands.wands.WandItems;

public class EmpireAura implements Listener {
    private final Map<UUID, BukkitTask> EmpireAuraTasks = new HashMap<>(); 
    private final Map<UUID, Boolean> EmpireAuraPlayers = new HashMap<>();

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();

        if (player.getItemInHand().getItemMeta() != null && 
    	        player.getItemInHand().getItemMeta().getDisplayName() != null && 
    	        player.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(WandItems.EmpireWand.getItemMeta().getDisplayName()) && 
    	        event.getAction() == Action.LEFT_CLICK_AIR && 
    	        EmpireWand.EmpireSpell.getOrDefault(player, 0) == 9) {

            boolean hasAura = EmpireAuraPlayers.getOrDefault(playerId, false);

            if (hasAura) {
              
                EmpireAuraPlayers.put(playerId, false);

                if (EmpireAuraTasks.containsKey(playerId)) {
                    BukkitTask task = EmpireAuraTasks.get(playerId);
                    if (task != null) {
                        task.cancel();
                    }
                    EmpireAuraTasks.remove(playerId);
                }

                String color = Main.getPlugin(Main.class).getConfig().getString("wands.empire.color");
                player.sendMessage(ChatUtil.color("&7[&" + color + "Magic&7] &" + color + "Stopped the Empire aura.."));
            } else {
               
                EmpireAuraPlayers.put(playerId, true);
                String color = Main.getPlugin(Main.class).getConfig().getString("wands.empire.color");
                player.sendMessage(ChatUtil.color("&7[&" + color + "Magic&7] &" + color + "Started the Empire aura.."));

                BukkitTask task = new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (!EmpireAuraPlayers.getOrDefault(playerId, false)) {
                            this.cancel();
                            EmpireAuraTasks.remove(playerId);
                            return;
                        }

                        SummonEmpireAura(player.getLocation(), 4);
                        player.playSound(player.getLocation(), Sound.ENTITY_PHANTOM_FLAP, (float) 0.5, 1);
                        for (Entity entity : player.getLocation().getWorld().getNearbyEntities(player.getLocation(), 4, 4, 4)) {
                            if (!(entity instanceof Player) || entity == player) continue;
                            Player nearbyPlayer = (Player) entity;
                            Vector direction = nearbyPlayer.getLocation().getDirection().normalize().multiply(-1);
                            nearbyPlayer.setVelocity(direction.multiply(-2));
                            nearbyPlayer.playSound(nearbyPlayer.getLocation(), Sound.ENTITY_PHANTOM_HURT, 1, -3);
                            nearbyPlayer.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 2));
                            nearbyPlayer.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 100, 2));
                        }
                    }
                }.runTaskTimer(Main.getPlugin(Main.class), 0L, 10L); 

                EmpireAuraTasks.put(playerId, task);
            }
        }
    }

   
    @EventHandler
    public void RemoveAuraQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();

        if (EmpireAuraTasks.containsKey(playerId)) {
            BukkitTask task = EmpireAuraTasks.get(playerId);
            if (task != null) {
                task.cancel();
            }
            EmpireAuraTasks.remove(playerId);
        }

        EmpireAuraPlayers.remove(playerId);
    }

    public void SummonEmpireAura(Location location, int size) {
    	 for (int d = 0; d <= 90; d += 1) {
             Location particleLoc = new Location(location.getWorld(), location.getX(), location.getY(), location.getZ());
             particleLoc.setX(location.getX() + Math.cos(d) * size);
             particleLoc.setZ(location.getZ() + Math.sin(d) * size);
            location.getWorld().spawnParticle(Particle.REDSTONE, particleLoc.add(0, 4.5, 0), 1, 0.5, 0.5, 0.5, new Particle.DustOptions(Color.PURPLE, 1));
            location.getWorld().spawnParticle(Particle.REDSTONE, particleLoc, 1, 0.5, 0.5, 0.5, new Particle.DustOptions(Color.BLACK, 1));
            location.getWorld().spawnParticle(Particle.SMOKE_LARGE, particleLoc, 1, 0.5, 0.5, 0.5, 0.02);
            location.getWorld().spawnParticle(Particle.ASH, particleLoc, 2, 1.0, 1.0, 1.0, 0.08);
            location.getWorld().spawnParticle(Particle.PORTAL, particleLoc, 5, 1.0, 1.0, 1.0, 0.08);
        }

    }
}
