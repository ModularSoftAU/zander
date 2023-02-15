package org.modularsoft.zander.auth.events;

import com.jayway.jsonpath.JsonPath;
import org.modularsoft.zander.auth.ZanderAuthMain;
import org.modularsoft.zander.auth.model.VerificationCode;
import io.github.ModularEnigma.Request;
import io.github.ModularEnigma.Response;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class PlayerVerificationCodePull implements Listener {
    ZanderAuthMain plugin;
    public PlayerVerificationCodePull(ZanderAuthMain plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void VerificationCodeGeneration(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        String username = player.getName();

        try {
            //
            // Verification Code Generation API POST
            //
            VerificationCode codeGen = VerificationCode.builder()
                    .username(username)
                    .build();

            Request codeGenReq = Request.builder()
                    .setURL(plugin.getConfig().get("BaseAPIURL") + "/web/verify/get?username=" + username + "&platform=ingame")
                    .setMethod(Request.Method.GET)
                    .addHeader("x-access-token", String.valueOf(plugin.getConfig().get("APIKey")))
                    .setRequestBody(codeGen.toString())
                    .build();

            Response codeGenRes = codeGenReq.execute();
            plugin.getServer().getConsoleSender().sendMessage("Response (" + codeGenRes.getStatusCode() + "): " + codeGenRes.getBody());

            // Kick user with their code for verification
            int code = JsonPath.read(codeGenRes.getBody(), "$.code");
            String codeMessage = ChatColor.GREEN + "Verification Code \n" + ChatColor.WHITE + "Go back over to the registration form and use the provided code to verify your Minecraft Account.\n\n" + ChatColor.YELLOW + code;
            event.disallow(PlayerLoginEvent.Result.KICK_BANNED, codeMessage);
        } catch (Exception e) {
            event.disallow(PlayerLoginEvent.Result.KICK_BANNED, ChatColor.RED + "There was an error verifying. Not a valid registration or user.");
            System.out.println(e);
        }
    }
}
