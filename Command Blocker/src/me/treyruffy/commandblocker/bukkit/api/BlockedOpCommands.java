package me.treyruffy.commandblocker.bukkit.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.configuration.file.FileConfiguration;

import me.treyruffy.commandblocker.bukkit.BukkitMain;
import me.treyruffy.commandblocker.bukkit.config.ConfigManager;
import me.treyruffy.commandblockerlegacy.OldConfigManager;

/**
 * @author TreyRuffy
 */
public class BlockedOpCommands {

	/**
	 *  Allows you to save the op blocked command list
	 * @return If the op blocked command list was saved
	 */
	public static boolean saveBlockedCommandList() {
		try {
			if (!BukkitMain.oldConfig()) {
				ConfigManager.saveOpDisabled();
			} else {
				OldConfigManager.saveOpDisabled();
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Allows you to add a command to be blocked for operators
	 * @param command The command name without "/"
	 * @param message The message displayed when the player executes the command
	 * @param worlds The worlds that the command should be blocked in. Can be all
	 * @param playerCommands Any commands to be executed by the player if they execute the command
	 * @param consoleCommands Any commands to be executed by the console if a player executes the command
	 * @return If the command was successfully added to the block list
	 */
	public static boolean addBlockedCommand(String command, String message, List<String> worlds, List<String> playerCommands, List<String> consoleCommands) {
		FileConfiguration disabled = BukkitMain.oldConfig() ? OldConfigManager.getOpDisabled() : ConfigManager.getOpDisabled();
		String cmd = command;
		if (Character.isLetter(command.charAt(0))) {
			cmd = command.substring(0, 1).toUpperCase() + command.substring(1).toLowerCase();
		}

		if (!disabled.contains("DisabledOpCommands." + cmd)) {
			if (message != null) {
				disabled.set("DisabledOpCommands." + cmd + ".Message", message.replace("§", "&"));
			}
			if (worlds != null) {
				disabled.set("DisabledOpCommands." + cmd + ".Worlds", worlds);
			}
			if (playerCommands != null) {
				disabled.set("DisabledOpCommands." + cmd + ".PlayerCommands", playerCommands);
			}
			if(consoleCommands != null) {
				disabled.set("DisabledOpCommands." + cmd + ".ConsoleCommands", consoleCommands);
			}
			
			if (!BukkitMain.oldConfig()) {
				ConfigManager.saveOpDisabled();
			} else {
				OldConfigManager.saveOpDisabled();
			}
			return true;
		}
		return false;
	}

	/**
	 * Allows you to edit the message displayed when a operator types the blocked command
	 * @param command The command to change the message of without the /
	 * @param message The new message to be displayed when a operator types the command
	 * @return If the message was successfully changed
	 */
	public static boolean editBlockedCommandMessage(String command, String message) {
		FileConfiguration disabled = BukkitMain.oldConfig() ? OldConfigManager.getOpDisabled() : ConfigManager.getOpDisabled();
		String cmd = command;
		if (Character.isLetter(command.charAt(0))) {
			cmd = command.substring(0, 1).toUpperCase() + command.substring(1).toLowerCase();
		}
		if (disabled.contains("DisabledOpCommands." + cmd)) {
			if (message == null) {
				return false;
			}
			disabled.set("DisabledOpCommands." + cmd + ".Message", message.replace("§", "&"));
			if (!BukkitMain.oldConfig()) {
				ConfigManager.saveOpDisabled();
			} else {
				OldConfigManager.saveOpDisabled();
			}
			return true;
		}
		return false;
	}

	/**
	 * Allows you to edit the worlds which the blocked command will be blocked
	 * @param command The command without /
	 * @param worlds The worlds which the command will be blocked in
	 * @return If the worlds were successfully changed
	 */
	public static boolean editBlockedCommandWorlds(String command, List<String> worlds) {
		FileConfiguration disabled = BukkitMain.oldConfig() ? OldConfigManager.getOpDisabled() : ConfigManager.getOpDisabled();
		String cmd = command;
		if (Character.isLetter(command.charAt(0))) {
			cmd = command.substring(0, 1).toUpperCase() + command.substring(1).toLowerCase();
		}

		if (disabled.contains("DisabledOpCommands." + cmd)) {
			if (worlds == null) {
				return false;
			}
			disabled.set("DisabledOpCommands." + cmd + ".Worlds", worlds);
			if (!BukkitMain.oldConfig()) {
				ConfigManager.saveOpDisabled();
			} else {
				OldConfigManager.saveOpDisabled();
			}
			return true;
		}
		return false;
	}

	/**
	 * Allows you to change the player commands executed when the operator executes the blocked command
	 * @param command The command without /
	 * @param playerCommands The player commands without / to execute when the operator executes the blocked command
	 * @return If the player commands were successfully changed
	 */
	public static boolean editBlockedCommandPlayerCommands(String command, List<String> playerCommands) {
		FileConfiguration disabled = BukkitMain.oldConfig() ? OldConfigManager.getOpDisabled() : ConfigManager.getOpDisabled();
		String cmd = command;
		if (Character.isLetter(command.charAt(0))) {
			cmd = command.substring(0, 1).toUpperCase() + command.substring(1).toLowerCase();
		}
		
		if (disabled.contains("DisabledOpCommands." + cmd)) {
			if (playerCommands == null) {
				return false;
			}
			disabled.set("DisabledOpCommands." + cmd + ".PlayerCommands", playerCommands);
			if (!BukkitMain.oldConfig()) {
				ConfigManager.saveOpDisabled();
			} else {
				OldConfigManager.saveOpDisabled();
			}
			return true;
		}
		return false;
	}

	/**
	 * Allows you to change the console commands executed when the operator executes the blocked command
	 * @param command The command without /
	 * @param consoleCommands The console commands without / to execute when the operator executes the blocked command
	 * @return If the console commands were successfully changed
	 */
	public static boolean editBlockedCommandConsoleCommands(String command, List<String> consoleCommands) {
		FileConfiguration disabled = BukkitMain.oldConfig() ? OldConfigManager.getOpDisabled() : ConfigManager.getOpDisabled();
		String cmd = command;
		if (Character.isLetter(command.charAt(0))) {
			cmd = command.substring(0, 1).toUpperCase() + command.substring(1).toLowerCase();
		}
		
		if (disabled.contains("DisabledOpCommands." + cmd)) {
			if (consoleCommands == null) {
				return false;
			}
			disabled.set("DisabledOpCommands." + cmd + ".ConsoleCommands", consoleCommands);
			if (!BukkitMain.oldConfig()) {
				ConfigManager.saveOpDisabled();
			} else {
				OldConfigManager.saveOpDisabled();
			}
			return true;
		}
		return false;
	}

	/**
	 * Allows you to get the message of a blocked command
	 * @param command The blocked command without /
	 * @return The message
	 */
	public static String getBlockedCommandMessage(String command) {
		FileConfiguration disabled = BukkitMain.oldConfig() ? OldConfigManager.getOpDisabled() : ConfigManager.getOpDisabled();
		String cmd = command;
		if (Character.isLetter(command.charAt(0))) {
			cmd = command.substring(0, 1).toUpperCase() + command.substring(1).toLowerCase();
		}
		
		if (disabled.contains("DisabledOpCommands." + cmd)) {
			if ((disabled.getString("DisabledOpCommands." + cmd + ".Message") != null) && !(disabled.getString("DisabledOpCommands." + cmd + ".Message").equalsIgnoreCase("default"))) {
				return disabled.getString("DisabledOpCommands." + cmd + ".Message");
			}
			FileConfiguration getConfig = BukkitMain.oldConfig() ? OldConfigManager.getConfig() : ConfigManager.getConfig();
			return getConfig.getString("Default.Message");
		}
		return null;
	}

	/**
	 * Allows you to get the worlds of a blocked command
	 * @param command The blocked command without /
	 * @return The worlds
	 */
	public static List<String> getBlockedCommandWorlds(String command) {
		FileConfiguration disabled = BukkitMain.oldConfig() ? OldConfigManager.getOpDisabled() : ConfigManager.getOpDisabled();
		String cmd = command;
		if (Character.isLetter(command.charAt(0))) {
			cmd = command.substring(0, 1).toUpperCase() + command.substring(1).toLowerCase();
		}
		
		if (disabled.contains("DisabledOpCommands." + cmd)) {
			if (disabled.getString("DisabledOpCommands." + cmd + ".Worlds") != null) {
				return disabled.getStringList("DisabledOpCommands." + cmd + ".Worlds");
			}
			List<String> worlds = new ArrayList<>();
			worlds.add("all");
			return worlds;
		}
		return null;
	}

	/**
	 * Allows you to get the player commands executed of a blocked command
	 * @param command The blocked command without /
	 * @return The player commands
	 */
	public static List<String> getBlockedCommandPlayerCommands(String command) {
		FileConfiguration disabled = BukkitMain.oldConfig() ? OldConfigManager.getOpDisabled() : ConfigManager.getOpDisabled();
		String cmd = command;
		if (Character.isLetter(command.charAt(0))) {
			cmd = command.substring(0, 1).toUpperCase() + command.substring(1).toLowerCase();
		}
		
		if (disabled.contains("DisabledOpCommands." + cmd)) {
			if (disabled.getString("DisabledOpCommands." + cmd + ".PlayerCommands") != null) {
				return disabled.getStringList("DisabledOpCommands." + cmd + ".PlayerCommands");
			}
			List<String> cmds = new ArrayList<>();
			cmds.add("none");
			return cmds;
		}
		return null;
	}

	/**
	 * Allows you to get the console commands executed of a blocked command
	 * @param command The blocked command without /
	 * @return The console commands
	 */
	public static List<String> getBlockedCommandConsoleCommands(String command) {
		FileConfiguration disabled = BukkitMain.oldConfig() ? OldConfigManager.getOpDisabled() : ConfigManager.getOpDisabled();
		String cmd = command;
		if (Character.isLetter(command.charAt(0))) {
			cmd = command.substring(0, 1).toUpperCase() + command.substring(1).toLowerCase();
		}
		
		if (disabled.contains("DisabledOpCommands." + cmd)) {
			if (disabled.getString("DisabledOpCommands." + cmd + ".ConsoleCommands") != null) {
				return disabled.getStringList("DisabledOpCommands." + cmd + ".ConsoleCommands");
			}
			List<String> cmds = new ArrayList<>();
			cmds.add("none");
			return cmds;
		}
		return null;
	}

	/**
	 * Allows you to remove a blocked command
	 * @param command The command to be removed
	 * @return If the command was removed successfully
	 */
	public static boolean removeBlockedCommand(String command) {
		FileConfiguration disabled = BukkitMain.oldConfig() ? OldConfigManager.getOpDisabled() : ConfigManager.getOpDisabled();
		String cmd = command;
		if (Character.isLetter(command.charAt(0))) {
			cmd = command.substring(0, 1).toUpperCase() + command.substring(1).toLowerCase();
		}
		
		if (disabled.contains("DisabledOpCommands." + cmd)) {
			disabled.set("DisabledOpCommands." + cmd, null);
			
			if (!BukkitMain.oldConfig()) {
				ConfigManager.saveOpDisabled();
			} else {
				OldConfigManager.saveOpDisabled();
			}
			return true;
		}
		return false;
	}

	/**
	 * Gets all blocked commands for operators
	 * @return The blocked commands
	 */
	public static Set<String> getBlockedCommands() {
		FileConfiguration disabled = BukkitMain.oldConfig() ? OldConfigManager.getOpDisabled() : ConfigManager.getOpDisabled();
		return disabled.getConfigurationSection("DisabledOpCommands").getKeys(false);
	}
}
