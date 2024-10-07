package org.modularsoft.zander.velocity.commands;

import com.jayway.jsonpath.JsonPath;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import dev.dejvokep.boostedyaml.route.Route;
import io.github.ModularEnigma.Request;
import io.github.ModularEnigma.Response;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.modularsoft.zander.velocity.ZanderVelocityMain;
import org.modularsoft.zander.velocity.model.Report;

import java.util.ArrayList;
import java.util.List;

import static org.modularsoft.zander.velocity.ZanderVelocityMain.proxy;

public class report implements SimpleCommand {
    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();
        String BaseAPIURL = ZanderVelocityMain.getConfig().getString(Route.from("BaseAPIURL"));
        String APIKey = ZanderVelocityMain.getConfig().getString(Route.from("APIKey"));

        if (source instanceof Player) {
            Player player = (Player) source;

            if (args.length < 2) {
                player.sendMessage(Component.text("Usage: /report <username> <reason>").color(NamedTextColor.RED));
                return;
            }

            String reportedUsername = args[0];
            StringBuilder reasonBuilder = new StringBuilder();
            for (int i = 1; i < args.length; i++) {
                reasonBuilder.append(args[i]).append(" ");
            }
            String reason = reasonBuilder.toString().trim();

            try {
                // Build the report object
                Report report = Report.builder()
                        .reportPlatform("INGAME")
                        .reportedUser(reportedUsername)
                        .reporterUser(player.getUsername())
                        .reportReason(reason)
                        .build();

                // Create and send the request
                Request reportReq = Request.builder()
                        .setURL(BaseAPIURL + "/report/create")
                        .setMethod(Request.Method.POST)
                        .addHeader("x-access-token", APIKey)
                        .setRequestBody(report.toString())
                        .build();

                // Get the response from the API
                Response reportRes = reportReq.execute();
                String json = reportRes.getBody();
                Boolean success = JsonPath.read(json, "$.success");
                String message = JsonPath.read(json, "$.message");

                if (success) {
                    player.sendMessage(Component.text(message).color(NamedTextColor.GREEN));
                } else {
                    player.sendMessage(Component.text(message).color(NamedTextColor.RED));
                }

            } catch (Exception e) {
                player.sendMessage(Component.text("An error has occurred. Is the API down?").color(NamedTextColor.RED));
                System.out.println(e);
            }
        } else {
            source.sendMessage(Component.text("Only players can use this command.").color(NamedTextColor.RED));
        }
    }

    // Tab-completion method
    @Override
    public List<String> suggest(Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();
        List<String> completions = new ArrayList<>();

        if (args.length == 1 && source instanceof Player) {
            // Autocomplete the username (first argument)
            for (Player player : proxy.getAllPlayers()) {
                if (player.getUsername().toLowerCase().startsWith(args[0].toLowerCase())) {
                    completions.add(player.getUsername());
                }
            }
        }

        return completions;
    }
}