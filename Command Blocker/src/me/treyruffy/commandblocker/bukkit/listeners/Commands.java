package me.treyruffy.commandblocker.bukkit.listeners;

import java.util.ArrayList;
import java.util.List;

import me.treyruffy.commandblocker.bukkit.config.ConfigManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;

import com.google.common.collect.Lists;
import org.jetbrains.annotations.NotNull;

public class Commands implements CommandExecutor, TabCompleter {
	
	private static Commands instance = null;

    public static Commands get() {
        return instance == null ? instance = new Commands() : instance;
    }
	
    @Override
	public boolean onCommand(@NotNull CommandSender sender, Command cmd, @NotNull String label, String[] args) {
    	if (cmd.getName().equalsIgnoreCase("cb")) {
    		String noPerms = ChatColor.translateAlternateColorCodes('&', ConfigManager.getConfig().getString("Messages.NoPermission"));
    		if (sender.hasPermission("cb.add") || sender.hasPermission("cb.reload") || sender.hasPermission("cb.remove") || sender.hasPermission("cb.addop")
    				|| sender.hasPermission("cb.removeop") || sender instanceof ConsoleCommandSender) {
    			if (args.length <= 0) {
    				sendMessage(sender, "noArgs", args);
    				return true;
    			}
    		} else {
    			sender.sendMessage(noPerms);
    			return true;
    		}
    		
    		FileConfiguration disabled = ConfigManager.getDisabled();
    		FileConfiguration config = ConfigManager.getConfig();
    		FileConfiguration opDisabled = ConfigManager.getOpDisabled();
    		
    		if (args[0].equalsIgnoreCase("add")) {
    			if (sender.hasPermission("cb.add")) {
    				if (args.length == 1) {
    					sendMessage(sender, "addNoArgs", args);
    				} else if (args.length == 2) {
    					if (disabled.getConfigurationSection("DisabledCommands").getKeys(false).contains(args[1])) {
    						sendMessage(sender, "addAlreadyAdded", args);
    					} else {
    						disabled.set("DisabledCommands." + args[1] + ".Message", config.getString("Default.Message"));
							sendMessage(sender, "added", args);
							ConfigManager.saveDisabled();
    					}
    				} else if (args.length == 3) {
    					if (disabled.getConfigurationSection("DisabledCommands").getKeys(false).contains(args[1])){
    						disabled.set("DisabledCommands." + args[1] + ".Permission", args[2]);
							sendMessage(sender, "changedPerm", args);
							ConfigManager.saveDisabled();
						} else {
							disabled.set("DisabledCommands." + args[1], "");
							disabled.set("DisabledCommands." + args[1] + ".Permission", args[2]);
							sendMessage(sender, "newWithPerm", args);
							ConfigManager.saveDisabled();
						}
    				} else if (args.length >= 4) {
						sendMessage(sender, "newWithMessage", args);
						ConfigManager.saveDisabled();
    				}
    			} else {
    				sender.sendMessage(noPerms);
    			}
    			return true;
    		}
    		
    		if (args[0].equalsIgnoreCase("remove")) {
    			if (sender.hasPermission("cb.remove")) {
    				if (args.length == 1) {
    					sendMessage(sender, "removeNoArgs", args);
    				} else {
    					if (disabled.getConfigurationSection("DisabledCommands").getKeys(false).contains(args[1])){
							disabled.set("DisabledCommands." + args[1] + ".Permission", null);
							disabled.set("DisabledCommands." + args[1] + ".Message", null);
							disabled.set("DisabledCommands." + args[1], null);
							sendMessage(sender, "remove", args);
							ConfigManager.saveDisabled();
						} else {
							sendMessage(sender, "couldNotRemove", args);
						}
    				}
    			} else {
    				sender.sendMessage(noPerms);
    			}
    			return true;
    		}
    		
    		if (args[0].equalsIgnoreCase("addop")) {
    			if (sender.hasPermission("cb.addop") || sender instanceof ConsoleCommandSender) {
    				if (args.length == 1) {
    					sendMessage(sender, "addOpNoArgs", args);
    				} else if (args.length == 2) {
    					if (opDisabled.getConfigurationSection("DisabledOpCommands").getKeys(false).contains(args[1])){
							sendMessage(sender, "addAlreadyAdded", args);
						} else {
							opDisabled.set("DisabledOpCommands." + args[1] + ".Message", config.getString("Default.Message"));
							sendMessage(sender, "addedOp", args);
							ConfigManager.saveOpDisabled();
						}
    				} else {
    					sendMessage(sender, "newOpWithMessage", args);
						ConfigManager.saveOpDisabled();
    				}
    			} else {
    				sender.sendMessage(noPerms);
    			}
    			return true;
    		}
    		
    		if (args[0].equalsIgnoreCase("removeop")) {
    			if (sender.hasPermission("cb.removeop") || sender instanceof ConsoleCommandSender) {
    				if (args.length == 1) {
    					sendMessage(sender, "removeOpNoArgs", args);
    				} else {
    					opDisabled.set("DisabledOpCommands." + args[1] + ".Message", null);
    					opDisabled.set("DisabledOpCommands." + args[1], null);
						sendMessage(sender, "removeOp", args);
						ConfigManager.saveOpDisabled();
    				}
    			} else {
    				sender.sendMessage(noPerms);
    			}
    			return true;
    		}
    		
    		if (args[0].equalsIgnoreCase("reload")) {
    			if (sender.hasPermission("cb.reload")) {
    				sendMessage(sender, "reload", args);
    			} else {
    				sender.sendMessage(noPerms);
    			}
    			return true;
    		}
    		
    		sendMessage(sender, "noArgs", args);
    		
    	}
		return true;
	}
    
	
	public void sendMessage(CommandSender sender, String string, String[] args) {
		FileConfiguration disabled = ConfigManager.getDisabled();
		FileConfiguration config = ConfigManager.getConfig();
		FileConfiguration opDisabled = ConfigManager.getOpDisabled();
		
		// /cb -=- No args or not correct args
		if (string.equalsIgnoreCase("noArgs")){
			sender.sendMessage(ChatColor.BLUE + "-=====[" + ChatColor.AQUA + ChatColor.BOLD + " Command Blocker " + ChatColor.BLUE + "]=====-");
			sender.sendMessage(ChatColor.RED + "You have not supplied enough arguments.");
			sender.sendMessage(ChatColor.RED + "Valid arguments are: add, remove, reload, addop, removeop.");
			sender.sendMessage(ChatColor.GREEN + "Made by TreyRuffy!");
			sender.sendMessage(ChatColor.BLUE + "-======================-");
			return;
		}
		// /cb add -=- Add with no args
		if (string.equalsIgnoreCase("addNoArgs")){
			sender.sendMessage(ChatColor.BLUE + "-=====[" + ChatColor.AQUA + ChatColor.BOLD + " Command Blocker " + ChatColor.BLUE + "]=====-");
			sender.sendMessage(ChatColor.RED + "You must do /cb add <command> [permission] [message]");
			sender.sendMessage(ChatColor.BLUE + "-======================-");
			return;
		}
		// /cb remove -=- Remove with no args
		if (string.equalsIgnoreCase("removeNoArgs")){
			sender.sendMessage(ChatColor.BLUE + "-=====[" + ChatColor.AQUA + ChatColor.BOLD + " Command Blocker " + ChatColor.BLUE + "]=====-");
			sender.sendMessage(ChatColor.RED + "You must do /cb remove <command>");
			sender.sendMessage(ChatColor.BLUE + "-======================-");
			return;
		}
		// /cb reload -=- Reloaded
		if (string.equalsIgnoreCase("reload")){
			sender.sendMessage(ChatColor.BLUE + "-=====[" + ChatColor.AQUA + ChatColor.BOLD + " Command Blocker " + ChatColor.BLUE + "]=====-");
			sender.sendMessage(ChatColor.GREEN + "Reloading the Command Blocker yml files...");
			try {
				ConfigManager.reloadConfig();
				ConfigManager.reloadDisabled();
				ConfigManager.reloadOpDisabled();
				sender.sendMessage(ChatColor.GREEN + "Reloaded the Command Blocker YAML files successfully!");
				sender.sendMessage(ChatColor.BLUE + "-======================-");
			} catch (Exception e){
				sender.sendMessage(ChatColor.RED + "Could not reload the Command Blocker YAML files.");
				sender.sendMessage(ChatColor.BLUE + "-======================-");
				e.printStackTrace();
			}
			return;
		}
		// /cb addOp -=- AddOp with no args
		if (string.equalsIgnoreCase("addOpNoArgs")){
			sender.sendMessage(ChatColor.BLUE + "-=====[" + ChatColor.AQUA + ChatColor.BOLD + " Command Blocker " + ChatColor.BLUE + "]=====-");
			sender.sendMessage(ChatColor.RED + "You must do /cb addOp <command> [message]");
			sender.sendMessage(ChatColor.BLUE + "-======================-");
		}
		if (string.equalsIgnoreCase("removeOpNoArgs")){
			sender.sendMessage(ChatColor.BLUE + "-=====[" + ChatColor.AQUA + ChatColor.BOLD + " Command Blocker " + ChatColor.BLUE + "]=====-");
			sender.sendMessage(ChatColor.RED + "You must do /cb removeOp <command>");
			sender.sendMessage(ChatColor.BLUE + "-======================-");
		}
		// /cb add <command> -=- Already Added
		if (string.equalsIgnoreCase("addAlreadyAdded")){
			sender.sendMessage(ChatColor.BLUE + "-======================-");
			sender.sendMessage(ChatColor.GREEN + "/" + args[1] + " has already been added!");
			sender.sendMessage(ChatColor.BLUE + "-======================-");
			return;
		}
		// /cb add <command> -=- Added
		if (string.equalsIgnoreCase("added")){
			sender.sendMessage(ChatColor.BLUE + "-======================-");
			sender.sendMessage(ChatColor.GREEN + "/" + args[1] + " has been added to the disabled commands list with the message:");
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("Default.Message")));
			sender.sendMessage(ChatColor.BLUE + "-======================-");
			return;
		}
		// /cb addOp <command> -=- Added for OP
		if (string.equalsIgnoreCase("addedOp")){
			sender.sendMessage(ChatColor.BLUE + "-======================-");
			sender.sendMessage(ChatColor.GREEN + "/" + args[1] + " has been added to the op disabled commands list with the message:");
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("Default.Message")));
			sender.sendMessage(ChatColor.BLUE + "-======================-");
			return;
		}
		// /cb remove <command> -=- Removed
		if (string.equalsIgnoreCase("remove")){
			sender.sendMessage(ChatColor.BLUE + "-======================-");
			sender.sendMessage(ChatColor.GREEN + "Successfully removed the command /" + args[1] + " from disabled.yml");
			sender.sendMessage(ChatColor.BLUE + "-======================-");
			return;
		}
		// /cb remove <command> -=- Not command
		if (string.equalsIgnoreCase("couldNotRemove")){
			sender.sendMessage(ChatColor.BLUE + "-======================-");
			sender.sendMessage(ChatColor.RED + "/" + args[1] + " is not a command which is in disabled.yml.");
			sender.sendMessage(ChatColor.BLUE + "-======================-");
			return;
		}
		// /cb removeOp <command> -=- Removed
		if (string.equalsIgnoreCase("removeOp")){
			sender.sendMessage(ChatColor.BLUE + "-======================-");
			sender.sendMessage(ChatColor.GREEN + "Successfully removed the command /" + args[1] + " from opblock.yml");
			sender.sendMessage(ChatColor.BLUE + "-======================-");
			return;
		}
		// /cb removeOp <command> -=- Not command
		if (string.equalsIgnoreCase("couldNotRemoveOp")){
			sender.sendMessage(ChatColor.BLUE + "-======================-");
			sender.sendMessage(ChatColor.RED + "/" + args[1] + " is not a command which is in opblock.yml.");
			sender.sendMessage(ChatColor.BLUE + "-======================-");
			return;
		}
		// /cb add <command> <perm> -=- Changed permission
		if (string.equalsIgnoreCase("changedPerm")){
			sender.sendMessage(ChatColor.BLUE + "-======================-");
			sender.sendMessage(ChatColor.GREEN + "Because /" + args[1] + " has already been added, the permission has been changed.");
			sender.sendMessage(ChatColor.GREEN + "The permission is now:");
			sender.sendMessage(ChatColor.GOLD + args[2]);
			sender.sendMessage(ChatColor.GREEN + "With the message being:");
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("Default.Message")));
			sender.sendMessage(ChatColor.BLUE + "-======================-");
			return;
		}
		// /cb add <command> <perm> -=- Added with permission
		if (string.equalsIgnoreCase("newWithPerm")){
			sender.sendMessage(ChatColor.BLUE + "-======================-");
			sender.sendMessage(ChatColor.GREEN + "Added /" + args[1] + " to disabled.yml!");
			sender.sendMessage(ChatColor.GREEN + "The permission is:");
			sender.sendMessage(ChatColor.GOLD + args[2]);
			sender.sendMessage(ChatColor.GREEN + "With the message being:");
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("Default.Message")));
			sender.sendMessage(ChatColor.BLUE + "-======================-");
			return;
		}
		// /cb add <command> <perm> <message> -=- Added with a message
		if (string.equalsIgnoreCase("newWithMessage")){
			StringBuilder msg = new StringBuilder();
			sender.sendMessage(ChatColor.BLUE + "-======================-");
			disabled.set("DisabledCommands." + args[1] + ".Permission", args[2]);
			for (int i = 3; i < args.length; i++){
				msg.append(args[i]).append(" ");
			}
			disabled.set("DisabledCommands." + args[1] + ".Message", msg.toString());
			sender.sendMessage(ChatColor.GREEN + "Added /" + args[1] + " to disabled.yml!");
			sender.sendMessage(ChatColor.GREEN + "The permission is:");
			sender.sendMessage(ChatColor.GOLD + args[2]);
			sender.sendMessage(ChatColor.GREEN + "With the message being:");
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg.toString()));
			sender.sendMessage(ChatColor.BLUE + "-======================-");
			return;
		}
		// /cb addOp <command> <message> -=- Added op with message
		if (string.equalsIgnoreCase("newOpWithMessage")){
			int i;
			String msg = "";
			sender.sendMessage(ChatColor.BLUE + "-======================-");
			for (i = 2; i < args.length; i++){
				msg = msg + args[i] + " ";
			}
			opDisabled.set("DisabledOpCommands." + args[1] + ".Message", msg);
			sender.sendMessage(ChatColor.GREEN + "Added /" + args[1] + " to opblock.yml!");
			sender.sendMessage(ChatColor.GREEN + "With the message being:");
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
			sender.sendMessage(ChatColor.BLUE + "-======================-");
		}
	}

	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args){
		if (label.equalsIgnoreCase("commandblocker")||label.equalsIgnoreCase("cb")){
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
			if (sender.hasPermission("cb.addOp") || sender instanceof ConsoleCommandSender){
				tab.add("addOp");
			}
			if (sender.hasPermission("cb.removeOp") || sender instanceof ConsoleCommandSender) {
				tab.add("removeOp");
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
	    return null;
	  }

}
