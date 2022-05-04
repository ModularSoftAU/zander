package com.modularenigma.zander.proxy.api;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Request {
    private final String url;
    private final URI uri;
    private final Method requestMethod;
    private final String requestBody;

    public enum Method {
        GET, POST // Add others if desired
    }

    public static RequestBuilder builder() {
        return new RequestBuilder();
    }

    public Request(String url, Method requestMethod, String requestBody) {
        if (url == null) {
            throw new IllegalArgumentException("URL cannot be null");
        } else if (requestMethod == null) {
            throw new IllegalArgumentException("Request method must be set");
        }

        try {
            this.uri = URI.create(url);
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException("URI is not valid as per RFC 2396");
        }

        this.url = url;
        this.requestMethod = requestMethod;
        this.requestBody = requestBody;
    }

    /**
     * Execute the request.
     *
     * @return A {@link Response} instance that contains the response's body and status code.
     */
    public Response execute() {
        // System.out.println(this);
        // Setup headers
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(uri)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json");

        // Include the body of the Request
        HttpRequest.BodyPublisher content = HttpRequest.BodyPublishers.ofString(requestBody);
        switch (requestMethod) {
            case POST -> builder.POST(content);
            case GET -> builder.GET();
        }

        // Send the request and listen for a response
        HttpRequest request = builder.build();
        HttpClient client = HttpClient.newHttpClient();
        try {
            return new Response(client.send(request, HttpResponse.BodyHandlers.ofString()));
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException("Exception raised during API call.");
        }
    }

    @Override
    public String toString() {
        return "Request:\n" + requestMethod + " " + url + "\n" +
                "Body: " + requestBody + "\n";
    }
}
