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
