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

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import org.solovyev.common.text.Strings;

import javax.annotation.Nonnull;
import java.util.*;

/**
 * User: serso
 * Date: 4/15/12
 * Time: 6:01 PM
 */
public final class AndroidDbUtils {

	// cache for databases. Used in order not to close database in nested transactions
	@Nonnull
	private static final Map<SQLiteOpenHelper, SQLiteDatabase> dbCache = new HashMap<SQLiteOpenHelper, SQLiteDatabase>(3);

	private AndroidDbUtils() {
		throw new AssertionError();
	}

	@Nonnull
	public static <R> R doDbQuery(@Nonnull SQLiteOpenHelper dbHelper, @Nonnull DbQuery<R> query) {
		final R result;

		// assuming there is only one dbHelper per database in application
		synchronized (dbHelper) {
			SQLiteDatabase db = null;
			boolean wasOpened = false;
			try {
				db = dbCache.get(dbHelper);
				if (db == null) {
					db = dbHelper.getWritableDatabase();
					dbCache.put(dbHelper, db);
					wasOpened = true;
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
				// if database was opened - close it
				if (db != null && wasOpened) {
					//db.close();
					dbCache.remove(dbHelper);
				}
			}
		}

		return result;
	}

	@Nonnull
	public static Long doDbExec(@Nonnull SQLiteOpenHelper dbHelper, @Nonnull DbExec exec) {
		final List<Long> result = doDbExecs(dbHelper, Arrays.asList(exec));

		if (!result.isEmpty()) {
			// must contain one value
			return result.get(0);
		} else {
			return DbExec.SQL_ERROR;
		}
	}

	public static List<Long> doDbExecs(@Nonnull SQLiteOpenHelper dbHelper, @Nonnull List<DbExec> execs) {
		final List<Long> result;

		// assuming there is only one dbHelper per database in application
		synchronized (dbHelper) {
			SQLiteDatabase db = null;
			boolean wasOpened = false;
			try {
				db = dbCache.get(dbHelper);
				if (db == null) {
					db = dbHelper.getWritableDatabase();
					dbCache.put(dbHelper, db);
					wasOpened = true;
				}

				result = doDbTransactions(db, execs);

			} finally {
				// if database was opened - close it
				if (db != null && wasOpened) {
					//db.close();
					dbCache.remove(dbHelper);
				}
			}
		}

		return result;
	}

	@Nonnull
	private static List<Long> doDbTransactions(@Nonnull SQLiteDatabase db, @Nonnull List<DbExec> execs) {
		final List<Long> result = new ArrayList<Long>(execs.size());
		try {
			// start transaction
			db.beginTransaction();

			// do action
			for (DbExec exec : execs) {
				result.add(exec.exec(db));
			}

			// mark transaction successful
			db.setTransactionSuccessful();
		} finally {
			// end transaction
			db.endTransaction();
		}

		return result;
	}

	@Nonnull
	public static String[] inClauseValues(@Nonnull List<?> objects, @Nonnull String... beforeInValues) {
		final String[] result = new String[objects.size() + beforeInValues.length];

		for (int i = 0; i < result.length; i++) {
			if (i < beforeInValues.length) {
				result[i] = beforeInValues[i];
			} else {
				result[i] = objects.get(i - beforeInValues.length).toString();
			}
		}

		return result;
	}

	@Nonnull
	public static String inClause(@Nonnull List<?> objects) {
		final StringBuilder result = new StringBuilder(3 * objects.size());

		result.append("(");
		if (objects.size() == 1) {
			result.append("?");
		} else if (objects.size() > 1) {
			result.append("?");
			result.append(Strings.repeat(", ?", objects.size() - 1));
		} else {
			result.append("'foo'");
		}
		result.append(")");

		return result.toString();
	}
}
