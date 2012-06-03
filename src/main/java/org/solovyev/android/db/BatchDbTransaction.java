package org.solovyev.android.db;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import org.jetbrains.annotations.NotNull;

import java.util.StringTokenizer;

/**
 * User: serso
 * Date: 6/3/12
 * Time: 4:36 PM
 */
public class BatchDbTransaction {

    private static final String TAG = "BatchDbOperation";

    @NotNull
    private final String sqls;

    @NotNull
    private final String delimiters;

    public BatchDbTransaction(@NotNull String sqls, @NotNull String delimiters) {
        this.sqls = sqls;
        this.delimiters = delimiters;
    }

    public void batchQuery(@NotNull SQLiteDatabase db) {
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
