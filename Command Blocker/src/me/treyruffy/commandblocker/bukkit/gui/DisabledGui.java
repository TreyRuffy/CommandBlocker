package me.treyruffy.commandblocker.bukkit.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.treyruffy.commandblocker.bukkit.PlaceholderAPITest;
import me.treyruffy.commandblocker.bukkit.config.Messages;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.treyruffy.commandblocker.Log;
import me.treyruffy.commandblocker.Universal;
import me.treyruffy.commandblocker.bukkit.BukkitMain;
import me.treyruffy.commandblocker.bukkit.config.ConfigManager;
import me.treyruffy.commandblockerlegacy.OldConfigManager;

public class DisabledGui implements Listener {


	private final HashMap<String, String> lookingFor = new HashMap<>();
	private final HashMap<String, String> command = new HashMap<>();

	@SuppressWarnings("deprecation")
	public void openGui(Player p, String args) {
		if (!p.hasPermission("cb.edit")) {
			return;
		}
		Inventory inv = Bukkit.createInventory(null, 36, PlaceholderAPITest.testforPAPI(p,
				ChatColor.translateAlternateColorCodes('&',
						Messages.getMessage("EditGui", "EditCommandInventoryName"))));
		String pinkGlassBlock = new LegacyBlocks().getBlock("GLASS");
		int pinkGlassBlockId = 0;
		if (BukkitMain.coloredGlassPane()) {
			pinkGlassBlock = new LegacyBlocks().getBlock("PINK_GLASS");
			if (pinkGlassBlock.equalsIgnoreCase("stained_glass")) {
				pinkGlassBlockId = new LegacyBlocks().getLegacyId("PINK");
			}
		}

		ItemStack commandSlot = new ItemStack(Material.valueOf(pinkGlassBlock), 1, (byte) pinkGlassBlockId);
		String pinkWool = new LegacyBlocks().getBlock("PINK_WOOL");
		String magentaWool = new LegacyBlocks().getBlock("MAGENTA_WOOL");
		String purpleWool = new LegacyBlocks().getBlock("PURPLE_WOOL");
		String blueWool = new LegacyBlocks().getBlock("BLUE_WOOL");
		String lightBlueWool = new LegacyBlocks().getBlock("LIGHT_BLUE_WOOL");

		int pinkId = 0;
		if (pinkWool.equalsIgnoreCase("wool")) {
			pinkId = new LegacyBlocks().getLegacyId("PINK");
		}
		int purpleId = 0;
		if (purpleWool.equalsIgnoreCase("wool")) {
			purpleId = new LegacyBlocks().getLegacyId("PURPLE");
		}
		int blueId = 0;
		if (blueWool.equalsIgnoreCase("wool")) {
			blueId = new LegacyBlocks().getLegacyId("BLUE");
		}
		int lightBlueId = 0;
		if (lightBlueWool.equalsIgnoreCase("wool")) {
			lightBlueId = new LegacyBlocks().getLegacyId("LIGHT_BLUE");
		}
		int magentaId = 0;
		if (magentaWool.equalsIgnoreCase("wool")) {
			magentaId = new LegacyBlocks().getLegacyId("MAGENTA");
		}

		if (BukkitMain.coloredGlassPane()) {
			String magentaGlass = new LegacyBlocks().getBlock("MAGENTA_GLASS_PANE");

			String pinkGlass = new LegacyBlocks().getBlock("PINK_GLASS_PANE");

			ItemStack magentaGlassSlots = new ItemStack(Material.valueOf(magentaGlass), 1, (byte) magentaId);
			ItemStack pinkGlassSlots = new ItemStack(Material.valueOf(pinkGlass), 1, (byte) pinkId);

			ItemMeta magentaGlassMeta = magentaGlassSlots.getItemMeta();
			assert magentaGlassMeta != null;
			magentaGlassMeta.setDisplayName(" ");
			magentaGlassSlots.setItemMeta(magentaGlassMeta);

			ItemMeta pinkGlassMeta = pinkGlassSlots.getItemMeta();
			assert pinkGlassMeta != null;
			pinkGlassMeta.setDisplayName(" ");
			pinkGlassSlots.setItemMeta(pinkGlassMeta);

			for (int i = 0; i <= 8; i += 2) {
				inv.setItem(i, pinkGlassSlots);
				inv.setItem(i + 26, pinkGlassSlots);
			}
			for (int i = 1; i <= 9; i += 2) {
				inv.setItem(i, magentaGlassSlots);
				inv.setItem(i + 26, magentaGlassSlots);
			}
			inv.setItem(17, magentaGlassSlots);
			inv.setItem(18, pinkGlassSlots);
		}

		ItemStack firstSlot = new ItemStack(Material.valueOf(pinkWool), 1, (byte) pinkId);
		ItemStack secondSlot = new ItemStack(Material.valueOf(magentaWool), 1, (byte) magentaId);
		ItemStack thirdSlot = new ItemStack(Material.valueOf(purpleWool), 1, (byte) purpleId);
		ItemStack fourthSlot = new ItemStack(Material.valueOf(blueWool), 1, (byte) blueId);
		ItemStack fifthSlot = new ItemStack(Material.valueOf(lightBlueWool), 1, (byte) lightBlueId);
		ItemStack sixthSlot;

		String redWool = new LegacyBlocks().getBlock("RED_WOOL");
		int redWoolId = 0;
		if (redWool.equalsIgnoreCase("wool")) {
			redWoolId = new LegacyBlocks().getLegacyId("RED");
		}
		String greenWool = new LegacyBlocks().getBlock("LIME_WOOL");
		int greenWoolId = 0;
		if (greenWool.equalsIgnoreCase("wool")) {
			greenWoolId = new LegacyBlocks().getLegacyId("LIME");
		}
		if (!BukkitMain.oldConfig()) {
			if (ConfigManager.MainDisabled.getString("DisabledCommands." + args + ".NoTabComplete") == null) {
				ConfigManager.MainDisabled.set("DisabledCommands." + args + ".NoTabComplete", true);
				ConfigManager.saveDisabled();
			}
			if (!ConfigManager.MainDisabled.getBoolean("DisabledCommands." + args + ".NoTabComplete")) {
				sixthSlot = new ItemStack(Material.valueOf(redWool), 1, (byte) redWoolId);
			} else {
				sixthSlot = new ItemStack(Material.valueOf(greenWool), 1, (byte) greenWoolId);
			}
		} else {
			if (OldConfigManager.MainDisabled.getString("DisabledCommands." + args + ".NoTabComplete") == null) {
				OldConfigManager.MainDisabled.set("DisabledCommands." + args + ".NoTabComplete", true);
				OldConfigManager.saveDisabled();
			}
			if (!OldConfigManager.MainDisabled.getBoolean("DisabledCommands." + args + ".NoTabComplete")) {
				sixthSlot = new ItemStack(Material.valueOf(redWool), 1, (byte) redWoolId);
			} else {
				sixthSlot = new ItemStack(Material.valueOf(greenWool), 1, (byte) greenWoolId);
			}
		}

		ItemMeta commandSlotMeta = commandSlot.getItemMeta();
		assert commandSlotMeta != null;
		commandSlotMeta.setDisplayName(PlaceholderAPITest.testforPAPI(p, ChatColor.translateAlternateColorCodes('&',
				Messages.getMessage("EditGui", "InventoryCommandName").replace("%c", args))));
		commandSlot.setItemMeta(commandSlotMeta);
		inv.setItem(4, commandSlot);


		ItemMeta firstSlotMeta = firstSlot.getItemMeta();
		assert firstSlotMeta != null;
		firstSlotMeta.setDisplayName(PlaceholderAPITest.testforPAPI(p, ChatColor.translateAlternateColorCodes('&',
				Messages.getMessage("EditGui", "PermissionItemName"))));

		String currentPermission = ChatColor.WHITE + (BukkitMain.oldConfig() ?
				OldConfigManager.MainDisabled.getString("DisabledCommands." + args + ".Permission") :
				ConfigManager.MainDisabled.getString("DisabledCommands." + args + ".Permission"));

		List<String> firstSlotLore = new ArrayList<>();
		for (String message : Messages.getMessages("EditGui", "PermissionItemLore")) {
			firstSlotLore.add(PlaceholderAPITest.testforPAPI(p, ChatColor.translateAlternateColorCodes('&', message)).replace("%p", currentPermission));
		}
		firstSlotMeta.setLore(firstSlotLore);
		firstSlot.setItemMeta(firstSlotMeta);
		inv.setItem(11, firstSlot);


		ItemMeta secondSlotMeta = secondSlot.getItemMeta();
		assert secondSlotMeta != null;
		secondSlotMeta.setDisplayName(PlaceholderAPITest.testforPAPI(p, ChatColor.translateAlternateColorCodes('&',
				Messages.getMessage("EditGui", "MessageItemName"))));

		String currentMessage = ChatColor.WHITE + ChatColor.translateAlternateColorCodes('&', BukkitMain.oldConfig() ?
				OldConfigManager.MainDisabled.getString("DisabledCommands." + args + ".Message") :
				ConfigManager.MainDisabled.getString("DisabledCommands." + args + ".Message"));
		List<String> secondSlotLore = new ArrayList<>();
		for (String message : Messages.getMessages("EditGui", "MessageItemLore")) {
			secondSlotLore.add(PlaceholderAPITest.testforPAPI(p, ChatColor.translateAlternateColorCodes('&', message)).replace("%m", currentMessage));
		}
		secondSlotMeta.setLore(secondSlotLore);
		secondSlot.setItemMeta(secondSlotMeta);
		inv.setItem(12, secondSlot);


		ItemMeta thirdSlotMeta = thirdSlot.getItemMeta();
		assert thirdSlotMeta != null;
		thirdSlotMeta.setDisplayName(PlaceholderAPITest.testforPAPI(p, ChatColor.translateAlternateColorCodes('&',
				Messages.getMessage("EditGui", "WorldsItemName"))));
		thirdSlot.setItemMeta(thirdSlotMeta);
		inv.setItem(13, thirdSlot);


		ItemMeta fourthSlotMeta = fourthSlot.getItemMeta();
		assert fourthSlotMeta != null;
		fourthSlotMeta.setDisplayName(PlaceholderAPITest.testforPAPI(p, ChatColor.translateAlternateColorCodes('&',
				Messages.getMessage("EditGui", "PlayerCommandsItemName"))));
		fourthSlot.setItemMeta(fourthSlotMeta);
		inv.setItem(14, fourthSlot);


		ItemMeta fifthSlotMeta = fifthSlot.getItemMeta();
		assert fifthSlotMeta != null;
		fifthSlotMeta.setDisplayName(PlaceholderAPITest.testforPAPI(p, ChatColor.translateAlternateColorCodes('&',
				Messages.getMessage("EditGui", "ConsoleCommandsItemName"))));
		fifthSlot.setItemMeta(fifthSlotMeta);
		inv.setItem(15, fifthSlot);


		ItemMeta sixthSlotMeta = sixthSlot.getItemMeta();
		assert sixthSlotMeta != null;
		sixthSlotMeta.setDisplayName(PlaceholderAPITest.testforPAPI(p, ChatColor.translateAlternateColorCodes('&',
				Messages.getMessage("EditGui", "NoTabCompletion"))));
		sixthSlot.setItemMeta(sixthSlotMeta);
		inv.setItem(22, sixthSlot);


		Bukkit.getScheduler().runTaskLater(BukkitMain.get(), () -> p.openInventory(inv), 1);
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		if (e.getInventory().getHolder() != null) {
			return;
		}
		if (e.getCurrentItem() == null) {
			return;
		}
		if (e.getCurrentItem().getItemMeta() == null) {
			return;
		}
		if (!e.getWhoClicked().hasPermission("cb.edit")) {
			return;
		}
		if (e.getView().getTitle().equals(PlaceholderAPITest.testforPAPI((Player) e.getWhoClicked(),
				ChatColor.translateAlternateColorCodes('&',
						Messages.getMessage("EditGui", "EditCommandInventoryName"))))) {
			if (e.getRawSlot() < 0 || e.getRawSlot() >= 54 || (!e.getCurrentItem().getItemMeta().hasDisplayName())) {
				e.setCancelled(true);
				return;
			}
			String cmd = ChatColor.stripColor(e.getInventory().getItem(4).getItemMeta().getDisplayName()).substring(1);
			Player p = (Player) e.getWhoClicked();
			if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(PlaceholderAPITest.testforPAPI(p,
					ChatColor.translateAlternateColorCodes('&', Messages.getMessage("EditGui", "PermissionItemName"))))) {
				p.closeInventory();

				for (String message : Messages.getMessages("EditGui", "ChangePermission")) {
					p.sendMessage(PlaceholderAPITest.testforPAPI(p, ChatColor.translateAlternateColorCodes('&',
							message.replace("%c", cmd))));
				}
				lookingFor.put(p.getUniqueId().toString(), "permission");
				command.put(p.getUniqueId().toString(), cmd);
			} else if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(PlaceholderAPITest.testforPAPI(p, ChatColor.translateAlternateColorCodes('&', Messages.getMessage("EditGui", "MessageItemName"))))) {
				p.closeInventory();

				for (String message : Messages.getMessages("EditGui", "ChangeMessage")) {
					p.sendMessage(PlaceholderAPITest.testforPAPI(p, ChatColor.translateAlternateColorCodes('&',
							message.replace("%c", cmd))));
				}
				lookingFor.put(p.getUniqueId().toString(), "message");
				command.put(p.getUniqueId().toString(), cmd);
			} else if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(PlaceholderAPITest.testforPAPI(p, ChatColor.translateAlternateColorCodes('&', Messages.getMessage("EditGui", "WorldsItemName"))))) {
				openWorldGui(p, cmd);

			} else if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(PlaceholderAPITest.testforPAPI(p, ChatColor.translateAlternateColorCodes('&', Messages.getMessage("EditGui", "PlayerCommandsItemName"))))) {
				openPCommandsGui(p, cmd);

			} else if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(PlaceholderAPITest.testforPAPI(p, ChatColor.translateAlternateColorCodes('&', Messages.getMessage("EditGui", "ConsoleCommandsItemName"))))) {
				openCCommandsGui(p, cmd);

			} else if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(PlaceholderAPITest.testforPAPI(p, ChatColor.translateAlternateColorCodes('&', Messages.getMessage("EditGui", "NoTabCompletion"))))) {
				if (!BukkitMain.oldConfig()) {
					if (!ConfigManager.MainDisabled.getBoolean("DisabledCommands." + cmd + ".NoTabComplete")) {
						ConfigManager.MainDisabled.set("DisabledCommands." + cmd + ".NoTabComplete", true);
						ConfigManager.saveDisabled();
						Log.addLog(Universal.get().getMethods(),
								p.getName() + ": Changed tab completion for /" + cmd + " from off to on in disabled" + ".yml");
					} else {
						ConfigManager.MainDisabled.set("DisabledCommands." + cmd + ".NoTabComplete", false);
						ConfigManager.saveDisabled();
						Log.addLog(Universal.get().getMethods(),
								p.getName() + ": Changed tab completion for /" + cmd + " from on to off in disabled" + ".yml");
					}
				} else {
					if (!OldConfigManager.MainDisabled.getBoolean("DisabledCommands." + cmd + ".NoTabComplete")) {
						OldConfigManager.MainDisabled.set("DisabledCommands." + cmd + ".NoTabComplete", true);
						OldConfigManager.saveDisabled();
						Log.addLog(Universal.get().getMethods(), p.getName() + ": Changed tab completion for /" + cmd + " from off to on in disabled.yml");
					} else {
						OldConfigManager.MainDisabled.set("DisabledCommands." + cmd + ".NoTabComplete", false);
						OldConfigManager.saveDisabled();
						Log.addLog(Universal.get().getMethods(),
								p.getName() + ": Changed tab completion for /" + cmd + " from on to off in disabled" + ".yml");
					}
				}

				openGui(p, cmd);

			}
			e.setCancelled(true);
		} else if (e.getView().getTitle().equals(PlaceholderAPITest.testforPAPI((Player) e.getWhoClicked(),
				ChatColor.translateAlternateColorCodes('&', Messages.getMessage("EditGui", "EditWorldsInventoryName"))))) {
			if (e.getRawSlot() < 0 || e.getRawSlot() >= 54 || (!e.getCurrentItem().getItemMeta().hasDisplayName())) {
				e.setCancelled(true);
				return;
			}
			String cmd = ChatColor.stripColor(e.getInventory().getItem(4).getItemMeta().getDisplayName()).substring(1);
			Player p = (Player) e.getWhoClicked();
			if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(PlaceholderAPITest.testforPAPI(p,
					ChatColor.translateAlternateColorCodes('&', Messages.getMessage("EditGui", "GoBackItemName"))))) {
				openGui(p, cmd);
			} else if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(PlaceholderAPITest.testforPAPI(p, ChatColor.translateAlternateColorCodes('&', Messages.getMessage("EditGui", "AddWorldItem"))))) {
				command.put(p.getUniqueId().toString(), cmd);
				lookingFor.put(p.getUniqueId().toString(), "world");
				for (String message : Messages.getMessages("EditGui", "AddWorldMessage")) {
					p.sendMessage(PlaceholderAPITest.testforPAPI(p, ChatColor.translateAlternateColorCodes('&',
							message.replace("%c", cmd))));
				}
				p.closeInventory();
			} else if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(" ")) {
				e.setCancelled(true);
			} else if (!(e.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase(PlaceholderAPITest.testforPAPI(p, ChatColor.translateAlternateColorCodes('&', Messages.getMessage("EditGui", "InventoryCommandName").replace("%c", cmd))))) {

				FileConfiguration disabled = BukkitMain.oldConfig() ? OldConfigManager.MainDisabled :
						ConfigManager.MainDisabled;
				if (disabled.getStringList("DisabledCommands." + cmd + ".Worlds").indexOf("all") != -1) {
					List<String> worlds = new ArrayList<>();
					for (World w : Bukkit.getWorlds()) {
						worlds.add(w.getName());
					}
					disabled.set("DisabledCommands." + cmd + ".Worlds", worlds);
					if (!BukkitMain.oldConfig()) {
						ConfigManager.saveDisabled();
					} else {
						OldConfigManager.saveDisabled();
					}
				}
				List<String> newWorldList = disabled.getStringList("DisabledCommands." + cmd + ".Worlds");
				if (disabled.getStringList("DisabledCommands." + cmd + ".Worlds").indexOf(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName())) != -1) {
					newWorldList.remove(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()));
					Log.addLog(Universal.get().getMethods(), p.getName() + ": Removed world " + e.getCurrentItem().getItemMeta().getDisplayName() + " from /" + cmd + " in disabled.yml");
				} else {
					newWorldList.add(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()));
					Log.addLog(Universal.get().getMethods(), p.getName() + ": Added world " + e.getCurrentItem().getItemMeta().getDisplayName() + " to /" + cmd + " in disabled.yml");
				}

				disabled.set("DisabledCommands." + cmd + ".Worlds", newWorldList);
				if (!BukkitMain.oldConfig()) {
					ConfigManager.saveDisabled();
				} else {
					OldConfigManager.saveDisabled();
				}
				openWorldGui(p, cmd);
			}
			e.setCancelled(true);
		} else if (e.getView().getTitle().equals(PlaceholderAPITest.testforPAPI((Player) e.getWhoClicked(),
				ChatColor.translateAlternateColorCodes('&', Messages.getMessage("EditGui",
						"EditPlayerCommandsInventoryName"))))) {
			if (e.getRawSlot() < 0 || e.getRawSlot() >= 54 || (!e.getCurrentItem().getItemMeta().hasDisplayName())) {
				e.setCancelled(true);
				return;
			}
			String cmd = ChatColor.stripColor(e.getInventory().getItem(4).getItemMeta().getDisplayName()).substring(1);
			Player p = (Player) e.getWhoClicked();
			if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(PlaceholderAPITest.testforPAPI(p,
					ChatColor.translateAlternateColorCodes('&', Messages.getMessage("EditGui", "GoBackItemName"))))) {
				openGui(p, cmd);
			} else if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(PlaceholderAPITest.testforPAPI(p, ChatColor.translateAlternateColorCodes('&', Messages.getMessage("EditGui", "AddPlayerCommandItem"))))) {
				command.put(p.getUniqueId().toString(), cmd);
				lookingFor.put(p.getUniqueId().toString(), "playercommand");
				for (String message : Messages.getMessages("EditGui", "AddPlayerCommandMessage")) {
					p.sendMessage(PlaceholderAPITest.testforPAPI(p, ChatColor.translateAlternateColorCodes('&',
							message.replace("%c", cmd))));
				}
				p.closeInventory();
			} else if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(" ")) {
				e.setCancelled(true);
			} else if (!(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(PlaceholderAPITest.testforPAPI(p, ChatColor.translateAlternateColorCodes('&', Messages.getMessage("EditGui", "InventoryCommandName").replace("%c", cmd)))))) {

				FileConfiguration disabled = BukkitMain.oldConfig() ? OldConfigManager.MainDisabled :
						ConfigManager.MainDisabled;
				List<String> newPCommandList = disabled.getStringList("DisabledCommands." + cmd + ".PlayerCommands");
				if (!ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase("none")) {
					newPCommandList.remove(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).substring(1));
					Log.addLog(Universal.get().getMethods(),
							p.getName() + ": Removed player command " + e.getCurrentItem().getItemMeta().getDisplayName() + " from /" + cmd + " in disabled.yml");
				} else {
					newPCommandList.remove(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()));
					Log.addLog(Universal.get().getMethods(),
							p.getName() + ": Removed none tag from player commands " + "for /" + cmd + " in disabled" +
									".yml");
				}

				disabled.set("DisabledCommands." + cmd + ".PlayerCommands", newPCommandList);
				if (!BukkitMain.oldConfig()) {
					ConfigManager.saveDisabled();
				} else {
					OldConfigManager.saveDisabled();
				}
				openPCommandsGui(p, cmd);
			}
			e.setCancelled(true);
		} else if (e.getView().getTitle().equals(PlaceholderAPITest.testforPAPI((Player) e.getWhoClicked(),
				ChatColor.translateAlternateColorCodes('&', Messages.getMessage("EditGui",
						"EditConsoleCommandsInventoryName"))))) {
			if (e.getRawSlot() < 0 || e.getRawSlot() >= 54 || (!e.getCurrentItem().getItemMeta().hasDisplayName())) {
				e.setCancelled(true);
				return;
			}
			String cmd = ChatColor.stripColor(e.getInventory().getItem(4).getItemMeta().getDisplayName()).substring(1);
			Player p = (Player) e.getWhoClicked();
			if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(PlaceholderAPITest.testforPAPI(p,
					ChatColor.translateAlternateColorCodes('&', Messages.getMessage("EditGui", "GoBackItemName"))))) {
				openGui(p, cmd);
			} else if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(PlaceholderAPITest.testforPAPI(p, ChatColor.translateAlternateColorCodes('&', Messages.getMessage("EditGui", "AddConsoleCommandItem"))))) {
				command.put(p.getUniqueId().toString(), cmd);
				lookingFor.put(p.getUniqueId().toString(), "consolecommand");
				for (String message : Messages.getMessages("EditGui", "AddConsoleCommandMessage")) {
					p.sendMessage(PlaceholderAPITest.testforPAPI(p, ChatColor.translateAlternateColorCodes('&',
							message.replace("%c", cmd))));
				}
				p.closeInventory();
			} else if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(" ")) {
				e.setCancelled(true);
			} else if (!(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(PlaceholderAPITest.testforPAPI(p, ChatColor.translateAlternateColorCodes('&', Messages.getMessage("EditGui", "InventoryCommandName").replace("%c", cmd)))))) {

				FileConfiguration disabled = BukkitMain.oldConfig() ? OldConfigManager.MainDisabled :
						ConfigManager.MainDisabled;
				List<String> newPCommandList = disabled.getStringList("DisabledCommands." + cmd + ".ConsoleCommands");
				if (!ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase("none")) {
					newPCommandList.remove(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).substring(1));
					Log.addLog(Universal.get().getMethods(),
							p.getName() + ": Removed console command " + e.getCurrentItem().getItemMeta().getDisplayName() + " from /" + cmd + " in disabled.yml");
				} else {
					newPCommandList.remove(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()));
					Log.addLog(Universal.get().getMethods(),
							p.getName() + ": Removed none tag from console commands " + "for /" + cmd + " in disabled" +
									".yml");
				}
				
				disabled.set("DisabledCommands." + cmd + ".ConsoleCommands", newPCommandList);
				if (!BukkitMain.oldConfig()) {
					ConfigManager.saveDisabled();
				} else {
					OldConfigManager.saveDisabled();
				}
				openCCommandsGui(p, cmd);
			}
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		Player p = e.getPlayer();
		if (!p.hasPermission("cb.edit")) {
			return;
		}
		if (!lookingFor.containsKey(p.getUniqueId().toString())) {
			return;
		}
		
		if (e.getMessage().replace(" ", "").equalsIgnoreCase("cancel")) {
			lookingFor.remove(p.getUniqueId().toString());
			command.remove(p.getUniqueId().toString());
			for (String message : Messages.getMessages("Main", "Cancelled")) {
				p.sendMessage(PlaceholderAPITest.testforPAPI(p, ChatColor.translateAlternateColorCodes('&', message)));
			}
			e.setCancelled(true);
			return;
		}
		
		if (lookingFor.get(p.getUniqueId().toString()).equalsIgnoreCase("permission")) {
			FileConfiguration disabled = BukkitMain.oldConfig() ? OldConfigManager.MainDisabled : ConfigManager.MainDisabled;
			
			Log.addLog(Universal.get().getMethods(), p.getName() + ": Changed permission from " +
					disabled.getString("DisabledCommands." + command.get(p.getUniqueId().toString()) + ".Permission") + " to "+ e.getMessage()
					+ "for /" + command.get(p.getUniqueId().toString()) + " in disabled.yml");
			
			disabled.set("DisabledCommands." + command.get(p.getUniqueId().toString()) + ".Permission", e.getMessage());
			
			if (!BukkitMain.oldConfig()) {
				ConfigManager.saveDisabled();
			} else {
				OldConfigManager.saveDisabled();
			}
			lookingFor.remove(p.getUniqueId().toString());
			
			openGui(p, command.get(p.getUniqueId().toString()));
			command.remove(p.getUniqueId().toString());
		} else if (lookingFor.get(p.getUniqueId().toString()).equalsIgnoreCase("message")) {
			FileConfiguration disabled = BukkitMain.oldConfig() ? OldConfigManager.MainDisabled : ConfigManager.MainDisabled;
			
			Log.addLog(Universal.get().getMethods(), p.getName() + ": Changed message from " +
					disabled.getString("DisabledCommands." + command.get(p.getUniqueId().toString()) + ".Message") + " to "+ e.getMessage()
					+ "for /" + command.get(p.getUniqueId().toString()) + " in disabled.yml");
			
			disabled.set("DisabledCommands." + command.get(p.getUniqueId().toString()) + ".Message", e.getMessage());
			if (!BukkitMain.oldConfig()) {
				ConfigManager.saveDisabled();
			} else {
				OldConfigManager.saveDisabled();
			}
			lookingFor.remove(p.getUniqueId().toString());
			
			openGui(p, command.get(p.getUniqueId().toString()));
			command.remove(p.getUniqueId().toString());
		} else if (lookingFor.get(p.getUniqueId().toString()).equalsIgnoreCase("world")) {
			FileConfiguration disabled = BukkitMain.oldConfig() ? OldConfigManager.MainDisabled : ConfigManager.MainDisabled;
			
			Log.addLog(Universal.get().getMethods(), p.getName() + ": Added world " + e.getMessage() + " for /" + command.get(p.getUniqueId().toString()) + " in disabled.yml");
			
			List<String> worldList = disabled.getStringList("DisabledCommands." + command.get(p.getUniqueId().toString()) + ".Worlds");
			worldList.add(e.getMessage());
			disabled.set("DisabledCommands." + command.get(p.getUniqueId().toString()) + ".Worlds", worldList);
			if (!BukkitMain.oldConfig()) {
				ConfigManager.saveDisabled();
			} else {
				OldConfigManager.saveDisabled();
			}
			lookingFor.remove(p.getUniqueId().toString());
			
			openWorldGui(p, command.get(p.getUniqueId().toString()));
			command.remove(p.getUniqueId().toString());
		} else if (lookingFor.get(p.getUniqueId().toString()).equalsIgnoreCase("playercommand")) {
			FileConfiguration disabled = BukkitMain.oldConfig() ? OldConfigManager.MainDisabled : ConfigManager.MainDisabled;
			
			Log.addLog(Universal.get().getMethods(), p.getName() + ": Added player command /" + e.getMessage() + " for /" + command.get(p.getUniqueId().toString()) + " in disabled.yml");
			
			List<String> pCmdList = disabled.getStringList("DisabledCommands." + command.get(p.getUniqueId().toString()) + ".PlayerCommands");
			pCmdList.add(e.getMessage());
			disabled.set("DisabledCommands." + command.get(p.getUniqueId().toString()) + ".PlayerCommands", pCmdList);
			if (!BukkitMain.oldConfig()) {
				ConfigManager.saveDisabled();
			} else {
				OldConfigManager.saveDisabled();
			}
			lookingFor.remove(p.getUniqueId().toString());
			openPCommandsGui(p, command.get(p.getUniqueId().toString()));
			command.remove(p.getUniqueId().toString());
		} else if (lookingFor.get(p.getUniqueId().toString()).equalsIgnoreCase("consolecommand")) {
			FileConfiguration disabled = BukkitMain.oldConfig() ? OldConfigManager.MainDisabled : ConfigManager.MainDisabled;
			
			Log.addLog(Universal.get().getMethods(), p.getName() + ": Added console command /" + e.getMessage() + " for /" + command.get(p.getUniqueId().toString()) + " in disabled.yml");
			
			List<String> cCmdList = disabled.getStringList("DisabledCommands." + command.get(p.getUniqueId().toString()) + ".ConsoleCommands");
			cCmdList.add(e.getMessage());
			disabled.set("DisabledCommands." + command.get(p.getUniqueId().toString()) + ".ConsoleCommands", cCmdList);
			if (!BukkitMain.oldConfig()) {
				ConfigManager.saveDisabled();
			} else {
				OldConfigManager.saveDisabled();
			}
			lookingFor.remove(p.getUniqueId().toString());
			openCCommandsGui(p, command.get(p.getUniqueId().toString()));
			command.remove(p.getUniqueId().toString());
		}
		e.setCancelled(true);
	}
	
	@EventHandler
	public void onCmd(PlayerCommandPreprocessEvent e) {
		Player p = e.getPlayer();
		if (!p.hasPermission("cb.edit")) {
			return;
		}
		if (!lookingFor.containsKey(p.getUniqueId().toString())) {
			return;
		}
		
		if (lookingFor.get(p.getUniqueId().toString()).equalsIgnoreCase("permission")) {
			for (String message : Messages.getMessages("EditGui", "CannotUseCommand")) {
				p.sendMessage(PlaceholderAPITest.testforPAPI(p, ChatColor.translateAlternateColorCodes('&', message)));
			}
		} else if (lookingFor.get(p.getUniqueId().toString()).equalsIgnoreCase("message")) {
			for (String message : Messages.getMessages("EditGui", "CannotUseCommand")) {
				p.sendMessage(PlaceholderAPITest.testforPAPI(p, ChatColor.translateAlternateColorCodes('&', message)));
			}
		} else if (lookingFor.get(p.getUniqueId().toString()).equalsIgnoreCase("world")) {
			for (String message : Messages.getMessages("EditGui", "CannotUseCommand")) {
				p.sendMessage(PlaceholderAPITest.testforPAPI(p, ChatColor.translateAlternateColorCodes('&', message)));
			}
		} else if (lookingFor.get(p.getUniqueId().toString()).equalsIgnoreCase("playercommand")) {
			FileConfiguration disabled = BukkitMain.oldConfig() ? OldConfigManager.MainDisabled : ConfigManager.MainDisabled;
			
			Log.addLog(Universal.get().getMethods(), p.getName() + ": Added player command " + e.getMessage() + " for /" + command.get(p.getUniqueId().toString()) + " in disabled.yml");
			
			List<String> pCmdList = disabled.getStringList("DisabledCommands." + command.get(p.getUniqueId().toString()) + ".PlayerCommands");
			pCmdList.add(e.getMessage().substring(1));
			disabled.set("DisabledCommands." + command.get(p.getUniqueId().toString()) + ".PlayerCommands", pCmdList);
			if (!BukkitMain.oldConfig()) {
				ConfigManager.saveDisabled();
			} else {
				OldConfigManager.saveDisabled();
			}
			lookingFor.remove(p.getUniqueId().toString());
			openPCommandsGui(p, command.get(p.getUniqueId().toString()));
			command.remove(p.getUniqueId().toString());
		} else if (lookingFor.get(p.getUniqueId().toString()).equalsIgnoreCase("consolecommand")) {
			FileConfiguration disabled = BukkitMain.oldConfig() ? OldConfigManager.MainDisabled : ConfigManager.MainDisabled;
			
			Log.addLog(Universal.get().getMethods(), p.getName() + ": Added console command " + e.getMessage() + " for /" + command.get(p.getUniqueId().toString()) + " in disabled.yml");
			
			List<String> cCmdList = disabled.getStringList("DisabledCommands." + command.get(p.getUniqueId().toString()) + ".ConsoleCommands");
			cCmdList.add(e.getMessage().substring(1));
			disabled.set("DisabledCommands." + command.get(p.getUniqueId().toString()) + ".ConsoleCommands", cCmdList);
			if (!BukkitMain.oldConfig()) {
				ConfigManager.saveDisabled();
			} else {
				OldConfigManager.saveDisabled();
			}
			lookingFor.remove(p.getUniqueId().toString());
			openCCommandsGui(p, command.get(p.getUniqueId().toString()));
			command.remove(p.getUniqueId().toString());
		}
		e.setCancelled(true);
	}

	@SuppressWarnings("deprecation")
	public void openWorldGui(Player p, String args) {
		if (!p.hasPermission("cb.edit")) {
			return;
		}
		FileConfiguration disabled = BukkitMain.oldConfig() ? OldConfigManager.MainDisabled :
				ConfigManager.MainDisabled;

		Inventory inv = Bukkit.createInventory(null, 54, PlaceholderAPITest.testforPAPI(p,
				ChatColor.translateAlternateColorCodes('&',
						Messages.getMessage("EditGui", "EditWorldsInventoryName"))));

		String pinkGlassBlock = new LegacyBlocks().getBlock("GLASS");
		int pinkGlassBlockId = 0;
		if (BukkitMain.coloredGlassPane()) {
			pinkGlassBlock = new LegacyBlocks().getBlock("PINK_GLASS");
			if (pinkGlassBlock.equalsIgnoreCase("stained_glass")) {
				pinkGlassBlockId = new LegacyBlocks().getLegacyId("PINK");
			}
		}

		ItemStack commandSlot = new ItemStack(Material.valueOf(pinkGlassBlock), 1, (byte) pinkGlassBlockId);

		if (BukkitMain.coloredGlassPane()) {
			String magentaGlass = new LegacyBlocks().getBlock("MAGENTA_GLASS_PANE");
			String pinkGlass = new LegacyBlocks().getBlock("PINK_GLASS_PANE");

			int magentaId = 0;
			int pinkId = 0;
			if (magentaGlass.equalsIgnoreCase("stained_glass_pane")) {
				magentaId = new LegacyBlocks().getLegacyId("MAGENTA");
			}
			if (pinkGlass.equalsIgnoreCase("stained_glass_pane")) {
				pinkId = new LegacyBlocks().getLegacyId("PINK");
			}

			ItemStack magentaGlassSlots = new ItemStack(Material.valueOf(magentaGlass), 1, (byte) magentaId);
			ItemStack pinkGlassSlots = new ItemStack(Material.valueOf(pinkGlass), 1, (byte) pinkId);

			ItemMeta magentaGlassMeta = magentaGlassSlots.getItemMeta();
			assert magentaGlassMeta != null;
			magentaGlassMeta.setDisplayName(" ");
			magentaGlassSlots.setItemMeta(magentaGlassMeta);

			ItemMeta pinkGlassMeta = pinkGlassSlots.getItemMeta();
			assert pinkGlassMeta != null;
			pinkGlassMeta.setDisplayName(" ");
			pinkGlassSlots.setItemMeta(pinkGlassMeta);

			for (int i = 0; i <= 8; i += 2) {
				inv.setItem(i, pinkGlassSlots);
				inv.setItem(i + 44, pinkGlassSlots);
			}
			for (int i = 1; i <= 9; i += 2) {
				inv.setItem(i, magentaGlassSlots);
				inv.setItem(i + 44, magentaGlassSlots);
			}
			inv.setItem(17, magentaGlassSlots);
			inv.setItem(18, pinkGlassSlots);
			inv.setItem(26, pinkGlassSlots);
			inv.setItem(27, magentaGlassSlots);
			inv.setItem(35, magentaGlassSlots);
			inv.setItem(36, pinkGlassSlots);
		}

		String pinkDye = new LegacyBlocks().getBlock("PINK_DYE");

		int pinkId = 0;
		if (pinkDye.equalsIgnoreCase("ink_sack")) {
			pinkId = 9;
		}
		ItemStack backSlot = new ItemStack(Material.valueOf(pinkDye), 1, (byte) pinkId);
		ItemMeta backSlotMeta = backSlot.getItemMeta();
		assert backSlotMeta != null;
		backSlotMeta.setDisplayName(PlaceholderAPITest.testforPAPI(p, ChatColor.translateAlternateColorCodes('&',
				Messages.getMessage("EditGui", "GoBackItemName"))));
		backSlot.setItemMeta(backSlotMeta);
		inv.setItem(49, backSlot);

		ItemMeta commandSlotMeta = commandSlot.getItemMeta();
		assert commandSlotMeta != null;
		commandSlotMeta.setDisplayName(PlaceholderAPITest.testforPAPI(p, ChatColor.translateAlternateColorCodes('&',
				Messages.getMessage("EditGui", "InventoryCommandName").replace("%c", args))));
		commandSlot.setItemMeta(commandSlotMeta);
		inv.setItem(4, commandSlot);

		if (disabled.getString("DisabledCommands." + args + ".Worlds") == null) {
			List<String> list = new ArrayList<>();
			list.add("all");
			disabled.set("DisabledCommands." + args + ".Worlds", list);
			if (!BukkitMain.oldConfig()) {
				ConfigManager.saveDisabled();
			} else {
				OldConfigManager.saveDisabled();
			}
		}

		int worldCount = 10;
		for (World w : Bukkit.getWorlds()) {
			if ((disabled.getStringList("DisabledCommands." + args + ".Worlds").indexOf(w.getName()) != -1) || (disabled.getStringList("DisabledCommands." + args + ".Worlds").indexOf("all") != -1)) {
				String limeWool = new LegacyBlocks().getBlock("LIME_WOOL");
				int limeWoolId = 0;
				if (limeWool.equalsIgnoreCase("wool")) {
					limeWoolId = new LegacyBlocks().getLegacyId("LIME");
				}

				ItemStack world = new ItemStack(Material.valueOf(limeWool), 1, (byte) limeWoolId);
				ItemMeta worldMeta = world.getItemMeta();
				assert worldMeta != null;
				worldMeta.setDisplayName(w.getName());
				world.setItemMeta(worldMeta);
				if (worldCount == 17) worldCount += 2;
				if (worldCount == 26) worldCount += 2;
				if (worldCount == 35) worldCount += 2;
				if (worldCount == 43) break;
				inv.setItem(worldCount, world);

			} else {
				String redWool = new LegacyBlocks().getBlock("RED_WOOL");
				int redWoolId = 0;
				if (redWool.equalsIgnoreCase("wool")) {
					redWoolId = new LegacyBlocks().getLegacyId("RED");
				}

				ItemStack world = new ItemStack(Material.valueOf(redWool), 1, (byte) redWoolId);
				ItemMeta worldMeta = world.getItemMeta();
				assert worldMeta != null;
				worldMeta.setDisplayName(w.getName());
				world.setItemMeta(worldMeta);
				if (worldCount == 17) worldCount += 2;
				if (worldCount == 26) worldCount += 2;
				if (worldCount == 35) worldCount += 2;
				if (worldCount == 43) break;
				inv.setItem(worldCount, world);
			}
			worldCount++;
			
		}
		for (String s : disabled.getStringList("DisabledCommands." + args + ".Worlds")) {
			if (Bukkit.getWorld(s) == null) {
				if (!s.equalsIgnoreCase("all")) {
					String limeWool = new LegacyBlocks().getBlock("LIME_WOOL");
					int limeWoolId = 0;
					if (limeWool.equalsIgnoreCase("wool")) {
						limeWoolId = new LegacyBlocks().getLegacyId("LIME");
					}

					ItemStack world = new ItemStack(Material.valueOf(limeWool), 1, (byte) limeWoolId);
					ItemMeta worldMeta = world.getItemMeta();
					assert worldMeta != null;
					worldMeta.setDisplayName(s);
					world.setItemMeta(worldMeta);
					if (worldCount == 17) worldCount += 2;
					if (worldCount == 26) worldCount += 2;
					if (worldCount == 35) worldCount += 2;
					if (worldCount == 43) break;
					inv.setItem(worldCount, world);
					worldCount++;
				} else {
					List<String> str = disabled.getStringList("DisabledCommands." + args + ".Worlds");
					str.remove("all");
					disabled.set("DisabledCommands." + args + ".Worlds", str);
					if (!BukkitMain.oldConfig()) {
						ConfigManager.saveDisabled();
					} else {
						OldConfigManager.saveDisabled();
					}
				}
			}
		}
		String magentaWool = new LegacyBlocks().getBlock("MAGENTA_WOOL");
		int magentaWoolId = 0;
		if (magentaWool.equalsIgnoreCase("wool")) {
			magentaWoolId = new LegacyBlocks().getLegacyId("MAGENTA");
		}

		ItemStack world = new ItemStack(Material.valueOf(magentaWool), 1, (byte) magentaWoolId);
		ItemMeta worldMeta = world.getItemMeta();
		assert worldMeta != null;
		worldMeta.setDisplayName(PlaceholderAPITest.testforPAPI(p, ChatColor.translateAlternateColorCodes('&',
				Messages.getMessage("EditGui", "AddWorldItem"))));
		world.setItemMeta(worldMeta);
		if (worldCount == 17) worldCount += 2;
		if (worldCount == 26) worldCount += 2;
		if (worldCount == 35) worldCount += 2;
		if (worldCount == 44) worldCount -= 1;
		inv.setItem(worldCount, world);

		Bukkit.getScheduler().runTaskLater(BukkitMain.get(), () -> p.openInventory(inv), 1);

	}

	@SuppressWarnings("deprecation")
	public void openPCommandsGui(Player p, String args) {
		if (!p.hasPermission("cb.edit")) {
			return;
		}
		FileConfiguration disabled = BukkitMain.oldConfig() ? OldConfigManager.MainDisabled :
				ConfigManager.MainDisabled;

		Inventory inv = Bukkit.createInventory(null, 54, PlaceholderAPITest.testforPAPI(p,
				ChatColor.translateAlternateColorCodes('&', Messages.getMessage("EditGui",
						"EditPlayerCommandsInventoryName"))));
		String pinkGlassBlock = new LegacyBlocks().getBlock("GLASS");
		int pinkGlassBlockId = 0;
		if (BukkitMain.coloredGlassPane()) {
			pinkGlassBlock = new LegacyBlocks().getBlock("PINK_GLASS");
			if (pinkGlassBlock.equalsIgnoreCase("stained_glass")) {
				pinkGlassBlockId = new LegacyBlocks().getLegacyId("PINK");
			}
		}

		ItemStack commandSlot = new ItemStack(Material.valueOf(pinkGlassBlock), 1, (byte) pinkGlassBlockId);

		if (BukkitMain.coloredGlassPane()) {
			String magentaGlass = new LegacyBlocks().getBlock("MAGENTA_GLASS_PANE");
			String pinkGlass = new LegacyBlocks().getBlock("PINK_GLASS_PANE");

			int magentaId = 0;
			int pinkId = 0;
			if (magentaGlass.equalsIgnoreCase("stained_glass_pane")) {
				magentaId = new LegacyBlocks().getLegacyId("MAGENTA");
			}
			if (pinkGlass.equalsIgnoreCase("stained_glass_pane")) {
				pinkId = new LegacyBlocks().getLegacyId("PINK");
			}

			ItemStack magentaGlassSlots = new ItemStack(Material.valueOf(magentaGlass), 1, (byte) magentaId);
			ItemStack pinkGlassSlots = new ItemStack(Material.valueOf(pinkGlass), 1, (byte) pinkId);

			ItemMeta magentaGlassMeta = magentaGlassSlots.getItemMeta();
			assert magentaGlassMeta != null;
			magentaGlassMeta.setDisplayName(" ");
			magentaGlassSlots.setItemMeta(magentaGlassMeta);

			ItemMeta pinkGlassMeta = pinkGlassSlots.getItemMeta();
			assert pinkGlassMeta != null;
			pinkGlassMeta.setDisplayName(" ");
			pinkGlassSlots.setItemMeta(pinkGlassMeta);

			for (int i = 0; i <= 8; i += 2) {
				inv.setItem(i, pinkGlassSlots);
				inv.setItem(i + 44, pinkGlassSlots);
			}
			for (int i = 1; i <= 9; i += 2) {
				inv.setItem(i, magentaGlassSlots);
				inv.setItem(i + 44, magentaGlassSlots);
			}
			inv.setItem(17, magentaGlassSlots);
			inv.setItem(18, pinkGlassSlots);
			inv.setItem(26, pinkGlassSlots);
			inv.setItem(27, magentaGlassSlots);
			inv.setItem(35, magentaGlassSlots);
			inv.setItem(36, pinkGlassSlots);
		}
		String pinkDye = new LegacyBlocks().getBlock("PINK_DYE");

		int pinkId = 0;
		if (pinkDye.equalsIgnoreCase("ink_sack")) {
			pinkId = 9;
		}
		ItemStack backSlot = new ItemStack(Material.valueOf(pinkDye), 1, (byte) pinkId);
		ItemMeta backSlotMeta = backSlot.getItemMeta();
		assert backSlotMeta != null;
		backSlotMeta.setDisplayName(PlaceholderAPITest.testforPAPI(p, ChatColor.translateAlternateColorCodes('&',
				Messages.getMessage("EditGui", "GoBackItemName"))));
		backSlot.setItemMeta(backSlotMeta);
		inv.setItem(49, backSlot);

		ItemMeta commandSlotMeta = commandSlot.getItemMeta();
		assert commandSlotMeta != null;
		commandSlotMeta.setDisplayName(PlaceholderAPITest.testforPAPI(p, ChatColor.translateAlternateColorCodes('&',
				Messages.getMessage("EditGui", "InventoryCommandName").replace("%c", args))));
		commandSlot.setItemMeta(commandSlotMeta);
		inv.setItem(4, commandSlot);

		int cmdCount = 10;
		if (!disabled.getStringList("DisabledCommands." + args + ".PlayerCommands").contains("none")) {
			for (String s : disabled.getStringList("DisabledCommands." + args + ".PlayerCommands")) {
				String limeWool = new LegacyBlocks().getBlock("LIME_WOOL");
				int limeWoolId = 0;
				if (limeWool.equalsIgnoreCase("wool")) {
					limeWoolId = new LegacyBlocks().getLegacyId("LIME");
				}

				ItemStack cmd = new ItemStack(Material.valueOf(limeWool), 1, (byte) limeWoolId);
				ItemMeta cmdMeta = cmd.getItemMeta();
				assert cmdMeta != null;
				cmdMeta.setDisplayName("/" + s);
				cmd.setItemMeta(cmdMeta);
				if (cmdCount == 17) cmdCount += 2;
				if (cmdCount == 26) cmdCount += 2;
				if (cmdCount == 35) cmdCount += 2;
				if (cmdCount == 43) break;
				inv.setItem(cmdCount, cmd);
				cmdCount++;
			}
		} else {
			for (String s : disabled.getStringList("DisabledCommands." + args + ".PlayerCommands")) {
				if (!s.equalsIgnoreCase("none")) {
					String redWool = new LegacyBlocks().getBlock("RED_WOOL");
					int redWoolId = 0;
					if (redWool.equalsIgnoreCase("wool")) {
						redWoolId = new LegacyBlocks().getLegacyId("RED");
					}

					ItemStack cmd = new ItemStack(Material.valueOf(redWool), 1, (byte) redWoolId);
					ItemMeta cmdMeta = cmd.getItemMeta();
					assert cmdMeta != null;
					cmdMeta.setDisplayName("/" + s);
					cmd.setItemMeta(cmdMeta);
					if (cmdCount == 17) cmdCount += 2;
					if (cmdCount == 26) cmdCount += 2;
					if (cmdCount == 35) cmdCount += 2;
					if (cmdCount == 43) break;
					inv.setItem(cmdCount, cmd);
					cmdCount++;
				} else {
					String limeWool = new LegacyBlocks().getBlock("LIME_WOOL");
					int limeWoolId = 0;
					if (limeWool.equalsIgnoreCase("wool")) {
						limeWoolId = new LegacyBlocks().getLegacyId("LIME");
					}

					ItemStack cmd = new ItemStack(Material.valueOf(limeWool), 1, (byte) limeWoolId);
					ItemMeta cmdMeta = cmd.getItemMeta();
					assert cmdMeta != null;
					cmdMeta.setDisplayName("none");
					cmd.setItemMeta(cmdMeta);
					if (cmdCount == 17) cmdCount += 2;
					if (cmdCount == 26) cmdCount += 2;
					if (cmdCount == 35) cmdCount += 2;
					if (cmdCount == 43) break;
					inv.setItem(cmdCount, cmd);
					cmdCount++;
				}
			}
		}
		String magentaWool = new LegacyBlocks().getBlock("MAGENTA_WOOL");
		int magentaWoolId = 0;
		if (magentaWool.equalsIgnoreCase("wool")) {
			magentaWoolId = new LegacyBlocks().getLegacyId("MAGENTA");
		}

		ItemStack cmd = new ItemStack(Material.valueOf(magentaWool), 1, (byte) magentaWoolId);
		ItemMeta cmdMeta = cmd.getItemMeta();
		assert cmdMeta != null;
		cmdMeta.setDisplayName(PlaceholderAPITest.testforPAPI(p, ChatColor.translateAlternateColorCodes('&',
				Messages.getMessage("EditGui", "AddPlayerCommandItem"))));
		cmd.setItemMeta(cmdMeta);
		if (cmdCount == 17) cmdCount += 2;
		if (cmdCount == 26) cmdCount += 2;
		if (cmdCount == 35) cmdCount += 2;
		if (cmdCount == 44) cmdCount -= 1;
		inv.setItem(cmdCount, cmd);

		Bukkit.getScheduler().runTaskLater(BukkitMain.get(), () -> p.openInventory(inv), 1);

	}

	@SuppressWarnings("deprecation")
	public void openCCommandsGui(Player p, String args) {
		if (!p.hasPermission("cb.edit")) {
			return;
		}
		FileConfiguration disabled = BukkitMain.oldConfig() ? OldConfigManager.MainDisabled :
				ConfigManager.MainDisabled;

		Inventory inv = Bukkit.createInventory(null, 54, PlaceholderAPITest.testforPAPI(p,
				ChatColor.translateAlternateColorCodes('&', Messages.getMessage("EditGui",
						"EditConsoleCommandsInventoryName"))));
		String pinkGlassBlock = new LegacyBlocks().getBlock("GLASS");
		int pinkGlassBlockId = 0;
		if (BukkitMain.coloredGlassPane()) {
			pinkGlassBlock = new LegacyBlocks().getBlock("PINK_GLASS");
			if (pinkGlassBlock.equalsIgnoreCase("stained_glass")) {
				pinkGlassBlockId = new LegacyBlocks().getLegacyId("PINK");
			}
		}

		ItemStack commandSlot = new ItemStack(Material.valueOf(pinkGlassBlock), 1, (byte) pinkGlassBlockId);

		if (BukkitMain.coloredGlassPane()) {
			String magentaGlass = new LegacyBlocks().getBlock("MAGENTA_GLASS_PANE");
			String pinkGlass = new LegacyBlocks().getBlock("PINK_GLASS_PANE");

			int magentaId = 0;
			int pinkId = 0;
			if (magentaGlass.equalsIgnoreCase("stained_glass_pane")) {
				magentaId = new LegacyBlocks().getLegacyId("MAGENTA");
			}
			if (pinkGlass.equalsIgnoreCase("stained_glass_pane")) {
				pinkId = new LegacyBlocks().getLegacyId("PINK");
			}

			ItemStack magentaGlassSlots = new ItemStack(Material.valueOf(magentaGlass), 1, (byte) magentaId);
			ItemStack pinkGlassSlots = new ItemStack(Material.valueOf(pinkGlass), 1, (byte) pinkId);

			ItemMeta magentaGlassMeta = magentaGlassSlots.getItemMeta();
			assert magentaGlassMeta != null;
			magentaGlassMeta.setDisplayName(" ");
			magentaGlassSlots.setItemMeta(magentaGlassMeta);

			ItemMeta pinkGlassMeta = pinkGlassSlots.getItemMeta();
			assert pinkGlassMeta != null;
			pinkGlassMeta.setDisplayName(" ");
			pinkGlassSlots.setItemMeta(pinkGlassMeta);

			for (int i = 0; i <= 8; i += 2) {
				inv.setItem(i, pinkGlassSlots);
				inv.setItem(i + 44, pinkGlassSlots);
			}
			for (int i = 1; i <= 9; i += 2) {
				inv.setItem(i, magentaGlassSlots);
				inv.setItem(i + 44, magentaGlassSlots);
			}
			inv.setItem(17, magentaGlassSlots);
			inv.setItem(18, pinkGlassSlots);
			inv.setItem(26, pinkGlassSlots);
			inv.setItem(27, magentaGlassSlots);
			inv.setItem(35, magentaGlassSlots);
			inv.setItem(36, pinkGlassSlots);
		}
		String pinkDye = new LegacyBlocks().getBlock("PINK_DYE");

		int pinkId = 0;
		if (pinkDye.equalsIgnoreCase("ink_sack")) {
			pinkId = 9;
		}
		ItemStack backSlot = new ItemStack(Material.valueOf(pinkDye), 1, (byte) pinkId);
		ItemMeta backSlotMeta = backSlot.getItemMeta();
		assert backSlotMeta != null;
		backSlotMeta.setDisplayName(PlaceholderAPITest.testforPAPI(p, ChatColor.translateAlternateColorCodes('&',
				Messages.getMessage("EditGui", "GoBackItemName"))));
		backSlot.setItemMeta(backSlotMeta);
		inv.setItem(49, backSlot);

		ItemMeta commandSlotMeta = commandSlot.getItemMeta();
		assert commandSlotMeta != null;
		commandSlotMeta.setDisplayName(PlaceholderAPITest.testforPAPI(p, ChatColor.translateAlternateColorCodes('&',
				Messages.getMessage("EditGui", "InventoryCommandName").replace("%c", args))));
		commandSlot.setItemMeta(commandSlotMeta);
		inv.setItem(4, commandSlot);

		int cmdCount = 10;
		if (!disabled.getStringList("DisabledCommands." + args + ".ConsoleCommands").contains("none")) {
			for (String s : disabled.getStringList("DisabledCommands." + args + ".ConsoleCommands")) {
				String limeWool = new LegacyBlocks().getBlock("LIME_WOOL");
				int limeWoolId = 0;
				if (limeWool.equalsIgnoreCase("wool")) {
					limeWoolId = new LegacyBlocks().getLegacyId("LIME");
				}

				ItemStack cmd = new ItemStack(Material.valueOf(limeWool), 1, (byte) limeWoolId);
				ItemMeta cmdMeta = cmd.getItemMeta();
				assert cmdMeta != null;
				cmdMeta.setDisplayName("/" + s);
				cmd.setItemMeta(cmdMeta);
				if (cmdCount == 17) cmdCount += 2;
				if (cmdCount == 26) cmdCount += 2;
				if (cmdCount == 35) cmdCount += 2;
				if (cmdCount == 43) break;
				inv.setItem(cmdCount, cmd);
				cmdCount++;
			}
		} else {
			for (String s : disabled.getStringList("DisabledCommands." + args + ".ConsoleCommands")) {
				if (!s.equalsIgnoreCase("none")) {
					String redWool = new LegacyBlocks().getBlock("RED_WOOL");
					int redWoolId = 0;
					if (redWool.equalsIgnoreCase("wool")) {
						redWoolId = new LegacyBlocks().getLegacyId("RED");
					}

					ItemStack cmd = new ItemStack(Material.valueOf(redWool), 1, (byte) redWoolId);
					ItemMeta cmdMeta = cmd.getItemMeta();
					assert cmdMeta != null;
					cmdMeta.setDisplayName("/" + s);
					cmd.setItemMeta(cmdMeta);
					if (cmdCount == 17) cmdCount += 2;
					if (cmdCount == 26) cmdCount += 2;
					if (cmdCount == 35) cmdCount += 2;
					if (cmdCount == 43) break;
					inv.setItem(cmdCount, cmd);
					cmdCount++;
				} else {
					String limeWool = new LegacyBlocks().getBlock("LIME_WOOL");
					int limeWoolId = 0;
					if (limeWool.equalsIgnoreCase("wool")) {
						limeWoolId = new LegacyBlocks().getLegacyId("LIME");
					}

					ItemStack cmd = new ItemStack(Material.valueOf(limeWool), 1, (byte) limeWoolId);
					ItemMeta cmdMeta = cmd.getItemMeta();
					assert cmdMeta != null;
					cmdMeta.setDisplayName("none");
					cmd.setItemMeta(cmdMeta);
					if (cmdCount == 17) cmdCount += 2;
					if (cmdCount == 26) cmdCount += 2;
					if (cmdCount == 35) cmdCount += 2;
					if (cmdCount == 43) break;
					inv.setItem(cmdCount, cmd);
					cmdCount++;
				}
			}
		}
		String magentaWool = new LegacyBlocks().getBlock("MAGENTA_WOOL");
		int magentaWoolId = 0;
		if (magentaWool.equalsIgnoreCase("wool")) {
			magentaWoolId = new LegacyBlocks().getLegacyId("MAGENTA");
		}

		ItemStack cmd = new ItemStack(Material.valueOf(magentaWool), 1, (byte) magentaWoolId);
		ItemMeta cmdMeta = cmd.getItemMeta();
		assert cmdMeta != null;
		cmdMeta.setDisplayName(PlaceholderAPITest.testforPAPI(p, ChatColor.translateAlternateColorCodes('&',
				Messages.getMessage("EditGui", "AddConsoleCommandItem"))));
		cmd.setItemMeta(cmdMeta);
		if (cmdCount == 17) cmdCount += 2;
		if (cmdCount == 26) cmdCount += 2;
		if (cmdCount == 35) cmdCount += 2;
		if (cmdCount == 44) cmdCount -= 1;
		inv.setItem(cmdCount, cmd);

		Bukkit.getScheduler().runTaskLater(BukkitMain.get(), () -> p.openInventory(inv), 1);

	}
	
}
