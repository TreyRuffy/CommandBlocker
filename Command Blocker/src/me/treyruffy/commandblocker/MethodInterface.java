package me.treyruffy.commandblocker;

import java.io.File;

public interface MethodInterface {

	Object getPlugin();
	
	String getVersion();
	
	String getServerType();
	
	File getDataFolder();
	
    void setupMetrics();

	void executeCommand(String cmd);

    void sendMessage(Object player, String msg);
	
    void log(String msg);
}
