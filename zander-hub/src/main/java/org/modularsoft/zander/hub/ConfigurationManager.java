package org.modularsoft.zander.hub;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.modularsoft.zander.hub.configs.MessageConfig;

import java.io.File;

public class ConfigurationManager {
    private static FileConfiguration welcomeFile;
    private static MessageConfig messageConfig;

    //
    // Welcome File
    //
    public static void setupWelcomeFile() {
        if (!ZanderHubMain.plugin.getDataFolder().exists()) {
            ZanderHubMain.plugin.getDataFolder().mkdir();
        }
        File existingWelcomeFile = new File(ZanderHubMain.plugin.getDataFolder(), "welcome.yml");

        if (!existingWelcomeFile.exists()) {
            // Reads the welcome.yml from the resource folder and then creates the file in
            // the folder that
            // goes next to the plugin.
            ZanderHubMain.plugin.saveResource("welcome.yml", false);
            Bukkit.getServer().getConsoleSender().sendMessage(
                    Component.text("The welcome.yml file has been created.", NamedTextColor.GREEN));
        }
        // Reopen the file since it should exist now
        existingWelcomeFile = new File(ZanderHubMain.plugin.getDataFolder(), "welcome.yml");
        welcomeFile = YamlConfiguration.loadConfiguration(existingWelcomeFile);
    }

    public static FileConfiguration getWelcome() {
        if (welcomeFile == null) {
            setupWelcomeFile();
        }
        return welcomeFile;
    }

    public static Location getHubLocation() {
        World defaultworld = Bukkit.getServer().getWorlds().get(0);
        World hubworld = Bukkit
                .getWorld(ZanderHubMain.plugin.getConfig().getString("hub.world", defaultworld.getName()));
        if (hubworld == null) {
            Bukkit.getLogger().warning(
                    "No world by the name of " + defaultworld.getName() + " was found! Assuming default world.");
            hubworld = Bukkit.getServer().getWorlds().get(0);
        }
        double hubx = ZanderHubMain.plugin.getConfig().getDouble("hub.x", defaultworld.getSpawnLocation().getX());
        double huby = ZanderHubMain.plugin.getConfig().getDouble("hub.y", defaultworld.getSpawnLocation().getY());
        double hubz = ZanderHubMain.plugin.getConfig().getDouble("hub.z", defaultworld.getSpawnLocation().getZ());
        float hubyaw = (float) ZanderHubMain.plugin.getConfig().getDouble("hub.yaw", 0);
        float hubpitch = (float) ZanderHubMain.plugin.getConfig().getDouble("hub.pitch", 0);

        return new Location(hubworld, hubx, huby, hubz, hubyaw, hubpitch);
    }

    public static void setupMessage() {
        FileConfiguration config = ZanderHubMain.plugin.getConfig();

        // ensure join/leave messages exist in 'config.yml'
        boolean hasModifiedConfig = false;
        if (!config.contains("messages.join")) {
            ZanderHubMain.plugin.getConfig().set("messages.join", "&7%p% joined.");
            hasModifiedConfig = true;
        }
        if (!config.contains("messages.leave")) {
            ZanderHubMain.plugin.getConfig().set("messages.leave", "&7%p% left.");
            hasModifiedConfig = true;
        }
        if (hasModifiedConfig) {
            ZanderHubMain.plugin.saveConfig();
        }

        String messageJoin = ZanderHubMain.plugin.getConfig().getString("messages.join");
        String messageLeave = ZanderHubMain.plugin.getConfig().getString("messages.leave");

        messageConfig = new MessageConfig(ZanderHubMain.plugin);
        messageConfig.setupJoinLeave(messageJoin, messageLeave);
    }

    public static MessageConfig getMessage() {
        return messageConfig;
    }
}
