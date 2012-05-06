package org.solovyev.android.list;

import android.view.View;
import android.widget.ArrayAdapter;
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
    private ArrayAdapter<ListItem<? extends View>> adapter;

    @NotNull
    private ListView listView;

    public ListItemOnClickDataImpl(@NotNull T dataObject, @NotNull ArrayAdapter<ListItem<? extends View>> adapter, @NotNull ListView listView) {
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
    public ArrayAdapter<ListItem<? extends View>> getAdapter() {
        return this.adapter;
    }

    @NotNull
    @Override
    public ListView getListView() {
        return this.listView;
    }
}
