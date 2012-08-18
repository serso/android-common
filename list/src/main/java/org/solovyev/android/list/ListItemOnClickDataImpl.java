package org.solovyev.android.list;

import android.widget.ListView;
import org.jetbrains.annotations.NotNull;

/**
 * User: serso
 * Date: 5/5/12
 * Time: 8:17 PM
 */
public class ListItemOnClickDataImpl<T> implements ListItemOnClickData<T> {

    @NotNull
    private T dataObject;

    @NotNull
    private ListAdapter<? extends ListItem> adapter;

    @NotNull
    private ListView listView;

    public ListItemOnClickDataImpl(@NotNull T dataObject, @NotNull ListAdapter<? extends ListItem> adapter, @NotNull ListView listView) {
        this.dataObject = dataObject;
        this.adapter = adapter;
        this.listView = listView;
    }

    @NotNull
    @Override
    public T getDataObject() {
        return this.dataObject;
    }

    @NotNull
    @Override
    public ListAdapter<? extends ListItem> getAdapter() {
        return this.adapter;
    }

    @NotNull
    @Override
    public ListView getListView() {
        return this.listView;
    }
}
