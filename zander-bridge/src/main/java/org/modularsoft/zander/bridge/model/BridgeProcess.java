package org.modularsoft.zander.bridge.model;

import com.google.gson.Gson;
import lombok.Builder;
import lombok.Getter;

@Builder
public class BridgeProcess {

    @Getter Integer bridgeId;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

}
