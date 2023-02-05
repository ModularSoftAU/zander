package org.modularsoft.zander.proxy.commands;

import com.jayway.jsonpath.JsonPath;
import org.modularsoft.zander.proxy.ConfigurationManager;
import org.modularsoft.zander.proxy.ZanderProxyMain;
import io.github.ModularEnigma.Request;
import io.github.ModularEnigma.Response;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class ranks extends Command {

    private ZanderProxyMain plugin = ZanderProxyMain.getInstance();

    public ranks() {
        super("ranks");
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if (commandSender instanceof ProxiedPlayer) {
            ProxiedPlayer player = (ProxiedPlayer) commandSender;

            // GET request to link to ranks.
            Request req = Request.builder()
                    .setURL(ConfigurationManager.getConfig().get("BaseAPIURL") + "/web/configuration")
                    .setMethod(Request.Method.GET)
                    .addHeader("x-access-token", String.valueOf(ConfigurationManager.getConfig().get("APIKey")))
                    .build();

            Response res = req.execute();
            String json = res.getBody();
            String siteAddress = JsonPath.read(json, "$.data.siteAddress");

            TextComponent message = new TextComponent("Look at rank perks and purchase ranks at " + ChatColor.BLUE + siteAddress + "/ranks");
            message.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, siteAddress + "/ranks"));
            player.sendMessage(message);
            return;
        }
    }

}
