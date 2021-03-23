package me.treyruffy.commandblocker.bungeecord;

import me.treyruffy.commandblocker.MethodInterface;
import me.treyruffy.commandblocker.bungeecord.api.BlockedCommands;
import me.treyruffy.commandblocker.bungeecord.config.BungeeConfigManager;
import net.kyori.adventure.platform.bungeecord.BungeeAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import org.bstats.bungeecord.Metrics;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class BungeeMethods implements MethodInterface {

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
		ProxyServer.getInstance().getPluginManager().dispatchCommand(ProxyServer.getInstance().getConsole(), cmd);
	}

	@Override
	public void setupMetrics() {
		int pluginId = 2955;
		Metrics metrics = new Metrics(BungeeMain.get(), pluginId);
		metrics.addCustomChart(new Metrics.SimplePie("blockedCommandsCount",
				() -> BlockedCommands.getBlockedCommands().size() + ""));
	}

	@Override
	public void sendMessage(Object commandSender, Component message) {
		if (!(commandSender instanceof CommandSender)) {
			log(ChatColor.RED + "Command Blocker: Tried sending message to non console sender.");
			return;
		}
		CommandSender sender = (CommandSender) commandSender;
		BungeeAudiences adventure = BungeeMain.adventure();
		if (sender instanceof ProxiedPlayer) {
			ProxiedPlayer player = (ProxiedPlayer) sender;
			adventure.player(player).sendMessage(message);
		} else {
			adventure.sender(sender).sendMessage(message);
		}
	}

	@Override
	public List<String> getOldMessages(String category, String message) {
		return getOldMessages(category, message, getMessagesConfig());
	}

	@Override
	public List<String> getOldMessages(String category, String message, Object configurationFile) {
		List<String> list = new ArrayList<>();
		if (!(configurationFile instanceof Configuration)) {
			list.add(ChatColor.RED + "Configuration file is not correct.");
			return list;
		}
		Configuration configuration = (Configuration) configurationFile;
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
		if (!(configurationFile instanceof Configuration)) {
			return ChatColor.RED + "Configuration file is not correct.";
		}
		Configuration configuration = (Configuration) configurationFile;
		File file = getConfigFile(configurationFile);
		String messageFromMessagesFile = configuration.getString(category + "." + message);
		if (messageFromMessagesFile == null)
			return ChatColor.RED + "Message (" + category + "." + message + ") is not set in " + file.getName() + ".";
		return ChatColor.translateAlternateColorCodes('&', messageFromMessagesFile);
	}

	@Override
	public Plugin getPlugin() {
		return BungeeMain.get();
	}

	@Override
	public Configuration getConfig() {
		return BungeeConfigManager.getConfig();
	}

	@Override
	public Configuration getDisabledCommandsConfig() {
		return BungeeConfigManager.getDisabled();
	}

	@Override
	public Configuration getMessagesConfig() {
		return BungeeConfigManager.getMessages();
	}

	@Override
	public Object getOpBlockConfig() {
		return null;
	}

	@Override
	public void saveConfig() {
		BungeeConfigManager.saveConfig();
	}

	@Override
	public void saveDisabledCommandsConfig() {
		BungeeConfigManager.saveDisabled();
	}

	@Override
	public void saveMessagesConfig() {
		BungeeConfigManager.saveMessages();
	}

	@Override
	public void saveOpBlockConfig() {}

	@Override
	public File getConfigFile(Object configurationFile) {
		if (configurationFile instanceof Configuration) {
			return BungeeConfigManager.getFileFromConfig((Configuration) configurationFile);
		}
		return null;
	}

	@Override
	public void log(String msg) {
		BungeeAudiences adventure = BungeeMain.adventure();
		Component component = MiniMessage.get().parse(ChatColor.translateAlternateColorCodes('&', msg));
		CommandSender commandSender = ProxyServer.getInstance().getConsole();
		adventure.sender(commandSender).sendMessage(component);
	}

	@Override
	public String getServerType() {
		try {
			Class.forName("io.github.waterfallmc.travertine.protocol.MultiVersionPacketV17");
			return "Travertine";
		} catch (ClassNotFoundException ignored) {}
		try {
			Class.forName("io.github.waterfallmc.waterfall.conf.WaterfallConfiguration");
			return "Waterfall";
		} catch (ClassNotFoundException ignored) {}
		return "BungeeCord";
	}

}
