package com.modularenigma.zander.proxy.model.filter;

import com.google.gson.Gson;
import lombok.Builder;
import lombok.Getter;

@Builder
public class PhraseFilter {

    @Getter String content;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

}
