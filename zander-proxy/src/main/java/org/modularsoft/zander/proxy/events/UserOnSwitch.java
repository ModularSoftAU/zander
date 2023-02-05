package org.modularsoft.zander.proxy.events;

import org.modularsoft.zander.proxy.ConfigurationManager;
import org.modularsoft.zander.proxy.ZanderProxyMain;
import org.modularsoft.zander.proxy.model.discord.DiscordSwitch;
import org.modularsoft.zander.proxy.model.session.SessionSwitch;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import io.github.ModularEnigma.Request;
import io.github.ModularEnigma.Response;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class UserOnSwitch implements Listener {
    private ZanderProxyMain plugin = ZanderProxyMain.getInstance();

    @EventHandler
    public void UserSwitchEvent (ServerSwitchEvent event) {
        ProxiedPlayer player = event.getPlayer();

        try {
            //
            // Switch Session API POST
            //
            SessionSwitch switchSession = SessionSwitch.builder()
                    .uuid(player.getUniqueId())
                    .server(event.getFrom().getName())
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
                    .username(player.getDisplayName())
                    .server(event.getFrom().getName())
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
