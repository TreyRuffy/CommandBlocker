package me.treyruffy.commandblocker.bukkit;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public class PlaceholderAPITest {

	public static String testforPAPI(OfflinePlayer p, String s) {
		if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
			return PlaceholderAPI.setPlaceholders(p, s);
		}
		return s;
	}

}
