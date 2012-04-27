package org.solovyev.android.db;

import android.database.sqlite.SQLiteDatabase;
import org.jetbrains.annotations.NotNull;

/**
 * User: serso
 * Date: 4/28/12
 * Time: 2:20 AM
 */
public interface DbExec {

    void exec(@NotNull SQLiteDatabase db);
}
