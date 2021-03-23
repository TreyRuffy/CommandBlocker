package me.treyruffy.commandblocker.bungeecord.config;

import com.google.common.io.ByteStreams;
import me.treyruffy.commandblocker.bungeecord.BungeeMain;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.*;
import java.util.logging.Level;

public class BungeeConfigManager {

	private static final BungeeMain plugin = BungeeMain.get();

	public static Configuration MainConfig;
	public static File MainConfigFile;

	public static Configuration MainDisabled;
	public static File MainDisabledFile;

	public static Configuration Messages;
	public static File MessagesFile;

	public static File getFileFromConfig(Configuration configuration) {
		if (configuration.equals(MainConfig)) {
			return MainConfigFile;
		}
		if (configuration.equals(MainDisabled)) {
			return MainDisabledFile;
		}
		if (configuration.equals(Messages)) {
			return MessagesFile;
		}
		return null;
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

	public static Configuration getMessages() {
		if (Messages == null) {
			try {
				reloadMessages();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return Messages;
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

	public static void saveMessages() {
		if (Messages == null) {
			try {
				throw new Exception("Cannot save a non-existent file!");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		try {
			ConfigurationProvider.getProvider(YamlConfiguration.class).save(Messages, MessagesFile);
		} catch (IOException e) {
			ProxyServer.getInstance().getLogger().log(Level.SEVERE, "Could not save " + MessagesFile + ".", e);
		}
	}

	public static void reloadConfig() throws IOException {
		if (!plugin.getDataFolder().exists()) {
			plugin.getDataFolder().mkdir();
		}

		MainConfigFile = new File(plugin.getDataFolder(), "config.yml");
		if (!MainConfigFile.exists()) {
			try {
				MainConfigFile.createNewFile();
				try (InputStream is = plugin.getResourceAsStream("config.yml"); OutputStream os =
						new FileOutputStream(MainConfigFile)) {
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
			ConfigurationProvider.getProvider(YamlConfiguration.class).save(MainConfig,
					new File(plugin.getDataFolder(), "config.yml"));
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
				try (InputStream is = plugin.getResourceAsStream("disabled.yml"); OutputStream os =
						new FileOutputStream(MainDisabledFile)) {
					ByteStreams.copy(is, os);
				}
			} catch (IOException e) {
				ProxyServer.getInstance().getLogger().log(Level.SEVERE, "Could not save " + MainDisabledFile + ".", e);
				throw e;
			}
		}
		MainDisabled = ConfigurationProvider.getProvider(YamlConfiguration.class).load(MainDisabledFile);
		InputStream disabledData = plugin.getResourceAsStream("disabled.yml");
		if (disabledData != null) {
			ConfigurationProvider.getProvider(YamlConfiguration.class).save(MainDisabled,
					new File(plugin.getDataFolder(), "disabled.yml"));
		}
	}

	public static void reloadMessages() throws IOException {
		if (!plugin.getDataFolder().exists()) {
			plugin.getDataFolder().mkdir();
		}

		MessagesFile = new File(plugin.getDataFolder(), "messages.yml");
		if (!MessagesFile.exists()) {
			try {
				MessagesFile.createNewFile();
				try (InputStream is = plugin.getResourceAsStream("messages.yml"); OutputStream os =
						new FileOutputStream(MessagesFile)) {
					ByteStreams.copy(is, os);
				}
			} catch (IOException e) {
				ProxyServer.getInstance().getLogger().log(Level.SEVERE, "Could not save " + MessagesFile + ".", e);
				throw e;
			}
		}
		Messages = ConfigurationProvider.getProvider(YamlConfiguration.class).load(MessagesFile);
		InputStream messagesData = plugin.getResourceAsStream("messages.yml");
		if (messagesData != null) {
			ConfigurationProvider.getProvider(YamlConfiguration.class).save(Messages, new File(plugin.getDataFolder(),
					"messages.yml"));
		}
	}
}