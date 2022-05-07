package com.modularenigma.zander.proxy.commands;

import com.modularenigma.zander.proxy.ConfigurationManager;
import com.modularenigma.zander.proxy.ZanderProxyMain;
import com.modularenigma.zander.proxy.api.Request;
import com.modularenigma.zander.proxy.api.Response;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class rules extends Command {

    private ZanderProxyMain plugin = ZanderProxyMain.getInstance();

    public rules() {
        super("rules");
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if (commandSender instanceof ProxiedPlayer) {
            ProxiedPlayer player = (ProxiedPlayer) commandSender;

            // GET request to link to rules.

            TextComponent message = new TextComponent("Please read and abide by the Network rules which you can find on our website here: " + "rules");
            message.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "rules"));
            player.sendMessage(message);
            return;
        }
    }

}
