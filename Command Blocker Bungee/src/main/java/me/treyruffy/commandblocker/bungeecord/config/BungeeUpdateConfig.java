package me.treyruffy.commandblocker.bungeecord.config;

import me.treyruffy.commandblocker.MethodInterface;
import me.treyruffy.commandblocker.Universal;
import net.md_5.bungee.config.Configuration;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class BungeeUpdateConfig {

	// Updates the config
	public void setup() {
		MethodInterface mi = Universal.get().getMethods();
		Configuration config = (Configuration) mi.getConfig();
		if (config.getString("Version") == null || config.getString("Version").equals("")) {
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

		Configuration messageConfig = (Configuration) mi.getMessagesConfig();

		if (lastVersion < 210) {
			String noArguments = messageConfig.getString("Main.BungeeNoArguments");
			assert noArguments != null;
			if (!noArguments.contains("list"))
				messageConfig.set("Main.BungeeNoArguments",
						noArguments.replace("remove, and", "remove, list, and"));

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
		}

		config.set("Version", mi.getVersion());
		mi.saveConfig();
		mi.saveMessagesConfig();
	}
}
