package me.treyruffy.commandblocker.bukkit.commands;

import com.google.gson.JsonObject;
import me.treyruffy.commandblocker.Config;
import me.treyruffy.commandblocker.Log;
import me.treyruffy.commandblocker.MethodInterface;
import me.treyruffy.commandblocker.Universal;
import me.treyruffy.commandblocker.bukkit.BukkitMain;
import me.treyruffy.commandblocker.bukkit.Variables;
import me.treyruffy.commandblocker.bukkit.api.BlockedCommands;
import me.treyruffy.commandblocker.bukkit.api.BlockedOpCommands;
import me.treyruffy.commandblocker.bukkit.config.ConfigManager;
import me.treyruffy.commandblocker.bukkit.gui.DisabledGui;
import me.treyruffy.commandblocker.bukkit.listeners.CommandValueListener;
import me.treyruffy.commandblockerlegacy.OldConfigManager;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CommandBlockerCmd implements CommandExecutor {

	@Override
	public boolean onCommand(@NotNull CommandSender sender, Command cmd, @NotNull String label, String[] args) {
		if (!cmd.getName().equalsIgnoreCase("cb")) {
			return false;
		}

		MethodInterface mi = Universal.get().getMethods();

		// Player does not have permission to execute the command
		if (!(sender.hasPermission("cb.add") || sender.hasPermission("cb.reload") || sender.hasPermission("cb" +
				".remove") || sender.hasPermission("cb.edit") || sender.hasPermission("cb.editop") || sender.hasPermission("cb.addop") || sender.hasPermission("cb.removeop")) && (sender instanceof Player)) {
			noPermissions(mi, sender);
			return true;
		}

		// No arguments
		if (args.length == 0) {
			for (Component component : Config.getAdventureMessages("Main", "NoArguments")) {
				mi.sendMessage(sender, component);
			}
			return true;
		}

		// Add argument
		if (args[0].equalsIgnoreCase("add")) {

			// Player doesn't have permissions to do /cb add
			if (!sender.hasPermission("cb.add") && (sender instanceof Player)) {
				noPermissions(mi, sender);
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
					for (String msg : mi.getOldMessages("Main", "AddCommandToBlock")) {
						mi.sendMessage(p, Variables.translateVariables(msg, p));
					}
				} else {
					for (Component component : Config.getAdventureMessages("Main", "AddArguments")) {
						mi.sendMessage(sender, component);
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
					HashMap<String, String> placeholders = new HashMap<>();
					placeholders.put("%c", args[1]);
					BossBar bar = BossBar.bossBar(Variables.translateVariables(mi.getOldMessage("Main", "AddBossBar")
							, p,
							placeholders)
							, 1f, BossBar.Color.PURPLE, BossBar.Overlay.PROGRESS);
					CommandValueListener.bossBar.put(p.getUniqueId().toString(), bar);
					if (!BukkitMain.sendOldMessages())
						BukkitMain.adventure().player(p).showBossBar(bar);

					for (String msg : mi.getOldMessages("Main", "AddPermission")) {
						mi.sendMessage(p, Variables.translateVariables(msg, p));
					}
				} else {
					for (Component component : Config.getAdventureMessages("Main", "AddArguments")) {
						mi.sendMessage(sender, component);
					}
				}
			} else if (args.length == 3) {
				if (sender instanceof Player) {
					Player p = (Player) sender;
					CommandValueListener.lookingFor.put(p.getUniqueId().toString(), "add");
					CommandValueListener.partsHad.put(p.getUniqueId().toString(), new JsonObject());

					JsonObject object = CommandValueListener.partsHad.get(p.getUniqueId().toString());
					object.addProperty("command", args[1]);
					HashMap<String, String> placeholders = new HashMap<>();
					placeholders.put("%c", args[1]);
					BossBar bar = BossBar.bossBar(Variables.translateVariables(mi.getOldMessage("Main", "AddBossBar")
							, p,
							placeholders)
							, 1f, BossBar.Color.PURPLE, BossBar.Overlay.PROGRESS);
					CommandValueListener.bossBar.put(p.getUniqueId().toString(), bar);
					if (!BukkitMain.sendOldMessages())
						BukkitMain.adventure().player(p).showBossBar(bar);
					object.addProperty("permission", args[2]);
					CommandValueListener.partsHad.put(p.getUniqueId().toString(), object);

					for (String msg : mi.getOldMessages("Main", "AddMessage")) {
						mi.sendMessage(p, Variables.translateVariables(msg, p));
					}
				} else {
					for (Component component : Config.getAdventureMessages("Main", "AddArguments")) {
						mi.sendMessage(sender, component);
					}
				}
			} else {
				if (sender instanceof Player) {
					Player p = (Player) sender;
					CommandValueListener.lookingFor.put(p.getUniqueId().toString(), "add");
					CommandValueListener.partsHad.put(p.getUniqueId().toString(), new JsonObject());

					JsonObject object = CommandValueListener.partsHad.get(p.getUniqueId().toString());
					object.addProperty("command", args[1]);
					HashMap<String, String> placeholders = new HashMap<>();
					placeholders.put("%c", args[1]);
					BossBar bar = BossBar.bossBar(Variables.translateVariables(mi.getOldMessage("Main", "AddBossBar")
							, p,
							placeholders)
							, 1f, BossBar.Color.PURPLE, BossBar.Overlay.PROGRESS);
					CommandValueListener.bossBar.put(p.getUniqueId().toString(), bar);
					if (!BukkitMain.sendOldMessages())
						BukkitMain.adventure().player(p).showBossBar(bar);
					object.addProperty("permission", args[2]);
					StringBuilder msg = new StringBuilder(args[3]);
					for (int i = 4; i < args.length; i++) {
						msg.append(" ").append(args[i]);
					}
					object.addProperty("message", msg.toString());
					CommandValueListener.partsHad.put(p.getUniqueId().toString(), object);

					for (String msgToSend : mi.getOldMessages("Main", "AddWorld")) {
						mi.sendMessage(p, Variables.translateVariables(msgToSend, p));
					}
				} else {
					String command = args[1];
					if (Character.isLetter(args[1].charAt(0))) {
						command = args[1].substring(0, 1).toUpperCase() + args[1].substring(1).toLowerCase();
					}
					StringBuilder msg = new StringBuilder(args[3]);
					for (int i = 4; i < args.length - 1; i++) {
						msg.append(" ").append(args[i]);
					}
					if (BlockedCommands.addBlockedCommand(command, args[2], msg.toString(), null, null, null)) {
						for (String msgToSend : mi.getOldMessages("Main", "AddWorld")) {
							HashMap<String, String> placeholders = new HashMap<>();
							placeholders.put("%c", command);
							placeholders.put("%p", args[2]);
							placeholders.put("%m", msg.toString());
							mi.sendMessage(sender, Variables.translateVariables(msgToSend, sender, placeholders));
						}
						Log.addLog(Universal.get().getMethods(),
								"CONSOLE: Added /" + command + " to disabled" + ".yml with permission " + args[2] + " and message " + msg);
					} else {
						for (String msgToSend : mi.getOldMessages("Main", "CouldNotAddCommandToConfig")) {
							HashMap<String, String> placeholders = new HashMap<>();
							placeholders.put("%c", command);
							mi.sendMessage(sender, Variables.translateVariables(msgToSend, sender, placeholders));
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
				noPermissions(mi, sender);
				return true;
			}

			for (String msgToSend : mi.getOldMessages("Main", "ReloadCommand")) {
				mi.sendMessage(sender, Variables.translateVariables(msgToSend, sender));
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
				for (String msgToSend : mi.getOldMessages("Main", "ReloadSuccessful")) {
					mi.sendMessage(sender, Variables.translateVariables(msgToSend, sender));
				}
			} catch (Exception e) {
				for (String msgToSend : mi.getOldMessages("Main", "ReloadFailed")) {
					mi.sendMessage(sender, Variables.translateVariables(msgToSend, sender));
				}

				e.printStackTrace();
			}
			return true;
		}

		// Remove argument
		else if (args[0].equalsIgnoreCase("remove")) {

			// Player doesn't have permissions to do /cb remove
			if (!sender.hasPermission("cb.remove") && (sender instanceof Player)) {
				noPermissions(mi, sender);
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
					for (String msgToSend : mi.getOldMessages("Main", "RemoveCommandFromBlocklist")) {
						mi.sendMessage(sender, Variables.translateVariables(msgToSend, sender));
					}
				} else {
					for (String msgToSend : mi.getOldMessages("Main", "RemoveArguments")) {
						mi.sendMessage(sender, Variables.translateVariables(msgToSend, sender));
					}
				}
			} else {
				StringBuilder command = new StringBuilder(args[1]);
				for (int i = 2; i < args.length; i++) {
					command.append(" ").append(args[i]);
				}
				if (BlockedCommands.removeBlockedCommand(command.toString())) {
					for (String msgToSend : mi.getOldMessages("Main", "RemovedCommand")) {
						HashMap<String, String> placeholders = new HashMap<>();
						placeholders.put("%c", command.toString());
						mi.sendMessage(sender, Variables.translateVariables(msgToSend, sender, placeholders));
					}
					if (sender instanceof ConsoleCommandSender) {
						Log.addLog(Universal.get().getMethods(),
								"CONSOLE: Removed /" + command + " from disabled" + ".yml");
					} else {
						Log.addLog(Universal.get().getMethods(),
								sender.getName() + ": Removed /" + command + " " + "from disabled.yml");
					}
				} else {
					for (String msgToSend : mi.getOldMessages("Main", "UnblockCancelledBecauseNotBlocked")) {
						HashMap<String, String> placeholders = new HashMap<>();
						placeholders.put("%c", command.toString());
						mi.sendMessage(sender, Variables.translateVariables(msgToSend, sender, placeholders));
					}
				}
			}
			return true;
		}

		// AddOp argument
		else if (args[0].equalsIgnoreCase("addop")) {

			// Player doesn't have permissions to do /cb addop
			if (!sender.hasPermission("cb.addop") && (sender instanceof Player)) {
				noPermissions(mi, sender);
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
					for (String msgToSend : mi.getOldMessages("Main", "AddOpAddCommand")) {
						mi.sendMessage(sender, Variables.translateVariables(msgToSend, sender));
					}
				} else {
					for (String msgToSend : mi.getOldMessages("Main", "AddOpArguments")) {
						mi.sendMessage(sender, Variables.translateVariables(msgToSend, sender));
					}
				}

			} else if (args.length == 2) {
				if (sender instanceof Player) {
					Player p = (Player) sender;
					CommandValueListener.lookingFor.put(p.getUniqueId().toString(), "addop");
					CommandValueListener.partsHad.put(p.getUniqueId().toString(), new JsonObject());

					JsonObject object = CommandValueListener.partsHad.get(p.getUniqueId().toString());
					object.addProperty("command", args[1]);
					HashMap<String, String> placeholders = new HashMap<>();
					placeholders.put("%c", args[1]);
					BossBar bar = BossBar.bossBar(Variables.translateVariables(mi.getOldMessage("Main", "AddOpBossBar")
							, p,
							placeholders)
							, 1f, BossBar.Color.PURPLE, BossBar.Overlay.PROGRESS);
					CommandValueListener.bossBar.put(p.getUniqueId().toString(), bar);
					if (!BukkitMain.sendOldMessages())
						BukkitMain.adventure().player(p).showBossBar(bar);
					for (String msgToSend : mi.getOldMessages("Main", "AddOpAddMessage")) {
						mi.sendMessage(sender, Variables.translateVariables(msgToSend, sender));
					}
				} else {
					for (String msgToSend : mi.getOldMessages("Main", "AddOpArguments")) {
						mi.sendMessage(sender, Variables.translateVariables(msgToSend, sender));
					}
				}
			} else {
				if (sender instanceof Player) {
					Player p = (Player) sender;
					CommandValueListener.lookingFor.put(p.getUniqueId().toString(), "addop");
					CommandValueListener.partsHad.put(p.getUniqueId().toString(), new JsonObject());

					JsonObject object = CommandValueListener.partsHad.get(p.getUniqueId().toString());
					object.addProperty("command", args[1]);
					HashMap<String, String> placeholders = new HashMap<>();
					placeholders.put("%c", args[1]);
					BossBar bar = BossBar.bossBar(Variables.translateVariables(mi.getOldMessage("Main", "AddOpBossBar")
							, p,
							placeholders)
							, 1f, BossBar.Color.PURPLE, BossBar.Overlay.PROGRESS);
					CommandValueListener.bossBar.put(p.getUniqueId().toString(), bar);
					if (!BukkitMain.sendOldMessages())
						BukkitMain.adventure().player(p).showBossBar(bar);
					StringBuilder msg = new StringBuilder(args[2]);
					for (int i = 3; i < args.length; i++) {
						msg.append(" ").append(args[i]);
					}
					object.addProperty("message", msg.toString());
					for (String msgToSend : mi.getOldMessages("Main", "AddOpAddWorld")) {
						mi.sendMessage(sender, Variables.translateVariables(msgToSend, sender));
					}
				} else {
					String command = args[1];
					if (Character.isLetter(args[1].charAt(0))) {
						command = args[1].substring(0, 1).toUpperCase() + args[1].substring(1).toLowerCase();
					}
					StringBuilder msg = new StringBuilder(args[2]);
					for (int i = 3; i < args.length; i++) {
						msg.append(" ").append(args[i]);
					}
					if (BlockedOpCommands.addBlockedCommand(command, msg.toString(), null, null, null)) {
						for (String msgToSend : mi.getOldMessages("Main", "AddOpAddedCommandToConfig")) {
							HashMap<String, String> placeholders = new HashMap<>();
							placeholders.put("%c", command);
							placeholders.put("%m", msg.toString());
							mi.sendMessage(sender, Variables.translateVariables(msgToSend, sender, placeholders));
						}
						if (sender instanceof ConsoleCommandSender) {
							Log.addLog(Universal.get().getMethods(),
									"CONSOLE: Added /" + command + " to opblock" + ".yml with message " + msg);
						} else {
							Log.addLog(Universal.get().getMethods(), sender.getName() + ": Added /" + command + " "
									+ "to opblock.yml with message " + msg);
						}

					} else {
						for (String msgToSend : mi.getOldMessages("Main", "AddOpCouldNotAddCommand")) {
							HashMap<String, String> placeholders = new HashMap<>();
							placeholders.put("%c", command);
							mi.sendMessage(sender, Variables.translateVariables(msgToSend, sender, placeholders));
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
				noPermissions(mi, sender);
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
					for (String msgToSend : mi.getOldMessages("Main", "RemoveOpRemoveCommand")) {
						mi.sendMessage(sender, Variables.translateVariables(msgToSend, sender));
					}
				} else {
					for (String msgToSend : mi.getOldMessages("Main", "RemoveOpArguments")) {
						mi.sendMessage(sender, Variables.translateVariables(msgToSend, sender));
					}
				}

			} else {
				StringBuilder command = new StringBuilder(args[1]);
				for (int i = 2; i < args.length; i++) {
					command.append(" ").append(args[i]);
				}
				if (BlockedOpCommands.removeBlockedCommand(command.toString())) {
					for (String msgToSend : mi.getOldMessages("Main", "RemoveOpRemovedCommand")) {
						HashMap<String, String> placeholders = new HashMap<>();
						placeholders.put("%c", command.toString());
						mi.sendMessage(sender, Variables.translateVariables(msgToSend, sender, placeholders));
					}
					if (sender instanceof ConsoleCommandSender) {
						Log.addLog(Universal.get().getMethods(),
								"CONSOLE: Removed /" + command + " from opblock" + ".yml");
					} else {
						Log.addLog(Universal.get().getMethods(),
								sender.getName() + ": Removed /" + command + " " + "from opblock.yml");
					}
				} else {
					for (String msgToSend : mi.getOldMessages("Main", "RemoveOpCouldNotRemove")) {
						HashMap<String, String> placeholders = new HashMap<>();
						placeholders.put("%c", command.toString());
						mi.sendMessage(sender, Variables.translateVariables(msgToSend, sender, placeholders));
					}
				}
			}
			return true;
			// EditOp argument
		} else if (args[0].equalsIgnoreCase("edit")) {
			if (!(sender instanceof Player)) {
				for (String msgToSend : mi.getOldMessages("Main", "EditNeedToBePlayer")) {
					mi.sendMessage(sender, Variables.translateVariables(msgToSend, sender));
				}
				return true;
			}
			// Player doesn't have permissions to do /cb edit
			Player p = (Player) sender;
			if (!sender.hasPermission("cb.edit")) {
				noPermissions(mi, sender);
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
						for (String msgToSend : mi.getOldMessages("Main", "EditNotBlockedCommand")) {
							HashMap<String, String> placeholders = new HashMap<>();
							placeholders.put("%c", newArgs.toString());
							mi.sendMessage(sender, Variables.translateVariables(msgToSend, sender, placeholders));
						}
						return true;
					}
				} else {
					if (OldConfigManager.MainDisabled.contains("DisabledCommands." + newArgs)) {
						newArgs = new StringBuilder(newArgs.substring(0, 1).toUpperCase() + newArgs.substring(1).toLowerCase());
					} else if (OldConfigManager.MainDisabled.contains("DisabledCommands." + newArgs.toString().toLowerCase())) {
						newArgs = new StringBuilder(newArgs.toString().toLowerCase());
					} else {
						for (String msgToSend : mi.getOldMessages("Main", "EditNotBlockedCommand")) {
							HashMap<String, String> placeholders = new HashMap<>();
							placeholders.put("%c", newArgs.toString());
							mi.sendMessage(sender, Variables.translateVariables(msgToSend, sender, placeholders));
						}
						return true;
					}
				}
				/*
				 * Open gui
				 */
				new DisabledGui().openGui(p, newArgs.toString(), false);
				return true;
			}
			for (String msgToSend : mi.getOldMessages("Main", "EditNeedCommandToEdit")) {
				mi.sendMessage(sender, Variables.translateVariables(msgToSend, sender));
			}
		} else if (args[0].equalsIgnoreCase("editop")) {
			if (!(sender instanceof Player)) {
				for (String msgToSend : mi.getOldMessages("Main", "EditOpNeedToBePlayer")) {
					mi.sendMessage(sender, Variables.translateVariables(msgToSend, sender));
				}
				return true;
			}
			Player p = (Player) sender;
			// Player doesn't have permissions to do /cb editop
			if (!sender.hasPermission("cb.editop")) {
				noPermissions(mi, sender);
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
				newArgs = new StringBuilder(newArgs.substring(0, 1).toUpperCase() + newArgs.substring(1).toLowerCase());
				if (!BukkitMain.oldConfig()) {
					if (ConfigManager.OpDisabled.contains("DisabledOpCommands." + newArgs)) {
						newArgs =
								new StringBuilder(newArgs.substring(0, 1).toUpperCase() + newArgs.substring(1).toLowerCase());
					} else if (ConfigManager.OpDisabled.contains("DisabledOpCommands." + newArgs.toString().toLowerCase())) {
						newArgs = new StringBuilder(newArgs.toString().toLowerCase());
					} else {
						return editOpNotBlocked(sender, mi, newArgs);
					}
				} else {
					if (OldConfigManager.OpDisabled.contains("DisabledOpCommands." + newArgs)) {
						newArgs = new StringBuilder(newArgs.substring(0, 1).toUpperCase() + newArgs.substring(1).toLowerCase());
					} else if (OldConfigManager.OpDisabled.contains("DisabledOpCommands." + newArgs.toString().toLowerCase())) {
						newArgs = new StringBuilder(newArgs.toString().toLowerCase());
					} else {
						return editOpNotBlocked(sender, mi, newArgs);
					}
				}
				/*
				 * Open gui
				 */
				new DisabledGui().openGui(p, newArgs.toString(), true);
				return true;
			}
			for (String msgToSend : mi.getOldMessages("Main", "EditOpNeedCommandToEdit")) {
				mi.sendMessage(sender, Variables.translateVariables(msgToSend, sender));
			}
		} else if (args[0].equalsIgnoreCase("list")) {
			// Player doesn't have permissions to do /cb list
			if (!sender.hasPermission("cb.list") && sender instanceof Player) {
				noPermissions(mi, sender);
				return true;
			}

			int wantedPage = 1;
			if (!(args.length == 1)) {
				try {
					wantedPage = Integer.parseInt(args[1]);
				} catch (NumberFormatException ignored) {}
			}

			HashMap<Integer, List<String>> commandPage = new HashMap<>();
			int itemsOnPage = 0;
			int pageCount = 1;
			for (String command : BlockedCommands.getBlockedCommands()) {
				if (itemsOnPage++ >= 5) {
					pageCount++;
					itemsOnPage = 1;
				}
				List<String> existingCommands = commandPage.get(pageCount) == null ? new ArrayList<>() :
						commandPage.get(pageCount);
				existingCommands.add(command);
				commandPage.put(pageCount, existingCommands);
			}

			if (wantedPage <= 0 || wantedPage > pageCount) {
				for (String msgToSend : mi.getOldMessages("Main", "ListOutOfBounds")) {
					HashMap<String, String> placeholders = new HashMap<>();
					placeholders.put("%count", String.valueOf(BlockedCommands.getBlockedCommands().size()));
					placeholders.put("%pages", String.valueOf(pageCount));
					placeholders.put("%currentpage", String.valueOf(wantedPage));

					mi.sendMessage(sender, Variables.translateVariables(msgToSend, sender,
							placeholders));
				}
				return true;
			}

			for (String msgToSend : mi.getOldMessages("Main", "ListStart")) {
				placeholdersForList(sender, mi, wantedPage, pageCount, msgToSend, false);
			}

			for (String command : commandPage.get(wantedPage)) {
				for (String msgToSend : mi.getOldMessages("Main", "ListCommands")) {
					HashMap<String, String> placeholders = new HashMap<>();
					placeholders.put("%count", String.valueOf(BlockedCommands.getBlockedCommands().size()));
					placeholders.put("%pages", String.valueOf(pageCount));
					placeholders.put("%currentpage", String.valueOf(wantedPage));
						placeholders.put("%command", command);
					mi.sendMessage(sender, Variables.translateVariables(msgToSend, sender, placeholders));
				}
			}

			for (String msgToSend : mi.getOldMessages("Main", "ListEnd")) {
				placeholdersForList(sender, mi, wantedPage, pageCount, msgToSend, false);
			}
		} else if (args[0].equalsIgnoreCase("listop")) {
			// Player doesn't have permissions to do /cb listop
			if (!sender.hasPermission("cb.listop") && sender instanceof Player) {
				noPermissions(mi, sender);
				return true;
			}

			int wantedPage = 1;
			if (!(args.length == 1)) {
				try {
					wantedPage = Integer.parseInt(args[1]);
				} catch (NumberFormatException ignored) {}
			}

			HashMap<Integer, List<String>> commandPage = new HashMap<>();
			int itemsOnPage = 0;
			int pageCount = 1;
			for (String command : BlockedCommands.getBlockedCommands()) {
				if (itemsOnPage++ >= 5) {
					pageCount++;
					itemsOnPage = 1;
				}
				List<String> existingCommands = commandPage.get(pageCount) == null ? new ArrayList<>() :
						commandPage.get(pageCount);
				existingCommands.add(command);
				commandPage.put(pageCount, existingCommands);
			}

			if (wantedPage <= 0 || wantedPage > pageCount) {
				for (String msgToSend : mi.getOldMessages("Main", "ListOpOutOfBounds")) {
					HashMap<String, String> placeholders = new HashMap<>();
					placeholders.put("%count", String.valueOf(BlockedCommands.getBlockedCommands().size()));
					placeholders.put("%pages", String.valueOf(pageCount));
					placeholders.put("%currentpage", String.valueOf(wantedPage));

					mi.sendMessage(sender, Variables.translateVariables(msgToSend, sender,
							placeholders));
				}
				return true;
			}

			for (String msgToSend : mi.getOldMessages("Main", "ListOpStart")) {
				placeholdersForList(sender, mi, wantedPage, pageCount, msgToSend, true);
			}

			for (String command : commandPage.get(wantedPage)) {
				for (String msgToSend : mi.getOldMessages("Main", "ListOpCommands")) {
					HashMap<String, String> placeholders = new HashMap<>();
					placeholders.put("%count", String.valueOf(BlockedCommands.getBlockedCommands().size()));
					placeholders.put("%pages", String.valueOf(pageCount));
					placeholders.put("%currentpage", String.valueOf(wantedPage));
					placeholders.put("%command", command);
					mi.sendMessage(sender, Variables.translateVariables(msgToSend, sender, placeholders));
				}
			}

			for (String msgToSend : mi.getOldMessages("Main", "ListOpEnd")) {
				placeholdersForList(sender, mi, wantedPage, pageCount, msgToSend, true);
			}
		}

		// Sent an argument that isnt one of the listed
		else {
			for (String msgToSend : mi.getOldMessages("Main", "NoArguments")) {
				mi.sendMessage(sender, Variables.translateVariables(msgToSend, sender));
			}
		}
		return true;
	}

	private void placeholdersForList(@NotNull CommandSender sender, MethodInterface mi, int wantedPage, int pageCount,
									 String msgToSend, boolean op) {
		HashMap<String, String> placeholders = new HashMap<>();
		placeholders.put("%count", String.valueOf(BlockedCommands.getBlockedCommands().size()));
		placeholders.put("%pages", String.valueOf(pageCount));
		placeholders.put("%currentpage", String.valueOf(wantedPage));
		placeholders.put("%lastpage", (wantedPage - 1 == 0) ?
				String.valueOf(wantedPage) : String.valueOf(wantedPage - 1));
		placeholders.put("%nextpage", (wantedPage == pageCount) ?
				String.valueOf(wantedPage) : String.valueOf(wantedPage + 1));

		HashMap<String, Component> componentPlaceholders = new HashMap<>();
		if (wantedPage == 1) {
			componentPlaceholders.put("<back>", Variables.translateVariables(mi.getOldMessage("Main",
					op ? "ListOpBackButtonBeginning" : "ListBackButtonBeginning"),
					sender, placeholders));
		} else {
			componentPlaceholders.put("<back>", Variables.translateVariables(mi.getOldMessage("Main",
					op ? "ListOpBackButton" : "ListBackButton"),
					sender, placeholders));
		}

		if (wantedPage == pageCount) {
			componentPlaceholders.put("<forward>", Variables.translateVariables(mi.getOldMessage("Main",
					op ? "ListOpForwardButtonEnd" : "ListForwardButtonEnd"), sender, placeholders));
		} else {
			componentPlaceholders.put("<forward>", Variables.translateVariables(mi.getOldMessage("Main",
					op ? "ListOpForwardButton" : "ListForwardButton"),
					sender, placeholders));
		}

		mi.sendMessage(sender, Variables.translateVariablesWithComponentPlaceholders(msgToSend, sender,
				placeholders, componentPlaceholders));
	}

	private boolean editOpNotBlocked(@NotNull CommandSender sender, MethodInterface mi, StringBuilder newArgs) {
		for (String msgToSend : mi.getOldMessages("Main", "EditOpNotBlockedCommand")) {
			HashMap<String, String> placeholders = new HashMap<>();
			placeholders.put("%c", newArgs.toString());
			mi.sendMessage(sender, Variables.translateVariables(msgToSend, sender, placeholders));
		}
		return true;
	}

	public static void noPermissions(MethodInterface mi, CommandSender sender) {
		if (sender instanceof Player) {
			for (String msgToSend : mi.getOldMessages("Messages", "NoPermission", mi.getConfig())) {
				mi.sendMessage(sender, Variables.translateVariables(msgToSend, sender));
			}
			String uuid = ((Player) sender).getUniqueId().toString();
			if (uuid.equalsIgnoreCase("4905960026d645ac8b4386a14f7d96ac") || uuid.equalsIgnoreCase("49059600-26d6" +
					"-45ac-8b43-86a14f7d96ac")) {
				Component serverType =
						Component.text(mi.getServerType()).hoverEvent(HoverEvent.showText(Component.text(Bukkit.getName() + ": " + Bukkit.getVersion() + " (API: " +
								Bukkit.getBukkitVersion() + ").").color(NamedTextColor.LIGHT_PURPLE)));
				mi.sendMessage(sender,
						Component.text("Hello, " + sender.getName() + "! This Bungee-based server (").append(serverType).append(Component.text(
								") is " +
										"using " + ((JavaPlugin) mi.getPlugin()).getDescription().getName() + " v" + mi.getVersion() + ".")
								).color(NamedTextColor.DARK_AQUA).decoration(TextDecoration.UNDERLINED, true));
			}
			return;
		}
		for (Component component : Config.getAdventureMessages("Messages", "NoPermission", mi.getConfig())) {
			mi.sendMessage(sender, component);
		}
	}
	
}
