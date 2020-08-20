package me.treyruffy.commandblocker.bungeecord.listeners;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import me.treyruffy.commandblocker.Log;
import me.treyruffy.commandblocker.Universal;
import me.treyruffy.commandblocker.bungeecord.api.BlockedCommands;
import me.treyruffy.commandblocker.bungeecord.config.BungeeConfigManager;
import me.treyruffy.commandblocker.bungeecord.config.Messages;
import me.treyruffy.commandblocker.json.AddCommand;
import me.treyruffy.commandblocker.json.RemoveCommand;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class BungeeCommandValueListener implements Listener {

	public static HashMap<String, String> lookingFor = new HashMap<>();
	public static HashMap<String, JsonObject> partsHad = new HashMap<>();
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onChat(ChatEvent e) {
		if (!(e.getSender() instanceof ProxiedPlayer)) {
			return;
		}
		ProxiedPlayer p = (ProxiedPlayer) e.getSender();
		if (!(p.hasPermission("cb.add") || p.hasPermission("cb.remove"))) {
			return;
		}

		if (!lookingFor.containsKey(p.getUniqueId().toString())) {
			return;
		}
		if (e.getMessage().startsWith("/")) {
			String message = e.getMessage().substring(1);

			String msg = message.substring(0, 1).toUpperCase() + message.substring(1).toLowerCase();
			if (lookingFor.get(p.getUniqueId().toString()).equalsIgnoreCase("add")) {
				
				if (!p.hasPermission("cb.add")) {
					p.sendMessage(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&',
							BungeeConfigManager.getConfig().getString("Messages" + ".NoPermission"))));
					return;
				}
				
				if (!partsHad.get(p.getUniqueId().toString()).has("command")) {
					Configuration disabled = BungeeConfigManager.getDisabled();

					if (disabled.getSection("DisabledCommands").getKeys().contains(msg)) {
						for (String message1 : Messages.getMessages("Main", "CommandAlreadyBlocked")) {
							p.sendMessage(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&',
									message1.replace("%c", msg))));
						}
						lookingFor.remove(p.getUniqueId().toString());
						partsHad.remove(p.getUniqueId().toString());
						e.setCancelled(true);
						return;
					}

					JsonObject object = partsHad.get(p.getUniqueId().toString());
					object.addProperty("command", message);
					partsHad.put(p.getUniqueId().toString(), object);

					for (String message1 : Messages.getMessages("Main", "AddPermission")) {
						p.sendMessage(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&',
								message1)));
					}
					/*
					 * Send message to add a permission
					 */

				} else if (!partsHad.get(p.getUniqueId().toString()).has("permission")) {

					/*
					 * Send message that you can't use a command
					 */
					for (String message1 : Messages.getMessages("Main", "CantUseCommand")) {
						p.sendMessage(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&',
								message1)));
					}

					e.setCancelled(true);
					return;
				} else if (!partsHad.get(p.getUniqueId().toString()).has("message")) {

					/*
					 * Send message that you can't use a command
					 */
					for (String message1 : Messages.getMessages("Main", "CantUseCommand")) {
						p.sendMessage(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&',
								message1)));
					}

					e.setCancelled(true);
					return;
				} else if (!partsHad.get(p.getUniqueId().toString()).has("worlds")) {

					/*
					 * Send message that you can't use a command
					 */
					for (String message1 : Messages.getMessages("Main", "CantUseCommand")) {
						p.sendMessage(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&',
								message1)));
					}

					e.setCancelled(true);
					return;
				} else if (!partsHad.get(p.getUniqueId().toString()).has("playercommands")) {
					JsonObject object = partsHad.get(p.getUniqueId().toString());
					object.addProperty("playercommands", message);
					partsHad.put(p.getUniqueId().toString(), object);

					for (String message1 : Messages.getMessages("Main", "Confirmation")) {
						p.sendMessage(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&',
								message1)));
					}
					/*
					 * Send message to add a confirmation
					 */
				} else if (!partsHad.get(p.getUniqueId().toString()).has("confirmation")) {
					/*
					 * Send message that you can't use a command
					 */
					for (String message1 : Messages.getMessages("Main", "CantUseCommand")) {
						p.sendMessage(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&',
								message1)));
					}

					e.setCancelled(true);
					return;
				}
				e.setCancelled(true);
			} 
			if (lookingFor.get(p.getUniqueId().toString()).equalsIgnoreCase("remove")) {
				
				if (!p.hasPermission("cb.remove")) {
					p.sendMessage(TextComponent.fromLegacyText("no perms"));
					return;
				}
				
				if (!partsHad.get(p.getUniqueId().toString()).has("command")) {
					Configuration disabled = BungeeConfigManager.getDisabled();
					if (!disabled.getSection("DisabledCommands").getKeys().contains(msg)) {
						for (String message1 : Messages.getMessages("Main", "RemoveCommandFromBlocklist")) {
							p.sendMessage(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&',
									message1)));
						}
						lookingFor.remove(p.getUniqueId().toString());
						partsHad.remove(p.getUniqueId().toString());
						e.setCancelled(true);
						return;
					}
					JsonObject object = partsHad.get(p.getUniqueId().toString());
					object.addProperty("command", message);
					partsHad.put(p.getUniqueId().toString(), object);

					for (String message1 : Messages.getMessages("Main", "RemoveCommandQuestion")) {
						p.sendMessage(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&',
								message1.replace("%c", message))));
					}


				} else if (!partsHad.get(p.getUniqueId().toString()).has("confirmation")) {
					/*
					 * Send message that you can't use a command
					 */
					for (String message1 : Messages.getMessages("Main", "CantUseCommand")) {
						p.sendMessage(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&',
								message1)));
					}

					e.setCancelled(true);
					return;
				}
				e.setCancelled(true);
			}
			return;
		}
		
		if (e.getMessage().equalsIgnoreCase("cancel")) {
			lookingFor.remove(p.getUniqueId().toString());
			partsHad.remove(p.getUniqueId().toString());
			for (String message1 : Messages.getMessages("Main", "Cancelled")) {
				p.sendMessage(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', message1)));
			}
			e.setCancelled(true);
			return;
		}
		
		if (lookingFor.get(p.getUniqueId().toString()).equalsIgnoreCase("add")) {
			
			if (!p.hasPermission("cb.add")) {
				p.sendMessage(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&',
						BungeeConfigManager.getConfig().getString("Messages" + ".NoPermission"))));
				return;
			}
			Configuration disabled = BungeeConfigManager.getDisabled();
			if (!partsHad.get(p.getUniqueId().toString()).has("command")) {
				String msg = e.getMessage().substring(0, 1).toUpperCase() + e.getMessage().substring(1).toLowerCase();
				if (disabled.getSection("DisabledCommands").getKeys().contains(msg)) {
					for (String message1 : Messages.getMessages("Main", "CommandAlreadyBlocked")) {
						p.sendMessage(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&',
								message1.replace("%c", e.getMessage()))));
					}
					lookingFor.remove(p.getUniqueId().toString());
					partsHad.remove(p.getUniqueId().toString());
					e.setCancelled(true);
					return;
				}

				JsonObject object = partsHad.get(p.getUniqueId().toString());
				object.addProperty("command", e.getMessage());
				partsHad.put(p.getUniqueId().toString(), object);

				for (String message1 : Messages.getMessages("Main", "AddPermission")) {
					p.sendMessage(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', message1)));
				}
				/*
				 * Send message to add a permission
				 */

			} else if (!partsHad.get(p.getUniqueId().toString()).has("permission")) {
				JsonObject object = partsHad.get(p.getUniqueId().toString());
				object.addProperty("permission", e.getMessage());
				partsHad.put(p.getUniqueId().toString(), object);

				for (String message1 : Messages.getMessages("Main", "AddMessage")) {
					p.sendMessage(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', message1)));
				}
				/*
				 * Send message to add a message
				 */

			} else if (!partsHad.get(p.getUniqueId().toString()).has("message")) {
				JsonObject object = partsHad.get(p.getUniqueId().toString());
				object.addProperty("message", e.getMessage());
				partsHad.put(p.getUniqueId().toString(), object);

				for (String message1 : Messages.getMessages("Main", "AddServer")) {
					p.sendMessage(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', message1)));
				}
				/*
				 * Send message to add worlds. Allow for all.
				 */

			} else if (!partsHad.get(p.getUniqueId().toString()).has("worlds")) {
				JsonObject object = partsHad.get(p.getUniqueId().toString());
				object.addProperty("worlds", e.getMessage());
				partsHad.put(p.getUniqueId().toString(), object);

				for (String message1 : Messages.getMessages("Main", "AddPlayerCommand")) {
					p.sendMessage(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', message1)));
				}
				/*
				 * Send message to add player commands. Allow for none.
				 */

			} else if (!partsHad.get(p.getUniqueId().toString()).has("playercommands")) {
				JsonObject object = partsHad.get(p.getUniqueId().toString());
				object.addProperty("playercommands", e.getMessage());
				partsHad.put(p.getUniqueId().toString(), object);

				for (String message1 : Messages.getMessages("Main", "AddConfirmation")) {
					p.sendMessage(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', message1)));
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
					for (String message1 : Messages.getMessages("Main", "CanOnlyBeYesOrNo")) {
						p.sendMessage(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&',
								message1)));
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
					for (String message1 : Messages.getMessages("Main", "Cancelled")) {
						p.sendMessage(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&',
								message1)));
					}
					e.setCancelled(true);
					return;
				}
				String cmd =
						addcommand.getCommand().substring(0, 1).toUpperCase() + addcommand.getCommand().substring(1).toLowerCase();

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
				
				Log.addLog(Universal.get().getMethods(), p.getName() + ": Added command /" + addcommand.getCommand() + " to disabled.yml with permission " + addcommand.getPermission() 
					+ " and message " + addcommand.getMessage() + " in servers: " + addcommand.getWorlds() + ". When executed, it runs " + addcommand.getPlayerCommands() 
					+ " as the player");
				
				if (BlockedCommands.addBlockedCommand(cmd, addcommand.getPermission(), addcommand.getMessage(), worlds, pCmds)) {
					for (String message1 : Messages.getMessages("Main", "AddedCommandOutputBungee")) {
						p.sendMessage(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&',
								message1.replace("%c", addcommand.getCommand()).replace("%p",
										addcommand.getPermission()).replace("%m", addcommand.getMessage()).replace("%s"
										, addcommand.getWorlds()).replace("%x", addcommand.getPlayerCommands()).replace("%y", addcommand.getConfirmation().toString()))));
					}
				} else {
					for (String message1 : Messages.getMessages("Main", "CouldNotAddCommandToConfig")) {
						p.sendMessage(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&',
								message1)));
					}
				}
				lookingFor.remove(p.getUniqueId().toString());
				partsHad.remove(p.getUniqueId().toString());
			}
			e.setCancelled(true);
		} else if (lookingFor.get(p.getUniqueId().toString()).equalsIgnoreCase("remove")) {
			if (!p.hasPermission("cb.remove")) {
				p.sendMessage(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&',
						BungeeConfigManager.getConfig().getString("Messages" + ".NoPermission"))));
				return;
			}

			if (!partsHad.get(p.getUniqueId().toString()).has("command")) {
				Configuration disabled = BungeeConfigManager.getDisabled();
				String message =
						e.getMessage().substring(0, 1).toUpperCase() + e.getMessage().substring(1).toLowerCase();
				if (!disabled.getSection("DisabledCommands").getKeys().contains(message)) {
					for (String message1 : Messages.getMessages("Main", "UnblockCancelledBecauseNotBlocked")) {
						p.sendMessage(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&',
								message1)));
					}
					lookingFor.remove(p.getUniqueId().toString());
					partsHad.remove(p.getUniqueId().toString());
					e.setCancelled(true);
					return;
				}
				JsonObject object = partsHad.get(p.getUniqueId().toString());
				object.addProperty("command", message);
				partsHad.put(p.getUniqueId().toString(), object);

				for (String message1 : Messages.getMessages("Main", "RemoveCommandQuestion")) {
					p.sendMessage(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&',
							message1.replace("%c", e.getMessage()))));
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
					for (String message1 : Messages.getMessages("Main", "CanOnlyBeYesOrNo")) {
						p.sendMessage(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&',
								message1)));
					}
					e.setCancelled(true);
					return;
				}
				
				partsHad.put(p.getUniqueId().toString(), object);
				
				Gson gson = new Gson();
				RemoveCommand removecommand = gson.fromJson(partsHad.get(p.getUniqueId().toString()), RemoveCommand.class);
				if (!removecommand.getConfirmation()) {
					lookingFor.remove(p.getUniqueId().toString());
					partsHad.remove(p.getUniqueId().toString());
					for (String message1 : Messages.getMessages("Main", "Cancelled")) {
						p.sendMessage(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&',
								message1)));
					}
					e.setCancelled(true);
					return;
				}

				Log.addLog(Universal.get().getMethods(),
						p.getName() + ": Removed command /" + removecommand.getCommand() + " in disabled.yml");

				if (BlockedCommands.removeBlockedCommand(removecommand.getCommand())) {
					for (String message1 : Messages.getMessages("Main", "RemovedCommandOutput")) {
						p.sendMessage(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&',
								message1.replace("%c", removecommand.getCommand()).replace("%y",
										removecommand.getConfirmation().toString()))));
					}
				} else {
					for (String message1 : Messages.getMessages("Main", "UnblockCancelledBecauseNotBlocked")) {
						p.sendMessage(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&',
								message1.replace("%c", removecommand.getCommand()))));
					}
				}
					
				partsHad.remove(p.getUniqueId().toString());
				lookingFor.remove(p.getUniqueId().toString());
			}
		}
		e.setCancelled(true);
	}
	
	@EventHandler
	public void removeOnLeave(PlayerDisconnectEvent e) {
		lookingFor.remove(e.getPlayer().getUniqueId().toString());
		partsHad.remove(e.getPlayer().getUniqueId().toString());
	}
	
}
