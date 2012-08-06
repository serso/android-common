package org.solovyev.android.menu;

import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import org.jetbrains.annotations.NotNull;

/**
 * User: serso
 * Date: 8/4/12
 * Time: 2:51 PM
 */
public final class AndroidMenuHelper implements MenuHelper<Menu, MenuItem> {

    @NotNull
    private static final AndroidMenuHelper instance = new AndroidMenuHelper();

    @NotNull
    public static MenuHelper<Menu, MenuItem> getInstance() {
        return instance;
    }

    private AndroidMenuHelper() {
    }

    @Override
    public int size(@NotNull Menu menu) {
        return menu.size();
    }

    @NotNull
    @Override
    public MenuItem add(@NotNull Menu menu, int groupId, int itemId, int orderId, @NotNull String caption) {
        return menu.add(groupId, itemId, orderId, caption);
    }

    @NotNull
    @Override
    public Integer getItemId(@NotNull MenuItem menuItem) {
        return menuItem.getItemId();
    }

    @Override
    public void setOnMenuItemClickListener(@NotNull final MenuItem menuItem, @NotNull final AMenuItem<MenuItem> onMenuItemClick, @NotNull final Activity activity) {
        menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                onMenuItemClick.onClick(menuItem, activity);
                return true;
            }
        });
    }

    @Override
    public void removeItem(@NotNull Menu menu, @NotNull Integer menuItemId) {
        menu.removeItem(menuItemId);
    }
}
