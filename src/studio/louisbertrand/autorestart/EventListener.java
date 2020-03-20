package studio.louisbertrand.autorestart;

import org.bukkit.event.Listener;

public class EventListener implements Listener {
	

    private Main plugin = new Main();

    public void ExampleListener(Main plugin) {
        this.setPlugin(plugin);
        this.getPlugin().getServer().getPluginManager().registerEvents(this, plugin);
    }
    
    
    /**
     * Set plugin
     * 
     * @param plugin
     * @return boolean
     */
    public boolean setPlugin(Main plugin) {

        this.plugin = plugin;
    	
		return true;    	
    	
    }
    
    public Main getPlugin() {
    	return this.plugin;
    }


}
