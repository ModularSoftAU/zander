package com.modularenigma.zander.proxy.events;

import com.modularenigma.zander.proxy.ZanderProxyMain;
import com.modularenigma.zander.proxy.api.Request;
import com.modularenigma.zander.proxy.api.Response;
import com.modularenigma.zander.proxy.model.discord.DiscordChat;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class UserChatEvent implements Listener {
    private ZanderProxyMain plugin = ZanderProxyMain.getInstance();

    @EventHandler
    public void UserChatEvent(ChatEvent event) {
        ProxiedPlayer player = (ProxiedPlayer) event.getSender();

        DiscordChat chat = DiscordChat.builder()
                .username(player.getDisplayName())
                .server(player.getServer().getInfo().getName())
                .content(event.getMessage())
                .build();

        // Send a log
        plugin.getProxy().getConsole().sendMessage(new TextComponent(player.getDisplayName() + ": " + player.getServer().getInfo().getName()));

        Request req = Request.builder()
                .setURL("http://localhost:8080/api/discord/chat")
                .setMethod(Request.Method.POST)
                .setRequestBody(chat.toString())
                .build();

        Response res = req.execute();
        plugin.getProxy().getConsole().sendMessage(new TextComponent("Response (" + res.getStatusCode() + "): " + res.getBody().toJSONString()));

    }

}
