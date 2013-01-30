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
import org.jetbrains.annotations.NotNull;

/**
 * User: serso
 * Date: 5/27/12
 * Time: 2:33 PM
 */
public abstract class AbstractHttpTransaction<R> implements HttpTransaction<R>, HttpTransactionDef {

    @NotNull
    private final HttpMethod httpMethod;

    @NotNull
    private final String uri;

    protected AbstractHttpTransaction(@NotNull String uri, @NotNull HttpMethod httpMethod) {
        this.httpMethod = httpMethod;
        this.uri = uri;
    }

    @Override
    @NotNull
    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    @Override
    @NotNull
    public String getUri() {
        return uri;
    }

    @NotNull
    @Override
    public HttpUriRequest createRequest() {
        return httpMethod.createRequest(uri, getRequestParameters());
    }

}
