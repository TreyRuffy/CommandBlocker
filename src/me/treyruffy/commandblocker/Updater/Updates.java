package me.treyruffy.commandblocker.Updater;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import me.treyruffy.commandblocker.CommandBlocker;
import me.treyruffy.commandblocker.ConfigManager;

public class Updates implements Listener {

	public static void updateCheck(){
		if (ConfigManager.getConfig().getBoolean("Updates.Check")){
			UpdateChecker.getLastUpdate();
			Object[] updates = UpdateChecker.getLastUpdate();
			if (updates.length == 2){
				ConsoleCommandSender console = Bukkit.getConsoleSender();
				console.sendMessage(ChatColor.AQUA + "+=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=+");
				console.sendMessage(ChatColor.GREEN + "There is a new update for");
				console.sendMessage(ChatColor.GREEN + "Command Blocker");
				console.sendMessage(ChatColor.RED + "Download at:");
				console.sendMessage(ChatColor.LIGHT_PURPLE + "https://www.spigotmc.org/resources/5280/");
				console.sendMessage(ChatColor.AQUA + "+=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=+");
				if (ConfigManager.getConfig().getBoolean("Updates.TellPlayers")){
					for (Player p : Bukkit.getOnlinePlayers()){
						if (p.hasPermission("cb.updates")){
							p.sendMessage(ChatColor.AQUA + "+=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=+");
							p.sendMessage(ChatColor.GREEN + "There is a new update for");
							p.sendMessage(ChatColor.GREEN + "Command Blocker");
							p.sendMessage(ChatColor.RED + "Download at:");
							p.sendMessage(ChatColor.LIGHT_PURPLE + "https://www.spigotmc.org/resources/5280/");
							p.sendMessage(ChatColor.AQUA + "+=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=+");
						}
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e){
		if (ConfigManager.getConfig().getBoolean("Updates.Check")){
			if (ConfigManager.getConfig().getBoolean("Updates.TellPlayers")){
				final Player p = e.getPlayer();
				if (p.hasPermission("cb.updates")){
					Bukkit.getScheduler().scheduleSyncDelayedTask(CommandBlocker.getPlugin(CommandBlocker.class), new Runnable() {
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
