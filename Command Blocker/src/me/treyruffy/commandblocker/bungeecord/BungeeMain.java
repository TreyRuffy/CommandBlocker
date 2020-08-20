package me.treyruffy.commandblocker.bungeecord;

import java.io.IOException;

import me.treyruffy.commandblocker.CopyConfigs;
import me.treyruffy.commandblocker.Universal;
import me.treyruffy.commandblocker.bungeecord.commands.CommandBlockerCommand;
import me.treyruffy.commandblocker.bungeecord.config.BungeeConfigManager;
import me.treyruffy.commandblocker.bungeecord.config.Messages;
import me.treyruffy.commandblocker.bungeecord.listeners.BungeeBlock;
import me.treyruffy.commandblocker.bungeecord.listeners.BungeeCommandValueListener;
import me.treyruffy.commandblocker.bungeecord.listeners.TabCompletion;
import me.treyruffy.commandblocker.bungeecord.listeners.Update;
import me.treyruffy.commandblocker.updater.UpdateChecker;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;

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
		fixCommands();
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
		String latestUpdate = UpdateChecker.request("5280", "CommandBlocker v" + this.getDescription().getVersion() + " BungeeCord");
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
			for (String message1 : Messages.getMessages("Updates", "UpdateFound")) {
				c.sendMessage(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', message1)));
			}
		});
	}

	public void fixCommands() {
		Configuration configuration = BungeeConfigManager.getDisabled().getSection("DisabledCommands");
		for (String cmds : configuration.getKeys()) {
			String path = cmds.substring(0, 1).toUpperCase() + cmds.substring(1).toLowerCase();
			if (!Character.isUpperCase(cmds.charAt(0))) {
				configuration.set(path, configuration.get(cmds));
				configuration.set(cmds, null);
				BungeeConfigManager.saveDisabled();
			}
			if (!cmds.substring(1).equals(cmds.substring(1).toLowerCase())) {
				configuration.set(path, configuration.get(cmds));
				configuration.set(cmds, null);
				BungeeConfigManager.saveDisabled();
			}
		}
	}

}
