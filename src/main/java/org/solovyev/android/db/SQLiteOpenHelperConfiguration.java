package org.solovyev.android.db;

import android.database.sqlite.SQLiteDatabase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * User: serso
 * Date: 6/3/12
 * Time: 4:33 PM
 */
public interface SQLiteOpenHelperConfiguration {

    @NotNull
    String getName();

    @Nullable
    SQLiteDatabase.CursorFactory getCursorFactory();

    int getVersion();
}
