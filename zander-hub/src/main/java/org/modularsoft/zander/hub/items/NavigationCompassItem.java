package org.modularsoft.zander.hub.items;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class NavigationCompassItem implements Listener {
    public static void giveCompass(Player player) {
        ItemStack navcompass = new ItemStack(Material.COMPASS);
        ItemMeta meta = navcompass.getItemMeta();

        meta.displayName(Component.text("Navigation Compass", NamedTextColor.AQUA, TextDecoration.BOLD));

        List<Component> lore = new ArrayList<>();
        lore.add(Component.text("Right Click me to access Servers", NamedTextColor.YELLOW));
        meta.lore(lore);
        navcompass.setItemMeta(meta);
        player.getInventory().setItem(4, navcompass); // Put the compass into the players middle hand
    }
}
