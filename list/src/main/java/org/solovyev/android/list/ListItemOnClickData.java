package org.solovyev.android.list;

import android.view.View;
import android.widget.ListView;
import org.jetbrains.annotations.NotNull;

/**
 * User: serso
 * Date: 5/5/12
 * Time: 8:16 PM
 */
public interface ListItemOnClickData<T> {

    @NotNull
    T getDataObject();

    @NotNull
    ListAdapter<ListItem<? extends View>> getAdapter();

    @NotNull
    ListView getListView();
}
