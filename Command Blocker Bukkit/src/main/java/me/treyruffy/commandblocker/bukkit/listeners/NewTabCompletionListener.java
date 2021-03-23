package me.treyruffy.commandblocker.bukkit.listeners;

import me.treyruffy.commandblocker.MethodInterface;
import me.treyruffy.commandblocker.Universal;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandSendEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class NewTabCompletionListener implements Listener {

	@EventHandler
	public void onTabComplete(PlayerCommandSendEvent e) {
		MethodInterface mi = Universal.get().getMethods();
		FileConfiguration config = (FileConfiguration) mi.getConfig();
		if (!config.getBoolean("DisableTabComplete")) {
			return;
		}
		Player p = e.getPlayer();
		if (p.hasPermission("cb.bypasstab")) {
			return;
		}
		FileConfiguration disabled = (FileConfiguration) mi.getDisabledCommandsConfig();

		List<String> commandsToRemove = new ArrayList<>();
		if (config.getBoolean("ColonedCommands.Enabled")) {
			commandsToRemove = colonedCommands(e, config, p);
		}

		if (disabled.getConfigurationSection("DisabledCommands") == null) {
			return;
		}

		for (String cmd : Objects.requireNonNull(disabled.getConfigurationSection("DisabledCommands")).getKeys(false)) {
			String cmds = cmd.replace("%colon%", ":").toLowerCase();
			String permission = cmd.replace("%colon%", "").replace(":", "").replace(" ", "");
			if (!disabled.getStringList("DisabledCommands." + cmd + ".WhitelistedPlayers").contains(p.getUniqueId().toString())) {
				if (disabled.getString("DisabledCommands." + cmd + ".Permission") == null) {
					if (!(p.hasPermission(Objects.requireNonNull(config.getString("Default.Permission")).replace("%command%",
							permission)))) {
						commandsToRemove.add(cmds);
					}
				} else {
					if (!(p.hasPermission(Objects.requireNonNull(disabled.getString("DisabledCommands." + cmd +
							".Permission"))))) {
						if (disabled.getString("DisabledCommands." + cmd + ".NoTabComplete") == null
								|| disabled.getBoolean("DisabledCommands." + cmd + ".NoTabComplete")) {
							if (!(disabled.getStringList("DisabledCommands." + cmd + ".Worlds").contains("all"))) {
								if (disabled.getStringList("DisabledCommands." + cmd + ".Worlds").contains(p.getWorld().getName())) {
									e.getCommands().remove(cmds);
								}
							}
							e.getCommands().remove(cmds);
						}
					}
				}
			}
		}
		for (String commands : commandsToRemove) {
			e.getCommands().remove(commands);
		}
	}

	@EventHandler
	public void onOperatorTabComplete(PlayerCommandSendEvent e) {
		MethodInterface mi = Universal.get().getMethods();
		FileConfiguration config = (FileConfiguration) mi.getConfig();
		if (!config.getBoolean("DisableOpTabComplete")) {
			return;
		}
		Player p = e.getPlayer();
		if (!p.isOp()) {
			return;
		}
		FileConfiguration disabled = (FileConfiguration) mi.getOpBlockConfig();

		List<String> commandsToRemove = new ArrayList<>();

		if (config.getBoolean("ColonedCommands.Enabled") && config.getBoolean("ColonedCommands" +
				".DisableForOperators")) {
			commandsToRemove = colonedCommands(e, config, p);
		}
		if (disabled.getConfigurationSection("DisabledOpCommands") == null) {
			return;
		}

		for (String cmd :
				Objects.requireNonNull(disabled.getConfigurationSection("DisabledOpCommands")).getKeys(false)) {
			String cmds = cmd.replace("%colon%", ":").toLowerCase();
			String permission = cmd.replace("%colon%", "").replace(":", "").replace(" ", "");
			if (!disabled.getStringList("DisabledOpCommands." + cmd + ".WhitelistedPlayers").contains(p.getUniqueId().toString())) {
				if (disabled.getString("DisabledOpCommands." + cmd + ".Permission") == null) {
					if (!(p.hasPermission(Objects.requireNonNull(config.getString("Default.Permission")).replace("%command%",
							permission)))) {
						e.getCommands().remove(cmds);
					}
				} else {
					if (!(p.hasPermission(Objects.requireNonNull(disabled.getString("DisabledOpCommands." + cmd +
							".Permission"))))) {
						if (disabled.getString("DisabledOpCommands." + cmd + ".NoTabComplete") == null
								|| disabled.getBoolean("DisabledOpCommands." + cmd + ".NoTabComplete")) {
							if (!(disabled.getStringList("DisabledOpCommands." + cmd + ".Worlds").contains("all"))) {
								if (disabled.getStringList("DisabledOpCommands." + cmd + ".Worlds").contains(p.getWorld().getName())) {
									e.getCommands().remove(cmds);
								}
							}
							e.getCommands().remove(cmds);
						}
					}
				}
			}
		}
		for (String commands : commandsToRemove) {
			e.getCommands().remove(commands);
		}
	}

	private List<String> colonedCommands(PlayerCommandSendEvent e, FileConfiguration config, Player p) {
		List<String> commandsToRemove = new ArrayList<>();
		boolean b = false;
		if (config.getStringList("ColonedCommands.Worlds").isEmpty() || config.getStringList("ColonedCommands" +
				".Worlds").indexOf("all") != -1) {
			b = true;
		} else {
			for (String world : config.getStringList("ColonedCommands.Worlds")) {
				if (world.equalsIgnoreCase(p.getWorld().getName())) {
					b = true;
					break;
				}
			}
		}
		if (b) {
			if (config.getBoolean("ColonedCommands.DisableAllColonsInCommands")) {
				e.getCommands().removeIf(commands -> commands.contains(":"));
			} else {
				for (String command : e.getCommands()) {
					for (String commandsToRemoveColons : config.getStringList("ColonedCommands" +
							".DisableColonsInFollowingCommands")) {
						if (command.startsWith(commandsToRemoveColons.toLowerCase()))
							commandsToRemove.add(command);
					}
				}
			}
		}
		return commandsToRemove;
	}
}
