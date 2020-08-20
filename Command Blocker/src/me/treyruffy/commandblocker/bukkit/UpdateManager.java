package me.treyruffy.commandblocker.bukkit;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.bukkit.configuration.file.YamlConfiguration;

public class UpdateManager {

	public void setup() {
		try {
			File file = new File(BukkitMain.get().getDataFolder(), "config.yml");
			YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

			List<String> lines = FileUtils.readLines(file, Charset.defaultCharset());

			if (!config.contains("DisableOpTabComplete")) {
				int index = lines.lastIndexOf("DisableTabComplete");
				lines.addAll(index + 1, Arrays.asList("", "# Allows you to disable op tab complete!",
						"DisableOpTabComplete: true"));
			}
			if (!config.contains("ColonedCommands")) {
				lines.addAll(lines.size(), Arrays.asList("", "ColonedCommands:", "    Enabled: false", "", "    # " +
						"Allows you to disable commands like bukkit:help and essentials:help", "    " +
						"DisableAllColonsInCommands: false", "", "    # If the above value is false, which commands " +
						"do" + " you want to disable colons with", "    DisableColonsInFollowingCommands:", "        #" +
						" " + "Disables bukkit: commands like bukkit:help", "        - bukkit", "    Worlds:", "      " +
						"  - " + "all", "    Message: '&cYou cannot use \":\" in this command.'",
						"    Permission: cb" + ".allowcolonedcommands", "    WhitelistedPlayers:", "        - " +
								"\"49059600-26d6-45ac-8b43" + "-86a14f7d96ac\"", "    PlayerCommands:", "        - " +
								"none", "    ConsoleCommands:", "        -" + " none", "    DisableForOperators: " +
								"false"));
			}
			FileUtils.writeLines(file, lines);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	
}
