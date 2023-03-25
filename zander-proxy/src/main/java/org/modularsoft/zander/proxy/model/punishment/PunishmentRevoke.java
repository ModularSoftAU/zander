package org.modularsoft.zander.proxy.model.punishment;

import com.google.gson.Gson;
import lombok.Builder;
import lombok.Getter;

@Builder
public class PunishmentRevoke {

    @Getter String punishmentId;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

}
