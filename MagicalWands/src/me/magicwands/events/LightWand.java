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

public class LightWand implements Listener {
	
    public static HashMap<Player, Integer> LightSpell = new HashMap<>();

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        ItemStack item = player.getItemInHand();
        
        if (item.getItemMeta() == null || !item.getItemMeta().getDisplayName().equalsIgnoreCase(me.magicwands.wands.LightWand.LightWand.getItemMeta().getDisplayName()) || e.getAction() != Action.RIGHT_CLICK_AIR) {
            return;
        }
        
        List<Integer> availableSpells = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            if (player.hasPermission("magicwands.light.*") || player.hasPermission("magicwands.light." + getSpellName(i).toLowerCase().replace(" ", ""))) {
                availableSpells.add(i);
            }
        }
        
        if (availableSpells.isEmpty()) {
            String color = Main.getPlugin(Main.class).getConfig().getString("wands.light.color");
            player.sendMessage(ChatUtil.color("&7[&" + color + "MagicWands&7] &cYou have no available spells!"));
            return;
        }
        
        int currentSpell = LightSpell.getOrDefault(player, 0);

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

        LightSpell.put(player, currentSpell);
        
        if (StaffCommands.playerParticles.getOrDefault(player.getUniqueId(), true)) {
        	new WandEffects(player, WandEffects.EffectType.LIGHT).start();
    }

    if (StaffCommands.playerSounds.getOrDefault(player.getUniqueId(), true)) {
    	 player.playSound(player.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1.0F, 1.0F);
         player.playSound(player.getLocation(), Sound.ENTITY_ILLUSIONER_CAST_SPELL, 1.0F, 1.0F);
    }
        String spellName = getSpellName(currentSpell);
        String color = Main.getPlugin(Main.class).getConfig().getString("wands.light.color");
        player.sendMessage(ChatUtil.color("&7[&" + color + "MagicWands&7] &fYou have selected spell &" + color + spellName + "&f!"));

       
    }


    private String getSpellName(int spellIndex) {
        switch (spellIndex) {
            case 1: return "Celestial Strike";
            case 2: return "Celestial Disappear";
            case 3: return "Celestial Wave";
            case 4: return "Celestial Teleport";
            case 5: return "Celestial Rage";
            case 6: return "Celestial Aura";
            case 7: return "Celestial Executioner";
            case 8: return "Celestial Time Turner";
            case 9: return "Celestial Vortex Grab";
            case 10: return "Celestial Cloud";
            default: return "Unknown Spell";
        }
    }
}