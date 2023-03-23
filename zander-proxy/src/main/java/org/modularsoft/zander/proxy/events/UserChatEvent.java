package org.modularsoft.zander.proxy.events;

import org.modularsoft.zander.proxy.ConfigurationManager;
import org.modularsoft.zander.proxy.ZanderProxyMain;
import io.github.ModularEnigma.Request;
import io.github.ModularEnigma.Response;
import org.modularsoft.zander.proxy.model.discord.DiscordChat;
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

        if (event.getMessage().startsWith("/")) return;

        DiscordChat chat = DiscordChat.builder()
                .username(player.getDisplayName())
                .server(player.getServer().getInfo().getName())
                .content(event.getMessage())
                .build();

        Request req = Request.builder()
                .setURL(ConfigurationManager.getConfig().get("BaseAPIURL") + "/discord/chat")
                .setMethod(Request.Method.POST)
                .addHeader("x-access-token", String.valueOf(ConfigurationManager.getConfig().get("APIKey")))
                .setRequestBody(chat.toString())
                .build();

        Response res = req.execute();
        plugin.getProxy().getConsole().sendMessage(new TextComponent("Response (" + res.getStatusCode() + "): " + res.getBody()));

    }

}
