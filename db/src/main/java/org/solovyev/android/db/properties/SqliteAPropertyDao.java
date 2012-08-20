package org.solovyev.android.db.properties;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import org.jetbrains.annotations.NotNull;
import org.solovyev.android.AProperty;
import org.solovyev.android.db.AbstractSQLiteHelper;
import org.solovyev.android.db.AndroidDbUtils;

import java.util.List;

/**
 * User: serso
 * Date: 8/20/12
 * Time: 8:40 PM
 */
public class SqliteAPropertyDao extends AbstractSQLiteHelper implements APropertyDao {

    @NotNull
    private final String tableName;

    @NotNull
    private final String idColumnName;

    public SqliteAPropertyDao(@NotNull Context context,
                              @NotNull SQLiteOpenHelper sqliteOpenHelper,
                              @NotNull String tableName,
                              @NotNull String idColumnName) {
        super(context, sqliteOpenHelper);
        this.tableName = tableName;
        this.idColumnName = idColumnName;
    }

    @NotNull
    @Override
    public List<AProperty> loadPropertiesById(@NotNull Object id) {
        return AndroidDbUtils.doDbQuery(getSqliteOpenHelper(), new PropertyByIdDbQuery(getContext(), getSqliteOpenHelper(), tableName, idColumnName, id));
    }
}
