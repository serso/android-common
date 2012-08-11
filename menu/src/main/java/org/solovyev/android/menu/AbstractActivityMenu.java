package org.solovyev.android.menu;

import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * User: serso
 * Date: 4/30/12
 * Time: 11:19 AM
 */
@Deprecated
abstract class AbstractActivityMenu<I extends IdentifiableMenuItem> implements ActivityMenu<Menu, MenuItem> {

    @NotNull
    private List<I> menuItems = new ArrayList<I>();

    protected boolean addAll(Collection<? extends I> activityMenuItems) {
        return menuItems.addAll(activityMenuItems);
    }

    @NotNull
    protected List<I> getMenuItems() {
        return menuItems;
    }

    @Override
    public boolean onOptionsItemSelected(@NotNull Activity activity, @NotNull MenuItem item) {
        for (I menuItem : menuItems) {
            if (menuItem.getItemId().equals(item.getItemId())) {
                menuItem.onClick(item, activity);
                return true;
            }
        }

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(@NotNull Activity activity, @NotNull Menu menu) {
        return true;
    }
}
