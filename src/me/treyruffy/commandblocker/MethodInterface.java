package me.treyruffy.commandblocker;

import java.io.File;
import java.util.List;

public interface MethodInterface {

	Object getPlugin();
	
	String getVersion();
	
	File getDataFolder();
	
	void executeCommand(String cmd);
	
    void setupMetrics();
    
    void sendMessage(Object player, String msg);
	
    void log(String msg);
}
