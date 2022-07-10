package com.modularenigma.zander.proxy.commands;

import com.jayway.jsonpath.JsonPath;
import com.modularenigma.zander.proxy.ConfigurationManager;
import com.modularenigma.zander.proxy.ZanderProxyMain;
import io.github.ModularEnigma.Request;
import io.github.ModularEnigma.Response;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.List;

public class rules extends Command {

    private ZanderProxyMain plugin = ZanderProxyMain.getInstance();

    public rules() {
        super("rules");
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if (commandSender instanceof ProxiedPlayer) {
            ProxiedPlayer player = (ProxiedPlayer) commandSender;

            // GET request to link to rules.
            Request req = Request.builder()
                .setURL(ConfigurationManager.getConfig().get("BaseAPIURL") + "/web/configuration")
                .setMethod(Request.Method.GET)
                .addHeader("x-access-token", String.valueOf(ConfigurationManager.getConfig().get("APIKey")))
                .build();

            Response res = req.execute();
            String json = res.getBody();
            String siteAddress = JsonPath.read(json, "$.data.siteAddress");

            TextComponent message = new TextComponent("Please read and abide by the rules which you can find on our website here: " + ChatColor.RED + siteAddress + "/rules");
            message.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, siteAddress + "/rules"));
            player.sendMessage(message);
            return;
        }
    }

}
