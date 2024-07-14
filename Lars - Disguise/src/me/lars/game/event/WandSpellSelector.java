package me.lars.game.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import me.lars.game.main.Main;
import me.lars.game.utils.ChatUtil;
import net.md_5.bungee.api.ChatColor;

public class WandSpellSelector implements Listener {
	private static ArrayList<String> noFallDamage = new ArrayList<>();
	public static HashMap<Player, Integer> Spell = new HashMap<Player, Integer>();
	
	@EventHandler
	public void onJoin(PlayerBucketEmptyEvent e) {
		Player p = e.getPlayer();
		WandSpellSelector.Spell.put(p, 0);
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if(p.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.RED + "Lars Wand")) {
		if(!p.isSneaking()) 
		if(e.getAction() == Action.RIGHT_CLICK_AIR)  {
		if(Spell.containsKey(p)) {
		Spell.put(p, Spell.get(p) + 1);
		Location block1 = p.getLocation();
		p.getWorld().spawnParticle(Particle.SPELL_WITCH, block1, 50);
		if(p.isSneaking()) {
			Spell.put(p, Spell.get(p) - 1);
		}
		if(Spell.get(p) > 3) {
			p.sendMessage(ChatUtil.color("&6[&7L&6] &6Slot #1 selected: &7Empty&6."));
			Spell.put(p, 0);
		}
		if(Spell.get(p) == 1) {
			p.sendMessage(ChatUtil.color("&6[&7L&6] &6Slot #2 selected: &7Leap&6."));
		}if(Spell.get(p) == 2) {
			p.sendMessage(ChatUtil.color("&6[&7L&6] &6Slot #3 selected: &7TNT Spawn&6."));
		}if(Spell.get(p) == 3) {
			p.sendMessage(ChatUtil.color("&6[&7L&6] &6Slot #4 selected: &7Empty&6."));
		return;
		}
		}
		}}
	}
		
    private final List<BukkitRunnable> tasks = new ArrayList<>();

    @EventHandler
    public void onPlayerSnea2(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();

        if (event.isSneaking()) {
            // Find all gold blocks within a radius of 10 blocks
            List<Block> goldBlocks = findGoldBlocksInRadius(player.getLocation(), 10, 1.0);

            if (goldBlocks.size() < 10) {
                startSneakTimer(player, 60);
            } else if (goldBlocks.size() > 20) {
                startSneakTimer(player, 30);
            }
        }
    }

    private void startSneakTimer(Player player, int duration) {
        new BukkitRunnable() {
            int progress = 1;

            @Override
            public void run() {
                if (!player.isSneaking() || progress > 100) {
                    cancel();
                    return;
                }

                // Send a chat message with the current progress
                player.sendMessage("Progress: " + progress + "%");

                if (progress >= 100) {
                    // Send a green "READY!" message when the progress reaches 100%
                    player.sendMessage(ChatColor.GREEN + "READY!");
                }

                // Draw the flame particles at the player's location
                player.spawnParticle(Particle.ASH, player.getLocation(), 1, 0, 0, 0, 0);

                progress++;
            }
        }.runTaskTimer(Main.getPlugin(Main.class), 0, duration * 20);
    }

    @EventHandler
    public void onPlayerSneak(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();

        if (event.isSneaking()) {
            // Find all gold blocks within a radius of 5 blocks
            List<Block> goldBlocks = findGoldBlocksInRadius(player.getLocation(), 10, 5);

            if (goldBlocks.isEmpty()) {
                player.sendMessage("No gold blocks found within a 5 block radius.");
                return;
            }

            // Create a repeating task for each gold block
            for (Block goldBlock : goldBlocks) {
                Vector direction = player.getLocation().toVector().subtract(goldBlock.getLocation().toVector()).normalize();

                BukkitRunnable task = new BukkitRunnable() {
                    Location currentLocation = goldBlock.getLocation();

                    @Override
                    public void run() {
                        if (!player.isSneaking()) {
                            cancel();
                            return;
                        }

                        // Draw the flame particles at the current location
                        player.spawnParticle(Particle.ASH, currentLocation, 1, 0, 0, 0, 0);
         
                        // Move the current location toward the player
                        currentLocation.add(direction);

                        // Check if the current location is near the player's location
                        if (currentLocation.distance(player.getLocation()) < 1.0) {
                            // Reset the current location back to the gold block
                            currentLocation = goldBlock.getLocation();
                        }
                    }
                };
                task.runTaskTimer(Main.getPlugin(Main.class), 0, 1); // Run every 2 ticks (0.1 seconds)
                tasks.add(task);
            }
        }
    }

    @EventHandler
    public void onPlayerStopSneak(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();

        if (!event.isSneaking()) {
            // Cancel all the active tasks
            tasks.forEach(BukkitRunnable::cancel);
            tasks.clear();
        }
    }

    private List<Block> findGoldBlocksInRadius(Location location, int radius, double percentage) {
        List<Block> goldBlocks = new ArrayList<>();

        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    Location checkLocation = location.clone().add(x, y, z);
                    Block block = checkLocation.getBlock();
                    if (block.getType() == Material.OAK_LEAVES || block.getType() == Material.BIRCH_LEAVES) {
                        if (Math.random() < percentage) {
                            goldBlocks.add(block);
                        }
                    }
                }
            }
        }

        return goldBlocks;
    }


	  

		@EventHandler
		public void onInteract2(PlayerInteractEvent e) {
			Player player = e.getPlayer();
			if(player.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.RED + "Lars Wand")) {
			if(e.getAction() == Action.LEFT_CLICK_AIR) {
			if(Spell.get(player) == 1) {
				noFallDamage.add(player.getName());

				double forwardPowerModifier = 1.5D;
				double upwardPowerModifier = forwardPowerModifier * 2.0D;
				double fwv = 2.0D;
				double uwv = 0.7D;

				Vector dir = player.getLocation().getDirection();

				dir.setY(0).normalize().multiply(fwv * forwardPowerModifier).setY(uwv * upwardPowerModifier);

				player.getPlayer().setVelocity(dir);

				final org.bukkit.World w = e.getPlayer().getWorld();
				w.playSound(player.getLocation(), Sound.BLOCK_BONE_BLOCK_FALL, 1.0F, 1.0F);
				Main.getPlugin().getServer().getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
					public void run() {
					player.sendMessage("pog");

					}
				}, 50L);// 60 L == 3 sec, 20 ticks == 1 se
			}
			
			}
			if(e.getAction() == Action.LEFT_CLICK_AIR) {
			if(Spell.get(player) == 2) {
				org.bukkit.block.Block block = player.getTargetBlock(null, 100);
				Location bl = block.getLocation();
				bl.getBlock().setType(Material.TNT);
			}
			}
			
			if(Spell.get(player) == 3) {
				player.sendMessage("Check3");
				return;
			}
			if(Spell.get(player) == 4) {
				player.sendMessage("Reset");
				return;
			}
		}
	}}
