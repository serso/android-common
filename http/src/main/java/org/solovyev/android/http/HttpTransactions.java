/*
 * Copyright 2013 serso aka se.solovyev
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Contact details
 *
 * Email: se.solovyev@gmail.com
 * Site:  http://se.solovyev.org
 */

package org.solovyev.android.http;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import org.apache.http.NameValuePair;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.util.List;

/**
 * User: serso
 * Date: 5/28/12
 * Time: 2:17 PM
 */
public final class HttpTransactions {

	private HttpTransactions() {
		throw new AssertionError();
	}

	@Nonnull
	public static AHttpClient newHttpClient() {
		return new AHttpClientImpl();
	}

	public static <R> R execute(@Nonnull HttpTransaction<R> httpTransaction) throws IOException {
		return newHttpClient().execute(httpTransaction);
	}

	@Nonnull
	public static <R> List<R> execute(@Nonnull List<? extends HttpTransaction<R>> httpTransactions) throws IOException {
		return newHttpClient().execute(httpTransactions);
	}

	public static void asyncExecute(@Nonnull HttpTransactionDef httpTransactionDef,
									@Nullable AsyncHttpResponseHandler responseHandle) throws IOException {
		final AsyncHttpClient httpClient = new AsyncHttpClient();

		final RequestParams requestParams = new RequestParams();
		for (NameValuePair requestParam : httpTransactionDef.getRequestParameters()) {
			requestParams.put(requestParam.getName(), requestParam.getValue());
		}

		httpTransactionDef.getHttpMethod().doAsyncRequest(httpClient, httpTransactionDef.getUri(), requestParams, responseHandle);

	}
}
