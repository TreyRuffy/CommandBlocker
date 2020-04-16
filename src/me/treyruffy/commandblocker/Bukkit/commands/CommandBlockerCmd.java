package me.treyruffy.commandblocker.bukkit.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import com.google.gson.JsonObject;

import me.clip.placeholderapi.PlaceholderAPI;
import me.treyruffy.commandblocker.Log;
import me.treyruffy.commandblocker.Universal;
import me.treyruffy.commandblocker.bukkit.BukkitMain;
import me.treyruffy.commandblocker.bukkit.Variables;
import me.treyruffy.commandblocker.bukkit.api.BlockedCommands;
import me.treyruffy.commandblocker.bukkit.api.BlockedOpCommands;
import me.treyruffy.commandblocker.bukkit.config.ConfigManager;
import me.treyruffy.commandblocker.bukkit.gui.DisabledGui;
import me.treyruffy.commandblocker.bukkit.gui.OpDisabledGui;
import me.treyruffy.commandblocker.bukkit.listeners.CommandValueListener;
import me.treyruffy.commandblockerlegacy.OldConfigManager;
import org.jetbrains.annotations.NotNull;

public class CommandBlockerCmd implements CommandExecutor {

	@Override
	public boolean onCommand(@NotNull CommandSender sender, Command cmd, @NotNull String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("cb")) {
			
			// Player does not have permission to execute the command
			if (!(sender.hasPermission("cb.add") || sender.hasPermission("cb.reload") || sender.hasPermission("cb.remove") || sender.hasPermission("cb.addop")
    				|| sender.hasPermission("cb.removeop")) && (sender instanceof Player)) {
				noPermissions(sender);
				return true;
			}
			
			// No arguments
			if (args.length == 0) {
				noArgs(sender);
				return true;
			}
			
			// Add argument
			if (args[0].equalsIgnoreCase("add")) {
				
				// Player doesn't have permissions to do /cb add
				if (!sender.hasPermission("cb.add") && (sender instanceof Player)) {
					noPermissions(sender);
					return true;
				}
				
				if (args.length == 1) {
				
					if (sender instanceof Player) {
						Player p = (Player) sender;
						CommandValueListener.lookingFor.put(p.getUniqueId().toString(), "add");
						CommandValueListener.partsHad.put(p.getUniqueId().toString(), new JsonObject());
						/*
						 * Send message to input a command
						 */
						p.sendMessage("add a command to block");
					} else {
						sender.sendMessage("args are: /cb add 'command' 'permission' 'message'");
					}
				} else if (args.length == 2) {
					if (sender instanceof Player) {
						Player p = (Player) sender;
						CommandValueListener.lookingFor.put(p.getUniqueId().toString(), "add");
						CommandValueListener.partsHad.put(p.getUniqueId().toString(), new JsonObject());
						
						JsonObject object = CommandValueListener.partsHad.get(p.getUniqueId().toString());
						object.addProperty("command", args[1]);
						CommandValueListener.partsHad.put(p.getUniqueId().toString(), object);
						
						p.sendMessage("add a permission");
					} else {
						sender.sendMessage("args are: /cb add 'command' 'permission' 'message'");
					}
				} else if (args.length == 3) {
					if (sender instanceof Player) {
						Player p = (Player) sender;
						CommandValueListener.lookingFor.put(p.getUniqueId().toString(), "add");
						CommandValueListener.partsHad.put(p.getUniqueId().toString(), new JsonObject());
						
						JsonObject object = CommandValueListener.partsHad.get(p.getUniqueId().toString());
						object.addProperty("command", args[1]);
						object.addProperty("permission", args[2]);
						CommandValueListener.partsHad.put(p.getUniqueId().toString(), object);
						
						p.sendMessage("add a message");
					} else {
						sender.sendMessage("args are: /cb add 'command' 'permission' 'message'");
					}
				} else {
					if (sender instanceof Player) {
						Player p = (Player) sender;
						CommandValueListener.lookingFor.put(p.getUniqueId().toString(), "add");
						CommandValueListener.partsHad.put(p.getUniqueId().toString(), new JsonObject());
						
						JsonObject object = CommandValueListener.partsHad.get(p.getUniqueId().toString());
						object.addProperty("command", args[1]);
						object.addProperty("permission", args[2]);
						StringBuilder msg = new StringBuilder(args[3]);
						for (int i = 4; i < args.length; i++) {
							msg.append(" ").append(args[i]);
						}
						object.addProperty("permission", msg.toString());
						CommandValueListener.partsHad.put(p.getUniqueId().toString(), object);
						
						p.sendMessage("add a message");
					} else {
						String command = args[1].substring(0, 1).toUpperCase() + args[1].substring(1).toLowerCase();
						StringBuilder msg = new StringBuilder(args[3]);
						for (int i = 4; i < args.length - 1; i++) {
							msg.append(" ").append(args[i]);
						}
						if (BlockedCommands.addBlockedCommand(command, args[2], msg.toString(), null, null, null)) {
							sender.sendMessage("Added /" + command + " with the permission " + args[2] + " and message " + ChatColor.translateAlternateColorCodes('&', msg.toString()));
							if (sender instanceof ConsoleCommandSender) {
								Log.addLog(Universal.get().getMethods(), "CONSOLE: Added /" + command + " to disabled.yml with permission " + args[2] + " and message " + msg);
							} else {
								Log.addLog(Universal.get().getMethods(), sender.getName() + ": Added /" + command + " to disabled.yml with permission " + args[2] + " and message " + msg);
							}
						} else {
							sender.sendMessage("Could not add /" + command + " to the blocked commands");
						}
					}
				}
				return true;
			}	
			// Reload argument
			else if (args[0].equalsIgnoreCase("reload")) {
				
				// Player doesn't have permissions to do /cb reload
				if (!sender.hasPermission("cb.reload") && (sender instanceof Player)) {
					noPermissions(sender);
					return true;
				}
				
				sender.sendMessage(ChatColor.BLUE + "-=====[" + ChatColor.AQUA + ChatColor.BOLD + " Command Blocker " + ChatColor.BLUE + "]=====-");
				sender.sendMessage(ChatColor.GREEN + "Reloading the Command Blocker yml files...");
				try {
					if (!BukkitMain.oldConfig()) {
						ConfigManager.reloadConfig();
						ConfigManager.reloadDisabled();
						ConfigManager.reloadOpDisabled();
					} else {
						OldConfigManager.reloadConfig();
						OldConfigManager.reloadDisabled();
						OldConfigManager.reloadOpDisabled();
					}
					sender.sendMessage(ChatColor.GREEN + "Reloaded the Command Blocker YAML files successfully!");
					sender.sendMessage(ChatColor.BLUE + "-======================-");
				} catch (Exception e) {
					sender.sendMessage(ChatColor.RED + "Could not reload the Command Blocker YAML files.");
					sender.sendMessage(ChatColor.BLUE + "-======================-");
					
					/*
					 * Add stack trace to hovering over the text?
					 * 
					 * Or can maybe do "click for more info"
					 */
					
					e.printStackTrace();
				}
				return true;
			}
			
			// Remove argument
			else if (args[0].equalsIgnoreCase("remove")) {
				
				// Player doesn't have permissions to do /cb remove
				if (!sender.hasPermission("cb.remove") && (sender instanceof Player)) {
					noPermissions(sender);
					return true;
				}
				
				if (args.length == 1) {
					if (sender instanceof Player) {
						Player p = (Player) sender;
						CommandValueListener.lookingFor.put(p.getUniqueId().toString(), "remove");
						CommandValueListener.partsHad.put(p.getUniqueId().toString(), new JsonObject());
						/*
						 * Send message to input a command
						 */
						p.sendMessage("add a command to unblock");
					} else {
						sender.sendMessage("args are: /cb remove 'command'");
					}
				} else {
					StringBuilder command = new StringBuilder(args[1]);
					for (int i = 2; i < args.length; i++) {
						command.append(" ").append(args[i]);
					}
					if (BlockedCommands.removeBlockedCommand(command.toString())) {
						sender.sendMessage("removed " + command + " from the blocked list");
						if (sender instanceof ConsoleCommandSender) {
							Log.addLog(Universal.get().getMethods(), "CONSOLE: Removed /" + command + " from disabled.yml");
						} else {
							Log.addLog(Universal.get().getMethods(), sender.getName() + ": Removed /" + command + " from disabled.yml");
						}
					} else {
						sender.sendMessage("could not remove " + command + " from the blocked list");
					}
				}
				return true;
			}
			
			// AddOp argument
			else if (args[0].equalsIgnoreCase("addop")) {
				
				// Player doesn't have permissions to do /cb addop
				if (!sender.hasPermission("cb.addop") && (sender instanceof Player)) {
					noPermissions(sender);
					return true;
				}
				
				if (args.length == 1) {
					if (sender instanceof Player) {
						Player p = (Player) sender;
						CommandValueListener.lookingFor.put(p.getUniqueId().toString(), "addop");
						CommandValueListener.partsHad.put(p.getUniqueId().toString(), new JsonObject());
						/*
						 * Send message to input a command
						 */
						p.sendMessage("add an op command to block");
					} else {
						sender.sendMessage("arguments: /cb addop 'command' 'message'");
					}
					
				} else if (args.length == 2) {
					if (sender instanceof Player) {
						Player p = (Player) sender;
						CommandValueListener.lookingFor.put(p.getUniqueId().toString(), "addop");
						CommandValueListener.partsHad.put(p.getUniqueId().toString(), new JsonObject());
						
						JsonObject object = CommandValueListener.partsHad.get(p.getUniqueId().toString());
						object.addProperty("command", args[1]);
						p.sendMessage("add a message");
					} else {
						sender.sendMessage("arguments: /cb addop 'command' 'message'");
					}
				} else {
					if (sender instanceof Player) {
						Player p = (Player) sender;
						CommandValueListener.lookingFor.put(p.getUniqueId().toString(), "addop");
						CommandValueListener.partsHad.put(p.getUniqueId().toString(), new JsonObject());
						
						JsonObject object = CommandValueListener.partsHad.get(p.getUniqueId().toString());
						object.addProperty("command", args[1]);
						p.sendMessage("add a world");
					} else {
						String command = args[1].substring(0, 1).toUpperCase() + args[1].substring(1).toLowerCase();
						StringBuilder msg = new StringBuilder(args[2]);
						for (int i = 3; i < args.length; i++) {
							msg.append(" ").append(args[i]);
						}
						if (BlockedOpCommands.addBlockedCommand(command, msg.toString(), null, null, null)) {
							sender.sendMessage("Added /" + command + " with the message " + ChatColor.translateAlternateColorCodes('&', msg.toString()));
							if (sender instanceof ConsoleCommandSender) {
								Log.addLog(Universal.get().getMethods(), "CONSOLE: Added /" + command + " to opblock.yml with message " + msg);
							} else {
								Log.addLog(Universal.get().getMethods(), sender.getName() + ": Added /" + command + " to opblock.yml with message " + msg);
							}
							
						} else {
							sender.sendMessage("could not add /" + command + " to the op blocked list");
						}
					}
				}
				return true;
			}
			
			// RemoveOp argument
			else if (args[0].equalsIgnoreCase("removeop")) {
				
				// Player doesn't have permissions to do /cb removeop
				if (!sender.hasPermission("cb.removeop") && (sender instanceof Player)) {
					noPermissions(sender);
					return true;
				}
				
				if (args.length == 1) {
					if (sender instanceof Player) {
						Player p = (Player) sender;
						CommandValueListener.lookingFor.put(p.getUniqueId().toString(), "removeop");
						CommandValueListener.partsHad.put(p.getUniqueId().toString(), new JsonObject());
						/*
						 * Send message to input a command
						 */
						p.sendMessage("add an op command to unblock");
					} else {
						sender.sendMessage("arguments: /cb removeop 'command'");
					}
					
				} else {
					StringBuilder command = new StringBuilder(args[1]);
					for (int i = 2; i < args.length; i++) {
						command.append(" ").append(args[i]);
					}
					if (BlockedOpCommands.removeBlockedCommand(command.toString())) {
						sender.sendMessage("removed " + command + " from the op blocked list");
						if (sender instanceof ConsoleCommandSender) {
							Log.addLog(Universal.get().getMethods(), "CONSOLE: Removed /" + command + " from opblock.yml");
						} else {
							Log.addLog(Universal.get().getMethods(), sender.getName() + ": Removed /" + command + " from opblock.yml");
						}
					} else {
						sender.sendMessage("could not remove " + command + " from the op blocked list");
					}
				}
				return true;
				// EditOp argument
			} else if (args[0].equalsIgnoreCase("edit")) {
				if (!(sender instanceof Player)) {
					sender.sendMessage("need to be player");
					return true;
				}
				// Player doesn't have permissions to do /cb edit
				if (!sender.hasPermission("cb.edit")) {
					noPermissions(sender);
					return true;
				}
				if (args.length >= 2) {
					StringBuilder newArgs = new StringBuilder();
					for (int i = 1; i <= args.length - 1; i++) {
						newArgs.append(args[i]);
						if (args.length - 1 > i) {
							newArgs.append(" ");
						}
					}
					newArgs =
							new StringBuilder(newArgs.substring(0, 1).toUpperCase() + newArgs.substring(1).toLowerCase());
					if (!BukkitMain.oldConfig()) {
						if (ConfigManager.MainDisabled.contains("DisabledCommands." + newArgs)) {
							newArgs =
									new StringBuilder(newArgs.substring(0, 1).toUpperCase() + newArgs.substring(1).toLowerCase());
						} else if (ConfigManager.MainDisabled.contains("DisabledCommands." + newArgs.toString().toLowerCase())) {
							newArgs = new StringBuilder(newArgs.toString().toLowerCase());
						} else {
							sender.sendMessage("/" + newArgs + " is not a blocked command.");
							return true;
						}
					} else {
						if (OldConfigManager.MainDisabled.contains("DisabledCommands." + newArgs)) {
							newArgs = new StringBuilder(newArgs.substring(0, 1).toUpperCase() + newArgs.substring(1).toLowerCase());
						} else if (OldConfigManager.MainDisabled.contains("DisabledCommands." + newArgs.toString().toLowerCase())) {
							newArgs = new StringBuilder(newArgs.toString().toLowerCase());
						} else {
							sender.sendMessage("/" + newArgs + " is not a blocked command.");
							return true;
						}
					}
					Player p = (Player) sender;
					/*
					 * Open gui
					 */
					new DisabledGui().openGui(p, newArgs.toString());
					return true;
				}
				sender.sendMessage("need the command to edit.");
			} else if (args[0].equalsIgnoreCase("editop")) {
				if (!(sender instanceof Player)) {
					sender.sendMessage("need to be player");
					return true;
				}
				// Player doesn't have permissions to do /cb editop
				if (!sender.hasPermission("cb.editop")) {
					noPermissions(sender);
					return true;
				}
				if (args.length >= 2) {
					StringBuilder newArgs = new StringBuilder();
					for (int i = 1; i <= args.length - 1; i++) {
						newArgs.append(args[i]);
						if (args.length - 1 > i) {
							newArgs.append(" ");
						}
					}
					newArgs =
							new StringBuilder(newArgs.substring(0, 1).toUpperCase() + newArgs.substring(1).toLowerCase());
					if (!BukkitMain.oldConfig()) {
						if (ConfigManager.OpDisabled.contains("DisabledOpCommands." + newArgs)) {
							newArgs =
									new StringBuilder(newArgs.substring(0, 1).toUpperCase() + newArgs.substring(1).toLowerCase());
						} else if (ConfigManager.OpDisabled.contains("DisabledOpCommands." + newArgs.toString().toLowerCase())) {
							newArgs = new StringBuilder(newArgs.toString().toLowerCase());
						} else {
							sender.sendMessage("/" + newArgs + " is not a blocked command.");
							return true;
						}
					} else {
						if (OldConfigManager.OpDisabled.contains("DisabledOpCommands." + newArgs)) {
							newArgs = new StringBuilder(newArgs.substring(0, 1).toUpperCase() + newArgs.substring(1).toLowerCase());
						} else if (OldConfigManager.OpDisabled.contains("DisabledOpCommands." + newArgs.toString().toLowerCase())) {
							newArgs = new StringBuilder(newArgs.toString().toLowerCase());
						} else {
							sender.sendMessage("/" + newArgs + " is not a blocked command.");
							return true;
						}
					}
					Player p = (Player) sender;
					/*
					 * Open gui
					 */
					new OpDisabledGui().openOpGui(p, newArgs.toString());
					return true;
				}
				sender.sendMessage("need the command to edit.");
			}
			
			// Sent an argument that isnt one of the listed
			else {
				sender.sendMessage("not right arguments");
			}
			return true;
		}
		return false;
	}

	public static void noPermissions(CommandSender sender) {
		if (sender instanceof Player) {
			if (!BukkitMain.oldConfig()) {
				String papiMsg = BukkitMain.isPapiEnabled() ? PlaceholderAPI.setPlaceholders((Player) sender, 
						Variables.translateVariables(ConfigManager.getConfig().getString("Messages.NoPermission"), (Player) sender)) :
							Variables.translateVariables(ConfigManager.getConfig().getString("Messages.NoPermission"), (Player) sender);
				String noPerms = ChatColor.translateAlternateColorCodes('&', papiMsg);
				sender.sendMessage(noPerms);
			} else {
				String papiMsg = BukkitMain.isPapiEnabled() ? PlaceholderAPI.setPlaceholders((Player) sender, 
						Variables.translateVariables(OldConfigManager.getConfig().getString("Messages.NoPermission"), (Player) sender)) :
							Variables.translateVariables(OldConfigManager.getConfig().getString("Messages.NoPermission"), (Player) sender);
				String noPerms = ChatColor.translateAlternateColorCodes('&', papiMsg);
				sender.sendMessage(noPerms);
			}
			return;
		}
		if (!BukkitMain.oldConfig()) {
			String msg = ConfigManager.getConfig().getString("Messages.NoPermission");
			assert msg != null;
			String noPerms = ChatColor.translateAlternateColorCodes('&', msg);
			sender.sendMessage(noPerms);
		} else {
			String msg = OldConfigManager.getConfig().getString("Messages.NoPermission");
			assert msg != null;
			String noPerms = ChatColor.translateAlternateColorCodes('&', msg);
			sender.sendMessage(noPerms);
		}
	}
	
	private void noArgs(CommandSender sender) {
		sender.sendMessage(ChatColor.BLUE + "-=====[" + ChatColor.AQUA + ChatColor.BOLD + " Command Blocker " + ChatColor.BLUE + "]=====-");
		sender.sendMessage(ChatColor.RED + "You have not supplied enough arguments.");
		sender.sendMessage(ChatColor.RED + "Valid arguments are: add, remove, reload, addop, removeop, edit, editop.");
		sender.sendMessage(ChatColor.GREEN + "Made by TreyRuffy!");
		sender.sendMessage(ChatColor.BLUE + "-======================-");
	}
	
}
