package org.modularsoft.zander.proxy.model.discord;

import com.google.gson.Gson;
import lombok.Builder;
import lombok.Getter;

@Builder
public class DiscordDirectMessage {

    @Getter String senderUsername;
    @Getter String recipientUsername;
    @Getter String server;
    @Getter String content;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

}
