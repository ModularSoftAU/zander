package com.modularenigma.zander.proxy;

import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;

public class ZanderProxyMain extends Plugin implements Listener {
    private static ZanderProxyMain plugin;

    @Override
    public void onEnable() {

        // Command Registry
        getProxy().getPluginManager().registerCommand(this, new ping());
//        getProxy().getPluginManager().registerCommand(this, new rules());
//        getProxy().getPluginManager().registerCommand(this, new discord());
//        getProxy().getPluginManager().registerCommand(this, new ranks());
//        getProxy().getPluginManager().registerCommand(this, new report());
//        getProxy().getPluginManager().registerCommand(this, new vote());
//        getProxy().getPluginManager().registerCommand(this, new guides());
//        getProxy().getPluginManager().registerCommand(this, new website());
//        getProxy().getPluginManager().registerCommand(this, new seen());
//        getProxy().getPluginManager().registerCommand(this, new playtime());

    }

    @Override
    public void onDisable() {

    }

}
