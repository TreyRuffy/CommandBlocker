package me.treyruffy.commandblocker.bungeecord.listeners;

import me.treyruffy.commandblocker.MethodInterface;
import me.treyruffy.commandblocker.Universal;
import me.treyruffy.commandblocker.bungeecord.Variables;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BungeeBlock implements Listener {
	
	@EventHandler (priority = EventPriority.HIGHEST)
	public void onCommand(ChatEvent e) {
		if (!(e.getSender() instanceof ProxiedPlayer)){
			return;
		}
		if (blocker((ProxiedPlayer) e.getSender(), e.getMessage().split(" ")))
			e.setCancelled(true);
	}

	private Boolean blocker(ProxiedPlayer p, String[] args) {

		MethodInterface mi = Universal.get().getMethods();
		Configuration disabled = (Configuration) mi.getDisabledCommandsConfig();
		Configuration config = (Configuration) mi.getConfig();

		if (config.getBoolean("ColonedCommands.Enabled")) {
			if (!config.getStringList("ColonedCommands.Servers").contains("all")) {
				if (!config.getStringList("ColonedCommands.Servers").contains(p.getServer().getInfo().getName())) {
					return false;
				}
			}

			if (config.getStringList("ColonedCommands.WhitelistedPlayers").contains(p.getUniqueId().toString())) {
				return false;
			}

			if (p.hasPermission("ColonedCommands.Permission")) {
				return false;
			}

			if (disableColons(p, args, config, mi)) return true;
		}
		if (disabled.getSection("DisabledCommands") == null) {
			return false;
		}

		for (String cmd : Objects.requireNonNull(disabled.getSection("DisabledCommands")).getKeys()) {
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

				if (disabled.getStringList("DisabledCommands." + cmd + ".Servers").isEmpty()) {
					List<String> a = new ArrayList<>();
					a.add("all");
					disabled.set("DisabledCommands." + cmd + ".Servers", a);
					mi.saveConfig();
				}

				if (!(disabled.getStringList("DisabledCommands." + cmd + ".Servers").contains("all"))) {
					if (!disabled.getStringList("DisabledCommands." + cmd + ".Servers").contains(p.getServer().getInfo().getName())) {
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
					for (String msg : mi.getOldMessages("Default", "Message", config)) {
						mi.sendMessage(p, Variables.translateVariables(msg, p));
					}
				} else if (!Objects.requireNonNull(disabled.getString("DisabledCommands." + cmd + ".Message")).replace(" ", "").equalsIgnoreCase("none")) {
					for (String msg : mi.getOldMessages("DisabledCommands", cmd + ".Message", disabled)) {
						mi.sendMessage(p, Variables.translateVariables(msg, p));
					}
				}


				if ((disabled.getStringList("DisabledCommands." + cmd + ".PlayerCommands").size() > 0) && (!disabled.getStringList("DisabledCommands." + cmd + ".PlayerCommands").contains("none"))) {
					for (String s : disabled.getStringList("DisabledCommands." + cmd + ".PlayerCommands")) {
						p.chat("/" + Variables.translateVariablesToString(s, p).replace("%command%", cmds));
					}
				}

				if (disabled.getStringList("DisabledCommands." + cmd + ".ConsoleCommands").size() > 0 && (!disabled.getStringList("DisabledCommands." + cmd + ".ConsoleCommands").contains("none"))) {
					for (String s : disabled.getStringList("DisabledCommands." + cmd + ".ConsoleCommands")) {
						mi.executeCommand(Variables.translateVariablesToString(s, p).replace(
								"%command%", cmds));
					}
				}
				return true;
			}
		}
		return false;
	}

	private boolean disableColons(ProxiedPlayer p, String[] args, Configuration config, MethodInterface mi) {
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

	private boolean colonedCmdsExecute(ProxiedPlayer p, Configuration config, MethodInterface mi) {
		if (!Objects.requireNonNull(config.getString("ColonedCommands.Message")).replace(" ", "").equalsIgnoreCase("none")) {
			colonedCmdsMessage(p, config, mi);

			if ((config.getStringList("ColonedCommands.PlayerCommands").size() > 0) && (!config.getStringList("ColonedCommands.PlayerCommands").contains("none"))) {
				for (String s : config.getStringList("ColonedCommands.PlayerCommands")) {
					p.chat("/" + Variables.translateVariablesToString(s, p));
				}
			}

			if (config.getStringList("ColonedCommands.ConsoleCommands").size() > 0 && (!config.getStringList("ColonedCommands.ConsoleCommands").contains("none"))) {
				for (String s : config.getStringList("ColonedCommands.ConsoleCommands")) {
					mi.executeCommand(Variables.translateVariablesToString(s, p));
				}
			}
		}
		return true;
	}

	private void colonedCmdsMessage(ProxiedPlayer p, Configuration config, MethodInterface mi) {
		if ((config.getString("ColonedCommands.Message") == null) || (Objects.requireNonNull(config.getString(
				"ColonedCommands.Message")).equalsIgnoreCase("default"))) {
			for (String msg : mi.getOldMessages("Default", "Message", config)) {
				mi.sendMessage(p, Variables.translateVariables(msg, p));
			}
		} else {
			for (String msg : mi.getOldMessages("ColonedCommands", "Message", config)) {
				mi.sendMessage(p, Variables.translateVariables(msg, p));
			}
		}
	}
}