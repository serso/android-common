package org.solovyev.android.http;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.solovyev.common.collections.Collections;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * User: serso
 * Date: 4/13/13
 * Time: 4:34 PM
 */
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
		return Collections.getFirstListElement(execute(Arrays.asList(httpTransaction)));
	}

	@Override
	@Nonnull
	public <R> List<R> execute(@Nonnull List<? extends HttpTransaction<R>> httpTransactions) throws IOException {
		final List<R> result = new ArrayList<R>();
		for (HttpTransaction<R> httpTransaction : httpTransactions) {
			final HttpUriRequest request = httpTransaction.createRequest();
			final HttpResponse httpResponse = httpClient.execute(request);

			final R response = httpTransaction.getResponse(httpResponse);
			result.add(response);
		}

		return result;
	}
}
