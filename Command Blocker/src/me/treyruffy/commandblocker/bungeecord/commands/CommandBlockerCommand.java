package me.treyruffy.commandblocker.bungeecord.commands;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;
import com.google.gson.JsonObject;

import me.treyruffy.commandblocker.Log;
import me.treyruffy.commandblocker.Universal;
import me.treyruffy.commandblocker.bungeecord.BungeeMain;
import me.treyruffy.commandblocker.bungeecord.api.BlockedCommands;
import me.treyruffy.commandblocker.bungeecord.config.BungeeConfigManager;
import me.treyruffy.commandblocker.bungeecord.config.Messages;
import me.treyruffy.commandblocker.bungeecord.listeners.BungeeCommandValueListener;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;


public class CommandBlockerCommand extends Command implements TabExecutor {
	
	private static CommandBlockerCommand instance = null;

    public static CommandBlockerCommand get() {
        return instance == null ? instance = new CommandBlockerCommand() : instance;
    }
    
    public CommandBlockerCommand() {
    	super("cb", "", "commandblockerbungee", "commandblocker", "commandblock");
    }
	
    @Override
	public void execute(CommandSender sender, String[] args) {
    	
    	if ((!(sender.hasPermission("cb.add") || sender.hasPermission("cb.reload") || sender.hasPermission("cb.remove")) && (sender instanceof ProxiedPlayer))) {
			sender.sendMessage(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&',
					BungeeConfigManager.getConfig().getString("Messages" + ".NoPermission"))));
			return;
		}
    	
		if (args.length == 0) {
			for (String message : Messages.getMessages("Main", "BungeeNoArguments")) {
				sender.sendMessage(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', message)));
			}
			return;
		}
    	
		if (args[0].equalsIgnoreCase("add")) {
			if (!sender.hasPermission("cb.add")) {
				sender.sendMessage(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&',
						BungeeConfigManager.getConfig().getString("Messages" + ".NoPermission"))));
				return;
			}
			if (args.length == 1) {
				if (!(sender instanceof ProxiedPlayer)) {
					for (String message : Messages.getMessages("Main", "AddArguments")) {
						sender.sendMessage(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&',
								message)));
					}
					return;
				}
				ProxiedPlayer p = (ProxiedPlayer) sender;
				BungeeCommandValueListener.lookingFor.put(p.getUniqueId().toString(), "add");
				BungeeCommandValueListener.partsHad.put(p.getUniqueId().toString(), new JsonObject());
				for (String message : Messages.getMessages("Main", "AddCommandToBlock")) {
					sender.sendMessage(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&',
							message)));
				}
			} else if (args.length == 2) {
				if (!(sender instanceof ProxiedPlayer)) {
					for (String message : Messages.getMessages("Main", "AddArguments")) {
						sender.sendMessage(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&',
								message)));
					}
					return;
				}
				ProxiedPlayer p = (ProxiedPlayer) sender;
				BungeeCommandValueListener.lookingFor.put(p.getUniqueId().toString(), "add");
				JsonObject object = new JsonObject();
				object.addProperty("command", args[0]);
				BungeeCommandValueListener.partsHad.put(p.getUniqueId().toString(), object);
				for (String message : Messages.getMessages("Main", "AddPermission")) {
					sender.sendMessage(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&',
							message)));
				}
			} else if (args.length == 3) {
				if (!(sender instanceof ProxiedPlayer)) {
					for (String message : Messages.getMessages("Main", "AddArguments")) {
						sender.sendMessage(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&',
								message)));
					}
					return;
				}
				ProxiedPlayer p = (ProxiedPlayer) sender;
				BungeeCommandValueListener.lookingFor.put(p.getUniqueId().toString(), "add");
				JsonObject object = new JsonObject();
				object.addProperty("command", args[0]);
				object.addProperty("permission", args[1]);
				BungeeCommandValueListener.partsHad.put(p.getUniqueId().toString(), object);
				for (String message : Messages.getMessages("Main", "AddMessage")) {
					sender.sendMessage(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&',
							message)));
				}
			} else {
				if (!(sender instanceof ProxiedPlayer)) {
					String command = args[1].substring(0, 1).toUpperCase() + args[1].substring(1).toLowerCase();
					StringBuilder msg = new StringBuilder(args[3]);
					for (int i = 4; i < args.length; i++) {
						msg.append(" ").append(args[i]);
					}
					if (BlockedCommands.addBlockedCommand(command, args[2], msg.toString(), null, null)) {
						for (String message : Messages.getMessages("Main", "AddCommandToConfig")) {
							sender.sendMessage(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&'
									, message.replace("%c", command).replace("%p", args[2]).replace("%m", msg))));
						}
						Log.addLog(Universal.get().getMethods(), sender.getName() + ": Added /" + command + " to " +
								"disabled.yml with permission " + args[2] + " and message " + msg);
					} else {
						for (String message : Messages.getMessages("Main", "CouldNotAddCommandToConfig")) {
							sender.sendMessage(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&'
									, message.replace("%c", command))));
						}
					}

				} else {
					ProxiedPlayer p = (ProxiedPlayer) sender;
					BungeeCommandValueListener.lookingFor.put(p.getUniqueId().toString(), "add");
					JsonObject object = new JsonObject();
					object.addProperty("command", args[1]);
					object.addProperty("permission", args[2]);
					StringBuilder msg = new StringBuilder(args[3]);
					for (int i = 4; i < args.length; i++) {
						msg.append(" ").append(args[i]);
					}
					object.addProperty("message", msg.toString());
					BungeeCommandValueListener.partsHad.put(p.getUniqueId().toString(), object);
					for (String message : Messages.getMessages("Main", "AddServer")) {
						sender.sendMessage(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&',
								message)));
					}
				}
			}
		} else if (args[0].equalsIgnoreCase("remove")) {
			if (!sender.hasPermission("cb.add")) {
				sender.sendMessage(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&',
						BungeeConfigManager.getConfig().getString("Messages" + ".NoPermission"))));
				return;
			}
			if (args.length == 1) {
				if (!(sender instanceof ProxiedPlayer)) {
					for (String message : Messages.getMessages("Main", "RemoveArguments")) {
						sender.sendMessage(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&',
								message)));
					}
					return;
				}
				ProxiedPlayer p = (ProxiedPlayer) sender;
				BungeeCommandValueListener.lookingFor.put(p.getUniqueId().toString(), "remove");
				BungeeCommandValueListener.partsHad.put(p.getUniqueId().toString(), new JsonObject());
				for (String message : Messages.getMessages("Main", "RemoveCommandFromBlocklist")) {
					sender.sendMessage(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&',
							message)));
				}
			} else {
				StringBuilder command = new StringBuilder(args[1]);
				for (int i = 2; i < args.length; i++) {
					command.append(" ").append(args[i]);
				}
				if (BlockedCommands.removeBlockedCommand(command.toString())) {
					for (String message : Messages.getMessages("Main", "RemovedCommand")) {
						sender.sendMessage(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&',
								message.replace("%c", command))));
					}
					Log.addLog(Universal.get().getMethods(), sender.getName() + ": Removed /" + command + " from " +
							"disabled.yml");
				} else {
					for (String message : Messages.getMessages("Main", "UnblockCancelledBecauseNotBlocked")) {
						sender.sendMessage(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&',
								message.replace("%c", command))));
					}
				}
			}
			
		} else if (args[0].equalsIgnoreCase("reload")) {
			if (!sender.hasPermission("cb.reload")) {
				sender.sendMessage(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&',
						BungeeConfigManager.getConfig().getString("Messages" + ".NoPermission"))));
				return;
			}

			for (String message : Messages.getMessages("Main", "ReloadCommand")) {
				sender.sendMessage(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', message)));
			}
			try {
				BungeeConfigManager.reloadConfig();
				BungeeConfigManager.reloadDisabled();
				BungeeConfigManager.reloadMessages();
				new BungeeMain().fixCommands();
				for (String message : Messages.getMessages("Main", "ReloadSuccessful")) {
					sender.sendMessage(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&',
							message)));
				}
			} catch (Exception e) {
				for (String message : Messages.getMessages("Main", "ReloadFailed")) {
					sender.sendMessage(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&',
							message)));
				}
				e.printStackTrace();
			}
		} else {
			for (String message : Messages.getMessages("Main", "BungeeNoArguments")) {
				sender.sendMessage(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', message)));
			}
		}
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
		if (args.length == 1) {
			for (String list : tab) {
				if (list.toLowerCase().startsWith(args[0].toLowerCase())) {
					tabList.add(list);
				}
			}
		}
		return tabList;
	}
}
