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
			case "PINK_WOOL":
				if (BukkitMain.newBlocks()) {
					return "PINK_WOOL";
				} else {
					return "WOOL";
				}
			case "PURPLE_WOOL":
				if (BukkitMain.newBlocks()) {
					return "PURPLE_WOOL";
				} else {
					return "WOOL";
				}
			case "BLUE_WOOL":
				if (BukkitMain.newBlocks()) {
					return "BLUE_WOOL";
				} else {
					return "WOOL";
				}
			case "LIGHT_BLUE_WOOL":
				if (BukkitMain.newBlocks()) {
					return "LIGHT_BLUE_WOOL";
				} else {
					return "WOOL";
				}
			case "PINK_GLASS_PANE":
				if (BukkitMain.newBlocks()) {
					return "PINK_STAINED_GLASS_PANE";
				} else {
					return "STAINED_GLASS_PANE";
				}
			case "MAGENTA_GLASS_PANE":
				if (BukkitMain.newBlocks()) {
					return "MAGENTA_STAINED_GLASS_PANE";
				} else {
					return "STAINED_GLASS_PANE";
				}
			case "PINK_GLASS":
				if (BukkitMain.newBlocks()) {
					return "PINK_STAINED_GLASS";
				} else {
					return "STAINED_GLASS";
				}
			case "PINK_DYE":
				if (BukkitMain.newBlocks()) {
					return "PINK_DYE";
				} else {
					return "INK_SACK";
				}
			case "GLASS":
				return "GLASS";
			default:
				return null;
		}
	}
	
	public int getLegacyId(String color) {
		switch (color) {
			case "ORANGE":
				return 1;
			case "MAGENTA":
				return 2;
			case "LIGHT_BLUE":
				return 3;
			case "YELLOW":
				return 4;
			case "LIME":
				return 5;
			case "PINK":
				return 6;
			case "GRAY":
				return 7;
			case "LIGHT_GRAY":
				return 8;
			case "CYAN":
				return 9;
			case "PURPLE":
				return 10;
			case "BLUE":
				return 11;
			case "BROWN":
				return 12;
			case "GREEN":
				return 13;
			case "RED":
				return 14;
			case "BLACK":
				return 15;
			default:
				return 0;
		}
	}
}
