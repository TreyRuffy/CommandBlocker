package me.treyruffy.commandblocker.bukkit.listeners;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;

import me.treyruffy.commandblocker.bukkit.BukkitMain;
import me.treyruffy.commandblocker.bukkit.config.ConfigManager;
import me.treyruffy.commandblockerlegacy.OldConfigManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class Packets {

	public static void protocol(BukkitMain plugin) {
		ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
		disableTabComplete(protocolManager, plugin);
		disableOpTabComplete(protocolManager, plugin);
	}
	
	public static void disableTabComplete(ProtocolManager protocolManager, Plugin plugin) {
		protocolManager.addPacketListener(new PacketAdapter(plugin, ListenerPriority.HIGH, PacketType.Play.Client.TAB_COMPLETE) {
			public void onPacketReceiving(PacketEvent e) {
				try {
					if (!e.getPacketType().equals(PacketType.Play.Client.TAB_COMPLETE)) {
						return;
					}
					FileConfiguration config = BukkitMain.oldConfig() ? OldConfigManager.getConfig() : ConfigManager.getConfig();
					if (!config.getBoolean("DisableTabComplete")) {
						return;
					}
					Player p = e.getPlayer();
					if (p.hasPermission("cb.bypasstab")) {
						return;
					}
					
					PacketContainer packet = e.getPacket();
					String message = packet.getSpecificModifier(String.class).read(0).toLowerCase();
					FileConfiguration disabled = BukkitMain.oldConfig() ? OldConfigManager.getDisabled() : ConfigManager.getDisabled();
					for (String cmd : disabled.getConfigurationSection("DisabledCommands").getKeys(false)) {
						String cmds = cmd.replace("%colon%", ":").toLowerCase();
						String permission = cmd.replace("%colon%", "").replace(" ", "");

						boolean b =	((message.startsWith("/" + cmds)) && (!message.contains("  "))) || ((message.startsWith("/") && (!message.contains(" "))));
						if (disabled.getString("DisabledCommands." + cmd + ".Permission") == null) {
							if (!(e.getPlayer().hasPermission(config.getString("Default.Permission").replace("%command%", permission)))) {
								if (disabled.getString("DisabledCommands." + cmd + ".NoTabComplete") == null) {
									if (b) {
										e.setCancelled(true);
									}
									
								} else if (disabled.getBoolean("DisabledCommands." + cmd + ".NoTabComplete")) {
									if (b) {
										e.setCancelled(true);
									}
								}
							}
							
						} else {
							if (!(e.getPlayer().hasPermission(disabled.getString("DisabledCommands." + cmd + ".Permission")))) {
								if (disabled.getString("DisabledCommands." + cmd + ".NoTabComplete") == null) {
									if (b) {
										e.setCancelled(true);
									}
									disabled.set("DisabledCommands." + cmd + ".NoTabComplete", true);
									if (!BukkitMain.oldConfig()) {
										ConfigManager.saveDisabled();
									} else {
										OldConfigManager.saveDisabled();
									}
								} else if (disabled.getBoolean("DisabledCommands." + cmd + ".NoTabComplete")) {
									if (b) {
										e.setCancelled(true);
									}
								}
							}
						}
						
					}
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		});
	}
	
	
	public static void disableOpTabComplete(ProtocolManager protocolManager, Plugin plugin) {
		protocolManager.addPacketListener(new PacketAdapter(plugin, ListenerPriority.HIGH, PacketType.Play.Client.TAB_COMPLETE) {
			public void onPacketReceiving(PacketEvent e) {
				try {
					if (!e.getPacketType().equals(PacketType.Play.Client.TAB_COMPLETE)) {
						return;
					}
					FileConfiguration config = BukkitMain.oldConfig() ? OldConfigManager.getConfig() : ConfigManager.getConfig();
					if (!config.getBoolean("DisableOpTabComplete")) {
						return;
					}
					Player p = e.getPlayer();
					if (!p.isOp()) {
						return;
					}
					
					PacketContainer packet = e.getPacket();
					String message = packet.getSpecificModifier(String.class).read(0).toLowerCase();
					FileConfiguration opDisabled = BukkitMain.oldConfig() ? OldConfigManager.getOpDisabled() : ConfigManager.getOpDisabled();
					for (String cmd : opDisabled.getConfigurationSection("DisabledOpCommands").getKeys(false)) {
						String cmds = cmd.toLowerCase();

						boolean b =	((message.startsWith("/" + cmds)) && (!message.contains("  "))) || ((message.startsWith("/") && (!message.contains(" "))));
						if (opDisabled.getString("DisabledOpCommands." + cmd + ".NoTabComplete") == null) {
							if (b) {
								e.setCancelled(true);
							}
							opDisabled.set("DisabledOpCommands." + cmd + ".NoTabComplete", true);
							if (!BukkitMain.oldConfig()) {
								ConfigManager.saveOpDisabled();
							} else {
								OldConfigManager.saveOpDisabled();
							}
							
						} else if (opDisabled.getBoolean("DisabledOpCommands." + cmd + ".NoTabComplete")) {
							if (b) {
								e.setCancelled(true);
							}
						}
						
					}
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		});
	}
}
