package me.treyruffy.commandblocker.bukkit;

import java.io.File;

import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import me.treyruffy.commandblocker.MethodInterface;
import me.treyruffy.commandblocker.bukkit.api.BlockedCommands;
import me.treyruffy.commandblocker.bukkit.api.BlockedOpCommands;

public class BukkitMethods implements MethodInterface {

	@Override
	public String getVersion() {
		return BukkitMain.get().getDescription().getVersion();
	}

	@Override
	public File getDataFolder() {
		return ((JavaPlugin) getPlugin()).getDataFolder();
	}

	@Override
	public void executeCommand(String cmd) {
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
	}

	@Override
	public void setupMetrics() {
		int pluginId = 1851;
		Metrics metrics = new Metrics(Bukkit.getPluginManager().getPlugin("TreysCommandBlocker"), pluginId);
		
		metrics.addCustomChart(new Metrics.SimplePie("blockedCommandsCount", () -> BlockedCommands.getBlockedCommands().size() + ""));
		
		metrics.addCustomChart(new Metrics.SimplePie("blockedOpCommandsCount", () -> BlockedOpCommands.getBlockedCommands().size() + ""));
	}

	@Override
	public void sendMessage(Object player, String msg) {
		((CommandSender) player).sendMessage(msg);
	}

	@Override
	public Object getPlugin() {
		return BukkitMain.get();
	}

	@Override
	public void log(String msg) {
		Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
	}

	@Override
	public String getServerType() {
		return "Bukkit";
	}

}
