package me.treyruffy.commandblocker.bungeecord.listeners;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import me.treyruffy.commandblocker.Log;
import me.treyruffy.commandblocker.MethodInterface;
import me.treyruffy.commandblocker.Universal;
import me.treyruffy.commandblocker.bungeecord.BungeeMain;
import me.treyruffy.commandblocker.bungeecord.Variables;
import me.treyruffy.commandblocker.bungeecord.api.BlockedCommands;
import me.treyruffy.commandblocker.bungeecord.commands.CommandBlockerCommand;
import me.treyruffy.commandblocker.json.AddCommand;
import me.treyruffy.commandblocker.json.RemoveCommand;
import net.kyori.adventure.bossbar.BossBar;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class BungeeCommandValueListener implements Listener {

	public static HashMap<String, String> lookingFor = new HashMap<>();
	public static HashMap<String, JsonObject> partsHad = new HashMap<>();
	public static HashMap<String, BossBar> bossBar = new HashMap<>();
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onChat(ChatEvent e) {
		if (!(e.getSender() instanceof ProxiedPlayer)) {
			return;
		}
		MethodInterface mi = Universal.get().getMethods();
		ProxiedPlayer p = (ProxiedPlayer) e.getSender();
		if (!lookingFor.containsKey(p.getUniqueId().toString())) {
			return;
		}
		if (!(p.hasPermission("cb.add") || p.hasPermission("cb.remove"))) {
			return;
		}

		if (e.getMessage().equalsIgnoreCase("cancel")) {
			lookingFor.remove(p.getUniqueId().toString());
			partsHad.remove(p.getUniqueId().toString());
			if (bossBar.containsKey(p.getUniqueId().toString()))
				BungeeMain.adventure().player(p).hideBossBar(bossBar.get(p.getUniqueId().toString()));
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
		if (e.getMessage().startsWith("/")) {
			String message = e.getMessage().substring(1);

			String msg = message.substring(0, 1).toUpperCase() + message.substring(1).toLowerCase();
			if (lookingFor.get(p.getUniqueId().toString()).equalsIgnoreCase("add")) {

				if (!p.hasPermission("cb.add")) {
					CommandBlockerCommand.noPermissions(mi, p);
					return;
				}

				if (!partsHad.get(p.getUniqueId().toString()).has("command")) {
					Configuration disabled = (Configuration) mi.getDisabledCommandsConfig();
					if (disabled.contains("DisabledCommands." + msg)) {
						for (String msgToSend : mi.getOldMessages("Main", "CommandAlreadyBlocked")) {
							HashMap<String, String> placeholders = new HashMap<>();
							placeholders.put("%c", message);
							mi.sendMessage(p, Variables.translateVariables(msgToSend, p, placeholders));
						}
						lookingFor.remove(p.getUniqueId().toString());
						if (bossBar.containsKey(p.getUniqueId().toString()))
							BungeeMain.adventure().player(p).hideBossBar(bossBar.get(p.getUniqueId().toString()));
						bossBar.remove(p.getUniqueId().toString());
						partsHad.remove(p.getUniqueId().toString());
						e.setCancelled(true);
						return;
					}

					JsonObject object = partsHad.get(p.getUniqueId().toString());
					object.addProperty("command", message);
					HashMap<String, String> placeholders = new HashMap<>();
					placeholders.put("%c", message);
					BossBar bar = BossBar.bossBar(Variables.translateVariables(mi.getOldMessage("Main", "AddBossBar"),
							p, placeholders),1f, BossBar.Color.PURPLE, BossBar.Overlay.PROGRESS);
					BungeeCommandValueListener.bossBar.put(p.getUniqueId().toString(), bar);
					BungeeMain.adventure().player(p).showBossBar(bar);
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
					CommandBlockerCommand.noPermissions(mi, p);
					return;
				}

				if (!partsHad.get(p.getUniqueId().toString()).has("command")) {
					Configuration disabled = (Configuration) mi.getDisabledCommandsConfig();
					if (!disabled.contains("DisabledCommands." + msg)) {
						for (String msgToSend : mi.getOldMessages("Main", "UnblockCancelledBecauseNotBlocked")) {
							HashMap<String, String> placeholders = new HashMap<>();
							placeholders.put("%c", message);
							mi.sendMessage(p, Variables.translateVariables(msgToSend, p, placeholders));
						}
						lookingFor.remove(p.getUniqueId().toString());
						if (bossBar.containsKey(p.getUniqueId().toString()))
							BungeeMain.adventure().player(p).hideBossBar(bossBar.get(p.getUniqueId().toString()));
						bossBar.remove(p.getUniqueId().toString());
						partsHad.remove(p.getUniqueId().toString());
						e.setCancelled(true);
						return;
					}
					JsonObject object = partsHad.get(p.getUniqueId().toString());
					object.addProperty("command", message);
					partsHad.put(p.getUniqueId().toString(), object);
					HashMap<String, String> placeholders = new HashMap<>();
					placeholders.put("%c", message);
					BossBar bar = BossBar.bossBar(Variables.translateVariables(mi.getOldMessage("Main",
							"RemoveBossBar"),
							p, placeholders),1f, BossBar.Color.PURPLE, BossBar.Overlay.PROGRESS);
					BungeeCommandValueListener.bossBar.put(p.getUniqueId().toString(), bar);
					BungeeMain.adventure().player(p).showBossBar(bar);

					for (String msgToSend : mi.getOldMessages("Main", "RemoveCommandQuestion")) {
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
		}
		if (lookingFor.get(p.getUniqueId().toString()).equalsIgnoreCase("add")) {

			if (!p.hasPermission("cb.add")) {
				lookingFor.remove(p.getUniqueId().toString());
				if (bossBar.containsKey(p.getUniqueId().toString()))
					BungeeMain.adventure().player(p).hideBossBar(bossBar.get(p.getUniqueId().toString()));
				bossBar.remove(p.getUniqueId().toString());
				partsHad.remove(p.getUniqueId().toString());
				CommandBlockerCommand.noPermissions(mi, p);
				return;
			}

			if (!partsHad.get(p.getUniqueId().toString()).has("command")) {
				Configuration disabled = (Configuration) mi.getDisabledCommandsConfig();
				String msg = e.getMessage().substring(0, 1).toUpperCase() + e.getMessage().substring(1).toLowerCase();
				if (disabled.getSection("DisabledCommands").getKeys().contains(msg)) {
					for (String msgToSend : mi.getOldMessages("Main", "CommandAlreadyBlocked")) {
						HashMap<String, String> placeholders = new HashMap<>();
						placeholders.put("%c", e.getMessage());
						mi.sendMessage(p, Variables.translateVariables(msgToSend, p, placeholders));
					}
					lookingFor.remove(p.getUniqueId().toString());
					if (bossBar.containsKey(p.getUniqueId().toString()))
						BungeeMain.adventure().player(p).hideBossBar(bossBar.get(p.getUniqueId().toString()));
					bossBar.remove(p.getUniqueId().toString());
					partsHad.remove(p.getUniqueId().toString());
					e.setCancelled(true);
					return;
				}

				JsonObject object = partsHad.get(p.getUniqueId().toString());
				object.addProperty("command", e.getMessage());
				HashMap<String, String> placeholders = new HashMap<>();
				placeholders.put("%c", e.getMessage());
				BossBar bar = BossBar.bossBar(Variables.translateVariables(mi.getOldMessage("Main", "AddBossBar"),
						p, placeholders),1f, BossBar.Color.PURPLE, BossBar.Overlay.PROGRESS);
				BungeeCommandValueListener.bossBar.put(p.getUniqueId().toString(), bar);
				BungeeMain.adventure().player(p).showBossBar(bar);
				partsHad.put(p.getUniqueId().toString(), object);

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

				for (String msgToSend : mi.getOldMessages("Main", "AddServer")) {
					mi.sendMessage(p, Variables.translateVariables(msgToSend, p));
				}
				/*
				 * Send message to add servers. Allow for all.
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
					if (bossBar.containsKey(p.getUniqueId().toString()))
						BungeeMain.adventure().player(p).hideBossBar(bossBar.get(p.getUniqueId().toString()));
					bossBar.remove(p.getUniqueId().toString());
					partsHad.remove(p.getUniqueId().toString());
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

				if (BlockedCommands.addBlockedCommand(cmd, addcommand.getPermission(), addcommand.getMessage(), worlds, pCmds)) {
					Log.addLog(Universal.get().getMethods(), p.getName() + ": Added command /" + addcommand.getCommand() + " to disabled.yml with permission " + addcommand.getPermission()
							+ " and message " + addcommand.getMessage() + " in servers: " + addcommand.getWorlds() + ". When executed, it runs " + addcommand.getPlayerCommands()
							+ " as the player");
					for (String msgToSend : mi.getOldMessages("Main", "AddedCommandOutputBungee")) {
						HashMap<String, String> placeholders = new HashMap<>();
						placeholders.put("%c", addcommand.getCommand());
						placeholders.put("%p", addcommand.getPermission());
						placeholders.put("%m", addcommand.getMessage());
						placeholders.put("%w", addcommand.getWorlds());
						placeholders.put("%x", addcommand.getPlayerCommands());
						placeholders.put("%y", String.valueOf(addcommand.getConfirmation()));
						mi.sendMessage(p, Variables.translateVariables(msgToSend, p, placeholders));
					}
				} else {
					for (String msgToSend : mi.getOldMessages("Main", "CouldNotAddCommandToConfig")) {
						mi.sendMessage(p, Variables.translateVariables(msgToSend, p));
					}
				}
				lookingFor.remove(p.getUniqueId().toString());
				if (bossBar.containsKey(p.getUniqueId().toString()))
					BungeeMain.adventure().player(p).hideBossBar(bossBar.get(p.getUniqueId().toString()));
				bossBar.remove(p.getUniqueId().toString());
				partsHad.remove(p.getUniqueId().toString());
			}
			e.setCancelled(true);
			return;
		} else if (lookingFor.get(p.getUniqueId().toString()).equalsIgnoreCase("remove")) {
			if (!p.hasPermission("cb.remove")) {
				CommandBlockerCommand.noPermissions(mi, p);
				lookingFor.remove(p.getUniqueId().toString());
				partsHad.remove(p.getUniqueId().toString());
				if (bossBar.containsKey(p.getUniqueId().toString()))
					BungeeMain.adventure().player(p).hideBossBar(bossBar.get(p.getUniqueId().toString()));
				bossBar.remove(p.getUniqueId().toString());
				return;
			}

			if (!partsHad.get(p.getUniqueId().toString()).has("command")) {
				Configuration disabled = (Configuration) mi.getDisabledCommandsConfig();
				String message = e.getMessage().substring(0, 1).toUpperCase() + e.getMessage().substring(1).toLowerCase();
				if (!disabled.contains("DisabledCommands." + message)) {
					for (String msgToSend : mi.getOldMessages("Main", "UnblockCancelledBecauseNotBlocked")) {
						HashMap<String, String> placeholders = new HashMap<>();
						placeholders.put("%c", e.getMessage());
						mi.sendMessage(p, Variables.translateVariables(msgToSend, p, placeholders));
					}
					lookingFor.remove(p.getUniqueId().toString());
					partsHad.remove(p.getUniqueId().toString());
					if (bossBar.containsKey(p.getUniqueId().toString()))
						BungeeMain.adventure().player(p).hideBossBar(bossBar.get(p.getUniqueId().toString()));
					bossBar.remove(p.getUniqueId().toString());
					e.setCancelled(true);
					return;
				}
				JsonObject object = partsHad.get(p.getUniqueId().toString());
				object.addProperty("command", message);
				HashMap<String, String> placeholders = new HashMap<>();
				placeholders.put("%c", message);
				BossBar bar = BossBar.bossBar(Variables.translateVariables(mi.getOldMessage("Main", "RemoveBossBar"),
						p, placeholders),1f, BossBar.Color.PURPLE, BossBar.Overlay.PROGRESS);
				BungeeCommandValueListener.bossBar.put(p.getUniqueId().toString(), bar);
				BungeeMain.adventure().player(p).showBossBar(bar);
				partsHad.put(p.getUniqueId().toString(), object);

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
						BungeeMain.adventure().player(p).hideBossBar(bossBar.get(p.getUniqueId().toString()));
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
					BungeeMain.adventure().player(p).hideBossBar(bossBar.get(p.getUniqueId().toString()));
				bossBar.remove(p.getUniqueId().toString());
			}
		}
		e.setCancelled(true);
	}
	
	@EventHandler
	public void removeOnLeave(PlayerDisconnectEvent e) {
		lookingFor.remove(e.getPlayer().getUniqueId().toString());
		partsHad.remove(e.getPlayer().getUniqueId().toString());
		if (bossBar.containsKey(e.getPlayer().getUniqueId().toString()))
			BungeeMain.adventure().player(e.getPlayer()).hideBossBar(bossBar.get(e.getPlayer().getUniqueId().toString()));
		bossBar.remove(e.getPlayer().getUniqueId().toString());
	}
	
}
