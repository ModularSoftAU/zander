package org.modularsoft.zander.proxy.commands;

import com.jayway.jsonpath.JsonPath;
import io.github.ModularEnigma.Request;
import io.github.ModularEnigma.Response;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;
import org.modularsoft.zander.proxy.ConfigurationManager;
import org.modularsoft.zander.proxy.ZanderProxyMain;
import org.modularsoft.zander.proxy.model.Report;

import java.util.ArrayList;
import java.util.List;

public class report extends Command implements TabExecutor {

    private ZanderProxyMain plugin = ZanderProxyMain.getInstance();

    public report() {
        super("report");
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if (commandSender instanceof ProxiedPlayer) {
            ProxiedPlayer player = (ProxiedPlayer) commandSender;

            if (strings.length < 2) {
                player.sendMessage(new TextComponent(ChatColor.RED + "Usage: /report <username> <reason>"));
                return;
            }

            String reportedUsername = strings[0];
            StringBuilder reasonBuilder = new StringBuilder();
            for (int i = 1; i < strings.length; i++) {
                reasonBuilder.append(strings[i]).append(" ");
            }
            String reason = reasonBuilder.toString().trim();

            try {
                Report report = Report.builder()
                        .reportPlatform("INGAME")
                        .reportedUser(reportedUsername)
                        .reporterUser(player.getDisplayName())
                        .reportReason(reason)
                        .build();

                Request reportReq = Request.builder()
                        .setURL(ConfigurationManager.getConfig().get("BaseAPIURL") + "/report/create")
                        .setMethod(Request.Method.POST)
                        .addHeader("x-access-token", String.valueOf(ConfigurationManager.getConfig().get("APIKey")))
                        .setRequestBody(report.toString())
                        .build();

                Response reportRes = reportReq.execute();
                String json = reportRes.getBody();
                Boolean success = JsonPath.read(json, "$.success");
                String message = JsonPath.read(json, "$.message");

                if (success) {
                    player.sendMessage(new TextComponent(ChatColor.GREEN + message));
                } else {
                    player.sendMessage(new TextComponent(ChatColor.RED + message));
                }

            } catch (Exception e) {
                player.sendMessage(new TextComponent("An error has occurred. Is the API down?"));
                System.out.println(e);
            }
        }
    }

    // Tab-completion method
    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1 && sender instanceof ProxiedPlayer) {
            // Autocomplete the username (first argument)
            for (ProxiedPlayer player : plugin.getProxy().getPlayers()) {
                if (player.getName().toLowerCase().startsWith(args[0].toLowerCase())) {
                    completions.add(player.getName());
                }
            }
        }
        return completions;
    }
}