package net.modularsoft.zander.hub.events;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.modularsoft.zander.hub.ZanderHubMain;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class PluginMessageChannel implements Listener {
    static ZanderHubMain plugin;
    public PluginMessageChannel(ZanderHubMain plugin) {
        this.plugin = plugin;
    }

//    @Override
//    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
//        if (!channel.equals("BungeeCord")) return;
//
//        ByteArrayDataInput input = ByteStreams.newDataInput(message);
//        String subchannel = input.readUTF();
//    }

    // Server Connection Function
    public static void connect(Player player, String server) {
        ByteArrayDataOutput output = ByteStreams.newDataOutput();
        output.writeUTF("Connect");
        output.writeUTF(server);

        player.sendPluginMessage(plugin, "BungeeCord", output.toByteArray());
    }

}
