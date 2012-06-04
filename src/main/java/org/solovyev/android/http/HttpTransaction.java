package org.solovyev.android.http;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.jetbrains.annotations.NotNull;

/**
 * User: serso
 * Date: 5/27/12
 * Time: 2:31 PM
 */
public interface HttpTransaction<R> {

    @NotNull
    HttpUriRequest createRequest();

    R getResponse(@NotNull HttpResponse response);
}
