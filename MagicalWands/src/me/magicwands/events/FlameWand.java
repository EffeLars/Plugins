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
package me.magicwands.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import me.magicwands.commands.StaffCommands;
import me.magicwands.main.Main;
import me.magicwands.utils.ChatUtil;
import me.magicwands.utils.WandEffects;

public class FlameWand
implements Listener {
   
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public static HashMap<Player, Integer> Spell = new HashMap();


    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        ItemStack item = player.getItemInHand();
        
        if (item.getItemMeta() == null || !item.getItemMeta().getDisplayName().equalsIgnoreCase(me.magicwands.wands.FireWand.IgnatiusWand.getItemMeta().getDisplayName()) || e.getAction() != Action.RIGHT_CLICK_AIR) {
            return;
        }
        
        List<Integer> availableSpells = new ArrayList<>();
        for (int i = 1; i <= 13; i++) {
            if (player.hasPermission("magicwands.fire.*") || player.hasPermission("magicwands.fire." + getSpellName(i).toLowerCase().replace(" ", ""))) {
                availableSpells.add(i);
            } else {
           	 if (player.hasPermission("magicwands.fire.player")) {
                    availableSpells.add(1);
                    availableSpells.add(3);
                    availableSpells.add(4);
                    availableSpells.add(6);
                    availableSpells.add(7);
                    availableSpells.add(2);
            }
        }
    }
        
        if (availableSpells.isEmpty()) {
            String color = Main.getPlugin(Main.class).getConfig().getString("wands.fire.color");
            player.sendMessage(ChatUtil.color("&7[&" + color + "MagicWands&7]" + " &cYou have no available spells!"));
            return;
        }

        int currentSpell = Spell.getOrDefault(player, 0);
        
        
        if (player.isSneaking()) {
            int previousSpellIndex = availableSpells.indexOf(currentSpell) - 1;
            if (previousSpellIndex < 0) {
                previousSpellIndex = availableSpells.size() - 1; 
            }
            currentSpell = availableSpells.get(previousSpellIndex);
        } else {
          
            int nextSpellIndex = availableSpells.indexOf(currentSpell) + 1;
            if (nextSpellIndex >= availableSpells.size()) {
                nextSpellIndex = 0; 
            }
            currentSpell = availableSpells.get(nextSpellIndex);
        }

        Spell.put(player, currentSpell);

        if (StaffCommands.playerParticles.getOrDefault(player.getUniqueId(), true)) {
        	new WandEffects(player, WandEffects.EffectType.FLAME).start();
    }

    if (StaffCommands.playerSounds.getOrDefault(player.getUniqueId(), true)) {
    	player.playSound(player.getLocation(), Sound.BLOCK_NETHER_BRICKS_BREAK, 1.0F, 1.0F);
        player.playSound(player.getLocation(), Sound.BLOCK_NETHERRACK_BREAK, 1.0F, 1.0F);
    }
        String spellName = getSpellName(currentSpell);
        String color = Main.getPlugin(Main.class).getConfig().getString("wands.fire.color");
        player.sendMessage(ChatUtil.color("&7[&" + color + "MagicWands&7] &fYou have selected spell &" + color + spellName + "&f!"));

       
    }


    private String getSpellName(int spellIndex) {
        switch (spellIndex) {
            case 1: return "Leap";
            case 2: return "Fire Teleport";
            case 3: return "Fire Pulse";
            case 4: return "Fire Spark";
            case 5: return "Fire Trail";
            case 6: return "Fire Launch";
            case 7: return "Fire Comet";
            case 8: return "Fire Disappear";
            case 9: return "Flame Wave";
            case 10: return "Fire Aura";
            case 11: return "Fire Bomb";
            case 12: return "Fire Tornado";
            case 13: return "Fire Explosive";
            default: return "Unknown Spell";
        }
    }
}