package org.modularsoft.zander.velocity.util.api;

import com.jayway.jsonpath.JsonPath;
import dev.dejvokep.boostedyaml.route.Route;
import io.github.ModularEnigma.Request;
import io.github.ModularEnigma.Response;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.modularsoft.zander.velocity.ZanderVelocityMain;
import org.modularsoft.zander.velocity.model.BridgeProcess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.CompletableFuture;

public class Bridge {
    // Initialize the logger
    private static final Logger logger = LoggerFactory.getLogger(Bridge.class);

    public static void startBridgeTask() {
        String BaseAPIURL = ZanderVelocityMain.getConfig().getString(Route.from("BaseAPIURL"));
        String APIKey = ZanderVelocityMain.getConfig().getString(Route.from("APIKey"));
        String TargetServerName = ZanderVelocityMain.getConfig().getString(Route.from("TargetServerName"));

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

                    // Execute the command asynchronously
                    CompletableFuture<Boolean> future = ZanderVelocityMain.getProxy()
                            .getCommandManager()
                            .executeImmediatelyAsync(ZanderVelocityMain.getProxy().getConsoleCommandSource(), command);

                    // Handle the result when the execution completes
                    future.thenAccept(executedSuccessfully -> {
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
                                ZanderVelocityMain.getLogger().info("Response (" + bridgeProcessRes.getStatusCode() + "): " + bridgeProcessRes.getBody());
                            } catch (Exception e) {
                                e.printStackTrace();
                                ZanderVelocityMain.getLogger().error("Failed to process command: {}", bridgeId);
                            }
                        } else {
                            logger.warn("Command execution failed: {}", command);
                        }
                    }).exceptionally(ex -> {
                        // Handle exceptions during command execution
                        logger.error("Command execution encountered an error: {}", command, ex);
                        return null;
                    });
                }

            } catch (Exception e) {
                // Handle exceptions here
                e.printStackTrace();
                ZanderVelocityMain.getLogger().error("Fetching Bridge actions Failed.");
            }
        }, 0, 60, TimeUnit.SECONDS);
    }
}