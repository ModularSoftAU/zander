package org.modularsoft.zander.velocity.commands;

import com.jayway.jsonpath.JsonPath;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import dev.dejvokep.boostedyaml.route.Route;
import io.github.ModularEnigma.Request;
import io.github.ModularEnigma.Response;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.modularsoft.zander.velocity.ZanderVelocityMain;

public class website implements SimpleCommand {
    @Override
    public void execute(final Invocation invocation) {
        CommandSource source = invocation.source();
        String BaseAPIURL = ZanderVelocityMain.getConfig().getString(Route.from("BaseAPIURL"));
        String APIKey = ZanderVelocityMain.getConfig().getString(Route.from("APIKey"));

        try {
            // GET request to link to discord.
            Request req = Request.builder()
                    .setURL(BaseAPIURL + "/web/configuration")
                    .setMethod(Request.Method.GET)
                    .addHeader("x-access-token", APIKey)
                    .build();

            Response res = req.execute();
            String json = res.getBody();
            String siteAddress = JsonPath.read(json, "$.data.siteAddress");

            Component message = Component.text("For all information regarding the Network, visit our website: " + siteAddress)
                    .color(NamedTextColor.YELLOW);
            message = message.clickEvent(ClickEvent.clickEvent(ClickEvent.Action.OPEN_URL,siteAddress));
            source.sendMessage(message);
        } catch (Exception e) {
            Component builder = Component.text("An error has occurred. Is the API down?").color(NamedTextColor.RED);
            source.sendMessage(builder);
            System.out.println(e);
        }
    }
}
