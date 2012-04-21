package org.solovyev.android.view;

import android.content.Context;
import android.view.View;
import org.jetbrains.annotations.NotNull;

/**
 * User: serso
 * Date: 4/19/12
 * Time: 4:10 PM
 */
public interface UpdatableViewBuilder<V extends View> extends ViewBuilder<V> {

    @NotNull
    V updateView(@NotNull Context context, @NotNull View view);
}
