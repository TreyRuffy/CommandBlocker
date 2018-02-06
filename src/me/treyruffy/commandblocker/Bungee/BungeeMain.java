package me.treyruffy.commandblocker.Bungee;

import me.treyruffy.commandblocker.Universal;
import me.treyruffy.commandblocker.Bungee.Listeners.BungeeBlock;
import me.treyruffy.commandblocker.Bungee.Listeners.Commands;
import me.treyruffy.commandblocker.Bungee.Listeners.TabCompletion;
import me.treyruffy.commandblocker.Bungee.Listeners.Update;
import me.treyruffy.commandblocker.Bungee.BungeeConfigManager;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;

public class BungeeMain extends Plugin implements Listener {

	private static BungeeMain instance;
	
	public static BungeeMain get() {
		return instance;
	}
	
	@Override
	public void onEnable() {
		if (!getDataFolder().exists()) {
			getDataFolder().mkdir();
		}
		
		instance = this;
		Universal.get().setup(new BungeeMethods());
		
		getProxy().getPluginManager().registerListener(this, this);
		getProxy().getPluginManager().registerListener(this, new Update());
		getProxy().getPluginManager().registerListener(this, new BungeeBlock());
		getProxy().getPluginManager().registerListener(this, new TabCompletion());
	    loadConfigManager();
	    
	    getProxy().getPluginManager().registerCommand(this, new Commands());
	}
	
	public void loadConfigManager(){
		BungeeConfigManager.reloadConfig();
		BungeeConfigManager.reloadDisabled();
	}
}
