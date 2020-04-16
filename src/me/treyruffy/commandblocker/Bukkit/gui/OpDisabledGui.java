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

public class OpDisabledGui implements Listener {

	
	private HashMap<String, String> lookingFor = new HashMap<>();
	private HashMap<String, String> command = new HashMap<>();

	@SuppressWarnings("deprecation")
	public void openOpGui(Player p, String args) {
		if (!p.hasPermission("cb.editop")) {
			return;
		}
		Inventory inv = Bukkit.createInventory(null, 18, ChatColor.GREEN + ChatColor.BOLD.toString() + "Edit Op Command");
		ItemStack commandSlot = new ItemStack(Material.BOOK, 1);
		String whiteWool = new LegacyBlocks().getBlock("WHITE_WOOL");
		int whiteWoolId = 0;
		ItemStack firstSlot = new ItemStack(Material.valueOf(whiteWool), 1, (byte) whiteWoolId);
		ItemStack secondSlot = new ItemStack(Material.valueOf(whiteWool), 1, (byte) whiteWoolId);
		ItemStack thirdSlot = new ItemStack(Material.valueOf(whiteWool), 1, (byte) whiteWoolId);
		ItemStack fourthSlot = new ItemStack(Material.valueOf(whiteWool), 1, (byte) whiteWoolId);
		ItemStack fifthSlot = new ItemStack(Material.valueOf(whiteWool), 1, (byte) whiteWoolId);
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
		String greenWool = new LegacyBlocks().getBlock("GREEN_WOOL");
		int greenWoolId = 0;
		if (greenWool.equalsIgnoreCase("wool")) {
			greenWoolId = new LegacyBlocks().getLegacyId("GREEN");
		}
		if (!opDisabled.getBoolean("DisabledOpCommands." + args + ".NoTabComplete")) {
			sixthSlot = new ItemStack(Material.valueOf(redWool), 1, (byte) redWoolId);
		} else {
			sixthSlot = new ItemStack(Material.valueOf(greenWool), 1, (byte) greenWoolId);
		}
		
		ItemMeta commandSlotMeta = commandSlot.getItemMeta();
		commandSlotMeta.setDisplayName(ChatColor.RED + ChatColor.BOLD.toString() + "/" + args);
		commandSlot.setItemMeta(commandSlotMeta);
		inv.setItem(4, commandSlot);
		
		
		
		ItemMeta secondSlotMeta = secondSlot.getItemMeta();
		secondSlotMeta.setDisplayName("Message");
		secondSlot.setItemMeta(secondSlotMeta);
		inv.setItem(11, secondSlot);
		
		
		ItemMeta thirdSlotMeta = thirdSlot.getItemMeta();
		thirdSlotMeta.setDisplayName("Worlds");
		thirdSlot.setItemMeta(thirdSlotMeta);
		inv.setItem(12, thirdSlot);
		
		
		ItemMeta fourthSlotMeta = fourthSlot.getItemMeta();
		fourthSlotMeta.setDisplayName("Player Commands");
		fourthSlot.setItemMeta(fourthSlotMeta);
		inv.setItem(13, fourthSlot);
		
		
		ItemMeta fifthSlotMeta = fifthSlot.getItemMeta();
		fifthSlotMeta.setDisplayName("Console Commands");
		fifthSlot.setItemMeta(fifthSlotMeta);
		inv.setItem(14, fifthSlot);
		
		
		ItemMeta sixthSlotMeta = sixthSlot.getItemMeta();
		sixthSlotMeta.setDisplayName("No Tab Completion");
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
		if (e.getView().getTitle().equals(ChatColor.GREEN + ChatColor.BOLD.toString() + "Edit Op Command")) {
			String cmd = ChatColor.stripColor(e.getInventory().getItem(4).getItemMeta().getDisplayName()).substring(1);
			Player p = (Player) e.getWhoClicked();
			if (ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase("message")) {
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
				if (!opDisabled.getBoolean("DisabledOpCommands." + cmd + ".NoTabComplete")) {
					opDisabled.set("DisabledOpCommands." + cmd + ".NoTabComplete", true);
					Log.addLog(Universal.get().getMethods(), p.getName() + ": Changed tab completion for /" + cmd + " from off to on in opblock.yml");
					if (!BukkitMain.oldConfig()) {
						ConfigManager.saveOpDisabled();
					} else {
						OldConfigManager.saveOpDisabled();
					}
				} else {
					opDisabled.set("DisabledOpCommands." + cmd + ".NoTabComplete", false);
					Log.addLog(Universal.get().getMethods(), p.getName() + ": Changed tab completion for /" + cmd + " from on to off in opblock.yml");
					if (!BukkitMain.oldConfig()) {
						ConfigManager.saveOpDisabled();
					} else {
						OldConfigManager.saveOpDisabled();
					}
				}
				
				openOpGui(p, cmd);
				
			}
			e.setCancelled(true);
		} else if (e.getView().getTitle().equals(ChatColor.GREEN + ChatColor.BOLD.toString() + "Edit Op Worlds")) {
			String cmd = ChatColor.stripColor(e.getInventory().getItem(4).getItemMeta().getDisplayName()).substring(1);
			Player p = (Player) e.getWhoClicked();
			if (ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase("go back")) {
				openOpGui(p, cmd);
			} else if (ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase("add world")) {
				command.put(p.getUniqueId().toString(), cmd);
				lookingFor.put(p.getUniqueId().toString(), "world");
				p.sendMessage("add world");
				p.closeInventory();
			} else if (!(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase("/" + cmd))) {
				
				FileConfiguration disabled = BukkitMain.oldConfig() ? OldConfigManager.OpDisabled : ConfigManager.OpDisabled;
				if (disabled.getStringList("DisabledOpCommands." + cmd + ".Worlds").indexOf("all") != -1) {
					List<String> worlds = new ArrayList<String>();
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
		} else if (e.getView().getTitle().equals(ChatColor.GREEN + ChatColor.BOLD.toString() + "Edit Op Player Commands")) {
			String cmd = ChatColor.stripColor(e.getInventory().getItem(4).getItemMeta().getDisplayName()).substring(1);
			Player p = (Player) e.getWhoClicked();
			if (ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase("go back")) {
				openOpGui(p, cmd);
			} else if (ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase("add player command")) {
				command.put(p.getUniqueId().toString(), cmd);
				lookingFor.put(p.getUniqueId().toString(), "playercommand");
				p.sendMessage("add player command");
				p.closeInventory();
			} else if (!(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase("/" + cmd))) {
				
				FileConfiguration disabled = BukkitMain.oldConfig() ? OldConfigManager.OpDisabled : ConfigManager.OpDisabled;
				List<String> newPCommandList = disabled.getStringList("DisabledOpCommands." + cmd + ".PlayerCommands");
				if (!ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase("none")) {
					newPCommandList.remove(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).substring(1));
					Log.addLog(Universal.get().getMethods(), p.getName() + ": Removed player command " + e.getCurrentItem().getItemMeta().getDisplayName() + " from /" + cmd + " in opblock.yml");
				} else {
					newPCommandList.remove(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()));
					Log.addLog(Universal.get().getMethods(), p.getName() + ": Removed none tag for player commands from /" + cmd + " in opblock.yml");
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
		} else if (e.getView().getTitle().equals(ChatColor.GREEN + ChatColor.BOLD.toString() + "Edit Op Console Commands")) {
			String cmd = ChatColor.stripColor(e.getInventory().getItem(4).getItemMeta().getDisplayName()).substring(1);
			Player p = (Player) e.getWhoClicked();
			if (ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase("go back")) {
				openOpGui(p, cmd);
			} else if (ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase("add console command")) {
				command.put(p.getUniqueId().toString(), cmd);
				lookingFor.put(p.getUniqueId().toString(), "consolecommand");
				p.sendMessage("add console command");
				p.closeInventory();
			} else if (!(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase("/" + cmd))) {
				
				FileConfiguration disabled = BukkitMain.oldConfig() ? OldConfigManager.OpDisabled : ConfigManager.OpDisabled;
				List<String> newPCommandList = disabled.getStringList("DisabledOpCommands." + cmd + ".ConsoleCommands");
				if (!ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase("none")) {
					newPCommandList.remove(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).substring(1));
					Log.addLog(Universal.get().getMethods(), p.getName() + ": Removed console command " + e.getCurrentItem().getItemMeta().getDisplayName() + " from /" + cmd + " in opblock.yml");
				} else {
					newPCommandList.remove(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()));
					Log.addLog(Universal.get().getMethods(), p.getName() + ": Removed none tag for console commands from /" + cmd + " in opblock.yml");
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
			p.sendMessage("cancelled");
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
			p.sendMessage("cant use command for this");
		} else if (lookingFor.get(p.getUniqueId().toString()).equalsIgnoreCase("message")) {
			p.sendMessage("cant use command for this");
		} else if (lookingFor.get(p.getUniqueId().toString()).equalsIgnoreCase("world")) {
			p.sendMessage("cant use command for this");
		} else if (lookingFor.get(p.getUniqueId().toString()).equalsIgnoreCase("playercommand")) {
			Log.addLog(Universal.get().getMethods(), p.getName() + ": Added player command " + e.getMessage() + " for /" + command.get(p.getUniqueId().toString()) + " in opblock.yml");
			FileConfiguration disabled = BukkitMain.oldConfig() ? OldConfigManager.OpDisabled : ConfigManager.OpDisabled;
			List<String> pCmdList = disabled.getStringList("DisabledOpCommands." + command.get(p.getUniqueId().toString()) + ".PlayerCommands");
			pCmdList.add(e.getMessage().substring(1));
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
		
		Inventory inv = Bukkit.createInventory(null, 54, ChatColor.GREEN + ChatColor.BOLD.toString() + "Edit Op Worlds");
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
		
		int worldCount = 0;
		for (World w : Bukkit.getWorlds()) {
			if (worldCount >= 45) {
				break;
			}
			
			if ((disabled.getStringList("DisabledOpCommands." + args + ".Worlds").indexOf(w.getName()) != -1) || (disabled.getStringList("DisabledOpCommands." + args + ".Worlds").indexOf("all") != -1)) {
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
					worldMeta.setDisplayName(s);
					world.setItemMeta(worldMeta);
					inv.setItem(worldCount + 9, world);
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
		worldMeta.setDisplayName("Add World");
		world.setItemMeta(worldMeta);
		inv.setItem(worldCount + 9, world);
		
		Bukkit.getScheduler().runTaskLater(BukkitMain.get(), () -> p.openInventory(inv), 1);
		
	}
	
	public void openPCommandsGui(Player p, String args) {
		if (!p.hasPermission("cb.editop")) {
			return;
		}
		FileConfiguration disabled = BukkitMain.oldConfig() ? OldConfigManager.OpDisabled : ConfigManager.OpDisabled;
		
		Inventory inv = Bukkit.createInventory(null, 54, ChatColor.GREEN + ChatColor.BOLD.toString() + "Edit Op Player Commands");
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
		if (!disabled.getStringList("DisabledOpCommands." + args + ".PlayerCommands").contains("none")) {
			for (String s : disabled.getStringList("DisabledOpCommands." + args + ".PlayerCommands")) {
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
			for (String s : disabled.getStringList("DisabledOpCommands." + args + ".PlayerCommands")) {
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
	
	public void openCCommandsGui(Player p, String args) {
		if (!p.hasPermission("cb.editop")) {
			return;
		}
		FileConfiguration disabled = BukkitMain.oldConfig() ? OldConfigManager.OpDisabled : ConfigManager.OpDisabled;
		
		Inventory inv = Bukkit.createInventory(null, 54, ChatColor.GREEN + ChatColor.BOLD.toString() + "Edit Op Console Commands");
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
		if (!disabled.getStringList("DisabledOpCommands." + args + ".ConsoleCommands").contains("none")) {
			for (String s : disabled.getStringList("DisabledOpCommands." + args + ".ConsoleCommands")) {
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
			for (String s : disabled.getStringList("DisabledOpCommands." + args + ".ConsoleCommands")) {
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
