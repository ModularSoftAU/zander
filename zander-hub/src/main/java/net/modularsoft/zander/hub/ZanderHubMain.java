package net.modularsoft.zander.hub;

import net.modularsoft.zander.hub.commands.fly;
import net.modularsoft.zander.hub.events.HubBoosterPlate;
import net.modularsoft.zander.hub.events.HubPlayerJoin;
import net.modularsoft.zander.hub.events.HubPlayerJoinChristmas;
import net.modularsoft.zander.hub.events.HubPlayerVoid;
import net.modularsoft.zander.hub.gui.HubCompassItem;
import net.modularsoft.zander.hub.protection.HubCreatureSpawnProtection;
import net.modularsoft.zander.hub.protection.HubInteractionProtection;
import net.modularsoft.zander.hub.protection.HubProtection;
import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class ZanderHubMain extends JavaPlugin {

    public static ZanderHubMain plugin;
    public ConfigurationManager configurationManager;

    public void onEnable() {
        plugin = this;
        loadConfigurationManager();

        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
//        this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new PluginMessageChannel(this));

        // Init Message
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "\n\nZander Hub has been enabled.\nRunning Version " + plugin.getDescription().getVersion() + "\nGitHub Repository: https://github.com/crafting-for-christ/zander-hub\nCreated by Ben Robson\n\n");

        // Event Registry
        PluginManager pluginmanager = this.getServer().getPluginManager();
        pluginmanager.registerEvents(new HubPlayerJoin(this), this);
        pluginmanager.registerEvents(new HubPlayerVoid(this), this);
        pluginmanager.registerEvents(new HubBoosterPlate(this), this);
        pluginmanager.registerEvents(new HubPlayerJoinChristmas(this), this);
        // Hub Protection
        pluginmanager.registerEvents(new HubProtection(this), this);
        pluginmanager.registerEvents(new HubInteractionProtection(this), this);
        pluginmanager.registerEvents(new HubCreatureSpawnProtection(this), this);

        // Item Event Registry
        pluginmanager.registerEvents(new HubCompassItem(), this);

        // Command Registry
        this.getCommand("fly").setExecutor(new fly());

        configurationManager.getHubLocation();
        saveConfig();
    }

    public void loadConfigurationManager() {
        configurationManager = new ConfigurationManager(plugin);
        configurationManager.loadlocalConfiguration(); // Loading the config.yml

        configurationManager.setupmotd();
    }

    @Override
    public void onDisable() {
        loadConfigurationManager();
    }

}
