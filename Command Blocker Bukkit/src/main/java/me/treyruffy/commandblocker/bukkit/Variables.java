package me.treyruffy.commandblocker.bukkit;

import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class Variables {

	public static String translateVariablesToString(String msg, CommandSender commandSender,
													HashMap<String, String> additionalPlaceholders,
													boolean excludePlayerPlaceholder) {
		if (commandSender instanceof Player) {
			Player p = (Player) commandSender;
			if (!excludePlayerPlaceholder) {
				msg = msg.replace("%player%", p.getName())
						.replace("%username%", p.getDisplayName())
						.replace("%displayname%", p.getDisplayName());
			}
			msg = msg.replace("%uuid%", p.getUniqueId().toString())
					.replace("%isop%", String.valueOf(p.isOp()))
					.replace("%server_name%", p.getServer().getName())
					.replace("%server_ip%", p.getServer().getIp())
					.replace("%server_port%", String.valueOf(p.getServer().getPort()))
					.replace("%world%", p.getWorld().getName());
			if (BukkitMain.isPapiEnabled()) {
				msg = PlaceholderAPI.setPlaceholders((Player) commandSender, msg);
			}
		} else {
			msg = msg.replace("%server_name%", BukkitMain.get().getServer().getName())
					.replace("%server_ip%", BukkitMain.get().getServer().getIp())
					.replace("%server_port%", String.valueOf(BukkitMain.get().getServer().getPort()));
		}

		if (additionalPlaceholders != null) {
			for (String placeholder : additionalPlaceholders.keySet()) {
				msg = msg.replace(placeholder, additionalPlaceholders.get(placeholder));
			}
		}
		return msg;
	}

	public static String translateVariablesToString(String msg, CommandSender commandSender) {
		return translateVariablesToString(msg, commandSender, null, false);
	}

	public static Component translateVariables(String msg, CommandSender commandSender,
											   HashMap<String, String> additionalPlaceholders,
											   boolean excludePlayerPlaceholder) {

		msg = translateVariablesToString(msg, commandSender, additionalPlaceholders, excludePlayerPlaceholder);
		return MiniMessage.get().parse(msg);
	}

	public static Component translateVariablesWithComponentPlaceholders(String msg, CommandSender commandSender,
																		HashMap<String, String> additionalPlaceholders,
																		HashMap<String, Component> additionalComponentPlaceholders) {
		msg = translateVariablesToString(msg, commandSender, additionalPlaceholders, false);
		if (additionalComponentPlaceholders != null) {
			for (String placeholder : additionalComponentPlaceholders.keySet()) {
				msg = msg.replace(placeholder,
						MiniMessage.get().serialize(additionalComponentPlaceholders.get(placeholder)) + "<reset>");
			}
		}
		return MiniMessage.get().parse(msg);
	}

	public static Component translateVariables(String msg, CommandSender commandSender,
											   HashMap<String, String> additionalPlaceholders) {
		return translateVariables(msg, commandSender, additionalPlaceholders, false);
	}

	public static Component translateVariables(String msg, CommandSender commandSender) {
		return translateVariables(msg, commandSender, null, false);
	}


	public static String translateVariablesToLegacyString(String msg, CommandSender commandSender) {
		Component translatedComponent = translateVariables(msg, commandSender);
		return LegacyComponentSerializer.legacy(ChatColor.COLOR_CHAR).serialize(translatedComponent);
	}

}
