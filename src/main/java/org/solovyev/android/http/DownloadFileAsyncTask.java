package org.solovyev.android.http;

import android.os.AsyncTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.solovyev.common.utils.Converter;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * User: serso
 * Date: 5/29/12
 * Time: 9:50 PM
 */
public class DownloadFileAsyncTask extends AsyncTask<DownloadFileAsyncTask.Input, Integer, List<Object>> {

    @Nullable
    private OnPostExecute<List<Object>> onPostExecute;

    public DownloadFileAsyncTask() {
    }

    public DownloadFileAsyncTask(@NotNull OnPostExecute<List<Object>> onPostExecute) {
        this.onPostExecute = onPostExecute;
    }

    @NotNull
    @Override
    protected List<Object> doInBackground(@NotNull Input... params) {
        final List<Object> result = new ArrayList<Object>();
        for (Input param : params) {
            final DownloadFileHttpTransaction<?> downloadFileHttpTransaction = new DownloadFileHttpTransaction<Object>(param.getUri(), param.getMethod(), param.getFileConverter());
            try {
                result.add(AndroidHttpUtils.execute(downloadFileHttpTransaction));
            } catch (IOException e) {
                throw new RuntimeIoException(e);
            }
        }

        return result;
    }

    @Override
    protected void onPostExecute(@NotNull List<Object> objects) {
        super.onPostExecute(objects);
        if ( onPostExecute != null ) {
            onPostExecute.onPostExecute(objects);
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
