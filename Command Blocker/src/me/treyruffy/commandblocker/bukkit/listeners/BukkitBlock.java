package me.treyruffy.commandblocker.bukkit.listeners;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import me.clip.placeholderapi.PlaceholderAPI;
import me.treyruffy.commandblocker.bukkit.BukkitMain;
import me.treyruffy.commandblocker.bukkit.Variables;
import me.treyruffy.commandblocker.bukkit.config.ConfigManager;
import me.treyruffy.commandblockerlegacy.OldConfigManager;
import org.jetbrains.annotations.NotNull;

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

		FileConfiguration disabled = BukkitMain.oldConfig() ? OldConfigManager.getDisabled() :
				ConfigManager.getDisabled();
		FileConfiguration config = BukkitMain.oldConfig() ? OldConfigManager.getConfig() : ConfigManager.getConfig();

		if (config.getBoolean("ColonedCommands.Enabled")) {
			if (!config.getStringList("ColonedCommands.Worlds").contains("all")) {
				if (!config.getStringList("ColonedCommands.Worlds").contains(p.getWorld().getName())) {
					return false;
				}
			}

			if (config.getStringList("ColonedCommands.WhitelistedPlayers").contains(p.getUniqueId().toString())) {
				return false;
			}

			if (p.hasPermission(config.getString("ColonedCommands.Permission"))) {
				return false;
			}

			if (config.getBoolean("ColonedCommands.DisableAllColonsInCommands")) {
				if (args[0].startsWith("/") && args[0].contains(":")) {
					if (!config.getString("ColonedCommands.Message").replace(" ", "").equalsIgnoreCase("none")) {
						String message;
						if ((config.getString("ColonedCommands.Message") == null) || (config.getString(
								"ColonedCommands.Message").equalsIgnoreCase("default"))) {
							String msg = ChatColor.translateAlternateColorCodes('&', config.getString("Default" +
									".Message"));

							message = (BukkitMain.isPapiEnabled() ? PlaceholderAPI.setPlaceholders(p,
									Variables.translateVariables(msg, p)) : Variables.translateVariables(msg, p));
						} else {
							String msg = ChatColor.translateAlternateColorCodes('&', config.getString("ColonedCommands"
									+ ".Message"));

							message = (BukkitMain.isPapiEnabled() ? PlaceholderAPI.setPlaceholders(p,
									Variables.translateVariables(msg, p)) : Variables.translateVariables(msg, p));
						}
						// MannyLama's patch #9
						if (message.length() != 0) {
							p.sendMessage(message);
						}

						if ((config.getStringList("ColonedCommands.PlayerCommands").size() > 0) && (!config.getStringList("ColonedCommands.PlayerCommands").contains("none"))) {
							for (String s : config.getStringList("ColonedCommands.PlayerCommands")) {
								p.performCommand(Variables.translateVariables(s, p));
							}
						}

						if (config.getStringList("ColonedCommands.ConsoleCommands").size() > 0 && (!config.getStringList("ColonedCommands.ConsoleCommands").contains("none"))) {
							for (String s : config.getStringList("ColonedCommands.ConsoleCommands")) {
								Bukkit.dispatchCommand(Bukkit.getConsoleSender(), Variables.translateVariables(s, p));
							}
						}
					}
					return true;
				}
			} else {
				for (String c : config.getStringList("ColonedCommands.DisableColonsInFollowingCommands")) {
					if (args[0].toLowerCase().startsWith("/" + c.toLowerCase() + ":")) {
						if (!config.getString("ColonedCommands.Message").replace(" ", "").equalsIgnoreCase("none")) {
							String message;
							if ((config.getString("ColonedCommands.Message") == null) || (config.getString(
									"ColonedCommands.Message").equalsIgnoreCase("default"))) {
								String msg = ChatColor.translateAlternateColorCodes('&', config.getString("Default" +
										".Message"));

								message = (BukkitMain.isPapiEnabled() ? PlaceholderAPI.setPlaceholders(p,
										Variables.translateVariables(msg, p)) : Variables.translateVariables(msg, p));
							} else {
								String msg = ChatColor.translateAlternateColorCodes('&', config.getString(
										"ColonedCommands.Message"));

								message = (BukkitMain.isPapiEnabled() ? PlaceholderAPI.setPlaceholders(p,
										Variables.translateVariables(msg, p)) : Variables.translateVariables(msg, p));
							}
							// MannyLama's patch #9
							if (message.length() != 0) {
								p.sendMessage(message);
							}

							if ((config.getStringList("ColonedCommands.PlayerCommands").size() > 0) && (!config.getStringList("ColonedCommands.PlayerCommands").contains("none"))) {
								for (String s : config.getStringList("ColonedCommands.PlayerCommands")) {
									p.performCommand(Variables.translateVariables(s, p));
								}
							}

							if (config.getStringList("ColonedCommands.ConsoleCommands").size() > 0 && (!config.getStringList("ColonedCommands.ConsoleCommands").contains("none"))) {
								for (String s : config.getStringList("ColonedCommands.ConsoleCommands")) {
									Bukkit.dispatchCommand(Bukkit.getConsoleSender(), Variables.translateVariables(s,
											p));
								}
							}
						}
						return true;
					}
				}
			}
		}

		if (disabled.getConfigurationSection("DisabledCommands") == null) {
			return false;
		}

		for (String cmd : disabled.getConfigurationSection("DisabledCommands").getKeys(false)) {
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
					if (BukkitMain.oldConfig()) {
						OldConfigManager.saveConfig();
					} else {
						ConfigManager.saveConfig();
					}
				}

				if (!(disabled.getStringList("DisabledCommands." + cmd + ".Worlds").contains("all"))) {
					if (!disabled.getStringList("DisabledCommands." + cmd + ".Worlds").contains(p.getWorld().getName())) {
						return false;
					}
				}

				if ((disabled.getString("DisabledCommands." + cmd + ".Permission") == null) || (disabled.getString("DisabledCommands." + cmd + ".Permission").equalsIgnoreCase("default"))) {
					if (p.hasPermission(config.getString("Default.Permission").replace("%command%", permission))) {
						return false;
					}
				} else {
					if (p.hasPermission(disabled.getString("DisabledCommands." + cmd + ".Permission"))) {
						return false;
					}
				}

				if (!disabled.getString("DisabledCommands." + cmd + ".Message").replace(" ", "").equalsIgnoreCase("none")) {
					String message;
					if ((disabled.getString("DisabledCommands." + cmd + ".Message") == null) || (disabled.getString("DisabledCommands." + cmd + ".Message").equalsIgnoreCase("default"))) {
						String msg = ChatColor.translateAlternateColorCodes('&', config.getString("Default.Message"));

						message = (BukkitMain.isPapiEnabled() ? PlaceholderAPI.setPlaceholders(p, Variables.translateVariables(msg, p)) : Variables.translateVariables(msg, p));
					} else {
						String msg = ChatColor.translateAlternateColorCodes('&', disabled.getString("DisabledCommands." + cmd + ".Message"));

						message = (BukkitMain.isPapiEnabled() ? PlaceholderAPI.setPlaceholders(p, Variables.translateVariables(msg, p)) : Variables.translateVariables(msg, p));
					}
					// MannyLama's patch #9
					if (message.length() != 0){
						p.sendMessage(message);
					}
				}


				if ((disabled.getStringList("DisabledCommands." + cmd + ".PlayerCommands").size() > 0) && (!disabled.getStringList("DisabledCommands." + cmd + ".PlayerCommands").contains("none"))) {
					for (String s : disabled.getStringList("DisabledCommands." + cmd + ".PlayerCommands")) {
						p.performCommand(Variables.translateVariables(s, p).replace("%command%", cmds));
					}
				}

				if (disabled.getStringList("DisabledCommands." + cmd + ".ConsoleCommands").size() > 0 && (!disabled.getStringList("DisabledCommands." + cmd + ".ConsoleCommands").contains("none"))) {
					for (String s : disabled.getStringList("DisabledCommands." + cmd + ".ConsoleCommands")) {
						Bukkit.dispatchCommand(Bukkit.getConsoleSender(), Variables.translateVariables(s, p).replace(
								"%command%", cmds));
					}
				}
				return true;
			}
		}
		return false;
	}

	private @NotNull Boolean opBlocker(Player p, String[] args) {
		FileConfiguration config = BukkitMain.oldConfig() ? OldConfigManager.getConfig() : ConfigManager.getConfig();
		FileConfiguration opDisabled = BukkitMain.oldConfig() ? OldConfigManager.getOpDisabled() :
				ConfigManager.getOpDisabled();

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

			if (config.getBoolean("ColonedCommands.DisableAllColonsInCommands")) {
				if (args[0].startsWith("/") && args[0].contains(":")) {
					if (!config.getString("ColonedCommands.Message").replace(" ", "").equalsIgnoreCase("none")) {
						String message;
						if ((config.getString("ColonedCommands.Message") == null) || (config.getString(
								"ColonedCommands.Message").equalsIgnoreCase("default"))) {
							String msg = ChatColor.translateAlternateColorCodes('&', config.getString("Default" +
									".Message"));

							message = (BukkitMain.isPapiEnabled() ? PlaceholderAPI.setPlaceholders(p,
									Variables.translateVariables(msg, p)) : Variables.translateVariables(msg, p));
						} else {
							String msg = ChatColor.translateAlternateColorCodes('&', config.getString("ColonedCommands"
									+ ".Message"));

							message = (BukkitMain.isPapiEnabled() ? PlaceholderAPI.setPlaceholders(p,
									Variables.translateVariables(msg, p)) : Variables.translateVariables(msg, p));
						}
						// MannyLama's patch #9
						if (message.length() != 0) {
							p.sendMessage(message);
						}

						if ((config.getStringList("ColonedCommands.PlayerCommands").size() > 0) && (!config.getStringList("ColonedCommands.PlayerCommands").contains("none"))) {
							for (String s : config.getStringList("ColonedCommands.PlayerCommands")) {
								p.performCommand(Variables.translateVariables(s, p));
							}
						}

						if (config.getStringList("ColonedCommands.ConsoleCommands").size() > 0 && (!config.getStringList("ColonedCommands.ConsoleCommands").contains("none"))) {
							for (String s : config.getStringList("ColonedCommands.ConsoleCommands")) {
								Bukkit.dispatchCommand(Bukkit.getConsoleSender(), Variables.translateVariables(s, p));
							}
						}
					}
					return true;
				}
			} else {
				for (String c : config.getStringList("ColonedCommands.DisableColonsInFollowingCommands")) {
					if (args[0].toLowerCase().startsWith("/" + c.toLowerCase() + ":")) {
						if (!config.getString("ColonedCommands.Message").replace(" ", "").equalsIgnoreCase("none")) {
							String message;
							if ((config.getString("ColonedCommands.Message") == null) || (config.getString(
									"ColonedCommands.Message").equalsIgnoreCase("default"))) {
								String msg = ChatColor.translateAlternateColorCodes('&', config.getString("Default" +
										".Message"));

								message = (BukkitMain.isPapiEnabled() ? PlaceholderAPI.setPlaceholders(p,
										Variables.translateVariables(msg, p)) : Variables.translateVariables(msg, p));
							} else {
								String msg = ChatColor.translateAlternateColorCodes('&', config.getString(
										"ColonedCommands.Message"));

								message = (BukkitMain.isPapiEnabled() ? PlaceholderAPI.setPlaceholders(p,
										Variables.translateVariables(msg, p)) : Variables.translateVariables(msg, p));
							}
							// MannyLama's patch #9
							if (message.length() != 0) {
								p.sendMessage(message);
							}

							if ((config.getStringList("ColonedCommands.PlayerCommands").size() > 0) && (!config.getStringList("ColonedCommands.PlayerCommands").contains("none"))) {
								for (String s : config.getStringList("ColonedCommands.PlayerCommands")) {
									p.performCommand(Variables.translateVariables(s, p));
								}
							}

							if (config.getStringList("ColonedCommands.ConsoleCommands").size() > 0 && (!config.getStringList("ColonedCommands.ConsoleCommands").contains("none"))) {
								for (String s : config.getStringList("ColonedCommands.ConsoleCommands")) {
									Bukkit.dispatchCommand(Bukkit.getConsoleSender(), Variables.translateVariables(s,
											p));
								}
							}
						}
						return true;
					}
				}
			}
		}

		if (opDisabled.getConfigurationSection("DisabledOpCommands") == null) {
			return false;
		}

		for (String cmd : opDisabled.getConfigurationSection("DisabledOpCommands").getKeys(false)) {
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
					if (BukkitMain.oldConfig()) {
						OldConfigManager.saveConfig();
					} else {
						ConfigManager.saveConfig();
					}
				}
				
				if (!(opDisabled.getStringList("DisabledOpCommand." + cmd + ".Worlds").contains("all"))) {
					if (!opDisabled.getStringList("DisabledOpCommands." + cmd + ".Worlds").contains(p.getWorld().getName())) {
						return false;
					}
				}
				
				if (!opDisabled.getString("DisabledOpCommands." + cmd + ".Message").replace(" ", "").equalsIgnoreCase("none")) {
					String message;
					if ((opDisabled.getString("DisabledOpCommands." + cmd + ".Message") == null) || (opDisabled.getString("DisabledOpCommands." + cmd + ".Message").equalsIgnoreCase("default"))) {
						String msg = ChatColor.translateAlternateColorCodes('&', config.getString("Default.Message"));
						
						message = (BukkitMain.isPapiEnabled() ? PlaceholderAPI.setPlaceholders(p, Variables.translateVariables(msg, p)) : Variables.translateVariables(msg, p));
					} else {
						String msg = ChatColor.translateAlternateColorCodes('&', opDisabled.getString("DisabledOpCommands." + cmd + ".Message"));
						
						message = (BukkitMain.isPapiEnabled() ? PlaceholderAPI.setPlaceholders(p, Variables.translateVariables(msg, p)) : Variables.translateVariables(msg, p));
					}
					// MannyLama's patch #9
					if (message.length() != 0){
						p.sendMessage(message);
					}
				}
				
				
				if ((opDisabled.getStringList("DisabledOpCommands." + cmd + ".PlayerCommands").size() > 0) && (!opDisabled.getStringList("DisabledOpCommands." + cmd + ".PlayerCommands").contains("none"))) {
					for (String s : opDisabled.getStringList("DisabledOpCommands." + cmd + ".PlayerCommands")) {
						p.performCommand(Variables.translateVariables(s, p).replace("%command%", cmds));
					}
				}
				
				if ((opDisabled.getStringList("DisabledOpCommands." + cmd + ".ConsoleCommands").size() > 0) && (!opDisabled.getStringList("DisabledCommands." + cmd + ".ConsoleCommands").contains("none"))) {
					for (String s : opDisabled.getStringList("DisabledOpCommands." + cmd + ".ConsoleCommands")) {
						Bukkit.dispatchCommand(Bukkit.getConsoleSender(), Variables.translateVariables(s, p).replace("%command%", cmds));
					}
				}
				return true;
			}
		}
		return false;
	}

}