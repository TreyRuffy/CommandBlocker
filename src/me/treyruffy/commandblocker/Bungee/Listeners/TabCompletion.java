package me.treyruffy.commandblocker.Bungee.listeners;

import me.treyruffy.commandblocker.Bungee.BungeeConfigManager;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.TabCompleteEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class TabCompletion implements Listener {

	@EventHandler (priority = EventPriority.HIGHEST)
	public void onTabComplete(TabCompleteEvent e) {
		if (!(e.getSender() instanceof ProxiedPlayer)) {
			return;
		}
		
		ProxiedPlayer p = (ProxiedPlayer) e.getSender();
		
		if (BungeeConfigManager.getConfig().getBoolean("DisableTabComplete")) {
			if (p.hasPermission("cb.bypasstab")){
				return;
			}
			String message = e.getCursor().toLowerCase();
			for (String cmd : BungeeConfigManager.getDisabled().getSection("DisabledCommands").getKeys()){
				String cmds = cmd.replace("%colon%", ":").toLowerCase();
				String permission = cmd.replace("%colon%", "").replace(" ", "");
				if (BungeeConfigManager.getDisabled().getString("DisabledCommands." + cmd + ".Permission") == null){
					if (!(p.hasPermission(BungeeConfigManager.getConfig().getString("Default.Permission").replace("%command%", permission)))){
						if (BungeeConfigManager.getDisabled().getString("DisabledCommands." + cmd + ".NoTabComplete") == null){
							if (((message.startsWith("/" + cmds)) && (!message.contains("  "))) || ((message.startsWith("/") && (!message.contains(" "))))){
								e.setCancelled(true);
							}
						} else if (BungeeConfigManager.getDisabled().getBoolean("DisabledCommands." + cmd + ".NoTabComplete")){
							if (((message.startsWith("/" + cmds)) && (!message.contains("  "))) || ((message.startsWith("/") && (!message.contains(" "))))){
								e.setCancelled(true);
							}
						}
					}
				} else {
					if (!(p.hasPermission(BungeeConfigManager.getDisabled().getString("DisabledCommands." + cmd + ".Permission")))){
						if (BungeeConfigManager.getDisabled().getString("DisabledCommands." + cmd + ".NoTabComplete") == null){
							if (((message.startsWith("/" + cmds)) && (!message.contains("  "))) || ((message.startsWith("/") && (!message.contains(" "))))){
								e.setCancelled(true);
							}
						} else if (BungeeConfigManager.getDisabled().getBoolean("DisabledCommands." + cmd + ".NoTabComplete")){
							if (((message.startsWith("/" + cmds)) && (!message.contains("  "))) || ((message.startsWith("/") && (!message.contains(" "))))){
								e.setCancelled(true);
							}
						}
					}
				}
			}
		}
	}
	
}
