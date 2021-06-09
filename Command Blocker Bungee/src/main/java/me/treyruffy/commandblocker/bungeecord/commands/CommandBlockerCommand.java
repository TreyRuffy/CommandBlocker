package me.treyruffy.commandblocker.bungeecord.commands;

import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import me.treyruffy.commandblocker.Config;
import me.treyruffy.commandblocker.Log;
import me.treyruffy.commandblocker.MethodInterface;
import me.treyruffy.commandblocker.Universal;
import me.treyruffy.commandblocker.bungeecord.BungeeMain;
import me.treyruffy.commandblocker.bungeecord.Variables;
import me.treyruffy.commandblocker.bungeecord.api.BlockedCommands;
import me.treyruffy.commandblocker.bungeecord.config.BungeeConfigManager;
import me.treyruffy.commandblocker.bungeecord.listeners.BungeeCommandValueListener;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.TabExecutor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class CommandBlockerCommand extends Command implements TabExecutor {
	
	public CommandBlockerCommand() {
    	super("cb", "", "commandblockerbungee", "commandblocker", "commandblock");
    }
	
    @Override
	public void execute(CommandSender sender, String[] args) {

		MethodInterface mi = Universal.get().getMethods();

		// Player does not have permission to execute the command
    	if ((!(sender.hasPermission("cb.add") || sender.hasPermission("cb.reload") || sender.hasPermission("cb.remove")) && (sender instanceof ProxiedPlayer))) {
			noPermissions(mi, sender);
			return;
		}

    	// No arguments
		if (args.length == 0) {
			for (Component component : Config.getAdventureMessages("Main", "BungeeNoArguments")) {
				mi.sendMessage(sender, component);
			}
			return;
		}

		// Add argument
		if (args[0].equalsIgnoreCase("add")) {

			// Player doesn't have permissions to do /cb add
			if (!sender.hasPermission("cb.add") && (sender instanceof ProxiedPlayer)) {
				noPermissions(mi, sender);
				return;
			}

			if (args.length == 1) {

				if (sender instanceof ProxiedPlayer) {
					ProxiedPlayer p = (ProxiedPlayer) sender;
					BungeeCommandValueListener.lookingFor.put(p.getUniqueId().toString(), "add");
					BungeeCommandValueListener.partsHad.put(p.getUniqueId().toString(), new JsonObject());
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
				if (sender instanceof ProxiedPlayer) {
					ProxiedPlayer p = (ProxiedPlayer) sender;
					BungeeCommandValueListener.lookingFor.put(p.getUniqueId().toString(), "add");
					BungeeCommandValueListener.partsHad.put(p.getUniqueId().toString(), new JsonObject());

					JsonObject object = BungeeCommandValueListener.partsHad.get(p.getUniqueId().toString());
					object.addProperty("command", args[1]);
					HashMap<String, String> placeholders = new HashMap<>();
					placeholders.put("%c", args[1]);
					BossBar bar = BossBar.bossBar(Variables.translateVariables(mi.getOldMessage("Main", "AddBossBar")
							, p, placeholders),1f, BossBar.Color.PURPLE, BossBar.Overlay.PROGRESS);
					BungeeCommandValueListener.bossBar.put(p.getUniqueId().toString(), bar);
					BungeeMain.adventure().player(p).showBossBar(bar);
					BungeeCommandValueListener.partsHad.put(p.getUniqueId().toString(), object);

					for (String msg : mi.getOldMessages("Main", "AddPermission")) {
						mi.sendMessage(p, Variables.translateVariables(msg, p));
					}
				} else {
					for (Component component : Config.getAdventureMessages("Main", "AddArguments")) {
						mi.sendMessage(sender, component);
					}
				}
			} else if (args.length == 3) {
				if (sender instanceof ProxiedPlayer) {
					ProxiedPlayer p = (ProxiedPlayer) sender;
					BungeeCommandValueListener.lookingFor.put(p.getUniqueId().toString(), "add");
					BungeeCommandValueListener.partsHad.put(p.getUniqueId().toString(), new JsonObject());

					JsonObject object = BungeeCommandValueListener.partsHad.get(p.getUniqueId().toString());
					object.addProperty("command", args[1]);
					HashMap<String, String> placeholders = new HashMap<>();
					placeholders.put("%c", args[1]);
					BossBar bar = BossBar.bossBar(Variables.translateVariables(mi.getOldMessage("Main", "AddBossBar")
							, p, placeholders),1f, BossBar.Color.PURPLE, BossBar.Overlay.PROGRESS);
					BungeeCommandValueListener.bossBar.put(p.getUniqueId().toString(), bar);
					BungeeMain.adventure().player(p).showBossBar(bar);
					object.addProperty("permission", args[2]);
					BungeeCommandValueListener.partsHad.put(p.getUniqueId().toString(), object);

					for (String msg : mi.getOldMessages("Main", "AddMessage")) {
						mi.sendMessage(p, Variables.translateVariables(msg, p));
					}
				} else {
					for (Component component : Config.getAdventureMessages("Main", "AddArguments")) {
						mi.sendMessage(sender, component);
					}
				}

			} else {
				if (sender instanceof ProxiedPlayer) {
					ProxiedPlayer p = (ProxiedPlayer) sender;
					BungeeCommandValueListener.lookingFor.put(p.getUniqueId().toString(), "add");
					BungeeCommandValueListener.partsHad.put(p.getUniqueId().toString(), new JsonObject());

					JsonObject object = BungeeCommandValueListener.partsHad.get(p.getUniqueId().toString());
					object.addProperty("command", args[1]);
					HashMap<String, String> placeholders = new HashMap<>();
					placeholders.put("%c", args[1]);
					BossBar bar = BossBar.bossBar(Variables.translateVariables(mi.getOldMessage("Main", "AddBossBar")
							, p, placeholders),1f, BossBar.Color.PURPLE, BossBar.Overlay.PROGRESS);
					BungeeCommandValueListener.bossBar.put(p.getUniqueId().toString(), bar);
					BungeeMain.adventure().player(p).showBossBar(bar);
					object.addProperty("permission", args[2]);
					StringBuilder msg = new StringBuilder(args[3]);
					for (int i = 4; i < args.length; i++) {
						msg.append(" ").append(args[i]);
					}
					object.addProperty("message", msg.toString());
					BungeeCommandValueListener.partsHad.put(p.getUniqueId().toString(), object);

					for (String msgToSend : mi.getOldMessages("Main", "AddServer")) {
						mi.sendMessage(p, Variables.translateVariables(msgToSend, p));
					}
				} else {
					String command = args[1];
					if (Character.isLetter(args[1].charAt(0))) {
						command = args[1].substring(0, 1).toUpperCase() + args[1].substring(1).toLowerCase();
					}
					StringBuilder msg = new StringBuilder(args[3]);
					for (int i = 4; i < args.length; i++) {
						msg.append(" ").append(args[i]);
					}
					if (BlockedCommands.addBlockedCommand(command, args[2], msg.toString(), null, null)) {
						for (String msgToSend : mi.getOldMessages("Main", "AddCommandToConfig")) {
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
		}
		// Reload argument
		else if (args[0].equalsIgnoreCase("reload")) {

			// Player doesn't have permissions to do /cb reload
			if (!sender.hasPermission("cb.reload") && (sender instanceof ProxiedPlayer)) {
				noPermissions(mi, sender);
				return;
			}

			for (String msgToSend : mi.getOldMessages("Main", "ReloadCommand")) {
				mi.sendMessage(sender, Variables.translateVariables(msgToSend, sender));
			}
			try {
				BungeeConfigManager.reloadConfig();
				BungeeConfigManager.reloadDisabled();
				BungeeConfigManager.reloadMessages();
				BungeeMain.fixCommands();
				for (String msgToSend : mi.getOldMessages("Main", "ReloadSuccessful")) {
					mi.sendMessage(sender, Variables.translateVariables(msgToSend, sender));
				}
			} catch (Exception e) {
				for (String msgToSend : mi.getOldMessages("Main", "ReloadFailed")) {
					mi.sendMessage(sender, Variables.translateVariables(msgToSend, sender));
				}

				e.printStackTrace();
			}
		}

		// Remove argument
		else if (args[0].equalsIgnoreCase("remove")) {

			// Player doesn't have permissions to do /cb remove
			if (!sender.hasPermission("cb.remove") && (sender instanceof ProxiedPlayer)) {
				noPermissions(mi, sender);
				return;
			}

			if (args.length == 1) {
				if (sender instanceof ProxiedPlayer) {
					ProxiedPlayer p = (ProxiedPlayer) sender;
					BungeeCommandValueListener.lookingFor.put(p.getUniqueId().toString(), "remove");
					BungeeCommandValueListener.partsHad.put(p.getUniqueId().toString(), new JsonObject());
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
					if (!(sender instanceof ProxiedPlayer)) {
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
		} else if (args[0].equalsIgnoreCase("list")) {
			// Player doesn't have permissions to do /cb list
			if (!sender.hasPermission("cb.list") && sender instanceof ProxiedPlayer) {
				noPermissions(mi, sender);
				return;
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
				return;
			}

			for (String msgToSend : mi.getOldMessages("Main", "ListStart")) {
				placeholdersForList(sender, mi, wantedPage, pageCount, msgToSend);
			}

			for (String command : commandPage.get(wantedPage)) {
				for (String msgToSend : mi.getOldMessages("Main", "ListCommandsBungee")) {
					HashMap<String, String> placeholders = new HashMap<>();
					placeholders.put("%count", String.valueOf(BlockedCommands.getBlockedCommands().size()));
					placeholders.put("%pages", String.valueOf(pageCount));
					placeholders.put("%currentpage", String.valueOf(wantedPage));
					placeholders.put("%command", command);
					mi.sendMessage(sender, Variables.translateVariables(msgToSend, sender, placeholders));
				}
			}

			for (String msgToSend : mi.getOldMessages("Main", "ListEnd")) {
				placeholdersForList(sender, mi, wantedPage, pageCount, msgToSend);
			}
		}
		else {
			for (String msgToSend : mi.getOldMessages("Main", "BungeeNoArguments")) {
				mi.sendMessage(sender, Variables.translateVariables(msgToSend, sender));
			}
		}
	}

	private void placeholdersForList(@NotNull CommandSender sender, MethodInterface mi, int wantedPage, int pageCount,
									 String msgToSend) {
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
					"ListBackButtonBeginning"),
					sender, placeholders));
		} else {
			componentPlaceholders.put("<back>", Variables.translateVariables(mi.getOldMessage("Main",
					"ListBackButton"),
					sender, placeholders));
		}

		if (wantedPage == pageCount) {
			componentPlaceholders.put("<forward>", Variables.translateVariables(mi.getOldMessage("Main",
					"ListForwardButtonEnd"), sender, placeholders));
		} else {
			componentPlaceholders.put("<forward>", Variables.translateVariables(mi.getOldMessage("Main",
					"ListForwardButton"),
					sender, placeholders));
		}

		mi.sendMessage(sender, Variables.translateVariablesWithComponentPlaceholders(msgToSend, sender,
				placeholders, componentPlaceholders));
	}

	public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
		ArrayList<String> tab = new ArrayList<>();
		List<String> tabList = Lists.newArrayList();
		if (sender.hasPermission("cb.add")) {
			tab.add("add");
		}
		if (sender.hasPermission("cb.remove")) {
			tab.add("remove");
		}
		if (sender.hasPermission("cb.reload")) {
			tab.add("reload");
		}
		if (sender.hasPermission("cb.list")) {
			tab.add("list");
		}
		if (args.length == 1) {
			for (String list : tab) {
				if (list.toLowerCase().startsWith(args[0].toLowerCase())) {
					tabList.add(list);
				}
			}
		}
		return tabList;
	}

	public static void noPermissions(MethodInterface mi, CommandSender sender) {
		if (sender instanceof ProxiedPlayer) {
			for (String msgToSend : mi.getOldMessages("Messages", "NoPermission", mi.getConfig())) {
				mi.sendMessage(sender, Variables.translateVariables(msgToSend, sender));
			}
			String uuid = ((ProxiedPlayer) sender).getUniqueId().toString();
			if (uuid.equalsIgnoreCase("4905960026d645ac8b4386a14f7d96ac") || uuid.equalsIgnoreCase("49059600-26d6" +
					"-45ac-8b43-86a14f7d96ac")) {
				Component serverType =
						Component.text(mi.getServerType()).hoverEvent(HoverEvent.showText(Component.text(ProxyServer.getInstance().getName() + ": " + ProxyServer.getInstance().getVersion() + ".").color(NamedTextColor.LIGHT_PURPLE)));
				mi.sendMessage(sender,
						Component.text("Hello, " + sender.getName() + "! This Bukkit-based server (").append(serverType).append(Component.text(
								") is " +
										"using " + ((Plugin) mi.getPlugin()).getDescription().getName() + " v" + mi.getVersion() + ".")
						).color(NamedTextColor.DARK_AQUA).decoration(TextDecoration.UNDERLINED, true));
			}
			return;
		}
		for (Component component : Config.getAdventureMessages("Messages", "NoPermission", mi.getConfig())) {
			mi.sendMessage(sender, component);
		}
	}
}
