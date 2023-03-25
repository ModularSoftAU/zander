package org.modularsoft.zander.proxy.events;

import com.jayway.jsonpath.JsonPath;
import org.modularsoft.zander.proxy.ConfigurationManager;
import org.modularsoft.zander.proxy.ZanderProxyMain;
import org.modularsoft.zander.proxy.model.vote.VoteCast;
import com.vexsoftware.votifier.bungee.events.VotifierEvent;
import io.github.ModularEnigma.Request;
import io.github.ModularEnigma.Response;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class UserOnVote implements Listener {
    private ZanderProxyMain plugin = ZanderProxyMain.getInstance();

    @EventHandler
    public void PlayerOnVote(VotifierEvent event) {
        String player = event.getVote().getUsername();
        String voteSite = event.getVote().getServiceName();

        try {
            // POST to cast vote
            VoteCast cast = VoteCast.builder()
                    .username(player)
                    .voteSite(voteSite)
                    .build();

            Request voteCastReq = Request.builder()
                    .setURL(ConfigurationManager.getConfig().get("BaseAPIURL") + "/vote/cast")
                    .setMethod(Request.Method.POST)
                    .addHeader("x-access-token", String.valueOf(ConfigurationManager.getConfig().get("APIKey")))
                    .setRequestBody(cast.toString())
                    .build();

            Response voteCastRes = voteCastReq.execute();
            plugin.getProxy().getConsole().sendMessage(new TextComponent("Response (" + voteCastRes.getStatusCode() + "): " + voteCastRes.getBody()));

            // GET the plugin prefix from configuration
            Request configReq = Request.builder()
                    .setURL(ConfigurationManager.getConfig().get("BaseAPIURL") + "/web/configuration")
                    .setMethod(Request.Method.GET)
                    .addHeader("x-access-token", String.valueOf(ConfigurationManager.getConfig().get("APIKey")))
                    .build();

            Response configRes = configReq.execute();
            String configResJSON = configRes.getBody();

            String prefix = JsonPath.read(configResJSON, "$.data.server.prefix");
            String siteAddress = JsonPath.read(configResJSON, "$.data.siteAddress");

            TextComponent message = new TextComponent(ChatColor.translateAlternateColorCodes('&', prefix + " " + player + " voted from " + ChatColor.AQUA + voteSite + ChatColor.RESET + ". You can too using clicking me!"));
            message.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, siteAddress + "/vote"));
            ProxyServer.getInstance().broadcast(message); // Broadcast to all Servers
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
