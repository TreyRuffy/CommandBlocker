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
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		if (cmd.getName().equalsIgnoreCase("cb")){
			if (sender.hasPermission("cb.add")||sender.hasPermission("cb.reload")||sender.hasPermission("cb.remove")){
				if (args.length == 0){
					sendMessage(sender, "noArgs", args);
					return true;
				} else if (args.length == 1){
					if (args[0].equalsIgnoreCase("add")){
						if (sender.hasPermission("cb.add")){
							sendMessage(sender, "addNoArgs", args);
							return true;
						} else {
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getConfig().getString("Messages.NoPermission")));
							return true;
						}
					} else if (args[0].equalsIgnoreCase("remove")){
						if (sender.hasPermission("cb.remove")){
							sendMessage(sender, "removeNoArgs", args);
							return true;
						} else {
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getConfig().getString("Messages.NoPermission")));
							return true;
						}
					} else if (args[0].equalsIgnoreCase("reload")){
						if (sender.hasPermission("cb.reload")){
							sendMessage(sender, "reload", args);
							return true;
						} else {
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getConfig().getString("Messages.NoPermission")));
							return true;
						}
					} else if (args[0].equalsIgnoreCase("addOp")){
						if (sender.hasPermission("cb.addOp")){
							sendMessage(sender, "addOpNoArgs", args);
							return true;
						} else {
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getConfig().getString("Messages.NoPermission")));
							return true;
						}
					} else if (args[0].equalsIgnoreCase("removeOp")){
						if (sender.hasPermission("cb.removeOp")){
							sendMessage(sender, "removeOpNoArgs", args);
							return true;
						} else {
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getConfig().getString("Messages.NoPermission")));
							return true;
						}
					} else {
						sendMessage(sender, "noArgs", args);
						return true;
					}
				} else if (args.length == 2){
					if (args[0].equalsIgnoreCase("add")){
						if (sender.hasPermission("cb.add")){
							if (ConfigManager.getDisabled().getConfigurationSection("DisabledCommands").getKeys(false).contains(args[1])){
								sendMessage(sender, "addAlreadyAdded", args);
								return true;
							} else {
								ConfigManager.getDisabled().set("DisabledCommands." + args[1] + ".Message", ConfigManager.getConfig().getString("Default.Message"));
								sendMessage(sender, "added", args);
								ConfigManager.saveDisabled();
								return true;
							}
						} else {
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getConfig().getString("Messages.NoPermission")));
							return true;
						}
					} else if (args[0].equalsIgnoreCase("remove")){
						if (sender.hasPermission("cb.remove")){
							if (ConfigManager.getDisabled().getConfigurationSection("DisabledCommands").getKeys(false).contains(args[1])){
								ConfigManager.getDisabled().set("DisabledCommands." + args[1] + ".Permission", null);
								ConfigManager.getDisabled().set("DisabledCommands." + args[1] + ".Message", null);
								ConfigManager.getDisabled().set("DisabledCommands." + args[1], null);
								sendMessage(sender, "remove", args);
								ConfigManager.saveDisabled();
								return true;
							} else {
								sendMessage(sender, "couldNotRemove", args);
								return true;
							}
						} else {
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getConfig().getString("Messages.NoPermission")));
							return true;
						}
					} else if (args[0].equalsIgnoreCase("addOp")){
						if (sender.hasPermission("cb.addop")){
							if (ConfigManager.getOpDisabled().getConfigurationSection("DisabledOpCommands").getKeys(false).contains(args[1])){
								sendMessage(sender, "addAlreadyAdded", args);
								return true;
							} else {
								ConfigManager.getOpDisabled().set("DisabledOpCommands." + args[1] + ".Message", ConfigManager.getConfig().getString("Default.Message"));
								sendMessage(sender, "addedOp", args);
								ConfigManager.saveOpDisabled();
								return true;
							}
						} else {
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getConfig().getString("Messages.NoPermission")));
							return true;
						}
					}  else if (args[0].equalsIgnoreCase("remove")){
						if (sender.hasPermission("cb.removeOp")){
							if (ConfigManager.getOpDisabled().getConfigurationSection("DisabledOpCommands").getKeys(false).contains(args[1])){
								ConfigManager.getOpDisabled().set("DisabledOpCommands." + args[1] + ".Message", null);
								ConfigManager.getOpDisabled().set("DisabledOpCommands." + args[1], null);
								sendMessage(sender, "removeOp", args);
								ConfigManager.saveOpDisabled();
								return true;
							} else {
								sendMessage(sender, "couldNotRemoveOp", args);
								return true;
							}
						} else {
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getConfig().getString("Messages.NoPermission")));
							return true;
						}
					} else {
						sendMessage(sender, "noArgs", args);
						return true;
					}
				} else if (args.length == 3){
					if (args[0].equalsIgnoreCase("add")){
						if (sender.hasPermission("cb.add")){
							if (ConfigManager.getDisabled().getConfigurationSection("DisabledCommands").getKeys(false).contains(args[1])){
								ConfigManager.getDisabled().set("DisabledCommands." + args[1] + ".Permission", args[2]);
								sendMessage(sender, "changedPerm", args);
								ConfigManager.saveDisabled();
								return true;
							} else {
								ConfigManager.getDisabled().set("DisabledCommands." + args[1], "");
								ConfigManager.getDisabled().set("DisabledCommands." + args[1] + ".Permission", args[2]);
								sendMessage(sender, "newWithPerm", args);
								ConfigManager.saveDisabled();
								return true;
							}
						} else {
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getConfig().getString("Messages.NoPermission")));
							return true;
						}
					} else {
						sendMessage(sender, "noArgs", args);
						return true;
					}
				} else if (args.length >= 3){
				 	if (args[0].equalsIgnoreCase("addOp")){
						if (sender.hasPermission("cb.addop")){
							sendMessage(sender, "newOpWithMessage", args);
							ConfigManager.saveOpDisabled();
							return true;
						} else {
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getConfig().getString("Messages.NoPermission")));
							return true;
						}
					}
				}
				if (args.length >= 4){
					if (args[0].equalsIgnoreCase("add")){
						if (sender.hasPermission("cb.add")){
							sendMessage(sender, "newWithMessage", args);
							ConfigManager.saveDisabled();
							return true;
						} else {
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getConfig().getString("Messages.NoPermission")));
							return true;
						}
					} else {
						sendMessage(sender, "noArgs", args);
						return true;
					}
				} else {
					sendMessage(sender, "noArgs", args);
					return true;
				}
			} else {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getConfig().getString("Messages.NoPermission")));
				return true;
			}
		}
		return true;
	}
	
	public void sendMessage(CommandSender sender, String string, String[] args) {
		// /cb -=- No args or not correct args
		if (string.equalsIgnoreCase("noArgs")){
			sender.sendMessage(ChatColor.BLUE + "-=====[" + ChatColor.AQUA + ChatColor.BOLD + " Command Blocker " + ChatColor.BLUE + "]=====-");
			sender.sendMessage(ChatColor.RED + "You have not supplied enough arguments.");
			sender.sendMessage(ChatColor.RED + "Valid arguments are: add, remove, reload, addop, removeop.");
			sender.sendMessage(ChatColor.GREEN + "Made by TreyRuffy!");
			sender.sendMessage(ChatColor.BLUE + "-======================-");
			return;
		}
		// /cb add -=- Add with no args
		if (string.equalsIgnoreCase("addNoArgs")){
			sender.sendMessage(ChatColor.BLUE + "-=====[" + ChatColor.AQUA + ChatColor.BOLD + " Command Blocker " + ChatColor.BLUE + "]=====-");
			sender.sendMessage(ChatColor.RED + "You must do /cb add <command> [permission] [message]");
			sender.sendMessage(ChatColor.BLUE + "-======================-");
			return;
		}
		// /cb remove -=- Remove with no args
		if (string.equalsIgnoreCase("removeNoArgs")){
			sender.sendMessage(ChatColor.BLUE + "-=====[" + ChatColor.AQUA + ChatColor.BOLD + " Command Blocker " + ChatColor.BLUE + "]=====-");
			sender.sendMessage(ChatColor.RED + "You must do /cb remove <command>");
			sender.sendMessage(ChatColor.BLUE + "-======================-");
			return;
		}
		// /cb reload -=- Reloaded
		if (string.equalsIgnoreCase("reload")){
			sender.sendMessage(ChatColor.BLUE + "-=====[" + ChatColor.AQUA + ChatColor.BOLD + " Command Blocker " + ChatColor.BLUE + "]=====-");
			sender.sendMessage(ChatColor.GREEN + "Reloading the Command Blocker yml files...");
			try {
				ConfigManager.reloadConfig();
				ConfigManager.reloadDisabled();
				ConfigManager.reloadOpDisabled();
				sender.sendMessage(ChatColor.GREEN + "Reloaded the Command Blocker YAML files successfully!");
				sender.sendMessage(ChatColor.BLUE + "-======================-");
			} catch (Exception e){
				sender.sendMessage(ChatColor.RED + "Could not reload the Command Blocker YAML files.");
				sender.sendMessage(ChatColor.BLUE + "-======================-");
				e.printStackTrace();
			}
			return;
		}
		// /cb addOp -=- AddOp with no args
		if (string.equalsIgnoreCase("addOpNoArgs")){
			sender.sendMessage(ChatColor.BLUE + "-=====[" + ChatColor.AQUA + ChatColor.BOLD + " Command Blocker " + ChatColor.BLUE + "]=====-");
			sender.sendMessage(ChatColor.RED + "You must do /cb addOp <command> [message]");
			sender.sendMessage(ChatColor.BLUE + "-======================-");
		}
		if (string.equalsIgnoreCase("removeOpNoArgs")){
			sender.sendMessage(ChatColor.BLUE + "-=====[" + ChatColor.AQUA + ChatColor.BOLD + " Command Blocker " + ChatColor.BLUE + "]=====-");
			sender.sendMessage(ChatColor.RED + "You must do /cb removeOp <command>");
			sender.sendMessage(ChatColor.BLUE + "-======================-");
		}
		// /cb add <command> -=- Already Added
		if (string.equalsIgnoreCase("addAlreadyAdded")){
			sender.sendMessage(ChatColor.BLUE + "-======================-");
			sender.sendMessage(ChatColor.GREEN + "/" + args[1] + " has already been added!");
			sender.sendMessage(ChatColor.BLUE + "-======================-");
			return;
		}
		// /cb add <command> -=- Added
		if (string.equalsIgnoreCase("added")){
			sender.sendMessage(ChatColor.BLUE + "-======================-");
			sender.sendMessage(ChatColor.GREEN + "/" + args[1] + " has been added to the disabled commands list with the message:");
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getConfig().getString("Default.Message")));
			sender.sendMessage(ChatColor.BLUE + "-======================-");
			return;
		}
		// /cb addOp <command> -=- Added for OP
		if (string.equalsIgnoreCase("addedOp")){
			sender.sendMessage(ChatColor.BLUE + "-======================-");
			sender.sendMessage(ChatColor.GREEN + "/" + args[1] + " has been added to the op disabled commands list with the message:");
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getConfig().getString("Default.Message")));
			sender.sendMessage(ChatColor.BLUE + "-======================-");
			return;
		}
		// /cb remove <command> -=- Removed
		if (string.equalsIgnoreCase("remove")){
			sender.sendMessage(ChatColor.BLUE + "-======================-");
			sender.sendMessage(ChatColor.GREEN + "Successfully removed the command /" + args[1] + " from disabled.yml");
			sender.sendMessage(ChatColor.BLUE + "-======================-");
			return;
		}
		// /cb remove <command> -=- Not command
		if (string.equalsIgnoreCase("couldNotRemove")){
			sender.sendMessage(ChatColor.BLUE + "-======================-");
			sender.sendMessage(ChatColor.RED + "/" + args[1] + " is not a command which is in disabled.yml.");
			sender.sendMessage(ChatColor.BLUE + "-======================-");
			return;
		}
		// /cb removeOp <command> -=- Removed
		if (string.equalsIgnoreCase("removeOp")){
			sender.sendMessage(ChatColor.BLUE + "-======================-");
			sender.sendMessage(ChatColor.GREEN + "Successfully removed the command /" + args[1] + " from opblock.yml");
			sender.sendMessage(ChatColor.BLUE + "-======================-");
			return;
		}
		// /cb removeOp <command> -=- Not command
		if (string.equalsIgnoreCase("couldNotRemoveOp")){
			sender.sendMessage(ChatColor.BLUE + "-======================-");
			sender.sendMessage(ChatColor.RED + "/" + args[1] + " is not a command which is in opblock.yml.");
			sender.sendMessage(ChatColor.BLUE + "-======================-");
			return;
		}
		// /cb add <command> <perm> -=- Changed permission
		if (string.equalsIgnoreCase("changedPerm")){
			sender.sendMessage(ChatColor.BLUE + "-======================-");
			sender.sendMessage(ChatColor.GREEN + "Because /" + args[1] + " has already been added, the permission has been changed.");
			sender.sendMessage(ChatColor.GREEN + "The permission is now:");
			sender.sendMessage(ChatColor.GOLD + args[2]);
			sender.sendMessage(ChatColor.GREEN + "With the message being:");
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getConfig().getString("Default.Message")));
			sender.sendMessage(ChatColor.BLUE + "-======================-");
			return;
		}
		// /cb add <command> <perm> -=- Added with permission
		if (string.equalsIgnoreCase("newWithPerm")){
			sender.sendMessage(ChatColor.BLUE + "-======================-");
			sender.sendMessage(ChatColor.GREEN + "Added /" + args[1] + " to disabled.yml!");
			sender.sendMessage(ChatColor.GREEN + "The permission is:");
			sender.sendMessage(ChatColor.GOLD + args[2]);
			sender.sendMessage(ChatColor.GREEN + "With the message being:");
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getConfig().getString("Default.Message")));
			sender.sendMessage(ChatColor.BLUE + "-======================-");
			return;
		}
		// /cb add <command> <perm> <message> -=- Added with a message
		if (string.equalsIgnoreCase("newWithMessage")){
			int i;
			String msg = "";
			sender.sendMessage(ChatColor.BLUE + "-======================-");
			ConfigManager.getDisabled().set("DisabledCommands." + args[1] + ".Permission", args[2]);
			for (i = 3; i < args.length; i++){
				msg = msg + args[i] + " ";
			}
			ConfigManager.getDisabled().set("DisabledCommands." + args[1] + ".Message", msg);
			sender.sendMessage(ChatColor.GREEN + "Added /" + args[1] + " to disabled.yml!");
			sender.sendMessage(ChatColor.GREEN + "The permission is:");
			sender.sendMessage(ChatColor.GOLD + args[2]);
			sender.sendMessage(ChatColor.GREEN + "With the message being:");
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
			sender.sendMessage(ChatColor.BLUE + "-======================-");
			return;
		}
		// /cb addOp <command> <message> -=- Added op with message
		if (string.equalsIgnoreCase("newOpWithMessage")){
			int i;
			String msg = "";
			sender.sendMessage(ChatColor.BLUE + "-======================-");
			for (i = 2; i < args.length; i++){
				msg = msg + args[i] + " ";
			}
			ConfigManager.getOpDisabled().set("DisabledOpCommands." + args[1] + ".Message", msg);
			sender.sendMessage(ChatColor.GREEN + "Added /" + args[1] + " to opblock.yml!");
			sender.sendMessage(ChatColor.GREEN + "With the message being:");
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
			sender.sendMessage(ChatColor.BLUE + "-======================-");
			return;
		}
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
			if (sender.hasPermission("cb.addOp")){
				tab.add("addOp");
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
