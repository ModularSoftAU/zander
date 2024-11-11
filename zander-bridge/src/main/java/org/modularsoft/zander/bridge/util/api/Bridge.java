package org.modularsoft.zander.bridge.util.api;

import com.jayway.jsonpath.JsonPath;
import io.github.ModularEnigma.Request;
import io.github.ModularEnigma.Response;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.modularsoft.zander.bridge.ZanderBridgeMain;
import org.modularsoft.zander.bridge.model.BridgeProcess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Bridge {
    // Initialize the logger
    private static final Logger logger = LoggerFactory.getLogger(Bridge.class);

    private final ZanderBridgeMain plugin;

    // Constructor to get the instance of the plugin
    public Bridge(ZanderBridgeMain plugin) {
        this.plugin = plugin;
    }

    public void startBridgeTask() {
        String BaseAPIURL = plugin.getConfig().getString("BaseAPIURL");
        String APIKey = plugin.getConfig().getString("APIKey");
        String TargetServerName = plugin.getConfig().getString("TargetServerName");

        // Create a ScheduledExecutorService with a single thread
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        // Schedule the task to run every 60 seconds
        scheduler.scheduleAtFixedRate(() -> {
            try {
                Request req = Request.builder()
                        .setURL(BaseAPIURL + "/bridge/get?targetServer=" + TargetServerName)
                        .setMethod(Request.Method.GET)
                        .addHeader("x-access-token", APIKey)
                        .build();

                Response res = req.execute();
                String json = res.getBody();

                // Extract the list of data objects
                List<Object> dataList = JsonPath.read(json, "$.data");

                // Check if dataList is null or empty
                if (dataList == null || dataList.isEmpty()) {
                    logger.info("No actions found for the bridge.");
                    return; // Exit the method early if there are no actions
                }

                // Loop through each entry in the data list
                for (Object dataEntry : dataList) {
                    int bridgeId = JsonPath.read(dataEntry, "$.bridgeId");
                    String command = JsonPath.read(dataEntry, "$.command");

                    // Execute the command asynchronously on the main server thread
                    Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                        ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
                        boolean executedSuccessfully = Bukkit.getServer().dispatchCommand(console, command);

                        if (executedSuccessfully) {
                            logger.info("Command executed successfully: {}", command);

                            // Mark the command as processed
                            try {
                                BridgeProcess bridgeProcess = BridgeProcess.builder()
                                        .bridgeId(bridgeId)
                                        .build();

                                Request bridgeProcessReq = Request.builder()
                                        .setURL(BaseAPIURL + "/bridge/command/process")
                                        .setMethod(Request.Method.POST)
                                        .addHeader("x-access-token", APIKey)
                                        .setRequestBody(bridgeProcess.toString())
                                        .build();

                                Response bridgeProcessRes = bridgeProcessReq.execute();
                                logger.info("Response (" + bridgeProcessRes.getStatusCode() + "): " + bridgeProcessRes.getBody());
                            } catch (Exception e) {
                                e.printStackTrace();
                                logger.error("Failed to process command: {}", bridgeId);
                            }
                        } else {
                            logger.warn("Command execution failed: {}", command);
                        }
                    });
                }

            } catch (Exception e) {
                // Handle exceptions here
                e.printStackTrace();
                logger.error("Fetching Bridge actions Failed.");
            }
        }, 0, 60, TimeUnit.SECONDS);
    }
}
