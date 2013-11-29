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

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.List;

import static org.solovyev.android.Android.newTag;

public final class HttpTransactions {

	@Nonnull
	static final String TAG = newTag("Http");

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
}
