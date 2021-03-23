package me.treyruffy.commandblocker;

import net.kyori.adventure.text.Component;

import java.io.File;
import java.util.List;

public interface MethodInterface {

	Object getPlugin();

	Object getConfig();
	Object getDisabledCommandsConfig();
	Object getMessagesConfig();
	Object getOpBlockConfig();

	void saveConfig();
	void saveDisabledCommandsConfig();
	void saveMessagesConfig();
	void saveOpBlockConfig();

	File getConfigFile(Object configurationFile);

	String getVersion();
	
	String getServerType();
	
	File getDataFolder();
	
    void setupMetrics();

	void executeCommand(String cmd);

    void sendMessage(Object commandSender, Component message);

    List<String> getOldMessages(String category, String message);
    List<String> getOldMessages(String category, String message, Object configurationFile);

    Character getChatComponentChar();

    String getOldMessage(String category, String message);
    String getOldMessage(String category, String message, Object configurationFile);
	
    void log(String msg);
}
