package org.solovyev.android.view;

import android.content.Context;
import android.view.View;
import org.jetbrains.annotations.NotNull;

/**
 * User: serso
 * Date: 4/18/12
 * Time: 12:55 AM
 */
public interface ViewBuilder<V extends View> {

    @NotNull
    V build(@NotNull Context context);
}
