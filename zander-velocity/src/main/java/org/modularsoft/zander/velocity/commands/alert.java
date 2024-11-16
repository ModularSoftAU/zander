package org.modularsoft.zander.velocity.commands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.modularsoft.zander.velocity.ZanderVelocityMain;
import dev.dejvokep.boostedyaml.route.Route;

public class alert implements SimpleCommand {

    @Override
    public void execute(final Invocation invocation) {
        CommandSource source = invocation.source();

        // Check if the command source has the required permission
        if (!source.hasPermission("zander.proxy.command.alert")) {
            source.sendMessage(Component.text("You do not have permission to use this command.")
                    .color(NamedTextColor.RED));
            return;
        }

        // Retrieve the announcement tip prefix from the configuration
        String alertCommandPrefix = ZanderVelocityMain.getConfig().getString(Route.from("alertCommandPrefix"));

        // Construct the alert message from command arguments
        String[] args = invocation.arguments();
        if (args.length == 0) {
            source.sendMessage(Component.text("Usage: /alert <message>").color(NamedTextColor.RED));
            return;
        }

        String alertMessage = String.join(" ", args);

        // Use the LegacyComponentSerializer to handle color codes and prefix
        char translate = '&';
        Component message = LegacyComponentSerializer.legacy(translate)
                .deserialize(alertCommandPrefix + alertMessage);

        // Broadcast the alert message
        ZanderVelocityMain.getProxy().sendMessage(message);
    }
}
