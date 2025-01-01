package org.modularsoft.zander.hub.events;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.modularsoft.zander.hub.ConfigurationManager;
import org.modularsoft.zander.hub.ZanderHubMain;
import org.modularsoft.zander.hub.items.NavigationCompassItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HubPlayerJoin implements Listener {
    // Misc settings
    private static final boolean TEST_ALWAYS_FIRST_JOIN = false; // * default false
    private static final int NAV_COMPASS_SLOT = 4;
    private static final long ROUTINE_PLAYER_JOINED_DELAY = (long) (1.5f * 20);

    // Firework settings
    private static final double FIREWORK_GROUND_HEIGHT = 3; // blocks
    private static final long FIREWORK_DETONATE_DELAY = (long) (0.3f * 20);
    private static final Color[] FIREWORK_COLOR_PALETTE = {
            Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.PURPLE,
            Color.ORANGE, Color.WHITE, Color.AQUA, Color.LIME,
    };

    // Sound settings
    private static final float SOUND_PITCH = 1.0f;
    private static final float SOUND_VOLUME = 1.0f;
    private static final Sound[] WELCOME_SOUNDS = {
            Sound.BLOCK_AMETHYST_BLOCK_FALL,
            Sound.BLOCK_AMETHYST_CLUSTER_BREAK,
            Sound.BLOCK_BEACON_ACTIVATE,
            Sound.BLOCK_BELL_RESONATE,
            Sound.BLOCK_CAMPFIRE_CRACKLE,
            Sound.BLOCK_COMPOSTER_FILL_SUCCESS,
            Sound.BLOCK_COPPER_FALL,
            Sound.BLOCK_DECORATED_POT_HIT,
            Sound.BLOCK_ENCHANTMENT_TABLE_USE,
            Sound.BLOCK_ENDER_CHEST_OPEN,
            Sound.BLOCK_END_PORTAL_FRAME_FILL,
            Sound.BLOCK_NETHERITE_BLOCK_STEP,
            Sound.BLOCK_RESPAWN_ANCHOR_CHARGE,
            Sound.BLOCK_RESPAWN_ANCHOR_DEPLETE,
            Sound.BLOCK_SCAFFOLDING_PLACE,
            Sound.BLOCK_WATER_AMBIENT,
            Sound.ENTITY_ALLAY_AMBIENT_WITHOUT_ITEM,
            Sound.ENTITY_ARMOR_STAND_BREAK,
            Sound.ENTITY_AXOLOTL_SPLASH,
            Sound.ENTITY_CAT_PURREOW,
            Sound.ENTITY_CHICKEN_DEATH,
            Sound.ENTITY_CREEPER_PRIMED,
            Sound.ENTITY_DOLPHIN_ATTACK,
            Sound.ENTITY_ENDERMAN_TELEPORT,
            Sound.ENTITY_ENDER_DRAGON_HURT,
            Sound.ENTITY_EVOKER_DEATH,
            Sound.ENTITY_FROG_DEATH,
            Sound.ENTITY_GLOW_SQUID_AMBIENT,
            Sound.ENTITY_GOAT_AMBIENT,
            Sound.ENTITY_HOGLIN_AMBIENT,
            Sound.ENTITY_PIGLIN_ADMIRING_ITEM,
            Sound.ENTITY_PIG_AMBIENT,
            Sound.ENTITY_PLAYER_BIG_FALL,
            Sound.ENTITY_PLAYER_HURT_FREEZE,
            Sound.ENTITY_SHULKER_SHOOT,
            Sound.ENTITY_VILLAGER_HURT,
            Sound.ENTITY_WANDERING_TRADER_AMBIENT,
            Sound.ENTITY_WANDERING_TRADER_DISAPPEARED,
            Sound.ENTITY_WANDERING_TRADER_REAPPEARED,
            Sound.ENTITY_WARDEN_NEARBY_CLOSER,
            Sound.ENTITY_WITCH_HURT,
            Sound.ENTITY_WITHER_SKELETON_STEP,
            Sound.ENTITY_ZOMBIE_INFECT,
    };

    private final ZanderHubMain plugin;

    public HubPlayerJoin(ZanderHubMain plugin) {
        this.plugin = plugin;
    }

    /// Triggers when player's client first connects.
    /// Best used for validation and basic setup (permission, flags, etc).
    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        setPermissions(player);
    }

    /// Triggers when player's client has joined the world.
    /// Best used for initial world interactions (player world state changes etc).
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.joinMessage(null); // * suppress default
        Player player = event.getPlayer();
        setInitialState(player);
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            // * bukkit uses 'world/playerdata' dir for tracking
            if (!player.hasPlayedBefore() || TEST_ALWAYS_FIRST_JOIN) {
                chatWelcomeMessage(player);
                spawnWelcomeFirework(player);
            }
            chatJoinMessage(player);
            playWelcomeSound(player);
        }, ROUTINE_PLAYER_JOINED_DELAY);
    }

    /// Set special permission depending on the player.
    private void setPermissions(Player player) {
        if (player.hasPermission("zander.hub.fly")) {
            player.setAllowFlight(true);
        }
    }

    /// Set the initial state of the player in the world.
    private void setInitialState(Player player) {
        setupNoCollision(player);
        player.teleport(ConfigurationManager.getHubLocation());
        player.getInventory().clear();
        player.getInventory().setHeldItemSlot(NAV_COMPASS_SLOT);
        NavigationCompassItem.giveCompass(player);
    }

    /// Disable entity collision for player.
    /// Correct way as mentioned at:
    /// https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/entity/LivingEntity.html#setCollidable(boolean)
    private void setupNoCollision(Player player) {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        Team team = scoreboard.getTeam("nocollision");
        if (team == null) {
            team = scoreboard.registerNewTeam("nocollision");
            team.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
        }
        team.addEntry(player.getName());
    }

    /// Spawn a pretty firework where the player is.
    private void spawnWelcomeFirework(Player player) {
        Location spawnLoc = player.getLocation().add(0, FIREWORK_GROUND_HEIGHT, 0);
        Firework firework = player.getWorld().spawn(spawnLoc, Firework.class);
        FireworkMeta meta = firework.getFireworkMeta();

        Random random = new Random();
        Color[] FCP = FIREWORK_COLOR_PALETTE;

        // random primary-colors
        List<Color> colors = new ArrayList<>();
        int numColors = random.nextInt(3) + 2; // 2-4 colors
        for (int i = 0; i < numColors; i++) {
            colors.add(FCP[random.nextInt(FCP.length)]);
        }
        // random fade-color and firework type
        Color fadeColor = FCP[random.nextInt(FCP.length)];
        FireworkEffect.Type[] types = FireworkEffect.Type.values();
        FireworkEffect.Type type = types[random.nextInt(types.length)];

        FireworkEffect effect = FireworkEffect.builder()
                .flicker(false)
                .trail(true)
                .with(type)
                .withColor(colors)
                .withFade(fadeColor)
                .build();
        meta.addEffect(effect);
        meta.setPower(1);
        firework.setFireworkMeta(meta);

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            firework.detonate();
        }, FIREWORK_DETONATE_DELAY);
    }

    /// Send a welcome message in player's chat.
    private void chatWelcomeMessage(Player player) {
        List<String> message = ConfigurationManager.getWelcome().getStringList("newplayerwelcome");
        for (String row : message) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', row));
        }
        player.sendMessage("");
    }

    /// Send a join message in player's chat.
    private void chatJoinMessage(Player player) {
        if (isVanished(player))
            return;
        Component message = Component.empty()
                .color(NamedTextColor.GRAY)
                .append(player.name())
                .append(Component.text(" joined."));
        Bukkit.getOnlinePlayers().forEach(p -> p.sendMessage(message));
    }

    /// Play a random sound for the player.
    private void playWelcomeSound(Player player) {
        Sound randomSound = WELCOME_SOUNDS[(int) (Math.random() * WELCOME_SOUNDS.length)];
        player.playSound(player.getLocation(), randomSound, SOUND_VOLUME, SOUND_PITCH);
    }

    /// Check if the player is currently vanished.
    private boolean isVanished(Player player) {
        return player.getMetadata("vanished").stream().anyMatch(MetadataValue::asBoolean);

    }
}
