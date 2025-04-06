package me.magicwands.utils;

import java.util.UUID;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import me.magicwands.commands.StaffCommands;
import me.magicwands.events.DarkWand;
import me.magicwands.events.EmpireWand;
import me.magicwands.events.FlameWand;
import me.magicwands.events.IceWand;
import me.magicwands.events.LightWand;
import me.magicwands.events.NatureWand;
import me.magicwands.events.WitchWand;
import me.magicwands.wands.WandItems;

public class WandManager implements Listener {
	
	@EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player p = event.getPlayer();
        
        DarkWand.DarkSpell.put(p, -1);
        EmpireWand.EmpireSpell.put(p, -1);
        FlameWand.Spell.put(p, -1);
        IceWand.IceSpell.put(p, -1);
        LightWand.LightSpell.put(p, -1);
        NatureWand.NatureSpell.put(p, -1);
        WitchWand.WitchSpell.put(p, -1);

        
        UUID playerId = p.getUniqueId();
  
        StaffCommands.playerParticles.putIfAbsent(playerId, true);  
        StaffCommands.playerSounds.putIfAbsent(playerId, true);   

        if (StaffCommands.playerParticles.get(playerId)) {

        }
        if (StaffCommands.playerSounds.get(playerId)) {
            p.playSound(p.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1.0F, -3.0F);
            p.playSound(p.getLocation(), Sound.ENTITY_ILLUSIONER_CAST_SPELL, 1.0F, -3.0F);
        }
    }
	
	 @EventHandler
	    private void WandPlaceDENIED(BlockPlaceEvent event) {
		 if (event.getPlayer().getInventory().getItemInHand().getItemMeta().getDisplayName().equals(WandItems.EmpireWand.getItemMeta().getDisplayName())) {
			    event.setCancelled(true);
			}

			if (event.getPlayer().getInventory().getItemInHand().getItemMeta().getDisplayName().equals(WandItems.DarkWand.getItemMeta().getDisplayName())) {
			    event.setCancelled(true);
			}

			if (event.getPlayer().getInventory().getItemInHand().getItemMeta().getDisplayName().equals(WandItems.IgnatiusWand.getItemMeta().getDisplayName())) {
			    event.setCancelled(true);
			}

			if (event.getPlayer().getInventory().getItemInHand().getItemMeta().getDisplayName().equals(WandItems.IceWand.getItemMeta().getDisplayName())) {
			    event.setCancelled(true);
			}

			if (event.getPlayer().getInventory().getItemInHand().getItemMeta().getDisplayName().equals(WandItems.LightWand.getItemMeta().getDisplayName())) {
			    event.setCancelled(true);
			}

			if (event.getPlayer().getInventory().getItemInHand().getItemMeta().getDisplayName().equals(WandItems.NatureWand.getItemMeta().getDisplayName())) {
			    event.setCancelled(true);
			}

			if (event.getPlayer().getInventory().getItemInHand().getItemMeta().getDisplayName().equals(WandItems.WitchWand.getItemMeta().getDisplayName())) {
			    event.setCancelled(true);
			}

	       
	    }


}
