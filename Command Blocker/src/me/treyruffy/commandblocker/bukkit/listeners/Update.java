package me.treyruffy.commandblocker.bukkit.listeners;

import me.clip.placeholderapi.PlaceholderAPI;
import me.treyruffy.commandblocker.bukkit.PlaceholderAPITest;
import me.treyruffy.commandblocker.bukkit.config.Messages;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import me.treyruffy.commandblocker.bukkit.BukkitMain;
import me.treyruffy.commandblocker.bukkit.config.ConfigManager;
import me.treyruffy.commandblocker.updater.UpdateChecker;
import me.treyruffy.commandblockerlegacy.OldConfigManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Update implements Listener {
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		FileConfiguration config = BukkitMain.oldConfig() ? OldConfigManager.getConfig() : ConfigManager.getConfig();
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

		String latestUpdate = UpdateChecker.request("5280",
				"Trey's Command Blocker v" + BukkitMain.get().getDescription().getVersion() + " Bukkit");
		if (latestUpdate.equals("")) {
			return;
		}
		int latestUpdateVersion = Integer.parseInt(latestUpdate.replace(".", ""));
		int versionOn = Integer.parseInt(BukkitMain.get().getDescription().getVersion().replace(".", ""));
		if (latestUpdateVersion <= versionOn) {
			return;
		}
		
		Bukkit.getScheduler().runTaskLaterAsynchronously(BukkitMain.get(), () -> {
			for (String msg2 : Messages.getMessages("Updates", "UpdateFound")) {
				p.sendMessage(PlaceholderAPITest.testforPAPI(p,
						ChatColor.translateAlternateColorCodes('&', msg2).replace("%s", latestUpdate)));
			}
		}, 4L);
		
	}
}
