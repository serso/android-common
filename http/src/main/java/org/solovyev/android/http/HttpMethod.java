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
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.protocol.HTTP;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.List;

/**
 * User: serso
 * Date: 5/27/12
 * Time: 2:45 PM
 */
public enum HttpMethod {

    DELETE(ParamsLocation.in_uri) {
        @NotNull
        @Override
        protected HttpRequestBase createRequest() {
            return new HttpDelete();
        }

        @Override
        public void doAsyncRequest(@NotNull AsyncHttpClient client, @NotNull String uri, @Nullable RequestParams requestParams, @Nullable AsyncHttpResponseHandler asyncHttpResponseHandler) {
            assert requestParams == null;
            client.delete(uri, asyncHttpResponseHandler);
        }
    },

    GET(ParamsLocation.in_uri) {
        @NotNull
        @Override
        protected HttpRequestBase createRequest() {
            return new HttpGet();
        }

        @Override
        public void doAsyncRequest(@NotNull AsyncHttpClient client, @NotNull String uri, @Nullable RequestParams requestParams, @Nullable AsyncHttpResponseHandler asyncHttpResponseHandler) {
            client.get(uri, requestParams, asyncHttpResponseHandler);
        }
    },

    HEAD(ParamsLocation.in_uri) {
        @NotNull
        @Override
        protected HttpRequestBase createRequest() {
            return new HttpHead();
        }

        @Override
        public void doAsyncRequest(@NotNull AsyncHttpClient client, @NotNull String uri, @Nullable RequestParams requestParams, @Nullable AsyncHttpResponseHandler asyncHttpResponseHandler) {
            throw new UnsupportedOperationException();
        }
    },

    POST(ParamsLocation.in_headers) {
        @NotNull
        @Override
        protected HttpRequestBase createRequest() {
            return new HttpPost();
        }

        @Override
        public void doAsyncRequest(@NotNull AsyncHttpClient client, @NotNull String uri, @Nullable RequestParams requestParams, @Nullable AsyncHttpResponseHandler asyncHttpResponseHandler) {
            client.post(uri, requestParams, asyncHttpResponseHandler);
        }
    },

    PUT(ParamsLocation.in_headers) {
        @NotNull
        @Override
        protected HttpRequestBase createRequest() {
            return new HttpPut();
        }

        @Override
        public void doAsyncRequest(@NotNull AsyncHttpClient client, @NotNull String uri, @Nullable RequestParams requestParams, @Nullable AsyncHttpResponseHandler asyncHttpResponseHandler) {
            client.put(uri, requestParams, asyncHttpResponseHandler);
        }
    };

    private HttpMethod(@NotNull ParamsLocation paramsLocation) {
        this.paramsLocation = paramsLocation;
    }

    private static enum ParamsLocation {
        in_uri {
            @NotNull
            @Override
            public String prepareUri(@NotNull String uri, @NotNull List<NameValuePair> params) {
                final StringBuilder result = new StringBuilder(uri);

                if (!params.isEmpty()) {
                    result.append("?");
                }

                boolean first = true;
                for (NameValuePair param : params) {
                    if (first) {
                        first = false;
                    } else {
                        result.append("&");
                    }
                    result.append(param.getName()).append("=").append(param.getValue());
                }

                return result.toString();
            }

            @Override
            public void addParams(@NotNull HttpRequestBase request, @NotNull List<NameValuePair> params) {
                // no additional parameters
            }
        },

        in_headers {
            @NotNull
            @Override
            public String prepareUri(@NotNull String uri, @NotNull List<NameValuePair> params) {
                return uri;
            }

            @Override
            public void addParams(@NotNull HttpRequestBase request, @NotNull List<NameValuePair> params) {
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

        @NotNull
        public abstract String prepareUri(@NotNull String uri, @NotNull List<NameValuePair> params);

        public abstract void addParams(@NotNull HttpRequestBase request, @NotNull List<NameValuePair> params);
    }

    @NotNull
    private final ParamsLocation paramsLocation;

    @NotNull
    protected abstract HttpRequestBase createRequest();

    @NotNull
    public HttpUriRequest createRequest(@NotNull String uri, @NotNull List<NameValuePair> params) {
        return createRequest(URI.create(paramsLocation.prepareUri(uri, params)), params);
    }

    @NotNull
    public HttpUriRequest createRequest(@NotNull URI uri, @NotNull List<NameValuePair> params) {
        final HttpRequestBase result = createRequest();
        result.setURI(uri);
        paramsLocation.addParams(result, params);
        return result;
    }

    public abstract void doAsyncRequest(@NotNull AsyncHttpClient client,
                                        @NotNull String uri,
                                        @Nullable RequestParams requestParams,
                                        @Nullable AsyncHttpResponseHandler asyncHttpResponseHandler);
}
