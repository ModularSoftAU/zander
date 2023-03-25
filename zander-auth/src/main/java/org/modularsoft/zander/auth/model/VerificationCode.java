package org.modularsoft.zander.auth.model;

import com.google.gson.Gson;
import lombok.Builder;
import lombok.Getter;

@Builder
public class VerificationCode {

    @Getter String username;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

}
