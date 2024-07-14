/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.event.Listener
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.PluginManager
 *  org.bukkit.plugin.java.JavaPlugin
 */
package me.rok.main;

import me.rok.main.MainL10;
import me.rok.main.command.RokMessageCommand;
import me.rok.main.command.TestCommand;
import me.rok.main.listener.Event;
import me.rok.main.listener.LetterMenu;
import me.rok.main.listener.MessageMenuListener;
import me.rok.main.listener.MessagingSystem;
import me.rok.main.listener.PlayerPaperData;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main
extends JavaPlugin
implements Listener {
    public void onEnable() {
        new MainL10().a(this.getDataFolder().getParent());
        this.getConfig().options().copyDefaults(true);
        this.saveDefaultConfig();
        this.saveConfig();
        PlayerPaperData.setup();
        MessagingSystem.setup();
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents((Listener)new Event(), (Plugin)this);
        pm.registerEvents((Listener)new MessagingSystem(), (Plugin)this);
        pm.registerEvents((Listener)new MessageMenuListener(), (Plugin)this);
        pm.registerEvents((Listener)new LetterMenu(), (Plugin)this);
        Bukkit.getServer().getPluginManager().registerEvents((Listener)this, (Plugin)this);
        this.getCommand("test").setExecutor((CommandExecutor)new TestCommand());
        this.getCommand("rokmessage").setExecutor((CommandExecutor)new RokMessageCommand());
    }

    public void onDisable() {
    }
}

