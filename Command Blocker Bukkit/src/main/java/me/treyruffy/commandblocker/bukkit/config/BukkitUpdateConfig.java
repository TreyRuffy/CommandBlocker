package me.treyruffy.commandblocker.bukkit.config;

import me.treyruffy.commandblocker.MethodInterface;
import me.treyruffy.commandblocker.Universal;
import org.apache.commons.io.FileUtils;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class BukkitUpdateConfig {

	// Updates the config
	public void setup() {
		MethodInterface mi = Universal.get().getMethods();
		YamlConfiguration config = (YamlConfiguration) mi.getConfig();
		if (config.getString("Version") == null) {
			config.set("Version", mi.getVersion());
			mi.saveConfig();
			return;
		}

		try {
			File source = new File(mi.getDataFolder() + File.separator + "config.yml");
			File dest = new File(mi.getDataFolder() + File.separator + "config.yml.old");
			if (!dest.exists())
				if (!dest.createNewFile()) {
					return;
				}
			FileUtils.copyFile(source, dest);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			File source = new File(mi.getDataFolder() + File.separator + "messages.yml");
			File dest = new File(mi.getDataFolder() + File.separator + "messages.yml.old");
			if (!dest.exists())
				if (!dest.createNewFile()) {
					return;
				}
			FileUtils.copyFile(source, dest);
		} catch (IOException e) {
			e.printStackTrace();
		}

		int lastVersion = Integer.parseInt(Objects.requireNonNull(config.getString("Version")).replace(".", ""));

		YamlConfiguration messageConfig = (YamlConfiguration) mi.getMessagesConfig();

		if (lastVersion < 210) {
			String noArguments = messageConfig.getString("Main.NoArguments");
			assert noArguments != null;
			if (!noArguments.contains("list"))
				messageConfig.set("Main.NoArguments",
						noArguments.replace("remove, edit", "remove, list, edit").replace("removeOP, editOP",
								"removeOP, listOP, editOP"));

			String couldNotAddCommandToConfig = messageConfig.getString("Main.CouldNotAddCommandToConfig");
			assert couldNotAddCommandToConfig != null;
			if (couldNotAddCommandToConfig.contains("disable.yml"))
				messageConfig.set("Main.CouldNotAddCommandToConfig",
						couldNotAddCommandToConfig.replace("disable.yml", "disabled.yml"));

			config.set("ColonedCommands.DisableTabComplete", true);

			messageConfig.set("ListOutOfBounds", "");
			messageConfig.set("ListStart", "");
			messageConfig.set("ListCommands", "");
			messageConfig.set("ListEnd", "");
			messageConfig.set("ListBackButton", "");
			messageConfig.set("ListBackButtonBeginning", "");
			messageConfig.set("ListForwardButton", "");
			messageConfig.set("ListForwardButtonEnd", "");
			messageConfig.set("ListOpOutOfBounds", "");
			messageConfig.set("ListOpStart", "");
			messageConfig.set("ListOpCommands", "");
			messageConfig.set("ListOpEnd", "");
			messageConfig.set("ListOpBackButton", "");
			messageConfig.set("ListOpBackButtonBeginning", "");
			messageConfig.set("ListOpForwardButton", "");
			messageConfig.set("ListOpForwardButtonEnd", "");

		}

		config.set("Version", mi.getVersion());
		mi.saveConfig();
		mi.saveMessagesConfig();
	}

}
