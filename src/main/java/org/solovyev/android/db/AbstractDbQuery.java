package org.solovyev.android.db;

import android.content.Context;
import org.jetbrains.annotations.NotNull;

/**
 * User: serso
 * Date: 6/3/12
 * Time: 4:32 PM
 */
public abstract class AbstractDbQuery<R> implements DbQuery<R> {

    @NotNull
    private final Context context;

    @NotNull
    private final SQLiteOpenHelperConfiguration configuration;

    protected AbstractDbQuery(@NotNull Context context, @NotNull SQLiteOpenHelperConfiguration configuration) {
        this.context = context;
        this.configuration = configuration;
    }

    @NotNull
    protected Context getContext() {
        return context;
    }

    @NotNull
    protected SQLiteOpenHelperConfiguration getConfiguration() {
        return configuration;
    }
}
