package me.treyruffy.commandblocker.bungeecord.commands;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;
import com.google.gson.JsonObject;

import me.treyruffy.commandblocker.Log;
import me.treyruffy.commandblocker.Universal;
import me.treyruffy.commandblocker.bungeecord.api.BlockedCommands;
import me.treyruffy.commandblocker.bungeecord.config.BungeeConfigManager;
import me.treyruffy.commandblocker.bungeecord.listeners.BungeeCommandValueListener;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;
import net.md_5.bungee.config.Configuration;


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
    		sender.sendMessage(new TextComponent(ChatColor.RED + "dont have perms"));
    		return;
    	}
    	
    	Configuration disabled = BungeeConfigManager.getDisabled();
		Configuration config = BungeeConfigManager.getConfig();
		
		if (args.length == 0) {
			sender.sendMessage(new TextComponent(ChatColor.RED + "its /cb <add, reload, remove>"));
    		return;
		}
    	
		if (args[0].equalsIgnoreCase("add")) {
			if (!sender.hasPermission("cb.add")) {
				sender.sendMessage(new TextComponent(ChatColor.RED + "dont have perms"));
				return;
			}
			if (args.length == 1) {
				if (!(sender instanceof ProxiedPlayer)) {
					sender.sendMessage(new TextComponent(ChatColor.RED + "its /cb add <command> <permission> <message>"));
		    		return;
				}
				ProxiedPlayer p = (ProxiedPlayer) sender;
				BungeeCommandValueListener.lookingFor.put(p.getUniqueId().toString(), "add");
				BungeeCommandValueListener.partsHad.put(p.getUniqueId().toString(), new JsonObject());
				p.sendMessage(new TextComponent("add the command to block"));
				return;
			} else if (args.length == 2) {
				if (!(sender instanceof ProxiedPlayer)) {
					sender.sendMessage(new TextComponent(ChatColor.RED + "its /cb add <command> <permission> <message>"));
		    		return;
				}
				ProxiedPlayer p = (ProxiedPlayer) sender;
				BungeeCommandValueListener.lookingFor.put(p.getUniqueId().toString(), "add");
				JsonObject object = new JsonObject();
				object.addProperty("command", args[0]);
				BungeeCommandValueListener.partsHad.put(p.getUniqueId().toString(), object);
				p.sendMessage(new TextComponent("add the permission for the blocked command"));
				return;
			} else if (args.length == 3) {
				if (!(sender instanceof ProxiedPlayer)) {
					sender.sendMessage(new TextComponent(ChatColor.RED + "its /cb add <command> <permission> <message>"));
		    		return;
				}
				ProxiedPlayer p = (ProxiedPlayer) sender;
				BungeeCommandValueListener.lookingFor.put(p.getUniqueId().toString(), "add");
				JsonObject object = new JsonObject();
				object.addProperty("command", args[0]);
				object.addProperty("permission", args[1]);
				BungeeCommandValueListener.partsHad.put(p.getUniqueId().toString(), object);
				p.sendMessage(new TextComponent("add the message for the blocked command"));
				return;
			} else {
				if (!(sender instanceof ProxiedPlayer)) {
					String command = args[1].substring(0, 1).toUpperCase() + args[1].substring(1).toLowerCase();
					String msg = args[3];
					for (int i = 4; i < args.length; i++) {
						msg = msg + " " + args[i];
					}
					if (BlockedCommands.addBlockedCommand(command, args[2], msg, null, null)) {
						sender.sendMessage(new TextComponent("added /" + command + " with permission " + args[2] + " and message " + msg));
						Log.addLog(Universal.get().getMethods(), sender.getName() + ": Added /" + command + " to disabled.yml with permission " + args[2] + " and message " + msg);
						return;
					} else {
						sender.sendMessage(new TextComponent("could not add /" + command + " to disabled.yml"));
						return;
					}
					
				} else {
					ProxiedPlayer p = (ProxiedPlayer) sender;
					BungeeCommandValueListener.lookingFor.put(p.getUniqueId().toString(), "add");
					JsonObject object = new JsonObject();
					object.addProperty("command", args[1]);
					object.addProperty("permission", args[2]);
					String msg = args[3];
					for (int i = 4; i < args.length; i++) {
						msg = msg + " " + args[i];
					}
					object.addProperty("message", msg);
					BungeeCommandValueListener.partsHad.put(p.getUniqueId().toString(), object);
					p.sendMessage(new TextComponent("add the servers for the blocked command"));
					return;
				}
			}
		} else if (args[0].equalsIgnoreCase("remove")) {
			if (!sender.hasPermission("cb.add")) {
				sender.sendMessage(new TextComponent(ChatColor.RED + "dont have perms"));
				return;
			}
			if (args.length == 1) {
				if (!(sender instanceof ProxiedPlayer)) {
					sender.sendMessage(new TextComponent(ChatColor.RED + "its /cb remove <command>"));
		    		return;
				}
				ProxiedPlayer p = (ProxiedPlayer) sender;
				BungeeCommandValueListener.lookingFor.put(p.getUniqueId().toString(), "remove");
				BungeeCommandValueListener.partsHad.put(p.getUniqueId().toString(), new JsonObject());
				p.sendMessage(new TextComponent("add the command to unblock"));
			} else {
				String command = args[1];
				for (int i = 2; i < args.length; i++) {
					command = command + " " + args[i];
				}
				if (BlockedCommands.removeBlockedCommand(command)) {
					sender.sendMessage(new TextComponent("removed /" + command + " from disabled.yml")); 
					Log.addLog(Universal.get().getMethods(), sender.getName() + ": Removed /" + command + " from disabled.yml");
				} else {
					sender.sendMessage(new TextComponent(ChatColor.RED + "could not remove /" + command));
				}
			}
			
		} else if (args[0].equalsIgnoreCase("reload")) {
			if (!sender.hasPermission("cb.reload")) {
				sender.sendMessage(new TextComponent(ChatColor.RED + "dont have perms"));
				return;
			}
			
			sender.sendMessage(new TextComponent(ChatColor.BLUE + "-=====[" + ChatColor.AQUA + ChatColor.BOLD + " Command Blocker " + ChatColor.BLUE + "]=====-"));
			sender.sendMessage(new TextComponent(ChatColor.GREEN + "Reloading the Command Blocker yml files..."));
			try {
				BungeeConfigManager.reloadConfig();
				BungeeConfigManager.reloadDisabled();
				sender.sendMessage(new TextComponent(ChatColor.GREEN + "Reloaded the Command Blocker YAML files successfully!"));
				sender.sendMessage(new TextComponent(ChatColor.BLUE + "-======================-"));
			} catch (Exception e) {
				sender.sendMessage(new TextComponent(ChatColor.RED + "Could not reload the Command Blocker YAML files."));
				sender.sendMessage(new TextComponent(ChatColor.BLUE + "-======================-"));
				e.printStackTrace();
			}
			return;
		} else {
			sender.sendMessage(new TextComponent(ChatColor.RED + "its /cb <add, reload, remove>"));
    		return;
		}
	}

	public Iterable<String> onTabComplete(CommandSender sender, String[] args){
			ArrayList<String> tab = new ArrayList<String>();
			List<String> tabList = Lists.newArrayList();
			if (sender.hasPermission("cb.add")){
				tab.add("add");
			}
			if (sender.hasPermission("cb.remove")){
				tab.add("remove");
			}
			if (sender.hasPermission("cb.reload")){
				tab.add("reload");
			}
			if (args.length == 1) {
				for (String list : tab) {
					if (list.toLowerCase().startsWith(args[0].toLowerCase())){
						tabList.add(list);
					}
				}
			}
			return tabList;
	  }
}
