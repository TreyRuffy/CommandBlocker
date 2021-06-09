package me.treyruffy.commandblocker.bukkit.gui;

import me.treyruffy.commandblocker.bukkit.BukkitMain;

public class SharedGui {

	public static int pinkGlassBlockId;
	public static int pinkId;
	public static int worldsItemId;
	public static int blueId;
	public static int lightBlueId;
	public static int magentaId;

	public static int redWoolId;
	public static int greenWoolId;

	public static String pinkGlassBlock;
	public static String pinkWool;
	public static String magentaWool;
	public static String worldsItem;
	public static String blueWool;
	public static String lightBlueWool;
	public static String redWool;
	public static String greenWool;

	public static void setupWool() {
		String setPinkGlassBlock;
		int setPinkGlassBlockId = 0;
		if (BukkitMain.coloredGlassPane()) {
			setPinkGlassBlock = new LegacyBlocks().getBlock("PINK_GLASS");
			if (setPinkGlassBlock.equalsIgnoreCase("stained_glass")) {
				setPinkGlassBlockId = new LegacyBlocks().getLegacyId("PINK");
			}
		} else {
			setPinkGlassBlock = "GLASS";
			setPinkGlassBlockId = 0;
		}

		String setPinkWool;
		String setMagentaWool;
		String setWorldsItem;
		String setBlueWool;
		String setLightBlueWool;
		if (BukkitMain.customSkulls()) {
			if (BukkitMain.newBlocks()) {
				setWorldsItem = "PLAYER_HEAD";
			} else {
				setWorldsItem = "SKULL_ITEM";
			}
		} else {
			setWorldsItem = new LegacyBlocks().getBlock("PURPLE_WOOL");
		}
		if (BukkitMain.customSkulls()) {
			if (BukkitMain.newBlocks()) {
				setPinkWool = "PLAYER_HEAD";
			} else {
				setPinkWool = "SKULL_ITEM";
			}
		} else {
			setPinkWool = new LegacyBlocks().getBlock("PINK_WOOL");
		}
		if (BukkitMain.customSkulls()) {
			if (BukkitMain.newBlocks()) {
				setMagentaWool = "PLAYER_HEAD";
			} else {
				setMagentaWool = "SKULL_ITEM";
			}
		} else {
			setMagentaWool = new LegacyBlocks().getBlock("MAGENTA_WOOL");
		}
		if (BukkitMain.customSkulls()) {
			if (BukkitMain.newBlocks()) {
				setBlueWool = "PLAYER_HEAD";
			} else {
				setBlueWool = "SKULL_ITEM";
			}
		} else {
			setBlueWool = new LegacyBlocks().getBlock("BLUE_WOOL");
		}
		if (BukkitMain.customSkulls()) {
			if (BukkitMain.newBlocks()) {
				setLightBlueWool = "PLAYER_HEAD";
			} else {
				setLightBlueWool = "SKULL_ITEM";
			}
		} else {
			setLightBlueWool = new LegacyBlocks().getBlock("LIGHT_BLUE_WOOL");
		}

		int setPinkId = 0;
		if (setPinkWool.equalsIgnoreCase("wool")) {
			setPinkId = new LegacyBlocks().getLegacyId("PINK");
		} else if (setPinkWool.equalsIgnoreCase("skull_item")) {
			setPinkId = 3;
		}
		int setWorldsItemId = 0;
		if (setWorldsItem.equalsIgnoreCase("wool")) {
			setWorldsItemId = new LegacyBlocks().getLegacyId("PURPLE");
		} else if (setWorldsItem.equalsIgnoreCase("skull_item")) {
			setWorldsItemId = 3;
		}
		int setBlueId = 0;
		if (setBlueWool.equalsIgnoreCase("wool")) {
			setBlueId = new LegacyBlocks().getLegacyId("BLUE");
		} else if (setBlueWool.equalsIgnoreCase("skull_item")) {
			setBlueId = 3;
		}
		int setLightBlueId = 0;
		if (setLightBlueWool.equalsIgnoreCase("wool")) {
			setLightBlueId = new LegacyBlocks().getLegacyId("LIGHT_BLUE");
		} else if (setLightBlueWool.equalsIgnoreCase("skull_item")) {
			setLightBlueId = 3;
		}
		int setMagentaId = 0;
		if (setMagentaWool.equalsIgnoreCase("wool")) {
			setMagentaId = new LegacyBlocks().getLegacyId("MAGENTA");
		} else if (setMagentaWool.equalsIgnoreCase("skull_item")) {
			setMagentaId = 3;
		}
		String setRedWool = new LegacyBlocks().getBlock("RED_WOOL");
		int setRedWoolId = 0;
		if (setRedWool.equalsIgnoreCase("wool")) {
			setRedWoolId = new LegacyBlocks().getLegacyId("RED");
		}
		String setGreenWool = new LegacyBlocks().getBlock("LIME_WOOL");
		int setGreenWoolId = 0;
		if (setGreenWool.equalsIgnoreCase("wool")) {
			setGreenWoolId = new LegacyBlocks().getLegacyId("LIME");
		}

		pinkId = setPinkId;
		worldsItemId = setWorldsItemId;
		blueId = setBlueId;
		lightBlueId = setLightBlueId;
		magentaId = setMagentaId;
		redWoolId = setRedWoolId;
		greenWoolId = setGreenWoolId;
		pinkGlassBlockId = setPinkGlassBlockId;

		pinkWool = setPinkWool;
		lightBlueWool = setLightBlueWool;
		magentaWool = setMagentaWool;
		worldsItem = setWorldsItem;
		blueWool = setBlueWool;
		redWool = setRedWool;
		greenWool = setGreenWool;
		pinkGlassBlock = setPinkGlassBlock;
	}

}
