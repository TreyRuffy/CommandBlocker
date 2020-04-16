package me.treyruffy.commandblocker.bukkit.gui;

import me.treyruffy.commandblocker.bukkit.BukkitMain;

public class LegacyBlocks {

	public String getBlock(String block) {
		switch (block) {
			case "WHITE_WOOL":
				if (BukkitMain.newBlocks()) {
					return "WHITE_WOOL";
				} else {
					return "WOOL";
				}
			case "RED_WOOL":
				if (BukkitMain.newBlocks()) {
					return "RED_WOOL";
				} else {
					return "WOOL";
				}
			case "GREEN_WOOL":
				if (BukkitMain.newBlocks()) {
					return "GREEN_WOOL";
				} else {
					return "WOOL";
				}
			case "LIME_WOOL":
				if (BukkitMain.newBlocks()) {
					return "LIME_WOOL";
				} else {
					return "WOOL";
				}
			case "MAGENTA_WOOL":
				if (BukkitMain.newBlocks()) {
					return "MAGENTA_WOOL";
				} else {
					return "WOOL";
				}
		}
		return null;
	}
	
	public int getLegacyId(String color) {
		switch (color) {
		case "WHITE":
			return 0;
		case "RED":
			return 14;
		case "GREEN":
			return 13;
		case "LIME":
			return 5;
		case "MAGENTA":
			return 2;
		}
		return 0;
	}
}
