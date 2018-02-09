package me.treyruffy.commandblocker.Bungee.listeners;

import me.treyruffy.commandblocker.MethodInterface;
import me.treyruffy.commandblocker.Universal;
import me.treyruffy.commandblocker.Bungee.BungeeConfigManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class BungeeBlock implements Listener {
	
	@EventHandler (priority = EventPriority.HIGHEST)
	public void onCommand(ChatEvent e){
		if (e.getSender() instanceof ProxiedPlayer) {
			ProxiedPlayer p = (ProxiedPlayer) e.getSender();
			String[] args = e.getMessage().split(" ");
			
			MethodInterface mi = Universal.get().getMethods();
			
			Configuration disabled = BungeeConfigManager.getDisabled();
			Configuration config = BungeeConfigManager.getConfig();
			
			if (disabled.getSection("DisabledCommands").getKeys().size() == 0) {
				return;
			}
			
			for (String command : disabled.getSection("DisabledCommands").getKeys()){
				String commands = command.replace("%colon%", ":");
				String permission = command.replace("%colon%", "");
				
				
				if (args[0].equalsIgnoreCase("/" + commands)) {
					if (disabled.getStringList("DisabledCommands." + command + ".Servers").size() > 0) {
						if (!(disabled.getStringList("DisabledCommands." + command + ".Servers").contains(p.getServer().getInfo().getName()))) {
							return;
						}
					}
					
					if (disabled.getString("DisabledCommands." + command + ".Permission") == null) {
						if (p.hasPermission(config.getString("Default.Permission").replace("%command%", permission))) {
							return;
						}
					} else {
						if (p.hasPermission(disabled.getString("DisabledCommands." + command + ".Permission"))) {
							return;
						}
					}
					
					e.setCancelled(true);
					String message;
					if (disabled.getStringList("DisabledCommands." + command + ".Message") == null) {
						String msg = ChatColor.translateAlternateColorCodes('&', config.getString("Default.Message"));
						
						message = msg;
					} else {
						String msg = ChatColor.translateAlternateColorCodes('&', disabled.getString("DisabledCommands." + command + ".Message"));
						
						message = msg;
					}
					
					p.sendMessage(new TextComponent(message));
					
					if (disabled.getStringList("DisabledCommands." + command + ".PlayerCommands").size() > 0) {
						for (String s : disabled.getStringList("DisabledCommands." + command + ".PlayerCommands")) {
							p.chat("/" + s.replace("%player%", p.getName()).replace("%username%", p.getDisplayName()).replace("%command%", commands));
						}
					}
					
				}
			}
		}
	}
}