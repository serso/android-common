package org.solovyev.android.menu;

import android.app.Activity;
import android.view.Menu;
import android.view.MenuInflater;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * User: serso
 * Date: 4/23/12
 * Time: 1:57 PM
 */
@Deprecated
public class LayoutActivityMenu extends AbstractActivityMenu<IdentifiableMenuItem> {

    public final int menuLayoutId;

    private LayoutActivityMenu(int menuLayoutId) {
        this.menuLayoutId = menuLayoutId;
    }

    @NotNull
    public static <E extends Enum & IdentifiableMenuItem> ActivityMenu newInstance(int menuLayoutId, @NotNull Class<E> enumMenuClass) {
        final LayoutActivityMenu result = new LayoutActivityMenu(menuLayoutId);

        Collections.addAll(result.getMenuItems(), enumMenuClass.getEnumConstants());

        return result;
    }

    @NotNull
    public static ActivityMenu newInstance(int menuLayoutId, @NotNull List<IdentifiableMenuItem> menuItems) {
        final LayoutActivityMenu result = new LayoutActivityMenu(menuLayoutId);

        result.addAll(menuItems);

        return result;
    }

    @Override
    public boolean onCreateOptionsMenu(@NotNull Activity activity, @NotNull Menu menu) {
        final MenuInflater menuInflater = activity.getMenuInflater();
        menuInflater.inflate(menuLayoutId, menu);
        return true;
    }
}
