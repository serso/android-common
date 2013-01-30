/*
 * Copyright 2013 serso aka se.solovyev
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * ---------------------------------------------------------------------
 * Contact details
 *
 * Email: se.solovyev@gmail.com
 * Site:  http://se.solovyev.org
 */

package org.solovyev.android.menu;

import android.app.Activity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.solovyev.common.JPredicate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * User: serso
 * Date: 4/30/12
 * Time: 11:19 AM
 */

/**
 * Activity menu implementation based on the list. Allows to use filter in order
 * to change displayed menu items.
 */
public class ListActivityMenu<M, MI> implements ActivityMenu<M, MI> {

    private static final int NO_MENU_RES_ID = -1;

    @NotNull
    private final List<MenuItemWrapper<MI>> menuItems = new ArrayList<MenuItemWrapper<MI>>();

    private final int menuResId;

    @Nullable
    private final JPredicate<AMenuItem<MI>> filter;

    @NotNull
    private final MenuHelper<M, MI> menuHelper;

    private ListActivityMenu(@Nullable JPredicate<AMenuItem<MI>> filter,
                             @NotNull MenuHelper<M, MI> menuHelper) {
        this(NO_MENU_RES_ID, filter, menuHelper);
    }

    private ListActivityMenu(int menuResId,
                             @Nullable JPredicate<AMenuItem<MI>> filter,
                             @NotNull MenuHelper<M, MI> menuHelper) {
        this.menuResId = menuResId;
        this.filter = filter;
        this.menuHelper = menuHelper;
    }

    /*
    **********************************************************************
    *
    *                           PUBLIC CONSTRUCTORS
    *
    **********************************************************************
    */

    @NotNull
    public static <M, MI> ActivityMenu<M, MI> fromList(@NotNull List<? extends LabeledMenuItem<MI>> menuItems,
                                                       @NotNull MenuHelper<M, MI> menuHelper) {
        final ListActivityMenu<M, MI> result = new ListActivityMenu<M, MI>(null, menuHelper);

        for (LabeledMenuItem<MI> menuItem : menuItems) {
            result.menuItems.add(new MenuItemWrapper<MI>(menuItem));
        }

        return result;
    }

    @NotNull
    public static <M, MI> ActivityMenu<M, MI> fromList(@NotNull List<? extends LabeledMenuItem<MI>> menuItems,
                                                       @NotNull MenuHelper<M, MI> menuHelper,
                                                       @NotNull JPredicate<AMenuItem<MI>> filter) {
        final ListActivityMenu<M, MI> result = new ListActivityMenu<M, MI>(filter, menuHelper);

        for (LabeledMenuItem<MI> menuItem : menuItems) {
            result.menuItems.add(new MenuItemWrapper<MI>(menuItem));
        }

        return result;
    }

    @NotNull
    public static <M, MI, E extends Enum & LabeledMenuItem<MI>> ActivityMenu<M, MI> fromList(
            @NotNull Class<E> enumMenuClass,
            @NotNull MenuHelper<M, MI> menuHelper,
            @NotNull JPredicate<AMenuItem<MI>> filter) {
        return fromList(toList(enumMenuClass), menuHelper, filter);
    }

    @NotNull
    public static <M, MI, E extends Enum & LabeledMenuItem<MI>> ActivityMenu<M, MI> fromList(
            @NotNull Class<E> enumMenuClass,
            @NotNull MenuHelper<M, MI> menuHelper) {
        return fromList(toList(enumMenuClass), menuHelper);
    }

    @NotNull
    public static <M, MI> ActivityMenu<M, MI> fromResource(int menuResId,
                                                         @NotNull List<? extends IdentifiableMenuItem<MI>> menuItems,
                                                         @NotNull MenuHelper<M, MI> menuHelper,
                                                         @NotNull JPredicate<AMenuItem<MI>> filter) {
        final ListActivityMenu<M, MI> result = new ListActivityMenu<M, MI>(menuResId, filter, menuHelper);

        for (IdentifiableMenuItem<MI> menuItem : menuItems) {
            result.menuItems.add(new MenuItemWrapper<MI>(menuItem));
        }

        return result;
    }

    @NotNull
    public static <M, MI> ActivityMenu<M, MI> fromResource(int menuResId,
                                                         @NotNull List<? extends IdentifiableMenuItem<MI>> menuItems,
                                                         @NotNull MenuHelper<M, MI> menuHelper) {
        final ListActivityMenu<M, MI> result = new ListActivityMenu<M, MI>(menuResId, null, menuHelper);

        for (IdentifiableMenuItem<MI> menuItem : menuItems) {
            result.menuItems.add(new MenuItemWrapper<MI>(menuItem));
        }

        return result;
    }

    @NotNull
    public static <M, MI, E extends Enum & IdentifiableMenuItem<MI>> ActivityMenu<M, MI> fromResource(
            int menuResId,
            @NotNull Class<? extends E> enumMenuClass,
            @NotNull MenuHelper<M, MI> menuHelper,
            @NotNull JPredicate<AMenuItem<MI>> filter) {
        return fromResource(menuResId, toList(enumMenuClass), menuHelper, filter);
    }


    @NotNull
    public static <M, MI, E extends Enum & IdentifiableMenuItem<MI>> ActivityMenu<M, MI> fromResource(
            int menuResId,
            @NotNull Class<? extends E> enumMenuClass,
            @NotNull MenuHelper<M, MI> menuHelper) {
        return fromResource(menuResId, toList(enumMenuClass), menuHelper);
    }

    @NotNull
    private static <E extends Enum> List<E> toList(@NotNull Class<E> enumMenuClass) {
        final List<E> result = new ArrayList<E>();

        Collections.addAll(result, enumMenuClass.getEnumConstants());

        return result;
    }


    /*
    **********************************************************************
    *
    *                           METHODS
    *
    **********************************************************************
    */

    @Override
    public boolean onCreateOptionsMenu(@NotNull final Activity activity, @NotNull M menu) {
        if (filter == null) {
            if (isFromMenuRes()) {
                this.menuHelper.inflateMenu(activity, menuResId, menu);
            } else {
                for (final MenuItemWrapper<MI> menuItem : this.menuItems) {
                    addMenuItem(activity, menu, menuItem);
                }
            }
        }

        return true;
    }

    private boolean isFromMenuRes() {
        return menuResId != NO_MENU_RES_ID;
    }

    private void addMenuItem(@NotNull final Activity activity,
                             @NotNull final M menu,
                             @NotNull final MenuItemWrapper<MI> menuItemWrapper) {
        final int size = menuHelper.size(menu);
        final int menuItemId = size + 1;
        final MI aMenuItem = menuHelper.add(menu, 0, menuItemId, 0, menuItemWrapper.getCaption(activity));

        menuItemWrapper.setMenuItemId(menuItemId);

        menuHelper.setOnMenuItemClickListener(aMenuItem, menuItemWrapper.getMenuItem(), activity);
    }

    @Override
    public boolean onPrepareOptionsMenu(@NotNull Activity activity, @NotNull M menu) {
        if (filter != null) {
            if (isFromMenuRes()) {

                for (MenuItemWrapper<MI> menuItemWrapper : menuItems) {
                    // always remove
                    final Integer menuItemId = menuItemWrapper.getMenuItemId();
                    if (menuItemId != null) {
                        menuHelper.removeItem(menu, menuItemId);
                    }
                }

                this.menuHelper.inflateMenu(activity, menuResId, menu);

                for (MenuItemWrapper<MI> menuItemWrapper : menuItems) {
                    if (filter.apply(menuItemWrapper.getMenuItem())) {
                        // remove if needed
                        menuHelper.removeItem(menu, menuItemWrapper.getMenuItemId());
                    }
                }
            } else {
                for (MenuItemWrapper<MI> menuItemWrapper : menuItems) {
                    // always remove
                    final Integer menuItemId = menuItemWrapper.getMenuItemId();
                    if (menuItemId != null) {
                        menuHelper.removeItem(menu, menuItemId);
                    }

                    if (!filter.apply(menuItemWrapper.getMenuItem())) {
                        // and if needed -> add
                        addMenuItem(activity, menu, menuItemWrapper);
                    }
                }
            }
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NotNull Activity activity, @NotNull MI item) {
        if (isFromMenuRes()) {
            for (MenuItemWrapper<MI> menuItem : menuItems) {
                if (menuHelper.getItemId(item).equals(menuItem.getMenuItemId())) {
                    menuItem.getMenuItem().onClick(item, activity);
                    return true;
                }
            }
        }

        return false;
    }

}
