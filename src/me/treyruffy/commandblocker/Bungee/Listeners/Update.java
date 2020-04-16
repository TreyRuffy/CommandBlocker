package me.treyruffy.commandblocker.bungee.listeners;

import me.treyruffy.commandblocker.Universal;
import me.treyruffy.commandblocker.bungee.BungeeMain;
import me.treyruffy.commandblocker.bungee.config.BungeeConfigManager;
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
		String latestUpdate = UpdateChecker.request("5280", "TreysCommandBlocker v" + BungeeMain.get().getProxy().getPluginManager().getPlugin("TreysCommandBlocker").getDescription().getVersion() + " BungeeCord");
		if (latestUpdate.equals("")) {
			return;
		}
		int latestUpdateVersion = Integer.parseInt(latestUpdate.replace(".", ""));
		int versionOn = Integer.parseInt(BungeeMain.get().getProxy().getPluginManager().getPlugin("TreysCommandBlocker").getDescription().getVersion().replace(".", ""));
		if (latestUpdateVersion <= versionOn) {
			return;
		}
		ProxyServer.getInstance().getScheduler().runAsync((Plugin) Universal.get().getMethods().getPlugin(), () -> {
			p.sendMessage(new TextComponent(ChatColor.AQUA + "+=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=+"));
			p.sendMessage(new TextComponent(ChatColor.GREEN + "There is a new update for"));
			p.sendMessage(new TextComponent(ChatColor.GREEN + "Command Blocker"));
			p.sendMessage(new TextComponent(ChatColor.RED + "Download at:"));
			p.sendMessage(new TextComponent(ChatColor.LIGHT_PURPLE + "https://www.spigotmc.org/resources/5280/"));
			p.sendMessage(new TextComponent(ChatColor.AQUA + "+=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=+"));
		});
	}
}
