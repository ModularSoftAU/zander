package net.modularsoft.zander.hub.gui;

import net.modularsoft.zander.hub.ZanderHubMain;
import net.modularsoft.zander.hub.events.PluginMessageChannel;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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

import java.util.Arrays;

public class HubCompassItem implements Listener {

    ZanderHubMain plugin;
    public void navigationgui(ZanderHubMain plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void navigationgui(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (player.getInventory().getItemInMainHand().getType() == Material.COMPASS) {
            if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() ==  Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                CompassNavGUI(player);
            }
        }
    }

    Inventory compnav = Bukkit.createInventory(null, 9, "Server Selector");

    public void CompassNavGUI(Player player) {
        if (player == null) {
            return;
        }

        ItemStack survival = new ItemStack(Material.IRON_PICKAXE);
        ItemMeta survivalMeta = survival.getItemMeta();
        survivalMeta.setDisplayName(ChatColor.WHITE + "Survival");
        survivalMeta.setLore(Arrays.asList(ChatColor.WHITE + "Click me to join our Survival server."));
        survival.setItemMeta(survivalMeta);
        compnav.setItem(4, survival);

        ItemStack mixed = new ItemStack(Material.IRON_SWORD);
        ItemMeta mixedMeta = mixed.getItemMeta();
        mixedMeta.setDisplayName(ChatColor.WHITE + "Mixed");
        mixedMeta.setLore(Arrays.asList(ChatColor.WHITE + "Play and Destroy your friends in Minigames."));
        mixed.setItemMeta(mixedMeta);
        compnav.setItem(6, mixed);

        player.openInventory(compnav);
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!event.getView().getTitle().equalsIgnoreCase("Server Selector")) {
            return;
        }

        Player player = (Player) event.getWhoClicked();
        event.setCancelled(true);

        if (event.getCurrentItem() == null || event.getCurrentItem().getType() == null || !event.getCurrentItem().hasItemMeta()) {
            player.closeInventory();
            return;
        }

        switch (event.getCurrentItem().getType()) {
            // Survival
            case IRON_PICKAXE:
                player.closeInventory();
                player.sendMessage(ChatColor.YELLOW + "Sending you to Survival..");
                if (player.hasPermission("bungeecord.server.survival")) {
                    PluginMessageChannel.connect(player, "survival");
                } else  {
                    player.sendMessage(ChatColor.RED + "You do not have access to this server.");
                }
                break;

            // Events
            case IRON_SWORD:
                player.closeInventory();
                player.sendMessage(ChatColor.YELLOW + "Sending you to Mixed..");
                if (player.hasPermission("bungeecord.server.mixed")) {
                    PluginMessageChannel.connect(player, "mixed");
                } else  {
                    player.sendMessage(ChatColor.RED + "You do not have access to this server.");
                }
                break;

            default:
                break;
        }
    }

}
