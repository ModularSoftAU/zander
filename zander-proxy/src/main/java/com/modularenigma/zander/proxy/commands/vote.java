package com.modularenigma.zander.proxy.commands;

import com.modularenigma.zander.proxy.ZanderProxyMain;
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

            // GET request to link to vote.

            TextComponent message = new TextComponent(ChatColor.AQUA + "Help out Crafting For Christ by voting on Minecraft Server lists! " + ChatColor.BLUE + "vote");
            message.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "vote"));
            player.sendMessage(message);
            return;
        }
    }

}
