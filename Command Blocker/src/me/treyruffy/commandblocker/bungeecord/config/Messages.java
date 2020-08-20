package me.treyruffy.commandblocker.bungeecord.config;

import net.md_5.bungee.config.Configuration;

import java.util.ArrayList;
import java.util.List;

public class Messages {

	public static List<String> getMessages(String category, String message) {
		// Test if the category / message exists, if not add them
		Configuration messageConfig = BungeeConfigManager.getMessages();
		if (messageConfig.getStringList(category + "." + message).isEmpty()) {
			List<String> list = new ArrayList<>();
			list.add(getMessage(category, message));
			return list;
		}
		return messageConfig.getStringList(category + "." + message);
	}

	public static String getMessage(String category, String message) {
		Configuration messageConfig = BungeeConfigManager.getMessages();
		return messageConfig.getString(category + "." + message);
	}

}
