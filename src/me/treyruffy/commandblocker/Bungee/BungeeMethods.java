package me.treyruffy.commandblocker.Bungee;

import java.io.File;

import me.treyruffy.commandblocker.MethodInterface;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;

public class BungeeMethods implements MethodInterface {

	private final File opDisabledFile = new File(getDataFolder(), "opdisabled.yml");
	private final File disabledFile = new File(getDataFolder(), "disabled.yml");
	private final File configFile = new File(getDataFolder(), "config.yml");
	
	private Configuration config;
	
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
		Metrics metrics = new Metrics((Plugin) getPlugin());
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

}
