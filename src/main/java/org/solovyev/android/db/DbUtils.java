package org.solovyev.android.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

/**
 * User: serso
 * Date: 4/15/12
 * Time: 6:01 PM
 */
public final class DbUtils {

    private DbUtils() {
        throw new AssertionError();
    }

    @NotNull
    public static <R> R doDbQuery(@NotNull SQLiteOpenHelper dbHelper, @NotNull DbQuery<R> query) {
        final R result;

        SQLiteDatabase db = null;
        try {
            // open database
            db = dbHelper.getWritableDatabase();

            Cursor cursor = null;
            try {
                // open cursor
                cursor = query.createCursor(db);
                // do operation
                result = query.retrieveData(cursor);
            } finally {
                // anyway if cursor was opened - close it
                if (cursor != null) {
                    cursor.close();
                }
            }
        } finally {
            // anyway if database was opened - close it
            if (db != null) {
                db.close();
            }
        }

        return result;
    }

    public static void doDbExec(@NotNull SQLiteOpenHelper dbHelper, @NotNull DbExec exec) {
        doDbExecs(dbHelper, Arrays.asList(exec));
    }

    public static void doDbExecs(@NotNull SQLiteOpenHelper dbHelper, @NotNull List<DbExec> execs) {

        SQLiteDatabase db = null;
        try {
            // open database
            db = dbHelper.getWritableDatabase();

            doDbTransaction(db, execs);
        } finally {

            // anyway if database was opened - close it
            if (db != null) {
                db.close();
            }
        }
    }

    private static void doDbTransaction(@NotNull SQLiteDatabase db, @NotNull List<DbExec> execs) {

        try {
            // start transaction
            db.beginTransaction();

            // do action
            for (DbExec exec : execs) {
                exec.exec(db);
            }

            // mark transaction successful
            db.setTransactionSuccessful();
        } finally {
            // end transaction
            db.endTransaction();
        }
    }
}
