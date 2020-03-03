package me.treyruffy.commandblocker.Bukkit.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import me.clip.placeholderapi.PlaceholderAPI;
import me.treyruffy.commandblocker.Bukkit.BukkitMain;
import me.treyruffy.commandblocker.Bukkit.ConfigManager;

public class BukkitBlock implements Listener{
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onCommand(PlayerCommandPreprocessEvent e){
		Player p = e.getPlayer();
		String[] args = e.getMessage().split(" ");
		
		FileConfiguration disabled = ConfigManager.getDisabled();
		FileConfiguration config = ConfigManager.getConfig();
		FileConfiguration opDisabled = ConfigManager.getOpDisabled();
		
		
		if (!p.isOp()) {
			
			if (disabled.getConfigurationSection("DisabledCommands") == null) {
				return;
			}
			
			for (String command : disabled.getConfigurationSection("DisabledCommands").getKeys(false)){
				String commands = command.replace("%colon%", ":");
				String permission = command.replace("%colon%", "");
				
				
				if (args[0].equalsIgnoreCase("/" + commands)) {
					if (disabled.getStringList("DisabledCommands." + command + ".Worlds").size() > 0) {
						if (!(disabled.getStringList("DisabledCommands." + command + ".Worlds").contains(p.getWorld().getName()))) {
							return;
						}
					}
					
					if (disabled.getString("DisabledCommands." + command + ".Permission") == null) {
						if (p.hasPermission(config.getString("Default.Permission").replace("%command%", permission))) {
							return;
						}
					} else {
						if (p.hasPermission(disabled.getString("DisabledCommands." + command + ".Permission"))) {
							return;
						}
					}
					
					e.setCancelled(true);
					String message;
					if (disabled.getString("DisabledCommands." + command + ".Message") == null) {
						String msg = ChatColor.translateAlternateColorCodes('&', config.getString("Default.Message"));
						
						message = (BukkitMain.isPapiEnabled() ? PlaceholderAPI.setPlaceholders(p, msg) : msg);
					} else {
						String msg = ChatColor.translateAlternateColorCodes('&', disabled.getString("DisabledCommands." + command + ".Message"));
						
						message = (BukkitMain.isPapiEnabled() ? PlaceholderAPI.setPlaceholders(p, msg) : msg);
					}
					
					if (message.length() != 0){
						p.sendMessage(message);
					}
					
					if (disabled.getStringList("DisabledCommands." + command + ".PlayerCommands").size() > 0) {
						for (String s : disabled.getStringList("DisabledCommands." + command + ".PlayerCommands")) {
							p.performCommand(s.replace("%player%", p.getName()).replace("%username%", p.getDisplayName()).replace("%command%", commands));
						}
					}
					
					if (disabled.getStringList("DisabledCommands." + command + ".ConsoleCommands").size() > 0) {
						for (String s : disabled.getStringList("DisabledCommands." + command + ".ConsoleCommands")) {
							Bukkit.dispatchCommand(Bukkit.getConsoleSender(), s.replace("%player%", p.getName()).replace("%username%", p.getDisplayName()).replace("%command%", commands));
						}
					}
				}
			}
		} else {
			if (opDisabled.getConfigurationSection("DisabledOpCommands") == null) {
				return;
			}
			
			for (String command : opDisabled.getConfigurationSection("DisabledOpCommands").getKeys(false)){
				String commands = command.replace("%colon%", ":");
				String permission = command.replace("%colon%", "");
				
				
				if (args[0].equalsIgnoreCase("/" + commands)) {
					if (opDisabled.getStringList("DisabledOpCommands." + command + ".Worlds").size() > 0) {
						if (!(opDisabled.getStringList("DisabledOpCommands." + command + ".Worlds").contains(p.getWorld().getName()))) {
							return;
						}
					}
					
					e.setCancelled(true);
					String message;
					if (opDisabled.getString("DisabledOpCommands." + command + ".Message") == null) {
						String msg = ChatColor.translateAlternateColorCodes('&', config.getString("Default.Message"));
						
						message = (BukkitMain.isPapiEnabled() ? PlaceholderAPI.setPlaceholders(p, msg) : msg);
					} else {
						String msg = ChatColor.translateAlternateColorCodes('&', opDisabled.getString("DisabledOpCommands." + command + ".Message"));
						
						message = (BukkitMain.isPapiEnabled() ? PlaceholderAPI.setPlaceholders(p, msg) : msg);
					}
					
					p.sendMessage(message);
					
					if (opDisabled.getStringList("DisabledOpCommands." + command + ".PlayerCommands").size() > 0) {
						for (String s : opDisabled.getStringList("DisabledOpCommands." + command + ".PlayerCommands")) {
							p.performCommand(s.replace("%player%", p.getName()).replace("%username%", p.getDisplayName()).replace("%command%", commands));
						}
					}
					
					if (opDisabled.getStringList("DisabledOpCommands." + command + ".ConsoleCommands").size() > 0) {
						for (String s : opDisabled.getStringList("DisabledOpCommands." + command + ".ConsoleCommands")) {
							Bukkit.dispatchCommand(Bukkit.getConsoleSender(), s.replace("%player%", p.getName()).replace("%username%", p.getDisplayName()).replace("%command%", commands));
						}
					}
				}
			}
		}
	}
}
