package org.modularsoft.zander.hub;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.modularsoft.zander.hub.commands.fly;
import org.modularsoft.zander.hub.events.HubBoosterPlate;
import org.modularsoft.zander.hub.events.HubPlayerJoin;
import org.modularsoft.zander.hub.events.HubPlayerJoinChristmas;
import org.modularsoft.zander.hub.events.HubPlayerVoid;
import org.modularsoft.zander.hub.gui.HubCompassItem;
import org.modularsoft.zander.hub.protection.HubCreatureSpawnProtection;
import org.modularsoft.zander.hub.protection.HubInteractionProtection;
import org.modularsoft.zander.hub.protection.HubProtection;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class ZanderHubMain extends JavaPlugin {
    public static ZanderHubMain plugin;

    public void onEnable() {
        plugin = this;

        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
//        this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new PluginMessageChannel(this));

        // Init Message
        TextComponent enabledMessage = Component.empty()
                .color(NamedTextColor.GREEN)
                .append(Component.text("\n\nZander Hub has been enabled.\n"))
//                .append(Component.text("Running Version " + ZanderHubMain.class.getPackage().getImplementationVersion() + "\n"))
                .append(Component.text("Running Version " + plugin.getDescription().getVersion() + "\n"))
                .append(Component.text("GitHub Repository: https://github.com/ModularSoftAU/zander\n"))
                .append(Component.text("Created by Modular Software\n\n", NamedTextColor.DARK_PURPLE));
        getServer().sendMessage(enabledMessage);

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

        ConfigurationManager.getHubLocation();
        saveConfig();
    }

    @Override
    public void onDisable() {}
}
