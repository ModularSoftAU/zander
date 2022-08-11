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

        // Check chat for blocked content
        try {
            PhraseFilter phrase = PhraseFilter.builder()
                    .content(event.getMessage().toString())
                    .build();

            Request phraseReq = Request.builder()
                    .setURL(ConfigurationManager.getConfig().get("BaseAPIURL") + "/filter")
                    .setMethod(Request.Method.POST)
                    .addHeader("x-access-token", String.valueOf(ConfigurationManager.getConfig().get("APIKey")))
                    .setRequestBody(phrase.toString())
                    .build();

            Response phraseRes = phraseReq.execute();
            String phraseJson = phraseRes.getBody();

//            String phraseCaught = JsonPath.read(phraseJson, "$.success");
//            String phraseCaughtMessage = JsonPath.read(phraseJson, "$.message");

//            if (phraseCaught == "false") {
//                player.sendMessage(new TextComponent(ChatColor.RED + phraseCaughtMessage));
//                event.setCancelled(true);
//                return;
//            }

            plugin.getProxy().getConsole().sendMessage(new TextComponent("[FILTER] Response (" + phraseRes.getStatusCode() + "): " + phraseRes.getBody()));

        } catch (Exception e) {
            player.sendMessage(new TextComponent(ChatColor.YELLOW + "The chat filter could not be reached at this time, there maybe an issue with the API."));
            System.out.println(e);
        }
    }

}
