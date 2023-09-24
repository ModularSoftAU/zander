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

public class UserOnPunishment implements Listener {
    private ZanderProxyMain plugin = ZanderProxyMain.getInstance();
    @EventHandler(priority = EventPriority.HIGHEST)
    public void PlayerOnPunish(PunishmentEvent event) {
        String punisheduser = event.getPunishment().getName();
        String punisher = event.getPunishment().getOperator();
        String reason = event.getPunishment().getReason();
        String type = event.getPunishment().getType().getName();
        int id = event.getPunishment().getId();

        // POST request to provide
        PunishmentIssue punishmentIssue = PunishmentIssue.builder()
                .punishmentId(id)
                .playerUsername(punisheduser)
                .staffUsername(punisher)
                .platform("SERVER")
                .type(type)
                .reason(reason)
                .build();

        Request punishmentIssueReq = Request.builder()
                .setURL(ConfigurationManager.getConfig().get("BaseAPIURL") + "/punishment/issue")
                .setMethod(Request.Method.POST)
                .addHeader("x-access-token", String.valueOf(ConfigurationManager.getConfig().get("APIKey")))
                .setRequestBody(punishmentIssue.toString())
                .build();

        Response punishmentIssueRes = punishmentIssueReq.execute();
        String json = punishmentIssueRes.getBody();

        plugin.getProxy().getConsole().sendMessage(new TextComponent("Response (" + punishmentIssueRes.getStatusCode() + "): " + punishmentIssueRes.getBody()));
    }
}
