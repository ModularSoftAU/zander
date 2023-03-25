package org.modularsoft.zander.proxy.model.vote;

import com.google.gson.Gson;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Builder
public class VoteCast {

    @Getter String username;
    @Getter String voteSite;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

}
