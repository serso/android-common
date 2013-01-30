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
 * ---------------------------------------------------------------------
 * Contact details
 *
 * Email: se.solovyev@gmail.com
 * Site:  http://se.solovyev.org
 */

package org.solovyev.android.db.properties;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import org.jetbrains.annotations.NotNull;
import org.solovyev.android.AProperty;
import org.solovyev.android.db.AbstractDbQuery;
import org.solovyev.android.db.ListMapper;

import java.util.List;

/**
 * User: serso
 * Date: 5/30/12
 * Time: 7:24 PM
 */
public class PropertyByIdDbQuery extends AbstractDbQuery<List<AProperty>> {

    @NotNull
    private String tableName;

    @NotNull
    private String idColumnName;

    @NotNull
    private Object id;

    public PropertyByIdDbQuery(@NotNull Context context,
                               @NotNull SQLiteOpenHelper sqliteOpenHelper,
                               @NotNull String tableName,
                               @NotNull String idColumnName,
                               @NotNull Object id) {
        super(context, sqliteOpenHelper);
        this.tableName = tableName;
        this.idColumnName = idColumnName;
        this.id = id;
    }

    @NotNull
    @Override
    public Cursor createCursor(@NotNull SQLiteDatabase db) {
        return db.query(tableName, null, idColumnName + " = ? ", new String[]{String.valueOf(id)}, null, null, null);
    }

    @NotNull
    @Override
    public List<AProperty> retrieveData(@NotNull Cursor cursor) {
        return new ListMapper<AProperty>(APropertyMapper.getInstance()).convert(cursor);
    }
}
