package me.treyruffy.commandblocker.bukkit.listeners;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import me.treyruffy.commandblocker.Log;
import me.treyruffy.commandblocker.MethodInterface;
import me.treyruffy.commandblocker.Universal;
import me.treyruffy.commandblocker.bukkit.BukkitMain;
import me.treyruffy.commandblocker.bukkit.Variables;
import me.treyruffy.commandblocker.bukkit.api.BlockedCommands;
import me.treyruffy.commandblocker.bukkit.api.BlockedOpCommands;
import me.treyruffy.commandblocker.bukkit.commands.CommandBlockerCmd;
import me.treyruffy.commandblocker.json.AddCommand;
import me.treyruffy.commandblocker.json.AddOpCommand;
import me.treyruffy.commandblocker.json.RemoveCommand;
import me.treyruffy.commandblocker.json.RemoveOpCommand;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.*;

public class CommandValueListener implements Listener {

	public static HashMap<String, String> lookingFor = new HashMap<>();
	public static HashMap<String, JsonObject> partsHad = new HashMap<>();
	public static HashMap<String, BossBar> bossBar = new HashMap<>();
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void readChat(AsyncPlayerChatEvent e) {
		MethodInterface mi = Universal.get().getMethods();
		Player p = e.getPlayer();
		if (!lookingFor.containsKey(p.getUniqueId().toString())) {
			return;
		}
		if (!(p.hasPermission("cb.add") || p.hasPermission("cb.remove") || p.hasPermission("cb.addop") || p.hasPermission("cb.removeop"))) {
			return;
		}

		if (e.getMessage().equalsIgnoreCase("cancel")) {
			lookingFor.remove(p.getUniqueId().toString());
			partsHad.remove(p.getUniqueId().toString());
			if (bossBar.containsKey(p.getUniqueId().toString()))
				if (!BukkitMain.sendOldMessages())
					BukkitMain.adventure().player(p).hideBossBar(bossBar.get(p.getUniqueId().toString()));
			bossBar.remove(p.getUniqueId().toString());
			for (String msgToSend : mi.getOldMessages("Main", "Cancelled")) {
				mi.sendMessage(p, Variables.translateVariables(msgToSend, p));
			}
			/*
			 * Send message that the event was cancelled
			 */
			e.setCancelled(true);
			return;
		}

		if (lookingFor.get(p.getUniqueId().toString()).equalsIgnoreCase("add")) {
			
			if (!p.hasPermission("cb.add")) {
				lookingFor.remove(p.getUniqueId().toString());
				partsHad.remove(p.getUniqueId().toString());
				if (bossBar.containsKey(p.getUniqueId().toString()))
					if (!BukkitMain.sendOldMessages())
						BukkitMain.adventure().player(p).hideBossBar(bossBar.get(p.getUniqueId().toString()));
				bossBar.remove(p.getUniqueId().toString());
				CommandBlockerCmd.noPermissions(mi, p);
				return;
			}
			
			if (!partsHad.get(p.getUniqueId().toString()).has("command")) {
				FileConfiguration disabled = (FileConfiguration) mi.getDisabledCommandsConfig();
				String message = e.getMessage().substring(0, 1).toUpperCase() + e.getMessage().substring(1).toLowerCase();
				if (disabled.contains("DisabledCommands." + message)) {
					for (String msgToSend : mi.getOldMessages("Main", "CommandAlreadyBlocked")) {
						HashMap<String, String> placeholders = new HashMap<>();
						placeholders.put("%c", e.getMessage());
						mi.sendMessage(p, Variables.translateVariables(msgToSend, p, placeholders));
					}
					lookingFor.remove(p.getUniqueId().toString());
					partsHad.remove(p.getUniqueId().toString());
					if (bossBar.containsKey(p.getUniqueId().toString()))
						if (!BukkitMain.sendOldMessages())
							BukkitMain.adventure().player(p).hideBossBar(bossBar.get(p.getUniqueId().toString()));
					bossBar.remove(p.getUniqueId().toString());
					e.setCancelled(true);
					return;
				}

				JsonObject object = partsHad.get(p.getUniqueId().toString());
				object.addProperty("command", e.getMessage());
				partsHad.put(p.getUniqueId().toString(), object);
				HashMap<String, String> placeholders = new HashMap<>();
				placeholders.put("%c", e.getMessage());
				BossBar bar = BossBar.bossBar(Variables.translateVariables(mi.getOldMessage("Main", "AddBossBar"), p,
						placeholders)
						, 1f, BossBar.Color.PURPLE, BossBar.Overlay.PROGRESS);
				bossBar.put(p.getUniqueId().toString(), bar);
				if (!BukkitMain.sendOldMessages())
					BukkitMain.adventure().player(p).showBossBar(bar);

				for (String msgToSend : mi.getOldMessages("Main", "AddPermission")) {
					mi.sendMessage(p, Variables.translateVariables(msgToSend, p));
				}
				/*
				 * Send message to add a permission
				 */

			} else if (!partsHad.get(p.getUniqueId().toString()).has("permission")) {
				JsonObject object = partsHad.get(p.getUniqueId().toString());
				object.addProperty("permission", e.getMessage());
				partsHad.put(p.getUniqueId().toString(), object);

				for (String msgToSend : mi.getOldMessages("Main", "AddMessage")) {
					mi.sendMessage(p, Variables.translateVariables(msgToSend, p));
				}
				/*
				 * Send message to add a message
				 */

			} else if (!partsHad.get(p.getUniqueId().toString()).has("message")) {
				JsonObject object = partsHad.get(p.getUniqueId().toString());
				object.addProperty("message", e.getMessage());
				partsHad.put(p.getUniqueId().toString(), object);

				for (String msgToSend : mi.getOldMessages("Main", "AddWorld")) {
					mi.sendMessage(p, Variables.translateVariables(msgToSend, p));
				}
				/*
				 * Send message to add worlds. Allow for all.
				 */

			} else if (!partsHad.get(p.getUniqueId().toString()).has("worlds")) {
				JsonObject object = partsHad.get(p.getUniqueId().toString());
				object.addProperty("worlds", e.getMessage());
				partsHad.put(p.getUniqueId().toString(), object);

				for (String msgToSend : mi.getOldMessages("Main", "AddPlayerCommand")) {
					mi.sendMessage(p, Variables.translateVariables(msgToSend, p));
				}
				/*
				 * Send message to add player commands. Allow for none.
				 */

			} else if (!partsHad.get(p.getUniqueId().toString()).has("playercommands")) {
				JsonObject object = partsHad.get(p.getUniqueId().toString());
				object.addProperty("playercommands", e.getMessage());
				partsHad.put(p.getUniqueId().toString(), object);

				for (String msgToSend : mi.getOldMessages("Main", "AddConsoleCommand")) {
					mi.sendMessage(p, Variables.translateVariables(msgToSend, p));
				}
				/*
				 * Send message to add console commands. Allow for none.
				 */

			} else if (!partsHad.get(p.getUniqueId().toString()).has("consolecommands")) {
				JsonObject object = partsHad.get(p.getUniqueId().toString());
				object.addProperty("consolecommands", e.getMessage());
				partsHad.put(p.getUniqueId().toString(), object);

				for (String msgToSend : mi.getOldMessages("Main", "AddConfirmation")) {
					mi.sendMessage(p, Variables.translateVariables(msgToSend, p));
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
					for (String msgToSend : mi.getOldMessages("Main", "CanOnlyBeYesOrNo")) {
						mi.sendMessage(p, Variables.translateVariables(msgToSend, p));
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
					if (bossBar.containsKey(p.getUniqueId().toString()))
						if (!BukkitMain.sendOldMessages())
							BukkitMain.adventure().player(p).hideBossBar(bossBar.get(p.getUniqueId().toString()));
					bossBar.remove(p.getUniqueId().toString());
					for (String msgToSend : mi.getOldMessages("Main", "Cancelled")) {
						mi.sendMessage(p, Variables.translateVariables(msgToSend, p));
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
					for (String msgToSend : mi.getOldMessages("Main", "AddedCommandOutput")) {
						HashMap<String, String> placeholders = new HashMap<>();
						placeholders.put("%c", addcommand.getCommand());
						placeholders.put("%p", addcommand.getPermission());
						placeholders.put("%m", addcommand.getMessage());
						placeholders.put("%w", addcommand.getWorlds());
						placeholders.put("%x", addcommand.getPlayerCommands());
						placeholders.put("%z", addcommand.getConsoleCommands());
						placeholders.put("%y", String.valueOf(addcommand.getConfirmation()));
						mi.sendMessage(p, Variables.translateVariables(msgToSend, p, placeholders));
					}
				} else {
					for (String msgToSend : mi.getOldMessages("Main", "CouldNotAddCommandToConfig")) {
						mi.sendMessage(p, Variables.translateVariables(msgToSend, p));
					}
				}
				lookingFor.remove(p.getUniqueId().toString());
				partsHad.remove(p.getUniqueId().toString());
				if (bossBar.containsKey(p.getUniqueId().toString()))
					if (!BukkitMain.sendOldMessages())
						BukkitMain.adventure().player(p).hideBossBar(bossBar.get(p.getUniqueId().toString()));
				bossBar.remove(p.getUniqueId().toString());
			}
			e.setCancelled(true);
			return;
		}
		if (lookingFor.get(p.getUniqueId().toString()).equalsIgnoreCase("remove")) {
			if (!p.hasPermission("cb.remove")) {
				CommandBlockerCmd.noPermissions(mi, p);
				lookingFor.remove(p.getUniqueId().toString());
				if (bossBar.containsKey(p.getUniqueId().toString()))
					if (!BukkitMain.sendOldMessages())
						BukkitMain.adventure().player(p).hideBossBar(bossBar.get(p.getUniqueId().toString()));
				bossBar.remove(p.getUniqueId().toString());
				partsHad.remove(p.getUniqueId().toString());
				return;
			}
			
			if (!partsHad.get(p.getUniqueId().toString()).has("command")) {
				FileConfiguration disabled = (FileConfiguration) mi.getDisabledCommandsConfig();
				String message = e.getMessage().substring(0, 1).toUpperCase() + e.getMessage().substring(1).toLowerCase();
				if (!disabled.contains("DisabledCommands." + message)) {
					for (String msgToSend : mi.getOldMessages("Main", "UnblockCancelledBecauseNotBlocked")) {
						HashMap<String, String> placeholders = new HashMap<>();
						placeholders.put("%c", e.getMessage());
						mi.sendMessage(p, Variables.translateVariables(msgToSend, p, placeholders));
					}
					lookingFor.remove(p.getUniqueId().toString());
					if (bossBar.containsKey(p.getUniqueId().toString()))
						if (!BukkitMain.sendOldMessages())
							BukkitMain.adventure().player(p).hideBossBar(bossBar.get(p.getUniqueId().toString()));
					bossBar.remove(p.getUniqueId().toString());
					partsHad.remove(p.getUniqueId().toString());
					e.setCancelled(true);
					return;
				}
				JsonObject object = partsHad.get(p.getUniqueId().toString());
				object.addProperty("command", message);
				partsHad.put(p.getUniqueId().toString(), object);
				HashMap<String, String> placeholders = new HashMap<>();
				placeholders.put("%c", e.getMessage());
				BossBar bar = BossBar.bossBar(Variables.translateVariables(mi.getOldMessage("Main", "RemoveBossBar"), p,
						placeholders)
						, 1f, BossBar.Color.PURPLE, BossBar.Overlay.PROGRESS);
				bossBar.put(p.getUniqueId().toString(), bar);
				if (!BukkitMain.sendOldMessages())
					BukkitMain.adventure().player(p).showBossBar(bar);

				for (String msgToSend : mi.getOldMessages("Main", "RemoveCommandQuestion")) {
					mi.sendMessage(p, Variables.translateVariables(msgToSend, p, placeholders));
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
					for (String msgToSend : mi.getOldMessages("Main", "CanOnlyBeYesOrNo")) {
						mi.sendMessage(p, Variables.translateVariables(msgToSend, p));
					}
					e.setCancelled(true);
					return;
				}
				partsHad.put(p.getUniqueId().toString(), object);
				
				Gson gson = new Gson();
				RemoveCommand removecommand = gson.fromJson(partsHad.get(p.getUniqueId().toString()), RemoveCommand.class);

				if (!removecommand.getConfirmation()) {
					lookingFor.remove(p.getUniqueId().toString());
					if (bossBar.containsKey(p.getUniqueId().toString()))
						if (!BukkitMain.sendOldMessages())
							BukkitMain.adventure().player(p).hideBossBar(bossBar.get(p.getUniqueId().toString()));
					bossBar.remove(p.getUniqueId().toString());
					partsHad.remove(p.getUniqueId().toString());
					for (String msgToSend : mi.getOldMessages("Main", "Cancelled")) {
						mi.sendMessage(p, Variables.translateVariables(msgToSend, p));
					}
					e.setCancelled(true);
					return;
				}

				if (BlockedCommands.removeBlockedCommand(removecommand.getCommand())) {
					Log.addLog(Universal.get().getMethods(),
							p.getName() + ": Removed command /" + removecommand.getCommand() + " in disabled.yml");

					for (String msgToSend : mi.getOldMessages("Main", "RemovedCommandOutput")) {
						HashMap<String, String> placeholders = new HashMap<>();
						placeholders.put("%c", removecommand.getCommand());
						placeholders.put("%y", String.valueOf(removecommand.getConfirmation()));
						mi.sendMessage(p, Variables.translateVariables(msgToSend, p, placeholders));
					}
				} else {
					for (String msgToSend : mi.getOldMessages("Main", "UnblockCancelledBecauseNotBlocked")) {
						HashMap<String, String> placeholders = new HashMap<>();
						placeholders.put("%c", removecommand.getCommand());
						mi.sendMessage(p, Variables.translateVariables(msgToSend, p, placeholders));
					}
				}

				partsHad.remove(p.getUniqueId().toString());
				lookingFor.remove(p.getUniqueId().toString());
				if (bossBar.containsKey(p.getUniqueId().toString()))
					if (!BukkitMain.sendOldMessages())
						BukkitMain.adventure().player(p).hideBossBar(bossBar.get(p.getUniqueId().toString()));
				bossBar.remove(p.getUniqueId().toString());
			}
			e.setCancelled(true);
			return;
		}
		if (lookingFor.get(p.getUniqueId().toString()).equalsIgnoreCase("addop")) {
			if (!p.hasPermission("cb.addop")) {
				CommandBlockerCmd.noPermissions(mi, p);
				lookingFor.remove(p.getUniqueId().toString());
				partsHad.remove(p.getUniqueId().toString());
				if (bossBar.containsKey(p.getUniqueId().toString()))
					if (!BukkitMain.sendOldMessages())
						BukkitMain.adventure().player(p).hideBossBar(bossBar.get(p.getUniqueId().toString()));
				bossBar.remove(p.getUniqueId().toString());
				return;
			}
			
			if (!partsHad.get(p.getUniqueId().toString()).has("command")) {
				FileConfiguration disabled = (FileConfiguration) mi.getOpBlockConfig();
				String message = e.getMessage().substring(0, 1).toUpperCase() + e.getMessage().substring(1).toLowerCase();
				if (disabled.contains("DisabledOpCommands." + message)) {
					for (String msgToSend : mi.getOldMessages("Main", "CommandAlreadyBlocked")) {
						HashMap<String, String> placeholders = new HashMap<>();
						placeholders.put("%c", e.getMessage());
						mi.sendMessage(p, Variables.translateVariables(msgToSend, p, placeholders));
					}
					lookingFor.remove(p.getUniqueId().toString());
					if (bossBar.containsKey(p.getUniqueId().toString()))
						if (!BukkitMain.sendOldMessages())
							BukkitMain.adventure().player(p).hideBossBar(bossBar.get(p.getUniqueId().toString()));
					bossBar.remove(p.getUniqueId().toString());
					partsHad.remove(p.getUniqueId().toString());
					e.setCancelled(true);
					return;
				}
				JsonObject object = partsHad.get(p.getUniqueId().toString());
				object.addProperty("command", e.getMessage());
				HashMap<String, String> placeholders = new HashMap<>();
				placeholders.put("%c", e.getMessage());
				BossBar bar = BossBar.bossBar(Variables.translateVariables(mi.getOldMessage("Main", "AddOpBossBar"), p,
						placeholders)
						, 1f, BossBar.Color.PURPLE, BossBar.Overlay.PROGRESS);
				bossBar.put(p.getUniqueId().toString(), bar);
				if (!BukkitMain.sendOldMessages())
					BukkitMain.adventure().player(p).showBossBar(bar);
				partsHad.put(p.getUniqueId().toString(), object);

				for (String msgToSend : mi.getOldMessages("Main", "AddOpAddMessage")) {
					mi.sendMessage(p, Variables.translateVariables(msgToSend, p));
				}
				/*
				 * Send message to add a message
				 */

			} else if (!partsHad.get(p.getUniqueId().toString()).has("message")) {
				JsonObject object = partsHad.get(p.getUniqueId().toString());
				object.addProperty("message", e.getMessage());
				partsHad.put(p.getUniqueId().toString(), object);

				for (String msgToSend : mi.getOldMessages("Main", "AddOpAddWorld")) {
					mi.sendMessage(p, Variables.translateVariables(msgToSend, p));
				}
				/*
				 * Send message to add worlds. Allow for all.
				 */

			} else if (!partsHad.get(p.getUniqueId().toString()).has("worlds")) {
				JsonObject object = partsHad.get(p.getUniqueId().toString());
				object.addProperty("worlds", e.getMessage());
				partsHad.put(p.getUniqueId().toString(), object);

				for (String msgToSend : mi.getOldMessages("Main", "AddOpAddPlayerCommand")) {
					mi.sendMessage(p, Variables.translateVariables(msgToSend, p));
				}
				/*
				 * Send message to add player commands. Allow for none.
				 */

			} else if (!partsHad.get(p.getUniqueId().toString()).has("playercommands")) {
				JsonObject object = partsHad.get(p.getUniqueId().toString());
				object.addProperty("playercommands", e.getMessage());
				partsHad.put(p.getUniqueId().toString(), object);

				for (String msgToSend : mi.getOldMessages("Main", "AddOpAddConsoleCommand")) {
					mi.sendMessage(p, Variables.translateVariables(msgToSend, p));
				}
				/*
				 * Send message to add console commands. Allow for none.
				 */

			} else if (!partsHad.get(p.getUniqueId().toString()).has("consolecommands")) {
				JsonObject object = partsHad.get(p.getUniqueId().toString());
				object.addProperty("consolecommands", e.getMessage());
				partsHad.put(p.getUniqueId().toString(), object);

				for (String msgToSend : mi.getOldMessages("Main", "AddOpConfirmation")) {
					mi.sendMessage(p, Variables.translateVariables(msgToSend, p));
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
					for (String msgToSend : mi.getOldMessages("Main", "CanOnlyBeYesOrNo")) {
						mi.sendMessage(p, Variables.translateVariables(msgToSend, p));
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
					if (bossBar.containsKey(p.getUniqueId().toString()))
						if (!BukkitMain.sendOldMessages())
							BukkitMain.adventure().player(p).hideBossBar(bossBar.get(p.getUniqueId().toString()));
					bossBar.remove(p.getUniqueId().toString());
					for (String msgToSend : mi.getOldMessages("Main", "Cancelled")) {
						mi.sendMessage(p, Variables.translateVariables(msgToSend, p));
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

					for (String msgToSend : mi.getOldMessages("Main", "AddedOpCommandOutput")) {
						HashMap<String, String> placeholders = new HashMap<>();
						placeholders.put("%c", addopcommand.getCommand());
						placeholders.put("%m", addopcommand.getMessage());
						placeholders.put("%w", addopcommand.getWorlds());
						placeholders.put("%x", addopcommand.getPlayerCommands());
						placeholders.put("%z", addopcommand.getConsoleCommands());
						placeholders.put("%y", String.valueOf(addopcommand.getConfirmation()));
						mi.sendMessage(p, Variables.translateVariables(msgToSend, p, placeholders));
					}
				} else {
					for (String msgToSend : mi.getOldMessages("Main", "CouldNotAddCommandToOpConfig")) {
						HashMap<String, String> placeholders = new HashMap<>();
						placeholders.put("%c", addopcommand.getCommand());
						mi.sendMessage(p, Variables.translateVariables(msgToSend, p, placeholders));
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
				if (bossBar.containsKey(p.getUniqueId().toString()))
					if (!BukkitMain.sendOldMessages())
						BukkitMain.adventure().player(p).hideBossBar(bossBar.get(p.getUniqueId().toString()));
				bossBar.remove(p.getUniqueId().toString());
			}
			e.setCancelled(true);
			return;
		}
		if (lookingFor.get(p.getUniqueId().toString()).equalsIgnoreCase("removeop")) {
			if (!p.hasPermission("cb.removeop")) {
				CommandBlockerCmd.noPermissions(mi, p);
				lookingFor.remove(p.getUniqueId().toString());
				partsHad.remove(p.getUniqueId().toString());
				if (bossBar.containsKey(p.getUniqueId().toString()))
					if (!BukkitMain.sendOldMessages())
						BukkitMain.adventure().player(p).hideBossBar(bossBar.get(p.getUniqueId().toString()));
				bossBar.remove(p.getUniqueId().toString());
				return;
			}
			
			if (!partsHad.get(p.getUniqueId().toString()).has("command")) {
				FileConfiguration disabled = (FileConfiguration) mi.getOpBlockConfig();
				String message = e.getMessage().substring(0, 1).toUpperCase() + e.getMessage().substring(1).toLowerCase();
				if (!disabled.contains("DisabledOpCommands." + message)) {
					for (String msgToSend : mi.getOldMessages("Main", "RemoveOpCouldNotRemove")) {
						HashMap<String, String> placeholders = new HashMap<>();
						placeholders.put("%c", e.getMessage());
						mi.sendMessage(p, Variables.translateVariables(msgToSend, p, placeholders));
					}
					lookingFor.remove(p.getUniqueId().toString());
					if (bossBar.containsKey(p.getUniqueId().toString()))
						if (!BukkitMain.sendOldMessages())
							BukkitMain.adventure().player(p).hideBossBar(bossBar.get(p.getUniqueId().toString()));
					bossBar.remove(p.getUniqueId().toString());
					partsHad.remove(p.getUniqueId().toString());
					e.setCancelled(true);
					return;
				}
				JsonObject object = partsHad.get(p.getUniqueId().toString());
				object.addProperty("command", e.getMessage());
				partsHad.put(p.getUniqueId().toString(), object);
				HashMap<String, String> placeholders = new HashMap<>();
				placeholders.put("%c", e.getMessage());
				BossBar bar = BossBar.bossBar(Variables.translateVariables(mi.getOldMessage("Main", "RemoveOpBossBar")
						, p,
						placeholders)
						, 1f, BossBar.Color.PURPLE, BossBar.Overlay.PROGRESS);
				bossBar.put(p.getUniqueId().toString(), bar);
				if (!BukkitMain.sendOldMessages())
					BukkitMain.adventure().player(p).showBossBar(bar);

				for (String msgToSend : mi.getOldMessages("Main", "RemoveOpQuestion")) {
					mi.sendMessage(p, Variables.translateVariables(msgToSend, p, placeholders));
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
					for (String msgToSend : mi.getOldMessages("Main", "CanOnlyBeYesOrNo")) {
						mi.sendMessage(p, Variables.translateVariables(msgToSend, p));
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
					if (bossBar.containsKey(p.getUniqueId().toString()))
						if (!BukkitMain.sendOldMessages())
							BukkitMain.adventure().player(p).hideBossBar(bossBar.get(p.getUniqueId().toString()));
					bossBar.remove(p.getUniqueId().toString());
					for (String msgToSend : mi.getOldMessages("Main", "Cancelled")) {
						mi.sendMessage(p, Variables.translateVariables(msgToSend, p));
					}
					e.setCancelled(true);
					return;
				}

				if (BlockedOpCommands.removeBlockedCommand(removeopcommand.getCommand())) {
					Log.addLog(Universal.get().getMethods(),
							p.getName() + ": Removed command /" + removeopcommand.getCommand() + " in opblock.yml");

					for (String msgToSend : mi.getOldMessages("Main", "RemovedOpCommandOutput")) {
						HashMap<String, String> placeholders = new HashMap<>();
						placeholders.put("%c", removeopcommand.getCommand());
						mi.sendMessage(p, Variables.translateVariables(msgToSend, p, placeholders));
					}
				} else {
					for (String msgToSend : mi.getOldMessages("Main", "RemoveOpCouldNotRemove")) {
						HashMap<String, String> placeholders = new HashMap<>();
						placeholders.put("%c", removeopcommand.getCommand());
						mi.sendMessage(p, Variables.translateVariables(msgToSend, p, placeholders));
					}
				}

				partsHad.remove(p.getUniqueId().toString());
				lookingFor.remove(p.getUniqueId().toString());
				if (bossBar.containsKey(p.getUniqueId().toString()))
					if (!BukkitMain.sendOldMessages())
						BukkitMain.adventure().player(p).hideBossBar(bossBar.get(p.getUniqueId().toString()));
				bossBar.remove(p.getUniqueId().toString());
			}
		}
		
		e.setCancelled(true);
	}
	
	@EventHandler
	public void removeOnLeave(PlayerQuitEvent e) {
		lookingFor.remove(e.getPlayer().getUniqueId().toString());
		partsHad.remove(e.getPlayer().getUniqueId().toString());
		if (bossBar.containsKey(e.getPlayer().getUniqueId().toString()))
			if (!BukkitMain.sendOldMessages())
				BukkitMain.adventure().player(e.getPlayer()).hideBossBar(bossBar.get(e.getPlayer().getUniqueId().toString()));
		bossBar.remove(e.getPlayer().getUniqueId().toString());
	}
	
	@EventHandler (priority = EventPriority.HIGHEST)
	public void readCommandsSent(PlayerCommandPreprocessEvent e) {
		MethodInterface mi = Universal.get().getMethods();
		Player p = e.getPlayer();
		BukkitAudiences adventure = BukkitMain.adventure();
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
				CommandBlockerCmd.noPermissions(mi, p);
				return;
			}
			
			if (!partsHad.get(p.getUniqueId().toString()).has("command")) {
				FileConfiguration disabled = (FileConfiguration) mi.getDisabledCommandsConfig();
				if (disabled.contains("DisabledCommands." + msg)) {
					for (String msgToSend : mi.getOldMessages("Main", "CommandAlreadyBlocked")) {
						HashMap<String, String> placeholders = new HashMap<>();
						placeholders.put("%c", message);
						mi.sendMessage(p, Variables.translateVariables(msgToSend, p, placeholders));
					}
					lookingFor.remove(p.getUniqueId().toString());
					partsHad.remove(p.getUniqueId().toString());
					if (bossBar.containsKey(p.getUniqueId().toString()))
						adventure.player(p).hideBossBar(bossBar.get(p.getUniqueId().toString()));
					bossBar.remove(p.getUniqueId().toString());
					e.setCancelled(true);
					return;
				}

				JsonObject object = partsHad.get(p.getUniqueId().toString());
				object.addProperty("command", message);
				partsHad.put(p.getUniqueId().toString(), object);

				for (String msgToSend : mi.getOldMessages("Main", "AddPermission")) {
					mi.sendMessage(p, Variables.translateVariables(msgToSend, p));
				}
				/*
				 * Send message to add a permission
				 */

			} else if (!partsHad.get(p.getUniqueId().toString()).has("permission")) {

				/*
				 * Send message that you can't use a command
				 */
				for (String msgToSend : mi.getOldMessages("Main", "CantUseCommand")) {
					mi.sendMessage(p, Variables.translateVariables(msgToSend, p));
				}

				e.setCancelled(true);
				return;
			} else if (!partsHad.get(p.getUniqueId().toString()).has("message")) {

				/*
				 * Send message that you can't use a command
				 */
				for (String msgToSend : mi.getOldMessages("Main", "CantUseCommand")) {
					mi.sendMessage(p, Variables.translateVariables(msgToSend, p));
				}

				e.setCancelled(true);
				return;
			} else if (!partsHad.get(p.getUniqueId().toString()).has("worlds")) {

				/*
				 * Send message that you can't use a command
				 */
				for (String msgToSend : mi.getOldMessages("Main", "CantUseCommand")) {
					mi.sendMessage(p, Variables.translateVariables(msgToSend, p));
				}

				e.setCancelled(true);
				return;
			} else if (!partsHad.get(p.getUniqueId().toString()).has("playercommands")) {
				JsonObject object = partsHad.get(p.getUniqueId().toString());
				object.addProperty("playercommands", message);
				partsHad.put(p.getUniqueId().toString(), object);

				for (String msgToSend : mi.getOldMessages("Main", "AddConsoleCommand")) {
					mi.sendMessage(p, Variables.translateVariables(msgToSend, p));
				}
				/*
				 * Send message to add a console command
				 */
			} else if (!partsHad.get(p.getUniqueId().toString()).has("consolecommands")) {
				JsonObject object = partsHad.get(p.getUniqueId().toString());
				object.addProperty("consolecommands", message);
				partsHad.put(p.getUniqueId().toString(), object);

				for (String msgToSend : mi.getOldMessages("Main", "Confirmation")) {
					mi.sendMessage(p, Variables.translateVariables(msgToSend, p));
				}
				/*
				 * Send message to add a confirmation
				 */
			} else if (!partsHad.get(p.getUniqueId().toString()).has("confirmation")) {
				/*
				 * Send message that you can't use a command
				 */
				for (String msgToSend : mi.getOldMessages("Main", "CantUseCommand")) {
					mi.sendMessage(p, Variables.translateVariables(msgToSend, p));
				}

				e.setCancelled(true);
				return;
			}
		} 
		if (lookingFor.get(p.getUniqueId().toString()).equalsIgnoreCase("remove")) {
			
			if (!p.hasPermission("cb.remove")) {
				CommandBlockerCmd.noPermissions(mi, p);
				return;
			}
			
			if (!partsHad.get(p.getUniqueId().toString()).has("command")) {
				FileConfiguration disabled = (FileConfiguration) mi.getDisabledCommandsConfig();
				if (!disabled.contains("DisabledCommands." + msg)) {
					for (String msgToSend : mi.getOldMessages("Main", "UnblockCancelledBecauseNotBlocked")) {
						HashMap<String, String> placeholders = new HashMap<>();
						placeholders.put("%c", message);
						mi.sendMessage(p, Variables.translateVariables(msgToSend, p, placeholders));
					}
					lookingFor.remove(p.getUniqueId().toString());
					if (bossBar.containsKey(p.getUniqueId().toString()))
						adventure.player(p).hideBossBar(bossBar.get(p.getUniqueId().toString()));
					bossBar.remove(p.getUniqueId().toString());
					partsHad.remove(p.getUniqueId().toString());
					e.setCancelled(true);
					return;
				}
				JsonObject object = partsHad.get(p.getUniqueId().toString());
				object.addProperty("command", message);
				partsHad.put(p.getUniqueId().toString(), object);

				for (String msgToSend : mi.getOldMessages("Main", "RemoveCommandQuestion")) {
					HashMap<String, String> placeholders = new HashMap<>();
					placeholders.put("%c", message);
					mi.sendMessage(p, Variables.translateVariables(msgToSend, p, placeholders));
				}

			} else if (!partsHad.get(p.getUniqueId().toString()).has("confirmation")) {
				/*
				 * Send message that you can't use a command
				 */
				for (String msgToSend : mi.getOldMessages("Main", "CantUseCommand")) {
					mi.sendMessage(p, Variables.translateVariables(msgToSend, p));
				}

				e.setCancelled(true);
				return;
			}
		}
		if (lookingFor.get(p.getUniqueId().toString()).equalsIgnoreCase("addop")) {
			
			if (!p.hasPermission("cb.addop")) {
				CommandBlockerCmd.noPermissions(mi, p);
				return;
			}
			
			if (!partsHad.get(p.getUniqueId().toString()).has("command")) {
				FileConfiguration disabled = (FileConfiguration) mi.getOpBlockConfig();
				if (disabled.contains("DisabledOpCommands." + msg)) {
					for (String msgToSend : mi.getOldMessages("Main", "CommandAlreadyBlocked")) {
						HashMap<String, String> placeholders = new HashMap<>();
						placeholders.put("%c", message);
						mi.sendMessage(p, Variables.translateVariables(msgToSend, p, placeholders));
					}
					lookingFor.remove(p.getUniqueId().toString());
					partsHad.remove(p.getUniqueId().toString());
					if (bossBar.containsKey(p.getUniqueId().toString()))
						adventure.player(p).hideBossBar(bossBar.get(p.getUniqueId().toString()));
					bossBar.remove(p.getUniqueId().toString());
					e.setCancelled(true);
					return;
				}
				JsonObject object = partsHad.get(p.getUniqueId().toString());
				object.addProperty("command", message);
				partsHad.put(p.getUniqueId().toString(), object);

				for (String msgToSend : mi.getOldMessages("Main", "AddOpAddMessage")) {
					mi.sendMessage(p, Variables.translateVariables(msgToSend, p));
				}
				/*
				 * Send message to add a permission
				 */

			} else if (!partsHad.get(p.getUniqueId().toString()).has("message")) {

				/*
				 * Send message that you can't use a command
				 */
				for (String msgToSend : mi.getOldMessages("Main", "CantUseCommand")) {
					mi.sendMessage(p, Variables.translateVariables(msgToSend, p));
				}

				e.setCancelled(true);
				return;
			} else if (!partsHad.get(p.getUniqueId().toString()).has("worlds")) {

				/*
				 * Send message that you can't use a command
				 */
				for (String msgToSend : mi.getOldMessages("Main", "CantUseCommand")) {
					mi.sendMessage(p, Variables.translateVariables(msgToSend, p));
				}

				e.setCancelled(true);
				return;
			} else if (!partsHad.get(p.getUniqueId().toString()).has("playercommands")) {
				JsonObject object = partsHad.get(p.getUniqueId().toString());
				object.addProperty("playercommands", message);
				partsHad.put(p.getUniqueId().toString(), object);

				for (String msgToSend : mi.getOldMessages("Main", "AddOpAddConsoleCommand")) {
					mi.sendMessage(p, Variables.translateVariables(msgToSend, p));
				}
				/*
				 * Send message to add a permission
				 */
			} else if (!partsHad.get(p.getUniqueId().toString()).has("consolecommands")) {
				JsonObject object = partsHad.get(p.getUniqueId().toString());
				object.addProperty("consolecommands", message);
				partsHad.put(p.getUniqueId().toString(), object);

				for (String msgToSend : mi.getOldMessages("Main", "Confirmation")) {
					mi.sendMessage(p, Variables.translateVariables(msgToSend, p));
				}
				/*
				 * Send message to add a permission
				 */
			} else if (!partsHad.get(p.getUniqueId().toString()).has("confirmation")) {
				/*
				 * Send message that you can't use a command
				 */
				for (String msgToSend : mi.getOldMessages("Main", "CantUseCommand")) {
					mi.sendMessage(p, Variables.translateVariables(msgToSend, p));
				}

				e.setCancelled(true);
				return;
			}
		} 
		if (lookingFor.get(p.getUniqueId().toString()).equalsIgnoreCase("removeop")) {
			
			if (!p.hasPermission("cb.removeop")) {
				CommandBlockerCmd.noPermissions(mi, p);
				return;
			}
			if (!partsHad.get(p.getUniqueId().toString()).has("command")) {
				FileConfiguration disabled = (FileConfiguration) mi.getOpBlockConfig();
				if (!disabled.contains("DisabledOpCommands." + msg)) {
					for (String msgToSend : mi.getOldMessages("Main", "RemoveOpCouldNotRemove")) {
						HashMap<String, String> placeholders = new HashMap<>();
						placeholders.put("%c", message);
						mi.sendMessage(p, Variables.translateVariables(msgToSend, p, placeholders));
					}
					lookingFor.remove(p.getUniqueId().toString());
					partsHad.remove(p.getUniqueId().toString());
					if (bossBar.containsKey(p.getUniqueId().toString()))
						adventure.player(p).hideBossBar(bossBar.get(p.getUniqueId().toString()));
					bossBar.remove(p.getUniqueId().toString());
					e.setCancelled(true);
					return;
				}
				JsonObject object = partsHad.get(p.getUniqueId().toString());
				object.addProperty("command", message);
				partsHad.put(p.getUniqueId().toString(), object);

				for (String msgToSend : mi.getOldMessages("Main", "RemoveOpQuestion")) {
					HashMap<String, String> placeholders = new HashMap<>();
					placeholders.put("%c", message);
					mi.sendMessage(p, Variables.translateVariables(msgToSend, p, placeholders));
				}
				/*
				 * Send message to add a permission
				 */

			} else if (!partsHad.get(p.getUniqueId().toString()).has("confirmation")) {
				/*
				 * Send message that you can't use a command
				 */
				for (String msgToSend : mi.getOldMessages("Main", "CantUseCommand")) {
					mi.sendMessage(p, Variables.translateVariables(msgToSend, p));
				}

				e.setCancelled(true);
				return;
			}
		}
		e.setCancelled(true);
	}
}
