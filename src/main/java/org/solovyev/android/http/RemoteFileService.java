package org.solovyev.android.http;

import android.widget.ImageView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * User: serso
 * Date: 6/2/12
 * Time: 11:18 PM
 */
public interface RemoteFileService {

    void loadImage(@NotNull String uri,
                   @NotNull ImageView imageView,
                   @Nullable Integer defaultImageId);

    void loadImage(@NotNull String uri);
}
