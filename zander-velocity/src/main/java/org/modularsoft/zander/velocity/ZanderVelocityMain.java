package org.modularsoft.zander.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Dependency;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.PluginContainer;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.dvs.versioning.BasicVersioning;
import dev.dejvokep.boostedyaml.settings.dumper.DumperSettings;
import dev.dejvokep.boostedyaml.settings.general.GeneralSettings;
import dev.dejvokep.boostedyaml.settings.loader.LoaderSettings;
import dev.dejvokep.boostedyaml.settings.updater.UpdaterSettings;
import lombok.Getter;
import org.modularsoft.zander.velocity.events.UserChatEvent;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;

@Plugin(
        id = "zander-velocity",
        authors = "ModularSoft",
        description = "The proxy that allows the connection and integration of the Zander minecraft suite.",
        name = "zander-velocity",
        version = "1.2.0",
        dependencies = {
                @Dependency(id = "signedvelocity")
        }
)
public class ZanderVelocityMain {
    @Getter
    private static Logger logger;
    @Getter
    private final ProxyServer proxy;
    @Getter
    private static YamlDocument config;

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        // Event Listeners
        proxy.getEventManager().register(this, new UserChatEvent());

        // Commands

    }

    @Inject
    public ZanderVelocityMain(
            ProxyServer proxy,
            Logger logger,
            @DataDirectory Path dataDirectory
    ) {
        this.proxy = proxy;
        this.logger = logger;

        // Create configuration file
        try {
            config = YamlDocument.create(new File(dataDirectory.toFile(), "config.yml"),
                    Objects.requireNonNull(getClass().getResourceAsStream("/config.yml")),
                    GeneralSettings.DEFAULT,
                    LoaderSettings.builder().setAutoUpdate(true).build(),
                    DumperSettings.DEFAULT,
                    UpdaterSettings.builder().setVersioning(new BasicVersioning("config-version"))
                            .setOptionSorting(UpdaterSettings.OptionSorting.SORT_BY_DEFAULTS).build());

            config.update();
            config.save();
        } catch (IOException e) {
            logger.error("Could not create or load plugin configuration, plugin will now be disabled.");
            Optional<PluginContainer> container = proxy.getPluginManager().getPlugin("zander-velocity");
            container.ifPresent(pluginContainer -> pluginContainer.getExecutorService().shutdown());
        }

        logger.info("Zander Proxy has started.");
    }
}
