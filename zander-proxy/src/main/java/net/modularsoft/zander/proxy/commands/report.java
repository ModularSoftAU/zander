package net.modularsoft.zander.proxy.commands;

import com.google.common.collect.ImmutableSet;
import net.modularsoft.zander.proxy.ConfigurationManager;
import net.modularsoft.zander.proxy.ZanderProxyMain;
import io.github.ModularEnigma.Request;
import io.github.ModularEnigma.Response;
import net.modularsoft.zander.proxy.model.report.ReportCreate;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.HashSet;
import java.util.Set;

public class report extends Command implements TabExecutor {

    public report() {
        super("report");
    }
    private ZanderProxyMain plugin = ZanderProxyMain.getInstance();

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if (commandSender instanceof ProxiedPlayer) {
            ProxiedPlayer player = (ProxiedPlayer) commandSender;

            try {
                if (strings.length == 0) {
                    player.sendMessage(new TextComponent(ChatColor.RED + "Please specify a player to report and the reason for it."));
                    return;
                } else if (strings.length == 1) {
                    player.sendMessage(new TextComponent(ChatColor.RED + "Please specify a reason for this report."));
                    return;
                } else {
                    ProxiedPlayer target = ProxyServer.getInstance().getPlayer(strings[0]);
                    String targetsServer = target.getServer().getInfo().getName();
                    String targetServerName = targetsServer.substring(0,1).toUpperCase() + targetsServer.substring(1).toLowerCase();

                    player.sendMessage(new TextComponent(ChatColor.GREEN + "Your report has been sent to the Server Staff."));

                    StringBuilder str = new StringBuilder();
                    for (int i = 1; i < strings.length; i++) {
                        str.append(strings[i] + " ");
                    }
                    String reportReason = str.toString().trim();

                    // For all people that have zander.report.notify, send them the report.
                    for (ProxiedPlayer pspp : ProxyServer.getInstance().getPlayers()) {
                        if (pspp.hasPermission("zander.report.notify")) {
                            pspp.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', ChatColor.GOLD + "============================================\n" + ChatColor.YELLOW + ChatColor.BOLD + "INCOMING REPORT\n" + player.getName() + " reported " + target.getDisplayName() + " on " + targetServerName + "\n" + ChatColor.GRAY + str.toString().trim() + "\n" + ChatColor.GOLD + "============================================")));
                        }
                    }

                    ReportCreate report = ReportCreate.builder()
                            .reportedUser(target.getDisplayName())
                            .reporterUser(player.getDisplayName())
                            .reason(reportReason)
                            .platform("SERVER")
                            .server(targetServerName)
                            .build();

                    Request req = Request.builder()
                            .setURL(ConfigurationManager.getConfig().get("BaseAPIURL") + "/report/create")
                            .setMethod(Request.Method.POST)
                            .addHeader("x-access-token", String.valueOf(ConfigurationManager.getConfig().get("APIKey")))
                            .setRequestBody(report.toString())
                            .build();

                    Response res = req.execute();
                    plugin.getProxy().getConsole().sendMessage(new TextComponent("Response (" + res.getStatusCode() + "): " + res.getBody()));

                }
                return;
            } catch (Exception e) {
                player.sendMessage(new TextComponent("An error has occurred. Is the API down?"));
                System.out.println(e);
            }

        }
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender commandSender, String[] args) {
        if (args.length > 2 || args.length == 0) {
            return ImmutableSet.of();
        }

        Set<String> matches = new HashSet<>();
        if (args.length == 1) {
            String search = args[0].toLowerCase();
            for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
                if (player.getName().toLowerCase().startsWith(search)) {
                    matches.add(player.getName());
                }
            }
        }
        return matches;
    }

}
