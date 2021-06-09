package me.treyruffy.commandblocker.bukkit.config;

import me.treyruffy.commandblocker.bukkit.BukkitMain;
import org.apache.commons.lang.NullArgumentException;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;

public class ConfigManager {

	private static final BukkitMain plugin = BukkitMain.get();

	public static FileConfiguration MainConfig;
	public static File MainConfigFile;

	public static FileConfiguration MainDisabled;
	public static File MainDisabledFile;

	public static FileConfiguration OpDisabled;
	public static File OpDisabledFile;

	public static FileConfiguration Messages;
	public static File MessagesFile;

	public static File getFileFromConfig(FileConfiguration configuration) {
		if (configuration.equals(MainConfig)) {
			return MainConfigFile;
		}
		if (configuration.equals(MainDisabled)) {
			return MainDisabledFile;
		}
		if (configuration.equals(OpDisabled)) {
			return OpDisabledFile;
		}
		if (configuration.equals(Messages)) {
			return MessagesFile;
		}
		return null;
	}

	public static FileConfiguration getConfig() {
		if (MainConfig == null) {
			reloadConfig();
		}
		return MainConfig; 
	}
	public static FileConfiguration getDisabled() {
		if (MainDisabled == null) {
			reloadDisabled();
		}
		return MainDisabled;
	}

	public static FileConfiguration getOpDisabled() {
		if (OpDisabled == null) {
			reloadOpDisabled();
		}
		return OpDisabled;
	}

	public static FileConfiguration getMessages() {
		if (Messages == null) {
			reloadMessages();
		}
		return Messages;
	}

	public static void saveConfig() {
		if (MainConfig == null) {
			throw new NullArgumentException("Cannot save a non-existent file!");
		}
		try {
			MainConfig.save(MainConfigFile);
		} catch (IOException e) {
			Bukkit.getServer().getLogger().log(Level.SEVERE, "Could not save " + MainConfigFile + ".", e);
		}
	}
	public static void saveDisabled() {
		if (MainDisabled == null) {
			throw new NullArgumentException("Cannot save a non-existent file!");
		}
		try {
			MainDisabled.save(MainDisabledFile);
		} catch (IOException e) {
			Bukkit.getServer().getLogger().log(Level.SEVERE, "Could not save " + MainDisabledFile + ".", e);
		}
	}

	public static void saveOpDisabled() {
		if (OpDisabled == null) {
			throw new NullArgumentException("Cannot save a non-existent file!");
		}
		try {
			OpDisabled.save(OpDisabledFile);
		} catch (IOException e) {
			Bukkit.getServer().getLogger().log(Level.SEVERE, "Could not save " + OpDisabledFile + ".", e);
		}
	}

	public static void saveMessages() {
		if (Messages == null) {
			throw new NullArgumentException("Cannot save a non-existent file!");
		}
		try {
			Messages.save(MessagesFile);
		} catch (IOException e) {
			Bukkit.getServer().getLogger().log(Level.SEVERE, "Could not save " + MessagesFile + ".", e);
		}
	}

	public static void reloadConfig() {
		MainConfigFile = new File(plugin.getDataFolder(), "config.yml");
		if (!MainConfigFile.exists()) {
			plugin.saveResource("config.yml", false);
		}
		MainConfig = YamlConfiguration.loadConfiguration(MainConfigFile);
		InputStream configData = plugin.getResource("config.yml");
		if (configData != null) {
			try {
				MainConfig.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(configData)));
			} catch (NoSuchMethodError e) {
				MainConfig.setDefaults(YamlConfiguration.loadConfiguration(MainConfigFile));
			}
		}
	}
	public static void reloadDisabled() {
		MainDisabledFile = new File(plugin.getDataFolder(), "disabled.yml");
		if (!MainDisabledFile.exists()) {
			plugin.saveResource("disabled.yml", false);
		}
		MainDisabled = YamlConfiguration.loadConfiguration(MainDisabledFile);
		InputStream disabledData = plugin.getResource("disabled.yml");
		if (disabledData != null) {
			try {
				MainDisabled.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(disabledData)));
			} catch (NoSuchMethodError e) {
				MainDisabled.setDefaults(YamlConfiguration.loadConfiguration(MainDisabledFile));
			}
		}
	}
	public static void reloadOpDisabled() {
		OpDisabledFile = new File(plugin.getDataFolder(), "opblock.yml");
		if (!OpDisabledFile.exists()) {
			plugin.saveResource("opblock.yml", false);
		}
		OpDisabled = YamlConfiguration.loadConfiguration(OpDisabledFile);
		InputStream opData = plugin.getResource("opblock.yml");
		if (opData != null) {
			try {
				OpDisabled.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(opData)));
			} catch (NoSuchMethodError e) {
				OpDisabled.setDefaults(YamlConfiguration.loadConfiguration(OpDisabledFile));
			}
		}
	}

	public static void reloadMessages() {
		MessagesFile = new File(plugin.getDataFolder(), "messages.yml");
		if (!MessagesFile.exists()) {
			plugin.saveResource("messages.yml", false);
		}
		Messages = YamlConfiguration.loadConfiguration(MessagesFile);
		InputStream messagesData = plugin.getResource("messages.yml");
		if (messagesData != null) {
			try {
				Messages.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(messagesData)));
			} catch (NoSuchMethodError e) {
				Messages.setDefaults(YamlConfiguration.loadConfiguration(MessagesFile));
			}
		}
	}


}
