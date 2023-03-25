package org.modularsoft.zander.proxy.events;

import io.github.ModularEnigma.Request;
import io.github.ModularEnigma.Response;
import me.leoko.advancedban.bungee.event.PunishmentEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;
import org.modularsoft.zander.proxy.ConfigurationManager;
import org.modularsoft.zander.proxy.ZanderProxyMain;
import org.modularsoft.zander.proxy.model.punishment.PunishmentIssue;

public class UserOnPunishmentRevoke implements Listener {
    private ZanderProxyMain plugin = ZanderProxyMain.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void PlayerOnPunishRevoke(PunishmentEvent event) {
        int id = event.getPunishment().getId();

        // POST request to provide
        PunishmentIssue punishmentDelete = PunishmentIssue.builder()
                .punishmentId(id)
                .build();

        Request punishmentIssueDeleteReq = Request.builder()
                .setURL(ConfigurationManager.getConfig().get("BaseAPIURL") + "/punishment/delete")
                .setMethod(Request.Method.POST)
                .addHeader("x-access-token", String.valueOf(ConfigurationManager.getConfig().get("APIKey")))
                .setRequestBody(punishmentDelete.toString())
                .build();

        Response punishmentIssueRes = punishmentIssueDeleteReq.execute();
        String json = punishmentIssueRes.getBody();

        plugin.getProxy().getConsole().sendMessage(new TextComponent("Response (" + punishmentIssueRes.getStatusCode() + "): " + punishmentIssueRes.getBody()));
    }
}
