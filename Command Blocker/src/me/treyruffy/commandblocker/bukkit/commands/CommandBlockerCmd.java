package me.treyruffy.commandblocker.bukkit.commands;

import me.clip.placeholderapi.PlaceholderAPI;
import me.treyruffy.commandblocker.bukkit.PlaceholderAPITest;
import me.treyruffy.commandblocker.bukkit.config.Messages;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import com.google.gson.JsonObject;

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
			if (!(sender.hasPermission("cb.add") || sender.hasPermission("cb.reload") || sender.hasPermission("cb" +
					".remove") || sender.hasPermission("cb.edit") || sender.hasPermission("cb.editop") || sender.hasPermission("cb.addop") || sender.hasPermission("cb.removeop")) && (sender instanceof Player)) {
				noPermissions(sender);
				return true;
			}

			// No arguments
			if (args.length == 0) {
				for (String message : Messages.getMessages("Main", "NoArguments")) {
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
				}
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
						for (String message : Messages.getMessages("Main", "AddCommandToBlock")) {
							p.sendMessage(PlaceholderAPITest.testforPAPI(p, ChatColor.translateAlternateColorCodes('&'
									, message)));
						}
					} else {
						for (String message : Messages.getMessages("Main", "AddArguments")) {
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
						}
					}
				} else if (args.length == 2) {
					if (sender instanceof Player) {
						Player p = (Player) sender;
						CommandValueListener.lookingFor.put(p.getUniqueId().toString(), "add");
						CommandValueListener.partsHad.put(p.getUniqueId().toString(), new JsonObject());

						JsonObject object = CommandValueListener.partsHad.get(p.getUniqueId().toString());
						object.addProperty("command", args[1]);
						CommandValueListener.partsHad.put(p.getUniqueId().toString(), object);

						for (String message : Messages.getMessages("Main", "AddPermission")) {
							p.sendMessage(PlaceholderAPITest.testforPAPI(p, ChatColor.translateAlternateColorCodes('&'
									, message)));
						}
					} else {
						for (String message : Messages.getMessages("Main", "AddArguments")) {
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
						}
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

						for (String message : Messages.getMessages("Main", "AddMessage")) {
							p.sendMessage(PlaceholderAPITest.testforPAPI(p, ChatColor.translateAlternateColorCodes('&'
									, message)));
						}
					} else {
						for (String message : Messages.getMessages("Main", "AddArguments")) {
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
						}
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
						object.addProperty("message", msg.toString());
						CommandValueListener.partsHad.put(p.getUniqueId().toString(), object);

						for (String message : Messages.getMessages("Main", "AddWorld")) {
							p.sendMessage(PlaceholderAPITest.testforPAPI(p, ChatColor.translateAlternateColorCodes('&'
									, message)));
						}
					} else {
						String command = args[1].substring(0, 1).toUpperCase() + args[1].substring(1).toLowerCase();
						StringBuilder msg = new StringBuilder(args[3]);
						for (int i = 4; i < args.length - 1; i++) {
							msg.append(" ").append(args[i]);
						}
						if (BlockedCommands.addBlockedCommand(command, args[2], msg.toString(), null, null, null)) {
							for (String message : Messages.getMessages("Main", "AddCommandToConfig")) {
								sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message.replace("%c",
										command).replace("%p", args[2]).replace("%m", msg)));
							}
							if (sender instanceof ConsoleCommandSender) {
								Log.addLog(Universal.get().getMethods(),
										"CONSOLE: Added /" + command + " to disabled" + ".yml with permission " + args[2] + " and message " + msg);
							} else {
								Log.addLog(Universal.get().getMethods(), sender.getName() + ": Added /" + command + " "
										+ "to disabled.yml with permission " + args[2] + " and message " + msg);
							}
						} else {
							for (String message : Messages.getMessages("Main", "CouldNotAddCommandToConfig")) {
								sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message.replace("%c",
										command)));
							}
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

				for (String message : Messages.getMessages("Main", "ReloadCommand")) {
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
				}
				try {
					if (!BukkitMain.oldConfig()) {
						ConfigManager.reloadConfig();
						ConfigManager.reloadDisabled();
						ConfigManager.reloadOpDisabled();
						ConfigManager.reloadMessages();
					} else {
						OldConfigManager.reloadConfig();
						OldConfigManager.reloadDisabled();
						OldConfigManager.reloadOpDisabled();
						OldConfigManager.reloadMessages();
					}
					BukkitMain.fixCommands();
					for (String message : Messages.getMessages("Main", "ReloadSuccessful")) {
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
					}
				} catch (Exception e) {
					for (String message : Messages.getMessages("Main", "ReloadFailed")) {
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
					}

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
						for (String message : Messages.getMessages("Main", "RemoveCommandFromBlocklist")) {
							p.sendMessage(PlaceholderAPITest.testforPAPI(p, ChatColor.translateAlternateColorCodes('&'
									, message)));
						}
					} else {
						for (String message : Messages.getMessages("Main", "RemoveArguments")) {
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
						}
					}
				} else {
					StringBuilder command = new StringBuilder(args[1]);
					for (int i = 2; i < args.length; i++) {
						command.append(" ").append(args[i]);
					}
					if (BlockedCommands.removeBlockedCommand(command.toString())) {
						for (String message : Messages.getMessages("Main", "RemovedCommand")) {
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message.replace("%c",
									command)));
						}
						if (sender instanceof ConsoleCommandSender) {
							Log.addLog(Universal.get().getMethods(),
									"CONSOLE: Removed /" + command + " from disabled" + ".yml");
						} else {
							Log.addLog(Universal.get().getMethods(),
									sender.getName() + ": Removed /" + command + " " + "from disabled.yml");
						}
					} else {
						for (String message : Messages.getMessages("Main", "UnblockCancelledBecauseNotBlocked")) {
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message.replace("%c",
									command)));
						}
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
						for (String message : Messages.getMessages("Main", "AddOpAddCommand")) {
							p.sendMessage(PlaceholderAPITest.testforPAPI(p, ChatColor.translateAlternateColorCodes('&'
									, message)));
						}
					} else {
						for (String message : Messages.getMessages("Main", "AddOpArguments")) {
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
						}
					}
					
				} else if (args.length == 2) {
					if (sender instanceof Player) {
						Player p = (Player) sender;
						CommandValueListener.lookingFor.put(p.getUniqueId().toString(), "addop");
						CommandValueListener.partsHad.put(p.getUniqueId().toString(), new JsonObject());

						JsonObject object = CommandValueListener.partsHad.get(p.getUniqueId().toString());
						object.addProperty("command", args[1]);
						for (String message : Messages.getMessages("Main", "AddOpAddMessage")) {
							p.sendMessage(PlaceholderAPITest.testforPAPI(p, ChatColor.translateAlternateColorCodes('&'
									, message)));
						}
					} else {
						for (String message : Messages.getMessages("Main", "AddOpArguments")) {
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
						}
					}
				} else {
					if (sender instanceof Player) {
						Player p = (Player) sender;
						CommandValueListener.lookingFor.put(p.getUniqueId().toString(), "addop");
						CommandValueListener.partsHad.put(p.getUniqueId().toString(), new JsonObject());

						JsonObject object = CommandValueListener.partsHad.get(p.getUniqueId().toString());
						object.addProperty("command", args[1]);
						StringBuilder msg = new StringBuilder(args[2]);
						for (int i = 3; i < args.length; i++) {
							msg.append(" ").append(args[i]);
						}
						object.addProperty("message", msg.toString());
						for (String message : Messages.getMessages("Main", "AddOpAddWorld")) {
							p.sendMessage(PlaceholderAPITest.testforPAPI(p, ChatColor.translateAlternateColorCodes('&'
									, message)));
						}
					} else {
						String command = args[1].substring(0, 1).toUpperCase() + args[1].substring(1).toLowerCase();
						StringBuilder msg = new StringBuilder(args[2]);
						for (int i = 3; i < args.length; i++) {
							msg.append(" ").append(args[i]);
						}
						if (BlockedOpCommands.addBlockedCommand(command, msg.toString(), null, null, null)) {
							for (String message : Messages.getMessages("Main", "AddOpAddedCommandToConfig")) {
								sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message.replace("%c",
										command).replace("%m", msg)));
							}
							if (sender instanceof ConsoleCommandSender) {
								Log.addLog(Universal.get().getMethods(),
										"CONSOLE: Added /" + command + " to opblock" + ".yml with message " + msg);
							} else {
								Log.addLog(Universal.get().getMethods(), sender.getName() + ": Added /" + command + " "
										+ "to opblock.yml with message " + msg);
							}

						} else {
							for (String message : Messages.getMessages("Main", "AddOpCouldNotAddCommand")) {
								sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message.replace("%c",
										command)));
							}
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
						for (String message : Messages.getMessages("Main", "RemoveOpRemoveCommand")) {
							p.sendMessage(PlaceholderAPITest.testforPAPI(p, ChatColor.translateAlternateColorCodes('&'
									, message)));
						}
					} else {
						for (String message : Messages.getMessages("Main", "RemoveOpArguments")) {
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
						}
					}
					
				} else {
					StringBuilder command = new StringBuilder(args[1]);
					for (int i = 2; i < args.length; i++) {
						command.append(" ").append(args[i]);
					}
					if (BlockedOpCommands.removeBlockedCommand(command.toString())) {
						for (String message : Messages.getMessages("Main", "RemoveOpRemovedCommand")) {
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message.replace("%c",
									command)));
						}
						if (sender instanceof ConsoleCommandSender) {
							Log.addLog(Universal.get().getMethods(),
									"CONSOLE: Removed /" + command + " from opblock" + ".yml");
						} else {
							Log.addLog(Universal.get().getMethods(),
									sender.getName() + ": Removed /" + command + " " + "from opblock.yml");
						}
					} else {
						for (String message : Messages.getMessages("Main", "RemoveOpCouldNotRemove")) {
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message.replace("%c",
									command)));
						}
					}
				}
				return true;
				// EditOp argument
			} else if (args[0].equalsIgnoreCase("edit")) {
				if (!(sender instanceof Player)) {
					for (String message : Messages.getMessages("Main", "EditNeedToBePlayer")) {
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
					}
					return true;
				}
				// Player doesn't have permissions to do /cb edit
				Player p = (Player) sender;
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
							for (String message : Messages.getMessages("Main", "EditNotBlockedCommand")) {
								p.sendMessage(PlaceholderAPITest.testforPAPI(p,
										ChatColor.translateAlternateColorCodes('&', message)).replace("%c", newArgs));
							}
							return true;
						}
					} else {
						if (OldConfigManager.MainDisabled.contains("DisabledCommands." + newArgs)) {
							newArgs = new StringBuilder(newArgs.substring(0, 1).toUpperCase() + newArgs.substring(1).toLowerCase());
						} else if (OldConfigManager.MainDisabled.contains("DisabledCommands." + newArgs.toString().toLowerCase())) {
							newArgs = new StringBuilder(newArgs.toString().toLowerCase());
						} else {
							for (String message : Messages.getMessages("Main", "EditNotBlockedCommand")) {
								p.sendMessage(PlaceholderAPITest.testforPAPI(p,
										ChatColor.translateAlternateColorCodes('&', message)).replace("%c", newArgs));
							}
							return true;
						}
					}
					/*
					 * Open gui
					 */
					new DisabledGui().openGui(p, newArgs.toString());
					return true;
				}
				for (String message : Messages.getMessages("Main", "EditNeedCommandToEdit")) {
					p.sendMessage(PlaceholderAPITest.testforPAPI(p, ChatColor.translateAlternateColorCodes('&',
							message)));
				}
			} else if (args[0].equalsIgnoreCase("editop")) {
				if (!(sender instanceof Player)) {
					for (String message : Messages.getMessages("Main", "EditOpNeedToBePlayer")) {
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
					}
					return true;
				}
				Player p = (Player) sender;
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
							for (String message : Messages.getMessages("Main", "EditOpNotBlockedCommand")) {
								p.sendMessage(PlaceholderAPITest.testforPAPI(p,
										ChatColor.translateAlternateColorCodes('&', message)).replace("%c", newArgs));
							}
							return true;
						}
					} else {
						if (OldConfigManager.OpDisabled.contains("DisabledOpCommands." + newArgs)) {
							newArgs = new StringBuilder(newArgs.substring(0, 1).toUpperCase() + newArgs.substring(1).toLowerCase());
						} else if (OldConfigManager.OpDisabled.contains("DisabledOpCommands." + newArgs.toString().toLowerCase())) {
							newArgs = new StringBuilder(newArgs.toString().toLowerCase());
						} else {
							for (String message : Messages.getMessages("Main", "EditOpNotBlockedCommand")) {
								p.sendMessage(PlaceholderAPITest.testforPAPI(p,
										ChatColor.translateAlternateColorCodes('&', message)).replace("%c", newArgs));
							}
							return true;
						}
					}
					/*
					 * Open gui
					 */
					new OpDisabledGui().openOpGui(p, newArgs.toString());
					return true;
				}
				for (String message : Messages.getMessages("Main", "EditOpNeedCommandToEdit")) {
					p.sendMessage(PlaceholderAPITest.testforPAPI(p, ChatColor.translateAlternateColorCodes('&',
							message)));
				}
			}
			
			// Sent an argument that isnt one of the listed
			else {
				for (String message : Messages.getMessages("Main", "NoArguments")) {
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
				}
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
						Variables.translateVariables(OldConfigManager.getConfig().getString("Messages.NoPermission"),
								(Player) sender)) :
						Variables.translateVariables(OldConfigManager.getConfig().getString("Messages.NoPermission"),
								(Player) sender);
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
	
}
