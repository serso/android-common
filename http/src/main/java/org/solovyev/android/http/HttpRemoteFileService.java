package org.solovyev.android.http;

import android.content.Context;
import android.widget.ImageView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
    private final ImageLoader imageLoader;

    public HttpRemoteFileService(@NotNull Context context, @NotNull String cacheFileName) {
        this.imageLoader = new ImageLoaderImpl(context, cacheFileName);
    }

    @Override
    public void loadImage(@NotNull String uri,
                          @NotNull ImageView imageView,
                          @Nullable Integer defaultImageId) {
        imageLoader.loadImage(uri, imageView, defaultImageId);
    }

    @Override
    public void loadImage(@NotNull String uri, @NotNull OnImageLoadedListener imageLoadedListener) {
        imageLoader.loadImage(uri, imageLoadedListener);
    }

    @Override
    public void loadImage(@NotNull String uri) {
        imageLoader.loadImage(uri);
    }
}
