/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.event.Listener
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.java.JavaPlugin
 */
package me.rok.main;

import me.rok.main.MainL10;
import me.rok.main.command.RPCommand;
import me.rok.main.command.TestCommand;
import me.rok.main.command.Undisguise;
import me.rok.main.command.configReloadCommand;
import me.rok.main.gui.RPDisguiseGUI;
import me.rok.main.gui.RPDisguiseNormal;
import me.rok.main.gui.RPDisguiseSpecial;
import me.rok.main.listener.Eldarion.EldarionWalkingEffect;
import me.rok.main.listener.EventManager;
import me.rok.main.listener.Haldirion.HaldirionDamage;
import me.rok.main.listener.Haldirion.HaldirionHeal;
import me.rok.main.listener.Haldirion.HaldirionHealing;
import me.rok.main.listener.Haldirion.HaldirionSpeed;
import me.rok.main.listener.Haldirion.HaldirionWalkingEffect;
import me.rok.main.listener.Ignatius.IgnatiusFireDamage;
import me.rok.main.listener.Ignatius.IgnatiusSword;
import me.rok.main.listener.Ignatius.IgnatiusWalkingEffect;
import me.rok.main.listener.Mortiferum.WalkingEffect;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class Main
extends JavaPlugin
implements Listener {
    public void onEnable() {
        new MainL10().a(this.getDataFolder().getParent());
        this.getConfig().options().copyDefaults(true);
        this.saveDefaultConfig();
        this.saveConfig();
        this.getServer().getPluginManager().registerEvents((Listener)new RPDisguiseNormal(), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new RPDisguiseSpecial(), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new RPDisguiseGUI(), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new EventManager(), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new Undisguise(), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new WalkingEffect(), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new EldarionWalkingEffect(), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new IgnatiusSword(), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new IgnatiusWalkingEffect(), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new IgnatiusFireDamage(), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new HaldirionDamage(), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new HaldirionWalkingEffect(), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new HaldirionSpeed(), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new HaldirionHeal(), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new HaldirionHealing(), (Plugin)this);
        Bukkit.getServer().getPluginManager().registerEvents((Listener)this, (Plugin)this);
        this.getCommand("reloadconfig").setExecutor((CommandExecutor)new configReloadCommand());
        this.getCommand("disguise").setExecutor((CommandExecutor)new TestCommand());
        this.getCommand("toggleffects").setExecutor((CommandExecutor)new EventManager());
        this.getCommand("rp").setExecutor((CommandExecutor)new RPCommand());
    }

    public void onDisable() {
    }
}

