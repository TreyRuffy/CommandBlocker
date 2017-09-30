package me.treyruffy.commandblocker;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import me.treyruffy.commandblocker.Updater.Updates;

public class CommandBlocker extends JavaPlugin implements Listener {
	
	@Override
	public void onEnable(){
	    getServer().getPluginManager().registerEvents(this, this);
	    getServer().getPluginManager().registerEvents(new Updates(), this);
	    getServer().getPluginManager().registerEvents(new CommandBlock(), this);
	    loadConfigManager();
	    
	    this.getCommand("cb").setExecutor(new Commands());
	    this.getCommand("commandblocker").setExecutor(new Commands());
	    if (Bukkit.getPluginManager().getPlugin("ProtocolLib") != null){
	    	Packets.protocol(this);
	    }
	    String version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
		if (!(version.equals("v1_7_R1")||version.equals("v1_7_R2")||version.equals("v1_7_R3")||version.equals("v1_7_R4"))){
			Updates.updateCheck();
		}
	}
	
	public void loadConfigManager(){
		new ConfigManager();
		ConfigManager.reloadConfig();
		ConfigManager.reloadDisabled();
	}
}
