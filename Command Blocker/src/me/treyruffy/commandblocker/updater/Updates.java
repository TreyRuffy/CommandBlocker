package me.treyruffy.commandblocker.updater;

import me.treyruffy.commandblocker.MethodInterface;

public class Updates {

	public static void updateCheck(MethodInterface mi) {
		String latestUpdate = UpdateChecker.request("5280", "TreysCommandBlocker v" + mi.getVersion() + " " + mi.getServerType());
		if (latestUpdate.equalsIgnoreCase("")) {
			return;
		}
		int latestUpdateVersion = Integer.parseInt(latestUpdate.replace(".", ""));
		int versionOn = Integer.parseInt(mi.getVersion().replace(".", ""));
		if (latestUpdateVersion <= versionOn) {
			return;
		}
		
		mi.log("&b+=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=+");
		mi.log("&aThere is a new update (" + latestUpdate + ") for");
		mi.log("&aTrey's Command Blocker");
		mi.log("&cDownload at:");
		mi.log("&dhttps://www.spigotmc.org/resources/5280/");
		mi.log("&b+=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=+");
	}
	
	
}
