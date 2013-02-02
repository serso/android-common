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

import android.content.Context;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.solovyev.android.RuntimeIoException;
import org.solovyev.android.async.CommonAsyncTask;
import org.solovyev.common.Converter;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * User: serso
 * Date: 5/29/12
 * Time: 9:50 PM
 */
public class DownloadFileAsyncTask extends CommonAsyncTask<DownloadFileAsyncTask.Input, Integer, List<Object>> {

    @Nullable
    private OnPostExecute<List<Object>> onPostExecute;

    public DownloadFileAsyncTask(@NotNull Context context) {
        super(context);
    }

    public DownloadFileAsyncTask(@NotNull Context context, @NotNull OnPostExecute<List<Object>> onPostExecute) {
        super(context);
        this.onPostExecute = onPostExecute;
    }

    @NotNull
    @Override
    protected List<Object> doWork(@NotNull List<Input> params) {
        final List<Object> result = new ArrayList<Object>();
        for (Input param : params) {
            final DownloadFileHttpTransaction<?> downloadFileHttpTransaction = new DownloadFileHttpTransaction<Object>(param.getUri(), param.getMethod(), param.getFileConverter());
            try {
                result.add(HttpTransactions.execute(downloadFileHttpTransaction));
            } catch (IOException e) {
                throw new RuntimeIoException(e);
            }
        }

        return result;
    }

    @Override
    protected void onSuccessPostExecute(@Nullable List<Object> result) {
        if ( onPostExecute != null ) {
            assert result != null;
            onPostExecute.onPostExecute(result);
        }
    }

    @Override
    protected void onFailurePostExecute(@NotNull Exception e) {
        if ( e instanceof RuntimeIoException ) {
            // no internet connection => ok
        } else {
            defaultOnFailurePostExecute(e);
        }
    }

    public static interface OnPostExecute<R> {
        void onPostExecute(@NotNull R result);
    }

    public static class Input {

        @NotNull
        private String uri;

        @NotNull
        private HttpMethod method;

        @NotNull
        private Converter<InputStream, ?> fileConverter;

        public Input(@NotNull String uri, @NotNull HttpMethod method, @NotNull Converter<InputStream, ?> fileConverter) {
            this.uri = uri;
            this.method = method;
            this.fileConverter = fileConverter;
        }


        @NotNull
        public String getUri() {
            return uri;
        }

        @NotNull
        public HttpMethod getMethod() {
            return method;
        }

        @NotNull
        public Converter<InputStream, Object> getFileConverter() {
            return (Converter<InputStream, Object>) fileConverter;
        }
    }
}
