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
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
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

    public DownloadFileAsyncTask(@Nonnull Context context) {
        super(context);
    }

    public DownloadFileAsyncTask(@Nonnull Context context, @Nonnull OnPostExecute<List<Object>> onPostExecute) {
        super(context);
        this.onPostExecute = onPostExecute;
    }

    @Nonnull
    @Override
    protected List<Object> doWork(@Nonnull List<Input> params) {
        final List<Object> result = new ArrayList<Object>();
        for (Input param : params) {
            final DownloadFileHttpTransaction<?> downloadFileHttpTransaction = new DownloadFileHttpTransaction<Object>(param.getUri(), param.getMethod(), param.getFileConverter());
            try {
                result.add(HttpTransactions.execute(downloadFileHttpTransaction));
            } catch (IOException e) {
                throw new HttpRuntimeIoException(e);
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
    protected void onFailurePostExecute(@Nonnull Exception e) {
        if ( e instanceof HttpRuntimeIoException) {
            // no internet connection => ok
        } else {
            defaultOnFailurePostExecute(e);
        }
    }

    public static interface OnPostExecute<R> {
        void onPostExecute(@Nonnull R result);
    }

    public static class Input {

        @Nonnull
        private String uri;

        @Nonnull
        private HttpMethod method;

        @Nonnull
        private Converter<InputStream, ?> fileConverter;

        public Input(@Nonnull String uri, @Nonnull HttpMethod method, @Nonnull Converter<InputStream, ?> fileConverter) {
            this.uri = uri;
            this.method = method;
            this.fileConverter = fileConverter;
        }


        @Nonnull
        public String getUri() {
            return uri;
        }

        @Nonnull
        public HttpMethod getMethod() {
            return method;
        }

        @Nonnull
        public Converter<InputStream, Object> getFileConverter() {
            return (Converter<InputStream, Object>) fileConverter;
        }
    }
}
