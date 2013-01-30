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

package org.solovyev.android.db;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import org.jetbrains.annotations.NotNull;

/**
 * User: serso
 * Date: 6/3/12
 * Time: 4:34 PM
 */
public abstract class AbstractSQLiteHelper {

    protected static final int MAX_IN_COUNT = 999;

    @NotNull
    private final Context context;

    @NotNull
    private final SQLiteOpenHelper sqliteOpenHelper;

    protected AbstractSQLiteHelper(@NotNull Context context, @NotNull SQLiteOpenHelper sqliteOpenHelper) {
        this.context = context;
        this.sqliteOpenHelper = sqliteOpenHelper;
    }

    @NotNull
    protected Context getContext() {
        return context;
    }

    @NotNull
    protected SQLiteOpenHelper getSqliteOpenHelper() {
        return sqliteOpenHelper;
    }
}
