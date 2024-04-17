package org.modularsoft.zander.velocity.util.announcement;

import com.jayway.jsonpath.JsonPath;
import dev.dejvokep.boostedyaml.route.Route;
import io.github.ModularEnigma.Request;
import io.github.ModularEnigma.Response;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.modularsoft.zander.velocity.ZanderVelocityMain;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TipChatter {
    public static void startAnnouncementTipTask() {
        String BaseAPIURL = ZanderVelocityMain.getConfig().getString(Route.from("BaseAPIURL"));
        String APIKey = ZanderVelocityMain.getConfig().getString(Route.from("APIKey"));

        // Create a ScheduledExecutorService with a single thread
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        // Schedule the task to run every 10 seconds
        scheduler.scheduleAtFixedRate(() -> {
            try {
                // GET request to fetch Announcement Tip.
                Request req = Request.builder()
                        .setURL(BaseAPIURL + "/announcement/get?announcementType=tip")
                        .setMethod(Request.Method.GET)
                        .addHeader("x-access-token", APIKey)
                        .build();

                Response res = req.execute();
                String json = res.getBody();

                String colourMessageFormat = JsonPath.read(json, "$.data[0].colourMessageFormat");
                String link = JsonPath.read(json, "$.data[0].link");

                // Broadcast the message to all online players
                ZanderVelocityMain.getProxy().getAllPlayers().forEach(player -> {
                    // Send the message to each player
                    char translate = '&';
                    Component message = LegacyComponentSerializer.legacy(translate).deserialize(colourMessageFormat);

                    if (link != null && !link.isEmpty()) {
                        // Set the click event only if link is true
                        message.clickEvent(ClickEvent.clickEvent(ClickEvent.Action.OPEN_URL,link));
                    }
                    player.sendMessage(message);
                });
            } catch (Exception e) {
                // Handle exceptions here
                e.printStackTrace();
                System.out.println("Announcement Tip Failed, will try again in " + ZanderVelocityMain.getConfig().getString(Route.from("announcementTipInterval")) + " minutes.");
            }
        }, 0, ZanderVelocityMain.getConfig().getInt(Route.from("announcementTipInterval")), TimeUnit.MINUTES);
    }
}
