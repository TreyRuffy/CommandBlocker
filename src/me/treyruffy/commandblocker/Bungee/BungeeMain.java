package me.treyruffy.commandblocker.bungee;

import java.io.IOException;

import me.treyruffy.commandblocker.CopyConfigs;
import me.treyruffy.commandblocker.Universal;
import me.treyruffy.commandblocker.bungee.commands.CommandBlockerCommand;
import me.treyruffy.commandblocker.bungee.config.BungeeConfigManager;
import me.treyruffy.commandblocker.bungee.listeners.BungeeBlock;
import me.treyruffy.commandblocker.bungee.listeners.BungeeCommandValueListener;
import me.treyruffy.commandblocker.bungee.listeners.TabCompletion;
import me.treyruffy.commandblocker.bungee.listeners.Update;
import me.treyruffy.commandblocker.updater.UpdateChecker;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
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
		getProxy().getPluginManager().registerListener(this, new BungeeCommandValueListener());
	    loadConfigManager();
	    
	    if (!BungeeConfigManager.getConfig().getString("Version").equalsIgnoreCase(this.getDescription().getVersion())) {
	    	CopyConfigs.duplicateOldSettings(Universal.get().getMethods());
	    	BungeeConfigManager.getConfig().set("Version", this.getDescription().getVersion());
	    	BungeeConfigManager.saveConfig();
	    }
	    
	    getProxy().getPluginManager().registerCommand(this, new CommandBlockerCommand());
	    updateCheck();
	}
	
	public void loadConfigManager() {
		try {
			BungeeConfigManager.reloadConfig();
			BungeeConfigManager.reloadDisabled();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void updateCheck() {
		String latestUpdate = UpdateChecker.request("5280", "TreysCommandBlocker v" + this.getDescription().getVersion() + " BungeeCord");
		if (latestUpdate.equals("")) {
			return;
		}
		int latestUpdateVersion = Integer.parseInt(latestUpdate.replace(".", ""));
		int versionOn = Integer.parseInt(this.getDescription().getVersion().replace(".", ""));
		if (latestUpdateVersion <= versionOn) {
			return;
		}
		
		getProxy().getScheduler().runAsync(this, () -> {
			CommandSender c = getProxy().getConsole();
			c.sendMessage(new TextComponent(ChatColor.AQUA + "+=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=+"));
			c.sendMessage(new TextComponent(ChatColor.GREEN + "There is a new update (" + latestUpdate + ") for"));
			c.sendMessage(new TextComponent(ChatColor.GREEN + "Trey's Command Blocker"));
			c.sendMessage(new TextComponent(ChatColor.RED + "Download at:"));
			c.sendMessage(new TextComponent(ChatColor.LIGHT_PURPLE + "https://www.spigotmc.org/resources/5280/"));
			c.sendMessage(new TextComponent(ChatColor.AQUA + "+=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=+"));
		});
	}
	
}
