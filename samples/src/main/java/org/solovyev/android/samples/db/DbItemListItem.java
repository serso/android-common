package org.solovyev.android.samples.db;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import org.jetbrains.annotations.NotNull;
import org.solovyev.android.list.ListItem;
import org.solovyev.android.samples.R;
import org.solovyev.android.view.TextViewBuilder;

/**
 * User: serso
 * Date: 8/10/12
 * Time: 5:06 PM
 */
public class DbItemListItem implements ListItem {

    @NotNull
    private final DbItem dbItem;

    public DbItemListItem(@NotNull DbItem dbItem) {
        this.dbItem = dbItem;
    }

    @Override
    public OnClickAction getOnClickAction() {
        return null;
    }

    @Override
    public OnClickAction getOnLongClickAction() {
        return null;
    }

    @NotNull
    @Override
    public View updateView(@NotNull Context context, @NotNull View view) {
        if (this.getTag().equals(view.getTag())) {
            fillView(context, (TextView) view);
            return view;
        } else {
            return build(context);
        }
    }

    @NotNull
    @Override
    public View build(@NotNull Context context) {
        final TextView view = TextViewBuilder.newInstance(R.layout.db_list_item, getTag()).build(context);

        fillView(context, view);

        return view;
    }

    @NotNull
    private String getTag() {
        return "db_list_item";
    }

    private void fillView(@NotNull Context context, @NotNull TextView view) {
        view.setText(dbItem.getName());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DbItemListItem)) return false;

        DbItemListItem that = (DbItemListItem) o;

        if (!dbItem.equals(that.dbItem)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return dbItem.hashCode();
    }
}
