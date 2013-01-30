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

package org.solovyev.android.samples;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import org.jetbrains.annotations.NotNull;
import org.solovyev.android.db.CommonSQLiteOpenHelper;
import org.solovyev.android.db.SQLiteOpenHelperConfiguration;
import org.solovyev.android.samples.db.DbItemDao;
import org.solovyev.android.samples.db.DbItemService;
import org.solovyev.android.samples.db.DbItemServiceImpl;
import org.solovyev.android.samples.db.SqliteDbItemDao;

/**
 * User: serso
 * Date: 8/10/12
 * Time: 4:29 PM
 */
public class SamplesApplication extends Application implements Locator {

    @NotNull
    private static SamplesApplication instance;

    @NotNull
    private CommonSQLiteOpenHelper sqliteOpenHelper;

    @NotNull
    private DbItemService dbItemService;

    public SamplesApplication() {
        instance = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        sqliteOpenHelper = new CommonSQLiteOpenHelper(this, getSqliteOpenHelperConfiguration());
        dbItemService = new DbItemServiceImpl();
    }

    @NotNull
    public static Locator getLocator() {
        return instance;
    }

    @NotNull
    @Override
    public SQLiteOpenHelper getSqliteOpenHelper() {
        return this.sqliteOpenHelper;
    }

    @NotNull
    @Override
    public DbItemDao getDbItemDao() {
        // even can have multiple instances as they are synchronized on ONE SQLiteOpenHelper
        // and daos have no states
        return new SqliteDbItemDao(this, getSqliteOpenHelper());
    }

    @NotNull
    @Override
    public DbItemService getDbItemService() {
        return this.dbItemService;
    }

    @NotNull
    private SQLiteOpenHelperConfiguration getSqliteOpenHelperConfiguration() {
        return new DbConfiguration();
    }

    private static final class DbConfiguration implements SQLiteOpenHelperConfiguration {

        @NotNull
        @Override
        public String getName() {
            return "samples";
        }

        @Override
        public SQLiteDatabase.CursorFactory getCursorFactory() {
            return null;
        }

        @Override
        public int getVersion() {
            return 3;
        }
    }
}
