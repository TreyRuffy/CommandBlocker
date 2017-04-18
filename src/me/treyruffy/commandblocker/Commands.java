package me.treyruffy.commandblocker;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import com.google.common.collect.Lists;


public class Commands implements CommandExecutor, TabCompleter{
	@SuppressWarnings("unused")
	private final CommandBlocker plugin;
	
	public Commands(CommandBlocker plugin){
		this.plugin = plugin;
	}
	
	public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args){
		if (label.equalsIgnoreCase("cb")||label.equalsIgnoreCase("commandblocker")){
			if (args.length == 0){
				sender.sendMessage(ChatColor.GOLD + "-===[" + ChatColor.BOLD + " Command Blocker " + ChatColor.GOLD + "]===-");
				sender.sendMessage(ChatColor.RED + "You have not supplied enough arguments.");
				sender.sendMessage(ChatColor.RED + "Valid arguments are: add, remove, reload.");
				sender.sendMessage(ChatColor.BLUE + "Made by TreyRuffy!");
				sender.sendMessage(ChatColor.GOLD + "-==============-");
				return false;
			} else if (args.length == 1){
				if (sender.hasPermission("cb.add")){
					if (args[0].equalsIgnoreCase("add")){
						sender.sendMessage(ChatColor.GOLD + "-===[" + ChatColor.BOLD + " Command Blocker " + ChatColor.GOLD + "]===-");
						sender.sendMessage(ChatColor.RED + "You must do /cb add <command> [permission] [message]");
						sender.sendMessage(ChatColor.GOLD + "-==============-");
						return false;
					} else {
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getConfig().getString("Messages.NoPermission")));
						return false;
					}
				} else if (args[0].equalsIgnoreCase("remove")){
					if (sender.hasPermission("cb.remove")){
						sender.sendMessage(ChatColor.GOLD + "-===[" + ChatColor.BOLD + " Command Blocker " + ChatColor.GOLD + "]===-");
						sender.sendMessage(ChatColor.RED + "You must do /cb remove <command>");
						sender.sendMessage(ChatColor.GOLD + "-==============-");
						return false;
					} else {
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getConfig().getString("Messages.NoPermission")));
						return false;
					}
				} else if (args[0].equalsIgnoreCase("reload")){
					if (sender.hasPermission("cb.reload")){
						sender.sendMessage(ChatColor.GOLD + "-==============-");
						sender.sendMessage(ChatColor.GREEN + "Reloading the Command Blocker yml files...");
						try {
							ConfigManager.reloadConfig();
							ConfigManager.reloadDisabled();
							sender.sendMessage(ChatColor.GREEN + "Reloaded the Command Blocker yml files successfully!");
							sender.sendMessage(ChatColor.GOLD + "-==============-");
						} catch (Exception e){
							sender.sendMessage(ChatColor.RED + "Could not reload the Command Blocker yml files.");
							sender.sendMessage(ChatColor.GOLD + "-==============-");
							e.printStackTrace();
						}
						return true;
					} else {
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getConfig().getString("Messages.NoPermission")));
						return false;
					}
				} else {
					sender.sendMessage(ChatColor.GOLD + "-===[" + ChatColor.BOLD + " Command Blocker " + ChatColor.GOLD + "]===-");
					sender.sendMessage(ChatColor.RED + "You have not supplied a valid argument.");
					sender.sendMessage(ChatColor.RED + "Valid arguments are: add, remove, reload.");
					sender.sendMessage(ChatColor.BLUE + "Made by TreyRuffy!");
					sender.sendMessage(ChatColor.GOLD + "-==============-");
					return false;
				}
			} else if (args.length == 2){
				if (args[0].equalsIgnoreCase("add")){
					if (sender.hasPermission("cb.add")){
						if (ConfigManager.getDisabled().getConfigurationSection("DisabledCommands").getKeys(false).contains(args[1])){
							sender.sendMessage(ChatColor.GOLD + "-==============-");
							sender.sendMessage(ChatColor.GREEN + "/" + args[1] + " has already been added!");
							sender.sendMessage(ChatColor.GOLD + "-==============-");
							return false;
						} else {
							ConfigManager.getDisabled().set("DisabledCommands." + args[1] + ".Message", ConfigManager.getConfig().getString("Default.Message"));
							sender.sendMessage(ChatColor.GOLD + "-==============-");
							sender.sendMessage(ChatColor.GREEN + "/" + args[1] + " has been added to the disabled commands list with the message:");
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getConfig().getString("Default.Message")));
							sender.sendMessage(ChatColor.GOLD + "-==============-");
							ConfigManager.saveDisabled();
							return true;
						}
					} else {
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getConfig().getString("Messages.NoPermission")));
						return false;
					}
				} else if (args[0].equalsIgnoreCase("remove")){
					if (sender.hasPermission("cb.remove")){
						if (ConfigManager.getDisabled().getConfigurationSection("DisabledCommands").getKeys(false).contains(args[1])){
							ConfigManager.getDisabled().set("DisabledCommands." + args[1] + ".Permission", null);
							ConfigManager.getDisabled().set("DisabledCommands." + args[1] + ".Message", null);
							ConfigManager.getDisabled().set("DisabledCommands." + args[1], null);
							sender.sendMessage(ChatColor.GOLD + "-==============-");
							sender.sendMessage(ChatColor.GREEN + "Successfully removed the command /" + args[1] + " from disabled.yml");
							sender.sendMessage(ChatColor.GOLD + "-==============-");
							ConfigManager.saveDisabled();
							return true;
						} else {
							sender.sendMessage(ChatColor.GOLD + "-==============-");
							sender.sendMessage(ChatColor.RED + "/" + args[1] + " is not a command which is in disabled.yml.");
							sender.sendMessage(ChatColor.GOLD + "-==============-");
							return false;
						}
					} else {
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getConfig().getString("Messages.NoPermission")));
						return false;
					}
				} else {
					sender.sendMessage(ChatColor.GOLD + "-===[" + ChatColor.BOLD + " Command Blocker " + ChatColor.GOLD + "]===-");
					sender.sendMessage(ChatColor.RED + "You have not supplied a valid argument.");
					sender.sendMessage(ChatColor.RED + "Valid arguments are: add, remove, reload.");
					sender.sendMessage(ChatColor.BLUE + "Made by TreyRuffy!");
					sender.sendMessage(ChatColor.GOLD + "-==============-");
					return false;
				}
			} else if (args.length == 3){
				if (args[0].equalsIgnoreCase("add")){
					if (sender.hasPermission("cb.add")){
						if (ConfigManager.getDisabled().getConfigurationSection("DisabledCommands").getKeys(false).contains(args[1])){
							ConfigManager.getDisabled().set("DisabledCommands." + args[1] + ".Permission", args[2]);
							sender.sendMessage(ChatColor.GOLD + "-==============-");
							sender.sendMessage(ChatColor.GREEN + "Because /" + args[1] + " has already been added, the permission has been changed.");
							sender.sendMessage(ChatColor.GREEN + "The permission is now:");
							sender.sendMessage(ChatColor.GOLD + args[2]);
							sender.sendMessage(ChatColor.GREEN + "With the message being:");
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getConfig().getString("Default.Message")));
							sender.sendMessage(ChatColor.GOLD + "-==============-");
							ConfigManager.saveDisabled();
							return true;
						} else {
							ConfigManager.getDisabled().set("DisabledCommands." + args[1], "");
							ConfigManager.getDisabled().set("DisabledCommands." + args[1] + ".Permission", args[2]);
							sender.sendMessage(ChatColor.GOLD + "-==============-");
							sender.sendMessage(ChatColor.GREEN + "Added /" + args[1] + "to disabled.yml!");
							sender.sendMessage(ChatColor.GREEN + "The permission is:");
							sender.sendMessage(ChatColor.GOLD + args[2]);
							sender.sendMessage(ChatColor.GREEN + "With the message being:");
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getConfig().getString("Default.Message")));
							sender.sendMessage(ChatColor.GOLD + "-==============-");
							ConfigManager.saveDisabled();
							return true;
						}
					} else {
						sender.sendMessage(ChatColor.GOLD + "-===[" + ChatColor.BOLD + " Command Blocker " + ChatColor.GOLD + "]===-");
						sender.sendMessage(ChatColor.RED + "You have not supplied a valid argument.");
						sender.sendMessage(ChatColor.RED + "Valid arguments are: add, remove, reload.");
						sender.sendMessage(ChatColor.BLUE + "Made by TreyRuffy!");
						sender.sendMessage(ChatColor.GOLD + "-==============-");
						return false;
					}
				} else {
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getConfig().getString("Messages.NoPermission")));
					return false;
				}
			} else if (args.length >= 4){
				if (args[0].equalsIgnoreCase("add")){
					if (sender.hasPermission("cb.add")){
						int i;
						String msg = "";
						sender.sendMessage(ChatColor.GOLD + "-==============-");
						ConfigManager.getDisabled().set("DisabledCommands." + args[1] + ".Permission", args[2]);
						for (i = 3; i < args.length; i++){
							msg = msg + args[i] + " ";
						}
						ConfigManager.getDisabled().set("DisabledCommands." + args[1] + ".Message", msg);
						sender.sendMessage(ChatColor.GREEN + "Added /" + args[1] + "to disabled.yml!");
						sender.sendMessage(ChatColor.GREEN + "The permission is:");
						sender.sendMessage(ChatColor.GOLD + args[2]);
						sender.sendMessage(ChatColor.GREEN + "With the message being:");
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
						sender.sendMessage(ChatColor.GOLD + "-==============-");
						ConfigManager.saveDisabled();
						return true;
					} else {
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getConfig().getString("Messages.NoPermission")));
						return false;
					}
				} else {
					sender.sendMessage(ChatColor.GOLD + "-===[" + ChatColor.BOLD + " Command Blocker " + ChatColor.GOLD + "]===-");
					sender.sendMessage(ChatColor.RED + "You have not supplied a valid argument.");
					sender.sendMessage(ChatColor.RED + "Valid arguments are: add, remove, reload.");
					sender.sendMessage(ChatColor.BLUE + "Made by TreyRuffy!");
					sender.sendMessage(ChatColor.GOLD + "-==============-");
					return false;
				}
			} else {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getConfig().getString("Messages.NoPermission")));
				return false;
			}
		}
		return false;
	}
	
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args){
		if (label.equalsIgnoreCase("commandblocker")||label.equalsIgnoreCase("cb")){
			ArrayList<String> tab = new ArrayList<String>();
			List<String> tabList = Lists.newArrayList();
			if (sender.hasPermission("cb.add")){
				tab.add("add");
			}
			if (sender.hasPermission("cb.remove")){
				tab.add("remove");
			}
			if (sender.hasPermission("cb.reload")){
				tab.add("reload");
			}
			if (args.length == 1) {
				for (String list : tab) {
					if (list.toLowerCase().startsWith(args[0].toLowerCase())){
						tabList.add(list);
					}
				}
			}
			return tabList;
	    }
	    return null;
	  }
}
