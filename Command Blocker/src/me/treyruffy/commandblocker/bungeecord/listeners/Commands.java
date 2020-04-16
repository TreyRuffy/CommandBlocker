package me.treyruffy.commandblocker.bungeecord.listeners;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;

import me.treyruffy.commandblocker.bungeecord.config.BungeeConfigManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;
import net.md_5.bungee.config.Configuration;

public class Commands extends Command implements TabExecutor {
	
	private static Commands instance = null;

    public static Commands get() {
        return instance == null ? instance = new Commands() : instance;
    }
    
    public Commands() {
    	super("cb", "", "commandblocker", "commandblock");
    }
	
    @Override
	public void execute(CommandSender sender, String[] args) {
		String noPerms = ChatColor.translateAlternateColorCodes('&', BungeeConfigManager.getConfig().getString("Messages.NoPermission"));
		if (sender.hasPermission("cb.add") || sender.hasPermission("cb.reload") || sender.hasPermission("cb.remove") || sender.hasPermission("cb.addop")
				|| sender.hasPermission("cb.removeop") || sender instanceof ProxyServer) {
			if (args.length <= 0) {
				sendMessage(sender, "noArgs", args);
				return;
			}
		} else {
			sender.sendMessage(new TextComponent(noPerms));
			return;
		}
		
		Configuration disabled = BungeeConfigManager.getDisabled();
		Configuration config = BungeeConfigManager.getConfig();
		
		if (args[0].equalsIgnoreCase("add")) {
			if (sender.hasPermission("cb.add")) {
				if (args.length == 1) {
					sendMessage(sender, "addNoArgs", args);
				} else if (args.length == 2) {
					if (disabled.getSection("DisabledCommands").get(args[1]) != null) {
						sendMessage(sender, "addAlreadyAdded", args);
					} else {
						disabled.set("DisabledCommands." + args[1] + ".Message", config.getString("Default.Message"));
						sendMessage(sender, "added", args);
						BungeeConfigManager.saveDisabled();
					}
				} else if (args.length == 3) {
					if (disabled.getSection("DisabledCommands").get(args[1]) != null){
						disabled.set("DisabledCommands." + args[1] + ".Permission", args[2]);
						sendMessage(sender, "changedPerm", args);
					} else {
						disabled.set("DisabledCommands." + args[1], "");
						disabled.set("DisabledCommands." + args[1] + ".Permission", args[2]);
						sendMessage(sender, "newWithPerm", args);
					}
					BungeeConfigManager.saveDisabled();
				} else {
					sendMessage(sender, "newWithMessage", args);
					BungeeConfigManager.saveDisabled();
				}
			} else {
				sender.sendMessage(new TextComponent(noPerms));
			}
			return;
		}
		
		if (args[0].equalsIgnoreCase("remove")) {
			if (sender.hasPermission("cb.remove")) {
				if (args.length == 1) {
					sendMessage(sender, "removeNoArgs", args);
				} else {
					if (disabled.getSection("DisabledCommands").get(args[1]) != null){
						disabled.set("DisabledCommands." + args[1] + ".Permission", null);
						disabled.set("DisabledCommands." + args[1] + ".Message", null);
						disabled.set("DisabledCommands." + args[1], null);
						sendMessage(sender, "remove", args);
						BungeeConfigManager.saveDisabled();
					} else {
						sendMessage(sender, "couldNotRemove", args);
					}
				}
			} else {
				sender.sendMessage(new TextComponent(noPerms));
			}
			return;
		}
		
		
		if (args[0].equalsIgnoreCase("reload")) {
			if (sender.hasPermission("cb.reload")) {
				sendMessage(sender, "reload", args);
			} else {
				sender.sendMessage(new TextComponent(noPerms));
			}
			return;
		}
		
		sendMessage(sender, "noArgs", args);
	}
    
	
	public void sendMessage(CommandSender sender, String string, String[] args) {
		Configuration disabled = BungeeConfigManager.getDisabled();
		Configuration config = BungeeConfigManager.getConfig();
		
		// /cb -=- No args or not correct args
		if (string.equalsIgnoreCase("noArgs")){
			sender.sendMessage(new TextComponent(ChatColor.BLUE + "-=====[" + ChatColor.AQUA + ChatColor.BOLD + " " +
					"Command Blocker " + ChatColor.BLUE + "]=====-"));
			sender.sendMessage(new TextComponent(ChatColor.RED + "You have not supplied enough arguments."));
			sender.sendMessage(new TextComponent(ChatColor.RED + "Valid arguments are: add, remove, reload."));
			sender.sendMessage(new TextComponent(ChatColor.GREEN + "Made by TreyRuffy!"));
			sender.sendMessage(new TextComponent(ChatColor.BLUE + "-======================-"));
			return;
		}
		// /cb add -=- Add with no args
		if (string.equalsIgnoreCase("addNoArgs")){
			sender.sendMessage(new TextComponent(ChatColor.BLUE + "-=====[" + ChatColor.AQUA + ChatColor.BOLD + " " +
					"Command Blocker " + ChatColor.BLUE + "]=====-"));
			sender.sendMessage(new TextComponent(ChatColor.RED + "You must do /cb add <command> [permission] " +
					"[message]"));
			sender.sendMessage(new TextComponent(ChatColor.BLUE + "-======================-"));
			return;
		}
		// /cb remove -=- Remove with no args
		if (string.equalsIgnoreCase("removeNoArgs")){
			sender.sendMessage(new TextComponent(ChatColor.BLUE + "-=====[" + ChatColor.AQUA + ChatColor.BOLD + " " +
					"Command Blocker " + ChatColor.BLUE + "]=====-"));
			sender.sendMessage(new TextComponent(ChatColor.RED + "You must do /cb remove <command>"));
			sender.sendMessage(new TextComponent(ChatColor.BLUE + "-======================-"));
			return;
		}
		// /cb reload -=- Reloaded
		if (string.equalsIgnoreCase("reload")){
			sender.sendMessage(new TextComponent(ChatColor.BLUE + "-=====[" + ChatColor.AQUA + ChatColor.BOLD + " " +
					"Command Blocker " + ChatColor.BLUE + "]=====-"));
			sender.sendMessage(new TextComponent(ChatColor.GREEN + "Reloading the Command Blocker yml files..."));
			try {
				BungeeConfigManager.reloadConfig();
				BungeeConfigManager.reloadDisabled();
				sender.sendMessage(new TextComponent(ChatColor.GREEN + "Reloaded the Command Blocker YAML files " +
						"successfully!"));
				sender.sendMessage(new TextComponent(ChatColor.BLUE + "-======================-"));
			} catch (Exception e){
				sender.sendMessage(new TextComponent(ChatColor.RED + "Could not reload the Command Blocker YAML files" +
						"."));
				sender.sendMessage(new TextComponent(ChatColor.BLUE + "-======================-"));
				e.printStackTrace();
			}
			return;
		}
		// /cb add <command> -=- Already Added
		if (string.equalsIgnoreCase("addAlreadyAdded")){
			sender.sendMessage(new TextComponent(ChatColor.BLUE + "-======================-"));
			sender.sendMessage(new TextComponent(ChatColor.GREEN + "/" + args[1] + " has already been added!"));
			sender.sendMessage(new TextComponent(ChatColor.BLUE + "-======================-"));
			return;
		}
		// /cb add <command> -=- Added
		if (string.equalsIgnoreCase("added")){
			sender.sendMessage(new TextComponent(ChatColor.BLUE + "-======================-"));
			sender.sendMessage(new TextComponent(ChatColor.GREEN + "/" + args[1] + " has been added to the disabled " +
					"commands list with the message:"));
			sender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', config.getString(
					"Default.Message"))));
			sender.sendMessage(new TextComponent(ChatColor.BLUE + "-======================-"));
			return;
		}
		// /cb remove <command> -=- Removed
		if (string.equalsIgnoreCase("remove")){
			sender.sendMessage(new TextComponent(ChatColor.BLUE + "-======================-"));
			sender.sendMessage(new TextComponent(ChatColor.GREEN + "Successfully removed the command /" + args[1] +
					" from disabled.yml"));
			sender.sendMessage(new TextComponent(ChatColor.BLUE + "-======================-"));
			return;
		}
		// /cb remove <command> -=- Not command
		if (string.equalsIgnoreCase("couldNotRemove")){
			sender.sendMessage(new TextComponent(ChatColor.BLUE + "-======================-"));
			sender.sendMessage(new TextComponent(ChatColor.RED + "/" + args[1] + " is not a command which is in " +
					"disabled.yml."));
			sender.sendMessage(new TextComponent(ChatColor.BLUE + "-======================-"));
			return;
		}
		// /cb add <command> <perm> -=- Changed permission
		if (string.equalsIgnoreCase("changedPerm")){
			sender.sendMessage(new TextComponent(ChatColor.BLUE + "-======================-"));
			sender.sendMessage(new TextComponent(ChatColor.GREEN + "Because /" + args[1] + " has already been added, " +
					"the permission has been changed."));
			sender.sendMessage(new TextComponent(ChatColor.GREEN + "The permission is now:"));
			sender.sendMessage(new TextComponent(ChatColor.GOLD + args[2]));
			sender.sendMessage(new TextComponent(ChatColor.GREEN + "With the message being:"));
			sender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', config.getString(
					"Default.Message"))));
			sender.sendMessage(new TextComponent(ChatColor.BLUE + "-======================-"));
			return;
		}
		// /cb add <command> <perm> -=- Added with permission
		if (string.equalsIgnoreCase("newWithPerm")){
			sender.sendMessage(new TextComponent(ChatColor.BLUE + "-======================-"));
			sender.sendMessage(new TextComponent(ChatColor.GREEN + "Added /" + args[1] + " to disabled.yml!"));
			sender.sendMessage(new TextComponent(ChatColor.GREEN + "The permission is:"));
			sender.sendMessage(new TextComponent(ChatColor.GOLD + args[2]));
			sender.sendMessage(new TextComponent(ChatColor.GREEN + "With the message being:"));
			sender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', config.getString(
					"Default.Message"))));
			sender.sendMessage(new TextComponent(ChatColor.BLUE + "-======================-"));
			return;
		}
		// /cb add <command> <perm> <message> -=- Added with a message
		if (string.equalsIgnoreCase("newWithMessage")){
			StringBuilder msg = new StringBuilder();
			sender.sendMessage(new TextComponent(ChatColor.BLUE + "-======================-"));
			disabled.set("DisabledCommands." + args[1] + ".Permission", args[2]);
			for (int i = 3; i < args.length; i++){
				msg.append(args[i]).append(" ");
			}
			disabled.set("DisabledCommands." + args[1] + ".Message", msg.toString());
			sender.sendMessage(new TextComponent(ChatColor.GREEN + "Added /" + args[1] + " to disabled.yml!"));
			sender.sendMessage(new TextComponent(ChatColor.GREEN + "The permission is:"));
			sender.sendMessage(new TextComponent(ChatColor.GOLD + args[2]));
			sender.sendMessage(new TextComponent(ChatColor.GREEN + "With the message being:"));
			sender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', msg.toString())));
			sender.sendMessage(new TextComponent(ChatColor.BLUE + "-======================-"));
		}
	}

	public Iterable<String> onTabComplete(CommandSender sender, String[] args){
			ArrayList<String> tab = new ArrayList<>();
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
