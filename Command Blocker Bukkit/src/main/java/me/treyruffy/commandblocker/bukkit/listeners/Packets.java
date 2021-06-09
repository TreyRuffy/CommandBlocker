package me.treyruffy.commandblocker.bukkit.listeners;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import me.treyruffy.commandblocker.MethodInterface;
import me.treyruffy.commandblocker.Universal;
import me.treyruffy.commandblocker.bukkit.BukkitMain;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Objects;

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
					MethodInterface mi = Universal.get().getMethods();
					FileConfiguration config = (FileConfiguration) mi.getConfig();
					if (!config.getBoolean("DisableTabComplete")) {
						return;
					}
					Player p = e.getPlayer();
					if (p.hasPermission("cb.bypasstab")) {
						return;
					}
					
					PacketContainer packet = e.getPacket();
					String message = packet.getSpecificModifier(String.class).read(0).toLowerCase();
					FileConfiguration disabled = (FileConfiguration) mi.getDisabledCommandsConfig();
					for (String cmd : Objects.requireNonNull(disabled.getConfigurationSection("DisabledCommands")).getKeys(false)) {
						String cmds = cmd.replace("%colon%", ":").toLowerCase();
						String permission = cmd.replace("%colon%", "").replace(" ", "");

						boolean b =	((message.startsWith("/" + cmds)) && (!message.contains("  "))) || ((message.startsWith("/") && (!message.contains(" "))));
						if (disabled.getString("DisabledCommands." + cmd + ".Permission") == null) {
							if (!(p.hasPermission(Objects.requireNonNull(config.getString("Default.Permission")).replace("%command%",
									permission)))) {
								if (disabled.getString("DisabledCommands." + cmd + ".NoTabComplete") == null) {
									if (!(disabled.getStringList("DisabledCommands." + cmd + ".Worlds").contains("all"))) {
										if (!disabled.getStringList("DisabledCommands." + cmd + ".Worlds").contains(p.getWorld().getName())) {
											return;
										}
									}
									if (b) {
										e.setCancelled(true);
									}
									
								} else if (disabled.getBoolean("DisabledCommands." + cmd + ".NoTabComplete")) {
									if (!(disabled.getStringList("DisabledCommands." + cmd + ".Worlds").contains("all"))) {
										if (!disabled.getStringList("DisabledCommands." + cmd + ".Worlds").contains(p.getWorld().getName())) {
											return;
										}
									}
									if (b) {
										e.setCancelled(true);
									}
								}
							}
							
						} else {
							if (!(p.hasPermission(Objects.requireNonNull(disabled.getString("DisabledCommands." + cmd + ".Permission"))))) {
								if (disabled.getString("DisabledCommands." + cmd + ".NoTabComplete") == null) {
									if (!(disabled.getStringList("DisabledCommands." + cmd + ".Worlds").contains("all"))) {
										if (!disabled.getStringList("DisabledCommands." + cmd + ".Worlds").contains(p.getWorld().getName())) {
											return;
										}
									}
									if (b) {
										e.setCancelled(true);
									}
									disabled.set("DisabledCommands." + cmd + ".NoTabComplete", true);
									mi.saveDisabledCommandsConfig();
								} else if (disabled.getBoolean("DisabledCommands." + cmd + ".NoTabComplete")) {
									if (!(disabled.getStringList("DisabledCommands." + cmd + ".Worlds").contains("all"))) {
										if (!disabled.getStringList("DisabledCommands." + cmd + ".Worlds").contains(p.getWorld().getName())) {
											return;
										}
									}
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
					MethodInterface mi = Universal.get().getMethods();
					FileConfiguration config = (FileConfiguration) mi.getConfig();
					if (!config.getBoolean("DisableOpTabComplete")) {
						return;
					}
					Player p = e.getPlayer();
					if (!p.isOp()) {
						return;
					}

					PacketContainer packet = e.getPacket();
					String message = packet.getSpecificModifier(String.class).read(0).toLowerCase();
					FileConfiguration opDisabled = (FileConfiguration) mi.getOpBlockConfig();
					if (opDisabled.getStringList("DisabledOpCommands").isEmpty()) {
						return;
					}
					for (String cmd : Objects.requireNonNull(opDisabled.getConfigurationSection("DisabledOpCommands")).getKeys(false)) {
						String cmds = cmd.toLowerCase();

						boolean b =
								((message.startsWith("/" + cmds)) && (!message.contains("  "))) || ((message.startsWith("/") && (!message.contains(" "))));
						if (opDisabled.getString("DisabledOpCommands." + cmd + ".NoTabComplete") == null) {
							if (!(opDisabled.getStringList("DisabledOpCommands." + cmd + ".Worlds").contains("all"))) {
								if (!opDisabled.getStringList("DisabledOpCommands." + cmd + ".Worlds").contains(p.getWorld().getName())) {
									return;
								}
							}
							if (b) {
								e.setCancelled(true);
							}
							opDisabled.set("DisabledOpCommands." + cmd + ".NoTabComplete", true);
							mi.saveOpBlockConfig();

						} else if (opDisabled.getBoolean("DisabledOpCommands." + cmd + ".NoTabComplete")) {
							if (!(opDisabled.getStringList("DisabledOpCommands." + cmd + ".Worlds").contains("all"))) {
								if (!opDisabled.getStringList("DisabledOpCommands." + cmd + ".Worlds").contains(p.getWorld().getName())) {
									return;
								}
							}
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
