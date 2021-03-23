package me.treyruffy.commandblocker.bukkit.config;

import me.treyruffy.commandblocker.MethodInterface;
import org.bukkit.configuration.file.FileConfiguration;

public class Messages {

	public static String getMessage(MethodInterface mi, String category, String message) {
		FileConfiguration messageConfig = (FileConfiguration) mi.getMessagesConfig();
		return messageConfig.getString(category + "." + message);
	}

}
