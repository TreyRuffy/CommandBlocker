package me.treyruffy.commandblocker;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CommandBlock implements Listener{
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onCommand(PlayerCommandPreprocessEvent event){
		Player sender = event.getPlayer();
		String[] args = event.getMessage().split(" ");
		try {
			if (ConfigManager.getDisabled().getConfigurationSection("DisabledCommands").getKeys(false) == null) {}
		} catch (NullPointerException e){
			return;
		}
		for (String command : ConfigManager.getDisabled().getConfigurationSection("DisabledCommands").getKeys(false)){
			String commands = command.replace("%colon%", ":").replace("%space%", " ");
			String permission = command.replace("%colon%", "").replace("%space%", "");
			if (args[0].equalsIgnoreCase("/" + commands)){
				if (ConfigManager.getDisabled().getString("DisabledCommands." + command + ".Permission") == null){
					if (ConfigManager.getDisabled().getStringList("DisabledCommands." + command + ".Worlds").size() == 0){
						if (!(sender.hasPermission(ConfigManager.getConfig().getString("Default.Permission").replace("%command%", permission)))){
							event.setCancelled(true);
							if (ConfigManager.getDisabled().getString("DisabledCommands." + command + ".Message") == null){
								sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getConfig().getString("Default.Message")));
							} else {
								sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getDisabled().getString("DisabledCommands." + command + ".Message")));
							}
							if (ConfigManager.getDisabled().getStringList("DisabledCommands." + command + ".PlayerCommands") != null){
								for (String s : ConfigManager.getDisabled().getStringList("DisabledCommands." + command + ".PlayerCommands")){
									sender.performCommand(s.replace("%player%", sender.getName()).replace("%username%", sender.getDisplayName()).replace("%command%", commands));
								}
							}
							if (ConfigManager.getDisabled().getStringList("DisabledCommands." + command + ".ConsoleCommands") != null){
								for (String s : ConfigManager.getDisabled().getStringList("DisabledCommands." + command + ".ConsoleCommands")){
									Bukkit.dispatchCommand(Bukkit.getConsoleSender(), s.replace("%player%", sender.getName()).replace("%username%", sender.getDisplayName()).replace("%command%", commands));
								}
							}
							return;
						}
					} else {
						if (ConfigManager.getDisabled().getStringList("DisabledCommands." + command + ".Worlds").contains(sender.getWorld().getName())){
							if (!(sender.hasPermission(ConfigManager.getConfig().getString("Default.Permission").replace("%command%", permission)))){
								event.setCancelled(true);
								if (ConfigManager.getDisabled().getString("DisabledCommands." + command + ".Message") == null){
									sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getConfig().getString("Default.Message")));
								} else {
									sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getDisabled().getString("DisabledCommands." + command + ".Message")));
								}
								if (ConfigManager.getDisabled().getStringList("DisabledCommands." + command + ".PlayerCommands") != null){
									for (String s : ConfigManager.getDisabled().getStringList("DisabledCommands." + command + ".PlayerCommands")){
										sender.performCommand(s.replace("%player%", sender.getName()).replace("%username%", sender.getDisplayName()).replace("%command%", commands));
									}
								}
								if (ConfigManager.getDisabled().getStringList("DisabledCommands." + command + ".ConsoleCommands") != null){
									for (String s : ConfigManager.getDisabled().getStringList("DisabledCommands." + command + ".ConsoleCommands")){
										Bukkit.dispatchCommand(Bukkit.getConsoleSender(), s.replace("%player%", sender.getName()).replace("%username%", sender.getDisplayName()).replace("%command%", commands));
									}
								}
								return;
							}
						}
					}
				} else {
					if (ConfigManager.getDisabled().getStringList("DisabledCommands." + command + ".Worlds").size() == 0){
						if(!(sender.hasPermission(ConfigManager.getDisabled().getString("DisabledCommands." + command + ".Permission")))){
							event.setCancelled(true);
							if (ConfigManager.getDisabled().getString("DisabledCommands." + command + ".Message") == null){
								sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getConfig().getString("Default.Message")));
							} else {
								sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getDisabled().getString("DisabledCommands." + command + ".Message")));
							}
							if (ConfigManager.getDisabled().getStringList("DisabledCommands." + command + ".PlayerCommands") != null){
								for (String s : ConfigManager.getDisabled().getStringList("DisabledCommands." + command + ".PlayerCommands")){
									Bukkit.dispatchCommand(sender, s.replace("%player%", sender.getName()).replace("%username%", sender.getDisplayName()).replace("%command%", commands));
								}
							}
							if (ConfigManager.getDisabled().getStringList("DisabledCommands." + command + ".ConsoleCommands") != null){
								for (String s : ConfigManager.getDisabled().getStringList("DisabledCommands." + command + ".ConsoleCommands")){
									Bukkit.dispatchCommand(Bukkit.getConsoleSender(), s.replace("%player%", sender.getName()).replace("%username%", sender.getDisplayName()).replace("%command%", commands));
								}
							}
							return;
						}
					} else {
						if (ConfigManager.getDisabled().getStringList("DisabledCommands." + command + ".Worlds").contains(sender.getWorld().getName())){
							if(!(sender.hasPermission(ConfigManager.getDisabled().getString("DisabledCommands." + command + ".Permission")))){
								event.setCancelled(true);
								if (ConfigManager.getDisabled().getString("DisabledCommands." + command + ".Message") == null){
									sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getConfig().getString("Default.Message")));
								} else {
									sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getDisabled().getString("DisabledCommands." + command + ".Message")));
								}
								if (ConfigManager.getDisabled().getStringList("DisabledCommands." + command + ".PlayerCommands") != null){
									for (String s : ConfigManager.getDisabled().getStringList("DisabledCommands." + command + ".PlayerCommands")){
										Bukkit.dispatchCommand(sender, s.replace("%player%", sender.getName()).replace("%username%", sender.getDisplayName()).replace("%command%", commands));
									}
								}
								if (ConfigManager.getDisabled().getStringList("DisabledCommands." + command + ".ConsoleCommands") != null){
									for (String s : ConfigManager.getDisabled().getStringList("DisabledCommands." + command + ".ConsoleCommands")){
										Bukkit.dispatchCommand(Bukkit.getConsoleSender(), s.replace("%player%", sender.getName()).replace("%username%", sender.getDisplayName()).replace("%command%", commands));
									}
								}
								return;
							}
						}
					}
				}
			}
		}
	}
}