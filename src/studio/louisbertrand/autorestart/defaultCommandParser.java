package studio.louisbertrand.autorestart;

//import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

// import com.google.common.io.Files;

// import java.io.File;
// import java.io.IOException;
import java.lang.reflect.Array;
// import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
// import java.nio.charset.StandardCharsets;

import javax.annotation.Nonnull;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
//import org.bukkit.craftbukkit.v1_15_R1.command.CraftRemoteConsoleCommandSender;
//import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;


// import java.lang.Class;

/**
 * defaultCommandParser
 * 
 * the default command parser of the plugin.
 * it parses the default command when triggered by the console
 * or a player.
 * 
 * @author Louis BERTRAND
 *
 */
// @SuppressWarnings("rawtypes")
public class defaultCommandParser{

    /**
     * Config is the plugin configuration that could be used through the
     * parsing of the command
     */
    private @Nonnull FileConfiguration config;
    
    /**
     * Dictionary correspond to a list of functions to trigger
     * according to the defaultcmd.config.yml file
     */
    private @Nonnull FileConfiguration dictionary;
    
    /**
     * This variable correspond to the command name for logging
     * purposes.
     * 
     * E.G. commandName = "spawn" means that when it logs something
     * the name spawn will be used to show which command has been
     * triggered in the chat.
     */
    public String commandName;

    public JavaPlugin plugin;

    /**
     * Constructor;
     * 
     * @param plugin
     * the plugin for configurations
     * @param commandName 
     */
    public defaultCommandParser( final Main plugin , final String commandName, final Integer ConfigCommandsID ) {

        this.plugin = plugin;

        // Loading the Plugin Configurations
        this.config = plugin.getConfig();

        // Loading the Command Plugin Configurations
        final Integer dictionaryId = ConfigCommandsID;
        this.dictionary = (FileConfiguration)Array.get(plugin.getCustomConfigs(), dictionaryId);

        this.commandName = commandName;
        
    }

    /**
     * logParser;
     * 
     * Returns a parsed message for logging
     * 
     * @param string
     * @return
     */
    public String logParser(final String string, final Integer type) {

        final String formatPrefix = "[";
        final String formatSuffix = "]";

        // 0: Everything is fine
        // 1: ERROR
        // 2: WARNING
        final ChatColor[] formatColors = { ChatColor.GREEN, ChatColor.RED, ChatColor.GOLD };

        final ChatColor color = (ChatColor) Array.get(formatColors, type);

        final String response = formatPrefix+color.toString()+this.commandName+ChatColor.RESET.toString()+formatSuffix+" "+string;
        return response;
    }

    /**
     * commandParsingArgumentHelp
     * 
     * help method
     * 
     * @param plugin
     * @param args
     * @param sender
     * @return
     */
    public commandResponse commandParsingArgumentHelp(final Main plugin, final String[] args, final CommandSender sender) {

    	String message;
    	
    	try {
			message = plugin.getCustomLangs()[plugin.LangHelpStringID];
	        sender.sendMessage(logParser(message,0));
	        final commandResponse response = new commandResponse();
	        response.code = 0;
	        response.message = "help has been shown";
	        return response;
		} catch (Throwable e) {
	        final commandResponse response = new commandResponse();
	        response.code = 1;
	        response.message = logParser("ERROR::"+e.getMessage(), response.code);
            e.printStackTrace();
	        return response;
		}
    }

    /**
     * Parse;
     * 
     * parse is a function that is used to parse the command arguments in order to
     * trigger the function attached to it.
     * 
     * @param args
     * the arguments passed when calling the function by chat or console
     * 
     * @return commandResponse 
     * 
     */
    public commandResponse parse(final String[] args, final CommandSender sender) throws Throwable {

    	try {
    		
    		/**
    		 * If no arguments passed,
    		 * return the help function
    		 */
        	if (args.length == 0) {
        		
        		// Invoke Setup
                final Class<?>[] argumentsTypes = {this.plugin.getClass(), args.getClass(), CommandSender.class};
                final Object[] arguments = {this.plugin, args, (CommandSender) sender};
                
                // retrieving the function name associated to the first cmd argument
                String methodName = this.dictionary.getString("help.function");
                
                // Actual invoking
            	final Method method = defaultCommandParser.class.getDeclaredMethod(methodName , argumentsTypes);
            													 // NOTE:   use 'this' to say who is triggering (Grooble)
            	final commandResponse response = (commandResponse) method.invoke(this, arguments);
            	return response;
            	
        	}

    		/**
    		 *  If the argument passed is not defined in commands.yml,
    		 * return an error to indicate the miss match
    		 */
            if(!this.dictionary.getKeys(false).contains(args[0])){
                final commandResponse response = new commandResponse();
                response.code = 1;
                response.message = logParser("This parametter is not defined, please check the help.\n        to get the help type /"
                        + this.commandName + " <help>", response.code);

                return response;
            }

            /**
             * else
             * return the response of the desired function according to the
             * function associated to the first argument
             */
    		// Invoke Setup
            final Class<?>[] argumentsTypes = {this.plugin.getClass(), args.getClass(), CommandSender.class};
            final Object[] arguments = {this.plugin, args, (CommandSender) sender};
            
            // retrieving the function name associated to the first cmd argument
            String methodName = this.dictionary.getString(args[0]+".function");

        	final Method method = defaultCommandParser.class.getDeclaredMethod(methodName , argumentsTypes);
															 // NOTE:   use 'this' to say who is triggering (Grooble)
        	final commandResponse response = (commandResponse) method.invoke(this, arguments);
        	return response;
    		
    	} catch (final Throwable e) {

            final commandResponse response = new commandResponse();
            response.code = 1;
            response.message = logParser("ERROR::"+e.toString()+"->"+e.hashCode()+"\n"+"Might be caused by a unavailability of the config file", response.code);

            e.printStackTrace();
    		return response;
    		//throw e;
    		
    	}

    }
	
}
