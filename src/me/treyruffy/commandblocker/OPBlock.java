package me.treyruffy.commandblocker;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class OPBlock implements Listener{
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onCommand(PlayerCommandPreprocessEvent event){
		Player sender = event.getPlayer();
		String[] args = event.getMessage().split(" ");
		try {
			if (ConfigManager.getOpDisabled().getConfigurationSection("DisabledOpCommands").getKeys(false) == null) {}
		} catch (NullPointerException e){
			return;
		}
		for (String command : ConfigManager.getOpDisabled().getConfigurationSection("DisabledOpCommands").getKeys(false)){
			String commands = command.replace("%colon%", ":");
			if (args[0].equalsIgnoreCase("/" + commands)){
				if (sender.isOp()){
					if (ConfigManager.getOpDisabled().getStringList("DisabledOpCommands." + command + ".Worlds").size() == 0){
						event.setCancelled(true);
						if (ConfigManager.getOpDisabled().getString("DisabledOpCommands." + command + ".Message") == null){
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getConfig().getString("Default.Message")));
						} else {
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getOpDisabled().getString("DisabledOpCommands." + command + ".Message")));
						}
						if (ConfigManager.getOpDisabled().getStringList("DisabledOpCommands." + command + ".PlayerCommands") != null){
							for (String s : ConfigManager.getOpDisabled().getStringList("DisabledOpCommands." + command + ".PlayerCommands")){
								sender.performCommand(s.replace("%player%", sender.getName()).replace("%username%", sender.getDisplayName()).replace("%command%", commands));
							}
						}
						if (ConfigManager.getOpDisabled().getStringList("DisabledOpCommands." + command + ".ConsoleCommands") != null){
							for (String s : ConfigManager.getOpDisabled().getStringList("DisabledOpCommands." + command + ".ConsoleCommands")){
								Bukkit.dispatchCommand(Bukkit.getConsoleSender(), s.replace("%player%", sender.getName()).replace("%username%", sender.getDisplayName()).replace("%command%", commands));
							}
						}
						return;
					} else {
						if (ConfigManager.getOpDisabled().getStringList("DisabledOpCommands." + command + ".Worlds").contains(sender.getWorld().getName())){
							event.setCancelled(true);
							if (ConfigManager.getOpDisabled().getString("DisabledOpCommands." + command + ".Message") == null){
								sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getConfig().getString("Default.Message")));
							} else {
								sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getOpDisabled().getString("DisabledOpCommands." + command + ".Message")));
							}
							if (ConfigManager.getOpDisabled().getStringList("DisabledOpCommands." + command + ".PlayerCommands") != null){
								for (String s : ConfigManager.getOpDisabled().getStringList("DisabledOpCommands." + command + ".PlayerCommands")){
									sender.performCommand(s.replace("%player%", sender.getName()).replace("%username%", sender.getDisplayName()).replace("%command%", commands));
								}
							}
							if (ConfigManager.getOpDisabled().getStringList("DisabledOpCommands." + command + ".ConsoleCommands") != null){
								for (String s : ConfigManager.getOpDisabled().getStringList("DisabledOpCommands." + command + ".ConsoleCommands")){
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