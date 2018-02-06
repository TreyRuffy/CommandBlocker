package me.treyruffy.commandblocker.Updater;

import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

import me.treyruffy.commandblocker.MethodInterface;
import me.treyruffy.commandblocker.Universal;

public class UpdateChecker {

	public static Boolean getLastUpdate(MethodInterface mi) {
		final String response = getFromURL("https://api.spigotmc.org/legacy/update.php?resource=5280");
		Boolean ver = true;
		if (response == null) {
			ver = false;
		} else if (response.equalsIgnoreCase(mi.getVersion())) {
			ver = false;
		} else {
			ver = true;
		}
		
		return ver;
    }
	
	
	public static String getFromURL(String surl) {
		String response = null;
		try {
			URL url = new URL(surl);
			Scanner s = new Scanner(url.openStream());
			if (s.hasNext()) {
				response = s.next();
				s.close();
			}
		} catch (IOException e) {
			System.out.println("Could not connect to URL: " + surl);
		}
		return response;
	}
}
