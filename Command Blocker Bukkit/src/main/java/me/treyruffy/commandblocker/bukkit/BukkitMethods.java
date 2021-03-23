package me.treyruffy.commandblocker.bukkit;

import me.treyruffy.commandblocker.MethodInterface;
import me.treyruffy.commandblocker.bukkit.api.BlockedCommands;
import me.treyruffy.commandblocker.bukkit.api.BlockedOpCommands;
import me.treyruffy.commandblocker.bukkit.config.ConfigManager;
import me.treyruffy.commandblockerlegacy.OldConfigManager;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class BukkitMethods implements MethodInterface {

	@Override
	public String getVersion() {
		return getPlugin().getDescription().getVersion();
	}

	@Override
	public File getDataFolder() {
		return getPlugin().getDataFolder();
	}

	@Override
	public void executeCommand(String cmd) {
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
	}

	@Override
	public void setupMetrics() {
		int pluginId = 1851;
		Metrics metrics = new Metrics(getPlugin(), pluginId);

		metrics.addCustomChart(new Metrics.SimplePie("blockedCommandsCount", () -> BlockedCommands.getBlockedCommands().size() + ""));
		
		metrics.addCustomChart(new Metrics.SimplePie("blockedOpCommandsCount", () -> BlockedOpCommands.getBlockedCommands().size() + ""));
	}

	@Override
	public void sendMessage(Object commandSender, Component message) {
		if (LegacyComponentSerializer.legacy(getChatComponentChar()).serialize(message).equalsIgnoreCase(""))
			return;
		if (!(commandSender instanceof CommandSender)) {
			log(ChatColor.RED + "Command Blocker: Tried sending message to non console sender.");
			return;
		}
		CommandSender sender = (CommandSender) commandSender;
		if (BukkitMain.sendOldMessages()) {
			String oldMessage = LegacyComponentSerializer.legacy(getChatComponentChar()).serialize(message);
			sender.sendMessage(oldMessage);
		} else {
			BukkitAudiences adventure = BukkitMain.adventure();
			if (sender instanceof Player) {
				Player player = (Player) sender;
				adventure.player(player).sendMessage(message);
			} else {
				adventure.sender(sender).sendMessage(message);
			}
		}
	}

	@Override
	public List<String> getOldMessages(String category, String message) {
		return getOldMessages(category, message, getMessagesConfig());
	}

	@Override
	public List<String> getOldMessages(String category, String message, Object configurationFile) {
		List<String> list = new ArrayList<>();
		if (!(configurationFile instanceof FileConfiguration)) {
			list.add(ChatColor.RED + "Configuration file is not correct.");
			return list;
		}
		FileConfiguration configuration = (FileConfiguration) configurationFile;
		if (configuration.getStringList(category + "." + message).isEmpty()) {
			list.add(getOldMessage(category, message, configuration));
			return list;
		}
		for (String stringListEntries : configuration.getStringList(category + "." + message)) {
			list.add(ChatColor.translateAlternateColorCodes('&', stringListEntries));
		}
		return list;
	}

	@Override
	public Character getChatComponentChar() {
		return ChatColor.COLOR_CHAR;
	}

	@Override
	public String getOldMessage(String category, String message) {
		return getOldMessage(category, message, getMessagesConfig());
	}

	@Override
	public String getOldMessage(String category, String message, Object configurationFile) {
		if (configurationFile == null)
			return ChatColor.RED + "Configuration file is not set.";
		if (!(configurationFile instanceof FileConfiguration)) {
			return ChatColor.RED + "Configuration file is not correct.";
		}
		FileConfiguration configuration = (FileConfiguration) configurationFile;
		File file = getConfigFile(configurationFile);
		String messageFromMessagesFile = configuration.getString(category + "." + message);
		if (messageFromMessagesFile == null)
			return ChatColor.RED + "Message (" + category + "." + message + ") is not set in " + file.getName() + ".";
		return ChatColor.translateAlternateColorCodes('&', messageFromMessagesFile);
	}

	@Override
	public JavaPlugin getPlugin() {
		return BukkitMain.get();
	}

	@Override
	public FileConfiguration getConfig() {
		if (BukkitMain.oldConfig())
			return OldConfigManager.getConfig();
		return ConfigManager.getConfig();
	}

	@Override
	public FileConfiguration getDisabledCommandsConfig() {
		if (BukkitMain.oldConfig())
			return OldConfigManager.getDisabled();
		return ConfigManager.getDisabled();
	}

	@Override
	public FileConfiguration getMessagesConfig() {
		if (BukkitMain.oldConfig())
			return OldConfigManager.getMessages();
		return ConfigManager.getMessages();
	}

	@Override
	public FileConfiguration getOpBlockConfig() {
		if (BukkitMain.oldConfig())
			return OldConfigManager.getOpDisabled();
		return ConfigManager.getOpDisabled();
	}

	@Override
	public void saveConfig() {
		if (BukkitMain.oldConfig())
			OldConfigManager.saveConfig();
		else
			ConfigManager.saveConfig();
	}

	@Override
	public void saveDisabledCommandsConfig() {
		if (BukkitMain.oldConfig())
			OldConfigManager.saveDisabled();
		else
			ConfigManager.saveDisabled();
	}

	@Override
	public void saveMessagesConfig() {
		if (BukkitMain.oldConfig())
			OldConfigManager.saveMessages();
		else
			ConfigManager.saveMessages();
	}

	@Override
	public void saveOpBlockConfig() {
		if (BukkitMain.oldConfig())
			OldConfigManager.saveOpDisabled();
		else
			ConfigManager.saveOpDisabled();
	}

	@Override
	public File getConfigFile(Object configurationFile) {
		if (configurationFile instanceof FileConfiguration) {
			if (BukkitMain.oldConfig()) {
				try {
					return OldConfigManager.getFileFromConfig((FileConfiguration) configurationFile);
				} catch (NoSuchMethodError e) {
					Bukkit.getLogger().log(Level.SEVERE,
							"Trey's Command Blocker needs the newest version of Trey's Command Blocker Legacy to run " +
									"on your server version" +
									". Please download at https://www.spigotmc.org/resources/command-blocker-legacy.82948/");
				}
			} else {
				return ConfigManager.getFileFromConfig((FileConfiguration) configurationFile);
			}
		}
		return null;
	}

	@Override
	public void log(String msg) {
		Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
	}

	@Override
	public String getServerType() {
		try {
			Class.forName("net.pl3x.purpur.PurpurConfig");
			return "Purpur";
		} catch (ClassNotFoundException ignored) {}
		try {
			Class.forName("com.tuinity.tuinity.config.TuinityConfig");
			return "Tuinity";
		} catch (ClassNotFoundException ignored) {}
		try {
			Class.forName("com.destroystokyo.paper.PaperConfig");
			return "Paper";
		} catch (ClassNotFoundException ignored) {}
		try {
			Class.forName("org.spigotmc.SpigotConfig");
			return "Spigot";
		} catch (ClassNotFoundException ignored) {}
		return "Bukkit";
	}

}
