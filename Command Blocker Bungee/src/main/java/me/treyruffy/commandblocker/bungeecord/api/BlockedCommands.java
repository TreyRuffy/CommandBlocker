package me.treyruffy.commandblocker.bungeecord.api;

import me.treyruffy.commandblocker.bungeecord.config.BungeeConfigManager;
import net.md_5.bungee.config.Configuration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * The type Blocked commands.
 *
 * @author TreyRuffy
 */
@SuppressWarnings("unused")
public class BlockedCommands {
	/**
	 * Allows you to save the blocked command list
	 *
	 * @return If the blocked command list was saved
	 */
	public static boolean saveBlockedCommandList() {
		try {
			BungeeConfigManager.saveDisabled();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Allows you to add a command to be blocked
	 *
	 * @param command The command name without "/"
	 * @param permission The permission for players to have the command unblocked for them
	 * @param message The message displayed when the player executes the command
	 * @param servers The servers that the command should be blocked in. Can be all
	 * @param playerCommands Any commands to be executed by the player if they execute the command
	 * @return If the command was successfully added to the block list
	 */
	public static boolean addBlockedCommand(String command, String permission, String message, List<String> servers, List<String> playerCommands) {
	    Configuration disabled = BungeeConfigManager.getDisabled();
	    String cmd = command.substring(0, 1).toUpperCase() + command.substring(1).toLowerCase();

	    if (!disabled.getSection("DisabledCommands").getKeys().contains(cmd)) {
			if (permission != null) {
				disabled.set("DisabledCommands." + cmd + ".Permission", permission);
			}
			if (message != null) {
				disabled.set("DisabledCommands." + cmd + ".Message", message.replace("§", "&"));
			}
			if (servers != null) {
				disabled.set("DisabledCommands." + cmd + ".Servers", servers);
			}
			if (playerCommands != null) {
				disabled.set("DisabledCommands." + cmd + ".PlayerCommands", playerCommands);
			}

			BungeeConfigManager.saveDisabled();
			return true;
	    }
	    return false;
	}

	/**
	 * Allows you to edit the permission of the blocked command
	 *
	 * @param command The command to change the permission of without /
	 * @param permission The new permission
	 * @return If the permission was successfully changed
	 */
	public static boolean editBlockedCommandPermission(String command, String permission) {
		Configuration disabled = BungeeConfigManager.getDisabled();
		String cmd = command.substring(0, 1).toUpperCase() + command.substring(1).toLowerCase();

		if (disabled.getSection("DisabledCommands").getKeys().contains(cmd) && permission != null) {
			disabled.set("DisabledCommands." + cmd + ".Permission", permission);

			BungeeConfigManager.saveDisabled();
			return true;
		}
		return false;
	}

	/**
	 * Allows you to edit the message displayed when a player types the blocked command
	 *
	 * @param command The command to change the message of without the /
	 * @param message The new message to be displayed when a player types the command
	 * @return If the message was successfully changed
	 */
	public static boolean editBlockedCommandMessage(String command, String message) {
		Configuration disabled = BungeeConfigManager.getDisabled();
		String cmd = command.substring(0, 1).toUpperCase() + command.substring(1).toLowerCase();
		if (disabled.getSection("DisabledCommands").getKeys().contains(cmd) && message != null) {
			disabled.set("DisabledCommands." + cmd + ".Message", message.replace("§", "&"));

			BungeeConfigManager.saveDisabled();
			return true;
		}
		return false;
	}

	/**
	 * Allows you to edit the servers which the blocked command will be blocked
	 *
	 * @param command The command without /
	 * @param servers The servers which the command will be blocked in
	 * @return If the servers were successfully changed
	 */
	public static boolean editBlockedCommandServers(String command, List<String> servers) {
		Configuration disabled = BungeeConfigManager.getDisabled();
		String cmd = command.substring(0, 1).toUpperCase() + command.substring(1).toLowerCase();

		if (disabled.getSection("DisabledCommands").getKeys().contains(cmd) && servers != null) {
			disabled.set("DisabledCommands." + cmd + ".Servers", servers);

			BungeeConfigManager.saveDisabled();
			return true;
		}
		return false;
	}

	/**
	 * Allows you to change the player commands executed when the player executes the blocked command
	 *
	 * @param command The command without /
	 * @param playerCommands The player commands without / to execute when the player executes the blocked command
	 * @return If the player commands were successfully changed
	 */
	public static boolean editBlockedCommandPlayerCommands(String command, List<String> playerCommands) {
		Configuration disabled = BungeeConfigManager.getDisabled();
		String cmd = command.substring(0, 1).toUpperCase() + command.substring(1).toLowerCase();

		if (disabled.getSection("DisabledCommands").getKeys().contains(cmd) && playerCommands != null) {
			disabled.set("DisabledCommands." + cmd + ".PlayerCommands", playerCommands);
			
			BungeeConfigManager.saveDisabled();
			return true;
		}
		return false;
	}

	/**
	 * Allows you to get the permission of a blocked command
	 *
	 * @param command The blocked command without /
	 * @return The permission
	 */
	public static String getBlockedCommandPermission(String command) {
		Configuration disabled = BungeeConfigManager.getDisabled();
		String cmd = command.substring(0, 1).toUpperCase() + command.substring(1).toLowerCase();

		if (disabled.getSection("DisabledCommands").getKeys().contains(cmd)) {
			if ((disabled.getString("DisabledCommands." + cmd + ".Permission") != null)
					&& !(disabled.getString("DisabledCommands." + cmd + ".Permission").equalsIgnoreCase("default"))) {
				return disabled.getString("DisabledCommands." + cmd + ".Permission");
			}
			Configuration getConfig = BungeeConfigManager.getConfig();
			return getConfig.getString("Default.Permission").replace("%command%", cmd.toLowerCase());
		}
		return null;
	}

	/**
	 * Allows you to get the message of a blocked command
	 *
	 * @param command The blocked command without /
	 * @return The message
	 */
	public static String getBlockedCommandMessage(String command) {
		Configuration disabled = BungeeConfigManager.getDisabled();
		String cmd = command.substring(0, 1).toUpperCase() + command.substring(1).toLowerCase();

		if (disabled.getSection("DisabledCommands").getKeys().contains(cmd)) {
			if ((disabled.getString("DisabledCommands." + cmd + ".Message") != null)
					&& !(disabled.getString("DisabledCommands." + cmd + ".Message").equalsIgnoreCase("default"))) {
				return disabled.getString("DisabledCommands." + cmd + ".Message");
			}
			Configuration getConfig = BungeeConfigManager.getConfig();
			return getConfig.getString("Default.Message");
		}
		return null;
	}

	/**
	 * Allows you to get the servers of a blocked command
	 *
	 * @param command The blocked command without /
	 * @return The servers
	 */
	public static List<String> getBlockedCommandServers(String command) {
		Configuration disabled = BungeeConfigManager.getDisabled();
		String cmd = command.substring(0, 1).toUpperCase() + command.substring(1).toLowerCase();

		if (disabled.getSection("DisabledCommands").getKeys().contains(cmd)) {
			if (disabled.getString("DisabledCommands." + cmd + ".Servers") != null) {
				return disabled.getStringList("DisabledCommands." + cmd + ".Servers");
			}
			List<String> servers = new ArrayList<>();
			servers.add("all");
			return servers;
		}
		return null;
	}

	/**
	 * Allows you to get the player commands executed of a blocked command
	 *
	 * @param command The blocked command without /
	 * @return The player commands
	 */
	public static List<String> getBlockedCommandPlayerCommands(String command) {
		Configuration disabled = BungeeConfigManager.getDisabled();
		String cmd = command.substring(0, 1).toUpperCase() + command.substring(1).toLowerCase();

		if (disabled.getSection("DisabledCommands").getKeys().contains(cmd)) {
			if (disabled.getString("DisabledCommands." + cmd + ".PlayerCommands") != null) {
				return disabled.getStringList("DisabledCommands." + cmd + ".PlayerCommands");
			}
			List<String> cmds = new ArrayList<>();
			cmds.add("none");
			return cmds;
		}
		return null;
	}

	/**
	 * Allows you to remove a blocked command
	 *
	 * @param command The command to be removed
	 * @return If the command was removed successfully
	 */
	public static boolean removeBlockedCommand(String command) {
		Configuration disabled = BungeeConfigManager.getDisabled();
		String cmd = command.substring(0, 1).toUpperCase() + command.substring(1).toLowerCase();

		if (disabled.getSection("DisabledCommands").getKeys().contains(cmd)) {
			disabled.set("DisabledCommands." + cmd, null);

			BungeeConfigManager.saveDisabled();
			return true;
		}
		return false;
	}

	/**
	 * Gets all blocked commands
	 *
	 * @return The blocked commands
	 */
	public static Collection<String> getBlockedCommands() {
		Configuration disabled = BungeeConfigManager.getDisabled();
		return disabled.getSection("DisabledCommands").getKeys();
	}
}
