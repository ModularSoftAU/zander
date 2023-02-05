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

public class guides extends Command {

    private ZanderProxyMain plugin = ZanderProxyMain.getInstance();

    public guides() {
        super("guides");
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if (commandSender instanceof ProxiedPlayer) {
            ProxiedPlayer player = (ProxiedPlayer) commandSender;

            // GET request to link to guides/knowledgebase.
            Request req = Request.builder()
                    .setURL(ConfigurationManager.getConfig().get("BaseAPIURL") + "/web/configuration")
                    .setMethod(Request.Method.GET)
                    .build();

            Response res = req.execute();
            String json = res.getBody();
            String siteAddress = JsonPath.read(json, "$.data.siteAddress");

            TextComponent message = new TextComponent("Little stuck? Need some help? Our guides site and resources may be of assistance to you. Check it out here: " + ChatColor.GOLD + siteAddress + "/knowledgebase");
            message.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, siteAddress + "/knowledgebase"));
            player.sendMessage(message);
            return;
        }
    }

}
