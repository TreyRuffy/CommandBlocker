package me.treyruffy.commandblocker;

import java.io.IOException;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;

import me.treyruffy.commandblocker.Metrics;

public class CommandBlocker extends JavaPlugin implements Listener{
	
	public void onEnable(){
	    getServer().getPluginManager().registerEvents(this, this);
	    getConfig().options().copyDefaults(true);
	    saveConfig();
	    if (getConfig().getBoolean("UseMetrics")){
	    	try {
	    		Metrics metrics = new Metrics(this);
	    		metrics.start();
	    	} catch (IOException e) {}
	    }
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onCommand(PlayerCommandPreprocessEvent event){
		Player sender = event.getPlayer();
		String msg = event.getMessage();
		List<String> commands = getConfig().getStringList("DisabledCommands");
		for (String command : commands)
		if (msg.split(" ")[0].equalsIgnoreCase("/" + command)){
			if(!(sender.hasPermission("cb.allow.blocked"))){
				event.setCancelled(true);
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("Language.YourDisabled")));
				return;
			}
	
		} else if (msg.split(" ")[0].equalsIgnoreCase("/pl")){
			if(!(sender.hasPermission("cb.allow.plugins"))){
				event.setCancelled(true);
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("Language.Plugins")));
				return;
			}
		} else if (msg.split(" ")[0].equalsIgnoreCase("/plugins")){
			if(!(sender.hasPermission("cb.allow.plugins"))){
				event.setCancelled(true);
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("Language.Plugins")));
				return;
			}
		} else if (msg.split(" ")[0].equalsIgnoreCase("/bukkit:plugins")){
			if(!(sender.hasPermission("cb.allow.plugins"))){
				event.setCancelled(true);
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("Language.Plugins")));
				return;
			}
		} else if (msg.split(" ")[0].equalsIgnoreCase("/bukkit:pl")){
			if(!(sender.hasPermission("cb.allow.plugins"))){
				event.setCancelled(true);
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("Language.Plugins")));
				return;
			}

		} else if (msg.split(" ")[0].equalsIgnoreCase("/?")){
			if(!(sender.hasPermission("cb.allow.help"))){
				event.setCancelled(true);
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("Language.Help")));
				return;
			}
		} else if (msg.split(" ")[0].equalsIgnoreCase("/help")){
			if(!(sender.hasPermission("cb.allow.help"))){
				event.setCancelled(true);
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("Language.Help")));
				return;
			}
		} else if (msg.split(" ")[0].equalsIgnoreCase("/bukkit:help")){
			if(!(sender.hasPermission("cb.allow.help"))){
				event.setCancelled(true);
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("Language.Help")));
				return;
			}
		} else if (msg.split(" ")[0].equalsIgnoreCase("/bukkit:?")){
			if(!(sender.hasPermission("cb.allow.help"))){
				event.setCancelled(true);
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("Language.Help")));
				return;
			}
		} else if (msg.split(" ")[0].equalsIgnoreCase("/minecraft:?")){
			if(!(sender.hasPermission("cb.allow.m.help"))){
				event.setCancelled(true);
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("Language.Help")));
				return;
			}
		} else if (msg.split(" ")[0].equalsIgnoreCase("/minecraft:help")){
			if(!(sender.hasPermission("cb.allow.m.help"))){
				event.setCancelled(true);
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("Language.Help")));
				return;
			}

		} else if (msg.split(" ")[0].equalsIgnoreCase("/ver")){
			if(!(sender.hasPermission("cb.allow.version"))){
				event.setCancelled(true);
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("Language.Version")));
				return;
			}
		} else if (msg.split(" ")[0].equalsIgnoreCase("/version")){
			if(!(sender.hasPermission("cb.allow.version"))){
				event.setCancelled(true);
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("Language.Version")));
				return;
			}
		} else if (msg.split(" ")[0].equalsIgnoreCase("/bukkit:ver")){
			if(!(sender.hasPermission("cb.allow.version"))){
				event.setCancelled(true);
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("Language.Version")));
				return;
			}
		} else if (msg.split(" ")[0].equalsIgnoreCase("/bukkit:version")){
			if(!(sender.hasPermission("cb.allow.version"))){
				event.setCancelled(true);
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("Language.Version")));
				return;
			}
		} else if (msg.split(" ")[0].equalsIgnoreCase("/about")){
			if(!(sender.hasPermission("cb.allow.version"))){
				event.setCancelled(true);
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("Language.Version")));
				return;
			}
		} else if (msg.split(" ")[0].equalsIgnoreCase("/bukkit:about")){
			if(!(sender.hasPermission("cb.allow.version"))){
				event.setCancelled(true);
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("Language.Version")));
				return;
			}
		} else if (msg.split(" ")[0].equalsIgnoreCase("/ver")){
			if(!(sender.hasPermission("cb.allow.version"))){
				event.setCancelled(true);
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("Language.Version")));
				return;
			}	

		} else if (msg.split(" ")[0].equalsIgnoreCase("/minecraft:me")){
			if(!(sender.hasPermission("cb.allow.m.me"))){
				event.setCancelled(true);
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("Language.Me")));
				return;
			}
		} else if (msg.split(" ")[0].equalsIgnoreCase("/me")){
			if(!(sender.hasPermission("cb.allow.me"))){
				event.setCancelled(true);
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("Language.Me")));
				return;
			}

		} else if (msg.split(" ")[0].equalsIgnoreCase("/rl")){
			if(!(sender.hasPermission("cb.allow.reload"))){
				event.setCancelled(true);
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("Language.Reload")));
				return;
			}
		} else if (msg.split(" ")[0].equalsIgnoreCase("/reload")){
			if(!(sender.hasPermission("cb.allow.reload"))){
				event.setCancelled(true);
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("Language.Reload")));
				return;
			}
		} else if (msg.split(" ")[0].equalsIgnoreCase("/restart")){
			if(!(sender.hasPermission("cb.allow.restart"))){
				event.setCancelled(true);
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("Language.Restart")));
				return;
			}
		} else if (msg.split(" ")[0].equalsIgnoreCase("/stop")){
			if(!(sender.hasPermission("cb.allow.stop"))){
				event.setCancelled(true);
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("Language.Reload")));
				return;
			}
		} else if (msg.split(" ")[0].equalsIgnoreCase("/bukkit:rl")){
			if(!(sender.hasPermission("cb.allow.reload"))){
				event.setCancelled(true);
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("Language.Reload")));
				return;
			}
		} else if (msg.split(" ")[0].equalsIgnoreCase("/bukkit:reload")){
			if(!(sender.hasPermission("cb.allow.reload"))){
				event.setCancelled(true);
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("Language.Reload")));
				return;
			}

		} else if (msg.split(" ")[0].equalsIgnoreCase("/op")){
			if(!(sender.hasPermission("cb.allow.op"))){
				event.setCancelled(true);
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("Language.Op")));
				return;
			}
		} else if (msg.split(" ")[0].equalsIgnoreCase("/minecraft.op")){
			if(!(sender.hasPermission("cb.allow.op"))){
				event.setCancelled(true);
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("Language.Op")));
				return;
			}

		} else if (msg.split(" ")[0].equalsIgnoreCase("/deop")){
			if(!(sender.hasPermission("cb.allow.deop"))){
				event.setCancelled(true);
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("Language.Deop")));
				return;
			}
		} else if (msg.split(" ")[0].equalsIgnoreCase("/minecraft:deop")){
			if(!(sender.hasPermission("cb.allow.deop"))){
				event.setCancelled(true);
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("Language.Deop")));
				return;
			}
		}
	}
}
