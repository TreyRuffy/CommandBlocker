package me.treyruffy.commandblocker.bukkit;

import java.util.logging.Level;

import me.treyruffy.commandblocker.bukkit.config.Messages;
import me.treyruffy.commandblocker.bukkit.listeners.Packets;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import me.treyruffy.commandblocker.CopyConfigs;
import me.treyruffy.commandblocker.Universal;
import me.treyruffy.commandblocker.bukkit.commands.CommandBlockerCmd;
import me.treyruffy.commandblocker.bukkit.commands.CommandBlockerTabComplete;
import me.treyruffy.commandblocker.bukkit.config.ConfigManager;
import me.treyruffy.commandblocker.bukkit.gui.DisabledGui;
import me.treyruffy.commandblocker.bukkit.gui.OpDisabledGui;
import me.treyruffy.commandblocker.bukkit.listeners.BukkitBlock;
import me.treyruffy.commandblocker.bukkit.listeners.CommandValueListener;
import me.treyruffy.commandblocker.bukkit.listeners.Update;
import me.treyruffy.commandblocker.updater.UpdateChecker;
import me.treyruffy.commandblockerlegacy.OldConfigManager;

public class BukkitMain extends JavaPlugin implements Listener {

	private static BukkitMain instance;

	public static BukkitMain get() {
		return instance;
	}
	
	@Override
	public void onEnable() {
		if (oldConfig()) {
			if (!Bukkit.getPluginManager().isPluginEnabled("CommandBlockerLegacy")) {
				Bukkit.getLogger().log(Level.SEVERE,
						"Trey's Command Blocker needs Trey's Command Blocker Legacy to " + "run on your server version" +
								". Please download at https://www.spigotmc.org/resources/5280/");
				this.getPluginLoader().disablePlugin(this);
				return;
			}
		}
		try {
			Class.forName("com.google.gson.JsonElement");
		} catch (ClassNotFoundException e) {
			Bukkit.getLogger().log(Level.SEVERE, "Trey's Command Blocker needs Trey's Command Blocker Legacy to run " +
					"on" + " your server version. Please download at https://www.spigotmc.org/resources/5280/");
			this.getPluginLoader().disablePlugin(this);
			return;
		}
		instance = this;
		Universal.get().setup(new BukkitMethods());

		getServer().getPluginManager().registerEvents(this, this);
		getServer().getPluginManager().registerEvents(new Update(), this);
		getServer().getPluginManager().registerEvents(new BukkitBlock(), this);
		getServer().getPluginManager().registerEvents(new CommandValueListener(), this);
		getServer().getPluginManager().registerEvents(new DisabledGui(), this);
		getServer().getPluginManager().registerEvents(new OpDisabledGui(), this);
		loadConfigManager();

		if (oldConfig()) {
			if (OldConfigManager.getConfig().get("Version") != null) {
				if (!OldConfigManager.getConfig().getString("Version").equalsIgnoreCase(this.getDescription().getVersion())) {
					CopyConfigs.duplicateOldSettings(Universal.get().getMethods());
					OldConfigManager.getConfig().set("Version", this.getDescription().getVersion());
					OldConfigManager.saveConfig();
				}
			} else {
				CopyConfigs.duplicateOldSettings(Universal.get().getMethods());
				OldConfigManager.getConfig().set("Version", this.getDescription().getVersion());
				OldConfigManager.saveConfig();
			}
		} else {
			if (ConfigManager.getConfig().get("Version") != null) {
				if (!ConfigManager.getConfig().getString("Version").equalsIgnoreCase(this.getDescription().getVersion())) {
					CopyConfigs.duplicateOldSettings(Universal.get().getMethods());
					ConfigManager.getConfig().set("Version", this.getDescription().getVersion());
					ConfigManager.saveConfig();
				}
			} else {
				CopyConfigs.duplicateOldSettings(Universal.get().getMethods());
				ConfigManager.getConfig().set("Version", this.getDescription().getVersion());
				ConfigManager.saveConfig();
			}
		}
		getCommand("cb").setExecutor(new CommandBlockerCmd());
		getCommand("cb").setTabCompleter(new CommandBlockerTabComplete());
		getCommand("commandblocker").setExecutor(new CommandBlockerCmd());
		getCommand("commandblocker").setTabCompleter(new CommandBlockerTabComplete());
		if (Bukkit.getPluginManager().isPluginEnabled("ProtocolLib")) {
			Packets.protocol(this);
		}
		new UpdateManager().setup();
		updateCheck();
		fixCommands();
	}

	private void updateCheck() {
		String latestUpdate = UpdateChecker.request("5280",
				"Trey's Command Blocker v" + this.getDescription().getVersion() + " Bukkit");
		if (latestUpdate.equalsIgnoreCase("")) {
			return;
		}
		int latestUpdateVersion = Integer.parseInt(latestUpdate.replace(".", ""));
		int versionOn = Integer.parseInt(this.getDescription().getVersion().replace(".", ""));
		if (latestUpdateVersion <= versionOn) {
			return;
		}

		Bukkit.getScheduler().runTaskLaterAsynchronously(this, () -> {
			ConsoleCommandSender c = Bukkit.getConsoleSender();
			for (String msg2 : Messages.getMessages("Updates", "UpdateFound")) {
				c.sendMessage(ChatColor.translateAlternateColorCodes('&', msg2).replace("%s", latestUpdate));
			}
		}, 4L);
	}

	public static Boolean isPapiEnabled() {
		return Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI");
	}
	
	public void loadConfigManager() {
		if (!oldConfig()) {
			ConfigManager.reloadConfig();
			ConfigManager.reloadDisabled();
			ConfigManager.reloadOpDisabled();
		} else {
			OldConfigManager.reloadConfig();
			OldConfigManager.reloadDisabled();
			OldConfigManager.reloadOpDisabled();
		}
	}

	public static String getBukkitVersion() {
		return Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
	}

	public static void fixCommands() {
		ConfigurationSection configuration = oldConfig() ? OldConfigManager.getDisabled().getConfigurationSection(
				"DisabledCommands") : ConfigManager.getDisabled().getConfigurationSection("DisabledCommands");
		assert configuration != null;
		for (String cmds : configuration.getKeys(false)) {
			String path = cmds.substring(0, 1).toUpperCase() + cmds.substring(1).toLowerCase();
			if (!Character.isUpperCase(cmds.charAt(0))) {
				configuration.set(path, configuration.get(cmds));
				configuration.set(cmds, null);
				if (oldConfig()) {
					OldConfigManager.saveDisabled();
				} else {
					ConfigManager.saveDisabled();
				}
			}
			if (!cmds.substring(1).equals(cmds.substring(1).toLowerCase())) {
				configuration.set(path, configuration.get(cmds));
				configuration.set(cmds, null);
				if (oldConfig()) {
					OldConfigManager.saveDisabled();
				} else {
					ConfigManager.saveDisabled();
				}
			}
		}
	}

	public static boolean newBlocks() {
		if (getBukkitVersion().equalsIgnoreCase("v1_4_R1")) {
			return false;
		}
		if (getBukkitVersion().equalsIgnoreCase("v1_5_R1")) {
			return false;
		}
		if (getBukkitVersion().equalsIgnoreCase("v1_5_R2")) {
			return false;
		}
		if (getBukkitVersion().equalsIgnoreCase("v1_5_R3")) {
			return false;
		}
		if (getBukkitVersion().equalsIgnoreCase("v1_6_R1")) {
			return false;
		}
		if (getBukkitVersion().equalsIgnoreCase("v1_6_R2")) {
			return false;
		}
		if (getBukkitVersion().equalsIgnoreCase("v1_6_R3")) {
			return false;
		}
		if (getBukkitVersion().equalsIgnoreCase("v1_7_R1")) {
			return false;
		}
		if (getBukkitVersion().equalsIgnoreCase("v1_7_R2")) {
			return false;
		}
		if (getBukkitVersion().equalsIgnoreCase("v1_7_R3")) {
			return false;
		}
		if (getBukkitVersion().equalsIgnoreCase("v1_7_R4")) {
			return false;
		}
		if (getBukkitVersion().equalsIgnoreCase("v1_8_R1")) {
			return false;
		}
		if (getBukkitVersion().equalsIgnoreCase("v1_8_R2")) {
			return false;
		}
		if (getBukkitVersion().equalsIgnoreCase("v1_8_R3")) {
			return false;
		}
		if (getBukkitVersion().equalsIgnoreCase("v1_9_R1")) {
			return false;
		}
		if (getBukkitVersion().equalsIgnoreCase("v1_9_R2")) {
			return false;
		}
		if (getBukkitVersion().equalsIgnoreCase("v1_10_R1")) {
			return false;
		}
		if (getBukkitVersion().equalsIgnoreCase("v1_11_R1")) {
			return false;
		}
		if (getBukkitVersion().equalsIgnoreCase("v1_12_R1")) {
			return false;
		}
		return true;
	}
	
	public static boolean oldConfig() {
		if (getBukkitVersion().equalsIgnoreCase("v1_4_R1")) {
			return true;
		}
		if (getBukkitVersion().equalsIgnoreCase("v1_5_R1")) {
			return true;
		}
		if (getBukkitVersion().equalsIgnoreCase("v1_5_R2")) {
			return true;
		}
		if (getBukkitVersion().equalsIgnoreCase("v1_5_R3")) {
			return true;
		}
		if (getBukkitVersion().equalsIgnoreCase("v1_6_R1")) {
			return true;
		}
		if (getBukkitVersion().equalsIgnoreCase("v1_6_R2")) {
			return true;
		}
		if (getBukkitVersion().equalsIgnoreCase("v1_6_R3")) {
			return true;
		}
		if (getBukkitVersion().equalsIgnoreCase("v1_7_R1")) {
			return true;
		}
		return false;
	}

	public static boolean coloredGlassPane() {
		if (getBukkitVersion().equalsIgnoreCase("v1_4_R1")) {
			return false;
		}
		if (getBukkitVersion().equalsIgnoreCase("v1_5_R1")) {
			return false;
		}
		if (getBukkitVersion().equalsIgnoreCase("v1_5_R2")) {
			return false;
		}
		if (getBukkitVersion().equalsIgnoreCase("v1_5_R3")) {
			return false;
		}
		if (getBukkitVersion().equalsIgnoreCase("v1_6_R1")) {
			return false;
		}
		if (getBukkitVersion().equalsIgnoreCase("v1_6_R2")) {
			return false;
		}
		if (getBukkitVersion().equalsIgnoreCase("v1_6_R3")) {
			return false;
		}
		return true;
	}
}