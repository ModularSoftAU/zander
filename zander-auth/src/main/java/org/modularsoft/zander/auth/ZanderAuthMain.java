package org.modularsoft.zander.auth;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.modularsoft.zander.auth.events.AuthPlayerJoin;
import org.modularsoft.zander.auth.events.UserOnServerPing;

public class ZanderAuthMain extends JavaPlugin {
    public static ZanderAuthMain plugin;

    public void onEnable() {
        plugin = this;

        // Init Message
        TextComponent enabledMessage = Component.empty()
                .color(NamedTextColor.GREEN)
                .append(Component.text("\n\nZander Auth has been enabled.\n"))
                .append(Component.text("Running Version " + plugin.getDescription().getVersion() + "\n"))
                .append(Component.text("GitHub Repository: https://github.com/ModularSoftAU/zander\n"))
                .append(Component.text("Created by Modular Software\n\n", NamedTextColor.DARK_PURPLE));
        getServer().sendMessage(enabledMessage);

        // Event Registry
        PluginManager pluginmanager = this.getServer().getPluginManager();
        pluginmanager.registerEvents(new AuthPlayerJoin(this), this);
        pluginmanager.registerEvents(new UserOnServerPing(), this);

        saveConfig();
    }

    @Override
    public void onDisable() {}
}
