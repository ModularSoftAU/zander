package com.modularenigma.zander.proxy.commands;

import com.modularenigma.zander.proxy.ZanderProxyMain;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class ranks extends Command {

    private ZanderProxyMain plugin = ZanderProxyMain.getInstance();

    public ranks() {
        super("ranks");
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if (commandSender instanceof ProxiedPlayer) {
            ProxiedPlayer player = (ProxiedPlayer) commandSender;

            // GET request to link to ranks.

            TextComponent message = new TextComponent("Look at rank perks and purchase ranks at " + ChatColor.BLUE + "ranks" + ChatColor.RESET);
            message.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "ranks"));
            player.sendMessage(message);
            return;
        }
    }

}
