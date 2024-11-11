package org.modularsoft.zander.bridge;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.modularsoft.zander.bridge.util.api.Bridge;

public class ZanderBridgeMain extends JavaPlugin {
    public static ZanderBridgeMain plugin;

    @Override
    public void onEnable() {
        plugin = this;

        // Init Message
        TextComponent enabledMessage = Component.empty()
                .color(NamedTextColor.GREEN)
                .append(Component.text("\n\nZander Bridge has been enabled.\n"))
                .append(Component.text("Running Version " + plugin.getDescription().getVersion() + "\n"))
                .append(Component.text("GitHub Repository: https://github.com/ModularSoftAU/zander\n"))
                .append(Component.text("Created by Modular Software\n\n", NamedTextColor.DARK_PURPLE));
        getServer().sendMessage(enabledMessage);

        // Create an instance of Bridge and start the task
        Bridge bridge = new Bridge(this);
        bridge.startBridgeTask();

        saveConfig();
    }

    @Override
    public void onDisable() {}
}