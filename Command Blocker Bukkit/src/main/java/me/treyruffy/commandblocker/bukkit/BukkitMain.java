package me.treyruffy.commandblocker.bukkit;

import me.treyruffy.commandblocker.Universal;
import me.treyruffy.commandblocker.bukkit.commands.CommandBlockerCmd;
import me.treyruffy.commandblocker.bukkit.commands.CommandBlockerTabComplete;
import me.treyruffy.commandblocker.bukkit.config.BukkitUpdateConfig;
import me.treyruffy.commandblocker.bukkit.config.ConfigManager;
import me.treyruffy.commandblocker.bukkit.gui.DisabledGui;
import me.treyruffy.commandblocker.bukkit.listeners.*;
import me.treyruffy.commandblockerlegacy.OldConfigManager;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.logging.Level;

public class BukkitMain extends JavaPlugin {

	private static BukkitMain instance;

	public static BukkitMain get() {
		return instance;
	}

	private static BukkitAudiences adventure;

	public static @NotNull BukkitAudiences adventure() {
		if (adventure == null) {
			throw new IllegalStateException("Tried to access Adventure when the plugin was disabled!");
		}
		return adventure;
	}

	@Override
	public void onEnable() {
		if (needGson())
			return;
		if (!sendOldMessages())
			adventure = BukkitAudiences.create(this);
		instance = this;
		Universal.get().setup(new BukkitMethods());

		getServer().getPluginManager().registerEvents(new Update(), this);
		getServer().getPluginManager().registerEvents(new BukkitBlock(), this);
		getServer().getPluginManager().registerEvents(new CommandValueListener(), this);
		getServer().getPluginManager().registerEvents(new DisabledGui(), this);
    	if (newBlocks()) {
      		getServer().getPluginManager().registerEvents(new NewTabCompletionListener(), this);
		}
		loadConfigManager();

		new BukkitUpdateConfig().setup();
		Objects.requireNonNull(getCommand("cb")).setExecutor(new CommandBlockerCmd());
		Objects.requireNonNull(getCommand("cb")).setTabCompleter(new CommandBlockerTabComplete());
		Objects.requireNonNull(getCommand("commandblocker")).setExecutor(new CommandBlockerCmd());
		Objects.requireNonNull(getCommand("commandblocker")).setTabCompleter(new CommandBlockerTabComplete());
		if (Bukkit.getPluginManager().isPluginEnabled("ProtocolLib")) {
			Packets.protocol(this);
		}
		new UpdateManager().setup();
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

	public static Boolean isPapiEnabled() {
		return Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI");
	}
	
	public void loadConfigManager() {
		if (!oldConfig()) {
			ConfigManager.reloadConfig();
			ConfigManager.reloadDisabled();
			ConfigManager.reloadOpDisabled();
			ConfigManager.reloadMessages();
		} else {
			OldConfigManager.reloadConfig();
			OldConfigManager.reloadDisabled();
			OldConfigManager.reloadOpDisabled();
			OldConfigManager.reloadMessages();
		}
	}

	public static String getBukkitVersion() {
		String packageName = Bukkit.getServer().getClass().getPackage().getName();
		if (!packageName.contains("_R")) {
			return "";
		}
		return packageName.split("\\.")[3];
	}

	public static void fixCommands() {
		ConfigurationSection configuration = oldConfig() ? OldConfigManager.getDisabled().getConfigurationSection(
				"DisabledCommands") : ConfigManager.getDisabled().getConfigurationSection("DisabledCommands");
		if (configuration != null) {
			for (String cmds : configuration.getKeys(false)) {
				String path = cmds.substring(0, 1).toUpperCase() + cmds.substring(1).toLowerCase();
				if (!Character.isLetter(cmds.charAt(0))) {
					return;
				}
				if (!Character.isUpperCase(cmds.charAt(0))) {
					configuration.set(path, configuration.get(cmds));
					configuration.set(cmds, null);
					if (oldConfig()) {
						OldConfigManager.saveDisabled();
					} else {
						ConfigManager.saveDisabled();
					}
				}
				if (!cmds.substring(1).equals(cmds.substring(1).toLowerCase())) {
					configuration.set(path, configuration.get(cmds));
					configuration.set(cmds, null);
					if (oldConfig()) {
						OldConfigManager.saveDisabled();
					} else {
						ConfigManager.saveDisabled();
					}
				}
			}
		}
		ConfigurationSection opBlockConfiguration = oldConfig() ?
				OldConfigManager.getOpDisabled().getConfigurationSection(
				"DisabledOpCommands") : ConfigManager.getOpDisabled().getConfigurationSection("DisabledOpCommands");
		if (opBlockConfiguration != null) {
			for (String cmds : opBlockConfiguration.getKeys(false)) {
				String path = cmds.substring(0, 1).toUpperCase() + cmds.substring(1).toLowerCase();
				if (!Character.isLetter(cmds.charAt(0))) {
					return;
				}
				if (!Character.isUpperCase(cmds.charAt(0))) {
					opBlockConfiguration.set(path, opBlockConfiguration.get(cmds));
					opBlockConfiguration.set(cmds, null);
					if (oldConfig()) {
						OldConfigManager.saveOpDisabled();
					} else {
						ConfigManager.saveOpDisabled();
					}
				}
				if (!cmds.substring(1).equals(cmds.substring(1).toLowerCase())) {
					opBlockConfiguration.set(path, opBlockConfiguration.get(cmds));
					opBlockConfiguration.set(cmds, null);
					if (oldConfig()) {
						OldConfigManager.saveOpDisabled();
					} else {
						ConfigManager.saveOpDisabled();
					}
				}
			}
		}
	}

	public static boolean newBlocks() {
		if (legacyVersion()) return false;
		switch (getBukkitVersion()) {
			case "v1_7_R1":
			case "v1_7_R2":
			case "v1_7_R3":
			case "v1_7_R4":
			case "v1_8_R1":
			case "v1_8_R2":
			case "v1_8_R3":
			case "v1_9_R1":
			case "v1_9_R2":
			case "v1_10_R1":
			case "v1_11_R1":
			case "v1_12_R1":
				return false;
			default:
				return true;
		}
	}

	public static boolean oldConfig() {
		if (legacyVersion()) return true;
		if (getBukkitVersion().equals("v1_7_R1")) {
			return true;
		}
		return false;
	}

	public static boolean sendOldMessages() {
		if (legacyVersion())
			return true;
		switch (getBukkitVersion()) {
			case "v1_7_R1":
			case "v1_7_R2":
			case "v1_7_R3":
				return true;
			default:
				return false;
		}
	}

	public static boolean customSkulls() {
		if (sendOldMessages())
			return false;
		if (getBukkitVersion().equals("v1_7_R4"))
			return false;
		return true;
	}

	private static boolean legacyVersion() {
		switch (getBukkitVersion()) {
			case "v1_4_R1":
			case "v1_5_R1":
			case "v1_5_R2":
			case "v1_5_R3":
			case "v1_6_R1":
			case "v1_6_R2":
			case "v1_6_R3":
				return true;
			default:
				return false;
		}
	}

	public static boolean coloredGlassPane() {
		return !legacyVersion();
	}

	private boolean needGson() {
		try {
			Class.forName("com.google.gson.JsonElement");
			if (oldConfig())
				Class.forName("me.treyruffy.commandblockerlegacy.OldConfigManager");
			return false;
		} catch (ClassNotFoundException ignored) {
		}
		Bukkit.getLogger().log(Level.SEVERE,
				"Trey's Command Blocker needs Trey's Command Blocker Legacy to run on your server version" +
						". Please download at https://www.spigotmc.org/resources/command-blocker-legacy.82948/");
		getPluginLoader().disablePlugin(this);
		return true;
	}

}