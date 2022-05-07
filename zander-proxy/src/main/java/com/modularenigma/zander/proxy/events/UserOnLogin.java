package com.modularenigma.zander.proxy.events;

import com.modularenigma.zander.proxy.ConfigurationManager;
import com.modularenigma.zander.proxy.ZanderProxyMain;
import com.modularenigma.zander.proxy.api.Request;
import com.modularenigma.zander.proxy.api.Response;
import com.modularenigma.zander.proxy.model.discord.DiscordChat;
import com.modularenigma.zander.proxy.model.discord.DiscordJoin;
import com.modularenigma.zander.proxy.model.session.SessionCreate;
import com.modularenigma.zander.proxy.model.user.UserCreation;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class UserOnLogin implements Listener {
    private ZanderProxyMain plugin = ZanderProxyMain.getInstance();

    @EventHandler
    public void UserLoginEvent (PostLoginEvent event) {
        ProxiedPlayer player = event.getPlayer();

        // Send User Creation API POST for new user
        UserCreation createUser = UserCreation.builder()
                .uuid(player.getUniqueId())
                .username(player.getDisplayName())
                .build();

        Request createUserReq = Request.builder()
                .setURL(ConfigurationManager.getConfig().get("BaseAPIURL") + "/user/new")
                .setMethod(Request.Method.POST)
                .setRequestBody(createUser.toString())
                .build();

        Response createUserRes = createUserReq.execute();
        plugin.getProxy().getConsole().sendMessage(new TextComponent("Response (" + createUserRes.getStatusCode() + "): " + createUserRes.getBody().toJSONString()));


        // Start Session API POST
        // Commented out till SQL is clarified.

//        SessionCreate createSession = SessionCreate.builder()
//                .uuid(player.getUniqueId())
//                .ipAddress(player.getAddress().toString())
//                .server(event.getPlayer().getServer().getInfo().getName())
//                .build();
//
//        Request createSessionReq = Request.builder()
//                .setURL(ConfigurationManager.getConfig().get("BaseAPIURL") + "/session/create")
//                .setMethod(Request.Method.POST)
//                .setRequestBody(createSession.toString())
//                .build();
//
//        Response createSessionRes = createSessionReq.execute();
//        plugin.getProxy().getConsole().sendMessage(new TextComponent("Response (" + createSessionRes.getStatusCode() + "): " + createSessionRes.getBody().toJSONString()));

        // Send Discord API POST for join message
        DiscordJoin join = DiscordJoin.builder()
                .username(player.getDisplayName())
                .build();

        Request discordJoinReq = Request.builder()
                .setURL(ConfigurationManager.getConfig().get("BaseAPIURL") + "/discord/join")
                .setMethod(Request.Method.POST)
                .setRequestBody(join.toString())
                .build();

        Response discordJoinRes = discordJoinReq.execute();
        plugin.getProxy().getConsole().sendMessage(new TextComponent("Response (" + discordJoinRes.getStatusCode() + "): " + discordJoinRes.getBody().toJSONString()));

    }

}
