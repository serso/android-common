package org.solovyev.android.samples.db;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * User: serso
 * Date: 8/10/12
 * Time: 4:40 PM
 */
public interface DbItemDao {

    @NotNull
    List<DbItem> loadAll();

    void insert(@NotNull DbItem dbItem);

    void removeByName(@NotNull String name);
}
