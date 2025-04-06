package me.magicwands.ice;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import me.magicwands.main.Main;
import me.magicwands.utils.ChatUtil;
import me.magicwands.utils.CooldownManager;
import me.magicwands.wands.IceWand;

public class IceTeleport implements Listener {
    private CooldownManager cooldownManager;  

    public IceTeleport() {
        long cooldownTimeInSeconds = Main.getPlugin(Main.class).getConfig().getLong("spells.IceTeleport.cooldown", 30L); 
        this.cooldownManager = new CooldownManager(cooldownTimeInSeconds);
        this.cooldownManager.setDefaultCooldownIfNotExist(cooldownTimeInSeconds, "IceTeleport");
    }

    @SuppressWarnings("deprecation")
	@EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (player.getItemInHand().getItemMeta() != null && 
        	    player.getItemInHand().getItemMeta().getDisplayName() != null && 
        	    player.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(IceWand.IceWand.getItemMeta().getDisplayName()) && 
        	    event.getAction() == Action.LEFT_CLICK_AIR && 
        	    me.magicwands.events.IceWand.IceSpell.getOrDefault(player, 0) == 1) {

            boolean isStaff = player.hasPermission("magicwands.*");
            if (player.hasPermission("magicwands.ice.coldteleport.bypasscooldown") || player.hasPermission("magicwands.ice.bypasscooldown") || isStaff) {
                this.teleportPlayer(player);
                return;
            }
            if (this.cooldownManager.hasCooldown(player, "IceTeleport")) {
                long secondsRemaining = this.cooldownManager.getCooldownRemaining(player, "IceTeleport");
                String color = Main.getPlugin(Main.class).getConfig().getString("wands.ice.color");
                player.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&',
                        ChatUtil.color("&7[&" + color + "MagicWands&7] &fThis spell is still on a &" + color + "cooldown&c&l! &fYou have to wait &" + color +
                                secondsRemaining + " &fseconds")));
                return;
            }

            this.cooldownManager.startCooldown(player, this.cooldownManager.getCooldownDuration(player, "IceTeleport"), "IceTeleport");
            this.teleportPlayer(player);
        }
        }
    

    private void teleportPlayer(final Player player) {
        Location targetLocation = player.getTargetBlock(null, 300).getLocation();
        if (targetLocation.distanceSquared(player.getLocation()) <= 4500.0) {
            if (this.isInsideBlock(targetLocation)) {
                targetLocation.setY(targetLocation.getY() + 1.0);
            }
            this.IceTeleporter(player, targetLocation);
        }
    }

    private boolean isInsideBlock(Location location) {
        Block block = location.getBlock();
        return !block.getType().isAir();
    }

    private void IceTeleporter(final Player player, final Location targetLocation) {
        final Location departureLocation = player.getLocation();
        Vector dep2 = player.getLocation().getDirection();
        final World world = departureLocation.getWorld();
        final int explosionRadius = 0;

        world.playSound(departureLocation, Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
        Particle.DustOptions iceShardColor = new Particle.DustOptions(Color.fromRGB(170, 255, 255), 1.0f);
        Particle.DustOptions cyanColor = new Particle.DustOptions(Color.AQUA, 1.0f);
        Particle.DustOptions blueColor = new Particle.DustOptions(Color.fromRGB(0, 0, 255), 1.0f);
        Location location = player.getLocation();
        world.spawnParticle(Particle.REDSTONE, location, 35, 1.0, 1.0, 1.0, 0.03, iceShardColor);
        world.playSound(location, Sound.BLOCK_FIRE_EXTINGUISH, 1, -3);
        world.spawnParticle(Particle.REDSTONE, location, 35, 1.0, 1.0, 1.0, 0.03, cyanColor);
        world.spawnParticle(Particle.REDSTONE, location, 35, 1.0, 1.0, 1.0, 0.03, blueColor);
        world.spawnParticle(Particle.CLOUD, location, 40, 0.9, 0.9, 0.9, 0.001);
        world.spawnParticle(Particle.ENCHANTMENT_TABLE, location, 50, 0.9, 0.9, 0.9, 0.01);
        world.spawnParticle(Particle.BLOCK_CRACK, location, 80, 1.0, 1.0, 1.0, 1.03, Material.ICE.createBlockData());
        createExplosion(departureLocation, explosionRadius);

        new BukkitRunnable() {
            public void run() {
            
                player.teleport(targetLocation.setDirection(dep2));
               
                world.playSound(targetLocation, Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
                player.getWorld().spawnParticle(Particle.REDSTONE, targetLocation, 5, 1.0, 1.0, 1.0, 0.03, iceShardColor);
                player.playSound(targetLocation, Sound.BLOCK_FIRE_EXTINGUISH, 1, -3);
                player.getWorld().spawnParticle(Particle.REDSTONE, targetLocation, 5, 1.0, 1.0, 1.0, 0.03, cyanColor);
                world.spawnParticle(Particle.REDSTONE, targetLocation, 35, 1.0, 1.0, 1.0, 0.03, iceShardColor);
                world.playSound(targetLocation, Sound.BLOCK_FIRE_EXTINGUISH, 1, -3);
                world.spawnParticle(Particle.REDSTONE, targetLocation, 35, 1.0, 1.0, 1.0, 0.03, cyanColor);
                world.spawnParticle(Particle.REDSTONE, targetLocation, 35, 1.0, 1.0, 1.0, 0.03, blueColor);
                world.spawnParticle(Particle.CLOUD, targetLocation, 40, 0.9, 0.9, 0.9, 0.001);
                world.spawnParticle(Particle.ENCHANTMENT_TABLE, targetLocation, 50, 0.9, 0.9, 0.9, 0.01);
                world.spawnParticle(Particle.BLOCK_CRACK, targetLocation, 80, 1.0, 1.0, 1.0, 1.03, Material.ICE.createBlockData());
                player.getWorld().spawnParticle(Particle.REDSTONE, targetLocation, 5, 1.0, 1.0, 1.0, 0.03, blueColor);
                createExplosion(targetLocation, explosionRadius);

                for (Entity entity : targetLocation.getWorld().getNearbyEntities(targetLocation, 4, 4, 4)) {
                    if (!(entity instanceof Player) || entity == player.getPlayer()) continue;
                    Player nearbyPlayer = (Player) entity;
                    nearbyPlayer.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 2));
                    nearbyPlayer.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 100, 2));
                    nearbyPlayer.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 700, 2));
                }
            }
        }.runTaskLater(Main.getPlugin(Main.class), 10L); 
    }

    private void createExplosion(Location location, int radius) {
        World world = location.getWorld();
        Particle.DustOptions iceShardColor = new Particle.DustOptions(Color.fromRGB(170, 255, 255), 1.0f);
        Particle.DustOptions cyanColor = new Particle.DustOptions(Color.AQUA, 1.0f);
        Particle.DustOptions blueColor = new Particle.DustOptions(Color.fromRGB(0, 0, 255), 1.0f);
        world.spawnParticle(Particle.REDSTONE, location, 35, 1.0, 1.0, 1.0, 0.03, iceShardColor);
        world.playSound(location, Sound.BLOCK_FIRE_EXTINGUISH, 1, -3);
        world.spawnParticle(Particle.REDSTONE, location, 35, 1.0, 1.0, 1.0, 0.03, cyanColor);
        world.spawnParticle(Particle.REDSTONE, location, 35, 1.0, 1.0, 1.0, 0.03, blueColor);
        world.spawnParticle(Particle.CLOUD, location, 40, 0.9, 0.9, 0.9, 0.001);
        world.spawnParticle(Particle.ENCHANTMENT_TABLE, location, 50, 0.9, 0.9, 0.9, 0.01);
        world.spawnParticle(Particle.BLOCK_CRACK, location, 80, 1.0, 1.0, 1.0, 1.03, Material.ICE.createBlockData());
        int x = -radius;
        while (x <= radius) {
            int y = -radius;
            while (y <= radius) {
                int z = -radius;
                while (z <= radius) {
                    Location currentLocation = location.clone().add((double) x, (double) y, (double) z);
                    Block block = currentLocation.getBlock();
                    if (block.getType() != Material.AIR) {
                        block.setType(Material.AIR);
                    }
                    ++z;
                }
                ++y;
            }
            ++x;
        }
    }
}
