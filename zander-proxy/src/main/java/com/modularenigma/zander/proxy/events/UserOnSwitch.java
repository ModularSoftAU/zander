package com.modularenigma.zander.proxy.events;

import com.modularenigma.zander.proxy.ConfigurationManager;
import com.modularenigma.zander.proxy.ZanderProxyMain;
import com.modularenigma.zander.proxy.api.Request;
import com.modularenigma.zander.proxy.api.Response;
import com.modularenigma.zander.proxy.model.discord.DiscordJoin;
import com.modularenigma.zander.proxy.model.discord.DiscordSwitch;
import com.modularenigma.zander.proxy.model.session.SessionDestroy;
import com.modularenigma.zander.proxy.model.session.SessionSwitch;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class UserOnSwitch implements Listener {
    private ZanderProxyMain plugin = ZanderProxyMain.getInstance();

    @EventHandler
    public void UserSwitchEvent (ServerSwitchEvent event) {
        ProxiedPlayer player = event.getPlayer();

        // Switch Session API POST
        // Commented out till SQL is clarrified.

//        SessionSwitch switchSession = SessionSwitch.builder()
//                .uuid(player.getUniqueId())
//                .build();
//
//        Request switchSessionReq = Request.builder()
//                .setURL(ConfigurationManager.getConfig().get("BaseAPIURL") + "/session/switch")
//                .setMethod(Request.Method.POST)
//                .setRequestBody(switchSession.toString())
//                .build();
//
//        Response switchSessionRes = switchSessionReq.execute();
//        plugin.getProxy().getConsole().sendMessage(new TextComponent("Response (" + switchSessionRes.getStatusCode() + "): " + switchSessionRes.getBody().toJSONString()));

        // Server Switch event throws an error when user has not switched from anywhere.

//        DiscordSwitch join = DiscordSwitch.builder()
//                .username(player.getDisplayName())
//                .server(event.getFrom().getName())
//                .build();
//
//        Request req = Request.builder()
//                .setURL(ConfigurationManager.getConfig().get("BaseAPIURL") + "/discord/switch")
//                .setMethod(Request.Method.POST)
//                .setRequestBody(join.toString())
//                .build();
//
//        Response res = req.execute();
//        plugin.getProxy().getConsole().sendMessage(new TextComponent("Response (" + res.getStatusCode() + "): " + res.getBody().toJSONString()));

    }

}
