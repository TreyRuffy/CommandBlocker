package me.treyruffy.commandblocker.bukkit.listeners;

import me.treyruffy.commandblocker.MethodInterface;
import me.treyruffy.commandblocker.Universal;
import me.treyruffy.commandblocker.bukkit.BukkitMain;
import me.treyruffy.commandblocker.bukkit.Variables;
import me.treyruffy.commandblocker.updater.UpdateChecker;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.HashMap;

public class Update implements Listener {

	static String latestUpdateVersion;
	static boolean notifyAboutUpdate = false;

	public static void updateCheck() {
		MethodInterface mi = Universal.get().getMethods();
		FileConfiguration config = (FileConfiguration) mi.getConfig();
		if (!config.getBoolean("Updates.Check")) {
			return;
		}
		latestUpdateVersion = UpdateChecker.request("5280",
				"Trey's Command Blocker v" + BukkitMain.get().getDescription().getVersion() + " Bukkit");
		if (latestUpdateVersion.equals("")) {
			return;
		}
		int latestUpdate = Integer.parseInt(latestUpdateVersion.replace(".", ""));
		int versionOn = Integer.parseInt(BukkitMain.get().getDescription().getVersion().replace(".", ""));
		if (latestUpdate <= versionOn) {
			return;
		}

		for (Component msgToSend : mi.getOldMessages("Updates", "UpdateFound")) {
			HashMap<String, String> placeholders = new HashMap<>();
			placeholders.put("%s", latestUpdateVersion);
			mi.sendMessage(Bukkit.getConsoleSender(), Variables.translateVariables(msgToSend, Bukkit.getConsoleSender(), placeholders));
		}

		notifyAboutUpdate = true;
		if (config.getBoolean("Updates.TellPlayers")) {
			for (Player p : Bukkit.getOnlinePlayers()) {
				if (p.hasPermission("cb.updates")) {
					for (Component msgToSend : mi.getOldMessages("Updates", "UpdateFound")) {
						HashMap<String, String> placeholders = new HashMap<>();
						placeholders.put("%s", latestUpdateVersion);
						mi.sendMessage(p, Variables.translateVariables(msgToSend, p, placeholders));
					}
				}
			}
		}
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		if (!notifyAboutUpdate)
			return;
		MethodInterface mi = Universal.get().getMethods();
		FileConfiguration config = (FileConfiguration) mi.getConfig();
		if (!config.getBoolean("Updates.Check")) {
			return;
		}
		if (!config.getBoolean("Updates.TellPlayers")) {
			return;
		}
		Player p = e.getPlayer();
		if (!p.hasPermission("cb.updates")) {
			return;
		}
		for (Component msgToSend : mi.getOldMessages("Updates", "UpdateFound")) {
			HashMap<String, String> placeholders = new HashMap<>();
			placeholders.put("%s", latestUpdateVersion);
			mi.sendMessage(p, Variables.translateVariables(msgToSend, p, placeholders));
		}
	}
}
