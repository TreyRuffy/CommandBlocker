package me.treyruffy.commandblocker.bungeecord;

import java.io.File;

import org.bstats.bungeecord.Metrics;

import me.treyruffy.commandblocker.MethodInterface;
import me.treyruffy.commandblocker.bungeecord.api.BlockedCommands;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Plugin;

public class BungeeMethods implements MethodInterface {

	@Override
	public String getVersion() {
		return ((Plugin) getPlugin()).getDescription().getVersion();
	}

	@Override
	public File getDataFolder() {
		return ((Plugin) getPlugin()).getDataFolder();
	}

	@Override
	public void executeCommand(String cmd) {
		ProxyServer.getInstance().getPluginManager().dispatchCommand(ProxyServer.getInstance().getConsole(), cmd);
	}

	@Override
	public void setupMetrics() {
		int pluginId = 1851;
		Metrics metrics = new Metrics(ProxyServer.getInstance().getPluginManager().getPlugin("TreysCommandBlocker"), pluginId);
		metrics.addCustomChart(new Metrics.SimplePie("blockedCommandsCount", () -> BlockedCommands.getBlockedCommands().size() + ""));
	}

	@Override
	public void sendMessage(Object player, String msg) {
		((CommandSender) player).sendMessage(new TextComponent(msg));
	}

	@Override
	public Object getPlugin() {
		return BungeeMain.get();
	}

	@Override
	public void log(String msg) {
		ProxyServer.getInstance().getConsole().sendMessage(TextComponent.fromLegacyText(msg.replaceAll("&", "§")));
	}

	@Override
	public String getServerType() {
		return "BungeeCord";
	}

}
