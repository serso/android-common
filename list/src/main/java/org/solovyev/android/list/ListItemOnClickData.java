package org.solovyev.android.list;

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
    ListAdapter<? extends ListItem> getAdapter();

    @NotNull
    ListView getListView();
}
