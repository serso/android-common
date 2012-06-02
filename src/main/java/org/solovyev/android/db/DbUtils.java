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

    // NOTE: currently DbUtils can handle only ONE database!!!

    @NotNull
    private static final ThreadLocal<SQLiteDatabase> threadLocalWriteDb = new ThreadLocal<SQLiteDatabase>();

    @NotNull
    private static final ThreadLocal<SQLiteDatabase> threadLocalReadDb = new ThreadLocal<SQLiteDatabase>();

    @NotNull
    private static final Object DB_LOCK = new Object();

    @NotNull
    public static <R> R doDbQuery(@NotNull SQLiteOpenHelper dbHelper, @NotNull DbQuery<R> query) {
        final R result;

        synchronized (DB_LOCK) {
            SQLiteDatabase db = null;
            boolean wasOpened = false;
            try {
                db = threadLocalReadDb.get();
                if ( db == null || !db.isOpen() ) {
                    // open database
                    wasOpened = true;
                    db = dbHelper.getReadableDatabase();
                    threadLocalReadDb.set(db);
                }

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
                if (wasOpened) {
                    threadLocalReadDb.set(null);

                    // if database was opened - close it
                    if (db != null) {
                        db.close();
                    }
                }
            }
        }

        return result;
    }

    public static void doDbExec(@NotNull SQLiteOpenHelper dbHelper, @NotNull DbExec exec) {
        doDbExecs(dbHelper, Arrays.asList(exec));
    }

    public static void doDbExecs(@NotNull SQLiteOpenHelper dbHelper, @NotNull List<DbExec> execs) {

        synchronized (DB_LOCK) {
            SQLiteDatabase db = null;
            boolean wasOpened = false;
            try {
                db = threadLocalWriteDb.get();
                if ( db == null || !db.isOpen() ) {
                    // open database
                    wasOpened = true;
                    db = dbHelper.getWritableDatabase();
                    threadLocalWriteDb.set(db);
                }

                doDbTransaction(db, execs);
            } finally {
                if (wasOpened) {
                    threadLocalWriteDb.set(null);

                    // if database was opened - close it
                    if (db != null) {
                        db.close();
                    }
                }
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
