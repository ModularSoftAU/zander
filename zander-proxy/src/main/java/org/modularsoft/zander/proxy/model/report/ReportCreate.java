package org.modularsoft.zander.proxy.model.report;

import com.google.gson.Gson;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Builder
public class ReportCreate {

    @Getter String reportedUser;
    @Getter String reporterUser;
    @Getter String reason;
    @Getter String platform;
    @Getter String server;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

}
