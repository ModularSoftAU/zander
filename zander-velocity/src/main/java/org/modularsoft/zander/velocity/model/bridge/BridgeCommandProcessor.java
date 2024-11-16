package org.modularsoft.zander.velocity.model.bridge;

import com.google.gson.Gson;
import lombok.Builder;
import lombok.Getter;

@Builder
public class BridgeCommandProcessor {

    @Getter Integer bridgeId;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

}
