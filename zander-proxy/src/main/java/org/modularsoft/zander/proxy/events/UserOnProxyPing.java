package org.modularsoft.zander.proxy.events;

import com.jayway.jsonpath.JsonPath;
import org.modularsoft.zander.proxy.ConfigurationManager;
import org.modularsoft.zander.proxy.ZanderProxyMain;
import io.github.ModularEnigma.Request;
import io.github.ModularEnigma.Response;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class UserOnProxyPing implements Listener {
    private ZanderProxyMain plugin = ZanderProxyMain.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void PlayerOnProxyPing(ProxyPingEvent event) {
        ServerPing serverPing = event.getResponse();

        // GET request to fetch MOTD.
        Request req = Request.builder()
                .setURL(ConfigurationManager.getConfig().get("BaseAPIURL") + "/announcement/get?announcementType=motd")
                .setMethod(Request.Method.GET)
                .addHeader("x-access-token", String.valueOf(ConfigurationManager.getConfig().get("APIKey")))
                .build();

        Response res = req.execute();
        String json = res.getBody();

        String colourMessageFormat = JsonPath.read(json, "$.data[0].colourMessageFormat");

        serverPing.setDescriptionComponent(new TextComponent(ChatColor.translateAlternateColorCodes('&', colourMessageFormat)));
        event.setResponse(serverPing);
    }
}
