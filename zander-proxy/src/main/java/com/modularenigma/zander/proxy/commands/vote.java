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

public class vote extends Command {

    private ZanderProxyMain plugin = ZanderProxyMain.getInstance();

    public vote() {
        super("vote");
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if (commandSender instanceof ProxiedPlayer) {
            ProxiedPlayer player = (ProxiedPlayer) commandSender;

            // GET the Top Voter and vote count
            Request topVoterReq = Request.builder()
                    .setURL(ConfigurationManager.getConfig().get("BaseAPIURL") + "/vote/get")
                    .setMethod(Request.Method.GET)
                    .build();

            Response topVoterRes = topVoterReq.execute();
            String topVoterResJSON = topVoterRes.getBody().toJSONString();

            String voteUsername = JsonPath.read(topVoterResJSON, "$.data[0].username");
            String voteCount = JsonPath.read(topVoterResJSON, "$.data[0].votes");

            // GET the plugin prefix from configuration
            Request configReq = Request.builder()
                    .setURL(ConfigurationManager.getConfig().get("BaseAPIURL") + "/web/configuration")
                    .setMethod(Request.Method.GET)
                    .build();

            Response configRes = configReq.execute();
            String configResJSON = configRes.getBody().toJSONString();

            String siteAddress = JsonPath.read(configResJSON, "$.data.siteAddress");
            String siteName = JsonPath.read(configResJSON, "$.data.siteName");

            TextComponent message = new TextComponent(ChatColor.AQUA + "Help out " + siteName + " by voting on Minecraft Server lists! Check it out by " + ChatColor.BLUE + "clicking me!\nThe current Top Voter is " + voteUsername + " with " + voteCount + "votes.");
            message.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, siteAddress + "/vote"));
            player.sendMessage(message);
            return;
        }
    }

}
