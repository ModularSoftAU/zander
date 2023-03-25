package org.modularsoft.zander.proxy;

import com.jayway.jsonpath.JsonPath;
import io.github.ModularEnigma.Request;
import io.github.ModularEnigma.Response;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import org.modularsoft.zander.proxy.commands.*;
import org.modularsoft.zander.proxy.events.*;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class ZanderProxyMain extends Plugin implements Listener {
    private static ZanderProxyMain plugin;
    public static ConfigurationManager configurationManager;

    @Override
    public void onEnable() {
        setInstance(this);
        configurationManager.initConfig();

        // Init Message
        getProxy().getConsole().sendMessage(new TextComponent(ChatColor.GREEN + "\n\nZander Proxy has been enabled.\nRunning Version " + plugin.getDescription().getVersion() + "\nGitHub Repository: https://github.com/ModularSoftAU/zander\nCreated by Modular Software\n\n"));

        // Command Registry
            getProxy().getPluginManager().registerCommand(this, new ping());
            getProxy().getPluginManager().registerCommand(this, new report());
            getProxy().getPluginManager().registerCommand(this, new rules());
            getProxy().getPluginManager().registerCommand(this, new discord());
            getProxy().getPluginManager().registerCommand(this, new guides());
            getProxy().getPluginManager().registerCommand(this, new ranks());
            getProxy().getPluginManager().registerCommand(this, new vote());
            getProxy().getPluginManager().registerCommand(this, new website());

        // Event Registry
            getProxy().getPluginManager().registerListener(this, new UserChatEvent());
            getProxy().getPluginManager().registerListener(this, new UserOnLogin());
            getProxy().getPluginManager().registerListener(this, new UserOnDisconnect());
            getProxy().getPluginManager().registerListener(this, new UserOnSwitch());
            getProxy().getPluginManager().registerListener(this, new UserOnVote());
            getProxy().getPluginManager().registerListener(this, new UserOnProxyPing());
            getProxy().getPluginManager().registerListener(this, new UserOnPunishment());
            getProxy().getPluginManager().registerListener(this, new UserOnPunishmentRevoke());

//        ProxyServer.getInstance().getScheduler().schedule(plugin, new Runnable() {
//            @Override
//            public void run() {
//                Random random = new Random();
//
//                // GET request to fetch tip.
//                Request tipReq = Request.builder()
//                        .setURL(ConfigurationManager.getConfig().get("BaseAPIURL") + "/announcement/get?announcementType=tip")
//                        .setMethod(Request.Method.GET)
//                        .addHeader("x-access-token", String.valueOf(ConfigurationManager.getConfig().get("APIKey")))
//                        .build();
//
//                Response tipRes = tipReq.execute();
//                String tipJson = tipRes.getBody();
//                String link = JsonPath.read(tipJson, "$.data[0].link");
//                String colourMessageFormat = JsonPath.read(tipJson, "$.data[0].colourMessageFormat");
//
//                TextComponent message = new TextComponent(ChatColor.translateAlternateColorCodes('&', "&8[&eTIP&8] " + colourMessageFormat));
//                message.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, link));
//                ProxyServer.getInstance().broadcast(message);
//            }
//        }, 2, 1, TimeUnit.MINUTES);

    }

    @Override
    public void onDisable() {

    }

    public static ZanderProxyMain getInstance() {
        return plugin;
    }

    private static void setInstance(ZanderProxyMain instance) {
        ZanderProxyMain.plugin = instance;
    }

}
