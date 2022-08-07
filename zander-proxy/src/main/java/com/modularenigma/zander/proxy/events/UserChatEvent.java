package com.modularenigma.zander.proxy.events;

import com.jayway.jsonpath.JsonPath;
import com.modularenigma.zander.proxy.ConfigurationManager;
import com.modularenigma.zander.proxy.ZanderProxyMain;
import com.modularenigma.zander.proxy.model.filter.PhraseFilter;
import io.github.ModularEnigma.Request;
import io.github.ModularEnigma.Response;
import com.modularenigma.zander.proxy.model.discord.DiscordChat;
import net.md_5.bungee.api.ChatColor;
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

        // Check filter for blocked phrases
        try {
            PhraseFilter phrase = PhraseFilter.builder()
                    .content(event.getMessage().toString())
                    .build();

            Request phraseReq = Request.builder()
                    .setURL(ConfigurationManager.getConfig().get("BaseAPIURL") + "/filter/phrase")
                    .setMethod(Request.Method.POST)
                    .addHeader("x-access-token", String.valueOf(ConfigurationManager.getConfig().get("APIKey")))
                    .setRequestBody(phrase.toString())
                    .build();

            Response phraseRes = phraseReq.execute();
            String phraseJson = phraseRes.getBody();

            System.out.println(phraseJson);
            System.out.println((String) JsonPath.read(phraseJson, "$.data[0].success"));

            String phraseCaught = JsonPath.read(phraseJson, "$.data.success");
            String phraseCaughtMessage = JsonPath.read(phraseJson, "$.data.message");

            if (phraseCaught == "false") {
                player.sendMessage(new TextComponent(ChatColor.RED + phraseCaughtMessage));
                event.setCancelled(true);
                return;
            }

            plugin.getProxy().getConsole().sendMessage(new TextComponent("[FILTER] Response (" + phraseRes.getStatusCode() + "): " + phraseRes.getBody()));

        } catch (Exception e) {
            System.out.println(e);
        }

        // Check filter for advertisement links
        try {

        } catch (Exception e) {
            System.out.println(e);
        }

        // Send message to Discord to logs
        if (event.getMessage().startsWith("/")) return;

        try {
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
        } catch (Exception e){
            System.out.println(e);
            return;
        }

    }

}
