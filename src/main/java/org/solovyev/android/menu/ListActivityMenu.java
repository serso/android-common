package org.solovyev.android.menu;

import android.app.Activity;
import android.view.Menu;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * User: serso
 * Date: 4/30/12
 * Time: 11:19 AM
 */
public class ListActivityMenu extends AbstractActivityMenu<LabeledActivityMenuItem> {

    @NotNull
    public static ActivityMenu newInstance(@NotNull List<LabeledActivityMenuItem> menuItems) {
        final ListActivityMenu result = new ListActivityMenu();

        result.addAll(menuItems);

        return result;
    }

    @Override
    public boolean onCreateOptionsMenu(@NotNull Activity activity, @NotNull Menu menu) {
        for (LabeledActivityMenuItem labeledActivityMenuItem : getMenuItems()) {
            menu.add(labeledActivityMenuItem.getCaption(activity));
        }

        return activity.onCreateOptionsMenu(menu);
    }
}
