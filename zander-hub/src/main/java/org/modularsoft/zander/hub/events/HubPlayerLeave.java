package org.modularsoft.zander.hub.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.modularsoft.zander.hub.ConfigurationManager;
import org.modularsoft.zander.hub.ZanderHubMain;
import org.modularsoft.zander.hub.utils.Misc;

public class HubPlayerLeave implements Listener {

    private final ZanderHubMain plugin;

    public HubPlayerLeave(ZanderHubMain plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (Misc.isVanish(player)) {
            return;
        }
        event.quitMessage(ConfigurationManager.getMessage().playerLeave(player.displayName()));
    }
}
