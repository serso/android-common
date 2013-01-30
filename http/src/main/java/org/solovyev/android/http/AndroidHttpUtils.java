package org.solovyev.android.http;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.solovyev.common.collections.JCollections;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * User: serso
 * Date: 5/28/12
 * Time: 2:17 PM
 */
public class AndroidHttpUtils {

    private AndroidHttpUtils() {
        throw new AssertionError();
    }

    public static <R> R execute(@NotNull HttpTransaction<R> httpTransaction) throws IOException {
        return JCollections.getFirstListElement(execute(Arrays.asList(httpTransaction)));
    }

    @NotNull
    public static <R> List<R> execute(@NotNull List<? extends HttpTransaction<R>> httpTransactions) throws IOException {
        final DefaultHttpClient httpClient = new DefaultHttpClient();

        final List<R> result = new ArrayList<R>();
        for (HttpTransaction<R> httpTransaction : httpTransactions) {
            final HttpUriRequest request = httpTransaction.createRequest();
            final HttpResponse httpResponse = httpClient.execute(request);

            final R response = httpTransaction.getResponse(httpResponse);
            result.add(response);
        }

        return result;
    }

    public static void asyncExecute(@NotNull HttpTransactionDef httpTransactionDef,
                                    @Nullable AsyncHttpResponseHandler responseHandle) throws IOException {
        final AsyncHttpClient httpClient = new AsyncHttpClient();

        final RequestParams requestParams = new RequestParams();
        for (NameValuePair requestParam : httpTransactionDef.getRequestParameters()) {
            requestParams.put(requestParam.getName(), requestParam.getValue());
        }

        httpTransactionDef.getHttpMethod().doAsyncRequest(httpClient, httpTransactionDef.getUri(), requestParams, responseHandle);

    }
}
