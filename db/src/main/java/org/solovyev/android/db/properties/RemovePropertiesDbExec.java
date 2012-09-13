package org.solovyev.android.db.properties;

import android.database.sqlite.SQLiteDatabase;
import org.jetbrains.annotations.NotNull;
import org.solovyev.android.db.DbExec;

/**
 * User: serso
 * Date: 9/2/12
 * Time: 8:17 PM
 */
public class RemovePropertiesDbExec implements DbExec {

    @NotNull
    private final Object id;

    @NotNull
    private final String tableName;

    @NotNull
    private final String idColumnName;

    public RemovePropertiesDbExec(@NotNull Object id,
                                  @NotNull String tableName,
                                  @NotNull String idColumnName) {
        this.id = id;
        this.tableName = tableName;
        this.idColumnName = idColumnName;
    }

    @Override
    public void exec(@NotNull SQLiteDatabase db) {
        db.delete(tableName, idColumnName + " = ? ", new String[]{String.valueOf(id)});
    }
}
