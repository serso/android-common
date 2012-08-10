package org.solovyev.android.samples;

import android.database.sqlite.SQLiteOpenHelper;
import org.jetbrains.annotations.NotNull;
import org.solovyev.android.samples.db.DbItemDao;
import org.solovyev.android.samples.db.DbItemService;

/**
* User: serso
* Date: 8/10/12
* Time: 4:38 PM
*/
public interface Locator {

    @NotNull
    SQLiteOpenHelper getSqliteOpenHelper();

    @NotNull
    DbItemDao getDbItemDao();

    @NotNull
    DbItemService getDbItemService();
}
