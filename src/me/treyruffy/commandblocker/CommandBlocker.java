package me.treyruffy.commandblocker;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandBlocker extends JavaPlugin implements Listener {
	public void onEnable(){
	    getServer().getPluginManager().registerEvents(this, this);
	    loadConfigManager();
	    new CommandBlock(this);
	    this.getCommand("cb").setExecutor(new Commands(this));
	    this.getCommand("commandblocker").setExecutor(new Commands(this));
	}
	
	public void loadConfigManager(){
		new ConfigManager();
		ConfigManager.reloadConfig();
		ConfigManager.reloadDisabled();
	}
}
