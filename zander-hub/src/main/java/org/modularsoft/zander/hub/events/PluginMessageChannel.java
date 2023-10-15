package org.modularsoft.zander.hub.events;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.modularsoft.zander.hub.ZanderHubMain;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class PluginMessageChannel implements Listener {
    static ZanderHubMain plugin;
    public PluginMessageChannel(ZanderHubMain plugin) {
        this.plugin = plugin;
    }

    // Server Connection Function
    public static void connect(Player player, String server) {
        ByteArrayDataOutput output = ByteStreams.newDataOutput();
        output.writeUTF("Connect");
        output.writeUTF(server);

        player.sendPluginMessage(plugin, "BungeeCord", output.toByteArray());
    }

}
