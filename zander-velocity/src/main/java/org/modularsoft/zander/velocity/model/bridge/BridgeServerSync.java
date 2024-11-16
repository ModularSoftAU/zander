package org.modularsoft.zander.velocity.model.bridge;

import com.google.gson.Gson;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Builder
public class BridgeServerSync {

    @Getter Object serverInfo;
    @Getter String lastUpdated;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

}
