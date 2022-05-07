package com.modularenigma.zander.proxy.commands;

import com.modularenigma.zander.proxy.ZanderProxyMain;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class discord extends Command {

    private ZanderProxyMain plugin = ZanderProxyMain.getInstance();

    public discord() {
        super("discord");
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if (commandSender instanceof ProxiedPlayer) {
            ProxiedPlayer player = (ProxiedPlayer) commandSender;

            // GET request to link to discord.

            TextComponent message = new TextComponent("Get to know the community and join our Discord here: " + ChatColor.BLUE + "discord");
            message.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "discord"));
            player.sendMessage(message);
            return;
        }
    }

}
