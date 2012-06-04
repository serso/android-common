package org.solovyev.android.http;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * User: serso
 * Date: 6/2/12
 * Time: 11:21 PM
 */
public interface RemoteFileEventListener {

    void onEvent(@NotNull String uri, @NotNull RemoteFileEventType eventType, @Nullable Object data);
}
