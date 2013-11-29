package org.solovyev.android.http;

import org.apache.http.impl.client.DefaultHttpClient;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.List;

public interface AHttpClient {

	@Nonnull
	DefaultHttpClient getHttpClient();

	<R> R execute(@Nonnull HttpTransaction<R> httpTransaction) throws IOException;

	@Nonnull
	<R> List<R> execute(@Nonnull List<? extends HttpTransaction<R>> httpTransactions) throws IOException;
}
