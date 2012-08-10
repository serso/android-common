package org.solovyev.android.samples.menu;

import android.content.Context;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import org.jetbrains.annotations.NotNull;
import org.solovyev.android.list.ListAdapter;
import org.solovyev.android.list.ListItem;
import org.solovyev.android.list.ListItemOnClickData;
import org.solovyev.android.list.SimpleMenuOnClick;
import org.solovyev.android.menu.LabeledMenuItem;
import org.solovyev.android.samples.R;
import org.solovyev.android.view.TextViewBuilder;

import java.util.Arrays;

/**
 * User: serso
 * Date: 8/10/12
 * Time: 1:39 PM
 */
public class MenuListItem implements ListItem<View> {

    private final int captionResId;

    private final int sortOrder;

    public MenuListItem(int captionResId, int sortOrder) {
        this.captionResId = captionResId;
        this.sortOrder = sortOrder;
    }

    @Override
    public OnClickAction getOnClickAction() {
        return new OnClickAction() {
            @Override
            public void onClick(@NotNull Context context, @NotNull ListAdapter<ListItem<? extends View>> adapter, @NotNull ListView listView) {
                Toast.makeText(context, context.getString(R.string.long_press_to_open_menu), Toast.LENGTH_SHORT).show();
            }
        };
    }

    @Override
    public OnClickAction getOnLongClickAction() {
        return new SimpleMenuOnClick<MenuListItem>(Arrays.asList(MenuItemMenu.values()), this);
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
        final TextView view = TextViewBuilder.newInstance(R.layout.menu_list_item, getTag()).build(context);

        fillView(context, view);

        return view;
    }

    private void fillView(@NotNull Context context, @NotNull TextView view) {
        view.setText(context.getString(captionResId));
    }

    @NotNull
    private String getTag() {
        return "menu_list_item";
    }

    public int getSortOrder() {
        return sortOrder;
    }

    private static enum MenuItemMenu implements LabeledMenuItem<ListItemOnClickData<MenuListItem>> {

        show_number(R.string.show_menu_number) {
            @Override
            public void onClick(@NotNull ListItemOnClickData<MenuListItem> data, @NotNull Context context) {
                Toast.makeText(context, context.getString(R.string.show_menu_number_text, String.valueOf(data.getDataObject().getSortOrder())), Toast.LENGTH_SHORT).show();
            }
        },

        show_name(R.string.show_menu_name) {
            @Override
            public void onClick(@NotNull ListItemOnClickData<MenuListItem> data, @NotNull Context context) {
                Toast.makeText(context, context.getString(R.string.show_menu_name_text, context.getString(data.getDataObject().captionResId)), Toast.LENGTH_SHORT).show();
            }
        };

        private final int captionResId;

        private MenuItemMenu(int captionResId) {
            this.captionResId = captionResId;
        }

        @NotNull
        @Override
        public String getCaption(@NotNull Context context) {
            return context.getString(captionResId);
        }
    }
}
