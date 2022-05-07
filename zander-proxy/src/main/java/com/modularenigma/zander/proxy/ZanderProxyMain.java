package com.modularenigma.zander.proxy;

import com.modularenigma.zander.proxy.commands.ping;
import com.modularenigma.zander.proxy.commands.report;
import com.modularenigma.zander.proxy.events.UserChatEvent;
import com.modularenigma.zander.proxy.events.UserOnDisconnect;
import com.modularenigma.zander.proxy.events.UserOnLogin;
import com.modularenigma.zander.proxy.events.UserOnSwitch;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;

public class ZanderProxyMain extends Plugin implements Listener {
    private static ZanderProxyMain plugin;
    public static ConfigurationManager configurationManager;

    @Override
    public void onEnable() {
        setInstance(this);
        configurationManager.initConfig();

        // Init Message
        getProxy().getConsole().sendMessage(new TextComponent(ChatColor.GREEN + "\n\nZander Proxy has been enabled.\nRunning Version " + plugin.getDescription().getVersion() + "\nGitHub Repository: https://github.com/ModularEnigma/zander\nCreated by ModularEnigma\n\n"));

        // Command Registry
            getProxy().getPluginManager().registerCommand(this, new ping());
            getProxy().getPluginManager().registerCommand(this, new report());


//        getProxy().getPluginManager().registerCommand(this, new rules());
//        getProxy().getPluginManager().registerCommand(this, new discord());
//        getProxy().getPluginManager().registerCommand(this, new ranks());
//        getProxy().getPluginManager().registerCommand(this, new vote());
//        getProxy().getPluginManager().registerCommand(this, new guides());
//        getProxy().getPluginManager().registerCommand(this, new website());
//        getProxy().getPluginManager().registerCommand(this, new seen());
//        getProxy().getPluginManager().registerCommand(this, new playtime());

        // Event Registry
            getProxy().getPluginManager().registerListener(this, new UserChatEvent());
            getProxy().getPluginManager().registerListener(this, new UserOnLogin());
            getProxy().getPluginManager().registerListener(this, new UserOnDisconnect());
            getProxy().getPluginManager().registerListener(this, new UserOnSwitch());

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
