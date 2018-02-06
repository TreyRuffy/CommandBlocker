package me.treyruffy.commandblocker.Bukkit;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import me.treyruffy.commandblocker.Universal;
import me.treyruffy.commandblocker.Bukkit.Listeners.BukkitBlock;
import me.treyruffy.commandblocker.Bukkit.Listeners.Commands;
import me.treyruffy.commandblocker.Bukkit.Listeners.Packets;
import me.treyruffy.commandblocker.Bukkit.Listeners.Update;
import me.treyruffy.commandblocker.Updater.Updates;

public class BukkitMain extends JavaPlugin implements Listener {

	private static BukkitMain instance;
	
	public static BukkitMain get() {
		return instance;
	}
	
	@Override
	public void onEnable() {
		instance = this;
		Universal.get().setup(new BukkitMethods());
		
		getServer().getPluginManager().registerEvents(this, this);
	    getServer().getPluginManager().registerEvents(new Update(), this);
	    getServer().getPluginManager().registerEvents(new BukkitBlock(), this);
	    loadConfigManager();
	    
	    getCommand("cb").setExecutor(new Commands());
	    getCommand("commandblocker").setExecutor(new Commands());
	    if (Bukkit.getPluginManager().isPluginEnabled("ProtocolLib")){
	    	Packets.protocol(this);
	    }
	}
	
	public static Boolean isPapiEnabled() {
		return Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI");
	}
	
	public void loadConfigManager(){
		new ConfigManager();
		ConfigManager.reloadConfig();
		ConfigManager.reloadDisabled();
		ConfigManager.reloadOpDisabled();
	}

}