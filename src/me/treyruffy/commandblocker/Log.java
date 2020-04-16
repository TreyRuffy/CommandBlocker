package me.treyruffy.commandblocker;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

public class Log {

	public static void addLog(MethodInterface mi, String s) {
		try {
			List<String> log = getLog(mi);
			if (log == null) {
				mi.log(s);
				mi.log("&cTrey's Command Blocker: Could not access log.txt!");
				return;
			}
			Path file = Paths.get(mi.getDataFolder() + "/log.txt");
			LocalDateTime time = LocalDateTime.now();
			String month = time.getMonth().toString().substring(0, 3);
			int year = time.getYear();
			int day = time.getDayOfMonth();
			String hour = time.getHour() + "";
			if (hour.length() == 1) {
				hour = "0" + hour;
			}
			String minute = time.getMinute() + "";
			if (minute.length() == 1) {
				minute = "0" + minute;
			}
			String second = time.getSecond() + "";
			if (second.length() == 1) {
				second = "0" + second;
			}
			log.add("[" + month + " " + day + ", " + year + " | " + hour + ":" + minute + ":" + second + "] " + s);
			Files.write(file, log, StandardCharsets.UTF_8);
			return;
		} catch (IOException e) {
			e.printStackTrace();
		}
		mi.log(s);
		mi.log("&cTrey's Command Blocker: Could not access log.txt!");

	}
	
	public static List<String> getLog(MethodInterface mi) {
		try {
			File log = new File(mi.getDataFolder(), "log.txt");
			if (!log.exists()) {
				log.createNewFile();
			}
			Path file = Paths.get(mi.getDataFolder() + "/log.txt");
			return Files.readAllLines(file, StandardCharsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
