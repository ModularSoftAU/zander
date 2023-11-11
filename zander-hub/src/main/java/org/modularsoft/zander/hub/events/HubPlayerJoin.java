package org.modularsoft.zander.hub.events;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.modularsoft.zander.hub.ConfigurationManager;
import org.modularsoft.zander.hub.ZanderHubMain;
import org.modularsoft.zander.hub.items.NavigationCompassItem;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Sound;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.metadata.MetadataValue;

import java.util.List;
import java.util.Random;

public class HubPlayerJoin implements Listener {
    ZanderHubMain plugin;
    public HubPlayerJoin(ZanderHubMain plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        // Clear the Players inventory for miscellaneous items
        player.getInventory().clear();

        // Teleport Player to Hub spawn point
        player.teleport(ConfigurationManager.getHubLocation());

        NavigationCompassItem.giveCompass(player); // Give player navigation compass
        player.getInventory().setHeldItemSlot(4); // Set the players current slot to the navigation compass

        event.joinMessage(null);
//        event.setJoinMessage(null); // disable default join message

        if (!player.hasPlayedBefore()) {
            // New user Joins for first time celebratory firework
            Firework firework = event.getPlayer().getWorld().spawn(event.getPlayer().getLocation(), Firework.class);
            FireworkMeta fireworkmeta = firework.getFireworkMeta();
            fireworkmeta.addEffect(FireworkEffect.builder()
                    .flicker(false)
                    .trail(true)
                    .with(FireworkEffect.Type.CREEPER)
                    .withColor(Color.GREEN)
                    .withFade(Color.BLUE)
                    .build());
            fireworkmeta.setPower(3);
            firework.setFireworkMeta(fireworkmeta);

            // Send new user a MOTD seperate to the main MOTD
            List<String> newplayermotd = ConfigurationManager.getWelcome().getStringList("newplayerwelcome");
            for (String s : newplayermotd) {
                event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', s));
            }
            event.getPlayer().sendMessage(" "); // Separate between messages
        }

//        NOTE:: This has been removed due to the MOTD having lack of purpose and content.

//        if (player.hasPlayedBefore()) {
//            // Dispatch MOTD to user
//            List<String> motd = plugin.configurationManager.getmotd().getStringList("motd");
//            for (String s : motd) {
//                event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', s));
//            }
//        }

        // Play random sound
        int randomSoundIndex = new Random().nextInt() % Sound.values().length - 1;
        Sound randomChosenSound = Sound.values()[randomSoundIndex];
        player.playSound(player.getLocation(), randomChosenSound, 1f,1f);

        if (!isVanished(player)) {
            Component broadcastMessage = Component.empty()
                    .color(NamedTextColor.GRAY)
                    .append(event.getPlayer().name())
                    .append(Component.text(" joined."));

            for (Player otherPlayer : Bukkit.getOnlinePlayers()) {
                otherPlayer.sendMessage(broadcastMessage);
            }
        }

        player.setCollidable(true); // Disable player collision.

        // If user has the fly permission, enable flight
        if (player.hasPermission("zander.hub.fly")) {
            player.setAllowFlight(true);
        }
    }

    private boolean isVanished(Player player) {
        for (MetadataValue meta : player.getMetadata("vanished")) {
            if (meta.asBoolean()) return true;
        }
        return false;
    }
}
