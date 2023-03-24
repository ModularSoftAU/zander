package net.modularsoft.zander.proxy.model.session;

import com.google.gson.Gson;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Builder
public class SessionDestroy {

    @Getter UUID uuid;
    
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

}
