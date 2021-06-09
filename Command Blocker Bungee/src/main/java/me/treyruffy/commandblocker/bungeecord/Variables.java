package me.treyruffy.commandblocker.bungeecord;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.HashMap;

public class Variables {

	@SuppressWarnings("deprecation")
	public static String translateVariablesToString(String msg, CommandSender commandSender,
											HashMap<String, String> additionalPlaceholders,
											boolean excludePlayerPlaceholder) {

		if (commandSender instanceof ProxiedPlayer) {
			ProxiedPlayer p = (ProxiedPlayer) commandSender;
			if (!excludePlayerPlaceholder) {
				msg = msg.replace("%player%", p.getName())
						.replace("%username%", p.getDisplayName())
						.replace("%displayname%", p.getDisplayName());
			}
			msg = msg.replace("%uuid%", p.getUniqueId().toString())
					.replace("%ping%", String.valueOf(p.getPing()))
					.replace("%language%", p.getLocale().getLanguage())
					.replace("%forge_enabled%", String.valueOf(p.isForgeUser()))
					.replace("%server%", p.getServer().getInfo().getName())
					.replace("%server_name%", p.getServer().getInfo().getName())
					.replace("%server_ip%", p.getServer().getInfo().getAddress().getHostName())
					.replace("%server_port%", String.valueOf(p.getServer().getInfo().getAddress().getPort()));
		}

		msg = msg.replace("%bungee_name%", BungeeMain.get().getProxy().getName())
				.replace("%bungee_version%", BungeeMain.get().getProxy().getVersion());


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

	public static Component translateVariables(String msg, CommandSender commandSender,
											   HashMap<String, String> additionalPlaceholders) {
		return translateVariables(msg, commandSender, additionalPlaceholders, false);
	}

	public static Component translateVariables(String msg, CommandSender commandSender) {
		return translateVariables(msg, commandSender, null, false);
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

	public static String translateVariablesToLegacyString(String msg, CommandSender commandSender) {
		Component translatedComponent = translateVariables(msg, commandSender);
		return LegacyComponentSerializer.legacy(ChatColor.COLOR_CHAR).serialize(translatedComponent);
	}
}
