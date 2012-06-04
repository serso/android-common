package org.solovyev.android.http;

import org.apache.http.NameValuePair;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * User: serso
 * Date: 6/4/12
 * Time: 4:24 PM
 */
public interface HttpTransactionDef {

    @NotNull
    HttpMethod getHttpMethod();

    @NotNull
    String getUri();

    @NotNull
    List<NameValuePair> getRequestParameters();
}
