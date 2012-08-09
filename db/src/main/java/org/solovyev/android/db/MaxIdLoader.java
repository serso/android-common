package org.solovyev.android.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import org.jetbrains.annotations.NotNull;

/**
 * User: serso
 * Date: 6/3/12
 * Time: 4:43 PM
 */
public class MaxIdLoader implements DbQuery<Integer> {

    @NotNull
    private final String tableName;

    @NotNull
    private final String columnName;

    public MaxIdLoader(@NotNull String tableName, @NotNull String columnName) {
        this.tableName = tableName;
        this.columnName = columnName;
    }

    @NotNull
    @Override
    public Cursor createCursor(@NotNull SQLiteDatabase db) {
        final StringBuilder query = new StringBuilder();
        query.append("select max(").append(columnName).append(") from ").append(tableName);
        return db.rawQuery(query.toString(), null);
    }

    @NotNull
    @Override
    public Integer retrieveData(@NotNull Cursor cursor) {
        cursor.moveToNext();
        return cursor.getInt(0);
    }
}

