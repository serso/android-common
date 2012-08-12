package org.solovyev.android.http;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.jetbrains.annotations.NotNull;
import org.solovyev.common.Converter;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

/**
 * User: serso
 * Date: 5/29/12
 * Time: 9:51 PM
 */
public class DownloadFileHttpTransaction<R> extends AbstractHttpTransaction<R> {

    @NotNull
    private final Converter<InputStream, R> fileConverter;

    public DownloadFileHttpTransaction(@NotNull String uri, @NotNull HttpMethod httpMethod, @NotNull Converter<InputStream, R> fileConverter) {
        super(uri, httpMethod);
        this.fileConverter = fileConverter;
    }

    @NotNull
    @Override
    public List<NameValuePair> getRequestParameters() {
        return Collections.emptyList();
    }

    @Override
    public R getResponse(@NotNull HttpResponse response) {
        try {
            return fileConverter.convert(response.getEntity().getContent());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
