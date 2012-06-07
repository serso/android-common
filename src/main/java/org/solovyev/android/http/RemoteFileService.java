package org.solovyev.android.http;

import android.content.Context;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.solovyev.common.utils.Converter;

import java.io.InputStream;
import java.util.List;

/**
 * User: serso
 * Date: 6/2/12
 * Time: 11:18 PM
 */
public interface RemoteFileService {

    void loadFile(@NotNull Context context,
                  @NotNull String uri,
                  @NotNull HttpMethod method,
                  @NotNull Converter<InputStream, ?> fileConverter,
                  @Nullable DownloadFileAsyncTask.OnPostExecute<List<Object>> onPostExecute);
}
