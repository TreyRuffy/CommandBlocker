package me.treyruffy.commandblocker.Bungee.Listeners;

import java.util.concurrent.TimeUnit;

import me.treyruffy.commandblocker.Universal;
import me.treyruffy.commandblocker.Bungee.BungeeMain;
import me.treyruffy.commandblocker.Bungee.BungeeConfigManager;
import me.treyruffy.commandblocker.Updater.UpdateChecker;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

public class Update implements Listener {
	
	@EventHandler
	public void onJoin(PostLoginEvent e){
		if (BungeeConfigManager.getConfig().getBoolean("Updates.Check")){
			if (BungeeConfigManager.getConfig().getBoolean("Updates.TellPlayers")){
				final ProxiedPlayer p = e.getPlayer();
				if (p.hasPermission("cb.updates")){
					Boolean updates = UpdateChecker.getLastUpdate(Universal.get().getMethods());
					if (updates){
						ProxyServer.getInstance().getScheduler().schedule((Plugin) Universal.get().getMethods().getPlugin(), new Runnable() {
							@Override
							public void run() {
								p.sendMessage(new TextComponent(ChatColor.AQUA + "+=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=+"));
								p.sendMessage(new TextComponent(ChatColor.GREEN + "There is a new update for"));
								p.sendMessage(new TextComponent(ChatColor.GREEN + "Command Blocker"));
								p.sendMessage(new TextComponent(ChatColor.RED + "Download at:"));
								p.sendMessage(new TextComponent(ChatColor.LIGHT_PURPLE + "https://www.spigotmc.org/resources/5280/"));
								p.sendMessage(new TextComponent(ChatColor.AQUA + "+=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=+"));
							}
						}, 4l, TimeUnit.SECONDS);
					}
				}
			}
		}
	}
}
