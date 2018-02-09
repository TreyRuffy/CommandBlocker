package me.treyruffy.commandblocker.updater;

import me.treyruffy.commandblocker.Universal;
import me.treyruffy.commandblocker.MethodInterface;

public class Updates {

	public static void updateCheck(MethodInterface mi){
		Boolean updates = UpdateChecker.getLastUpdate(mi);
		if (updates){
			mi.log("&b+=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=+");
			mi.log("&aThere is a new update for");
			mi.log("&aCommand Blocker");
			mi.log("&cDownload at:");
			mi.log("&dhttps://www.spigotmc.org/resources/5280/");
			mi.log("&b+=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=+");
		}
	}
	
	
}
