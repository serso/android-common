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

package org.solovyev.android.db;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import javax.annotation.Nonnull;

import java.util.StringTokenizer;

/**
 * User: serso
 * Date: 6/3/12
 * Time: 4:36 PM
 */
public class BatchDbTransaction {

    private static final String TAG = "BatchDbOperation";

    @Nonnull
    private final String sqls;

    @Nonnull
    private final String delimiters;

    public BatchDbTransaction(@Nonnull String sqls, @Nonnull String delimiters) {
        this.sqls = sqls;
        this.delimiters = delimiters;
    }

    public void batchQuery(@Nonnull SQLiteDatabase db) {
        try {
            db.beginTransaction();

            final StringTokenizer st = new StringTokenizer(sqls, delimiters, false);
            while (st.hasMoreTokens()) {
                final String sql = st.nextToken();
                if ( sql.startsWith("--") ) {
                    Log.d(TAG, "Comments: " + sql);
                    continue;
                }
                Log.d(TAG, "Executing sql: " + sql);
                db.execSQL(sql);
            }

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }
}
