package com.modularenigma.zander.proxy.events;

import com.modularenigma.zander.proxy.ConfigurationManager;
import com.modularenigma.zander.proxy.ZanderProxyMain;
import com.modularenigma.zander.proxy.model.discord.DiscordSwitch;
import com.modularenigma.zander.proxy.model.session.SessionSwitch;
import io.github.ModularEnigma.Request;
import io.github.ModularEnigma.Response;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.UUID;

public class UserOnSwitch implements Listener {
    private ZanderProxyMain plugin = ZanderProxyMain.getInstance();

    @EventHandler
    public void UserSwitchEvent (ServerSwitchEvent event) {
        ProxiedPlayer player = event.getPlayer();
        String username = player.getDisplayName();
        UUID playerUUID = player.getUniqueId();
        String server = event.getFrom().getName();

        try {
            //
            // Switch Session API POST
            //
            SessionSwitch switchSession = SessionSwitch.builder()
                    .uuid(playerUUID)
                    .server(server)
                    .build();

            Request switchSessionReq = Request.builder()
                    .setURL(ConfigurationManager.getConfig().get("BaseAPIURL") + "/session/switch")
                    .setMethod(Request.Method.POST)
                    .addHeader("x-access-token", String.valueOf(ConfigurationManager.getConfig().get("APIKey")))
                    .setRequestBody(switchSession.toString())
                    .build();

            Response switchSessionRes = switchSessionReq.execute();
            plugin.getProxy().getConsole().sendMessage(new TextComponent("Response (" + switchSessionRes.getStatusCode() + "): " + switchSessionRes.getBody()));
        } catch (Exception e) {
            player.disconnect(new TextComponent("An error has occurred. Is the API down?"));
            System.out.println(e);
        }

        try {
            //
            // Server Switch event throws an error when user has not switched from anywhere.
            //
            DiscordSwitch discordSwitch = DiscordSwitch.builder()
                    .username(username)
                    .server(server)
                    .build();

            Request req = Request.builder()
                    .setURL(ConfigurationManager.getConfig().get("BaseAPIURL") + "/discord/switch")
                    .setMethod(Request.Method.POST)
                    .addHeader("x-access-token", String.valueOf(ConfigurationManager.getConfig().get("APIKey")))
                    .setRequestBody(discordSwitch.toString())
                    .build();

            Response res = req.execute();
            plugin.getProxy().getConsole().sendMessage(new TextComponent("Response (" + res.getStatusCode() + "): " + res.getBody()));
        } catch (Exception e) {
            player.disconnect(new TextComponent("An error has occurred. Is the API down?"));
            System.out.println(e);
        }
    }

}
