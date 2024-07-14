/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.event.Listener
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.PluginManager
 *  org.bukkit.plugin.java.JavaPlugin
 */
package me.lars.game;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import me.lars.game.command.ItemCommand;
import me.lars.game.event.FlameWand;
import me.lars.game.wands.FireItems;
import me.lars.game.wands.FlowerWand;
import me.lars.game.wands.HaldirionItems;
import me.lars.game.wands.IceWand;
import me.lars.game.wands.MortiferumItems;
import me.lars.game.wands.NatureWand;
import me.lars.game.wands.NyxaraItems;
import me.lars.game.wands.Wands;
import me.rok.magicalwands.fire.customitems.IgnatiusBow;
import me.rok.magicalwands.fire.customitems.IgnatiusBowListener;
import me.rok.magicalwands.fire.spells.FireAura;
import me.rok.magicalwands.fire.spells.FireComet;
import me.rok.magicalwands.fire.spells.FireDisappear;
import me.rok.magicalwands.fire.spells.FireLaunch;
import me.rok.magicalwands.fire.spells.FireLeap;
import me.rok.magicalwands.fire.spells.FirePulse;
import me.rok.magicalwands.fire.spells.FireSpark;
import me.rok.magicalwands.fire.spells.FireTeleport;
import me.rok.magicalwands.fire.spells.FireTrail;
import me.rok.magicalwands.fire.spells.FireWave;
import me.rok.magicalwands.flower.spells.FlowerShockwave;
import me.rok.magicalwands.flower.spells.FlowerWave;
import me.rok.magicalwands.haldirion.customitems.HaldirionBow;
import me.rok.magicalwands.haldirion.customitems.HaldirionBowListener;
import me.rok.magicalwands.haldirion.customitems.HaldirionSword;
import me.rok.magicalwands.ice.spells.IceAura;
import me.rok.magicalwands.ice.spells.IceBlind;
import me.rok.magicalwands.ice.spells.IceDisappear;
import me.rok.magicalwands.ice.spells.IceLaunch;
import me.rok.magicalwands.ice.spells.IceLeap;
import me.rok.magicalwands.ice.spells.IceSpark;
import me.rok.magicalwands.ice.spells.IceSparkWave;
import me.rok.magicalwands.ice.spells.IceTeleport;
import me.rok.magicalwands.ice.spells.IceTrail;
import me.rok.magicalwands.ice.spells.IceWave;
import me.rok.magicalwands.mortiferum.customitems.MortiferumBow;
import me.rok.magicalwands.mortiferum.customitems.MortiferumBowListener;
import me.rok.magicalwands.nature.spells.NatureExplosion;
import me.rok.magicalwands.nature.spells.NatureFountain;
import me.rok.magicalwands.nature.spells.NatureHeal;
import me.rok.magicalwands.nature.spells.NatureHealListener;
import me.rok.magicalwands.nature.spells.NatureHealPlayer;
import me.rok.magicalwands.nature.spells.NatureLeap;
import me.rok.magicalwands.nature.spells.NatureSpark;
import me.rok.magicalwands.nature.spells.NatureSparkWave;
import me.rok.magicalwands.nature.spells.NatureTrail;
import me.rok.magicalwands.nature.spells.NatureWave;
import me.rok.magicalwands.nyxara.customitems.NyxaraBow;
import me.rok.magicalwands.nyxara.customitems.NyxaraBowListener;
import me.rok.magicalwands.nyxara.spells.ShadowVeil;

public class Main
extends JavaPlugin
implements Listener {
    private static Main plugin;

    public static Main getPlugin() {
        return plugin;
    }

    
    
    public void onEnable() {
        PluginManager pm = Bukkit.getPluginManager();
        getConfig().addDefault("wand.fire", true);
        saveDefaultConfig();
        pm.registerEvents((Listener)new FlameWand(), (Plugin)this);
        pm.registerEvents((Listener)new me.lars.game.event.NatureWand(), (Plugin)this);
        pm.registerEvents((Listener)new me.lars.game.event.IceWand(), (Plugin)this);
        pm.registerEvents((Listener)new me.lars.game.event.FlowerWand(), (Plugin)this);
        pm.registerEvents((Listener)new IgnatiusBowListener(), (Plugin)this);
        pm.registerEvents((Listener)new IgnatiusBow(), (Plugin)this);
        pm.registerEvents((Listener)new NyxaraBowListener(), (Plugin)this);
        pm.registerEvents((Listener)new NyxaraBow(), (Plugin)this);
        pm.registerEvents((Listener)new MortiferumBowListener(), (Plugin)this);
        pm.registerEvents((Listener)new MortiferumBow(), (Plugin)this);
        pm.registerEvents((Listener)new HaldirionBowListener(), (Plugin)this);
        pm.registerEvents((Listener)new HaldirionSword(), (Plugin)this);
        pm.registerEvents((Listener)new HaldirionBow(), (Plugin)this);
        pm.registerEvents((Listener)new ShadowVeil(), (Plugin)this);
        // FIRE SPELLS //
        pm.registerEvents((Listener)new FirePulse(), (Plugin)this);
        pm.registerEvents((Listener)new FireTrail(), (Plugin)this);
        pm.registerEvents((Listener)new FireSpark(), (Plugin)this);
        pm.registerEvents((Listener)new FireAura(), (Plugin)this);
        pm.registerEvents((Listener)new FireLaunch(), (Plugin)this);
        pm.registerEvents((Listener)new FireComet(), (Plugin)this);
        pm.registerEvents((Listener)new FireDisappear(), (Plugin)this);
        pm.registerEvents((Listener)new FireWave(), (Plugin)this);
        pm.registerEvents((Listener)new FireLeap(), (Plugin)this);
        pm.registerEvents((Listener)new FireTeleport(), (Plugin)this);
        
        //ICE SPELLS//
        pm.registerEvents((Listener)new IceDisappear(), (Plugin)this);
        pm.registerEvents((Listener)new IceWave(), (Plugin)this);
        pm.registerEvents((Listener)new IceTeleport(), (Plugin)this);
        pm.registerEvents((Listener)new IceSpark(), (Plugin)this);
        pm.registerEvents((Listener)new IceLaunch(), (Plugin)this);
        pm.registerEvents((Listener)new IceLeap(), (Plugin)this);
        pm.registerEvents((Listener)new IceSparkWave(), (Plugin)this);
        pm.registerEvents((Listener)new IceTrail(), (Plugin)this);
        pm.registerEvents((Listener)new IceAura(), (Plugin)this);
        pm.registerEvents((Listener)new IceBlind(), (Plugin)this);
        
      //NATURE SPELLS//
        pm.registerEvents((Listener)new NatureLeap(), (Plugin)this);
        pm.registerEvents((Listener)new NatureWave(), (Plugin)this);
        pm.registerEvents((Listener)new NatureSparkWave(), (Plugin)this);
        pm.registerEvents((Listener)new NatureHeal(), (Plugin)this);
        pm.registerEvents((Listener)new NatureHealListener(), (Plugin)this);
        pm.registerEvents((Listener)new NatureTrail(), (Plugin)this);
        pm.registerEvents((Listener)new NatureSpark(), (Plugin)this);
        pm.registerEvents((Listener)new NatureExplosion(), (Plugin)this);
        pm.registerEvents((Listener)new NatureFountain(), (Plugin)this);
        pm.registerEvents((Listener)new NatureHealPlayer(), (Plugin)this);
        
        // FLOWER SPELLS //
        pm.registerEvents((Listener)new FlowerWave(), (Plugin)this);
        pm.registerEvents((Listener)new FlowerShockwave(), (Plugin)this);
        Wands Wands2 = new Wands();
        IceWand Icewand = new IceWand();
        NatureWand naturewand = new NatureWand();
        FlowerWand flowerwand = new FlowerWand();
        HaldirionItems HaldirionBow2 = new HaldirionItems();
        HaldirionItems HaldirionSword2 = new HaldirionItems();
        NyxaraItems NyxaraBow2 = new NyxaraItems();
        FireItems IgnatiusBow2 = new FireItems();
        MortiferumItems MortiferumBow2 = new MortiferumItems();
        ItemStack IgnatiusWand2 = Wands.IgnatiusWand;
        Bukkit.getServer().getPluginManager().registerEvents((Listener)this, (Plugin)this);
        this.getCommand("MagicWands").setExecutor((CommandExecutor)new ItemCommand());
    }


    	
    public void onDisable() {
    }
}

