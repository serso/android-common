package org.solovyev.android.samples.db;

import android.content.Context;
import org.jetbrains.annotations.NotNull;
import org.solovyev.android.samples.SamplesApplication;

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
        return SamplesApplication.getLocator().getDbItemDao();
    }
}
