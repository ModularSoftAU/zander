package org.modularsoft.zander.velocity.events;

import com.jayway.jsonpath.JsonPath;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyPingEvent;
import com.velocitypowered.api.proxy.server.ServerPing;
import dev.dejvokep.boostedyaml.route.Route;
import io.github.ModularEnigma.Request;
import io.github.ModularEnigma.Response;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.modularsoft.zander.velocity.ZanderVelocityMain;

public class UserOnProxyPing {

    @Subscribe
    public void onProxyPingEvent(ProxyPingEvent event) {
        try {
            // ServerPing serverPing = event.getPing();
            String BaseAPIURL = ZanderVelocityMain.getConfig().getString(Route.from("BaseAPIURL"));
            String APIKey = ZanderVelocityMain.getConfig().getString(Route.from("APIKey"));

            // GET request to fetch MOTD.
            Request req = Request.builder()
                    .setURL(BaseAPIURL + "/announcement/get?announcementType=motd")
                    .setMethod(Request.Method.GET)
                    .addHeader("x-access-token", APIKey)
                    .build();

            Response res = req.execute();
            String json = res.getBody();

            String colourMessageFormat = JsonPath.read(json, "$.data[0].colourMessageFormat");
            String motdTopLine = ZanderVelocityMain.getConfig().getString(Route.from("announcementMOTDTopLine"));
            Component serverPingDescription = LegacyComponentSerializer.builder()
                    .character('&')
                    .build()
                    .deserialize(motdTopLine + "\n" + colourMessageFormat);

            ServerPing newServerPing = ServerPing.builder()
                    .description(serverPingDescription)
                    .build();

            event.setPing(newServerPing);
        } catch (Exception e) {
            System.out.print(e);
            // ServerPing serverPing = event.getPing();

            String motdTopLine = ZanderVelocityMain.getConfig().getString(Route.from("announcementMOTDTopLine"));
            Component serverPingDescription = LegacyComponentSerializer.builder()
                    .character('&')
                    .build()
                    .deserialize(motdTopLine + "\n" + "&3&lPowered by Zander");

            ServerPing newServerPing = ServerPing.builder()
                    .description(serverPingDescription)
                    .build();

            event.setPing(newServerPing);
        }
    }
}
