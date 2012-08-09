package org.solovyev.android.http;

import org.apache.http.client.methods.HttpUriRequest;
import org.jetbrains.annotations.NotNull;

/**
 * User: serso
 * Date: 5/27/12
 * Time: 2:33 PM
 */
public abstract class AbstractHttpTransaction<R> implements HttpTransaction<R>, HttpTransactionDef {

    @NotNull
    private final HttpMethod httpMethod;

    @NotNull
    private final String uri;

    protected AbstractHttpTransaction(@NotNull String uri, @NotNull HttpMethod httpMethod) {
        this.httpMethod = httpMethod;
        this.uri = uri;
    }

    @Override
    @NotNull
    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    @Override
    @NotNull
    public String getUri() {
        return uri;
    }

    @NotNull
    @Override
    public HttpUriRequest createRequest() {
        return httpMethod.createRequest(uri, getRequestParameters());
    }

}
