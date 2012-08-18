package org.solovyev.android.list;

import org.jetbrains.annotations.NotNull;
import org.solovyev.android.menu.LabeledMenuItem;

import java.util.List;

/**
 * User: serso
 * Date: 5/5/12
 * Time: 7:58 PM
 */
// class for types object to be passed on click
public class SimpleMenuOnClick<T> extends MenuOnClick<T> {

    @NotNull
    private final T data;

    public SimpleMenuOnClick(@NotNull List<? extends LabeledMenuItem<ListItemOnClickData<T>>> labeledMenuItems, @NotNull T data) {
        super(labeledMenuItems);
        this.data = data;
    }

    @NotNull
    @Override
    protected T getData() {
        return this.data;
    }
}
