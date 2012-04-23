package org.solovyev.android.menu;

import android.content.Context;
import org.jetbrains.annotations.NotNull;

/**
 * User: serso
 * Date: 4/23/12
 * Time: 2:05 PM
 */
public interface AMenuItem<T> {

    void onClick(@NotNull T data, @NotNull Context context);
}
