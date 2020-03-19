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
    public defaultCommandParser( final Main plugin , final String commandName, final Integer ConfigCommandsID ) {

        this.plugin = plugin;

        // Loading the Plugin Configurations
        this.config = plugin.getConfig();

        // Loading the Command Plugin Configurations
        final Integer dictionaryId = ConfigCommandsID;
        this.dictionary = (FileConfiguration)Array.get(plugin.customConfigs, dictionaryId);

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

        sender.sendMessage("Hellow");

        final commandResponse response = new commandResponse();
        response.message = "world";
        response.code = 0;
        return response;
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
        	if (args.length == 0) {
        		
                final Class<?>[] argumentsTypes = {this.plugin.getClass(), args.getClass(), CommandSender.class};
                final Object[] arguments = {this.plugin, args, (CommandSender) sender};
                
                String methodName = this.dictionary.getString("help.function");
                
                // sender.sendMessage(methodName);
                
            	final Method method = defaultCommandParser.class.getDeclaredMethod(methodName , argumentsTypes);
            	final commandResponse response = (commandResponse) method.invoke(null, arguments);
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

            final Class<?>[] argumentsTypes = {this.plugin.getClass(), args.getClass(), CommandSender.class};
            final Object[] arguments = {this.plugin, args, (CommandSender) sender};
            
            // final String methodName = this.dictionary.getString(args[0]+".function");

        	final Method method = defaultCommandParser.class.getDeclaredMethod(this.dictionary.getString(args[0]+".function") , argumentsTypes);
        	final commandResponse response = (commandResponse) method.invoke(null, arguments);
        	return response;
    		
    	} catch (final Throwable e) {

            final commandResponse response = new commandResponse();
            response.code = 1;
            response.message = logParser("ERROR::"+e.toString()+"->"+e.getLocalizedMessage()+"\n"+"Might be caused by a unavailability of the config file", response.code);
    		
    		return response;
    		//throw e;
    		
    	}

    }
	
}
