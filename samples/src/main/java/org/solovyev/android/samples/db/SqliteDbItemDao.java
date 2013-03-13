/*
 * Copyright 2013 serso aka se.solovyev
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Contact details
 *
 * Email: se.solovyev@gmail.com
 * Site:  http://se.solovyev.org
 */

package org.solovyev.android.samples.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import org.solovyev.android.db.*;
import org.solovyev.common.Converter;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * User: serso
 * Date: 8/10/12
 * Time: 4:45 PM
 */
public class SqliteDbItemDao extends AbstractSQLiteHelper implements DbItemDao {

    public SqliteDbItemDao(@Nonnull Context context, @Nonnull SQLiteOpenHelper sqliteOpenHelper) {
        super(context, sqliteOpenHelper);
    }

    @Nonnull
    @Override
    public List<DbItem> loadAll() {
        return AndroidDbUtils.doDbQuery(getSqliteOpenHelper(), new LoadAll(getContext(), getSqliteOpenHelper()));
    }

    @Override
    public void insert(@Nonnull DbItem dbItem) {
        AndroidDbUtils.doDbExec(getSqliteOpenHelper(), new Insert(dbItem));
    }

    @Override
    public void removeByName(@Nonnull String name) {
        AndroidDbUtils.doDbExec(getSqliteOpenHelper(), new RemoveByName(name));
    }


    private static final class RemoveByName extends AbstractObjectDbExec<String> {

        protected RemoveByName(@Nonnull String name) {
            super(name);
        }

        @Override
        public long exec(@Nonnull SQLiteDatabase db) {
            final String name = getNotNullObject();

            return db.delete("items", "name = ?", new String[]{name});
        }
    }
    private static final class Insert extends AbstractObjectDbExec<DbItem> {

        protected Insert(@Nonnull DbItem object) {
            super(object);
        }

        @Override
        public long exec(@Nonnull SQLiteDatabase db) {
            final DbItem dbItem = getNotNullObject();

            final ContentValues values = new ContentValues();
            values.put("name", dbItem.getName());
            return db.insert("items", null, values);
        }
    }

    private static final class LoadAll extends AbstractDbQuery<List<DbItem>> {

        protected LoadAll(@Nonnull Context context, @Nonnull SQLiteOpenHelper sqliteOpenHelper) {
            super(context, sqliteOpenHelper);
        }

        @Nonnull
        @Override
        public Cursor createCursor(@Nonnull SQLiteDatabase db) {
            return db.query("items", null, null, null, null, null, null);
        }

        @Nonnull
        @Override
        public List<DbItem> retrieveData(@Nonnull Cursor cursor) {
            return new ListMapper<DbItem>(DbItemMapper.getInstance()).convert(cursor);
        }
    }

    private static final class DbItemMapper implements Converter<Cursor, DbItem> {

        @Nonnull
        private static final DbItemMapper instance = new DbItemMapper();

        private DbItemMapper() {
        }

        @Nonnull
        public static DbItemMapper getInstance() {
            return instance;
        }

        @Nonnull
        @Override
        public DbItem convert(@Nonnull Cursor cursor) {
            final String name = cursor.getString(0);
            return new DbItemImpl(name);
        }
    }
}
