package org.solovyev.android.menu;

import android.content.Context;
import org.jetbrains.annotations.NotNull;

/**
 * User: serso
 * Date: 8/2/12
 * Time: 4:01 PM
 */
public abstract class AbstractLabeledMenuItem<T> implements LabeledMenuItem<T> {

    private final int captionResId;

    protected AbstractLabeledMenuItem(int captionResId) {
        this.captionResId = captionResId;
    }

    @NotNull
    @Override
    public String getCaption(@NotNull Context context) {
        return context.getString(captionResId);
    }
}
