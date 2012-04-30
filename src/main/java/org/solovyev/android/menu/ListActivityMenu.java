package org.solovyev.android.menu;

import android.app.Activity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * User: serso
 * Date: 4/23/12
 * Time: 1:57 PM
 */
public class ListActivityMenu implements ActivityMenu {

    public final int menuLayoutId;

    @NotNull
    private List<ActivityMenuItem> menuItems;

    private ListActivityMenu(int menuLayoutId) {
        this.menuLayoutId = menuLayoutId;
    }

    public static <E extends Enum & ActivityMenuItem> ActivityMenu newInstance(int menuLayoutId, @NotNull Class<E> enumMenuClass) {
        final ListActivityMenu result = new ListActivityMenu(menuLayoutId);

        result.menuItems = new ArrayList<ActivityMenuItem>(enumMenuClass.getEnumConstants().length);

        Collections.addAll(result.menuItems, enumMenuClass.getEnumConstants());

        return result;
    }

    public static ActivityMenu newInstance(int menuLayoutId, @NotNull List<ActivityMenuItem> menuItems) {
        final ListActivityMenu result = new ListActivityMenu(menuLayoutId);

        result.menuItems = menuItems;

        return result;
    }


    @Override
    public boolean onCreateOptionsMenu(@NotNull Activity activity, @NotNull Menu menu) {
        final MenuInflater menuInflater = activity.getMenuInflater();
        menuInflater.inflate(menuLayoutId, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NotNull Activity activity, @NotNull MenuItem item) {
        for (ActivityMenuItem menuItem : menuItems) {
            if (menuItem.getItemId().equals(item.getItemId())) {
                menuItem.onClick(item, activity);
                return true;
            }
        }

        return activity.onOptionsItemSelected(item);
    }

}
