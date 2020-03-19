package studio.louisbertrand.autorestart;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
// import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;
// import java.io.IOException;
// import java.lang.reflect.Array;
// import java.lang.reflect.Array;
// import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
// import javax.annotation.Nonnull;
// import javax.annotation.Nonnull;
import com.google.common.io.Files;
// import com.mojang.datafixers.types.templates.List;
// import com.sun.tools.javac.util.ArrayUtils;

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
    private FileConfiguration[] customConfigs = new YamlConfiguration[99];

    private File[] customLangsFiles = new File[99];
    private String[] customLangs = new String[99];
    
    public boolean valid = true;
    public boolean error = false;
    public boolean warning = this.valid;

    // YML CONFIG FILES IDS SHORTCUTS
    public Integer ConfigCommandsID = 0;

    // TXT LANGS FILES IDS SHORTCUTS
    public Integer LangHelpStringID = 0;
    
    private defaultCommandParser commandP;
    
    @Override
    public void onEnable() {
    	
    	try {
	    	// Loading Command Config
	        this.createCustomConfig("commands", this.ConfigCommandsID);
	        
	        // Loading Lang File for Help in english
	        this.loadLangFileByName("lang/help.en.txt", this.LangHelpStringID);
	        
	        // declaring default command
	        setCommandParser(new defaultCommandParser(this, "autorestart", ConfigCommandsID));
	        
	        
	        /**
	         * IF RETURNED,
	         * IT MEENS THAT EVERYTHING IS WELL IMPORTED/ACTIVATED/DECLARED
	         */
	    	System.out.println("[AUTORESTART FOR SPIGOT] Plugin activated");
    	}catch(Throwable e){
    		
    		e.printStackTrace();
    		throw e;
    		
    	}
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

            /*
            // List Arguments
            // DEBUG PURPOSE ONLY
            for (int i = 0 ; i < args.length ; i++) {
                sender.sendMessage("args["+i+"]");
                sender.sendMessage(args[i]);
            } 
            */           
            
			try {
	            // Parsing Command
				parsed = this.getCommandParser().parse(args, sender);
				
                if(parsed.code == 2) {
	            	
		            // Send response
	                sender.sendMessage(parsed.message);
    	            // Return if Warning
    	            return this.warning;

                }
				
				// Test if the response is not a valid response
	            if(parsed.code != 0){
	            	
		            // Send response
	                sender.sendMessage(parsed.message);
    	            // Return error
	                return this.error;

	            }
	            
	            // Return valid
	            return this.valid;
            	
			} catch (final Throwable e) {
				
				// in case of fatal error, throw the error
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
    public void createCustomConfig(final String $name, final Integer id) {
        try {
            //this.customConfigFiles[id] = new File(this.getDataFolder()+"/"+name+".yml");
        	File tmpfile = new File(this.getDataFolder()+"/"+$name+".yml");
        	if(!tmpfile.exists()) {
        		tmpfile.getParentFile().mkdirs();
                saveResource($name+".yml", false);
        	}

            File[] TEMPcustomConfigFiles = getCustomConfigFiles();
            TEMPcustomConfigFiles[id] = tmpfile;
            this.setCustomConfigFiles(TEMPcustomConfigFiles);

            FileConfiguration[] TEMPcustomConfigs = this.getCustomConfigs();
            TEMPcustomConfigs[id] = new YamlConfiguration();
            this.setCustomConfigs(TEMPcustomConfigs);
        	this.getCustomConfigs()[id].load(tmpfile);
        } catch (Throwable e) {
            e.printStackTrace();
        }
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
    public void loadLangFileByName(final String $name, final Integer id) {
        try {
        	File tmpfile;
        	tmpfile = new File(this.getDataFolder().toString()+"/"+$name);
        	if(!tmpfile.exists()) {
        		tmpfile.getParentFile().mkdirs();
                this.saveResource($name, false);
        	}
        	File[] TEMPcustomLangsFiles = this.getCustomLangsFiles();
            TEMPcustomLangsFiles[id] = tmpfile;
            String[] TEMPcustomLangs = this.getCustomLangs();
			TEMPcustomLangs[id] = Files.toString(TEMPcustomLangsFiles[id], StandardCharsets.UTF_8);

			this.setCustomLangsFiles(TEMPcustomLangsFiles);
			this.setCustomLangs(TEMPcustomLangs);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 
     * FROM HERE IT'S GET AND SET REGION
     * 
     * ACCESS VARIABLE VIA THESE METHODS
     * 
     */

    /**
     * Get customConfigFiles
     * @return customLangsFiles
     */
    public File[] getCustomConfigFiles() {
    	return this.customConfigFiles;
    }
    
    /**
     * Set customConfigFiles
     */
    public void setCustomConfigFiles(File[] variable) {
    	try {
    		
    		// loading a list to test values and other stuff
    		// without touching the actual variable
            java.util.List<File> list = Arrays.asList(variable);
    		
    		if (list.contains(null)) {
    			
    			getLogger().info("[studio.louisbertrand.autorestart.Main.setCustomConfigFiles] :: one or more pointer is defined as Null on the variable to set. Ignoring these.");
    			
    			ArrayUtils.nullToEmpty(variable);
    			
    			// Throwing
    			// throw new NullPointerException("the variable to set contains a nullPointer.");
    		}
    		
    		this.customConfigFiles = variable;
    		
    	} catch (Throwable e) {
    		
    	}
    }
    
    /**
     * Get customLangsFiles
     * @return customLangsFiles
     */
    public File[] getCustomLangsFiles() {
    	return this.customLangsFiles;
    }
    
    /**
     * Set customLangsFiles
     * 
     */
    public void setCustomLangsFiles(File[] variable) {
    	try {
    		
    		// loading a list to test values and other stuff
    		// without touching the actual variable
            java.util.List<File> list = Arrays.asList(variable);
    		
    		if (list.contains(null)) {
    			
    			getLogger().info("[studio.louisbertrand.autorestart.Main.setCustomLangsFiles] :: one or more pointer is defined as Null on the variable to set. Ignoring these.");
    			
    			ArrayUtils.nullToEmpty(variable);
    			
    			// Throwing
    			// throw new NullPointerException("the variable to set contains a nullPointer.");
    		}
    		
    		this.customLangsFiles = variable;
    		
    	} catch (Throwable e) {
    		
    	}
    }
    
    /**
     * get customConfigs
     * @return CustomConfigs
     */
    public FileConfiguration[] getCustomConfigs() {
    	return this.customConfigs;
    }
    
    /**
     * Set customConfigs
     */
    public void setCustomConfigs(FileConfiguration[] variable) {
    	try {
    		
    		// loading a list to test values and other stuff
    		// without touching the actual variable
            List<FileConfiguration> list = Arrays.asList(variable);
    		
    		if (list.contains(null)) {
    			
    			getLogger().info("[studio.louisbertrand.autorestart.Main.setCustomConfigs] :: one or more pointer is defined as Null on the variable to set. Ignoring these.");
    			
    			ArrayUtils.nullToEmpty(variable);
    			
    			// Throwing
    			// throw new NullPointerException("the variable to set contains a nullPointer.");
    		}
    		
    		this.customConfigs = variable;
    		
    	} catch (Throwable e) {
    		
    	}
    }

    /**
     * get customConfigs
     * @return CustomConfigs
     */
    public String[] getCustomLangs() {
    	return this.customLangs;
    }
    
    /**
     * Set customConfigs
     */
    public void setCustomLangs(String[] variable) {
    	try {
    		
    		// loading a list to test values and other stuff
    		// without touching the actual variable
            List<String> list = Arrays.asList(variable);
    		
    		if (list.contains(null)) {
    			
    			getLogger().info("[studio.louisbertrand.autorestart.Main.setCustomLangs] :: one or more pointer is defined as Null on the variable to set. Ignoring these.");
    			
    			ArrayUtils.nullToEmpty(variable);
    			
    			// Throwing
    			// throw new NullPointerException("the variable to set contains a nullPointer.");
    		}
    		
    		this.customLangs = variable;
    		
    	} catch (Throwable e) {
    		
    	}
    }

    /**
     * get customConfigs
     * @return CustomConfigs
     */
    public defaultCommandParser getCommandParser() {
    	return this.commandP;
    }
    
    /**
     * Set customConfigs
     */
    public boolean setCommandParser(defaultCommandParser variable) {
    	try {
    		
    		if (variable == null) {

    			getLogger().info("[studio.louisbertrand.autorestart.Main.setCustomLangs] :: the command parser is defined as Null. Ignoring it and continue. Note that this will cause malfunction of the default command");
    			return this.error;
    		}
    		
    		this.commandP = variable;
    		
    	} catch (Throwable e) {
    		e.printStackTrace();
    	}
		return this.valid;
    }
    
    
}
