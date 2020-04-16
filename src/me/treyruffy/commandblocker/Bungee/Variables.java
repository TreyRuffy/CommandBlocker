package me.treyruffy.commandblocker.bungee;

import net.md_5.bungee.api.connection.ProxiedPlayer;

public class Variables {

	public static String translateVariables(String msg, ProxiedPlayer p) {

		return msg.replace("%player%", p.getName())
				.replace("%uuid%", p.getUniqueId().toString())
				.replace("%username%", p.getDisplayName())
				.replace("%displayname%", p.getDisplayName())
				.replace("%server%", p.getServer().toString())
				.replace("%bungee_name%", BungeeMain.get().getProxy().getName());
	}
	
}
