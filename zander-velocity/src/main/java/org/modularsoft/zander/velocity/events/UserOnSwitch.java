package org.modularsoft.zander.velocity.events;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import com.velocitypowered.api.proxy.Player;
import dev.dejvokep.boostedyaml.route.Route;
import io.github.ModularEnigma.Request;
import io.github.ModularEnigma.Response;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.modularsoft.zander.velocity.ZanderVelocityMain;
import org.modularsoft.zander.velocity.model.discord.DiscordSwitch;
import org.modularsoft.zander.velocity.model.session.SessionSwitch;

import java.util.UUID;

public class UserOnSwitch {
    @Subscribe
    public void onServerConnect(ServerConnectedEvent event) {
        Player player = event.getPlayer();
        String username = player.getUsername();
        UUID playerUUID = player.getUniqueId();
        String server = event.getServer().getServerInfo().getName();
        String BaseAPIURL = ZanderVelocityMain.getConfig().getString(Route.from("BaseAPIURL"));
        String APIKey = ZanderVelocityMain.getConfig().getString(Route.from("APIKey"));

        try {
            //
            // Switch Session API POST
            //
            SessionSwitch switchSession = SessionSwitch.builder()
                    .uuid(playerUUID)
                    .server(server)
                    .build();

            Request switchSessionReq = Request.builder()
                    .setURL(BaseAPIURL + "/session/switch")
                    .setMethod(Request.Method.POST)
                    .addHeader("x-access-token", APIKey)
                    .setRequestBody(switchSession.toString())
                    .build();

            Response switchSessionRes = switchSessionReq.execute();
            ZanderVelocityMain.getLogger().info("Response (" + switchSessionRes.getStatusCode() + "): " + switchSessionRes.getBody());
        } catch (Exception e) {
            Component builder = Component.text("An error has occurred. Is the API down?").color(NamedTextColor.RED);
            player.disconnect(builder);
            System.out.println(e);
        }

        try {
            //
            // Discord Switch API POST
            //
            DiscordSwitch discordSwitch = DiscordSwitch.builder()
                    .username(username)
                    .server(server)
                    .build();

            Request discordSwitchReq = Request.builder()
                    .setURL(BaseAPIURL + "/discord/switch")
                    .setMethod(Request.Method.POST)
                    .addHeader("x-access-token", APIKey)
                    .setRequestBody(discordSwitch.toString())
                    .build();

            Response discordSwitchRes = discordSwitchReq.execute();
            ZanderVelocityMain.getLogger().info("Response (" + discordSwitchRes.getStatusCode() + "): " + discordSwitchRes.getBody());
        } catch (Exception e) {
            Component builder = Component.text("An error has occurred. Is the API down?").color(NamedTextColor.RED);
            player.disconnect(builder);
            System.out.println(e);
        }
    }
}
