package org.solovyev.android.menu;

import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.view.MenuItem;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * User: serso
 * Date: 4/30/12
 * Time: 11:19 AM
 */
public class ListActivityMenu extends AbstractActivityMenu<ListActivityMenu.MutableLabeledActivityMenuItem> {

    @NotNull
    public static ActivityMenu newInstance(@NotNull List<LabeledActivityMenuItem> menuItems) {
        final ListActivityMenu result = new ListActivityMenu();

        final List<MutableLabeledActivityMenuItem> mutableMenuItems = new ArrayList<MutableLabeledActivityMenuItem>(menuItems.size());
        for (LabeledActivityMenuItem menuItem : menuItems) {
            mutableMenuItems.add(new MutableLabeledActivityMenuItem(menuItem));
        }
        result.addAll(mutableMenuItems);

        return result;
    }

    @Override
    public boolean onCreateOptionsMenu(@NotNull Activity activity, @NotNull Menu menu) {
        for (MutableLabeledActivityMenuItem menuItem : getMenuItems()) {
            menuItem.setItemId(menu.add(menuItem.getCaption(activity)).getItemId());
        }

        return activity.onCreateOptionsMenu(menu);
    }

    static class MutableLabeledActivityMenuItem implements LabeledActivityMenuItem {

        @NotNull
        private LabeledActivityMenuItem labeledActivityMenuItem;

        @NotNull
        private Integer itemId;

        private MutableLabeledActivityMenuItem(@NotNull LabeledActivityMenuItem labeledActivityMenuItem) {
            this.labeledActivityMenuItem = labeledActivityMenuItem;
            this.itemId = labeledActivityMenuItem.getItemId();
        }

        @Override
        @NotNull
        public Integer getItemId() {
            return this.itemId;
        }

        public void setItemId(@NotNull Integer itemId) {
            this.itemId = itemId;
        }

        @Override
        public void onClick(@NotNull MenuItem data, @NotNull Context context) {
            labeledActivityMenuItem.onClick(data, context);
        }

        @Override
        @NotNull
        public String getCaption(@NotNull Context context) {
            return labeledActivityMenuItem.getCaption(context);
        }
    }
}
