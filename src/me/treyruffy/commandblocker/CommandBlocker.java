package me.treyruffy.commandblocker;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandBlocker extends JavaPlugin implements Listener {
	
	@Override
	public void onEnable(){
	    getServer().getPluginManager().registerEvents(this, this);
	    loadConfigManager();
	    new CommandBlock(this);
	    this.getCommand("cb").setExecutor(new Commands());
	    this.getCommand("commandblocker").setExecutor(new Commands());
	    if (Bukkit.getPluginManager().getPlugin("ProtocolLib") != null){
	    	Packets.protocol(this);
	    }
	}
	
	public void loadConfigManager(){
		new ConfigManager();
		ConfigManager.reloadConfig();
		ConfigManager.reloadDisabled();
	}
	
}
