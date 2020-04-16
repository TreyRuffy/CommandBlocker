package me.treyruffy.commandblocker.bukkit.listeners;

import java.util.*;

import org.bukkit.Bukkit;
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
			p.sendMessage("cancelled the command blocker steps");
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
					p.sendMessage("command is already blocked. cancelled.");
					lookingFor.remove(p.getUniqueId().toString());
					partsHad.remove(p.getUniqueId().toString());
					e.setCancelled(true);
					return;
				}
				JsonObject object = partsHad.get(p.getUniqueId().toString());
				object.addProperty("command", e.getMessage());
				partsHad.put(p.getUniqueId().toString(), object);
				
				p.sendMessage("add a permission");
				/*
				 * Send message to add a permission
				 */
				
			} else if (!partsHad.get(p.getUniqueId().toString()).has("permission")) {
				JsonObject object = partsHad.get(p.getUniqueId().toString());
				object.addProperty("permission", e.getMessage());
				partsHad.put(p.getUniqueId().toString(), object);
				
				p.sendMessage("add a message");
				/*
				 * Send message to add a message
				 */
				
			} else if (!partsHad.get(p.getUniqueId().toString()).has("message")) {
				JsonObject object = partsHad.get(p.getUniqueId().toString());
				object.addProperty("message", e.getMessage());
				partsHad.put(p.getUniqueId().toString(), object);
				
				p.sendMessage("add a world");
				/*
				 * Send message to add worlds. Allow for all.
				 */
				
			} else if (!partsHad.get(p.getUniqueId().toString()).has("worlds")) {
				JsonObject object = partsHad.get(p.getUniqueId().toString());
				object.addProperty("worlds", e.getMessage());
				partsHad.put(p.getUniqueId().toString(), object);
				
				p.sendMessage("add a player command");
				/*
				 * Send message to add player commands. Allow for none.
				 */
				
			} else if (!partsHad.get(p.getUniqueId().toString()).has("playercommands")) {
				JsonObject object = partsHad.get(p.getUniqueId().toString());
				object.addProperty("playercommands", e.getMessage());
				partsHad.put(p.getUniqueId().toString(), object);
				
				p.sendMessage("add a console command");
				/*
				 * Send message to add console commands. Allow for none.
				 */
				
			} else if (!partsHad.get(p.getUniqueId().toString()).has("consolecommands")) {
				JsonObject object = partsHad.get(p.getUniqueId().toString());
				object.addProperty("consolecommands", e.getMessage());
				partsHad.put(p.getUniqueId().toString(), object);
				
				p.sendMessage("confirmation: yes or no");
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
					p.sendMessage("can only be yes or no");
					e.setCancelled(true);
					return;
				}
				
				partsHad.put(p.getUniqueId().toString(), object);
				
				Gson gson = new Gson();
				AddCommand addcommand = gson.fromJson(partsHad.get(p.getUniqueId().toString()), AddCommand.class);
				
				if (!addcommand.getConfirmation()) {
					lookingFor.remove(p.getUniqueId().toString());
					partsHad.remove(p.getUniqueId().toString());
					p.sendMessage("cancelled the command blocker steps");
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
				
				Log.addLog(Universal.get().getMethods(), p.getName() + ": Added command /" + addcommand.getCommand() + " in disabled.yml with permission " + addcommand.getPermission() 
					+ " and message " + addcommand.getMessage() + " in worlds: " + addcommand.getWorlds() + ". When executed, it runs " + addcommand.getPlayerCommands() 
					+ " as the player and " + addcommand.getConsoleCommands() + " as console.");
				
				p.sendMessage("Command=" + addcommand.getCommand());
				p.sendMessage("Permission=" + addcommand.getPermission());
				p.sendMessage("Message=" + addcommand.getMessage());
				p.sendMessage("Worlds=" + addcommand.getWorlds());
				p.sendMessage("PlayerCommands=" + addcommand.getPlayerCommands());
				p.sendMessage("ConsoleCommands=" + addcommand.getConsoleCommands());
				p.sendMessage("Confirmation=" + addcommand.getConfirmation());
				
				if (BlockedCommands.addBlockedCommand(cmd, addcommand.getPermission(), addcommand.getMessage(), worlds, pCmds, cCmds)) {
					p.sendMessage("successfully added to the file.");
				} else {
					p.sendMessage("already in the file. edit with /cb edit" + cmd);
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
					p.sendMessage("command is not blocked. cancelled.");
					lookingFor.remove(p.getUniqueId().toString());
					partsHad.remove(p.getUniqueId().toString());
					e.setCancelled(true);
					return;
				}
				JsonObject object = partsHad.get(p.getUniqueId().toString());
				object.addProperty("command", message);
				partsHad.put(p.getUniqueId().toString(), object);
				
				p.sendMessage("do you want to remove /" + e.getMessage() + " from the block list?");
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
					p.sendMessage("can only be yes or no");
					e.setCancelled(true);
					return;
				}
				
				partsHad.put(p.getUniqueId().toString(), object);
				
				Gson gson = new Gson();
				RemoveCommand removecommand = gson.fromJson(partsHad.get(p.getUniqueId().toString()), RemoveCommand.class);
				
				if (!removecommand.getConfirmation()) {
					lookingFor.remove(p.getUniqueId().toString());
					partsHad.remove(p.getUniqueId().toString());
					p.sendMessage("cancelled the command blocker steps");
					e.setCancelled(true);
					return;
				}
				
				Log.addLog(Universal.get().getMethods(), p.getName() + ": Removed command /" + removecommand.getCommand() + " in disabled.yml");
				
				p.sendMessage("Command=" + removecommand.getCommand());
				p.sendMessage("Confirmation=" + removecommand.getConfirmation());
				
				BlockedCommands.removeBlockedCommand(removecommand.getCommand());
					
				partsHad.remove(p.getUniqueId().toString());
				lookingFor.remove(p.getUniqueId().toString());
			}
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
					p.sendMessage("command is already blocked. cancelled.");
					lookingFor.remove(p.getUniqueId().toString());
					partsHad.remove(p.getUniqueId().toString());
					e.setCancelled(true);
					return;
				}
				JsonObject object = partsHad.get(p.getUniqueId().toString());
				object.addProperty("command", e.getMessage());
				partsHad.put(p.getUniqueId().toString(), object);
				
				p.sendMessage("add a message");
				/*
				 * Send message to add a message
				 */
				
			} else if (!partsHad.get(p.getUniqueId().toString()).has("message")) {
				JsonObject object = partsHad.get(p.getUniqueId().toString());
				object.addProperty("message", e.getMessage());
				partsHad.put(p.getUniqueId().toString(), object);
				
				p.sendMessage("add a world");
				/*
				 * Send message to add worlds. Allow for all.
				 */
				
			} else if (!partsHad.get(p.getUniqueId().toString()).has("worlds")) {
				JsonObject object = partsHad.get(p.getUniqueId().toString());
				object.addProperty("worlds", e.getMessage());
				partsHad.put(p.getUniqueId().toString(), object);
				
				p.sendMessage("add a player command");
				/*
				 * Send message to add player commands. Allow for none.
				 */
				
			} else if (!partsHad.get(p.getUniqueId().toString()).has("playercommands")) {
				JsonObject object = partsHad.get(p.getUniqueId().toString());
				object.addProperty("playercommands", e.getMessage());
				partsHad.put(p.getUniqueId().toString(), object);
				
				p.sendMessage("add a console command");
				/*
				 * Send message to add console commands. Allow for none.
				 */
				
			} else if (!partsHad.get(p.getUniqueId().toString()).has("consolecommands")) {
				JsonObject object = partsHad.get(p.getUniqueId().toString());
				object.addProperty("consolecommands", e.getMessage());
				partsHad.put(p.getUniqueId().toString(), object);
				
				p.sendMessage("confirmation: yes or no");
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
					p.sendMessage("can only be yes or no");
					e.setCancelled(true);
					return;
				}
				
				partsHad.put(p.getUniqueId().toString(), object);
				
				Gson gson = new Gson();
				AddOpCommand addopcommand = gson.fromJson(partsHad.get(p.getUniqueId().toString()), AddOpCommand.class);
				
				if (!addopcommand.getConfirmation()) {
					lookingFor.remove(p.getUniqueId().toString());
					partsHad.remove(p.getUniqueId().toString());
					p.sendMessage("cancelled the command blocker steps");
					e.setCancelled(true);
					return;
				}
				FileConfiguration opDisabled = BukkitMain.oldConfig() ? OldConfigManager.getOpDisabled() : ConfigManager.getOpDisabled();
				String cmd = addopcommand.getCommand().substring(0, 1).toUpperCase() + addopcommand.getCommand().substring(1).toLowerCase();
				
				opDisabled.set("DisabledOpCommands." + cmd + ".Message", addopcommand.getMessage());
				
				List<String> worlds = Arrays.asList(addopcommand.getWorlds().split(","));
				opDisabled.set("DisabledOpCommands." + cmd + ".Worlds", worlds);
				
				List<String> pCmds = Arrays.asList(addopcommand.getPlayerCommands().split(","));
				for (String s : pCmds) {
					if (s.startsWith("/")) {
						pCmds.set(pCmds.indexOf(s), s.substring(1));
					}
				}
				opDisabled.set("DisabledOpCommands." + cmd + ".PlayerCommands", pCmds);
				
				List<String> cCmds = Arrays.asList(addopcommand.getConsoleCommands().split(","));
				for (String s : cCmds) {
					if (s.startsWith("/")) {
						cCmds.set(cCmds.indexOf(s), s.substring(1));
					}
				}
				opDisabled.set("DisabledOpCommands." + cmd + ".ConsoleCommands", cCmds);
				
				if (!BukkitMain.oldConfig()) {
					ConfigManager.saveOpDisabled();
				} else {
					OldConfigManager.saveOpDisabled();
				}
				
				Log.addLog(Universal.get().getMethods(), p.getName() + ": Added command /" + addopcommand.getCommand() + " in opblock.yml with message " + addopcommand.getMessage()
				+ " in worlds: " + addopcommand.getWorlds() + ". When executed, it runs " + addopcommand.getPlayerCommands() 
				+ " as the player and " + addopcommand.getConsoleCommands() + " as console.");
				
				p.sendMessage("Command=" + addopcommand.getCommand());
				p.sendMessage("Message=" + addopcommand.getMessage());
				p.sendMessage("Worlds=" + addopcommand.getWorlds());
				p.sendMessage("PlayerCommands=" + addopcommand.getPlayerCommands());
				p.sendMessage("ConsoleCommands=" + addopcommand.getConsoleCommands());
				p.sendMessage("Confirmation=" + addopcommand.getConfirmation());
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
					p.sendMessage("command is not blocked. cancelled.");
					lookingFor.remove(p.getUniqueId().toString());
					partsHad.remove(p.getUniqueId().toString());
					e.setCancelled(true);
					return;
				}
				JsonObject object = partsHad.get(p.getUniqueId().toString());
				object.addProperty("command", e.getMessage());
				partsHad.put(p.getUniqueId().toString(), object);
				
				p.sendMessage("do you want to remove /" + e.getMessage() + " from the block list for ops?");
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
					p.sendMessage("can only be yes or no");
					e.setCancelled(true);
					return;
				}
				
				partsHad.put(p.getUniqueId().toString(), object);
				
				Gson gson = new Gson();
				RemoveOpCommand removeopcommand = gson.fromJson(partsHad.get(p.getUniqueId().toString()), RemoveOpCommand.class);
				
				if (!removeopcommand.getConfirmation()) {
					lookingFor.remove(p.getUniqueId().toString());
					partsHad.remove(p.getUniqueId().toString());
					p.sendMessage("cancelled the command blocker steps");
					e.setCancelled(true);
					return;
				}
				
				Log.addLog(Universal.get().getMethods(), p.getName() + ": Removed command /" + removeopcommand.getCommand() + " in opblock.yml");
				
				p.sendMessage("Command=" + removeopcommand.getCommand());
				p.sendMessage("Confirmation=" + removeopcommand.getConfirmation());
				
				BlockedCommands.removeBlockedCommand(removeopcommand.getCommand());
				
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
				FileConfiguration disabled = BukkitMain.oldConfig() ? OldConfigManager.getDisabled() : ConfigManager.getDisabled();
				if (disabled.getString("DisabledCommands." + msg) != null) {
					p.sendMessage("command is already blocked. cancelled.");
					lookingFor.remove(p.getUniqueId().toString());
					partsHad.remove(p.getUniqueId().toString());
					e.setCancelled(true);
					return;
				}
				JsonObject object = partsHad.get(p.getUniqueId().toString());
				object.addProperty("command", message);
				partsHad.put(p.getUniqueId().toString(), object);
				
				p.sendMessage("add a permission");
				/*
				 * Send message to add a permission
				 */
				
			} else if (!partsHad.get(p.getUniqueId().toString()).has("permission")) {
				
				/*
				 * Send message that you can't use a command 
				 */
				p.sendMessage("you cant use a command for this");
				
				e.setCancelled(true);
				return;
			} else if (!partsHad.get(p.getUniqueId().toString()).has("message")) {
				
				/*
				 * Send message that you can't use a command 
				 */
				p.sendMessage("you cant use a command for this");
				
				e.setCancelled(true);
				return;
			} else if (!partsHad.get(p.getUniqueId().toString()).has("worlds")) {
				
				/*
				 * Send message that you can't use a command 
				 */
				p.sendMessage("you cant use a command for this");
				
				e.setCancelled(true);
				return;
			} else if (!partsHad.get(p.getUniqueId().toString()).has("playercommands")) {
				JsonObject object = partsHad.get(p.getUniqueId().toString());
				object.addProperty("playercommands", message);
				partsHad.put(p.getUniqueId().toString(), object);
				
				p.sendMessage("add a console command");
				/*
				 * Send message to add a console command
				 */
			} else if (!partsHad.get(p.getUniqueId().toString()).has("consolecommands")) {
				JsonObject object = partsHad.get(p.getUniqueId().toString());
				object.addProperty("consolecommands", message);
				partsHad.put(p.getUniqueId().toString(), object);
				
				p.sendMessage("confirmation: yes or no");
				/*
				 * Send message to add a confirmation
				 */
			} else if (!partsHad.get(p.getUniqueId().toString()).has("confirmation")) {
				/*
				 * Send message that you can't use a command 
				 */
				p.sendMessage("you cant use a command for this");
				
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
				FileConfiguration disabled = BukkitMain.oldConfig() ? OldConfigManager.getDisabled() : ConfigManager.getDisabled();
				if (disabled.getString("DisabledCommands." + msg) == null) {
					p.sendMessage("command is not blocked. cancelled.");
					lookingFor.remove(p.getUniqueId().toString());
					partsHad.remove(p.getUniqueId().toString());
					e.setCancelled(true);
					return;
				}
				JsonObject object = partsHad.get(p.getUniqueId().toString());
				object.addProperty("command", message);
				partsHad.put(p.getUniqueId().toString(), object);
				
				p.sendMessage("do you want to remove /" + message + " from the block list?");

				
			} else if (!partsHad.get(p.getUniqueId().toString()).has("confirmation")) {
				/*
				 * Send message that you can't use a command 
				 */
				p.sendMessage("you cant use a command for this");
				
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
				FileConfiguration disabled = BukkitMain.oldConfig() ? OldConfigManager.getOpDisabled() : ConfigManager.getOpDisabled();
				if (disabled.getString("DisabledOpCommands." + msg) != null) {
					p.sendMessage("command is already blocked. cancelled.");
					lookingFor.remove(p.getUniqueId().toString());
					partsHad.remove(p.getUniqueId().toString());
					e.setCancelled(true);
					return;
				}
				JsonObject object = partsHad.get(p.getUniqueId().toString());
				object.addProperty("command", message);
				partsHad.put(p.getUniqueId().toString(), object);
				
				p.sendMessage("add a message");
				/*
				 * Send message to add a permission
				 */
				
			} else if (!partsHad.get(p.getUniqueId().toString()).has("message")) {
				
				/*
				 * Send message that you can't use a command 
				 */
				p.sendMessage("you cant use a command for this");
				
				e.setCancelled(true);
				return;
			} else if (!partsHad.get(p.getUniqueId().toString()).has("worlds")) {
				
				/*
				 * Send message that you can't use a command 
				 */
				p.sendMessage("you cant use a command for this");
				
				e.setCancelled(true);
				return;
			} else if (!partsHad.get(p.getUniqueId().toString()).has("playercommands")) {
				JsonObject object = partsHad.get(p.getUniqueId().toString());
				object.addProperty("playercommands", message);
				partsHad.put(p.getUniqueId().toString(), object);
				
				p.sendMessage("add a console command");
				/*
				 * Send message to add a permission
				 */
			} else if (!partsHad.get(p.getUniqueId().toString()).has("consolecommands")) {
				JsonObject object = partsHad.get(p.getUniqueId().toString());
				object.addProperty("consolecommands", message);
				partsHad.put(p.getUniqueId().toString(), object);
				
				p.sendMessage("confirmation: yes or no");
				/*
				 * Send message to add a permission
				 */
			} else if (!partsHad.get(p.getUniqueId().toString()).has("confirmation")) {
				/*
				 * Send message that you can't use a command 
				 */
				p.sendMessage("you can't use a command for this");
				
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
				FileConfiguration disabled = BukkitMain.oldConfig() ? OldConfigManager.getOpDisabled() : ConfigManager.getOpDisabled();
				if (disabled.getString("DisabledCommands." + msg) == null) {
					p.sendMessage("command is not blocked. cancelled.");
					lookingFor.remove(p.getUniqueId().toString());
					partsHad.remove(p.getUniqueId().toString());
					e.setCancelled(true);
					return;
				}
				JsonObject object = partsHad.get(p.getUniqueId().toString());
				object.addProperty("command", message);
				partsHad.put(p.getUniqueId().toString(), object);
				
				p.sendMessage("do you want to remove /" + message + " from the block list for ops?");
				/*
				 * Send message to add a permission
				 */
				
			} else if (!partsHad.get(p.getUniqueId().toString()).has("confirmation")) {
				/*
				 * Send message that you can't use a command 
				 */
				p.sendMessage("you cant use a command for this");
				
				e.setCancelled(true);
				return;
			}
		}
		e.setCancelled(true);
	}
}
