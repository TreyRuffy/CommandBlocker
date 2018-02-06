package me.treyruffy.commandblocker.Bukkit;

import java.io.File;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import me.treyruffy.commandblocker.MethodInterface;
import me.treyruffy.commandblocker.Bukkit.Listeners.Commands;

public class BukkitMethods implements MethodInterface {

	private final File opDisabledFile = new File(getDataFolder(), "opdisabled.yml");
	private final File disabledFile = new File(getDataFolder(), "disabled.yml");
	private final File configFile = new File(getDataFolder(), "config.yml");
	
	private YamlConfiguration config;
	
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
		Metrics metrics = new Metrics((JavaPlugin) getPlugin());
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
		Bukkit.getServer().getConsoleSender().sendMessage(msg.replaceAll("&", "§"));
	}

	@Override
	public Object getConfig() {
		return config;
	}

}
