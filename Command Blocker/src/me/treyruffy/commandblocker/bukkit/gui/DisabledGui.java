package me.treyruffy.commandblocker.bukkit.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
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

	
	private HashMap<String, String> lookingFor = new HashMap<>();
	private HashMap<String, String> command = new HashMap<>();

	@SuppressWarnings("deprecation")
	public void openGui(Player p, String args) {
		if (!p.hasPermission("cb.edit")) {
			return;
		}
		Inventory inv = Bukkit.createInventory(null, 18, ChatColor.GREEN + ChatColor.BOLD.toString() + "Edit Command");
		ItemStack commandSlot = new ItemStack(Material.BOOK, 1);
		String whiteWool = new LegacyBlocks().getBlock("WHITE_WOOL");
		int whiteWoolId = 0;
		ItemStack firstSlot = new ItemStack(Material.valueOf(whiteWool), 1, (byte) whiteWoolId);
		ItemStack secondSlot = new ItemStack(Material.valueOf(whiteWool), 1, (byte) whiteWoolId);
		ItemStack thirdSlot = new ItemStack(Material.valueOf(whiteWool), 1, (byte) whiteWoolId);
		ItemStack fourthSlot = new ItemStack(Material.valueOf(whiteWool), 1, (byte) whiteWoolId);
		ItemStack fifthSlot = new ItemStack(Material.valueOf(whiteWool), 1, (byte) whiteWoolId);
		ItemStack sixthSlot;
		String redWool = new LegacyBlocks().getBlock("RED_WOOL");
		int redWoolId = 0;
		if (redWool.equalsIgnoreCase("wool")) {
			redWoolId = new LegacyBlocks().getLegacyId("RED");
		}
		String greenWool = new LegacyBlocks().getBlock("GREEN_WOOL");
		int greenWoolId = 0;
		if (greenWool.equalsIgnoreCase("wool")) {
			greenWoolId = new LegacyBlocks().getLegacyId("GREEN");
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
		commandSlotMeta.setDisplayName(ChatColor.RED + ChatColor.BOLD.toString() + "/" + args);
		commandSlot.setItemMeta(commandSlotMeta);
		inv.setItem(4, commandSlot);
		
		
		ItemMeta firstSlotMeta = firstSlot.getItemMeta();
		firstSlotMeta.setDisplayName("Permission");
		List<String> firstSlotLore = new ArrayList<>();
		firstSlotLore.add(ChatColor.GOLD + "Currently:");
		String currentPermission = ChatColor.WHITE + (BukkitMain.oldConfig() ? OldConfigManager.MainDisabled.getString("DisabledCommands." + args + ".Permission") : ConfigManager.MainDisabled.getString("DisabledCommands." + args + ".Permission"));
		firstSlotLore.add(currentPermission);
		firstSlotMeta.setLore(firstSlotLore);
		firstSlot.setItemMeta(firstSlotMeta);
		inv.setItem(11, firstSlot);
		
		
		ItemMeta secondSlotMeta = secondSlot.getItemMeta();
		secondSlotMeta.setDisplayName("Message");
		List<String> secondSlotLore = new ArrayList<>();
		secondSlotLore.add(ChatColor.GOLD + "Currently:");
		String currentMessage = ChatColor.WHITE + ChatColor.translateAlternateColorCodes('&', BukkitMain.oldConfig() ? OldConfigManager.MainDisabled.getString("DisabledCommands." + args + ".Message") : ConfigManager.MainDisabled.getString("DisabledCommands." + args + ".Message"));
		secondSlotLore.add(currentMessage);
		secondSlotMeta.setLore(secondSlotLore);
		secondSlot.setItemMeta(secondSlotMeta);
		inv.setItem(12, secondSlot);
		
		
		ItemMeta thirdSlotMeta = thirdSlot.getItemMeta();
		thirdSlotMeta.setDisplayName("Worlds");
		thirdSlot.setItemMeta(thirdSlotMeta);
		inv.setItem(13, thirdSlot);
		
		
		ItemMeta fourthSlotMeta = fourthSlot.getItemMeta();
		fourthSlotMeta.setDisplayName("Player Commands");
		fourthSlot.setItemMeta(fourthSlotMeta);
		inv.setItem(14, fourthSlot);
		
		
		ItemMeta fifthSlotMeta = fifthSlot.getItemMeta();
		fifthSlotMeta.setDisplayName("Console Commands");
		fifthSlot.setItemMeta(fifthSlotMeta);
		inv.setItem(15, fifthSlot);
		
		
		ItemMeta sixthSlotMeta = sixthSlot.getItemMeta();
		sixthSlotMeta.setDisplayName("No Tab Completion");
		sixthSlot.setItemMeta(sixthSlotMeta);
		inv.setItem(16, sixthSlot);
		
		
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
		if (e.getView().getTitle().equals(ChatColor.GREEN + ChatColor.BOLD.toString() + "Edit Command")) {
			String cmd = ChatColor.stripColor(e.getInventory().getItem(4).getItemMeta().getDisplayName()).substring(1);
			Player p = (Player) e.getWhoClicked();
			if (ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase("permission")) {
				p.closeInventory();
				
				p.sendMessage("change permission");
				lookingFor.put(p.getUniqueId().toString(), "permission");
				command.put(p.getUniqueId().toString(), cmd);
			} else if (ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase("message")) {
				p.closeInventory();
				
				p.sendMessage("change message");
				lookingFor.put(p.getUniqueId().toString(), "message");
				command.put(p.getUniqueId().toString(), cmd);
			} else if (ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase("worlds")) {
				openWorldGui(p, cmd);
				
			} else if (ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase("player commands")) {
				openPCommandsGui(p, cmd);
				
			} else if (ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase("console commands")) {
				openCCommandsGui(p, cmd);
				
			} else if (ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase("no tab completion")) {
				if (!BukkitMain.oldConfig()) {
					if (!ConfigManager.MainDisabled.getBoolean("DisabledCommands." + cmd + ".NoTabComplete")) {
						ConfigManager.MainDisabled.set("DisabledCommands." + cmd + ".NoTabComplete", true);
						ConfigManager.saveDisabled();
						Log.addLog(Universal.get().getMethods(), p.getName() + ": Changed tab completion for /" + cmd + " from off to on in disabled.yml");
					} else {
						ConfigManager.MainDisabled.set("DisabledCommands." + cmd + ".NoTabComplete", false);
						ConfigManager.saveDisabled();
						Log.addLog(Universal.get().getMethods(), p.getName() + ": Changed tab completion for /" + cmd + " from on to off in disabled.yml");
					}
				} else {
					if (!OldConfigManager.MainDisabled.getBoolean("DisabledCommands." + cmd + ".NoTabComplete")) {
						OldConfigManager.MainDisabled.set("DisabledCommands." + cmd + ".NoTabComplete", true);
						OldConfigManager.saveDisabled();
						Log.addLog(Universal.get().getMethods(), p.getName() + ": Changed tab completion for /" + cmd + " from off to on in disabled.yml");
					} else {
						OldConfigManager.MainDisabled.set("DisabledCommands." + cmd + ".NoTabComplete", false);
						OldConfigManager.saveDisabled();
						Log.addLog(Universal.get().getMethods(), p.getName() + ": Changed tab completion for /" + cmd + " from on to off in disabled.yml");
					}
				}
				
				openGui(p, cmd);
				
			}
			e.setCancelled(true);
		} else if (e.getView().getTitle().equals(ChatColor.GREEN + ChatColor.BOLD.toString() + "Edit Worlds")) {
			String cmd = ChatColor.stripColor(e.getInventory().getItem(4).getItemMeta().getDisplayName()).substring(1);
			Player p = (Player) e.getWhoClicked();
			if (ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase("go back")) {
				openGui(p, cmd);
			} else if (ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase("add world")) {
				command.put(p.getUniqueId().toString(), cmd);
				lookingFor.put(p.getUniqueId().toString(), "world");
				p.sendMessage("add world");
				p.closeInventory();
			} else if (!(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase("/" + cmd))) {
				
				FileConfiguration disabled = BukkitMain.oldConfig() ? OldConfigManager.MainDisabled : ConfigManager.MainDisabled;
				if (disabled.getStringList("DisabledCommands." + cmd + ".Worlds").indexOf("all") != -1) {
					List<String> worlds = new ArrayList<String>();
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
		} else if (e.getView().getTitle().equals(ChatColor.GREEN + ChatColor.BOLD.toString() + "Edit Player Commands")) {
			String cmd = ChatColor.stripColor(e.getInventory().getItem(4).getItemMeta().getDisplayName()).substring(1);
			Player p = (Player) e.getWhoClicked();
			if (ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase("go back")) {
				openGui(p, cmd);
			} else if (ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase("add player command")) {
				command.put(p.getUniqueId().toString(), cmd);
				lookingFor.put(p.getUniqueId().toString(), "playercommand");
				p.sendMessage("add player command");
				p.closeInventory();
			} else if (!(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase("/" + cmd))) {
				
				FileConfiguration disabled = BukkitMain.oldConfig() ? OldConfigManager.MainDisabled : ConfigManager.MainDisabled;
				List<String> newPCommandList = disabled.getStringList("DisabledCommands." + cmd + ".PlayerCommands");
				if (!ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase("none")) {
					newPCommandList.remove(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).substring(1));
					Log.addLog(Universal.get().getMethods(), p.getName() + ": Removed player command " + e.getCurrentItem().getItemMeta().getDisplayName() + " from /" + cmd + " in disabled.yml");
				} else {
					newPCommandList.remove(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()));
					Log.addLog(Universal.get().getMethods(), p.getName() + ": Removed none tag from player commands for /" + cmd + " in disabled.yml");
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
		} else if (e.getView().getTitle().equals(ChatColor.GREEN + ChatColor.BOLD.toString() + "Edit Console Commands")) {
			String cmd = ChatColor.stripColor(e.getInventory().getItem(4).getItemMeta().getDisplayName()).substring(1);
			Player p = (Player) e.getWhoClicked();
			if (ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase("go back")) {
				openGui(p, cmd);
			} else if (ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase("add console command")) {
				command.put(p.getUniqueId().toString(), cmd);
				lookingFor.put(p.getUniqueId().toString(), "consolecommand");
				p.sendMessage("add console command");
				p.closeInventory();
			} else if (!(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase("/" + cmd))) {
				
				FileConfiguration disabled = BukkitMain.oldConfig() ? OldConfigManager.MainDisabled : ConfigManager.MainDisabled;
				List<String> newPCommandList = disabled.getStringList("DisabledCommands." + cmd + ".ConsoleCommands");
				if (!ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase("none")) {
					newPCommandList.remove(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).substring(1));
					Log.addLog(Universal.get().getMethods(), p.getName() + ": Removed console command " + e.getCurrentItem().getItemMeta().getDisplayName() + " from /" + cmd + " in disabled.yml");
				} else {
					newPCommandList.remove(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()));
					Log.addLog(Universal.get().getMethods(), p.getName() + ": Removed none tag from console commands for /" + cmd + " in disabled.yml");
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
			p.sendMessage("cancelled");
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
			p.sendMessage("cant use command for this");
		} else if (lookingFor.get(p.getUniqueId().toString()).equalsIgnoreCase("message")) {
			p.sendMessage("cant use command for this");
		} else if (lookingFor.get(p.getUniqueId().toString()).equalsIgnoreCase("world")) {
			p.sendMessage("cant use command for this");
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
		FileConfiguration disabled = BukkitMain.oldConfig() ? OldConfigManager.MainDisabled : ConfigManager.MainDisabled;
		
		Inventory inv = Bukkit.createInventory(null, 54, ChatColor.GREEN + ChatColor.BOLD.toString() + "Edit Worlds");
		ItemStack backSlot = new ItemStack(Material.ARROW, 1);
		ItemMeta backSlotMeta = backSlot.getItemMeta();
		backSlotMeta.setDisplayName("Go back");
		backSlot.setItemMeta(backSlotMeta);
		inv.setItem(0, backSlot);
		
		ItemStack commandSlot = new ItemStack(Material.BOOK, 1);
		ItemMeta commandSlotMeta = commandSlot.getItemMeta();
		commandSlotMeta.setDisplayName(ChatColor.RED + ChatColor.BOLD.toString() + "/" + args);
		commandSlot.setItemMeta(commandSlotMeta);
		inv.setItem(4, commandSlot);
		
		if (disabled.getString("DisabledCommands." + args + ".Worlds") == null) {
			List<String> list = new ArrayList<String>();
			list.add("all");
			disabled.set("DisabledCommands." + args + ".Worlds", list);
			if (!BukkitMain.oldConfig()) {
				ConfigManager.saveDisabled();
			} else {
				OldConfigManager.saveDisabled();
			}
		}
		
		int worldCount = 0;
		for (World w : Bukkit.getWorlds()) {
			if (worldCount >= 45) {
				break;
			}
			
			if ((disabled.getStringList("DisabledCommands." + args + ".Worlds").indexOf(w.getName()) != -1) || (disabled.getStringList("DisabledCommands." + args + ".Worlds").indexOf("all") != -1)) {
				String limeWool = new LegacyBlocks().getBlock("LIME_WOOL");
				int limeWoolId = 0;
				if (limeWool.equalsIgnoreCase("wool")) {
					limeWoolId = new LegacyBlocks().getLegacyId("LIME");
				}
				
				ItemStack world = new ItemStack(Material.valueOf(limeWool), 1, (byte) limeWoolId);
				ItemMeta worldMeta = world.getItemMeta();
				worldMeta.setDisplayName(w.getName());
				world.setItemMeta(worldMeta);
				inv.setItem(worldCount + 9, world);
				
			} else {
				String redWool = new LegacyBlocks().getBlock("RED_WOOL");
				int redWoolId = 0;
				if (redWool.equalsIgnoreCase("wool")) {
					redWoolId = new LegacyBlocks().getLegacyId("RED");
				}
				
				ItemStack world = new ItemStack(Material.valueOf(redWool), 1, (byte) redWoolId);
				ItemMeta worldMeta = world.getItemMeta();
				worldMeta.setDisplayName(w.getName());
				world.setItemMeta(worldMeta);
				inv.setItem(worldCount + 9, world);
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
					worldMeta.setDisplayName(s);
					world.setItemMeta(worldMeta);
					inv.setItem(worldCount + 9, world);
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
		worldMeta.setDisplayName("Add World");
		world.setItemMeta(worldMeta);
		inv.setItem(worldCount + 9, world);
		
		Bukkit.getScheduler().runTaskLater(BukkitMain.get(), () -> p.openInventory(inv), 1);
		
	}

	@SuppressWarnings("deprecation")
	public void openPCommandsGui(Player p, String args) {
		if (!p.hasPermission("cb.edit")) {
			return;
		}
		FileConfiguration disabled = BukkitMain.oldConfig() ? OldConfigManager.MainDisabled : ConfigManager.MainDisabled;
		
		Inventory inv = Bukkit.createInventory(null, 54, ChatColor.GREEN + ChatColor.BOLD.toString() + "Edit Player Commands");
		ItemStack backSlot = new ItemStack(Material.ARROW, 1);
		ItemMeta backSlotMeta = backSlot.getItemMeta();
		backSlotMeta.setDisplayName("Go back");
		backSlot.setItemMeta(backSlotMeta);
		inv.setItem(0, backSlot);
		
		ItemStack commandSlot = new ItemStack(Material.BOOK, 1);
		ItemMeta commandSlotMeta = commandSlot.getItemMeta();
		commandSlotMeta.setDisplayName(ChatColor.RED + ChatColor.BOLD.toString() + "/" + args);
		commandSlot.setItemMeta(commandSlotMeta);
		inv.setItem(4, commandSlot);
		
		int cmdCount = 0;
		if (!disabled.getStringList("DisabledCommands." + args + ".PlayerCommands").contains("none")) {
			for (String s : disabled.getStringList("DisabledCommands." + args + ".PlayerCommands")) {
				String limeWool = new LegacyBlocks().getBlock("LIME_WOOL");
				int limeWoolId = 0;
				if (limeWool.equalsIgnoreCase("wool")) {
					limeWoolId = new LegacyBlocks().getLegacyId("LIME");
				}
				
				ItemStack cmd = new ItemStack(Material.valueOf(limeWool), 1, (byte) limeWoolId);
				ItemMeta cmdMeta = cmd.getItemMeta();
				cmdMeta.setDisplayName("/" + s);
				cmd.setItemMeta(cmdMeta);
				inv.setItem(cmdCount + 9, cmd);
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
					inv.setItem(cmdCount + 9, cmd);
					cmdCount++;
				}
			}
		}
		String magentaWool = new LegacyBlocks().getBlock("MAGENTA_WOOL");
		int magentaWoolId = 0;
		if (magentaWool.equalsIgnoreCase("wool")) {
			magentaWoolId = new LegacyBlocks().getLegacyId("LIME");
		}
		
		ItemStack cmd = new ItemStack(Material.valueOf(magentaWool), 1, (byte) magentaWoolId);
		ItemMeta cmdMeta = cmd.getItemMeta();
		cmdMeta.setDisplayName("Add Player Command");
		cmd.setItemMeta(cmdMeta);
		inv.setItem(cmdCount + 9, cmd);
		
		Bukkit.getScheduler().runTaskLater(BukkitMain.get(), () -> p.openInventory(inv), 1);
		
	}

	@SuppressWarnings("deprecation")
	public void openCCommandsGui(Player p, String args) {
		if (!p.hasPermission("cb.edit")) {
			return;
		}
		FileConfiguration disabled = BukkitMain.oldConfig() ? OldConfigManager.MainDisabled : ConfigManager.MainDisabled;
		
		Inventory inv = Bukkit.createInventory(null, 54, ChatColor.GREEN + ChatColor.BOLD.toString() + "Edit Console Commands");
		ItemStack backSlot = new ItemStack(Material.ARROW, 1);
		ItemMeta backSlotMeta = backSlot.getItemMeta();
		backSlotMeta.setDisplayName("Go back");
		backSlot.setItemMeta(backSlotMeta);
		inv.setItem(0, backSlot);
		
		ItemStack commandSlot = new ItemStack(Material.BOOK, 1);
		ItemMeta commandSlotMeta = commandSlot.getItemMeta();
		commandSlotMeta.setDisplayName(ChatColor.RED + ChatColor.BOLD.toString() + "/" + args);
		commandSlot.setItemMeta(commandSlotMeta);
		inv.setItem(4, commandSlot);
		
		int cmdCount = 0;
		if (!disabled.getStringList("DisabledCommands." + args + ".ConsoleCommands").contains("none")) {
			for (String s : disabled.getStringList("DisabledCommands." + args + ".ConsoleCommands")) {
				String limeWool = new LegacyBlocks().getBlock("LIME_WOOL");
				int limeWoolId = 0;
				if (limeWool.equalsIgnoreCase("wool")) {
					limeWoolId = new LegacyBlocks().getLegacyId("LIME");
				}
				
				ItemStack cmd = new ItemStack(Material.valueOf(limeWool), 1, (byte) limeWoolId);
				ItemMeta cmdMeta = cmd.getItemMeta();
				cmdMeta.setDisplayName("/" + s);
				cmd.setItemMeta(cmdMeta);
				inv.setItem(cmdCount + 9, cmd);
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
					inv.setItem(cmdCount + 9, cmd);
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
		cmdMeta.setDisplayName("Add Console Command");
		cmd.setItemMeta(cmdMeta);
		inv.setItem(cmdCount + 9, cmd);
		
		Bukkit.getScheduler().runTaskLater(BukkitMain.get(), () -> p.openInventory(inv), 1);
		
	}
	
}
