package org.modularsoft.zandervelocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import lombok.Getter;
import org.slf4j.Logger;

@Plugin(
        id = "zander-velocity",
        name = "zander-velocity",
        version = "1.0-SNAPSHOT"
)
public class ZanderVelocityMain {
    @Getter
    private final Logger logger;
    @Getter
    private final ProxyServer proxy;

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {

    }

    @Inject
    public ZanderVelocityMain(ProxyServer proxy, Logger logger) {
        this.proxy = proxy;
        this.logger = logger;

        logger.info("Zander Proxy has started.");
    }

}
