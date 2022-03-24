package me.treyruffy.commandblocker.bukkit.listeners;

import me.treyruffy.commandblocker.MethodInterface;
import me.treyruffy.commandblocker.Universal;
import me.treyruffy.commandblocker.bukkit.Variables;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BukkitBlock implements Listener{

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onCommand(@NotNull PlayerCommandPreprocessEvent e) {
		Player p = e.getPlayer();

		Boolean cancel = p.isOp() ? opBlocker(e.getPlayer(), e.getMessage().split(" ")) : blocker(e.getPlayer(),
				e.getMessage().split(" "));
		if (cancel) {
			e.setCancelled(true);
		}
	}

	private @NotNull Boolean blocker(Player p, String[] args) {

		MethodInterface mi = Universal.get().getMethods();
		FileConfiguration disabled = (FileConfiguration) mi.getDisabledCommandsConfig();
		FileConfiguration config = (FileConfiguration) mi.getConfig();

		if (config.getBoolean("ColonedCommands.Enabled")) {
			if (!config.getStringList("ColonedCommands.Worlds").contains("all")) {
				if (!config.getStringList("ColonedCommands.Worlds").contains(p.getWorld().getName())) {
					return false;
				}
			}

			if (config.getStringList("ColonedCommands.WhitelistedPlayers").contains(p.getUniqueId().toString())) {
				return false;
			}

			if (p.hasPermission(Objects.requireNonNull(config.getString("ColonedCommands.Permission")))) {
				return false;
			}

			if (disableColons(p, args, config, mi)) return true;
		}

		if (disabled.getConfigurationSection("DisabledCommands") == null) {
			return false;
		}

		for (String cmd : Objects.requireNonNull(disabled.getConfigurationSection("DisabledCommands")).getKeys(false)) {
			String cmds = cmd.replace("%colon%", ":");
			String[] cmdList = cmds.split(" ");

			if (args[0].equalsIgnoreCase("/" + cmdList[0])) {
				if (args.length != 1) {
					int i = 1, j = 0;
					for (String s : cmdList) {
						
						if (j != 0) {
							if (!s.equalsIgnoreCase(args[i])) {
								return false;
							}
							i++;
						}
						j = 1;
					}
				}
				if (disabled.getStringList("DisabledCommands." + cmd + ".WhitelistedPlayers").contains(p.getUniqueId().toString())) {
					return false;
				}

				String permission = cmd.replace(":", "").replace("%colon%", "").replace(" ", "");

				if (disabled.getStringList("DisabledCommands." + cmd + ".Worlds").isEmpty()) {
					List<String> a = new ArrayList<>();
					a.add("all");
					disabled.set("DisabledCommands." + cmd + ".Worlds", a);
					mi.saveConfig();
				}

				if (!(disabled.getStringList("DisabledCommands." + cmd + ".Worlds").contains("all"))) {
					if (!disabled.getStringList("DisabledCommands." + cmd + ".Worlds").contains(p.getWorld().getName())) {
						return false;
					}
				}

				if ((disabled.getString("DisabledCommands." + cmd + ".Permission") == null) || (Objects.requireNonNull(disabled.getString("DisabledCommands." + cmd + ".Permission")).equalsIgnoreCase("default"))) {
					if (p.hasPermission(Objects.requireNonNull(config.getString("Default.Permission")).replace("%command%", permission))) {
						return false;
					}
				} else {
					if (p.hasPermission(Objects.requireNonNull(disabled.getString("DisabledCommands." + cmd +
							".Permission")))) {
						return false;
					}
				}

				if ((disabled.getString("DisabledCommands." + cmd + ".Message") == null) || Objects.requireNonNull(disabled.getString("DisabledCommands." + cmd + ".Message")).equalsIgnoreCase("default")) {
					for (Component msg : mi.getOldMessages("Default", "Message", config)) {
						mi.sendMessage(p, Variables.translateVariables(msg, p));
					}
				} else if (!Objects.requireNonNull(disabled.getString("DisabledCommands." + cmd + ".Message")).replace(" ", "").equalsIgnoreCase("none")) {
					for (Component msg : mi.getOldMessages("DisabledCommands", cmd + ".Message", disabled)) {
						mi.sendMessage(p, Variables.translateVariables(msg, p));
					}
				}


				if ((disabled.getStringList("DisabledCommands." + cmd + ".PlayerCommands").size() > 0) && (!disabled.getStringList("DisabledCommands." + cmd + ".PlayerCommands").contains("none"))) {
					for (String s : disabled.getStringList("DisabledCommands." + cmd + ".PlayerCommands")) {
						p.performCommand(Variables.translateVariablesToString(s, p).replace("%command%", cmds));
					}
				}

				if (disabled.getStringList("DisabledCommands." + cmd + ".ConsoleCommands").size() > 0 && (!disabled.getStringList("DisabledCommands." + cmd + ".ConsoleCommands").contains("none"))) {
					for (String s : disabled.getStringList("DisabledCommands." + cmd + ".ConsoleCommands")) {
						Bukkit.dispatchCommand(Bukkit.getConsoleSender(), Variables.translateVariablesToString(s, p).replace(
								"%command%", cmds));
					}
				}
				return true;
			}
		}
		return false;
	}

	private @NotNull Boolean opBlocker(Player p, String[] args) {
		MethodInterface mi = Universal.get().getMethods();
		FileConfiguration config = (FileConfiguration) mi.getConfig();
		FileConfiguration opDisabled = (FileConfiguration) mi.getOpBlockConfig();

		if (config.getBoolean("ColonedCommands.Enabled")) {
			if (!config.getStringList("ColonedCommands.Worlds").contains("all")) {
				if (!config.getStringList("ColonedCommands.Worlds").contains(p.getWorld().getName())) {
					return false;
				}
			}

			if (config.getStringList("ColonedCommands.WhitelistedPlayers").contains(p.getUniqueId().toString())) {
				return false;
			}

			if (config.getBoolean("ColonedCommands.DisableForOperators")) {
				return false;
			}

			if (disableColons(p, args, config, mi)) return true;
		}

		if (opDisabled.getConfigurationSection("DisabledOpCommands") == null) {
			return false;
		}

		for (String cmd : Objects.requireNonNull(opDisabled.getConfigurationSection("DisabledOpCommands")).getKeys(false)) {
			String cmds = cmd.replace("%colon%", ":");

			String[] cmdList = cmds.split(" ");
			
			if (args[0].equalsIgnoreCase("/" + cmdList[0])) {
				if (args.length != 1) {
					int i = 1, j = 0;
					for (String s : cmdList) {
						if (j != 0) {
							if (!args[i].equalsIgnoreCase(s)) {
								return false;
							}
							i++;
						}
						j = 1;
					}
				}

				if (opDisabled.getStringList("DisabledOpCommands." + cmd + ".WhitelistedPlayers").contains(p.getUniqueId().toString())) {
					return false;
				}

				if (opDisabled.getStringList("DisabledOpCommands." + cmd + ".Worlds").isEmpty()) {
					List<String> a = new ArrayList<>();
					a.add("all");
					opDisabled.set("DisabledOpCommands." + cmd + ".Worlds", a);
					mi.saveConfig();
				}
				
				if (!(opDisabled.getStringList("DisabledOpCommand." + cmd + ".Worlds").contains("all"))) {
					if (!opDisabled.getStringList("DisabledOpCommands." + cmd + ".Worlds").contains(p.getWorld().getName())) {
						return false;
					}
				}


				if ((opDisabled.getString("DisabledOpCommands." + cmd + ".Message") == null) || (Objects.requireNonNull(opDisabled.getString("DisabledOpCommands." + cmd + ".Message")).equalsIgnoreCase("default"))) {
					for (Component msg : mi.getOldMessages("Default", "Message", config)) {
						mi.sendMessage(p, Variables.translateVariables(msg, p));
					}
				} else if (!(Objects.requireNonNull(opDisabled.getString("DisabledOpCommands." + cmd + ".Message"))).replace(" ", "").equalsIgnoreCase("none")) {
					for (Component msg : mi.getOldMessages("DisabledOpCommands", cmd + ".Message", opDisabled)) {
						mi.sendMessage(p, Variables.translateVariables(msg, p));
					}
				}
				
				
				if ((opDisabled.getStringList("DisabledOpCommands." + cmd + ".PlayerCommands").size() > 0) && (!opDisabled.getStringList("DisabledOpCommands." + cmd + ".PlayerCommands").contains("none"))) {
					for (String s : opDisabled.getStringList("DisabledOpCommands." + cmd + ".PlayerCommands")) {
						p.performCommand(Variables.translateVariablesToString(s, p).replace("%command%", cmds));
					}
				}
				
				if ((opDisabled.getStringList("DisabledOpCommands." + cmd + ".ConsoleCommands").size() > 0) && (!opDisabled.getStringList("DisabledCommands." + cmd + ".ConsoleCommands").contains("none"))) {
					for (String s : opDisabled.getStringList("DisabledOpCommands." + cmd + ".ConsoleCommands")) {
						Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
								Variables.translateVariablesToString(s, p).replace("%command%", cmds));
					}
				}
				return true;
			}
		}
		return false;
	}

	private boolean disableColons(Player p, String[] args, FileConfiguration config, MethodInterface mi) {
		if (config.getBoolean("ColonedCommands.DisableAllColonsInCommands")) {
			if (args[0].startsWith("/") && args[0].contains(":")) {
				return colonedCmdsExecute(p, config, mi);
			}
		} else {
			for (String c : config.getStringList("ColonedCommands.DisableColonsInFollowingCommands")) {
				if (args[0].toLowerCase().startsWith("/" + c.toLowerCase() + ":")) {
					return colonedCmdsExecute(p, config, mi);
				}
			}
		}
		return false;
	}

	private boolean colonedCmdsExecute(Player p, FileConfiguration config, MethodInterface mi) {
		if (!Objects.requireNonNull(config.getString("ColonedCommands.Message")).replace(" ", "").equalsIgnoreCase("none")) {
			colonedCmdsMessage(p, config, mi);

			if ((config.getStringList("ColonedCommands.PlayerCommands").size() > 0) && (!config.getStringList("ColonedCommands.PlayerCommands").contains("none"))) {
				for (String s : config.getStringList("ColonedCommands.PlayerCommands")) {
					p.performCommand(Variables.translateVariablesToString(s, p));
				}
			}

			if (config.getStringList("ColonedCommands.ConsoleCommands").size() > 0 && (!config.getStringList("ColonedCommands.ConsoleCommands").contains("none"))) {
				for (String s : config.getStringList("ColonedCommands.ConsoleCommands")) {
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), Variables.translateVariablesToString(s, p));
				}
			}
		}
		return true;
	}

	private void colonedCmdsMessage(Player p, FileConfiguration config, MethodInterface mi) {
		if ((config.getString("ColonedCommands.Message") == null) || (Objects.requireNonNull(config.getString(
				"ColonedCommands.Message")).equalsIgnoreCase("default"))) {
			for (Component msg : mi.getOldMessages("Default", "Message", config)) {
				mi.sendMessage(p, Variables.translateVariables(msg, p));
			}
		} else {
			for (Component msg : mi.getOldMessages("ColonedCommands", "Message", config)) {
				mi.sendMessage(p, Variables.translateVariables(msg, p));
			}
		}
	}

}