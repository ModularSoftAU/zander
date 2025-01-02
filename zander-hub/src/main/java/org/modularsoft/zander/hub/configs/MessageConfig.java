package org.modularsoft.zander.hub.configs;

import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.modularsoft.zander.hub.ZanderHubMain;

public class MessageConfig {
    private final ZanderHubMain plugin;

    // holds pre-deserialized legacy text
    private TextComponent textCompJoinTemplate;
    private TextComponent textCompLeaveTemplate;

    public MessageConfig(ZanderHubMain plugin) {
        this.plugin = plugin;
    }

    /// Configure the join/leave messages to be provided for the entire project.
    /// Example string: "&7%p% joined the game" (%p% gets replaced by player name).
    public void setupJoinLeave(String textLegacyJoin, String textLegacyLeave) {
        int rc1 = parseToTemplate(textLegacyJoin, "&7%p% <default join message>", // * relied to be correct
                (template) -> this.textCompJoinTemplate = template);
        int rc2 = parseToTemplate(textLegacyLeave, "&7%p% <default leave message>", // * relied to be correct
                (template) -> this.textCompLeaveTemplate = template);
        if (rc1 == 0 && rc2 == 0) {
            if (plugin.getServer().getPluginManager().getPlugin("PremiumVanish") != null) {
                setPremiumVanish(textLegacyJoin, textLegacyLeave);
            }
        }
    }

    /// Retrieve the 'join' message for `playerName`.
    public Component playerJoin(Component playerName) {
        if (textCompJoinTemplate == null)
            throw new IllegalStateException("Message not initialized, please run 'setupJoinLeave'");
        return insertPlayerName(this.textCompJoinTemplate, playerName);
    }

    /// Retrieve the 'leave' message for `playerName`.
    public Component playerLeave(Component playerName) {
        if (textCompLeaveTemplate == null)
            throw new IllegalStateException("Message not initialized, please run 'setupJoinLeave'");
        return insertPlayerName(this.textCompLeaveTemplate, playerName);
    }

    /// Insert component `playerName` into `template`.
    // * depends on 'parseToTemplate' who ensures `template` contains single '%p%'
    private Component insertPlayerName(TextComponent template, Component playerName) {
        return template.replaceText(builder -> builder
                .match("%p%")
                .replacement(playerName)); // * correctly retains any style
    }

    /// Parse `textLegacy` (+ verify placeholder %p%) into TextComponent template.
    /// Returns 0 if successful (use custom text), 1 if failed (use default text).
    private int parseToTemplate(String textLegacy, String textLegacyDefault, Consumer<TextComponent> setter) {
        LegacyComponentSerializer serializer = LegacyComponentSerializer.legacyAmpersand();
        try {
            // verify the '%p%' exists exactly once
            int placeholderIndex = textLegacy.indexOf("%p%");
            if (placeholderIndex == -1 || placeholderIndex != textLegacy.lastIndexOf("%p%")) {
                throw new IllegalArgumentException("Should contain a single '%p%' (placeholder player name)");
            }
            TextComponent template = serializer.deserialize(textLegacy);
            setter.accept(template);
            return 0;
        } catch (Exception e) {
            plugin.getLogger().warning(String.format("Invalid legacy string \"%s\"", textLegacy));
            plugin.getLogger().warning(String.format("%s", e.getMessage()));

            // * fall back to default
            TextComponent template = serializer.deserialize(textLegacyDefault);
            setter.accept(template);
            return 1;
        }
    }

    /// Set join/leave messages in PremiumVanish 'messages.yml' file.
    private void setPremiumVanish(String textLegacyJoin, String textLegacyLeave) {
        File pvYML = new File(plugin.getDataFolder().getParentFile(), "PremiumVanish/messages.yml");
        if (pvYML.exists()) {
            YamlConfiguration config = YamlConfiguration.loadConfiguration(pvYML);
            config.set("Messages.DiscordSRVFakeJoin", textLegacyJoin);
            config.set("Messages.DiscordSRVFakeQuit", textLegacyLeave);
            config.set("Messages.ReappearMessage", textLegacyJoin);
            config.set("Messages.VanishMessage", textLegacyLeave);
            try {
                config.save(pvYML);
            } catch (IOException e) {
                plugin.getLogger().warning("Failed to update PremiumVanish messages.yml");
            }
        }
    }
}
