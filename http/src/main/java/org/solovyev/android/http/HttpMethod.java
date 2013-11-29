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

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.protocol.HTTP;

import javax.annotation.Nonnull;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.List;

public enum HttpMethod {

	DELETE(ParamsLocation.in_uri) {
		@Nonnull
		@Override
		protected HttpRequestBase createRequest() {
			return new HttpDelete();
		}
	},

	GET(ParamsLocation.in_uri) {
		@Nonnull
		@Override
		protected HttpRequestBase createRequest() {
			return new HttpGet();
		}
	},

	HEAD(ParamsLocation.in_uri) {
		@Nonnull
		@Override
		protected HttpRequestBase createRequest() {
			return new HttpHead();
		}
	},

	POST(ParamsLocation.in_headers) {
		@Nonnull
		@Override
		protected HttpRequestBase createRequest() {
			return new HttpPost();
		}
	},

	PUT(ParamsLocation.in_headers) {
		@Nonnull
		@Override
		protected HttpRequestBase createRequest() {
			return new HttpPut();
		}
	};

	private HttpMethod(@Nonnull ParamsLocation paramsLocation) {
		this.paramsLocation = paramsLocation;
	}

	static enum ParamsLocation {
		in_uri {
			@Nonnull
			@Override
			public String prepareUri(@Nonnull String uri, @Nonnull List<? extends NameValuePair> params) {
				if (params.isEmpty()) {
					return uri;
				} else {
					return uri + "?" + URLEncodedUtils.format(params, null);
				}
			}

			@Override
			public void addParams(@Nonnull HttpRequestBase request, @Nonnull List<? extends NameValuePair> params) {
				// no additional parameters
			}
		},

		in_headers {
			@Nonnull
			@Override
			public String prepareUri(@Nonnull String uri, @Nonnull List<? extends NameValuePair> params) {
				return uri;
			}

			@Override
			public void addParams(@Nonnull HttpRequestBase request, @Nonnull List<? extends NameValuePair> params) {
				if (request instanceof HttpEntityEnclosingRequestBase) {
					try {
						((HttpEntityEnclosingRequestBase) request).setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
					} catch (UnsupportedEncodingException e) {
						throw new RuntimeException(e);
					}
				} else {
					throw new IllegalArgumentException("Not known request type: " + request.getClass());
				}
			}
		};

		@Nonnull
		public abstract String prepareUri(@Nonnull String uri, @Nonnull List<? extends NameValuePair> params);

		public abstract void addParams(@Nonnull HttpRequestBase request, @Nonnull List<? extends NameValuePair> params);
	}

	@Nonnull
	private final ParamsLocation paramsLocation;

	@Nonnull
	protected abstract HttpRequestBase createRequest();

	@Nonnull
	public HttpUriRequest createRequest(@Nonnull String uri, @Nonnull List<NameValuePair> params) {
		return createRequest(URI.create(paramsLocation.prepareUri(uri, params)), params);
	}

	@Nonnull
	public HttpUriRequest createRequest(@Nonnull URI uri, @Nonnull List<NameValuePair> params) {
		final HttpRequestBase result = createRequest();
		result.setURI(uri);
		paramsLocation.addParams(result, params);
		return result;
	}
}
