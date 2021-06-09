package me.treyruffy.commandblocker.bungeecord;

import me.treyruffy.commandblocker.Universal;
import me.treyruffy.commandblocker.bungeecord.commands.CommandBlockerCommand;
import me.treyruffy.commandblocker.bungeecord.config.BungeeConfigManager;
import me.treyruffy.commandblocker.bungeecord.config.BungeeUpdateConfig;
import me.treyruffy.commandblocker.bungeecord.listeners.BungeeBlock;
import me.treyruffy.commandblocker.bungeecord.listeners.BungeeCommandValueListener;
import me.treyruffy.commandblocker.bungeecord.listeners.TabCompletion;
import me.treyruffy.commandblocker.bungeecord.listeners.Update;
import net.kyori.adventure.platform.bungeecord.BungeeAudiences;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class BungeeMain extends Plugin {

	private static BungeeMain instance;

	private static BungeeAudiences adventure;

	public static @NotNull BungeeAudiences adventure() {
		if (adventure == null) {
			throw new IllegalStateException("Tried to access Adventure when the plugin was disabled!");
		}
		return adventure;
	}

	public static BungeeMain get() {
		return instance;
	}
	
	@Override
	public void onEnable() {
		if (!getDataFolder().exists()) {
			getDataFolder().mkdir();
		}

		adventure = BungeeAudiences.create(this);
		instance = this;
		Universal.get().setup(new BungeeMethods());

		getProxy().getPluginManager().registerListener(this, new Update());
		getProxy().getPluginManager().registerListener(this, new BungeeBlock());
		getProxy().getPluginManager().registerListener(this, new TabCompletion());
		getProxy().getPluginManager().registerListener(this, new BungeeCommandValueListener());
		loadConfigManager();

		new BungeeUpdateConfig().setup();

		getProxy().getPluginManager().registerCommand(this, new CommandBlockerCommand());
		Update.updateCheck();
		fixCommands();
	}

	@Override
	public void onDisable() {
		if (adventure != null) {
			adventure.close();
			adventure = null;
		}
	}
	
	public void loadConfigManager() {
		try {
			BungeeConfigManager.reloadConfig();
			BungeeConfigManager.reloadDisabled();
			BungeeConfigManager.reloadMessages();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void fixCommands() {
		Configuration configuration = BungeeConfigManager.getDisabled().getSection("DisabledCommands");
		if (!configuration.getKeys().isEmpty()) {
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

}
