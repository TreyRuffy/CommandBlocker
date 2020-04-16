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
				lines.addAll(index + 1, Arrays.asList(
						"",
						"# Allows you to disable op tab complete!",
						"DisableOpTabComplete: true"
				));
			}
			FileUtils.writeLines(file, lines);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	
}
