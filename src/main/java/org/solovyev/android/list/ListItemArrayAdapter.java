package org.solovyev.android.list;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * User: serso
 * Date: 4/14/12
 * Time: 8:02 PM
 */
public class ListItemArrayAdapter extends ListAdapter<ListItem<? extends View>> {


    public ListItemArrayAdapter(@NotNull Context context,
                                @NotNull List<ListItem<? extends View>> listItems) {
        super(context, 0, 0, listItems);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ListItem listItem = getItem(position);

        if (convertView == null) {
            return listItem.build(getContext());
        } else {
            return listItem.updateView(getContext(), convertView);
        }
    }
}
