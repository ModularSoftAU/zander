package com.modularenigma.zander.proxy.api;

public class RequestBuilder {
    private String url;
    private Request.Method requestMethod;
    private String requestBody;

    /**
     * Set the url of the Request. Validity of the url is checked in {@link RequestBuilder#build()}.
     *
     * @param url The url of the website we will be sending a request to
     * @return {@link RequestBuilder}
     * @throws IllegalArgumentException If the url is null
     */
    public RequestBuilder setURL(String url) throws IllegalArgumentException {
        if (url == null) {
            throw new IllegalArgumentException("url cannot be null.");
        }
        this.url = url;
        return this;
    }

    /**
     * Sets the request method for the request
     *
     * @param requestMethod The Method of the request.
     * @return {@link RequestBuilder}
     */
    public RequestBuilder setMethod(Request.Method requestMethod) throws IllegalArgumentException {
        if (requestMethod == null) {
            throw new IllegalArgumentException("requestMethod cannot be null.");
        }
        this.requestMethod = requestMethod;
        return this;
    }

    /**
     * Set the contents of the request. Only useful for requests that support content
     * such as POST requests.
     *
     * @param body The body of the request
     * @return {@link RequestBuilder}
     * @throws IllegalArgumentException If body is null
     */
    public RequestBuilder setRequestBody(String body) throws IllegalArgumentException {
        if (body == null) {
            throw new IllegalArgumentException("body cannot be null.");
        }
        this.requestBody = body;
        return this;
    }

    /**
     * Builds the {@link Request} object if the {@link RequestBuilder} has been given enough
     * information to do so.
     *
     * @return A {@link Request} instance containing all data added to the {@link RequestBuilder}
     * @throws IllegalStateException If the url has not been set. If the requestMethod has not been
     *                               set.
     */
    public Request build() throws IllegalStateException {
        if (url == null) {
            throw new IllegalStateException("url is required.");
        } else if (requestMethod == null) {
            throw new IllegalStateException("requestMethod is required.");
        }
        return new Request(url, requestMethod, requestBody);
    }
}
