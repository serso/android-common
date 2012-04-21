package org.solovyev.android.list;

import android.content.Context;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import org.jetbrains.annotations.NotNull;
import org.solovyev.android.view.UpdatableViewBuilder;

/**
 * User: serso
 * Date: 4/14/12
 * Time: 4:28 PM
 */
public interface ListItem<V extends View> extends UpdatableViewBuilder<V> {

    static final String TAG = "ListItem";

    @NotNull
    OnClickAction getOnClickAction();

    public static interface OnClickAction {

        void onClick(@NotNull Context context, @NotNull ArrayAdapter<ListItem<? extends View>> adapter, @NotNull ListView listView);
    }
}
