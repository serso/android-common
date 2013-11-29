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

import org.apache.http.client.methods.HttpUriRequest;

import javax.annotation.Nonnull;

public abstract class AbstractHttpTransaction<R> implements HttpTransaction<R>, HttpTransactionDef {

	@Nonnull
	private final HttpMethod httpMethod;

	@Nonnull
	private final String uri;

	protected AbstractHttpTransaction(@Nonnull String uri, @Nonnull HttpMethod httpMethod) {
		this.httpMethod = httpMethod;
		this.uri = uri;
	}

	@Override
	@Nonnull
	public HttpMethod getHttpMethod() {
		return httpMethod;
	}

	@Override
	@Nonnull
	public String getUri() {
		return uri;
	}

	@Nonnull
	@Override
	public HttpUriRequest createRequest() {
		return httpMethod.createRequest(uri, getRequestParameters());
	}

}
