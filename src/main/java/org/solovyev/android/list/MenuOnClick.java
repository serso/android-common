package org.solovyev.android.list;

import android.content.Context;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import org.jetbrains.annotations.NotNull;
import org.solovyev.android.menu.AMenuBuilder;
import org.solovyev.android.menu.LabeledMenuItem;
import org.solovyev.android.menu.MenuImpl;

import java.util.List;

/**
 * User: serso
 * Date: 5/5/12
 * Time: 7:52 PM
 */
public abstract class MenuOnClick<T> implements ListItem.OnClickAction {

    @NotNull
    private final List<? extends LabeledMenuItem<ListItemOnClickData<T>>> menuItems;

    protected MenuOnClick(@NotNull List<? extends LabeledMenuItem<ListItemOnClickData<T>>> menuItems) {
        this.menuItems = menuItems;
    }

    @Override
    public void onClick(@NotNull Context context, @NotNull ArrayAdapter<ListItem<? extends View>> adapter, @NotNull ListView listView) {
        if (!menuItems.isEmpty()) {
            AMenuBuilder.newInstance(context, MenuImpl.newInstance(menuItems)).create(new ListItemOnClickDataImpl<T>(getData(), adapter, listView)).show();
        }
    }

    @NotNull
    protected abstract T getData();
}
