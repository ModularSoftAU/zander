package org.modularsoft.zander.velocity.events;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.proxy.Player;
import dev.dejvokep.boostedyaml.route.Route;
import io.github.ModularEnigma.Request;
import io.github.ModularEnigma.Response;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.modularsoft.zander.velocity.ZanderVelocityMain;
import org.modularsoft.zander.velocity.model.discord.spy.DiscordCommandSpy;

public class UserCommandSpyEvent {

    @Subscribe
    public void UserChatEvent(PlayerChatEvent event) {
        Player player = event.getPlayer();
        String BaseAPIURL = ZanderVelocityMain.getConfig().getString(Route.from("BaseAPIURL"));
        String APIKey = ZanderVelocityMain.getConfig().getString(Route.from("APIKey"));
        String command = event.getMessage().substring(1); // Remove the leading slash

        if (event.getMessage().startsWith("/")) {
            // Excludes specific commands from being logged in both spy events.
            if (command.startsWith("msg") || command.startsWith("tell") || command.startsWith("w") || command.startsWith("message") || command.startsWith("r")) {
                return;
            }

            //
            // Command Spy API POST
            //
            try {
                DiscordCommandSpy commandSpy = DiscordCommandSpy.builder()
                        .username(player.getUsername())
                        .command(command)
                        .server(player.getCurrentServer().get().getServer().getServerInfo().getName())
                        .build();

                Request commandSpyReq = Request.builder()
                        .setURL(BaseAPIURL + "/discord/spy/command")
                        .setMethod(Request.Method.POST)
                        .addHeader("x-access-token", String.valueOf(APIKey))
                        .setRequestBody(commandSpy.toString())
                        .build();

                Response commandSpyRes = commandSpyReq.execute();
                ZanderVelocityMain.getLogger().info("Response (" + commandSpyRes.getStatusCode() + "): " + commandSpyRes.getBody());
            } catch (Exception e) {
                Component builder = Component.text("An error has occurred. Is the API down?").color(NamedTextColor.RED);
                player.disconnect(builder);
                System.out.println(e);
            }
        }
    }

}
