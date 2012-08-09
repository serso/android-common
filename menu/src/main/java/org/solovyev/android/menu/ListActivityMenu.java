package org.solovyev.android.menu;

import android.app.Activity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.solovyev.android.IFilter;

import java.util.ArrayList;
import java.util.List;

/**
 * User: serso
 * Date: 4/30/12
 * Time: 11:19 AM
 */
public class ListActivityMenu<M, MI> implements ActivityMenu<M, MI> {

    @NotNull
    private final List<MenuItemWrapper<MI>> menuItems = new ArrayList<MenuItemWrapper<MI>>();

    @Nullable
    private final IFilter<LabeledMenuItem<MI>> filter;

    @NotNull
    private final MenuHelper<M, MI> menuHelper;

    public ListActivityMenu(@Nullable IFilter<LabeledMenuItem<MI>> filter,
                            @NotNull MenuHelper<M, MI> menuHelper) {
        this.filter = filter;
        this.menuHelper = menuHelper;
    }

    @NotNull
    public static <M, MI> ActivityMenu<M, MI> newInstance(@NotNull List<LabeledMenuItem<MI>> menuItems,
                                                          @NotNull MenuHelper<M, MI> menuHelper) {
        final ListActivityMenu<M, MI> result = new ListActivityMenu<M, MI>(null, menuHelper);

        for (LabeledMenuItem<MI> menuItem : menuItems) {
            result.menuItems.add(new MenuItemWrapper<MI>(menuItem));
        }

        return result;
    }

    @NotNull
    public static <M, MI> ActivityMenu<M, MI> newInstance(@NotNull List<LabeledMenuItem<MI>> menuItems,
                                                          @NotNull MenuHelper<M, MI> menuHelper,
                                                          @NotNull IFilter<LabeledMenuItem<MI>> filter) {
        final ListActivityMenu<M, MI> result = new ListActivityMenu<M, MI>(filter, menuHelper);

        for (LabeledMenuItem<MI> menuItem : menuItems) {
            result.menuItems.add(new MenuItemWrapper<MI>(menuItem));
        }

        return result;
    }

    @Override
    public boolean onCreateOptionsMenu(@NotNull final Activity activity, @NotNull M menu) {
        if (filter == null) {
            for (final MenuItemWrapper<MI> menuItem : this.menuItems) {
                addMenuItem(activity, menu, menuItem);
            }
        }

        return true;
    }

    private void addMenuItem(@NotNull final Activity activity,
                             @NotNull final M menu,
                             @NotNull final MenuItemWrapper<MI> menuItemWrapper) {
        final int size = menuHelper.size(menu);
        final int menuItemId = size + 1;
        final MI aMenuItem = menuHelper.add(menu, 0, menuItemId, 0, menuItemWrapper.menuItem.getCaption(activity));

        menuItemWrapper.menuItemId = menuItemId;

        menuHelper.setOnMenuItemClickListener(aMenuItem, menuItemWrapper.menuItem, activity);
    }

    @Override
    public boolean onPrepareOptionsMenu(@NotNull Activity activity, @NotNull M menu) {
        if (filter != null) {
            for (MenuItemWrapper<MI> menuItemWrapper : menuItems) {
                // always remove
                final Integer menuItemId = menuItemWrapper.menuItemId;
                if (menuItemId != null) {
                    menuHelper.removeItem(menu, menuItemId);
                }

                if (!filter.isFiltered(menuItemWrapper.menuItem)) {
                    // and if needed -> add
                    addMenuItem(activity, menu, menuItemWrapper);
                }
            }
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NotNull Activity activity, @NotNull MI item) {
        return false;
    }

    private static class MenuItemWrapper<MI> {

        @NotNull
        private final LabeledMenuItem<MI> menuItem;

        @Nullable
        private Integer menuItemId;

        private MenuItemWrapper(@NotNull LabeledMenuItem<MI> menuItem) {
            this.menuItem = menuItem;
        }
    }
}
