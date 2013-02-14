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

package org.solovyev.android.samples.db;

import android.content.Context;
import org.jetbrains.annotations.NotNull;
import org.solovyev.android.App;
import org.solovyev.android.samples.Locator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * User: serso
 * Date: 8/10/12
 * Time: 4:41 PM
 */
public class DbItemServiceImpl implements DbItemService {

    // cached items
    @NotNull
    private final List<DbItem> items = new ArrayList<DbItem>();

    @NotNull
    @Override
    public List<DbItem> getAllDbItems(@NotNull Context context) {
        synchronized (items) {
            if (items.isEmpty()) {
                // assuming: empty => not loaded
                items.addAll(getDbItemDao().loadAll());
            }

            return Collections.unmodifiableList(items);
        }
    }

    @NotNull
    @Override
    public List<DbItem> getAllStartsWith(@NotNull String prefix, @NotNull Context context) {
        final List<DbItem> result;
        synchronized (items) {
            result = new ArrayList<DbItem>(getAllDbItems(context));
        }

        prefix = prefix.toLowerCase();

        // filter by prefix
        for (Iterator<DbItem> it = result.iterator(); it.hasNext(); ) {
            final DbItem notFilteredItem = it.next();
            if (!notFilteredItem.getName().toLowerCase().startsWith(prefix)) {
                it.remove();
            }
        }

        return result;
    }

    @Override
    public void addItem(@NotNull DbItem dbItem, @NotNull Context context) {
        synchronized (items) {
            getDbItemDao().insert(dbItem);
            // if successfully insert => add to the cache
            items.add(dbItem);
        }
    }

    @NotNull
    @Override
    public List<DbItem> removeItemByName(@NotNull String name, @NotNull Context context) {
        synchronized (items) {
            final List<DbItem> removedItems = new ArrayList<DbItem>();

            // remove from db
            getDbItemDao().removeByName(name);

            // remove from cache
            for (Iterator<DbItem> it = items.iterator(); it.hasNext(); ) {
                final DbItem dbItem = it.next();
                if (name.equals(dbItem.getName())) {
                    it.remove();
                    removedItems.add(dbItem);
                }
            }

            return removedItems;
        }
    }

    @NotNull
    private DbItemDao getDbItemDao() {
        return ((Locator) App.getLocator()).getDbItemDao();
    }
}
