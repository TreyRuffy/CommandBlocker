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
import org.bukkit.event.inventory.InventoryAction;
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

public class OpDisabledGui implements Listener {


	private final HashMap<String, String> lookingFor = new HashMap<>();
	private final HashMap<String, String> command = new HashMap<>();

	@SuppressWarnings("deprecation")
	public void openOpGui(Player p, String args) {
		if (!p.hasPermission("cb.editop")) {
			return;
		}
		Inventory inv = Bukkit.createInventory(null, 27, PlaceholderAPITest.testforPAPI(p,
				ChatColor.translateAlternateColorCodes('&', Messages.getMessage("EditOpGui", "EditCommandInventoryName"
				))));
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
				if ((i + 18) < 26) inv.setItem(i + 18, pinkGlassSlots);
			}
			for (int i = 1; i <= 9; i += 2) {
				inv.setItem(i, magentaGlassSlots);
				if ((i + 18) < 26) inv.setItem(i + 18, magentaGlassSlots);
			}
			inv.setItem(17, magentaGlassSlots);
			inv.setItem(26, pinkGlassSlots);
		}

		ItemStack secondSlot = new ItemStack(Material.valueOf(magentaWool), 1, (byte) magentaId);
		ItemStack thirdSlot = new ItemStack(Material.valueOf(purpleWool), 1, (byte) purpleId);
		ItemStack fourthSlot = new ItemStack(Material.valueOf(blueWool), 1, (byte) blueId);
		ItemStack fifthSlot = new ItemStack(Material.valueOf(lightBlueWool), 1, (byte) lightBlueId);
		ItemStack sixthSlot;
		FileConfiguration opDisabled = BukkitMain.oldConfig() ? OldConfigManager.OpDisabled : ConfigManager.OpDisabled;
		if (opDisabled.getString("DisabledOpCommands." + args + ".NoTabComplete") == null) {
			opDisabled.set("DisabledOpCommands." + args + ".NoTabComplete", true);
			if (!BukkitMain.oldConfig()) {
				ConfigManager.saveOpDisabled();
			} else {
				OldConfigManager.saveOpDisabled();
			}
		}
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
		if (!opDisabled.getBoolean("DisabledOpCommands." + args + ".NoTabComplete")) {
			sixthSlot = new ItemStack(Material.valueOf(redWool), 1, (byte) redWoolId);
		} else {
			sixthSlot = new ItemStack(Material.valueOf(greenWool), 1, (byte) greenWoolId);
		}

		ItemMeta commandSlotMeta = commandSlot.getItemMeta();
		assert commandSlotMeta != null;
		commandSlotMeta.setDisplayName(PlaceholderAPITest.testforPAPI(p, ChatColor.translateAlternateColorCodes('&',
				Messages.getMessage("EditOpGui", "InventoryCommandName").replace("%c", args))));
		commandSlot.setItemMeta(commandSlotMeta);
		inv.setItem(4, commandSlot);


		ItemMeta secondSlotMeta = secondSlot.getItemMeta();
		assert secondSlotMeta != null;
		secondSlotMeta.setDisplayName(PlaceholderAPITest.testforPAPI(p, ChatColor.translateAlternateColorCodes('&',
				Messages.getMessage("EditOpGui", "MessageItemName"))));
		String currentMessage = ChatColor.WHITE + ChatColor.translateAlternateColorCodes('&', BukkitMain.oldConfig() ?
				OldConfigManager.OpDisabled.getString("DisabledOpCommands." + args + ".Message") :
				ConfigManager.OpDisabled.getString("DisabledOpCommands." + args + ".Message"));
		List<String> secondSlotLore = new ArrayList<>();
		for (String message : Messages.getMessages("EditOpGui", "MessageItemLore")) {
			secondSlotLore.add(PlaceholderAPITest.testforPAPI(p, ChatColor.translateAlternateColorCodes('&', message)).replace("%m", currentMessage));
		}
		secondSlotMeta.setLore(secondSlotLore);
		secondSlot.setItemMeta(secondSlotMeta);
		inv.setItem(11, secondSlot);


		ItemMeta thirdSlotMeta = thirdSlot.getItemMeta();
		assert thirdSlotMeta != null;
		thirdSlotMeta.setDisplayName(PlaceholderAPITest.testforPAPI(p, ChatColor.translateAlternateColorCodes('&',
				Messages.getMessage("EditOpGui", "WorldsItemName"))));
		thirdSlot.setItemMeta(thirdSlotMeta);
		inv.setItem(12, thirdSlot);


		ItemMeta fourthSlotMeta = fourthSlot.getItemMeta();
		assert fourthSlotMeta != null;
		fourthSlotMeta.setDisplayName(PlaceholderAPITest.testforPAPI(p, ChatColor.translateAlternateColorCodes('&',
				Messages.getMessage("EditOpGui", "PlayerCommandsItemName"))));
		fourthSlot.setItemMeta(fourthSlotMeta);
		inv.setItem(13, fourthSlot);


		ItemMeta fifthSlotMeta = fifthSlot.getItemMeta();
		assert fifthSlotMeta != null;
		fifthSlotMeta.setDisplayName(PlaceholderAPITest.testforPAPI(p, ChatColor.translateAlternateColorCodes('&',
				Messages.getMessage("EditOpGui", "ConsoleCommandsItemName"))));
		fifthSlot.setItemMeta(fifthSlotMeta);
		inv.setItem(14, fifthSlot);


		ItemMeta sixthSlotMeta = sixthSlot.getItemMeta();
		assert sixthSlotMeta != null;
		sixthSlotMeta.setDisplayName(PlaceholderAPITest.testforPAPI(p, ChatColor.translateAlternateColorCodes('&',
				Messages.getMessage("EditOpGui", "NoTabCompletion"))));
		sixthSlot.setItemMeta(sixthSlotMeta);
		inv.setItem(15, sixthSlot);


		Bukkit.getScheduler().runTaskLater(BukkitMain.get(), () -> p.openInventory(inv), 1);
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		if (e.getInventory().getHolder() != null) {
			return;
		}
		if (!e.getWhoClicked().hasPermission("cb.editop")) {
			return;
		}
		if (e.getCurrentItem() == null) {
			return;
		}
		if (e.getCurrentItem().getItemMeta() == null) {
			return;
		}
		FileConfiguration opDisabled = BukkitMain.oldConfig() ? OldConfigManager.OpDisabled : ConfigManager.OpDisabled;
		if (e.getView().getTitle().equals(PlaceholderAPITest.testforPAPI((Player) e.getWhoClicked(),
				ChatColor.translateAlternateColorCodes('&', Messages.getMessage("EditOpGui", "EditCommandInventoryName"
				))))) {
			if (e.getRawSlot() < 0 || e.getRawSlot() >= 54 || (!e.getCurrentItem().getItemMeta().hasDisplayName())) {
				e.setCancelled(true);
				return;
			}
			String cmd = ChatColor.stripColor(e.getInventory().getItem(4).getItemMeta().getDisplayName()).substring(1);
			Player p = (Player) e.getWhoClicked();
			if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(PlaceholderAPITest.testforPAPI(p,
					ChatColor.translateAlternateColorCodes('&', Messages.getMessage("EditOpGui", "MessageItemName"))))) {
				p.closeInventory();

				for (String message : Messages.getMessages("EditOpGui", "ChangeMessage")) {
					p.sendMessage(PlaceholderAPITest.testforPAPI(p, ChatColor.translateAlternateColorCodes('&',
							message.replace("%c", cmd))));
				}
				lookingFor.put(p.getUniqueId().toString(), "message");
				command.put(p.getUniqueId().toString(), cmd);
			} else if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(PlaceholderAPITest.testforPAPI(p, ChatColor.translateAlternateColorCodes('&', Messages.getMessage("EditOpGui", "WorldsItemName"))))) {
				openWorldGui(p, cmd);

			} else if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(PlaceholderAPITest.testforPAPI(p, ChatColor.translateAlternateColorCodes('&', Messages.getMessage("EditOpGui", "PlayerCommandsItemName"))))) {
				openPCommandsGui(p, cmd);

			} else if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(PlaceholderAPITest.testforPAPI(p, ChatColor.translateAlternateColorCodes('&', Messages.getMessage("EditOpGui", "ConsoleCommandsItemName"))))) {
				openCCommandsGui(p, cmd);

			} else if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(PlaceholderAPITest.testforPAPI(p, ChatColor.translateAlternateColorCodes('&', Messages.getMessage("EditOpGui", "NoTabCompletion"))))) {
				if (!opDisabled.getBoolean("DisabledOpCommands." + cmd + ".NoTabComplete")) {
					opDisabled.set("DisabledOpCommands." + cmd + ".NoTabComplete", true);
					Log.addLog(Universal.get().getMethods(), p.getName() + ": Changed tab completion for /" + cmd + " "
							+ "from off to on in opblock.yml");
				} else {
					opDisabled.set("DisabledOpCommands." + cmd + ".NoTabComplete", false);
					Log.addLog(Universal.get().getMethods(), p.getName() + ": Changed tab completion for /" + cmd + " "
							+ "from on to off in opblock.yml");
				}
				if (!BukkitMain.oldConfig()) {
					ConfigManager.saveOpDisabled();
				} else {
					OldConfigManager.saveOpDisabled();
				}

				openOpGui(p, cmd);

			}
			e.setCancelled(true);
		} else if (e.getView().getTitle().equals(PlaceholderAPITest.testforPAPI((Player) e.getWhoClicked(),
				ChatColor.translateAlternateColorCodes('&', Messages.getMessage("EditOpGui",
						"EditWorldsInventoryName"))))) {
			if (e.getRawSlot() < 0 || e.getRawSlot() >= 54 || (!e.getCurrentItem().getItemMeta().hasDisplayName())) {
				e.setCancelled(true);
				return;
			}
			String cmd = ChatColor.stripColor(e.getInventory().getItem(4).getItemMeta().getDisplayName()).substring(1);
			Player p = (Player) e.getWhoClicked();

			if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(PlaceholderAPITest.testforPAPI(p,
					ChatColor.translateAlternateColorCodes('&', Messages.getMessage("EditOpGui", "GoBackItemName"))))) {
				openOpGui(p, cmd);

			} else if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(PlaceholderAPITest.testforPAPI(p, ChatColor.translateAlternateColorCodes('&', Messages.getMessage("EditOpGui", "AddWorldItem"))))) {

				command.put(p.getUniqueId().toString(), cmd);
				lookingFor.put(p.getUniqueId().toString(), "world");
				for (String message : Messages.getMessages("EditOpGui", "AddWorldMessage")) {
					p.sendMessage(PlaceholderAPITest.testforPAPI(p, ChatColor.translateAlternateColorCodes('&',
							message.replace("%c", cmd))));

				}

				p.closeInventory();
			} else if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(" ")) {
				e.setCancelled(true);
			} else if (!(e.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase(PlaceholderAPITest.testforPAPI(p, ChatColor.translateAlternateColorCodes('&', Messages.getMessage("EditOpGui", "InventoryCommandName").replace("%c", cmd))))) {

				FileConfiguration disabled = BukkitMain.oldConfig() ? OldConfigManager.OpDisabled :
						ConfigManager.OpDisabled;
				if (disabled.getStringList("DisabledOpCommands." + cmd + ".Worlds").indexOf("all") != -1) {
					List<String> worlds = new ArrayList<>();
					for (World w : Bukkit.getWorlds()) {
						worlds.add(w.getName());
					}
					disabled.set("DisabledOpCommands." + cmd + ".Worlds", worlds);
					if (!BukkitMain.oldConfig()) {
						ConfigManager.saveOpDisabled();
					} else {
						OldConfigManager.saveOpDisabled();
					}
				}
				List<String> newWorldList = disabled.getStringList("DisabledOpCommands." + cmd + ".Worlds");
				if (disabled.getStringList("DisabledOpCommands." + cmd + ".Worlds").indexOf(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName())) != -1) {
					newWorldList.remove(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()));
					Log.addLog(Universal.get().getMethods(), p.getName() + ": Removed world " + e.getCurrentItem().getItemMeta().getDisplayName() + " for /" + cmd + " in opblock.yml");
				} else {
					newWorldList.add(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()));
					Log.addLog(Universal.get().getMethods(), p.getName() + ": Added world " + e.getCurrentItem().getItemMeta().getDisplayName() + " for /" + cmd + " in opblock.yml");
				}

				disabled.set("DisabledOpCommands." + cmd + ".Worlds", newWorldList);
				if (!BukkitMain.oldConfig()) {
					ConfigManager.saveOpDisabled();
				} else {
					OldConfigManager.saveOpDisabled();
				}
				openWorldGui(p, cmd);
			}
			e.setCancelled(true);
		} else if (e.getView().getTitle().equals(PlaceholderAPITest.testforPAPI((Player) e.getWhoClicked(),
				ChatColor.translateAlternateColorCodes('&', Messages.getMessage("EditOpGui",
						"EditPlayerCommandsInventoryName"))))) {
			if (e.getRawSlot() < 0 || e.getRawSlot() >= 54 || (!e.getCurrentItem().getItemMeta().hasDisplayName())) {
				e.setCancelled(true);
				return;
			}
			String cmd = ChatColor.stripColor(e.getInventory().getItem(4).getItemMeta().getDisplayName()).substring(1);
			Player p = (Player) e.getWhoClicked();
			if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(PlaceholderAPITest.testforPAPI(p,
					ChatColor.translateAlternateColorCodes('&', Messages.getMessage("EditOpGui", "GoBackItemName"))))) {
				openOpGui(p, cmd);
			} else if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(PlaceholderAPITest.testforPAPI(p, ChatColor.translateAlternateColorCodes('&', Messages.getMessage("EditOpGui", "AddPlayerCommandItem"))))) {
				command.put(p.getUniqueId().toString(), cmd);
				lookingFor.put(p.getUniqueId().toString(), "playercommand");
				for (String message : Messages.getMessages("EditOpGui", "AddPlayerCommandMessage")) {
					p.sendMessage(PlaceholderAPITest.testforPAPI(p, ChatColor.translateAlternateColorCodes('&',
							message.replace("%c", cmd))));
				}
				p.closeInventory();
			} else if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(" ")) {
				e.setCancelled(true);
			} else if (!(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(PlaceholderAPITest.testforPAPI(p, ChatColor.translateAlternateColorCodes('&', Messages.getMessage("EditOpGui", "InventoryCommandName").replace("%c", cmd)))))) {

				FileConfiguration disabled = BukkitMain.oldConfig() ? OldConfigManager.OpDisabled :
						ConfigManager.OpDisabled;
				List<String> newPCommandList = disabled.getStringList("DisabledOpCommands." + cmd + ".PlayerCommands");
				if (!ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase("none")) {
					newPCommandList.remove(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).substring(1));
					Log.addLog(Universal.get().getMethods(),
							p.getName() + ": Removed player command " + e.getCurrentItem().getItemMeta().getDisplayName() + " from /" + cmd + " in opblock.yml");
				} else {
					newPCommandList.remove(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()));
					Log.addLog(Universal.get().getMethods(),
							p.getName() + ": Removed none tag for player commands " + "from /" + cmd + " in opblock" +
									".yml");
				}

				disabled.set("DisabledOpCommands." + cmd + ".PlayerCommands", newPCommandList);
				if (!BukkitMain.oldConfig()) {
					ConfigManager.saveOpDisabled();
				} else {
					OldConfigManager.saveOpDisabled();
				}
				openPCommandsGui(p, cmd);
			}
			e.setCancelled(true);
		} else if (e.getView().getTitle().equals(PlaceholderAPITest.testforPAPI((Player) e.getWhoClicked(),
				ChatColor.translateAlternateColorCodes('&', Messages.getMessage("EditOpGui",
						"EditConsoleCommandsInventoryName"))))) {
			if (e.getRawSlot() < 0 || e.getRawSlot() >= 54 || (!e.getCurrentItem().getItemMeta().hasDisplayName())) {
				e.setCancelled(true);
				return;
			}
			String cmd = ChatColor.stripColor(e.getInventory().getItem(4).getItemMeta().getDisplayName()).substring(1);
			Player p = (Player) e.getWhoClicked();
			if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(PlaceholderAPITest.testforPAPI(p,
					ChatColor.translateAlternateColorCodes('&', Messages.getMessage("EditOpGui", "GoBackItemName"))))) {
				openOpGui(p, cmd);
			} else if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(PlaceholderAPITest.testforPAPI(p, ChatColor.translateAlternateColorCodes('&', Messages.getMessage("EditOpGui", "AddConsoleCommandItem"))))) {
				command.put(p.getUniqueId().toString(), cmd);
				lookingFor.put(p.getUniqueId().toString(), "consolecommand");
				for (String message : Messages.getMessages("EditOpGui", "AddConsoleCommandMessage")) {
					p.sendMessage(PlaceholderAPITest.testforPAPI(p, ChatColor.translateAlternateColorCodes('&',
							message.replace("%c", cmd))));
				}
				p.closeInventory();
			} else if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(" ")) {
				e.setCancelled(true);
			} else if (!(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(PlaceholderAPITest.testforPAPI(p, ChatColor.translateAlternateColorCodes('&', Messages.getMessage("EditOpGui", "InventoryCommandName").replace("%c", cmd)))))) {

				FileConfiguration disabled = BukkitMain.oldConfig() ? OldConfigManager.OpDisabled :
						ConfigManager.OpDisabled;
				List<String> newPCommandList = disabled.getStringList("DisabledOpCommands." + cmd +
						".ConsoleCommands");
				if (!ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase("none")) {
					newPCommandList.remove(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).substring(1));
					Log.addLog(Universal.get().getMethods(),
							p.getName() + ": Removed console command " + e.getCurrentItem().getItemMeta().getDisplayName() + " from /" + cmd + " in opblock.yml");
				} else {
					newPCommandList.remove(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()));
					Log.addLog(Universal.get().getMethods(),
							p.getName() + ": Removed none tag for console commands " + "from /" + cmd + " in opblock" +
									".yml");
				}
				disabled.set("DisabledOpCommands." + cmd + ".ConsoleCommands", newPCommandList);
				if (!BukkitMain.oldConfig()) {
					ConfigManager.saveOpDisabled();
				} else {
					OldConfigManager.saveOpDisabled();
				}
				openCCommandsGui(p, cmd);
			}
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		Player p = e.getPlayer();
		if (!p.hasPermission("cb.editop")) {
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
		FileConfiguration disabled = BukkitMain.oldConfig() ? OldConfigManager.OpDisabled : ConfigManager.OpDisabled;
		if (lookingFor.get(p.getUniqueId().toString()).equalsIgnoreCase("message")) {
			Log.addLog(Universal.get().getMethods(), p.getName() + ": Changed message from " +
					disabled.getString("DisabledOpCommands." + command.get(p.getUniqueId().toString()) + ".Message") + " to "+ e.getMessage()
					+ "for /" + command.get(p.getUniqueId().toString()) + " in opblock.yml");
			disabled.set("DisabledOpCommands." + command.get(p.getUniqueId().toString()) + ".Message", e.getMessage());
			if (!BukkitMain.oldConfig()) {
				ConfigManager.saveOpDisabled();
			} else {
				OldConfigManager.saveOpDisabled();
			}
			lookingFor.remove(p.getUniqueId().toString());
			
			openOpGui(p, command.get(p.getUniqueId().toString()));
			command.remove(p.getUniqueId().toString());
		} else if (lookingFor.get(p.getUniqueId().toString()).equalsIgnoreCase("world")) {
			Log.addLog(Universal.get().getMethods(), p.getName() + ": Added world " + e.getMessage() + " for /" + command.get(p.getUniqueId().toString()) + " in opblock.yml");
			List<String> worldList = disabled.getStringList("DisabledOpCommands." + command.get(p.getUniqueId().toString()) + ".Worlds");
			worldList.add(e.getMessage());
			disabled.set("DisabledOpCommands." + command.get(p.getUniqueId().toString()) + ".Worlds", worldList);
			if (!BukkitMain.oldConfig()) {
				ConfigManager.saveOpDisabled();
			} else {
				OldConfigManager.saveOpDisabled();
			}
			lookingFor.remove(p.getUniqueId().toString());
			
			openWorldGui(p, command.get(p.getUniqueId().toString()));
			command.remove(p.getUniqueId().toString());
		} else if (lookingFor.get(p.getUniqueId().toString()).equalsIgnoreCase("playercommand")) {
			Log.addLog(Universal.get().getMethods(), p.getName() + ": Added player command /" + e.getMessage() + " for /" + command.get(p.getUniqueId().toString()) + " in opblock.yml");
			List<String> pCmdList = disabled.getStringList("DisabledOpCommands." + command.get(p.getUniqueId().toString()) + ".PlayerCommands");
			pCmdList.add(e.getMessage());
			disabled.set("DisabledOpCommands." + command.get(p.getUniqueId().toString()) + ".PlayerCommands", pCmdList);
			if (!BukkitMain.oldConfig()) {
				ConfigManager.saveOpDisabled();
			} else {
				OldConfigManager.saveOpDisabled();
			}
			lookingFor.remove(p.getUniqueId().toString());
			openPCommandsGui(p, command.get(p.getUniqueId().toString()));
			command.remove(p.getUniqueId().toString());
		} else if (lookingFor.get(p.getUniqueId().toString()).equalsIgnoreCase("consolecommand")) {
			Log.addLog(Universal.get().getMethods(), p.getName() + ": Added console command /" + e.getMessage() + " for /" + command.get(p.getUniqueId().toString()) + " in opblock.yml");
			List<String> cCmdList = disabled.getStringList("DisabledOpCommands." + command.get(p.getUniqueId().toString()) + ".ConsoleCommands");
			cCmdList.add(e.getMessage());
			disabled.set("DisabledOpCommands." + command.get(p.getUniqueId().toString()) + ".ConsoleCommands", cCmdList);
			if (!BukkitMain.oldConfig()) {
				ConfigManager.saveOpDisabled();
			} else {
				OldConfigManager.saveOpDisabled();
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
		if (!p.hasPermission("cb.editop")) {
			return;
		}
		if (!lookingFor.containsKey(p.getUniqueId().toString())) {
			return;
		}
		
		if (lookingFor.get(p.getUniqueId().toString()).equalsIgnoreCase("permission")) {
			for (String message : Messages.getMessages("EditOpGui", "CannotUseCommand")) {
				p.sendMessage(PlaceholderAPITest.testforPAPI(p, ChatColor.translateAlternateColorCodes('&', message)));
			}
		} else if (lookingFor.get(p.getUniqueId().toString()).equalsIgnoreCase("message")) {
			for (String message : Messages.getMessages("EditOpGui", "CannotUseCommand")) {
				p.sendMessage(PlaceholderAPITest.testforPAPI(p, ChatColor.translateAlternateColorCodes('&', message)));
			}
		} else if (lookingFor.get(p.getUniqueId().toString()).equalsIgnoreCase("world")) {
			for (String message : Messages.getMessages("EditOpGui", "CannotUseCommand")) {
				p.sendMessage(PlaceholderAPITest.testforPAPI(p, ChatColor.translateAlternateColorCodes('&', message)));
			}
		} else if (lookingFor.get(p.getUniqueId().toString()).equalsIgnoreCase("playercommand")) {
			Log.addLog(Universal.get().getMethods(), p.getName() + ": Added player command " + e.getMessage() + " " +
					"for /" + command.get(p.getUniqueId().toString()) + " in opblock.yml");
			FileConfiguration disabled = BukkitMain.oldConfig() ? OldConfigManager.OpDisabled :
					ConfigManager.OpDisabled;
			List<String> pCmdList =
					disabled.getStringList("DisabledOpCommands." + command.get(p.getUniqueId().toString()) +
							".PlayerCommands");
			pCmdList.add(e.getMessage().substring(1));
			disabled.set("DisabledOpCommands." + command.get(p.getUniqueId().toString()) + ".PlayerCommands",
					pCmdList);
			if (!BukkitMain.oldConfig()) {
				ConfigManager.saveOpDisabled();
			} else {
				OldConfigManager.saveOpDisabled();
			}
			lookingFor.remove(p.getUniqueId().toString());
			openPCommandsGui(p, command.get(p.getUniqueId().toString()));
			command.remove(p.getUniqueId().toString());
		} else if (lookingFor.get(p.getUniqueId().toString()).equalsIgnoreCase("consolecommand")) {
			Log.addLog(Universal.get().getMethods(), p.getName() + ": Added console command " + e.getMessage() + " for /" + command.get(p.getUniqueId().toString()) + " in opblock.yml");
			FileConfiguration disabled = BukkitMain.oldConfig() ? OldConfigManager.OpDisabled : ConfigManager.OpDisabled;
			List<String> cCmdList = disabled.getStringList("DisabledOpCommands." + command.get(p.getUniqueId().toString()) + ".ConsoleCommands");
			cCmdList.add(e.getMessage().substring(1));
			disabled.set("DisabledOpCommands." + command.get(p.getUniqueId().toString()) + ".ConsoleCommands", cCmdList);
			if (!BukkitMain.oldConfig()) {
				ConfigManager.saveOpDisabled();
			} else {
				OldConfigManager.saveOpDisabled();
			}
			lookingFor.remove(p.getUniqueId().toString());
			openCCommandsGui(p, command.get(p.getUniqueId().toString()));
			command.remove(p.getUniqueId().toString());
		}
		e.setCancelled(true);
	}

	@SuppressWarnings("deprecation")
	public void openWorldGui(Player p, String args) {
		if (!p.hasPermission("cb.editop")) {
			return;
		}
		FileConfiguration disabled = BukkitMain.oldConfig() ? OldConfigManager.OpDisabled : ConfigManager.OpDisabled;

		Inventory inv = Bukkit.createInventory(null, 54, PlaceholderAPITest.testforPAPI(p,
				ChatColor.translateAlternateColorCodes('&', Messages.getMessage("EditOpGui",
						"EditWorldsInventoryName"))));
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
				Messages.getMessage("EditOpGui", "GoBackItemName"))));
		backSlot.setItemMeta(backSlotMeta);
		inv.setItem(49, backSlot);

		ItemMeta commandSlotMeta = commandSlot.getItemMeta();
		assert commandSlotMeta != null;
		commandSlotMeta.setDisplayName(PlaceholderAPITest.testforPAPI(p, ChatColor.translateAlternateColorCodes('&',
				Messages.getMessage("EditOpGui", "InventoryCommandName").replace("%c", args))));
		commandSlot.setItemMeta(commandSlotMeta);
		inv.setItem(4, commandSlot);

		if (disabled.getString("DisabledOpCommands." + args + ".Worlds") == null) {
			List<String> list = new ArrayList<String>();
			list.add("all");
			disabled.set("DisabledOpCommands." + args + ".Worlds", list);
			if (!BukkitMain.oldConfig()) {
				ConfigManager.saveOpDisabled();
			} else {
				OldConfigManager.saveOpDisabled();
			}
		}

		int worldCount = 10;
		for (World w : Bukkit.getWorlds()) {
			if ((disabled.getStringList("DisabledOpCommands." + args + ".Worlds").indexOf(w.getName()) != -1) || (disabled.getStringList("DisabledOpCommands." + args + ".Worlds").indexOf("all") != -1)) {
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
		for (String s : disabled.getStringList("DisabledOpCommands." + args + ".Worlds")) {
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
					List<String> str = disabled.getStringList("DisabledOpCommands." + args + ".Worlds");
					str.remove("all");
					disabled.set("DisabledOpCommands." + args + ".Worlds", str);
					if (!BukkitMain.oldConfig()) {
						ConfigManager.saveOpDisabled();
					} else {
						OldConfigManager.saveOpDisabled();
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
				Messages.getMessage("EditOpGui", "AddWorldItem"))));
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
		if (!p.hasPermission("cb.editop")) {
			return;
		}
		FileConfiguration disabled = BukkitMain.oldConfig() ? OldConfigManager.OpDisabled : ConfigManager.OpDisabled;

		Inventory inv = Bukkit.createInventory(null, 54, PlaceholderAPITest.testforPAPI(p,
				ChatColor.translateAlternateColorCodes('&', Messages.getMessage("EditOpGui",
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
				Messages.getMessage("EditOpGui", "GoBackItemName"))));
		backSlot.setItemMeta(backSlotMeta);
		inv.setItem(49, backSlot);

		ItemMeta commandSlotMeta = commandSlot.getItemMeta();
		assert commandSlotMeta != null;
		commandSlotMeta.setDisplayName(PlaceholderAPITest.testforPAPI(p, ChatColor.translateAlternateColorCodes('&',
				Messages.getMessage("EditOpGui", "InventoryCommandName").replace("%c", args))));
		commandSlot.setItemMeta(commandSlotMeta);
		inv.setItem(4, commandSlot);

		int cmdCount = 10;
		if (!disabled.getStringList("DisabledOpCommands." + args + ".PlayerCommands").contains("none")) {
			for (String s : disabled.getStringList("DisabledOpCommands." + args + ".PlayerCommands")) {
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
			for (String s : disabled.getStringList("DisabledOpCommands." + args + ".PlayerCommands")) {
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
				Messages.getMessage("EditOpGui", "AddPlayerCommandItem"))));
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
		if (!p.hasPermission("cb.editop")) {
			return;
		}
		FileConfiguration disabled = BukkitMain.oldConfig() ? OldConfigManager.OpDisabled : ConfigManager.OpDisabled;

		Inventory inv = Bukkit.createInventory(null, 54, PlaceholderAPITest.testforPAPI(p,
				ChatColor.translateAlternateColorCodes('&', Messages.getMessage("EditOpGui",
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
				Messages.getMessage("EditOpGui", "GoBackItemName"))));
		backSlot.setItemMeta(backSlotMeta);
		inv.setItem(49, backSlot);

		ItemMeta commandSlotMeta = commandSlot.getItemMeta();
		assert commandSlotMeta != null;
		commandSlotMeta.setDisplayName(PlaceholderAPITest.testforPAPI(p, ChatColor.translateAlternateColorCodes('&',
				Messages.getMessage("EditOpGui", "InventoryCommandName").replace("%c", args))));
		commandSlot.setItemMeta(commandSlotMeta);
		inv.setItem(4, commandSlot);

		int cmdCount = 10;
		if (!disabled.getStringList("DisabledOpCommands." + args + ".ConsoleCommands").contains("none")) {
			for (String s : disabled.getStringList("DisabledOpCommands." + args + ".ConsoleCommands")) {
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
				inv.setItem(cmdCount + 10, cmd);
				cmdCount++;
			}
		} else {
			for (String s : disabled.getStringList("DisabledOpCommands." + args + ".ConsoleCommands")) {
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
					inv.setItem(cmdCount + 9, cmd);
					cmdCount++;
				} else {
					String limeWool = new LegacyBlocks().getBlock("LIME_WOOL");
					int limeWoolId = 0;
					if (limeWool.equalsIgnoreCase("wool")) {
						limeWoolId = new LegacyBlocks().getLegacyId("LIME");
					}

					ItemStack cmd = new ItemStack(Material.valueOf(limeWool), 1, (byte) limeWoolId);
					ItemMeta cmdMeta = cmd.getItemMeta();
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
				Messages.getMessage("EditOpGui", "AddConsoleCommandItem"))));
		cmd.setItemMeta(cmdMeta);
		if (cmdCount == 17) cmdCount += 2;
		if (cmdCount == 26) cmdCount += 2;
		if (cmdCount == 35) cmdCount += 2;
		if (cmdCount == 44) cmdCount -= 1;
		inv.setItem(cmdCount, cmd);

		Bukkit.getScheduler().runTaskLater(BukkitMain.get(), () -> p.openInventory(inv), 1);

	}
	
}
