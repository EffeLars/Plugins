package me.magicwands.empire;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import me.magicwands.events.EmpireWand;
import me.magicwands.main.Main;
import me.magicwands.utils.CalculateVelocity;
import me.magicwands.utils.ChatUtil;
import me.magicwands.utils.CooldownManager;
import me.magicwands.utils.ParticleColor;
import me.magicwands.wands.WandItems;

public class EmpireEscape implements Listener {

	private CooldownManager cooldownManager;
	private Map<UUID, BukkitRunnable> particleTasks = new HashMap<>();
	private Map<UUID, Boolean> leapingPlayers = new HashMap<>();
	private Map<UUID, Boolean> hasActiveParticles = new HashMap<>(); 

	public EmpireEscape() {
	    long cooldownTimeInSeconds = Main.getPlugin(Main.class).getConfig().getLong("spells.EmpireEscape.cooldown", 30L); 
	    this.cooldownManager = new CooldownManager(cooldownTimeInSeconds);
	    this.cooldownManager.setDefaultCooldownIfNotExist(cooldownTimeInSeconds, "EmpireEscape");
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
	    Player player = event.getPlayer();
	    if (player.getItemInHand().getItemMeta() != null && 
	            player.getItemInHand().getItemMeta().getDisplayName() != null && 
	            player.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(WandItems.EmpireWand.getItemMeta().getDisplayName()) && 
	            event.getAction() == Action.LEFT_CLICK_AIR && 
	            EmpireWand.EmpireSpell.getOrDefault(player, 0) == 10) {
	        boolean isStaff = player.hasPermission("magicwands.*");

	        if (player.hasPermission("magicwands.empire.empireescape.bypasscooldown") || player.hasPermission("magicwands.empire.bypasscooldown") || isStaff) {
	            this.leapPlayer(player, isStaff);
	            return;
	        }

	        long cooldownTimeInMillis = cooldownManager.getCooldownDuration(player, "EmpireEscape");
	        if (this.cooldownManager.hasCooldown(player, "EmpireEscape")) {
	            long secondsRemaining = this.cooldownManager.getCooldownRemaining(player, "EmpireEscape");
	            String color = Main.getPlugin(Main.class).getConfig().getString("wands.empire.color");
	            player.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&',
	                    ChatUtil.color("&7[&" + color + "MagicWands&7] &fThis spell is still on a &" + color + "cooldown&c&l! &fYou have to wait &" + color +
	                            secondsRemaining + " &fseconds")));
	            return;
	        }
	        this.cooldownManager.startCooldown(player, cooldownTimeInMillis, "EmpireEscape");
	        this.leapPlayer(player, isStaff);
	    }
	}

	private static Location playerLocation;
	
	public static void setPlayerLocation(Player player) {
	    playerLocation = player.getLocation();
	}

	public static Location getPlayerLocation() {
	    return playerLocation;
	}
	
	@SuppressWarnings("deprecation")
	private void leapPlayer(final Player player, final boolean isStaff) {
	    long cooldownDuration = isStaff ? 0L : this.cooldownManager.getCooldownDuration(player, "EmpireEscape");

	    this.cooldownManager.startCooldown(player, cooldownDuration, "EmpireEscape");
	    leapingPlayers.put(player.getUniqueId(), true);

        setPlayerLocation(player);
        player.setVelocity(CalculateVelocity.calculateVelocity(player.getLocation().getDirection().multiply(2), isStaff));
        this.playLeapSound(player.getLocation().getWorld(), player.getLocation(), player);

       
        Bukkit.getScheduler().runTaskLater(Main.getPlugin(Main.class), () -> {
            if (!player.isOnline() || player.isOnGround()) {
                return; 
            }

          
            Bukkit.getPluginManager().registerEvents(new Listener() {
                @EventHandler
                public void onEntityDamage(EntityDamageEvent event) {
                    if (event.getEntity() instanceof Player) {
                        Player p = (Player) event.getEntity();
                        if (leapingPlayers.getOrDefault(p.getUniqueId(), false) && event.getCause() == DamageCause.FALL) {
                            event.setCancelled(true);
                            cancelParticleTask(p.getUniqueId(), false);
                        }
                    }
                }
            }, Main.getPlugin(Main.class));

            if (!hasActiveParticles.getOrDefault(player.getUniqueId(), false)) {
                BukkitRunnable particleTask = new BukkitRunnable() {
                    public void run() {
                        if (!player.isOnline()) {
                            leapingPlayers.put(player.getUniqueId(), false);
                            cancelParticleTask(player.getUniqueId(), false);
                            this.cancel();
                            return;
                        }

                        if (player.isOnGround()) {
                            leapingPlayers.put(player.getUniqueId(), false);
                            cancelParticleTask(player.getUniqueId(), true);
                            this.cancel();
                        } else {
                            playIceParticles(player.getLocation());
                        }
                    }
                };
                particleTask.runTaskTimer(Main.getPlugin(Main.class), 1L, 1L);
                this.particleTasks.put(player.getUniqueId(), particleTask);
                hasActiveParticles.put(player.getUniqueId(), true);
            }
        }, 8L); 

       
        BukkitRunnable groundCheckTask = new BukkitRunnable() {
            public void run() {
                if (!player.isOnline() || player.isOnGround()) {
                    leapingPlayers.put(player.getUniqueId(), false);
                    cancelParticleTask(player.getUniqueId(), true);
                    player.getWorld().createExplosion(getPlayerLocation(), 0, true);
                    this.cancel();
                }
            }
        };
        groundCheckTask.runTaskTimer(Main.getPlugin(Main.class), 80L, 100L); 
        
       
        new BukkitRunnable() {
            @Override
            public void run() {
                if (player.isOnline()) {
                    Vector direction = player.getLocation().getDirection().normalize();
                    Vector launchVelocity = direction.multiply(3.2).setY(-3.5);
                    player.setVelocity(launchVelocity);

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if (!player.isOnline()) {
                                this.cancel();
                                return;
                            }

                            if (player.isOnGround()) {
                                playIceParticles(player.getLocation());
                                playGroundEffect(player, player.getLocation());
                                this.cancel();
                            }
                        }
                    }.runTaskTimer(Main.getPlugin(Main.class), 1L, 1L);
                }
            }
        }.runTaskLater(Main.getPlugin(Main.class), 28L);
    }



    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
           
            if (leapingPlayers.getOrDefault(player.getUniqueId(), false) && event.getCause() == DamageCause.FALL) {
                event.setCancelled(true);
                cancelParticleTask(player.getUniqueId(), false);
            }
        }
    }

    @SuppressWarnings("deprecation")
	@EventHandler
    public void LeapEffect(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (player.isOnGround() && leapingPlayers.getOrDefault(player.getUniqueId(), false)) {
            Vector velocity = player.getVelocity();
            if (velocity.getY() == 0) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        leapingPlayers.put(player.getUniqueId(), false);
                        cancelParticleTask(player.getUniqueId(), true);
                    }
                }.runTaskLater(Main.getPlugin(Main.class), 5L);
            }
        }
    }

 

    private void playIceParticles(Location location) {
        World world = location.getWorld();
        if (world != null) {
            location.getWorld().spawnParticle(Particle.REDSTONE, location, 8, 0.4, 0.4, 0.4, 0.03, ParticleColor.PURPLE);
            location.getWorld().spawnParticle(Particle.REDSTONE, location, 8, 0.4, 0.4, 0.4, 0.03, ParticleColor.DARK_PURPLE);
            location.getWorld().spawnParticle(Particle.SMOKE_LARGE, location, 8, 0.2, 0.3, 0.2, 0.001);
            location.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, location, 70, 0.3, 0.5, 0.3, 0.01);
            location.getWorld().spawnParticle(Particle.BLOCK_CRACK, location, 9, 0.4, 0.4, 0.4, 0.03, Material.NETHER_PORTAL.createBlockData());
        }
    }
    
    public static double getRandomDouble(double min, double max) {
        Random ran = new Random();
        return min + (max - min) * ran.nextDouble();
    }
    
    private void playGroundEffect(Player player, Location location) {
        World world = location.getWorld();
        Material[] materials = {Material.OBSIDIAN, Material.FIRE, Material.COAL_BLOCK};
        if (world != null) {
            Particle.DustOptions iceShardColor = new Particle.DustOptions(Color.fromRGB(170, 255, 255), 1.0f);
            Particle.DustOptions cyanColor = new Particle.DustOptions(Color.AQUA, 1.0f);
            location.getWorld().spawnParticle(Particle.REDSTONE, location, 2, 2.4, 2.4, 2.4, 0.03, iceShardColor);
            location.getWorld().spawnParticle(Particle.REDSTONE, location, 2, 2.4, 2.4, 2.4, 0.03, cyanColor);
            location.getWorld().spawnParticle(Particle.CLOUD, location, 2, 2.2, 2.3, 2.2, 0.001);
            location.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, location, 20, 1.2, 1.3, 1.2, 0.001);
            player.getWorld().playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1, 1);
            location.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, location, 30, 2.3, 2.5, 2.3, 0.01);
            for (Entity entity : player.getWorld().getNearbyEntities(player.getLocation(), 4, 4, 4)) {
                if (!(entity instanceof Player) || entity == player.getPlayer()) continue;
                ((LivingEntity) entity).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 2));
                ((LivingEntity) entity).addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 100, 2));
                entity.getWorld().spawnParticle(Particle.REDSTONE, location, 2, 2.4, 2.4, 2.4, 0.03, iceShardColor);
                entity.getWorld().spawnParticle(Particle.REDSTONE, location, 2, 2.4, 2.4, 2.4, 0.03, cyanColor);
                entity.getWorld().spawnParticle(Particle.CLOUD, location, 2, 2.2, 2.3, 2.2, 0.001);
                entity.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, location, 30, 2.3, 2.5, 2.3, 0.01);
                ((LivingEntity) entity).getWorld().playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1, 1);
                ((LivingEntity) entity).getWorld().playSound(player.getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 1, 1);
                Vector direction = location.getDirection();
                Vector velocity = direction.multiply(-4); 
                velocity.setY(1.9);
                ((LivingEntity) entity).setVelocity(velocity);
            }
         for (int i = 0; i < 3; i++) {
             for (Material material : materials) {
					location.getWorld().spawnFallingBlock(location, material, (byte) 2).setVelocity(new Vector(
							getRandomDouble(-0.2, 0.5), getRandomDouble(0.4, 1.5), getRandomDouble(-0.2, 0.5))

					);
             }
         }
            location.getWorld().spawnParticle(Particle.BLOCK_CRACK, location, 2, 2.4, 2.4, 2.4, 0.03, Material.NETHER_PORTAL.createBlockData());
        }
    }


    private void playLeapSound(World world, Location location, Player player) {
        if (world != null) {
            world.playSound(location, Sound.ITEM_TRIDENT_THROW, 1.0f, -3.0f);
            world.playSound(location, Sound.ITEM_FIRECHARGE_USE, 1.0f, 1.0f);
            world.playSound(location, Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 1.0f, 2.0f);
            world.playSound(player.getLocation(), Sound.ITEM_TRIDENT_THROW, Integer.MAX_VALUE, -3.0f);
            world.playSound(player.getLocation(), Sound.ITEM_FIRECHARGE_USE, Integer.MAX_VALUE, 1.0f);
            world.playSound(player.getLocation(), Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, Integer.MAX_VALUE, 2.0f);
        }
    }

    private void cancelParticleTask(UUID playerId, boolean isStaff) {
        if (this.particleTasks.containsKey(playerId)) {
            BukkitRunnable particleTask = this.particleTasks.get(playerId);
            particleTask.cancel();
            this.particleTasks.remove(playerId);
            hasActiveParticles.put(playerId, false);
            if (isStaff) {
                leapingPlayers.put(playerId, false);
            }
        }
    }
}
