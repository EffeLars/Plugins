
package me.magicwands.witch;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
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
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import me.magicwands.main.Main;
import me.magicwands.utils.ChatUtil;

    public class WitchAura implements Listener {
        private final Map<UUID, BukkitTask> witchAuraTasks = new HashMap<>();
        private final Map<UUID, Boolean> witchAuraPlayers = new HashMap<>();

        @SuppressWarnings("deprecation")
        @EventHandler
        public void onPlayerInteract(PlayerInteractEvent event) {
            Player player = event.getPlayer();
            if (player.getItemInHand().getItemMeta() != null &&
                player.getItemInHand().getItemMeta().getDisplayName() != null &&
                player.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(me.magicwands.wands.WitchWand.WitchWand.getItemMeta().getDisplayName()) &&
                event.getAction() == Action.LEFT_CLICK_AIR &&
                me.magicwands.events.WitchWand.WitchSpell.getOrDefault(player, 0) == 8) {

                UUID playerUUID = player.getUniqueId();
                boolean hasAura = witchAuraPlayers.getOrDefault(playerUUID, false);
                String color = Main.getPlugin(Main.class).getConfig().getString("wands.witch.color");

                if (hasAura) {
                    witchAuraPlayers.put(playerUUID, false);
                    
                    if (witchAuraTasks.containsKey(playerUUID)) {
                        witchAuraTasks.get(playerUUID).cancel();
                        witchAuraTasks.remove(playerUUID);
                    }

                    player.sendMessage(ChatUtil.color("&7[&" + color + "Magic&7] &" + color + "Stopped the witch aura.."));
                } else {
                    witchAuraPlayers.put(playerUUID, true);
                    player.sendMessage(ChatUtil.color("&7[&" + color + "Magic&7] &" + color + "Started the witch aura.."));

                    BukkitTask task = Bukkit.getScheduler().runTaskTimer(Main.getPlugin(Main.class), new Runnable() {
                        @Override
                        public void run() {
                            SummonWitchAura(player, player.getLocation(), 4);
                            for (Entity entity : player.getLocation().getWorld().getNearbyEntities(player.getLocation(), 4, 4, 4)) {
                                if (!(entity instanceof Player) || entity == player) continue;
                                Player nearbyPlayer = (Player) entity;
                                nearbyPlayer.setFireTicks(300);
                                Vector direction = nearbyPlayer.getLocation().getDirection().normalize().multiply(-1);
                                nearbyPlayer.setVelocity(direction.multiply(2));
                                nearbyPlayer.playSound(nearbyPlayer.getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 1, 1);
                            }
                        }
                    }, 0L, 15L);

                    witchAuraTasks.put(playerUUID, task);
                }
            }
        }
    


    @EventHandler
    public void RemoveAura(PlayerJoinEvent event) {
        Player p = event.getPlayer();
        witchAuraPlayers.remove(p.getUniqueId());  
       
    }
    
    @EventHandler
    public void RemoveAuraQuit(PlayerQuitEvent event) {
        Player p = event.getPlayer();
        UUID playerUUID = p.getUniqueId();
        if (witchAuraTasks.containsKey(playerUUID)) {
            witchAuraTasks.get(playerUUID).cancel();
            witchAuraTasks.remove(playerUUID);
        }
    }
    
    public void SummonWitchAura(Player player, Location location, int size) {
        player.playSound(player.getLocation(), Sound.ENTITY_WITCH_AMBIENT, 1, 1);

       
        for (int d = 0; d < 360; d += 10) {  
            Location particleLoc = location.clone(); 
            double offsetX = Math.cos(Math.toRadians(d)) * size;
            double offsetZ = Math.sin(Math.toRadians(d)) * size;
            particleLoc.add(offsetX, 0, offsetZ); 
            for (int i = 0; i < 3; i++) {
                double randomX = Math.random() * 0.5 - 0.25;
                double randomY = Math.random() * 0.5 - 0.25;
                double randomZ = Math.random() * 0.5 - 0.25;

                location.getWorld().spawnParticle(Particle.REDSTONE, particleLoc.add(randomX, randomY, randomZ), 1, new Particle.DustOptions(Color.fromRGB(70, 0, 125), 1));
                location.getWorld().spawnParticle(Particle.REDSTONE, particleLoc.add(randomX, randomY, randomZ), 1, new Particle.DustOptions(Color.fromRGB(75, 0, 130), 1));
                location.getWorld().spawnParticle(Particle.REDSTONE, particleLoc.add(randomX, randomY, randomZ), 1, new Particle.DustOptions(Color.PURPLE, 1));
            }

            // Add additional particles like CLOUD, ENCHANTMENT_TABLE, and BLOCK_CRACK to create the ice effect
            location.getWorld().spawnParticle(Particle.SPELL_WITCH, particleLoc, 1, 1.0, 1.0, 1.0, 0.003);
            location.getWorld().spawnParticle(Particle.PORTAL, particleLoc, 1, 1.0, 1.0, 1.0, 0.003);
            location.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, particleLoc, 4, 1.0, 1.0, 1.0, 0.1);
            location.getWorld().spawnParticle(Particle.BLOCK_CRACK, particleLoc, 2, 0.0, 0.0, 0.0, 0.05, Material.PURPLE_CARPET.createBlockData());
        }
    }
}