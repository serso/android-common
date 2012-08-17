package org.solovyev.android.http;

import android.widget.ImageView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * User: serso
 * Date: 8/17/12
 * Time: 11:41 AM
 */
public interface ImageLoader {

    void loadImage(@NotNull String url, @NotNull ImageView imageView, @Nullable Integer defaultImageId);

    void loadImage(@NotNull String url, @NotNull OnImageLoadedListener imageLoadedListener);

    void loadImage(@NotNull String url);

}
