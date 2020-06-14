package me.treyruffy.commandblockerlegacy;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;

import org.apache.commons.lang.NullArgumentException;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class OldConfigManager {
	
	public static FileConfiguration MainConfig;
	public static File MainConfigFile;
	
	public static FileConfiguration MainDisabled;
	public static File MainDisabledFile;
	
	public static FileConfiguration OpDisabled;
	public static File OpDisabledFile;
	
	public void setup() {
		if (!Bukkit.getPluginManager().getPlugin("CommandBlocker").getDataFolder().exists()) {
			Bukkit.getPluginManager().getPlugin("CommandBlocker").getDataFolder().mkdir();
		}
		
		MainConfigFile = new File(Bukkit.getPluginManager().getPlugin("CommandBlocker").getDataFolder(), "config.yml");
		MainDisabledFile = new File(Bukkit.getPluginManager().getPlugin("CommandBlocker").getDataFolder(), "disabled.yml");
		OpDisabledFile = new File(Bukkit.getPluginManager().getPlugin("CommandBlocker").getDataFolder(), "opblock.yml");
		
		if (!MainConfigFile.exists()) {
			try {
				MainConfigFile.createNewFile();
			} catch (IOException e) {
				Bukkit.getServer().getLogger().log(Level.SEVERE, "Could not save " + MainConfigFile + ".", e);
			}
		}
		
		if (!MainDisabledFile.exists()) {
			try {
				MainDisabledFile.createNewFile();
			} catch (IOException e) {
				Bukkit.getServer().getLogger().log(Level.SEVERE, "Could not save " + MainDisabledFile + ".", e);
			}
		}
		if (!OpDisabledFile.exists()) {
			try {
				OpDisabledFile.createNewFile();
			} catch (IOException e) {
				Bukkit.getServer().getLogger().log(Level.SEVERE, "Could not save " + OpDisabledFile + ".", e);
			}
		}
		
		MainConfig = YamlConfiguration.loadConfiguration(MainConfigFile);
		MainDisabled = YamlConfiguration.loadConfiguration(MainDisabledFile);
		OpDisabled = YamlConfiguration.loadConfiguration(OpDisabledFile);
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
	
	public static void reloadConfig() {
		MainConfigFile = new File(Bukkit.getPluginManager().getPlugin("CommandBlocker").getDataFolder(), "config.yml");
		if (!MainConfigFile.exists()) {
			Bukkit.getPluginManager().getPlugin("CommandBlocker").saveResource("config.yml", false);
		}
		MainConfig = YamlConfiguration.loadConfiguration(MainConfigFile);
		InputStream configData = Bukkit.getPluginManager().getPlugin("CommandBlocker").getResource("config.yml");
		if (configData != null) {
			try {
				MainConfig.setDefaults(YamlConfiguration.loadConfiguration(configData));
			} catch (NoSuchMethodError e) {
				MainConfig.setDefaults(YamlConfiguration.loadConfiguration(MainConfigFile));
			}
		}
	}
	public static void reloadDisabled() {
		MainDisabledFile = new File(Bukkit.getPluginManager().getPlugin("CommandBlocker").getDataFolder(), "disabled.yml");
		if (!MainDisabledFile.exists()) {
			Bukkit.getPluginManager().getPlugin("CommandBlocker").saveResource("disabled.yml", false);
		}
		MainDisabled = YamlConfiguration.loadConfiguration(MainDisabledFile);
		InputStream disabledData = Bukkit.getPluginManager().getPlugin("CommandBlocker").getResource("disabled.yml");
		if (disabledData != null) {
			try {
				MainDisabled.setDefaults(YamlConfiguration.loadConfiguration(disabledData));
			} catch (NoSuchMethodError e) {
				MainDisabled.setDefaults(YamlConfiguration.loadConfiguration(MainDisabledFile));
			}
		}
	}
	public static void reloadOpDisabled() {
		OpDisabledFile = new File(Bukkit.getPluginManager().getPlugin("CommandBlocker").getDataFolder(), "opblock.yml");
		if (!OpDisabledFile.exists()) {
			Bukkit.getPluginManager().getPlugin("CommandBlocker").saveResource("opblock.yml", false);
		}
		OpDisabled = YamlConfiguration.loadConfiguration(OpDisabledFile);
		InputStream opData = Bukkit.getPluginManager().getPlugin("CommandBlocker").getResource("opblock.yml");
		if (opData != null) {
			try {
				OpDisabled.setDefaults(YamlConfiguration.loadConfiguration(opData));
			} catch (NoSuchMethodError e) {
				OpDisabled.setDefaults(YamlConfiguration.loadConfiguration(OpDisabledFile));
			}
		}
	}
}
