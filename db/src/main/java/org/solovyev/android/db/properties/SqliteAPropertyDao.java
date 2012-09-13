package org.solovyev.android.db.properties;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import org.jetbrains.annotations.NotNull;
import org.solovyev.android.AProperty;
import org.solovyev.android.db.AbstractSQLiteHelper;
import org.solovyev.android.db.AndroidDbUtils;
import org.solovyev.android.db.DbExec;

import java.util.ArrayList;
import java.util.Collection;
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

    @NotNull
    private final String propertyNameColumnName;

    @NotNull
    private final String propertyValueColumnName;

    public SqliteAPropertyDao(@NotNull Context context,
                              @NotNull SQLiteOpenHelper sqliteOpenHelper,
                              @NotNull String tableName,
                              @NotNull String idColumnName,
                              @NotNull String propertyNameColumnName,
                              @NotNull String propertyValueColumnName) {
        super(context, sqliteOpenHelper);
        this.tableName = tableName;
        this.idColumnName = idColumnName;
        this.propertyNameColumnName = propertyNameColumnName;
        this.propertyValueColumnName = propertyValueColumnName;
    }

    @NotNull
    @Override
    public List<AProperty> loadPropertiesById(@NotNull Object id) {
        return AndroidDbUtils.doDbQuery(getSqliteOpenHelper(), new PropertyByIdDbQuery(getContext(), getSqliteOpenHelper(), tableName, idColumnName, id));
    }

    @Override
    public void removePropertiesById(@NotNull Object id) {
        AndroidDbUtils.doDbExec(getSqliteOpenHelper(), new RemovePropertiesDbExec(id, tableName, idColumnName));
    }

    @Override
    public void insertProperty(@NotNull Object id, @NotNull AProperty property) {
        AndroidDbUtils.doDbExec(getSqliteOpenHelper(), new InsertPropertyDbExec(id, property, tableName, idColumnName, propertyNameColumnName, propertyValueColumnName));
    }

    @Override
    public void insertProperties(@NotNull Object id, @NotNull Collection<AProperty> properties) {
        final List<DbExec> execs = new ArrayList<DbExec>(properties.size());

        for (AProperty property : properties) {
            execs.add(new InsertPropertyDbExec(id, property, tableName, idColumnName, propertyNameColumnName, propertyValueColumnName));
        }

        AndroidDbUtils.doDbExecs(getSqliteOpenHelper(), execs);
    }
}
