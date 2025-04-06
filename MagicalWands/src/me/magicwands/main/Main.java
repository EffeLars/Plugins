package me.magicwands.main;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import me.magicwands.commands.MagicWandsCommand;
import me.magicwands.commands.MagicWandsTabCompleter;
import me.magicwands.commands.StaffCommands;
import me.magicwands.customitems.HornOfValere;
import me.magicwands.customitems.LightBow;
import me.magicwands.customitems.LightBowListener;
import me.magicwands.customitems.LightSword;
import me.magicwands.dark.DarkAbyss;
import me.magicwands.dark.DarkAura;
import me.magicwands.dark.DarkCloud;
import me.magicwands.dark.DarkDisappear;
import me.magicwands.dark.DarkLaunch;
import me.magicwands.dark.DarkLeap;
import me.magicwands.dark.DarkPulse;
import me.magicwands.dark.DarkSpark;
import me.magicwands.dark.DarkTeleport;
import me.magicwands.dark.DarkTrail;
import me.magicwands.dark.DarkWave;
import me.magicwands.empire.EmpireAura;
import me.magicwands.empire.EmpireBlind;
import me.magicwands.empire.EmpireConfuse;
import me.magicwands.empire.EmpireConfusionWave;
import me.magicwands.empire.EmpireEscape;
import me.magicwands.empire.EmpireExplosion;
import me.magicwands.empire.EmpireExplosiveWave;
import me.magicwands.empire.EmpireHunt;
import me.magicwands.empire.EmpireKratisMadness;
import me.magicwands.empire.EmpireLaunch;
import me.magicwands.empire.EmpireLeap;
import me.magicwands.empire.EmpirePoisonWave;
import me.magicwands.empire.EmpireShockwave;
import me.magicwands.empire.EmpireSpark;
import me.magicwands.events.FlameWand;
import me.magicwands.fire.FireAura;
import me.magicwands.fire.FireComet;
import me.magicwands.fire.FireDisappear;
import me.magicwands.fire.FireExplosion;
import me.magicwands.fire.FireLaunch;
import me.magicwands.fire.FireLeap;
import me.magicwands.fire.FirePulse;
import me.magicwands.fire.FireSpark;
import me.magicwands.fire.FireTeleport;
import me.magicwands.fire.FireTrail;
import me.magicwands.fire.FireWave;
import me.magicwands.ice.IceAura;
import me.magicwands.ice.IceBlind;
import me.magicwands.ice.IceDisappear;
import me.magicwands.ice.IceDome;
import me.magicwands.ice.IceLaunch;
import me.magicwands.ice.IceLeap;
import me.magicwands.ice.IcePermaFrost;
import me.magicwands.ice.IceSpark;
import me.magicwands.ice.IceSparkWave;
import me.magicwands.ice.IceTeleport;
import me.magicwands.ice.IceTrail;
import me.magicwands.ice.IceWave;
import me.magicwands.light.LightAura;
import me.magicwands.light.LightCloud;
import me.magicwands.light.LightDisappear;
import me.magicwands.light.LightExecutioner;
import me.magicwands.light.LightLightning;
import me.magicwands.light.LightRage;
import me.magicwands.light.LightTeleport;
import me.magicwands.light.LightTimeTurn;
import me.magicwands.light.LightVortex;
import me.magicwands.light.LightWave;
import me.magicwands.nature.NatureExplosion;
import me.magicwands.nature.NatureExplosiveWave;
import me.magicwands.nature.NatureFountain;
import me.magicwands.nature.NatureFreeze;
import me.magicwands.nature.NatureHeal;
import me.magicwands.nature.NatureHealListener;
import me.magicwands.nature.NatureHealPlayer;
import me.magicwands.nature.NatureLeap;
import me.magicwands.nature.NatureSpark;
import me.magicwands.nature.NatureSparkWave;
import me.magicwands.nature.NatureTrail;
import me.magicwands.nature.NatureWave;
import me.magicwands.utils.WandManager;
import me.magicwands.wands.CustomItems;
import me.magicwands.wands.DarkWand;
import me.magicwands.wands.FireItems;
import me.magicwands.wands.FireWand;
import me.magicwands.wands.IceItems;
import me.magicwands.wands.IceWand;
import me.magicwands.wands.LightItems;
import me.magicwands.wands.LightWand;
import me.magicwands.wands.NatureItems;
import me.magicwands.wands.NatureWand;
import me.magicwands.wands.WandItems;
import me.magicwands.wands.WitchItems;
import me.magicwands.wands.WitchWand;
import me.magicwands.witch.WitchAura;
import me.magicwands.witch.WitchDisappear;
import me.magicwands.witch.WitchLaunch;
import me.magicwands.witch.WitchLeap;
import me.magicwands.witch.WitchSpark;
import me.magicwands.witch.WitchTeleport;
import me.magicwands.witch.WitchTrail;
import me.magicwands.witch.WitchWave;

public class Main
extends JavaPlugin
implements Listener {
    private static Main plugin;

    public static Main getPlugin() {
        return plugin;
    }

    
    @SuppressWarnings("unused")
	public void onEnable() {
        PluginManager pm = Bukkit.getPluginManager();
        getConfig().addDefault("wand.fire", true);
        saveDefaultConfig();
        String[] wandTypes = {"light", "dark", "nature", "fire", "ice", "witch", "empire"};

        for (String wandName : wandTypes) {
            checkAndSetDefaultWand(wandName);
        }
        
        String[] ItemTypes = {"horn", "lightsword", "naturebow", "firebow", "icebow"};
        
        for (String ItemName : ItemTypes) {
            checkAndSetDefaultItem(ItemName);
        }
        pm.registerEvents((Listener)new FlameWand(), (Plugin)this);
        pm.registerEvents((Listener)new me.magicwands.events.EmpireWand(), (Plugin)this);
        pm.registerEvents((Listener)new me.magicwands.events.WitchWand(), (Plugin)this);
        pm.registerEvents((Listener)new StaffCommands(), (Plugin)this);
        pm.registerEvents((Listener)new me.magicwands.events.DarkWand(), (Plugin)this);
        pm.registerEvents((Listener)new LightWand(), (Plugin)this);
        pm.registerEvents((Listener)new me.magicwands.events.NatureWand(), (Plugin)this);
        pm.registerEvents((Listener)new me.magicwands.events.IceWand(), (Plugin)this);
        pm.registerEvents((Listener)new me.magicwands.events.LightWand(), (Plugin)this);
        
        pm.registerEvents((Listener)new HornOfValere(), (Plugin)this);
        pm.registerEvents((Listener)new FireItems(), (Plugin)this);
        pm.registerEvents((Listener)new WandManager(), (Plugin)this);
      
        pm.registerEvents((Listener)new FireItems(), (Plugin)this);
        pm.registerEvents((Listener)new IceItems(), (Plugin)this);
        pm.registerEvents((Listener)new LightBowListener(), (Plugin)this);
        pm.registerEvents((Listener)new LightSword(), (Plugin)this);
        pm.registerEvents((Listener)new LightBow(), (Plugin)this);
        pm.registerEvents((Listener)new HornOfValere(), (Plugin)this);
        // FIRE SPELLS //
        pm.registerEvents((Listener)new FirePulse(), (Plugin)this);
        pm.registerEvents((Listener)new FireTrail(), (Plugin)this);
        pm.registerEvents((Listener)new FireSpark(), (Plugin)this);
        pm.registerEvents((Listener)new FireAura(), (Plugin)this);
        pm.registerEvents((Listener)new FireExplosion(), (Plugin)this);
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
        pm.registerEvents((Listener)new IceDome(), (Plugin)this);
        pm.registerEvents((Listener)new IcePermaFrost(), (Plugin)this);
        
      //EMPIRE SPELLS//
        pm.registerEvents((Listener)new EmpireLeap(), (Plugin)this);
        pm.registerEvents((Listener)new EmpireExplosion(), (Plugin)this);
        pm.registerEvents((Listener)new EmpireSpark(), (Plugin)this);
        pm.registerEvents((Listener)new EmpireLaunch(), (Plugin)this);
        pm.registerEvents((Listener)new EmpireConfuse(), (Plugin)this);
        pm.registerEvents((Listener)new EmpireConfusionWave(), (Plugin)this);
        pm.registerEvents((Listener)new EmpireBlind(), (Plugin)this);
        pm.registerEvents((Listener)new EmpirePoisonWave(), (Plugin)this);
        pm.registerEvents((Listener)new EmpireAura(), (Plugin)this);
        pm.registerEvents((Listener)new EmpireEscape(), (Plugin)this);
        pm.registerEvents((Listener)new EmpireExplosiveWave(), (Plugin)this);
        pm.registerEvents((Listener)new EmpireShockwave(), (Plugin)this);
        pm.registerEvents((Listener)new EmpireHunt(), (Plugin)this);
        pm.registerEvents((Listener)new EmpireKratisMadness(), (Plugin)this);
        
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
        pm.registerEvents((Listener)new NatureFreeze(), (Plugin)this);
        pm.registerEvents((Listener)new NatureExplosiveWave(), (Plugin)this);
        
      //NATURE SPELLS//
        pm.registerEvents((Listener)new LightLightning(), (Plugin)this);
        pm.registerEvents((Listener)new LightDisappear(), (Plugin)this);
        pm.registerEvents((Listener)new LightWave(), (Plugin)this);
        pm.registerEvents((Listener)new LightTeleport(), (Plugin)this);
        pm.registerEvents((Listener)new LightRage(), (Plugin)this);
        pm.registerEvents((Listener)new LightAura(), (Plugin)this);
        pm.registerEvents((Listener)new LightExecutioner(), (Plugin)this);
        pm.registerEvents((Listener)new LightTimeTurn(), (Plugin)this);
        pm.registerEvents((Listener)new LightVortex(), (Plugin)this);
        pm.registerEvents((Listener)new LightCloud(), (Plugin)this);
        
        // DARK SPELLS //
        pm.registerEvents((Listener)new DarkLeap(), (Plugin)this);
        pm.registerEvents((Listener)new DarkWave(), (Plugin)this);
        pm.registerEvents((Listener)new DarkPulse(), (Plugin)this);
        pm.registerEvents((Listener)new DarkSpark(), (Plugin)this);
        pm.registerEvents((Listener)new DarkAura(), (Plugin)this);
        pm.registerEvents((Listener)new DarkTeleport(), (Plugin)this);
        pm.registerEvents((Listener)new DarkTrail(), (Plugin)this);
        pm.registerEvents((Listener)new DarkDisappear(), (Plugin)this);
        pm.registerEvents((Listener)new DarkLaunch(), (Plugin)this);
        pm.registerEvents((Listener)new DarkCloud(), (Plugin)this);
        pm.registerEvents((Listener)new DarkAbyss(), (Plugin)this);
        
        // WITCH SPELLS //
        pm.registerEvents((Listener)new WitchLeap(), (Plugin)this);
        pm.registerEvents((Listener)new WitchWave(), (Plugin)this);
        pm.registerEvents((Listener)new WitchSpark(), (Plugin)this);
        pm.registerEvents((Listener)new WitchAura(), (Plugin)this);
        pm.registerEvents((Listener)new WitchTeleport(), (Plugin)this);
        pm.registerEvents((Listener)new WitchTrail(), (Plugin)this);
        pm.registerEvents((Listener)new WitchDisappear(), (Plugin)this);
        pm.registerEvents((Listener)new WitchLaunch(), (Plugin)this);
      //  pm.registerEvents((Listener)new LightLevitate(), (Plugin)this);
        FlameWand Wands2 = new FlameWand();
        FireWand FireWand = new FireWand();
        IceWand Icewand = new IceWand();
        WandItems wands = new WandItems();
        NatureWand naturewand = new NatureWand();
        DarkWand DarkWand = new DarkWand();
        LightItems HaldirionBow2 = new LightItems();
        LightItems HaldirionSword2 = new LightItems();
        WitchItems NyxaraBow2 = new WitchItems();
        CustomItems HornOfValere = new CustomItems();
        WitchWand WitchWand = new WitchWand();
        FireItems IgnatiusBow2 = new FireItems();
        NatureItems MortiferumBow2 = new NatureItems();
        Bukkit.getServer().getPluginManager().registerEvents((Listener)this, (Plugin)this);
        this.getCommand("MagicWands").setExecutor(new MagicWandsCommand());
        this.getCommand("mw").setExecutor(new MagicWandsCommand());
        this.getCommand("MagicWands").setTabCompleter(new MagicWandsTabCompleter());
        this.getCommand("mw").setTabCompleter(new MagicWandsTabCompleter());
    }


    private void checkAndSetDefaultWand(String wandName) {
    	if (!getConfig().contains("wands.plugin.prefix")) {
            getConfig().set("wands.plugin.prefix", "&7[&e&lMagic&6&lWands&7]");  
        }
        if (!getConfig().contains("wands." + wandName + ".color")) {
            getConfig().set("wands." + wandName + ".color", "4");  
        }
        if (!getConfig().contains("wands." + wandName + ".item")) {
            getConfig().set("wands." + wandName + ".item", "BARRIER");  
        }
        if (!getConfig().contains("wands." + wandName + ".customname")) {
            getConfig().set("wands." + wandName + ".customname", "" + wandName.substring(0, 1).toUpperCase() + wandName.substring(1) + " Wand");  // Default custom name: Light Wand, etc.
        }
        if (!getConfig().contains("wands." + wandName + ".bowtime")) {
            getConfig().set("wands." + wandName + ".bowtime", 10);  
        }
        saveConfig();
    }
    
    @SuppressWarnings("unused")
	private void checkAndSetDefaultItem(String ItemName) {
        if (!getConfig().contains("items." + ItemName + ".color")) {
            getConfig().set("items." + ItemName + ".color", "4");  
        }
        if (!getConfig().contains("items." + ItemName + ".item")) {
            getConfig().set("items." + ItemName + ".item", "BARRIER");  
        }
        if (!getConfig().contains("items." + ItemName + ".customname")) {
            getConfig().set("items." + ItemName + ".customname", "" + ItemName.substring(0, 1).toUpperCase() + ItemName.substring(1) + " Item"); 
        }
        saveConfig();
    }
    
    public void onDisable() {
    }
}

