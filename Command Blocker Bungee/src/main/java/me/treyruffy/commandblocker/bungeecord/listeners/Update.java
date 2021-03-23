package me.treyruffy.commandblocker.bungeecord.listeners;

import me.treyruffy.commandblocker.MethodInterface;
import me.treyruffy.commandblocker.Universal;
import me.treyruffy.commandblocker.bungeecord.BungeeMain;
import me.treyruffy.commandblocker.bungeecord.Variables;
import me.treyruffy.commandblocker.updater.UpdateChecker;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.event.EventHandler;

import java.util.HashMap;

public class Update implements Listener {

	static String latestUpdateVersion;
	static boolean notifyAboutUpdate = false;

	public static void updateCheck() {
		MethodInterface mi = Universal.get().getMethods();
		Configuration config = (Configuration) mi.getConfig();
		if (!config.getBoolean("Updates.Check")) {
			return;
		}
		latestUpdateVersion = UpdateChecker.request("5280",
				"Trey's Command Blocker v" + BungeeMain.get().getDescription().getVersion() + " BungeeCord");
		if (latestUpdateVersion.equals("")) {
			return;
		}
		int latestUpdate = Integer.parseInt(latestUpdateVersion.replace(".", ""));
		int versionOn = Integer.parseInt(BungeeMain.get().getDescription().getVersion().replace(".", ""));
		if (latestUpdate <= versionOn) {
			return;
		}

		for (String msgToSend : mi.getOldMessages("Updates", "UpdateFound")) {
			HashMap<String, String> placeholders = new HashMap<>();
			placeholders.put("%s", latestUpdateVersion);
			mi.sendMessage(BungeeMain.get().getProxy().getConsole(), Variables.translateVariables(msgToSend,
					BungeeMain.get().getProxy().getConsole(), placeholders));
		}

		notifyAboutUpdate = true;
		if (config.getBoolean("Updates.TellPlayers")) {
			for (ProxiedPlayer p : BungeeMain.get().getProxy().getPlayers()) {
				if (p.hasPermission("cb.updates")) {
					for (String msgToSend : mi.getOldMessages("Updates", "UpdateFound")) {
						HashMap<String, String> placeholders = new HashMap<>();
						placeholders.put("%s", latestUpdateVersion);
						mi.sendMessage(p, Variables.translateVariables(msgToSend, p, placeholders));
					}
				}
			}
		}
	}

	@EventHandler
	public void onJoin(PostLoginEvent e) {
		if (!notifyAboutUpdate)
			return;
		MethodInterface mi = Universal.get().getMethods();
		Configuration config = (Configuration) mi.getConfig();
		if (!config.getBoolean("Updates.Check")) {
			return;
		}
		if (!config.getBoolean("Updates.TellPlayers")) {
			return;
		}
		ProxiedPlayer p = e.getPlayer();
		if (!p.hasPermission("cb.updates")) {
			return;
		}
		for (String msgToSend : mi.getOldMessages("Updates", "UpdateFound")) {
			HashMap<String, String> placeholders = new HashMap<>();
			placeholders.put("%s", latestUpdateVersion);
			mi.sendMessage(p, Variables.translateVariables(msgToSend, p, placeholders));
		}
	}
}
