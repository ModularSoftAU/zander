package org.modularsoft.zander.proxy.model.punishment;

import com.google.gson.Gson;
import lombok.Builder;
import lombok.Getter;

@Builder
public class PunishmentIssue {

    @Getter int punishmentId;
    @Getter String playerUsername;
    @Getter String staffUsername;
    @Getter String platform;
    @Getter String type;
    @Getter String reason;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

}
