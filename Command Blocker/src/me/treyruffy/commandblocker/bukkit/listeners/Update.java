package me.treyruffy.commandblocker.bukkit.listeners;

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
				"CommandBlocker v" + BukkitMain.get().getDescription().getVersion() +
						" Bukkit");
		if (latestUpdate.equals("")) {
			return;
		}
		int latestUpdateVersion = Integer.parseInt(latestUpdate.replace(".", ""));
		int versionOn = Integer.parseInt(BukkitMain.get().getDescription().getVersion().replace(".", ""));
		if (latestUpdateVersion <= versionOn) {
			return;
		}
		
		Bukkit.getScheduler().runTaskLaterAsynchronously(BukkitMain.get(), () -> {
			p.sendMessage(ChatColor.AQUA + "+=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=+");
			p.sendMessage(ChatColor.GREEN + "There is a new update (" + latestUpdate + ") for");
			p.sendMessage(ChatColor.GREEN + "Trey's Command Blocker");
			p.sendMessage(ChatColor.RED + "Download at:");
			p.sendMessage(ChatColor.LIGHT_PURPLE + "https://www.spigotmc.org/resources/5280/");
			p.sendMessage(ChatColor.AQUA + "+=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=+");
		}, 4L);
		
	}
}
