package me.treyruffy.commandblocker.Bukkit.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import me.treyruffy.commandblocker.Universal;
import me.treyruffy.commandblocker.Bukkit.BukkitMain;
import me.treyruffy.commandblocker.Bukkit.ConfigManager;
import me.treyruffy.commandblocker.updater.UpdateChecker;

public class Update implements Listener {
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e){
		if (ConfigManager.getConfig().getBoolean("Updates.Check")){
			if (ConfigManager.getConfig().getBoolean("Updates.TellPlayers")){
				final Player p = e.getPlayer();
				if (p.hasPermission("cb.updates")){
					Boolean updates = UpdateChecker.getLastUpdate(Universal.get().getMethods());
					if (updates){
						Bukkit.getScheduler().scheduleSyncDelayedTask(BukkitMain.get(), new Runnable() {
							@Override
							public void run() {
								p.sendMessage(ChatColor.AQUA + "+=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=+");
								p.sendMessage(ChatColor.GREEN + "There is a new update for");
								p.sendMessage(ChatColor.GREEN + "Command Blocker");
								p.sendMessage(ChatColor.RED + "Download at:");
								p.sendMessage(ChatColor.LIGHT_PURPLE + "https://www.spigotmc.org/resources/5280/");
								p.sendMessage(ChatColor.AQUA + "+=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=+");
							}
						}, 4);
					}
				}
			}
		}
	}
}
