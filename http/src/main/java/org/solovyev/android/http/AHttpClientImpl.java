package org.solovyev.android.http;

import android.util.Log;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.solovyev.common.collections.Collections;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.Arrays.asList;
import static org.solovyev.android.http.HttpTransactions.TAG;

class AHttpClientImpl implements AHttpClient {

	@Nonnull
	private final DefaultHttpClient httpClient = new DefaultHttpClient();

	@Nonnull
	@Override
	public DefaultHttpClient getHttpClient() {
		return httpClient;
	}

	@Override
	public <R> R execute(@Nonnull HttpTransaction<R> httpTransaction) throws IOException {
		return Collections.getFirstListElement(execute(asList(httpTransaction)));
	}

	@Override
	@Nonnull
	public <R> List<R> execute(@Nonnull List<? extends HttpTransaction<R>> httpTransactions) throws IOException {
		final List<R> result = new ArrayList<R>();
		for (HttpTransaction<R> httpTransaction : httpTransactions) {
			final String transactionName = httpTransaction.getClass().getSimpleName();
			Log.d(TAG, "Executing transaction: " + transactionName);

			final HttpUriRequest request = httpTransaction.createRequest();
			final HttpResponse httpResponse = httpClient.execute(request);

			final R response = httpTransaction.getResponse(httpResponse);
			result.add(response);
			Log.d(TAG, "Execution finished: " + transactionName);
		}

		return result;
	}
}
