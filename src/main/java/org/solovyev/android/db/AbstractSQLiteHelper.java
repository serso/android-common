package org.solovyev.android.db;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import org.jetbrains.annotations.NotNull;

/**
 * User: serso
 * Date: 6/3/12
 * Time: 4:34 PM
 */
public abstract class AbstractSQLiteHelper {

    @NotNull
    private final Context context;

    @NotNull
    private final SQLiteOpenHelperConfiguration configuration;

    @NotNull
    private final SQLiteOpenHelper dbHelper;

    protected AbstractSQLiteHelper(@NotNull Context context, @NotNull SQLiteOpenHelperConfiguration configuration) {
        this.context = context;
        this.configuration = configuration;
        this.dbHelper = new CommonSQLiteOpenHelper(context, configuration);
    }

    @NotNull
    protected Context getContext() {
        return context;
    }

    @NotNull
    protected SQLiteOpenHelper getDbHelper() {
        return dbHelper;
    }

    @NotNull
    protected SQLiteOpenHelperConfiguration getConfiguration() {
        return configuration;
    }
}
