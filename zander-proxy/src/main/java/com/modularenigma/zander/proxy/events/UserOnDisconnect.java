package com.modularenigma.zander.proxy.events;

import com.modularenigma.zander.proxy.ConfigurationManager;
import com.modularenigma.zander.proxy.ZanderProxyMain;
import com.modularenigma.zander.proxy.api.Request;
import com.modularenigma.zander.proxy.api.Response;
import com.modularenigma.zander.proxy.model.discord.DiscordJoin;
import com.modularenigma.zander.proxy.model.discord.DiscordLeave;
import com.modularenigma.zander.proxy.model.session.SessionCreate;
import com.modularenigma.zander.proxy.model.session.SessionDestroy;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class UserOnDisconnect implements Listener {
    private ZanderProxyMain plugin = ZanderProxyMain.getInstance();

    @EventHandler
    public void UserDisconnectEvent (PlayerDisconnectEvent event) {
        ProxiedPlayer player = event.getPlayer();

        if (player.isConnected()) return;

        // Destory Session API POST
        // Commented out till SQL is clarrified.

//        SessionDestroy destroySession = SessionDestroy.builder()
//                .uuid(player.getUniqueId())
//                .build();
//
//        Request destroySessionReq = Request.builder()
//                .setURL(ConfigurationManager.getConfig().get("BaseAPIURL") + "/session/destroy")
//                .setMethod(Request.Method.POST)
//                .setRequestBody(destroySession.toString())
//                .build();
//
//        Response destroySessionRes = destroySessionReq.execute();
//        plugin.getProxy().getConsole().sendMessage(new TextComponent("Response (" + destroySessionRes.getStatusCode() + "): " + destroySessionRes.getBody().toJSONString()));

        // Send Discord API POST for join message
        DiscordLeave leave = DiscordLeave.builder()
                .username(player.getDisplayName())
                .build();

        Request discordLeaveReq = Request.builder()
                .setURL(ConfigurationManager.getConfig().get("BaseAPIURL") + "/discord/leave")
                .setMethod(Request.Method.POST)
                .setRequestBody(leave.toString())
                .build();

        Response discordLeaveRes = discordLeaveReq.execute();
        plugin.getProxy().getConsole().sendMessage(new TextComponent("Response (" + discordLeaveRes.getStatusCode() + "): " + discordLeaveRes.getBody().toJSONString()));

    }

}
