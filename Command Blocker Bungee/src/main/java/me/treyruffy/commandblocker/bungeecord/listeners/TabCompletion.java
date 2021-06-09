package me.treyruffy.commandblocker.bungeecord.listeners;

import me.treyruffy.commandblocker.MethodInterface;
import me.treyruffy.commandblocker.Universal;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.TabCompleteEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class TabCompletion implements Listener {

	@EventHandler (priority = EventPriority.HIGHEST)
	public void onTabComplete(TabCompleteEvent e) {
		if (!(e.getSender() instanceof ProxiedPlayer)) {
			return;
		}
		MethodInterface mi = Universal.get().getMethods();
		Configuration config = (Configuration) mi.getConfig();
		if (!config.getBoolean("DisableTabComplete")) {
			return;
		}
		ProxiedPlayer p = (ProxiedPlayer) e.getSender();
		if (p.hasPermission("cb.bypasstab")) {
			return;
		}
		Configuration disabled = (Configuration) mi.getDisabledCommandsConfig();
		String message = e.getCursor().toLowerCase();

		for (String cmd : disabled.getSection("DisabledCommands").getKeys()) {
			String cmds = cmd.replace("%colon%", ":").toLowerCase();
			String permission = cmd.replace("%colon%", "").replace(":", "").replace(" ", "");

			boolean b =	((message.startsWith("/" + cmds)) && (!message.contains("  "))) || ((message.startsWith("/") && (!message.contains(" "))));
			if (disabled.getString("DisabledCommands." + cmd + ".Permission") == null) {
				if (!(p.hasPermission(config.getString("Default.Permission").replace("%command%", permission)))) {
					if (disabled.getString("DisabledCommands." + cmd + ".NoTabComplete") == null) {
						if (!(disabled.getStringList("DisabledCommands." + cmd + ".Servers").contains("all"))) {
							if (!disabled.getStringList("DisabledCommands." + cmd + ".Servers").contains(p.getServer().getInfo().getName())) {
								return;
							}
						}
						if (b) {
							e.setCancelled(true);
						}

					} else if (disabled.getBoolean("DisabledCommands." + cmd + ".NoTabComplete")) {
						if (!(disabled.getStringList("DisabledCommands." + cmd + ".Servers").contains("all"))) {
							if (!disabled.getStringList("DisabledCommands." + cmd + ".Servers").contains(p.getServer().getInfo().getName())) {
								return;
							}
						}
						if (b) {
							e.setCancelled(true);
						}
					}
				}
			} else {
				if (!(p.hasPermission(disabled.getString("DisabledCommands." + cmd + ".Permission")))) {
					if (disabled.getString("DisabledCommands." + cmd + ".NoTabComplete") == null) {
						if (!(disabled.getStringList("DisabledCommands." + cmd + ".Servers").contains("all"))) {
							if (!disabled.getStringList("DisabledCommands." + cmd + ".Servers").contains(p.getServer().getInfo().getName())) {
								return;
							}
						}
						if (b) {
							e.setCancelled(true);
						}
						disabled.set("DisabledCommands." + cmd + ".NoTabComplete", true);
						mi.saveDisabledCommandsConfig();
					} else if (disabled.getBoolean("DisabledCommands." + cmd + ".NoTabComplete")) {
						if (!(disabled.getStringList("DisabledCommands." + cmd + ".Servers").contains("all"))) {
							if (!disabled.getStringList("DisabledCommands." + cmd + ".Servers").contains(p.getServer().getInfo().getName())) {
								return;
							}
						}
						if (b) {
							e.setCancelled(true);
						}
					}
				}
			}
		}
	}
	
}
