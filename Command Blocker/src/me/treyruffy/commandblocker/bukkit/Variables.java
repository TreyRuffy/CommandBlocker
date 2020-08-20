package me.treyruffy.commandblocker.bukkit;

import org.bukkit.entity.Player;

public class Variables {

	public static String translateVariables(String msg, Player p) {

		return msg.replace("%player%", p.getName())
				.replace("%uuid%", p.getUniqueId().toString())
				.replace("%username%", p.getDisplayName())
				.replace("%isop%", p.isOp() + "")
				.replace("%server_name%", p.getServer().getName())
				.replace("%server_ip%", p.getServer().getIp())
				.replace("%server_port%", p.getServer().getPort() + "")
				.replace("%world%", p.getWorld().getName());
	}
	
}
