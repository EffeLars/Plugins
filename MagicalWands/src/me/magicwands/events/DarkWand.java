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

public class DarkWand implements Listener {
    public static HashMap<Player, Integer> DarkSpell = new HashMap<>();

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        ItemStack item = player.getItemInHand();

        if (item.getItemMeta() == null || !item.getItemMeta().getDisplayName().equalsIgnoreCase(me.magicwands.wands.DarkWand.DarkWand.getItemMeta().getDisplayName()) || e.getAction() != Action.RIGHT_CLICK_AIR) {
            return;
        }

        List<Integer> availableSpells = new ArrayList<>();
        for (int i = 1; i <= 11; i++) {
            if (player.hasPermission("magicwands.dark.*") || player.hasPermission("magicwands.dark." + getSpellName(i).toLowerCase().replace(" ", ""))) {
                availableSpells.add(i);
            }
        }

        if (availableSpells.isEmpty()) {
            String color = getConfigString("wands.dark.color", "&0"); 
            player.sendMessage(ChatUtil.color("&7[&" + color + "MagicWands&7] &cYou have no available spells!"));
            return;
        }

        int currentSpell = DarkSpell.getOrDefault(player, 0);

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

        DarkSpell.put(player, currentSpell);

        if (StaffCommands.playerParticles.getOrDefault(player.getUniqueId(), true)) {
        	new WandEffects(player, WandEffects.EffectType.DARK).start();
        }

        if (StaffCommands.playerSounds.getOrDefault(player.getUniqueId(), true)) {
            player.playSound(player.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1.0F, -3.0F);
            player.playSound(player.getLocation(), Sound.ENTITY_ILLUSIONER_CAST_SPELL, 1.0F, -3.0F);
        }

       

        String spellName = getSpellName(currentSpell);
        String color = getConfigString("wands.dark.color", "&0"); 
        player.sendMessage(ChatUtil.color("&7[&" + color + "MagicWands&7] &fYou have selected spell &" + color + spellName + "&f!"));

        
    }


    private String getSpellName(int spellIndex) {
        switch (spellIndex) {
            case 1: return "Dark Leap";
            case 2: return "Dark Wave";
            case 3: return "Dark Pulse";
            case 4: return "Dark Teleport";
            case 5: return "Dark Trail";
            case 6: return "Dark Disappear";
            case 7: return "Dark Aura";
            case 8: return "Dark Spark";
            case 9: return "Dark Launch";
            case 10: return "Dark Cloud";
            case 11: return "Dark Abyss";
            default: return "Unknown Spell";
        }
    }


    private String getConfigString(String path, String defaultValue) {
        String value = Main.getPlugin(Main.class).getConfig().getString(path);
        if (value == null) {
            Main.getPlugin(Main.class).getConfig().set(path, defaultValue); 
            Main.getPlugin(Main.class).saveConfig(); 
            return defaultValue;
        }
        return value;
    }
}
