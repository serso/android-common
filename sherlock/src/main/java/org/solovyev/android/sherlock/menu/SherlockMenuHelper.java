package org.solovyev.android.sherlock.menu;

import android.app.Activity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import org.jetbrains.annotations.NotNull;
import org.solovyev.android.menu.AMenuItem;
import org.solovyev.android.menu.MenuHelper;
import org.solovyev.android.sherlock.SherlockUtils;

/**
 * User: serso
 * Date: 8/4/12
 * Time: 3:02 PM
 */
public final class SherlockMenuHelper implements MenuHelper<Menu, MenuItem> {

    @NotNull
    private static final SherlockMenuHelper instance = new SherlockMenuHelper();

    @NotNull
    public static MenuHelper<Menu, MenuItem> getInstance() {
        return instance;
    }

    private SherlockMenuHelper() {
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

    @Override
    public void inflateMenu(@NotNull Activity activity, int layoutId, @NotNull Menu menu) {
        SherlockUtils.getSupportMenuInflater(activity).inflate(layoutId, menu);
    }

    @NotNull
    @Override
    public Integer getItemId(@NotNull MenuItem item) {
        return item.getItemId();
    }

}
