package me.treyruffy.commandblocker;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class CopyConfigs {

	public static void duplicateOldSettings(MethodInterface mi){
		try {
			File config = new File(mi.getDataFolder(), "config.yml");
			if (config.exists()) {
				Path file = Paths.get(mi.getDataFolder() + "/config.yml");
				List<String> values = Files.readAllLines(file, StandardCharsets.UTF_8);
				File oldConfig = new File(mi.getDataFolder(), "old-config.yml");
				if (!oldConfig.exists()) {
					oldConfig.createNewFile();
					Path oldFile = Paths.get(mi.getDataFolder() + "/old-config.yml");
					Files.write(oldFile, values, StandardCharsets.UTF_8);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			File disabled = new File(mi.getDataFolder(), "disabled.yml");
			if (disabled.exists()) {
				Path file = Paths.get(mi.getDataFolder() + "/disabled.yml");
				List<String> values = Files.readAllLines(file, StandardCharsets.UTF_8);
				File oldDisabled = new File(mi.getDataFolder(), "old-disabled.yml");
				if (!oldDisabled.exists()) {
					oldDisabled.createNewFile();
					Path oldFile = Paths.get(mi.getDataFolder() + "/old-disabled.yml");
					Files.write(oldFile, values, StandardCharsets.UTF_8);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			File opblock = new File(mi.getDataFolder(), "opblock.yml");
			if (opblock.exists()) {
				Path file = Paths.get(mi.getDataFolder() + "/opblock.yml");
				List<String> values = Files.readAllLines(file, StandardCharsets.UTF_8);
				File oldOpBlock = new File(mi.getDataFolder(), "old-opblock.yml");
				if (!oldOpBlock.exists()) {
					oldOpBlock.createNewFile();
					Path oldFile = Paths.get(mi.getDataFolder() + "/old-opblock.yml");
					Files.write(oldFile, values, StandardCharsets.UTF_8);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
