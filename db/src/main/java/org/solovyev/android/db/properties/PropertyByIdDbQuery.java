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

package org.solovyev.android.db.properties;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import javax.annotation.Nonnull;
import org.solovyev.android.properties.AProperty;
import org.solovyev.android.db.AbstractDbQuery;
import org.solovyev.android.db.ListMapper;

import java.util.List;

/**
 * User: serso
 * Date: 5/30/12
 * Time: 7:24 PM
 */
public class PropertyByIdDbQuery extends AbstractDbQuery<List<AProperty>> {

    @Nonnull
    private String tableName;

    @Nonnull
    private String idColumnName;

    @Nonnull
    private Object id;

    public PropertyByIdDbQuery(@Nonnull Context context,
                               @Nonnull SQLiteOpenHelper sqliteOpenHelper,
                               @Nonnull String tableName,
                               @Nonnull String idColumnName,
                               @Nonnull Object id) {
        super(context, sqliteOpenHelper);
        this.tableName = tableName;
        this.idColumnName = idColumnName;
        this.id = id;
    }

    @Nonnull
    @Override
    public Cursor createCursor(@Nonnull SQLiteDatabase db) {
        return db.query(tableName, null, idColumnName + " = ? ", new String[]{String.valueOf(id)}, null, null, null);
    }

    @Nonnull
    @Override
    public List<AProperty> retrieveData(@Nonnull Cursor cursor) {
        return new ListMapper<AProperty>(APropertyMapper.getInstance()).convert(cursor);
    }
}
