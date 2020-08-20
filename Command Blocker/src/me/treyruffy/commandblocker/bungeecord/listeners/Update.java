package me.treyruffy.commandblocker.bungeecord.listeners;

import me.treyruffy.commandblocker.Universal;
import me.treyruffy.commandblocker.bungeecord.BungeeMain;
import me.treyruffy.commandblocker.bungeecord.config.BungeeConfigManager;
import me.treyruffy.commandblocker.bungeecord.config.Messages;
import me.treyruffy.commandblocker.updater.UpdateChecker;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

public class Update implements Listener {
	
	@EventHandler
	public void onJoin(PostLoginEvent e) {
		if (!BungeeConfigManager.getConfig().getBoolean("Updates.Check")) {
			return;
		}
		if (!BungeeConfigManager.getConfig().getBoolean("Updates.TellPlayers")) {
			return;
		}
		ProxiedPlayer p = e.getPlayer();
		if (!p.hasPermission("cb.updates")) {
			return;
		}
		String latestUpdate = UpdateChecker.request("5280",
				"Trey's Command Blocker v" + BungeeMain.get().getProxy().getPluginManager().getPlugin("CommandBlocker").getDescription().getVersion() + " BungeeCord");
		if (latestUpdate.equals("")) {
			return;
		}
		int latestUpdateVersion = Integer.parseInt(latestUpdate.replace(".", ""));
		int versionOn = Integer.parseInt(BungeeMain.get().getDescription().getVersion().replace(".", ""));
		if (latestUpdateVersion <= versionOn) {
			return;
		}
		ProxyServer.getInstance().getScheduler().runAsync(BungeeMain.get(), () -> {
			for (String message1 : Messages.getMessages("Updates", "UpdateFound")) {
				p.sendMessage(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', message1)));
			}
		});
	}
}
