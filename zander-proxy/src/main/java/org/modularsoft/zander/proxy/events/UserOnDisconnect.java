package org.modularsoft.zander.proxy.events;

import org.modularsoft.zander.proxy.ConfigurationManager;
import org.modularsoft.zander.proxy.ZanderProxyMain;
import org.modularsoft.zander.proxy.model.session.SessionDestroy;
import io.github.ModularEnigma.Request;
import io.github.ModularEnigma.Response;
import org.modularsoft.zander.proxy.model.discord.DiscordLeave;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class UserOnDisconnect implements Listener {
    private ZanderProxyMain plugin = ZanderProxyMain.getInstance();

    @EventHandler
    public void UserDisconnectEvent (PlayerDisconnectEvent event) {
        ProxiedPlayer player = event.getPlayer();

        if (player.isConnected()) return;

        //
        // Destory Session API POST
        //
        SessionDestroy destroySession = SessionDestroy.builder()
                .uuid(player.getUniqueId())
                .build();

        Request destroySessionReq = Request.builder()
                .setURL(ConfigurationManager.getConfig().get("BaseAPIURL") + "/session/destroy")
                .setMethod(Request.Method.POST)
                .addHeader("x-access-token", String.valueOf(ConfigurationManager.getConfig().get("APIKey")))
                .setRequestBody(destroySession.toString())
                .build();

        Response destroySessionRes = destroySessionReq.execute();
        plugin.getProxy().getConsole().sendMessage(new TextComponent("Response (" + destroySessionRes.getStatusCode() + "): " + destroySessionRes.getBody()));

        //
        // Send Discord API POST for disconnect message
        //
        DiscordLeave leave = DiscordLeave.builder()
                .username(player.getDisplayName())
                .build();

        Request discordLeaveReq = Request.builder()
                .setURL(ConfigurationManager.getConfig().get("BaseAPIURL") + "/discord/leave")
                .setMethod(Request.Method.POST)
                .addHeader("x-access-token", String.valueOf(ConfigurationManager.getConfig().get("APIKey")))
                .setRequestBody(leave.toString())
                .build();

        Response discordLeaveRes = discordLeaveReq.execute();
        plugin.getProxy().getConsole().sendMessage(new TextComponent("Response (" + discordLeaveRes.getStatusCode() + "): " + discordLeaveRes.getBody()));

    }

}
