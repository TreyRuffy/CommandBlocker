package me.treyruffy.commandblocker;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.plugin.Plugin;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;

public class Packets {

	public static void protocol(CommandBlocker plugin){
		ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
		tabComplete(protocolManager, plugin);
	}
	
	public static void tabComplete(ProtocolManager protocolManager, Plugin plugin){
		protocolManager.addPacketListener(new PacketAdapter(plugin, ListenerPriority.NORMAL, new PacketType[]{ PacketType.Play.Client.TAB_COMPLETE }){
			@EventHandler(priority = EventPriority.HIGHEST)
			public void onPacketReceiving(PacketEvent e){
				if (e.getPacketType() == PacketType.Play.Client.TAB_COMPLETE){
					try{
						if (ConfigManager.getConfig().getBoolean("DisableTabComplete"))
							if (e.getPlayer().hasPermission("cb.bypasstab")){
								return;
							}
							PacketContainer packet = e.getPacket();
							String message = ((String) packet.getSpecificModifier(String.class).read(0)).toLowerCase();
							for (String cmd : ConfigManager.getDisabled().getConfigurationSection("DisabledCommands").getKeys(false)){
								String cmds = cmd.replace("%colon%", ":").toLowerCase();
								String permission = cmd.replace("%colon%", "").replace(" ", "");
								if (ConfigManager.getDisabled().getString("DisabledCommands." + cmd + ".Permission") == null){
									if (!(e.getPlayer().hasPermission(ConfigManager.getConfig().getString("Default.Permission").replace("%command%", permission)))){
										if (ConfigManager.getDisabled().getString("DisabledCommands." + cmd + ".NoTabComplete") == null){
											if (((message.startsWith("/" + cmds)) && (!message.contains("  "))) || ((message.startsWith("/") && (!message.contains(" "))))){
												e.setCancelled(true);
											}
										} else if (ConfigManager.getDisabled().getBoolean("DisabledCommands." + cmd + ".NoTabComplete")){
											if (((message.startsWith("/" + cmds)) && (!message.contains("  "))) || ((message.startsWith("/") && (!message.contains(" "))))){
												e.setCancelled(true);
											}
										}
									}
								} else {
									if (!(e.getPlayer().hasPermission(ConfigManager.getDisabled().getString("DisabledCommands." + cmd + ".Permission")))){
										if (ConfigManager.getDisabled().getString("DisabledCommands." + cmd + ".NoTabComplete") == null){
											if (((message.startsWith("/" + cmds)) && (!message.contains("  "))) || ((message.startsWith("/") && (!message.contains(" "))))){
												e.setCancelled(true);
											}
										} else if (ConfigManager.getDisabled().getBoolean("DisabledCommands." + cmd + ".NoTabComplete")){
											if (((message.startsWith("/" + cmds)) && (!message.contains("  "))) || ((message.startsWith("/") && (!message.contains(" "))))){
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
			}
		});
	}
	
}
