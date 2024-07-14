/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  net.md_5.bungee.api.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.Particle
 *  org.bukkit.World
 *  org.bukkit.block.Block
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.block.Action
 *  org.bukkit.event.player.PlayerBucketEmptyEvent
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 *  org.bukkit.util.Vector
 */
package me.lars.game.event;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import me.lars.game.Main;
import me.lars.game.utils.ChatUtil;

public class IceWand
implements Listener {
    private static ArrayList<String> noFallDamage = new ArrayList();
    public static HashMap<Player, Integer> IceSpell = new HashMap();

    @EventHandler
    public void onJoin(PlayerBucketEmptyEvent e) {
        Player p = e.getPlayer();
        IceSpell.put(p, 0);
    }

    private void spawnparticles(Player player) {
        IceWandEffect wandAnimation = new IceWandEffect(player);
        wandAnimation.start();
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if (!player.hasPermission("staff.wand") && player.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(me.lars.game.wands.IceWand.IceWand.getItemMeta().getDisplayName()) && !player.isSneaking() && e.getAction() == Action.RIGHT_CLICK_AIR) {
            int currentSpell;
            int nextSpell = currentSpell = IceSpell.getOrDefault(player, 0).intValue();
            do {
                if (++nextSpell <= 10) continue;
                nextSpell = 1;
            } while (nextSpell != 1 && nextSpell != 5);
            IceSpell.put(player, nextSpell);
            Location blockLocation = player.getLocation();
            if(player.getScoreboardTags().contains("particleon")) {
            this.spawnparticles(player);
            } else {
            	//
            }
            if (nextSpell == 0) {
                player.sendMessage(ChatUtil.color("&4&lMagical Wand &cYou have selected the spell &c&lEmpty"));
            } else {
                String spellName = this.getSpellName(nextSpell);
            	player.playSound(player.getLocation(), Sound.BLOCK_LEVER_CLICK, 1.0F, 1.0F);
                player.playSound(player.getLocation(), Sound.BLOCK_LAVA_POP, 1.0F, 1.0F);
                String color = Main.getPlugin(Main.class).getConfig().getString("wands.ice.color");
                player.sendMessage(ChatUtil.color("&7[&" + color + "Magic&7] &fYou have selected the spell &d&l" + spellName + "&f!"));

            }
            return;
        }
        if (player.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(me.lars.game.wands.IceWand.IceWand.getItemMeta().getDisplayName()) && !player.isSneaking() && e.getAction() == Action.RIGHT_CLICK_AIR) {
            if (!IceSpell.containsKey(player)) {
                IceSpell.put(player, 1);
            } else {
                int currentSpell = IceSpell.get(player);
                int nextSpell = currentSpell + 1;
                if (nextSpell > 10) {
                    nextSpell = 1;
                }
                IceSpell.put(player, nextSpell);
            }
            Location blockLocation = player.getLocation();
            if(player.getScoreboardTags().contains("particleon")) {
                this.spawnparticles(player);
                } else {
                	//
                }
            if (IceSpell.get(player) == 0) {
                player.sendMessage(ChatUtil.color("&4&lMagical Wand &cYou have selected the spell &c&lEmpty"));
            } else {
                String spellName = this.getSpellName(IceSpell.get(player));
                String color = Main.getPlugin(Main.class).getConfig().getString("wands.ice.color");
                player.sendMessage(ChatUtil.color("&7[&" + color + "MagicWands&7] &fYou have selected the spell &" + color + spellName + "&f!"));
            }
        }
    }



    private String getSpellName(int spellIndex) {
        switch (spellIndex) {
            case 1: {
                return "Cold Teleport";
            }
            case 2: {
                return "Cold Wave";
            }
            case 3: {
                return "Cold Spark";
            }
            case 4: {
                return "Cold Disappear";
            }
            case 5: {
                return "Cold Launch";
            }
            case 6: {
                return "Cold Leap";
            }
            case 7: {
                return "Cold Spark Wave";
            }
            case 8: {
                return "Ice Trail";
            }
            case 9: {
                return "Ice Aura";
            }
            case 10: {
                return "Ice Blindness";
            }
        }
        return "Unknown Spell";
    }
}