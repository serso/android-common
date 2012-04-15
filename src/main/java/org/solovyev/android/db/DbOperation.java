package org.solovyev.android.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import org.jetbrains.annotations.NotNull;

/**
 * User: serso
 * Date: 4/15/12
 * Time: 6:00 PM
 */
public interface DbOperation<R> {

    @NotNull
    Cursor createCursor(@NotNull SQLiteDatabase db);

    @NotNull
    R doOperation(@NotNull Cursor cursor);
}
