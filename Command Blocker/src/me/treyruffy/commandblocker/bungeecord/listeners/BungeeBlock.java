package me.treyruffy.commandblocker.bungeecord.listeners;

import me.treyruffy.commandblocker.bungeecord.Variables;
import me.treyruffy.commandblocker.bungeecord.config.BungeeConfigManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class BungeeBlock implements Listener {
	
	@EventHandler (priority = EventPriority.HIGHEST)
	public void onCommand(ChatEvent e) {
		if (e.getSender() instanceof ProxiedPlayer) {
			ProxiedPlayer p = (ProxiedPlayer) e.getSender();
			String[] args = e.getMessage().split(" ");
			
			Configuration disabled = BungeeConfigManager.getDisabled();
			Configuration config = BungeeConfigManager.getConfig();
			
			if (disabled.getSection("DisabledCommands").getKeys() == null) {
				return;
			}
			
			for (String command : disabled.getSection("DisabledCommands").getKeys()) {
				String commands = command.replace("%colon%", ":");
				String[] cmdList = commands.split(" ");
				String permission = command.replace("%colon%", "").replace(":", "").replace(" ", "");
				
				
				if (args[0].equalsIgnoreCase("/" + cmdList[0])) {
					if (args.length != 1) {
						int i = 1, j = 0;
						for (String s : cmdList) {
							
							if (j != 0) {
								if (!args[i].equalsIgnoreCase(s)) {
									return;
								}
								i++;
							}
							j = 1;
						}
					}
				}

				if (disabled.getStringList("DisabledCommands." + command + ".WhitelistedPlayers").contains(p.getUniqueId().toString())) {
					return;
				}

				if (!disabled.getStringList("DisabledCommands." + command + ".Servers").contains("all") || !(disabled.getSection("DisabledCommands").getSection(command).getString("Servers") == null)) {
					if (!(disabled.getStringList("DisabledCommands." + command + ".Servers").contains(p.getServer().getInfo().getName()))) {
						return;
					}
				}
				
				if ((disabled.getSection("DisabledCommands").getSection(command).getString("Permission") == null) || (disabled.getString("DisabledCommands." + command + ".Permission").equalsIgnoreCase("default"))) {
					if (p.hasPermission(config.getString("Default.Permission").replace("%command%", permission))) {
						return;
					}
				} else {
					if (p.hasPermission(disabled.getString("DisabledCommands." + command + ".Permission"))) {
						return;
					}
				}
				
				e.setCancelled(true);
				if (!(disabled.getString("DisabledCommands." + command + ".Message").replace(" ", "").equalsIgnoreCase("none"))) {
					String message;
					if ((disabled.getString("DisabledCommands." + command + ".Message") == null) || (disabled.getString("DisabledCommands." + command + ".Message").equalsIgnoreCase("default"))) {
						String msg = ChatColor.translateAlternateColorCodes('&', config.getString("Default.Message"));
						
						message = Variables.translateVariables(msg, p);
					} else {
						String msg = ChatColor.translateAlternateColorCodes('&', disabled.getString("DisabledCommands." + command + ".Message"));
						
						message = Variables.translateVariables(msg, p);
					}

					// MannyLama's patch #9
					if (message.length() != 0) {
						p.sendMessage(new TextComponent(message));
					}
				}
				
				if ((disabled.getStringList("DisabledCommands." + command + ".PlayerCommands").size() > 0) && (!disabled.getStringList("DisabledCommands." + command + ".PlayerCommands").contains("none"))) {
					for (String s : disabled.getStringList("DisabledCommands." + command + ".PlayerCommands")) {
						p.chat("/" + Variables.translateVariables(s, p).replace("%command%", commands));
					}
				}
			}
		}
	}
}