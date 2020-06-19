package me.treyruffy.commandblocker.bukkit.config;

import me.treyruffy.commandblocker.bukkit.BukkitMain;
import me.treyruffy.commandblockerlegacy.OldConfigManager;
import org.bukkit.configuration.Configuration;

import java.util.ArrayList;
import java.util.List;

public class Messages {

	public static List<String> getMessages(String category, String message) {
		Configuration messageConfig = BukkitMain.oldConfig() ? OldConfigManager.getMessages() :
				ConfigManager.getMessages();
		if (messageConfig.getStringList(category + "." + message).isEmpty()) {
			List<String> list = new ArrayList<>();
			list.add(getMessage(category, message));
			return list;
		}
		return messageConfig.getStringList(category + "." + message);
	}

	public static String getMessage(String category, String message) {
		Configuration messageConfig = BukkitMain.oldConfig() ? OldConfigManager.getMessages() :
				ConfigManager.getMessages();
		return messageConfig.getString(category + "." + message);
	}

}
