package org.modularsoft.zander.velocity.util.api.bridge;

import dev.dejvokep.boostedyaml.route.Route;
import io.github.ModularEnigma.Request;
import io.github.ModularEnigma.Response;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import org.modularsoft.zander.velocity.ZanderVelocityMain;
import org.modularsoft.zander.velocity.model.bridge.BridgeServerSync;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class ServerSync {
    private static final ProxyServer proxy = ZanderVelocityMain.getProxy();

    public static void startServerSyncTask() {
        String BaseAPIURL = ZanderVelocityMain.getConfig().getString(Route.from("BaseAPIURL"));
        String APIKey = ZanderVelocityMain.getConfig().getString(Route.from("APIKey"));

        // Create a ScheduledExecutorService with a single thread
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        // Schedule the task to run every 60 seconds
        scheduler.scheduleAtFixedRate(() -> {
            try {
                // Collect server and player information
                List<ServerInfo> serverInfoList = new ArrayList<>();
                for (RegisteredServer server : proxy.getAllServers()) {
                    List<String> playerNames = server.getPlayersConnected().stream()
                            .map(Player::getUsername)
                            .collect(Collectors.toList());

                    ServerInfo info = new ServerInfo(
                            server.getServerInfo().getName(),
                            playerNames.size(),
                            playerNames
                    );
                    serverInfoList.add(info);
                }

                // Get the current time and format it as a DATETIME string
                LocalDateTime now = LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault());
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String lastUpdated = now.format(formatter);

                // Build the BridgeServerSync object
                BridgeServerSync serverInfo = BridgeServerSync.builder()
                        .serverInfo(serverInfoList)
                        .lastUpdated(lastUpdated)
                        .build();

                // Prepare the request body
                Request serverSyncReq = Request.builder()
                        .setURL(BaseAPIURL + "/bridge/server/update")
                        .setMethod(Request.Method.POST)
                        .addHeader("x-access-token", APIKey)
                        .setRequestBody(serverInfo.toString())
                        .build();

                // Send the request and get the response
                Response serverSyncRes = serverSyncReq.execute();
                if (serverSyncRes.getStatusCode() != 200) {
                    ZanderVelocityMain.getLogger().error("Error syncing server info: " + serverSyncRes.getBody());
                }

            } catch (Exception e) {
                // Handle any exceptions
                ZanderVelocityMain.getLogger().error("Error occurred while syncing server info: " + e.getMessage());
            }
        }, 0, 60, TimeUnit.SECONDS);
    }

    // Helper class to hold server information
    public static class ServerInfo {
        private final String serverName;
        private final int playerCount;
        private final List<String> playerNames;

        public ServerInfo(String serverName, int playerCount, List<String> playerNames) {
            this.serverName = serverName;
            this.playerCount = playerCount;
            this.playerNames = playerNames;
        }

        // Getters
        public String getServerName() {
            return serverName;
        }

        public int getPlayerCount() {
            return playerCount;
        }

        public List<String> getPlayerNames() {
            return playerNames;
        }
    }
}