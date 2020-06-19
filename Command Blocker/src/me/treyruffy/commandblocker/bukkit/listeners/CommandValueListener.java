package me.treyruffy.commandblocker.bukkit.listeners;

import java.util.*;

import me.treyruffy.commandblocker.bukkit.PlaceholderAPITest;
import me.treyruffy.commandblocker.bukkit.api.BlockedOpCommands;
import me.treyruffy.commandblocker.bukkit.config.Messages;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import me.treyruffy.commandblocker.Log;
import me.treyruffy.commandblocker.Universal;
import me.treyruffy.commandblocker.bukkit.BukkitMain;
import me.treyruffy.commandblocker.bukkit.api.BlockedCommands;
import me.treyruffy.commandblocker.bukkit.commands.CommandBlockerCmd;
import me.treyruffy.commandblocker.bukkit.config.ConfigManager;
import me.treyruffy.commandblocker.json.AddCommand;
import me.treyruffy.commandblocker.json.AddOpCommand;
import me.treyruffy.commandblocker.json.RemoveCommand;
import me.treyruffy.commandblocker.json.RemoveOpCommand;
import me.treyruffy.commandblockerlegacy.OldConfigManager;

public class CommandValueListener implements Listener {

	public static HashMap<String, String> lookingFor = new HashMap<>();
	public static HashMap<String, JsonObject> partsHad = new HashMap<>();
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void readChat(AsyncPlayerChatEvent e) {
		Player p = e.getPlayer();
		if (!(p.hasPermission("cb.add") || p.hasPermission("cb.remove") || p.hasPermission("cb.addop") || p.hasPermission("cb.removeop"))) {
			return;
		}
		
		if (!lookingFor.containsKey(p.getUniqueId().toString())) {
			return;
		}
		
		if (e.getMessage().equalsIgnoreCase("cancel")) {
			lookingFor.remove(p.getUniqueId().toString());
			partsHad.remove(p.getUniqueId().toString());
			for (String message : Messages.getMessages("Main", "Cancelled")) {
				p.sendMessage(PlaceholderAPITest.testforPAPI(p, ChatColor.translateAlternateColorCodes('&', message)));
			}
			/*
			 * Send message that the event was cancelled
			 */
			e.setCancelled(true);
			return;
		}

		if (lookingFor.get(p.getUniqueId().toString()).equalsIgnoreCase("add")) {
			
			if (!p.hasPermission("cb.add")) {
				CommandBlockerCmd.noPermissions(p);
				return;
			}
			
			if (!partsHad.get(p.getUniqueId().toString()).has("command")) {
				FileConfiguration disabled = BukkitMain.oldConfig() ? OldConfigManager.getDisabled() : ConfigManager.getDisabled();
				String message = e.getMessage().substring(0, 1).toUpperCase() + e.getMessage().substring(1).toLowerCase();
				if (disabled.contains("DisabledCommands." + message)) {
					for (String msg : Messages.getMessages("Main", "CommandAlreadyBlocked")) {
						p.sendMessage(PlaceholderAPITest.testforPAPI(p, ChatColor.translateAlternateColorCodes('&',
								msg.replace("%c", e.getMessage()))));
					}
					lookingFor.remove(p.getUniqueId().toString());
					partsHad.remove(p.getUniqueId().toString());
					e.setCancelled(true);
					return;
				}
				JsonObject object = partsHad.get(p.getUniqueId().toString());
				object.addProperty("command", e.getMessage());
				partsHad.put(p.getUniqueId().toString(), object);

				for (String msg : Messages.getMessages("Main", "AddPermission")) {
					p.sendMessage(PlaceholderAPITest.testforPAPI(p, ChatColor.translateAlternateColorCodes('&', msg)));
				}
				/*
				 * Send message to add a permission
				 */

			} else if (!partsHad.get(p.getUniqueId().toString()).has("permission")) {
				JsonObject object = partsHad.get(p.getUniqueId().toString());
				object.addProperty("permission", e.getMessage());
				partsHad.put(p.getUniqueId().toString(), object);

				for (String msg : Messages.getMessages("Main", "AddMessage")) {
					p.sendMessage(PlaceholderAPITest.testforPAPI(p, ChatColor.translateAlternateColorCodes('&', msg)));
				}
				/*
				 * Send message to add a message
				 */

			} else if (!partsHad.get(p.getUniqueId().toString()).has("message")) {
				JsonObject object = partsHad.get(p.getUniqueId().toString());
				object.addProperty("message", e.getMessage());
				partsHad.put(p.getUniqueId().toString(), object);

				for (String msg : Messages.getMessages("Main", "AddWorld")) {
					p.sendMessage(PlaceholderAPITest.testforPAPI(p, ChatColor.translateAlternateColorCodes('&', msg)));
				}
				/*
				 * Send message to add worlds. Allow for all.
				 */

			} else if (!partsHad.get(p.getUniqueId().toString()).has("worlds")) {
				JsonObject object = partsHad.get(p.getUniqueId().toString());
				object.addProperty("worlds", e.getMessage());
				partsHad.put(p.getUniqueId().toString(), object);

				for (String msg : Messages.getMessages("Main", "AddPlayerCommand")) {
					p.sendMessage(PlaceholderAPITest.testforPAPI(p, ChatColor.translateAlternateColorCodes('&', msg)));
				}
				/*
				 * Send message to add player commands. Allow for none.
				 */

			} else if (!partsHad.get(p.getUniqueId().toString()).has("playercommands")) {
				JsonObject object = partsHad.get(p.getUniqueId().toString());
				object.addProperty("playercommands", e.getMessage());
				partsHad.put(p.getUniqueId().toString(), object);

				for (String msg : Messages.getMessages("Main", "AddConsoleCommand")) {
					p.sendMessage(PlaceholderAPITest.testforPAPI(p, ChatColor.translateAlternateColorCodes('&', msg)));
				}
				/*
				 * Send message to add console commands. Allow for none.
				 */

			} else if (!partsHad.get(p.getUniqueId().toString()).has("consolecommands")) {
				JsonObject object = partsHad.get(p.getUniqueId().toString());
				object.addProperty("consolecommands", e.getMessage());
				partsHad.put(p.getUniqueId().toString(), object);

				for (String msg : Messages.getMessages("Main", "AddConfirmation")) {
					p.sendMessage(PlaceholderAPITest.testforPAPI(p, ChatColor.translateAlternateColorCodes('&', msg)));
				}
				/*
				 * Send message to confirm if the command is right
				 */
			} else if (!partsHad.get(p.getUniqueId().toString()).has("confirmation")) {
				JsonObject object = partsHad.get(p.getUniqueId().toString());
				if (e.getMessage().equalsIgnoreCase("no")) {
					object.addProperty("confirmation", false);
				} else if (e.getMessage().equalsIgnoreCase("yes")) {
					object.addProperty("confirmation", true);
				} else {
					for (String msg : Messages.getMessages("Main", "CanOnlyBeYesOrNo")) {
						p.sendMessage(PlaceholderAPITest.testforPAPI(p, ChatColor.translateAlternateColorCodes('&',
								msg)));
					}
					e.setCancelled(true);
					return;
				}
				
				partsHad.put(p.getUniqueId().toString(), object);
				
				Gson gson = new Gson();
				AddCommand addcommand = gson.fromJson(partsHad.get(p.getUniqueId().toString()), AddCommand.class);
				
				if (!addcommand.getConfirmation()) {
					lookingFor.remove(p.getUniqueId().toString());
					partsHad.remove(p.getUniqueId().toString());
					for (String msg : Messages.getMessages("Main", "Cancelled")) {
						p.sendMessage(PlaceholderAPITest.testforPAPI(p, ChatColor.translateAlternateColorCodes('&',
								msg)));
					}
					e.setCancelled(true);
					return;
				}
				String cmd = addcommand.getCommand().substring(0, 1).toUpperCase() + addcommand.getCommand().substring(1).toLowerCase();
				
				List<String> worlds = new ArrayList<>();
				Collections.addAll(worlds, addcommand.getWorlds().split(","));
				
				List<String> pCmds = new ArrayList<>();
				for (String s : addcommand.getPlayerCommands().split(",")) {
					pCmds.add("/" + s);
				}
				for (String s : pCmds) {
					if (s.startsWith("/")) {
						pCmds.set(pCmds.indexOf(s), s.substring(1));
					}
				}

				List<String> cCmds = new ArrayList<>();
				for (String s : addcommand.getConsoleCommands().split(",")) {
					cCmds.add("/" + s);
				}

				for (String s : cCmds) {
					if (s.startsWith("/")) {
						cCmds.set(cCmds.indexOf(s), s.substring(1));
					}
				}

				if (BlockedCommands.addBlockedCommand(cmd, addcommand.getPermission(), addcommand.getMessage(), worlds
						, pCmds, cCmds)) {
					Log.addLog(Universal.get().getMethods(),
							p.getName() + ": Added command /" + addcommand.getCommand() + " in disabled.yml with " +
									"permission " + addcommand.getPermission() + " and message " + addcommand.getMessage() + " in worlds: " + addcommand.getWorlds() + ". When executed, it runs " + addcommand.getPlayerCommands() + " as the player and " + addcommand.getConsoleCommands() + " as console.");
					for (String msg : Messages.getMessages("Main", "AddedCommandOutput")) {

						String msgToReplace = msg.replace("%c", addcommand.getCommand()).replace("%p",
								addcommand.getPermission()).replace("%m", addcommand.getMessage()).replace("%w",
								addcommand.getWorlds()).replace("%x", addcommand.getPlayerCommands()).replace("%z",
								addcommand.getConsoleCommands()).replace("%y",
								addcommand.getConfirmation().toString());
						p.sendMessage(PlaceholderAPITest.testforPAPI(p, ChatColor.translateAlternateColorCodes('&',
								msgToReplace)));
					}
				} else {
					for (String msg : Messages.getMessages("Main", "CouldNotAddCommandToConfig")) {
						p.sendMessage(PlaceholderAPITest.testforPAPI(p, ChatColor.translateAlternateColorCodes('&',
								msg)));
					}
				}
				/*
				 * Send message saying if it was completed or not
				 *
				 * Add to disabled here
				 * With partsHad
				 *
				 * Deserialize json and put into disabled.yml
				 */

				lookingFor.remove(p.getUniqueId().toString());
				partsHad.remove(p.getUniqueId().toString());
			}
			e.setCancelled(true);
			return;
		}
		if (lookingFor.get(p.getUniqueId().toString()).equalsIgnoreCase("remove")) {
			if (!p.hasPermission("cb.remove")) {
				CommandBlockerCmd.noPermissions(p);
				return;
			}
			
			if (!partsHad.get(p.getUniqueId().toString()).has("command")) {
				FileConfiguration disabled = BukkitMain.oldConfig() ? OldConfigManager.getDisabled() : ConfigManager.getDisabled();
				String message = e.getMessage().substring(0, 1).toUpperCase() + e.getMessage().substring(1).toLowerCase();
				if (!disabled.contains("DisabledCommands." + message)) {
					for (String msg : Messages.getMessages("Main", "UnblockCancelledBecauseNotBlocked")) {
						p.sendMessage(PlaceholderAPITest.testforPAPI(p, ChatColor.translateAlternateColorCodes('&',
								msg.replace("%c", e.getMessage()))));
					}
					lookingFor.remove(p.getUniqueId().toString());
					partsHad.remove(p.getUniqueId().toString());
					e.setCancelled(true);
					return;
				}
				JsonObject object = partsHad.get(p.getUniqueId().toString());
				object.addProperty("command", message);
				partsHad.put(p.getUniqueId().toString(), object);

				for (String msg : Messages.getMessages("Main", "RemoveCommandQuestion")) {
					p.sendMessage(PlaceholderAPITest.testforPAPI(p, ChatColor.translateAlternateColorCodes('&',
							msg.replace("%c", e.getMessage()))));
				}
				e.setCancelled(true);
				/*
				 * Send message to confirm
				 */
			} else if (!partsHad.get(p.getUniqueId().toString()).has("confirmation")) {
				JsonObject object = partsHad.get(p.getUniqueId().toString());
				/*
				 * For the confirmation, make a config option for yes or no options (different languages?)
				 */
				if (e.getMessage().equalsIgnoreCase("no")) {
					object.addProperty("confirmation", false);
				} else if (e.getMessage().equalsIgnoreCase("yes")) {
					object.addProperty("confirmation", true);
				} else {
					for (String msg : Messages.getMessages("Main", "CanOnlyBeYesOrNo")) {
						p.sendMessage(PlaceholderAPITest.testforPAPI(p, ChatColor.translateAlternateColorCodes('&',
								msg)));
					}
					e.setCancelled(true);
					return;
				}
				e.setCancelled(true);
				partsHad.put(p.getUniqueId().toString(), object);
				
				Gson gson = new Gson();
				RemoveCommand removecommand = gson.fromJson(partsHad.get(p.getUniqueId().toString()), RemoveCommand.class);

				if (!removecommand.getConfirmation()) {
					lookingFor.remove(p.getUniqueId().toString());
					partsHad.remove(p.getUniqueId().toString());
					for (String msg : Messages.getMessages("Main", "Cancelled")) {
						p.sendMessage(PlaceholderAPITest.testforPAPI(p, ChatColor.translateAlternateColorCodes('&',
								msg)));
					}
					e.setCancelled(true);
					return;
				}

				if (BlockedCommands.removeBlockedCommand(removecommand.getCommand())) {
					Log.addLog(Universal.get().getMethods(),
							p.getName() + ": Removed command /" + removecommand.getCommand() + " in disabled.yml");

					for (String msg : Messages.getMessages("Main", "RemovedCommandOutput")) {
						p.sendMessage(PlaceholderAPITest.testforPAPI(p, ChatColor.translateAlternateColorCodes('&',
								msg.replace("%c", removecommand.getCommand()).replace("%y",
										removecommand.getConfirmation().toString()))));
					}
				} else {
					for (String msg : Messages.getMessages("Main", "UnblockCancelledBecauseNotBlocked")) {
						p.sendMessage(PlaceholderAPITest.testforPAPI(p, ChatColor.translateAlternateColorCodes('&',
								msg.replace("%c", e.getMessage()))));
					}
				}

				partsHad.remove(p.getUniqueId().toString());
				lookingFor.remove(p.getUniqueId().toString());
			}
			e.setCancelled(true);
			return;
		}
		if (lookingFor.get(p.getUniqueId().toString()).equalsIgnoreCase("addop")) {
			if (!p.hasPermission("cb.addop")) {
				CommandBlockerCmd.noPermissions(p);
				return;
			}
			
			if (!partsHad.get(p.getUniqueId().toString()).has("command")) {
				FileConfiguration disabled = BukkitMain.oldConfig() ? OldConfigManager.getOpDisabled() : ConfigManager.getOpDisabled();
				String message = e.getMessage().substring(0, 1).toUpperCase() + e.getMessage().substring(1).toLowerCase();
				if (disabled.contains("DisabledOpCommands." + message)) {
					for (String msg : Messages.getMessages("Main", "CommandAlreadyBlocked")) {
						p.sendMessage(PlaceholderAPITest.testforPAPI(p, ChatColor.translateAlternateColorCodes('&',
								msg.replace("%c", e.getMessage()))));
					}
					lookingFor.remove(p.getUniqueId().toString());
					partsHad.remove(p.getUniqueId().toString());
					e.setCancelled(true);
					return;
				}
				JsonObject object = partsHad.get(p.getUniqueId().toString());
				object.addProperty("command", e.getMessage());
				partsHad.put(p.getUniqueId().toString(), object);

				for (String msg : Messages.getMessages("Main", "AddOpAddMessage")) {
					p.sendMessage(PlaceholderAPITest.testforPAPI(p, ChatColor.translateAlternateColorCodes('&', msg)));
				}
				/*
				 * Send message to add a message
				 */

			} else if (!partsHad.get(p.getUniqueId().toString()).has("message")) {
				JsonObject object = partsHad.get(p.getUniqueId().toString());
				object.addProperty("message", e.getMessage());
				partsHad.put(p.getUniqueId().toString(), object);

				for (String msg : Messages.getMessages("Main", "AddOpAddWorld")) {
					p.sendMessage(PlaceholderAPITest.testforPAPI(p, ChatColor.translateAlternateColorCodes('&', msg)));
				}
				/*
				 * Send message to add worlds. Allow for all.
				 */

			} else if (!partsHad.get(p.getUniqueId().toString()).has("worlds")) {
				JsonObject object = partsHad.get(p.getUniqueId().toString());
				object.addProperty("worlds", e.getMessage());
				partsHad.put(p.getUniqueId().toString(), object);

				for (String msg : Messages.getMessages("Main", "AddOpAddPlayerCommand")) {
					p.sendMessage(PlaceholderAPITest.testforPAPI(p, ChatColor.translateAlternateColorCodes('&', msg)));
				}
				/*
				 * Send message to add player commands. Allow for none.
				 */

			} else if (!partsHad.get(p.getUniqueId().toString()).has("playercommands")) {
				JsonObject object = partsHad.get(p.getUniqueId().toString());
				object.addProperty("playercommands", e.getMessage());
				partsHad.put(p.getUniqueId().toString(), object);

				for (String msg : Messages.getMessages("Main", "AddOpAddConsoleCommand")) {
					p.sendMessage(PlaceholderAPITest.testforPAPI(p, ChatColor.translateAlternateColorCodes('&', msg)));
				}
				/*
				 * Send message to add console commands. Allow for none.
				 */

			} else if (!partsHad.get(p.getUniqueId().toString()).has("consolecommands")) {
				JsonObject object = partsHad.get(p.getUniqueId().toString());
				object.addProperty("consolecommands", e.getMessage());
				partsHad.put(p.getUniqueId().toString(), object);

				for (String msg : Messages.getMessages("Main", "AddOpConfirmation")) {
					p.sendMessage(PlaceholderAPITest.testforPAPI(p, ChatColor.translateAlternateColorCodes('&', msg)));
				}
				/*
				 * Send message to confirm if the command is right
				 */
			} else if (!partsHad.get(p.getUniqueId().toString()).has("confirmation")) {
				JsonObject object = partsHad.get(p.getUniqueId().toString());
				if (e.getMessage().equalsIgnoreCase("no")) {
					object.addProperty("confirmation", false);
				} else if (e.getMessage().equalsIgnoreCase("yes")) {
					object.addProperty("confirmation", true);
				} else {
					for (String msg : Messages.getMessages("Main", "CanOnlyBeYesOrNo")) {
						p.sendMessage(PlaceholderAPITest.testforPAPI(p, ChatColor.translateAlternateColorCodes('&',
								msg)));
					}
					e.setCancelled(true);
					return;
				}
				
				partsHad.put(p.getUniqueId().toString(), object);
				
				Gson gson = new Gson();
				AddOpCommand addopcommand = gson.fromJson(partsHad.get(p.getUniqueId().toString()), AddOpCommand.class);

				if (!addopcommand.getConfirmation()) {
					lookingFor.remove(p.getUniqueId().toString());
					partsHad.remove(p.getUniqueId().toString());
					for (String msg : Messages.getMessages("Main", "Cancelled")) {
						p.sendMessage(PlaceholderAPITest.testforPAPI(p, ChatColor.translateAlternateColorCodes('&',
								msg)));
					}
					e.setCancelled(true);
					return;
				}
				String cmd =
						addopcommand.getCommand().substring(0, 1).toUpperCase() + addopcommand.getCommand().substring(1).toLowerCase();

				List<String> worlds = Arrays.asList(addopcommand.getWorlds().split(","));

				List<String> pCmds = Arrays.asList(addopcommand.getPlayerCommands().split(","));
				for (String s : pCmds) {
					if (s.startsWith("/")) {
						pCmds.set(pCmds.indexOf(s), s.substring(1));
					}
				}
				List<String> cCmds = Arrays.asList(addopcommand.getConsoleCommands().split(","));
				for (String s : cCmds) {
					if (s.startsWith("/")) {
						cCmds.set(cCmds.indexOf(s), s.substring(1));
					}
				}

				if (BlockedOpCommands.addBlockedCommand(cmd, addopcommand.getMessage(), worlds, pCmds, cCmds)) {
					Log.addLog(Universal.get().getMethods(),
							p.getName() + ": Added command /" + addopcommand.getCommand() + " in opblock.yml with " +
									"message " + addopcommand.getMessage() + " in worlds: " + addopcommand.getWorlds() + ". When executed, it runs " + addopcommand.getPlayerCommands() + " as the player and " + addopcommand.getConsoleCommands() + " as console.");

					for (String msg : Messages.getMessages("Main", "AddedOpCommandOutput")) {
						String msgToReplace = msg.replace("%c", addopcommand.getCommand()).replace("%m",
								addopcommand.getMessage()).replace("%w", addopcommand.getWorlds()).replace("%x",
								addopcommand.getPlayerCommands()).replace("%z", addopcommand.getConsoleCommands()).replace("%y", addopcommand.getConfirmation().toString());
						p.sendMessage(PlaceholderAPITest.testforPAPI(p, ChatColor.translateAlternateColorCodes('&',
								msgToReplace)));
					}
				} else {
					for (String msg : Messages.getMessages("Main", "CouldNotAddCommandToOpConfig")) {
						p.sendMessage(PlaceholderAPITest.testforPAPI(p, ChatColor.translateAlternateColorCodes('&',
								msg.replace("%c", addopcommand.getCommand()))));
					}
				}
				/*
				 * Send message saying if it was completed or not
				 *
				 * Add to disabled here
				 * With partsHad
				 *
				 * Deserialize json and put into disabled.yml
				 */
				
				lookingFor.remove(p.getUniqueId().toString());
				partsHad.remove(p.getUniqueId().toString());
			}
			e.setCancelled(true);
			return;
		}
		if (lookingFor.get(p.getUniqueId().toString()).equalsIgnoreCase("removeop")) {
			if (!p.hasPermission("cb.removeop")) {
				CommandBlockerCmd.noPermissions(p);
				return;
			}
			
			if (!partsHad.get(p.getUniqueId().toString()).has("command")) {
				FileConfiguration disabled = BukkitMain.oldConfig() ? OldConfigManager.getOpDisabled() : ConfigManager.getOpDisabled();
				String message = e.getMessage().substring(0, 1).toUpperCase() + e.getMessage().substring(1).toLowerCase();
				if (!disabled.contains("DisabledOpCommands." + message)) {
					for (String msg : Messages.getMessages("Main", "RemoveOpCouldNotRemove")) {
						p.sendMessage(PlaceholderAPITest.testforPAPI(p, ChatColor.translateAlternateColorCodes('&',
								msg.replace("%c", e.getMessage()))));
					}
					lookingFor.remove(p.getUniqueId().toString());
					partsHad.remove(p.getUniqueId().toString());
					e.setCancelled(true);
					return;
				}
				JsonObject object = partsHad.get(p.getUniqueId().toString());
				object.addProperty("command", e.getMessage());
				partsHad.put(p.getUniqueId().toString(), object);

				for (String msg : Messages.getMessages("Main", "RemoveOpQuestion")) {
					p.sendMessage(PlaceholderAPITest.testforPAPI(p, ChatColor.translateAlternateColorCodes('&',
							msg.replace("%c", e.getMessage()))));
				}
				/*
				 * Send message to confirm
				 */
			} else if (!partsHad.get(p.getUniqueId().toString()).has("confirmation")) {
				JsonObject object = partsHad.get(p.getUniqueId().toString());
				/*
				 * For the confirmation, make a config option for yes or no options (different languages?)
				 */
				if (e.getMessage().equalsIgnoreCase("no")) {
					object.addProperty("confirmation", false);
				} else if (e.getMessage().equalsIgnoreCase("yes")) {
					object.addProperty("confirmation", true);
				} else {
					for (String msg : Messages.getMessages("Main", "CanOnlyBeYesOrNo")) {
						p.sendMessage(PlaceholderAPITest.testforPAPI(p, ChatColor.translateAlternateColorCodes('&',
								msg)));
					}
					e.setCancelled(true);
					return;
				}
				
				partsHad.put(p.getUniqueId().toString(), object);
				
				Gson gson = new Gson();
				RemoveOpCommand removeopcommand = gson.fromJson(partsHad.get(p.getUniqueId().toString()), RemoveOpCommand.class);

				if (!removeopcommand.getConfirmation()) {
					lookingFor.remove(p.getUniqueId().toString());
					partsHad.remove(p.getUniqueId().toString());
					for (String msg : Messages.getMessages("Main", "Cancelled")) {
						p.sendMessage(PlaceholderAPITest.testforPAPI(p, ChatColor.translateAlternateColorCodes('&',
								msg)));
					}
					e.setCancelled(true);
					return;
				}

				if (BlockedOpCommands.removeBlockedCommand(removeopcommand.getCommand())) {
					Log.addLog(Universal.get().getMethods(),
							p.getName() + ": Removed command /" + removeopcommand.getCommand() + " in opblock.yml");

					for (String msg : Messages.getMessages("Main", "RemovedOpCommandOutput")) {
						p.sendMessage(PlaceholderAPITest.testforPAPI(p, ChatColor.translateAlternateColorCodes('&',
								msg.replace("%c", removeopcommand.getCommand()))));
					}
				} else {
					for (String msg : Messages.getMessages("Main", "RemoveOpCouldNotRemove")) {
						p.sendMessage(PlaceholderAPITest.testforPAPI(p, ChatColor.translateAlternateColorCodes('&',
								msg.replace("%c", removeopcommand.getCommand()).replace("%y",
										removeopcommand.getConfirmation().toString()))));
					}
				}

				partsHad.remove(p.getUniqueId().toString());
				lookingFor.remove(p.getUniqueId().toString());
			}
		}
		
		e.setCancelled(true);
	}
	
	@EventHandler
	public void removeOnLeave(PlayerQuitEvent e) {
		lookingFor.remove(e.getPlayer().getUniqueId().toString());
		partsHad.remove(e.getPlayer().getUniqueId().toString());
	}
	
	@EventHandler (priority = EventPriority.HIGHEST)
	public void readCommandsSent(PlayerCommandPreprocessEvent e) {
		Player p = e.getPlayer();
		if (!(p.hasPermission("cb.add") || p.hasPermission("cb.remove") || p.hasPermission("cb.addop") || p.hasPermission("cb.removeop"))) {
			return;
		}
		if (!lookingFor.containsKey(p.getUniqueId().toString())) {
			return;
		}
		String message = e.getMessage().substring(1);
		Bukkit.getConsoleSender().sendMessage("Trey's Command Blocker: " + p.getName() + " executed /" + message + " but it was not actually executed.");

		String msg = message.substring(0, 1).toUpperCase() + message.substring(1).toLowerCase();
		if (lookingFor.get(p.getUniqueId().toString()).equalsIgnoreCase("add")) {
			
			if (!p.hasPermission("cb.add")) {
				CommandBlockerCmd.noPermissions(p);
				return;
			}
			
			if (!partsHad.get(p.getUniqueId().toString()).has("command")) {
				FileConfiguration disabled = BukkitMain.oldConfig() ? OldConfigManager.getDisabled() :
						ConfigManager.getDisabled();
				if (disabled.contains("DisabledCommands." + msg)) {
					for (String msg2 : Messages.getMessages("Main", "CommandAlreadyBlocked")) {
						p.sendMessage(PlaceholderAPITest.testforPAPI(p, ChatColor.translateAlternateColorCodes('&',
								msg2.replace("%c", message))));
					}
					lookingFor.remove(p.getUniqueId().toString());
					partsHad.remove(p.getUniqueId().toString());
					e.setCancelled(true);
					return;
				}
				JsonObject object = partsHad.get(p.getUniqueId().toString());
				object.addProperty("command", message);
				partsHad.put(p.getUniqueId().toString(), object);

				for (String msg2 : Messages.getMessages("Main", "AddPermission")) {
					p.sendMessage(PlaceholderAPITest.testforPAPI(p, ChatColor.translateAlternateColorCodes('&',
							msg2)));
				}
				/*
				 * Send message to add a permission
				 */

			} else if (!partsHad.get(p.getUniqueId().toString()).has("permission")) {

				/*
				 * Send message that you can't use a command
				 */
				for (String msg2 : Messages.getMessages("Main", "CantUseCommand")) {
					p.sendMessage(PlaceholderAPITest.testforPAPI(p, ChatColor.translateAlternateColorCodes('&',
							msg2)));
				}

				e.setCancelled(true);
				return;
			} else if (!partsHad.get(p.getUniqueId().toString()).has("message")) {

				/*
				 * Send message that you can't use a command
				 */
				for (String msg2 : Messages.getMessages("Main", "CantUseCommand")) {
					p.sendMessage(PlaceholderAPITest.testforPAPI(p, ChatColor.translateAlternateColorCodes('&',
							msg2)));
				}

				e.setCancelled(true);
				return;
			} else if (!partsHad.get(p.getUniqueId().toString()).has("worlds")) {

				/*
				 * Send message that you can't use a command
				 */
				for (String msg2 : Messages.getMessages("Main", "CantUseCommand")) {
					p.sendMessage(PlaceholderAPITest.testforPAPI(p, ChatColor.translateAlternateColorCodes('&',
							msg2)));
				}

				e.setCancelled(true);
				return;
			} else if (!partsHad.get(p.getUniqueId().toString()).has("playercommands")) {
				JsonObject object = partsHad.get(p.getUniqueId().toString());
				object.addProperty("playercommands", message);
				partsHad.put(p.getUniqueId().toString(), object);

				for (String msg2 : Messages.getMessages("Main", "AddConsoleCommand")) {
					p.sendMessage(PlaceholderAPITest.testforPAPI(p, ChatColor.translateAlternateColorCodes('&',
							msg2)));
				}
				/*
				 * Send message to add a console command
				 */
			} else if (!partsHad.get(p.getUniqueId().toString()).has("consolecommands")) {
				JsonObject object = partsHad.get(p.getUniqueId().toString());
				object.addProperty("consolecommands", message);
				partsHad.put(p.getUniqueId().toString(), object);

				for (String msg2 : Messages.getMessages("Main", "Confirmation")) {
					p.sendMessage(PlaceholderAPITest.testforPAPI(p, ChatColor.translateAlternateColorCodes('&',
							msg2)));
				}
				/*
				 * Send message to add a confirmation
				 */
			} else if (!partsHad.get(p.getUniqueId().toString()).has("confirmation")) {
				/*
				 * Send message that you can't use a command
				 */
				for (String msg2 : Messages.getMessages("Main", "CantUseCommand")) {
					p.sendMessage(PlaceholderAPITest.testforPAPI(p, ChatColor.translateAlternateColorCodes('&',
							msg2)));
				}

				e.setCancelled(true);
				return;
			}
		} 
		if (lookingFor.get(p.getUniqueId().toString()).equalsIgnoreCase("remove")) {
			
			if (!p.hasPermission("cb.remove")) {
				CommandBlockerCmd.noPermissions(p);
				return;
			}
			
			if (!partsHad.get(p.getUniqueId().toString()).has("command")) {
				FileConfiguration disabled = BukkitMain.oldConfig() ? OldConfigManager.getDisabled() :
						ConfigManager.getDisabled();
				if (!disabled.contains("DisabledCommands." + msg)) {
					for (String msg2 : Messages.getMessages("Main", "UnblockCancelledBecauseNotBlocked")) {
						p.sendMessage(PlaceholderAPITest.testforPAPI(p, ChatColor.translateAlternateColorCodes('&',
								msg2.replace("%c", message))));
					}
					lookingFor.remove(p.getUniqueId().toString());
					partsHad.remove(p.getUniqueId().toString());
					e.setCancelled(true);
					return;
				}
				JsonObject object = partsHad.get(p.getUniqueId().toString());
				object.addProperty("command", message);
				partsHad.put(p.getUniqueId().toString(), object);

				for (String msg2 : Messages.getMessages("Main", "RemoveCommandQuestion")) {
					p.sendMessage(PlaceholderAPITest.testforPAPI(p,
							ChatColor.translateAlternateColorCodes('&', msg2).replace("%c", message)));
				}


			} else if (!partsHad.get(p.getUniqueId().toString()).has("confirmation")) {
				/*
				 * Send message that you can't use a command
				 */
				for (String msg2 : Messages.getMessages("Main", "CantUseCommand")) {
					p.sendMessage(PlaceholderAPITest.testforPAPI(p, ChatColor.translateAlternateColorCodes('&',
							msg2)));
				}

				e.setCancelled(true);
				return;
			}
		}
		if (lookingFor.get(p.getUniqueId().toString()).equalsIgnoreCase("addop")) {
			
			if (!p.hasPermission("cb.addop")) {
				CommandBlockerCmd.noPermissions(p);
				return;
			}
			
			if (!partsHad.get(p.getUniqueId().toString()).has("command")) {
				FileConfiguration disabled = BukkitMain.oldConfig() ? OldConfigManager.getOpDisabled() :
						ConfigManager.getOpDisabled();
				if (disabled.contains("DisabledOpCommands." + msg)) {
					for (String msg2 : Messages.getMessages("Main", "CommandAlreadyBlocked")) {
						p.sendMessage(PlaceholderAPITest.testforPAPI(p, ChatColor.translateAlternateColorCodes('&',
								msg2.replace("%c", message))));
					}
					lookingFor.remove(p.getUniqueId().toString());
					partsHad.remove(p.getUniqueId().toString());
					e.setCancelled(true);
					return;
				}
				JsonObject object = partsHad.get(p.getUniqueId().toString());
				object.addProperty("command", message);
				partsHad.put(p.getUniqueId().toString(), object);

				for (String msg2 : Messages.getMessages("Main", "AddOpAddMessage")) {
					p.sendMessage(PlaceholderAPITest.testforPAPI(p, ChatColor.translateAlternateColorCodes('&',
							msg2)));
				}
				/*
				 * Send message to add a permission
				 */

			} else if (!partsHad.get(p.getUniqueId().toString()).has("message")) {

				/*
				 * Send message that you can't use a command
				 */
				for (String msg2 : Messages.getMessages("Main", "CantUseCommand")) {
					p.sendMessage(PlaceholderAPITest.testforPAPI(p, ChatColor.translateAlternateColorCodes('&',
							msg2)));
				}

				e.setCancelled(true);
				return;
			} else if (!partsHad.get(p.getUniqueId().toString()).has("worlds")) {

				/*
				 * Send message that you can't use a command
				 */
				for (String msg2 : Messages.getMessages("Main", "CantUseCommand")) {
					p.sendMessage(PlaceholderAPITest.testforPAPI(p, ChatColor.translateAlternateColorCodes('&',
							msg2)));
				}

				e.setCancelled(true);
				return;
			} else if (!partsHad.get(p.getUniqueId().toString()).has("playercommands")) {
				JsonObject object = partsHad.get(p.getUniqueId().toString());
				object.addProperty("playercommands", message);
				partsHad.put(p.getUniqueId().toString(), object);

				for (String msg2 : Messages.getMessages("Main", "AddOpAddConsoleCommand")) {
					p.sendMessage(PlaceholderAPITest.testforPAPI(p, ChatColor.translateAlternateColorCodes('&',
							msg2)));
				}
				/*
				 * Send message to add a permission
				 */
			} else if (!partsHad.get(p.getUniqueId().toString()).has("consolecommands")) {
				JsonObject object = partsHad.get(p.getUniqueId().toString());
				object.addProperty("consolecommands", message);
				partsHad.put(p.getUniqueId().toString(), object);

				for (String msg2 : Messages.getMessages("Main", "Confirmation")) {
					p.sendMessage(PlaceholderAPITest.testforPAPI(p, ChatColor.translateAlternateColorCodes('&',
							msg2)));
				}
				/*
				 * Send message to add a permission
				 */
			} else if (!partsHad.get(p.getUniqueId().toString()).has("confirmation")) {
				/*
				 * Send message that you can't use a command
				 */
				for (String msg2 : Messages.getMessages("Main", "CantUseCommand")) {
					p.sendMessage(PlaceholderAPITest.testforPAPI(p, ChatColor.translateAlternateColorCodes('&',
							msg2)));
				}

				e.setCancelled(true);
				return;
			}
		} 
		if (lookingFor.get(p.getUniqueId().toString()).equalsIgnoreCase("removeop")) {
			
			if (!p.hasPermission("cb.removeop")) {
				CommandBlockerCmd.noPermissions(p);
				return;
			}
			if (!partsHad.get(p.getUniqueId().toString()).has("command")) {
				FileConfiguration disabled = BukkitMain.oldConfig() ? OldConfigManager.getOpDisabled() :
						ConfigManager.getOpDisabled();
				if (!disabled.contains("DisabledOpCommands." + msg)) {
					for (String msg2 : Messages.getMessages("Main", "RemoveOpCouldNotRemove")) {
						p.sendMessage(PlaceholderAPITest.testforPAPI(p, ChatColor.translateAlternateColorCodes('&',
								msg2.replace("%c", message))));
					}
					lookingFor.remove(p.getUniqueId().toString());
					partsHad.remove(p.getUniqueId().toString());
					e.setCancelled(true);
					return;
				}
				JsonObject object = partsHad.get(p.getUniqueId().toString());
				object.addProperty("command", message);
				partsHad.put(p.getUniqueId().toString(), object);

				for (String msg2 : Messages.getMessages("Main", "RemoveOpQuestion")) {
					p.sendMessage(PlaceholderAPITest.testforPAPI(p,
							ChatColor.translateAlternateColorCodes('&', msg2).replace("%c", message)));
				}
				/*
				 * Send message to add a permission
				 */

			} else if (!partsHad.get(p.getUniqueId().toString()).has("confirmation")) {
				/*
				 * Send message that you can't use a command
				 */
				for (String msg2 : Messages.getMessages("Main", "CantUseCommand")) {
					p.sendMessage(PlaceholderAPITest.testforPAPI(p, ChatColor.translateAlternateColorCodes('&',
							msg2)));
				}

				e.setCancelled(true);
				return;
			}
		}
		e.setCancelled(true);
	}
}
