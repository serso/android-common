package org.solovyev.android.http;

import android.util.Log;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.solovyev.common.utils.CollectionsUtils;
import org.solovyev.common.utils.Converter;

import java.io.InputStream;
import java.util.*;
import java.util.concurrent.RejectedExecutionException;

/**
 * User: serso
 * Date: 6/2/12
 * Time: 11:22 PM
 */
public class HttpRemoteFileService implements RemoteFileService {

    private static final String TAG = "RemoteFileService";

    /*
    **********************************************************************
    *
    *                           SERVICE
    *
    **********************************************************************
    */


    @NotNull
    private final Map<String, Object> cache = new HashMap<String, Object>();

    @Override
    public void loadFile(@NotNull final String uri,
                         @NotNull final HttpMethod method,
                         @NotNull final Converter<InputStream, ?> fileConverter,
                         @Nullable final DownloadFileAsyncTask.OnPostExecute<List<Object>> onPostExecute) {
        final LoadRemoteFileAsyncTask loadRemoteFileAsyncTask = createTask(onPostExecute);

        final DownloadFileAsyncTask.Input input = new DownloadFileAsyncTask.Input(uri, method, fileConverter);
        try {
            loadRemoteFileAsyncTask.execute(input);
        } catch (RejectedExecutionException e) {
            // not enough "free" threads
            if (onPostExecute != null) {
                onPostExecute.onPostExecute(Collections.emptyList());
            }
        }
    }

    @NotNull
    private LoadRemoteFileAsyncTask createTask(@Nullable DownloadFileAsyncTask.OnPostExecute<List<Object>> onPostExecute) {
        final LoadRemoteFileAsyncTask loadRemoteFileAsyncTask;

        if (onPostExecute != null) {
            loadRemoteFileAsyncTask = new LoadRemoteFileAsyncTask(this, onPostExecute);
        } else {
            loadRemoteFileAsyncTask = new LoadRemoteFileAsyncTask(this);
        }
        return loadRemoteFileAsyncTask;
    }

    private static class LoadRemoteFileAsyncTask extends DownloadFileAsyncTask {

        @NotNull
        private HttpRemoteFileService httpRemoteFileService;


        private LoadRemoteFileAsyncTask(@NotNull HttpRemoteFileService httpRemoteFileService) {
            this.httpRemoteFileService = httpRemoteFileService;
        }

        private LoadRemoteFileAsyncTask(@NotNull HttpRemoteFileService httpRemoteFileService, @NotNull OnPostExecute<List<Object>> onPostExecute) {
            super(onPostExecute);
            this.httpRemoteFileService = httpRemoteFileService;
        }

        @NotNull
        @Override
        protected List<Object> doInBackground(@NotNull Input... params) {
            final List<Input> inputsForLookup = CollectionsUtils.asList(params);
            final List<Input> inputsForRemoteLoad = new ArrayList<Input>(params.length);

            final List<Object> result = new ArrayList<Object>(inputsForLookup.size());

            synchronized (httpRemoteFileService.cache) {
                for (Input inputForLookup : inputsForLookup) {
                    final Object objectFromCache = httpRemoteFileService.cache.get(inputForLookup.getUri());
                    if (objectFromCache != null) {
                        result.add(objectFromCache);
                    } else {
                        inputsForRemoteLoad.add(inputForLookup);
                    }
                }
            }

            if (!inputsForRemoteLoad.isEmpty()) {
                // NOTE: doInBackground method instead of execute as we are already "in background"
                try {
                    final List<Object> objectsFromRemoteServer = super.doInBackground(inputsForRemoteLoad.toArray(new Input[inputsForRemoteLoad.size()]));

                    for (int i = 0; i < inputsForRemoteLoad.size(); i++) {
                        final Object objectFromRemoteServer = objectsFromRemoteServer.get(i);
                        synchronized (httpRemoteFileService.cache) {
                            httpRemoteFileService.cache.put(inputsForRemoteLoad.get(i).getUri(), objectFromRemoteServer);
                        }
                        result.add(objectFromRemoteServer);
                    }
                } catch (RuntimeException e) {
                    Log.e(TAG, e.getMessage(), e);
                }
            }

            return result;
        }
    }
}
