package org.solovyev.android.samples.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import org.jetbrains.annotations.NotNull;
import org.solovyev.android.db.*;
import org.solovyev.common.Converter;

import java.util.List;

/**
 * User: serso
 * Date: 8/10/12
 * Time: 4:45 PM
 */
public class SqliteDbItemDao extends AbstractSQLiteHelper implements DbItemDao {

    public SqliteDbItemDao(@NotNull Context context, @NotNull SQLiteOpenHelper sqliteOpenHelper) {
        super(context, sqliteOpenHelper);
    }

    @NotNull
    @Override
    public List<DbItem> loadAll() {
        return AndroidDbUtils.doDbQuery(getSqliteOpenHelper(), new LoadAll(getContext(), getSqliteOpenHelper()));
    }

    @Override
    public void insert(@NotNull DbItem dbItem) {
        AndroidDbUtils.doDbExec(getSqliteOpenHelper(), new Insert(dbItem));
    }

    @Override
    public void removeByName(@NotNull String name) {
        AndroidDbUtils.doDbExec(getSqliteOpenHelper(), new RemoveByName(name));
    }


    private static final class RemoveByName extends AbstractObjectDbExec<String> {

        protected RemoveByName(@NotNull String name) {
            super(name);
        }

        @Override
        public void exec(@NotNull SQLiteDatabase db) {
            final String name = getNotNullObject();

            db.delete("items", "name = ?", new String[]{name});
        }
    }
    private static final class Insert extends AbstractObjectDbExec<DbItem> {

        protected Insert(@NotNull DbItem object) {
            super(object);
        }

        @Override
        public void exec(@NotNull SQLiteDatabase db) {
            final DbItem dbItem = getNotNullObject();

            final ContentValues values = new ContentValues();
            values.put("name", dbItem.getName());
            db.insert("items", null, values);
        }
    }

    private static final class LoadAll extends AbstractDbQuery<List<DbItem>> {

        protected LoadAll(@NotNull Context context, @NotNull SQLiteOpenHelper sqliteOpenHelper) {
            super(context, sqliteOpenHelper);
        }

        @NotNull
        @Override
        public Cursor createCursor(@NotNull SQLiteDatabase db) {
            return db.query("items", null, null, null, null, null, null);
        }

        @NotNull
        @Override
        public List<DbItem> retrieveData(@NotNull Cursor cursor) {
            return new ListMapper<DbItem>(DbItemMapper.getInstance()).convert(cursor);
        }
    }

    private static final class DbItemMapper implements Converter<Cursor, DbItem> {

        @NotNull
        private static final DbItemMapper instance = new DbItemMapper();

        private DbItemMapper() {
        }

        @NotNull
        public static DbItemMapper getInstance() {
            return instance;
        }

        @NotNull
        @Override
        public DbItem convert(@NotNull Cursor cursor) {
            final String name = cursor.getString(0);
            return new DbItemImpl(name);
        }
    }
}
