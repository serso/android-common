package org.solovyev.android.menu;

import android.app.Activity;
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
public class ListActivityMenu implements ActivityMenu {

    @NotNull
    private final List<LabeledMenuItem<MenuItem>> menuItems = new ArrayList<LabeledMenuItem<MenuItem>>();

    @NotNull
    public static ActivityMenu newInstance(@NotNull List<LabeledMenuItem<MenuItem>> menuItems) {
        final ListActivityMenu result = new ListActivityMenu();

        result.menuItems.addAll(menuItems);

        return result;
    }

    @Override
    public boolean onCreateOptionsMenu(@NotNull final Activity activity, @NotNull Menu menu) {
        for (final LabeledMenuItem<MenuItem> labeledMenuItem : this.menuItems) {
            final MenuItem menuItem = menu.add(labeledMenuItem.getCaption(activity));

            menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    labeledMenuItem.onClick(menuItem, activity);
                    return true;
                }
            });
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NotNull Activity activity, @NotNull MenuItem item) {
        return false;
    }

}
