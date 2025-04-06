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
import me.magicwands.wands.WandItems;

public class EmpireWand implements Listener {
    public static HashMap<Player, Integer> EmpireSpell = new HashMap<>();


    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        ItemStack item = player.getItemInHand();
        
        if (item.getItemMeta() == null || !item.getItemMeta().getDisplayName().equalsIgnoreCase(WandItems.EmpireWand.getItemMeta().getDisplayName()) || e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK) {
            return;
        }
        
        List<Integer> availableSpells = new ArrayList<>();
         for (int i = 1; i <= 16; i++) {
        	 if(player.getName().equals("Treption")) {
        		 player.sendMessage("This user has been banned from using this plugin.");
        		 return;
        	 }
             if (player.hasPermission("magicwands.empire.*") || player.hasPermission("magicwands.empire." + getSpellName(i).toLowerCase().replace(" ", ""))) {
                 availableSpells.add(i);
             } else {
                 if (player.hasPermission("magicwands.empire.player")) {
                     availableSpells.add(1);
                     availableSpells.add(3);
                     availableSpells.add(5);
                     availableSpells.add(6);
                     availableSpells.add(10);
             }
         }
     }
        
        if (availableSpells.isEmpty()) {
            String color = Main.getPlugin(Main.class).getConfig().getString("wands.empire.color");
            player.sendMessage(ChatUtil.color("&7[&" + color + "MagicWands&7] &cYou have no available spells!"));
            return;
        }
        
        int currentSpell = EmpireSpell.getOrDefault(player, 0);

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

        EmpireSpell.put(player, currentSpell);
        
        if (StaffCommands.playerParticles.getOrDefault(player.getUniqueId(), true)) {
        	new WandEffects(player, WandEffects.EffectType.EMPIRE).start();
    }

    if (StaffCommands.playerSounds.getOrDefault(player.getUniqueId(), true)) {
        player.playSound(player.getLocation(), Sound.BLOCK_LEVER_CLICK, 1.0F, 1.0F);
        player.playSound(player.getLocation(), Sound.BLOCK_END_PORTAL_FRAME_FILL, 1.0F, 1.0F);
    }
        
        String spellName = getSpellName(currentSpell);
        String color = Main.getPlugin(Main.class).getConfig().getString("wands.empire.color");
        player.sendMessage(ChatUtil.color("&7[&" + color + "MagicWands&7] &fYou have selected spell &" + color + spellName + "&f!"));

    }

   
    
    private String getSpellName(int spellIndex) {
    	switch (spellIndex) {
        case 1: return "Empire Leap"; 
        case 2: return "Empire Launch";
        case 3: return "Empire Spark";
        case 4: return "Empire Confuse";
        case 5: return "Empire Confusion Wave"; 
        case 6: return "Empire Blind";
        case 7: return "Empire Poison Wave";
        case 8: return "Empire Empty";
        case 9: return "Empire Aura";
        case 10: return "Empire Escape";
        case 11: return "Empire Explosion";
        case 12: return "Empire Explosive Wave";
        case 13: return "Empire Shockwave";
        case 14: return "Empire Empty";
        case 15: return "Oreon Empire Hunt";
        case 16: return "Kratis Empire Madness";
        default: return "Unknown Spell";
    }

    }
}