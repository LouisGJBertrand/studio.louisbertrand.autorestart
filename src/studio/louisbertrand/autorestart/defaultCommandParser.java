package studio.louisbertrand.autorestart;

//import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.annotation.Nonnull;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;


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
    public @Nonnull FileConfiguration config;
    
    /**
     * Dictionary correspond to a list of functions to trigger
     * according to the defaultcmd.config.yml file
     */
    public @Nonnull FileConfiguration dictionary;
    
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
    public defaultCommandParser( final Main plugin , String commandName, Integer ConfigCommandsID ) {

        this.plugin = plugin;

        // Loading the Plugin Configurations
        this.config = plugin.getConfig();

        // Loading the Command Plugin Configurations
        Integer dictionaryId = ConfigCommandsID;
        this.dictionary = (FileConfiguration)Array.get(plugin.customConfigs, dictionaryId);

        this.commandName = commandName;
        
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
    public commandResponse parse(final String[] args, CommandSender sender) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {

    	if (args.length == 0) {
            Class<?>[] argumentsTypes = {this.plugin.getClass(), args.getClass(), sender.getClass()};
            Object[] arguments = {this.plugin, args, sender};
        	Method method = this.getClass().getDeclaredMethod(this.dictionary.getString("help.function"), argumentsTypes);
        	commandResponse response = (commandResponse) method.invoke(null, arguments);
        	return response;
    		/*
            try {
                Class<?>[] argumentsTypes = {this.plugin.getClass(), args.getClass(), sender.getClass()};
                Object[] arguments = {this.plugin, args, sender};
            	Method method = this.getClass().getDeclaredMethod(this.dictionary.getString("help.function"), argumentsTypes);
            	commandResponse response = (commandResponse) method.invoke(null, arguments);
            	return response;
            } catch (Throwable e){
                final commandResponse response = new commandResponse();
                response.code = 1;
                response.message = logParser("INTERNAL SERVER ERROR : "+e.toString(), response.code);
            	System.out.println(e.toString());
                return response;
            }*/
    	}
    	
        if(!this.dictionary.getKeys(false).contains(args[0])){
            final commandResponse response = new commandResponse();
            response.code = 1;
            response.message = logParser("This parametter is not defined, please check the help.\n        to get the help type /"
                    + this.commandName + " <help>", response.code);

            return response;
        }
        try {
            Method method = this.getClass().getDeclaredMethod(this.dictionary.getString(args[0]+".function"));
            Object[] arguments = {this.plugin, args, sender};
            commandResponse response = (commandResponse) method.invoke(this, arguments);
            return response;
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
        | InvocationTargetException e){
            final commandResponse response = new commandResponse();
            response.code = 1;
            response.message = logParser("INTERNAL SERVER ERROR : "+e.getMessage(), response.code);
            return response;
        	
        }

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

    public commandResponse commandParsingArgumentHelp(Main plugin, String[] args, CraftPlayer sender) {

        sender.sendMessage("Hellow");

        final commandResponse response = new commandResponse();
        response.message = "world";
        response.code = 0;
        return response;
    }
	
}
