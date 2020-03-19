package studio.louisbertrand.autorestart;

/**
 *  Class Importations
 */
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;
import java.io.IOException;
// import java.lang.reflect.Array;
// import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;

// import javax.annotation.Nonnull;
import com.google.common.io.Files;

/**
 * Main class
 * 
 * the plugin main class
 * 
 * @author Louis BERTRAND
 *
 */
public class Main extends JavaPlugin {

    private final File[] customConfigFiles = new File[99];
    public FileConfiguration[] customConfigs = new YamlConfiguration[99];

    private final File[] customLangsFiles = new File[99];
    public String[] customLangs = new String[99];
    
    public boolean valid = true;
    public boolean error = false;
    public boolean warning = this.valid;

    // YML CONFIG FILES IDS SHORTCUTS
    public Integer ConfigCommandsID = 0;

    // TXT LANGS FILES IDS SHORTCUTS
    public Integer LangHelpStringID = 0;
    
    public defaultCommandParser commandP;
    
    @Override
    public void onEnable() {
    	
    	try {
	    	// Loading Command Config
	        this.createCustomConfig("commands", this.ConfigCommandsID);
	        
	        // Loading Lang File for Help in english
	        this.loadLangFileByName("lang/help.en.txt", this.LangHelpStringID);
	        
	        // declaring default command
	        this.commandP = new defaultCommandParser(this, "autorestart", ConfigCommandsID);
	        
	        
	        /**
	         * IF RETURNED,
	         * IT MEENS THAT EVERYTHING IS WELL IMPORTED/ACTIVATED/DECLARED
	         */
	    	System.out.println("[AUTORESTART] Activated");
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
				parsed = this.commandP.parse(args, sender);
				
				// Test if the response is not a valid response
	            if(parsed.code != 0){
	            	
		            // Send response
	                sender.sendMessage(parsed.message);
	                if(parsed.code == 2) {
	    	            // Return if Warning
	    	            return this.warning;
	                }
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
        	final File tmpfile = new File(this.getDataFolder()+"/"+$name+".yml");
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

            this.customLangsFiles[id] = tmpfile;
            this.customLangs[id] = Files.toString(tmpfile, StandardCharsets.UTF_8);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
    
    
}
