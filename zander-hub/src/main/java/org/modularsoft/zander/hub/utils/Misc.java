package org.modularsoft.zander.hub.utils;

import de.myzelyam.api.vanish.VanishAPI;
import org.bukkit.entity.Player;

public final class Misc {

    private Misc() {
        throw new IllegalStateException("Utility class shouldn't be instantiated");
    }

    public static boolean isVanish(Player player) {
        return (player != null && VanishAPI.isInvisible(player));
    }
}
