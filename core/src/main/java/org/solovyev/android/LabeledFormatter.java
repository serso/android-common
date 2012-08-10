package org.solovyev.android;

import android.content.Context;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.solovyev.common.text.Formatter;

import java.lang.ref.WeakReference;

/**
 * User: serso
 * Date: 8/10/12
 * Time: 1:01 PM
 */
public class LabeledFormatter<T extends Labeled> implements Formatter<T> {

    @NotNull
    private WeakReference<Context> contextRef;

    public LabeledFormatter(@NotNull Context context) {
        this.contextRef = new WeakReference<Context>(context);
    }

    @Override
    public String formatValue(@Nullable T value) throws IllegalArgumentException {
        final Context context = contextRef.get();
        if (context != null) {
            return context.getString(value.getCaptionResId());
        } else {
            return String.valueOf(value);
        }
    }
}
