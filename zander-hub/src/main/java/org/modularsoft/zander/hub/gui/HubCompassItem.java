package org.modularsoft.zander.hub.gui;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.modularsoft.zander.hub.events.PluginMessageChannel;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;

public class HubCompassItem implements Listener {
    static final Inventory navCompass = Bukkit.createInventory(null, 9, "Server Selector");

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (player.getInventory().getItemInMainHand().getType() == Material.COMPASS) {
            if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() ==  Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                navCompassGui(player);
            }
        }
    }

    public void navCompassGui(Player player) {
        if (player == null) {
            return;
        }

        ItemStack survival = new ItemStack(Material.IRON_PICKAXE);
        ItemMeta survivalMeta = survival.getItemMeta();
        survivalMeta.displayName(Component.text("Survival", NamedTextColor.WHITE));
        survivalMeta.lore(Collections.singletonList(Component.text("Click me to join our Survival server.", NamedTextColor.WHITE)));
        survival.setItemMeta(survivalMeta);
        navCompass.setItem(2, survival);

        ItemStack mixed = new ItemStack(Material.IRON_SWORD);
        ItemMeta mixedMeta = mixed.getItemMeta();
        mixedMeta.displayName(Component.text("Mixed", NamedTextColor.WHITE));
        mixedMeta.lore(Collections.singletonList(Component.text("Play and Destroy your friends in Minigames.", NamedTextColor.WHITE)));
        mixed.setItemMeta(mixedMeta);
        navCompass.setItem(6, mixed);

        player.openInventory(navCompass);
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getInventory() != navCompass) {
            return;
        }

        Player player = (Player) event.getWhoClicked();
        event.setCancelled(true);

        if (event.getCurrentItem() == null || !event.getCurrentItem().hasItemMeta()) {
            player.closeInventory();
            return;
        }

        switch (event.getCurrentItem().getType()) {
            // Survival
            case IRON_PICKAXE:
                player.closeInventory();
                player.sendMessage(Component.text("Sending you to Survival...", NamedTextColor.YELLOW));
                if (player.hasPermission("bungeecord.server.survival")) {
                    PluginMessageChannel.connect(player, "survival");
                } else  {
                    player.sendMessage(Component.text("You do not have access to this server.", NamedTextColor.RED));
                }
                break;

            // Events
            case IRON_SWORD:
                player.closeInventory();
                player.sendMessage(Component.text("Sending you to Mixed...", NamedTextColor.YELLOW));
                if (player.hasPermission("bungeecord.server.mixed")) {
                    PluginMessageChannel.connect(player, "mixed");
                } else  {
                    player.sendMessage(Component.text("You do not have access to this server.", NamedTextColor.RED));
                }
                break;

            default:
                break;
        }
    }
}
