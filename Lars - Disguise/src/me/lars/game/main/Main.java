package me.lars.game.main;

import java.net.http.WebSocket.Listener;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import me.lars.game.command.WandCommand;
import me.lars.game.event.WandSpellSelector;

public class Main extends JavaPlugin implements Listener, org.bukkit.event.Listener {
	
	private static Main plugin;

	public static Main getPlugin() {
		return plugin;
	}
	public void onEnable( ) {
		PluginManager pm = Bukkit.getPluginManager();		 
		pm.registerEvents(new WandSpellSelector(), this);
		Bukkit.getServer().getPluginManager().registerEvents(this, this);
		getCommand("disguise").setExecutor(new WandCommand());
	}
	
	public void onDisable() {
		
	}
	
}
