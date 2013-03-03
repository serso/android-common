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

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import javax.annotation.Nonnull;
import org.solovyev.android.properties.AProperty;
import org.solovyev.android.db.DbExec;

/**
 * User: serso
 * Date: 9/2/12
 * Time: 8:06 PM
 */
public class InsertPropertyDbExec implements DbExec {

    @Nonnull
    private final Object id;

    @Nonnull
    private final AProperty property;

    @Nonnull
    private final String tableName;

    @Nonnull
    private final String idColumnName;

    @Nonnull
    private final String propertyNameColumnName;

    @Nonnull
    private final String propertyValueColumnName;


    public InsertPropertyDbExec(@Nonnull Object id,
                                @Nonnull AProperty property,
                                @Nonnull String tableName,
                                @Nonnull String idColumnName,
                                @Nonnull String propertyNameColumnName,
                                @Nonnull String propertyValueColumnName) {
        this.id = id;
        this.property = property;
        this.tableName = tableName;
        this.idColumnName = idColumnName;
        this.propertyNameColumnName = propertyNameColumnName;
        this.propertyValueColumnName = propertyValueColumnName;
    }

    @Override
    public void exec(@Nonnull SQLiteDatabase db) {
        final ContentValues values = new ContentValues();

        values.put(idColumnName, String.valueOf(id));
        values.put(propertyNameColumnName, property.getName());
        values.put(propertyValueColumnName, property.getValue());

        db.insert(tableName, null, values);
    }
}
