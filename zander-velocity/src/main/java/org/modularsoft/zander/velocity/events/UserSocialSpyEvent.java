package org.modularsoft.zander.velocity.events;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ServerConnection;
import dev.dejvokep.boostedyaml.route.Route;
import io.github.ModularEnigma.Request;
import io.github.ModularEnigma.Response;
import net.kyori.adventure.text.Component;
import org.modularsoft.zander.velocity.ZanderVelocityMain;
import org.modularsoft.zander.velocity.model.discord.spy.DiscordSocialSpy;

import java.util.Arrays;

public class UserSocialSpyEvent {

    @Subscribe
    public void onUserChatEvent(PlayerChatEvent event) {
        Player player = event.getPlayer();
        String command = event.getMessage().substring(1); // Remove the leading slash

        if (command.startsWith("msg") || command.startsWith("tell") || command.startsWith("w") || command.startsWith("message") || command.startsWith("r")) {
            String[] messageParts = event.getMessage().split(" ");
            String targetPlayer = messageParts[1];
            String directMessage = String.join(" ", Arrays.copyOfRange(messageParts, 2, messageParts.length));

            //
            // Social Spy API POST
            //
            try {
                if (player.getCurrentServer().isEmpty()){
                    return;
                }

                ServerConnection currentServer = player.getCurrentServer().get();
                DiscordSocialSpy socialSpy = DiscordSocialSpy.builder()
                        .usernameFrom(player.getUsername())
                        .usernameTo(targetPlayer)
                        .directMessage(directMessage)
                        .server(currentServer.getServerInfo().getName())
                        .build();

                String BaseAPIURL = ZanderVelocityMain.getConfig().getString(Route.from("BaseAPIURL"));
                String APIKey = ZanderVelocityMain.getConfig().getString(Route.from("APIKey"));

                Request socialSpyReq = Request.builder()
                        .setURL(BaseAPIURL + "/discord/spy/directMessage")
                        .setMethod(Request.Method.POST)
                        .addHeader("x-access-token", APIKey)
                        .setRequestBody(socialSpy.toString())
                        .build();

                Response socialSpyRes = socialSpyReq.execute();
                ZanderVelocityMain.getLogger().info("Response (" + socialSpyRes.getStatusCode() + "): " + socialSpyRes.getBody());
            } catch (Exception e) {
                player.disconnect(Component.text("An error has occurred. Is the API down?"));
                System.out.println(e);
            }
        }
    }
}