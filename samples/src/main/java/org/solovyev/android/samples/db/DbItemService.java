package org.solovyev.android.samples.db;

import android.content.Context;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * User: serso
 * Date: 8/10/12
 * Time: 4:40 PM
 */
public interface DbItemService {

    @NotNull
    List<DbItem> getAllDbItems(@NotNull Context context);

    @NotNull
    List<DbItem> getAllStartsWith(@NotNull String prefix, @NotNull Context context);

    void addItem(@NotNull DbItem dbItem, @NotNull Context context);

    @NotNull
    List<DbItem> removeItemByName(@NotNull String name, @NotNull Context context);
}
