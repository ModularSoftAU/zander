package com.modularenigma.zander.auth;

import com.modularenigma.zander.auth.events.PlayerVerificationCodePull;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class ZanderAuthMain extends JavaPlugin {
    public static ZanderAuthMain plugin;

    public void onEnable() {
        plugin = this;

        // Init Message
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "\n\nZander Auth has been enabled.\nRunning Version " + plugin.getDescription().getVersion() + "\nGitHub Repository: https://github.com/ModularEnigma/zander\nCreated by ModularEnigma\n\n");

        // Event Registry
        PluginManager pluginmanager = this.getServer().getPluginManager();
        pluginmanager.registerEvents(new PlayerVerificationCodePull(this), this);

        saveConfig();
    }

    @Override
    public void onDisable() {

    }

}
