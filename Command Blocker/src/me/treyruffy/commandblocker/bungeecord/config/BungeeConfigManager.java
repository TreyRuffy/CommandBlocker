package me.treyruffy.commandblocker.bungeecord.config;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;

import com.google.common.io.ByteStreams;

import me.treyruffy.commandblocker.bungeecord.BungeeMain;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class BungeeConfigManager {

	private static BungeeMain plugin = BungeeMain.get();
	
	public static Configuration MainConfig;
	public static File MainConfigFile;
	
	public static Configuration MainDisabled;
	public static File MainDisabledFile;
	
	public void setup() {
		if (!plugin.getDataFolder().exists()) {
			plugin.getDataFolder().mkdir();
		}
		
		MainConfigFile = new File(plugin.getDataFolder(), "config.yml");
		MainDisabledFile = new File(plugin.getDataFolder(), "disabled.yml");
		
		if (!MainConfigFile.exists()) {
			try {
				MainConfigFile.createNewFile();
				try (InputStream is = plugin.getResourceAsStream("config.yml");
						OutputStream os = new FileOutputStream(MainConfigFile)) {
					ByteStreams.copy(is, os);
				}
			} catch (IOException e) {
				ProxyServer.getInstance().getLogger().log(Level.SEVERE, "Could not save " + MainConfigFile + ".", e);
			}
		}
		if (!MainDisabledFile.exists()) {
			try {
				MainDisabledFile.createNewFile();
				try (InputStream is = plugin.getResourceAsStream("disabled.yml");
						OutputStream os = new FileOutputStream(MainDisabledFile)) {
					ByteStreams.copy(is, os);
				}
			} catch (IOException e) {
				ProxyServer.getInstance().getLogger().log(Level.SEVERE, "Could not save " + MainDisabledFile + ".", e);
			}
		}
		
		try {
			MainConfig = ConfigurationProvider.getProvider(YamlConfiguration.class).load(MainConfigFile);
			MainDisabled = ConfigurationProvider.getProvider(YamlConfiguration.class).load(MainDisabledFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static Configuration getConfig() {
		if (MainConfig == null) {
			try {
				reloadConfig();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return MainConfig; 
	}
	public static Configuration getDisabled() {
		if (MainDisabled == null) {
			try {
				reloadDisabled();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return MainDisabled; 
	}
	
	public static void saveConfig() {
		if (MainConfig == null) {
			try {
				throw new Exception("Cannot save a non-existent file!");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		try {
			ConfigurationProvider.getProvider(YamlConfiguration.class).save(MainConfig, MainConfigFile);
		} catch (IOException e) {
			ProxyServer.getInstance().getLogger().log(Level.SEVERE, "Could not save " + MainConfigFile + ".", e);
		}
	}
	public static void saveDisabled() {
		if (MainDisabled == null) {
			try {
				throw new Exception("Cannot save a non-existent file!");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		try {
			ConfigurationProvider.getProvider(YamlConfiguration.class).save(MainDisabled, MainDisabledFile);
		} catch (IOException e) {
			ProxyServer.getInstance().getLogger().log(Level.SEVERE, "Could not save " + MainDisabledFile + ".", e);
		}
	}
	
	public static void reloadConfig() throws IOException {
		if (!plugin.getDataFolder().exists()) {
			plugin.getDataFolder().mkdir();
		}
		
		MainConfigFile = new File(plugin.getDataFolder(), "config.yml");
		if (!MainConfigFile.exists()) {
			try{
				MainConfigFile.createNewFile();
				try (InputStream is = plugin.getResourceAsStream("config.yml");
						OutputStream os = new FileOutputStream(MainConfigFile)) {
					ByteStreams.copy(is, os);
				}
			} catch (IOException e) {
				ProxyServer.getInstance().getLogger().log(Level.SEVERE, "Could not save " + MainConfigFile + ".", e);
				throw e;
			}
		}
		try {
			MainConfig = ConfigurationProvider.getProvider(YamlConfiguration.class).load(MainConfigFile);
		} catch (IOException e1) {
			throw e1;
		}
		InputStream configData = plugin.getResourceAsStream("config.yml");
		if (configData != null) {
			try {
				ConfigurationProvider.getProvider(YamlConfiguration.class).save(MainConfig, new File(plugin.getDataFolder(), "config.yml"));
			} catch (IOException e) {
				throw e;
			}
		}
	}
	public static void reloadDisabled() throws IOException {
		if (!plugin.getDataFolder().exists()) {
			plugin.getDataFolder().mkdir();
		}
		
		MainDisabledFile = new File(plugin.getDataFolder(), "disabled.yml");
		if (!MainDisabledFile.exists()) {
			try {
				MainDisabledFile.createNewFile();
				try (InputStream is = plugin.getResourceAsStream("disabled.yml");
						OutputStream os = new FileOutputStream(MainDisabledFile)) {
					ByteStreams.copy(is, os);
				}
			} catch (IOException e) {
				ProxyServer.getInstance().getLogger().log(Level.SEVERE, "Could not save " + MainDisabledFile + ".", e);
				throw e;
			}
		}
		try {
			MainDisabled = ConfigurationProvider.getProvider(YamlConfiguration.class).load(MainDisabledFile);
		} catch (IOException e1) {
			throw e1;
		}
		InputStream disabledData = plugin.getResourceAsStream("disabled.yml");
		if (disabledData != null) {
			try {
				ConfigurationProvider.getProvider(YamlConfiguration.class).save(MainDisabled, new File(plugin.getDataFolder(), "disabled.yml"));
			} catch (IOException e) {
				throw e;
			}
		}
	}
}