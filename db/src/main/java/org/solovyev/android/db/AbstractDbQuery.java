package org.solovyev.android.db;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
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
    private final SQLiteOpenHelper sqliteOpenHelper;

    protected AbstractDbQuery(@NotNull Context context, @NotNull SQLiteOpenHelper sqliteOpenHelper) {
        this.context = context;
        this.sqliteOpenHelper = sqliteOpenHelper;
    }

    @NotNull
    protected Context getContext() {
        return context;
    }

    @NotNull
    protected SQLiteOpenHelper getSqliteOpenHelper() {
        return sqliteOpenHelper;
    }
}
