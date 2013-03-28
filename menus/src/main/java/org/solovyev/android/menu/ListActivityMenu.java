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
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Contact details
 *
 * Email: se.solovyev@gmail.com
 * Site:  http://se.solovyev.org
 */

package org.solovyev.android.menu;

import android.app.Activity;
import org.solovyev.common.JPredicate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * User: serso
 * Date: 4/30/12
 * Time: 11:19 AM
 */

/**
 * Activity menu implementation. Allows to use filter in order
 * to change displayed menu items.
 *
 * Currently there are two strategies of creating menu:
 * 1. From Android resource file
 * 2. Programatically
 *
 * In spite of the strategies menu can be filtered with {@link JPredicate}.
 * Note: different strategies are used for creating menu with or without filter.
 */
public class ListActivityMenu<M, MI> implements ActivityMenu<M, MI> {

    private static final int NO_MENU_RES_ID = -1;

    @Nonnull
    private final List<MenuItemWrapper<MI>> menuItems = new ArrayList<MenuItemWrapper<MI>>();

    private final int menuResId;

    @Nullable
    private final JPredicate<AMenuItem<MI>> filter;

    @Nonnull
    private final MenuHelper<M, MI> menuHelper;

    private ListActivityMenu(@Nullable JPredicate<AMenuItem<MI>> filter,
                             @Nonnull MenuHelper<M, MI> menuHelper) {
        this(NO_MENU_RES_ID, filter, menuHelper);
    }

    private ListActivityMenu(int menuResId,
                             @Nullable JPredicate<AMenuItem<MI>> filter,
                             @Nonnull MenuHelper<M, MI> menuHelper) {
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

    /**
     * Creates simple menu from list of menu items.
     * Note: you need to add method calls of {@link ActivityMenu} in your Activity or Fragment
     *
     * @param menuItems menu items
     * @param menuHelper menu helper. See {@link AndroidMenuHelper}
     * @param <M> menu type
     * @param <MI> menu item type
     *
     * @return constructed menu
     */
    @Nonnull
    public static <M, MI> ActivityMenu<M, MI> fromList(@Nonnull List<? extends LabeledMenuItem<MI>> menuItems,
                                                       @Nonnull MenuHelper<M, MI> menuHelper) {
        final ListActivityMenu<M, MI> result = new ListActivityMenu<M, MI>(null, menuHelper);

        for (LabeledMenuItem<MI> menuItem : menuItems) {
            result.menuItems.add(new MenuItemWrapper<MI>(menuItem));
        }

        return result;
    }

    /**
     * Creates simple menu from list of menu items which can be filtered before displaying on screen.
     *
     * Note: you need to add method calls of {@link ActivityMenu} in your Activity or Fragment
     * Note 2: filter is applied only in {@link ListActivityMenu#onPrepareOptionsMenu(android.app.Activity, M)} method.
     * {@link ListActivityMenu#onCreateOptionsMenu(android.app.Activity, M)} does nothing in that case.
     * Note 3: filter should be persistent, i.e. should always return same values for same objects (as filtering might be called at any point in time)
     *
     * @param menuItems menu items
     * @param menuHelper menu helper. See {@link AndroidMenuHelper}
     * @param <M> menu type
     * @param <MI> menu item type
     *
     * @return constructed menu
     */
    @Nonnull
    public static <M, MI> ActivityMenu<M, MI> fromList(@Nonnull List<? extends LabeledMenuItem<MI>> menuItems,
                                                       @Nonnull MenuHelper<M, MI> menuHelper,
                                                       @Nonnull JPredicate<AMenuItem<MI>> filter) {
        final ListActivityMenu<M, MI> result = new ListActivityMenu<M, MI>(filter, menuHelper);

        for (LabeledMenuItem<MI> menuItem : menuItems) {
            result.menuItems.add(new MenuItemWrapper<MI>(menuItem));
        }

        return result;
    }

    /**
     * Creates simple menu from enum class which can be filtered before displaying on screen.
     *
     * Note: you need to add method calls of {@link ActivityMenu} in your Activity or Fragment
     * Note 2: filter is applied only in {@link ListActivityMenu#onPrepareOptionsMenu(android.app.Activity, M)} method.
     * {@link ListActivityMenu#onCreateOptionsMenu(android.app.Activity, M)} does nothing in that case.
     * Note 3: filter should be persistent, i.e. should always return same values for same objects (as filtering might be called at any point in time)
     *
     * @param enumMenuClass enum class representing menu. Should implement {@link LabeledMenuItem} interface
     * @param menuHelper menu helper. See {@link AndroidMenuHelper}
     * @param <M> menu type
     * @param <MI> menu item type
     *
     * @return constructed menu
     */
    @Nonnull
    public static <M, MI, E extends Enum & LabeledMenuItem<MI>> ActivityMenu<M, MI> fromEnum(
            @Nonnull Class<E> enumMenuClass,
            @Nonnull MenuHelper<M, MI> menuHelper,
            @Nonnull JPredicate<AMenuItem<MI>> filter) {
        return fromList(toList(enumMenuClass), menuHelper, filter);
    }

    /**
     * Creates simple menu from enum class.
     * Note: you need to add method calls of {@link ActivityMenu} in your Activity or Fragment
     *
     * @param enumMenuClass enum class representing menu. Should implement {@link LabeledMenuItem} interface
     * @param menuHelper menu helper. See {@link AndroidMenuHelper}
     * @param <M> menu type
     * @param <MI> menu item type
     *
     * @return constructed menu
     */
    @Nonnull
    public static <M, MI, E extends Enum & LabeledMenuItem<MI>> ActivityMenu<M, MI> fromEnum(
            @Nonnull Class<E> enumMenuClass,
            @Nonnull MenuHelper<M, MI> menuHelper) {
        return fromList(toList(enumMenuClass), menuHelper);
    }

    /**
     * Creates simple menu from Android resource which can be filtered before displaying on screen.
     *
     * Note: you need to add method calls of {@link ActivityMenu} in your Activity or Fragment
     * Note 2: filter is applied only in {@link ListActivityMenu#onPrepareOptionsMenu(android.app.Activity, M)} method.
     * {@link ListActivityMenu#onCreateOptionsMenu(android.app.Activity, M)} does nothing in that case.
     * Note 3: filter should be persistent, i.e. should always return same values for same objects (as filtering might be called at any point in time)
     *
     * @param menuResId id of android resource menu
     * @param menuItems menu items which are bound by identifiers to menu items from resource and handle clicks on them
     * @param menuHelper menu helper. See {@link AndroidMenuHelper}
     * @param filter applied filter
     * @param <M> menu type
     * @param <MI> menu item type
     *
     * @return constructed menu
     */
    @Nonnull
    public static <M, MI> ActivityMenu<M, MI> fromResource(int menuResId,
                                                           @Nonnull List<? extends IdentifiableMenuItem<MI>> menuItems,
                                                           @Nonnull MenuHelper<M, MI> menuHelper,
                                                           @Nonnull JPredicate<AMenuItem<MI>> filter) {
        final ListActivityMenu<M, MI> result = new ListActivityMenu<M, MI>(menuResId, filter, menuHelper);

        for (IdentifiableMenuItem<MI> menuItem : menuItems) {
            result.menuItems.add(new MenuItemWrapper<MI>(menuItem));
        }

        return result;
    }

    /**
     * Creates simple menu from Android resource.
     *
     * Note: you need to add method calls of {@link ActivityMenu} in your Activity or Fragment
     *
     * @param menuResId id of android resource menu
     * @param menuItems menu items which are bound by identifiers to menu items from resource and handle clicks on them
     * @param menuHelper menu helper. See {@link AndroidMenuHelper}
     * @param <M> menu type
     * @param <MI> menu item type
     *
     * @return constructed menu
     */
    @Nonnull
    public static <M, MI> ListActivityMenu<M, MI> fromResource(int menuResId,
                                                               @Nonnull List<? extends IdentifiableMenuItem<MI>> menuItems,
                                                               @Nonnull MenuHelper<M, MI> menuHelper) {
        final ListActivityMenu<M, MI> result = new ListActivityMenu<M, MI>(menuResId, null, menuHelper);

        for (IdentifiableMenuItem<MI> menuItem : menuItems) {
            result.menuItems.add(new MenuItemWrapper<MI>(menuItem));
        }

        return result;
    }

    /**
     * Creates simple menu from Android resource which can be filtered before displaying on screen.
     *
     * Note: you need to add method calls of {@link ActivityMenu} in your Activity or Fragment
     * Note 2: filter is applied only in {@link ListActivityMenu#onPrepareOptionsMenu(android.app.Activity, M)} method.
     * {@link ListActivityMenu#onCreateOptionsMenu(android.app.Activity, M)} does nothing in that case.
     * Note 3: filter should be persistent, i.e. should always return same values for same objects (as filtering might be called at any point in time)
     *
     * @param menuResId id of android resource menu
     * @param enumMenuClass enum class representing menu. Should implement {@link IdentifiableMenuItem} interface in order to handle clicks
     * @param menuHelper menu helper. See {@link AndroidMenuHelper}
     * @param filter applied filter
     * @param <M> menu type
     * @param <MI> menu item type
     *
     * @return constructed menu
     */
    @Nonnull
    public static <M, MI, E extends Enum & IdentifiableMenuItem<MI>> ActivityMenu<M, MI> fromResource(
            int menuResId,
            @Nonnull Class<? extends E> enumMenuClass,
            @Nonnull MenuHelper<M, MI> menuHelper,
            @Nonnull JPredicate<AMenuItem<MI>> filter) {
        return fromResource(menuResId, toList(enumMenuClass), menuHelper, filter);
    }

    /**
     * Creates simple menu from Android resource.
     *
     * Note: you need to add method calls of {@link ActivityMenu} in your Activity or Fragment
     *
     * @param menuResId id of android resource menu
     * @param enumMenuClass enum class representing menu. Should implement {@link IdentifiableMenuItem} interface in order to handle clicks
     * @param menuHelper menu helper. See {@link AndroidMenuHelper}
     * @param <M> menu type
     * @param <MI> menu item type
     *
     * @return constructed menu
     */
    @Nonnull
    public static <M, MI, E extends Enum & IdentifiableMenuItem<MI>> ActivityMenu<M, MI> fromResource(
            int menuResId,
            @Nonnull Class<? extends E> enumMenuClass,
            @Nonnull MenuHelper<M, MI> menuHelper) {
        return fromResource(menuResId, toList(enumMenuClass), menuHelper);
    }

    @Nonnull
    private static <E extends Enum> List<E> toList(@Nonnull Class<E> enumMenuClass) {
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
    public boolean onCreateOptionsMenu(@Nonnull final Activity activity, @Nonnull M menu) {
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

    private void addMenuItem(@Nonnull final Activity activity,
                             @Nonnull final M menu,
                             @Nonnull final MenuItemWrapper<MI> menuItemWrapper) {
        final int size = menuHelper.size(menu);
        final int menuItemId = size + 1;
        final MI aMenuItem = menuHelper.add(menu, 0, menuItemId, 0, menuItemWrapper.getCaption(activity));

        menuItemWrapper.setMenuItemId(menuItemId);

        menuHelper.setOnMenuItemClickListener(aMenuItem, menuItemWrapper.getMenuItem(), activity);
    }

    @Override
    public boolean onPrepareOptionsMenu(@Nonnull Activity activity, @Nonnull M menu) {
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
    public boolean onOptionsItemSelected(@Nonnull Activity activity, @Nonnull MI item) {
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

    @Nullable
    public AMenuItem<MI> findMenuItemById(int menuItemId) {
        for (MenuItemWrapper<MI> menuItem : menuItems) {
            if (  Integer.valueOf(menuItemId).equals(menuItem.getMenuItemId()) ) {
                return menuItem.getMenuItem();
            }
        }

        return null;
    }

}
