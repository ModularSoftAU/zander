package com.modularenigma.zander.proxy.api;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.net.http.HttpResponse;

public class Response {
    private final JSONObject responseJSON;
    private final int statusCode;

    public Response(HttpResponse<String> response) {
        JSONParser jsonParser = new JSONParser();
        try {
            responseJSON = (JSONObject)jsonParser.parse(response.body());
        } catch (ParseException e) {
            e.printStackTrace();
            throw new RuntimeException("Response could not be parsed to JSON: " + response.body());
        }
        this.statusCode = response.statusCode();
    }

    /**
     * Gets the body of the response. May be empty
     */
    public JSONObject getBody() {
        return responseJSON;
    }

    /**
     * Gets the response's status code
     */
    public int getStatusCode() {
        return statusCode;
    }
}
