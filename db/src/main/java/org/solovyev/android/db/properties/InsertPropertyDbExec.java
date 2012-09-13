package org.solovyev.android.db.properties;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import org.jetbrains.annotations.NotNull;
import org.solovyev.android.AProperty;
import org.solovyev.android.db.DbExec;

/**
 * User: serso
 * Date: 9/2/12
 * Time: 8:06 PM
 */
public class InsertPropertyDbExec implements DbExec {

    @NotNull
    private final Object id;

    @NotNull
    private final AProperty property;

    @NotNull
    private final String tableName;

    @NotNull
    private final String idColumnName;

    @NotNull
    private final String propertyNameColumnName;

    @NotNull
    private final String propertyValueColumnName;


    public InsertPropertyDbExec(@NotNull Object id,
                                @NotNull AProperty property,
                                @NotNull String tableName,
                                @NotNull String idColumnName,
                                @NotNull String propertyNameColumnName,
                                @NotNull String propertyValueColumnName) {
        this.id = id;
        this.property = property;
        this.tableName = tableName;
        this.idColumnName = idColumnName;
        this.propertyNameColumnName = propertyNameColumnName;
        this.propertyValueColumnName = propertyValueColumnName;
    }

    @Override
    public void exec(@NotNull SQLiteDatabase db) {
        final ContentValues values = new ContentValues();

        values.put(idColumnName, String.valueOf(id));
        values.put(propertyNameColumnName, property.getName());
        values.put(propertyValueColumnName, property.getValue());

        db.insert(tableName, null, values);
    }
}
