package me.treyruffy.commandblocker;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;

import org.apache.commons.lang.NullArgumentException;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class ConfigManager {

	private static CommandBlocker plugin = CommandBlocker.getPlugin(CommandBlocker.class);
	
	public static FileConfiguration MainConfig;
	public static File MainConfigFile;
	
	public static FileConfiguration MainDisabled;
	public static File MainDisabledFile;
	
	public static FileConfiguration OpDisabled;
	public static File OpDisabledFile;
	
	public void setup(){
		if(!plugin.getDataFolder().exists()){
			plugin.getDataFolder().mkdir();
		}
		
		MainConfigFile = new File(plugin.getDataFolder(), "config.yml");
		MainDisabledFile = new File(plugin.getDataFolder(), "disabled.yml");
		OpDisabledFile = new File(plugin.getDataFolder(), "opblock.yml");
		
		if(!MainConfigFile.exists()){
			try{
				MainConfigFile.createNewFile();
			}catch(IOException e){
				Bukkit.getServer().getLogger().log(Level.SEVERE, "Could not save " + MainConfigFile + ".", e);
			}
		}
		if(!MainDisabledFile.exists()){
			try{
				MainDisabledFile.createNewFile();
			}catch(IOException e){
				Bukkit.getServer().getLogger().log(Level.SEVERE, "Could not save " + MainDisabledFile + ".", e);
			}
		}
		if (!OpDisabledFile.exists()){
			try{
				OpDisabledFile.createNewFile();
			}catch(IOException e){
				Bukkit.getServer().getLogger().log(Level.SEVERE, "Could not save " + OpDisabledFile + ".", e);
			}
		}
		
		MainConfig = YamlConfiguration.loadConfiguration(MainConfigFile);
		MainDisabled = YamlConfiguration.loadConfiguration(MainDisabledFile);
		OpDisabled = YamlConfiguration.loadConfiguration(OpDisabledFile);
	}
	
	public static FileConfiguration getConfig(){
		if (MainConfig == null){
			reloadConfig();
		}
		return MainConfig; 
	}
	public static FileConfiguration getDisabled(){
		if (MainDisabled == null){
			reloadDisabled();
		}
		return MainDisabled; 
	}
	public static FileConfiguration getOpDisabled(){
		if (OpDisabled == null){
			reloadOpDisabled();
		}
		return OpDisabled;
	}
	
	public static void saveConfig(){
		if (MainConfig == null){
			throw new NullArgumentException("Cannot save a non-existant file!");
		}
		try{
			MainConfig.save(MainConfigFile);
		}catch(IOException e){
			Bukkit.getServer().getLogger().log(Level.SEVERE, "Could not save " + MainConfigFile + ".", e);
		}
	}
	public static void saveDisabled(){
		if (MainDisabled == null){
			throw new NullArgumentException("Cannot save a non-existant file!");
		}
		try{
			MainDisabled.save(MainDisabledFile);
		}catch(IOException e){
			Bukkit.getServer().getLogger().log(Level.SEVERE, "Could not save " + MainDisabledFile + ".", e);
		}
	}
	public static void saveOpDisabled(){
		if (OpDisabled == null){
			throw new NullArgumentException("Cannot save a non-existant file!");
		}
		try{
			OpDisabled.save(OpDisabledFile);
		}catch(IOException e){
			Bukkit.getServer().getLogger().log(Level.SEVERE, "Could not save " + OpDisabledFile + ".", e);
		}
	}
	
	public static void reloadConfig(){
		MainConfigFile = new File(plugin.getDataFolder(), "config.yml");
		if (!MainConfigFile.exists()){
			plugin.saveResource("config.yml", false);
		}
		MainConfig = YamlConfiguration.loadConfiguration(MainConfigFile);
		InputStream configData = plugin.getResource("config.yml");
		if (configData != null){
			MainConfig.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(configData)));
		}
	}
	public static void reloadDisabled(){
		MainDisabledFile = new File(plugin.getDataFolder(), "disabled.yml");
		if (!MainDisabledFile.exists()){
			plugin.saveResource("disabled.yml", false);
		}
		MainDisabled = YamlConfiguration.loadConfiguration(MainDisabledFile);
		InputStream disabledData = plugin.getResource("disabled.yml");
		if (disabledData != null){
			MainDisabled.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(disabledData)));
		}
	}
	public static void reloadOpDisabled(){
		OpDisabledFile = new File(plugin.getDataFolder(), "opblock.yml");
		if (!OpDisabledFile.exists()){
			plugin.saveResource("opblock.yml", false);
		}
		OpDisabled = YamlConfiguration.loadConfiguration(OpDisabledFile);
		InputStream opData = plugin.getResource("opblock.yml");
		if (opData != null){
			OpDisabled.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(opData)));
		}
	}
}
