package studio.louisbertrand.autorestart;

import java.io.File;
import java.io.IOException;
//import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;

/* Class Importations
 * 
 * imported:
 * 	org.bukkit.command.Command
 *  org.bukkit.command.CommandSender
 *  org.bukkit.plugin.java.JavaPlugin
 */
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Main class
 * 
 * the plugin main class
 * 
 * @author Louis BERTRAND
 *
 */
public class Main extends JavaPlugin {

    private File[] customConfigFiles = new File[99];
    public FileConfiguration[] customConfigs = new YamlConfiguration[99];
    public boolean valid = true;
    public boolean error = false;

    public Integer ConfigCommandsID = 0;
    
    public defaultCommandParser commandP;
    
    @Override
    public void onEnable() {
        this.createCustomConfig("commands",this.ConfigCommandsID);
        this.commandP = new defaultCommandParser(this, "autorestart", ConfigCommandsID);
    	System.out.println("[AUTORESTART] Activated");
    }

    @Override
    public void onDisable() {
    	System.out.println("[AUTORESTART] Disabled");
    }

    /**
     * onCommand
     * 
     * when a command has been triggered
     * 
     * @param sender
     * the command sender when triggered
     * 
     * @param command
     * the command triggered
     * 
     * @param label
     * i don't know, replace here later
     * 
     * @param args
     * arguments passed in the command
     */
    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label,
            final String[] args) {
        if (command.getName().equalsIgnoreCase("autorestart")) {
            commandResponse parsed;
			try {
				parsed = this.commandP.parse(args, sender);
	            if(parsed.code != 0){
	                sender.sendMessage(parsed.message);
	                if(parsed.code == 2) {
	    	            return this.valid;
	                }
	                return this.error;
	            }
	            return this.valid;
			} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				e.printStackTrace();
		        return this.error;
			}
        }
        return this.error;
    }

    /**
     * createCustomConfig
     * 
     * Loads custom configuration File with defined by a name and stores
     * it inside a public variable with a defined Identifier
     * 
     * @param $name
     * the name of the file
     * 
     * @param id
     * the id assigned to the configuration file
     * 
     * @return void
     */
    public void createCustomConfig(String $name, Integer id) {
        try {
            //this.customConfigFiles[id] = new File(this.getDataFolder()+"/"+name+".yml");
        	File tmpfile = new File(this.getDataFolder()+"/"+$name+".yml");
        	if(!tmpfile.exists()) {
        		tmpfile.getParentFile().mkdirs();
                saveResource($name+".yml", false);
        	}

            this.customConfigFiles[id] = tmpfile;
            this.customConfigs[id] = new YamlConfiguration();
        	this.customConfigs[id].load(tmpfile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
    
}
