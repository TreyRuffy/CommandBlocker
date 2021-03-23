package me.treyruffy.commandblocker.bungeecord.config;

import me.treyruffy.commandblocker.MethodInterface;
import net.md_5.bungee.config.Configuration;

import java.util.ArrayList;
import java.util.List;

public class Messages {

	public static List<String> getMessages(MethodInterface mi, String category, String message) {
		Configuration messageConfig = (Configuration) mi.getMessagesConfig();
		if (messageConfig.getStringList(category + "." + message).isEmpty()) {
			List<String> list = new ArrayList<>();
			list.add(getMessage(mi, category, message));
			return list;
		}
		return messageConfig.getStringList(category + "." + message);
	}

	public static String getMessage(MethodInterface mi, String category, String message) {
		Configuration messageConfig = (Configuration) mi.getMessagesConfig();
		return messageConfig.getString(category + "." + message);
	}

}
