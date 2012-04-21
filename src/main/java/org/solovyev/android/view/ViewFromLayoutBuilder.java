package org.solovyev.android.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import org.jetbrains.annotations.NotNull;

/**
 * User: serso
 * Date: 4/18/12
 * Time: 12:57 AM
 */
public class ViewFromLayoutBuilder<V extends View> implements ViewBuilder<V> {

    private final int layoutId;

    private final int viewId;

    private final boolean wholeLayout;

    private ViewFromLayoutBuilder(int layoutId, int viewId, boolean wholeLayout) {
        this.layoutId = layoutId;
        this.viewId = viewId;
        this.wholeLayout = wholeLayout;
    }

    @NotNull
    public static <V extends View> ViewBuilder<V> newInstance(int layoutId, int viewId) {
        return new ViewFromLayoutBuilder<V>(layoutId, viewId, false);
    }

    @NotNull
    public static <V extends View> ViewBuilder<V> newInstance(int layoutId) {
        return new ViewFromLayoutBuilder<V>(layoutId, 0, true);
    }

    @NotNull
    @Override
    public V build(@NotNull Context context) {
        final LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (wholeLayout) {
            // if whole layout - just return view
            return (V)layoutInflater.inflate(layoutId, null);
        } else {
            // else try to find view by id
            final ViewGroup itemView = (ViewGroup) layoutInflater.inflate(layoutId, null);
            return (V)itemView.findViewById(viewId);
        }
    }
}
