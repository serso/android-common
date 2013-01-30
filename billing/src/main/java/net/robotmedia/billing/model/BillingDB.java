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

package net.robotmedia.billing.model;

import android.app.Application;
import net.robotmedia.billing.model.Transaction.PurchaseState;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.solovyev.android.db.AndroidDbUtils;
import org.solovyev.android.db.DbExec;
import org.solovyev.android.db.DbQuery;

import java.util.ArrayList;
import java.util.List;

// public only for tests
public class BillingDB {

	static final String DATABASE_NAME = "billing.db";
	static final int DATABASE_VERSION = 1;
	static final String TABLE_TRANSACTIONS = "purchases";

	static final String COLUMN_ID = "_id";
	static final String COLUMN_STATE = "state";
	static final String COLUMN_PRODUCT_ID = "productId";
	static final String COLUMN_PURCHASE_TIME = "purchaseTime";
	static final String COLUMN_DEVELOPER_PAYLOAD = "developerPayload";

	static final String[] TABLE_TRANSACTIONS_COLUMNS = {
			COLUMN_ID,
			COLUMN_PRODUCT_ID,
			COLUMN_STATE,
			COLUMN_PURCHASE_TIME,
			COLUMN_DEVELOPER_PAYLOAD
	};

	// NOTE: package protected for tests - should not be used directly
	final SQLiteDatabase db;

	// NOTE: package protected for tests - should not be used directly
	final DatabaseHelper databaseHelper;

	private static volatile BillingDB instance;

	private BillingDB(@NotNull Context context) {
		databaseHelper = new DatabaseHelper(context);
		db = databaseHelper.getWritableDatabase();
	}

	public static void init(@NotNull Application application) {
		instance = new BillingDB(application);
	}

	@NotNull
	public static BillingDB getInstance() {
		return instance;
	}

	@NotNull
	private static List<Transaction> getTransactionsFromCursor(@NotNull final Cursor cursor) {
		final List<Transaction> result = new ArrayList<Transaction>();

		while (cursor.moveToNext()) {
			result.add(createTransaction(cursor));
		}

		return result;
	}

	public void close() {
		// database should never be closed
		//db.close();
		//databaseHelper.close();
	}

	public void insert(@NotNull Transaction transaction) {
		AndroidDbUtils.doDbExec(this.getDatabaseHelper(), new InsertTransaction(transaction));
	}

	@NotNull
	protected static Transaction createTransaction( @NotNull Cursor cursor) {
		final Transaction purchase = new Transaction();

		purchase.orderId = cursor.getString(0);
		purchase.productId = cursor.getString(1);
		purchase.purchaseState = PurchaseState.valueOf(cursor.getInt(2));
		purchase.purchaseTime = cursor.getLong(3);
		purchase.developerPayload = cursor.getString(4);

		return purchase;
	}

	static class CountPurchases implements DbQuery<Integer> {

		@NotNull
		private final String productId;

		public CountPurchases(@NotNull String productId) {
			this.productId = productId;
		}

		@NotNull
		@Override
		public Cursor createCursor(@NotNull SQLiteDatabase db) {
			return db.query(TABLE_TRANSACTIONS, TABLE_TRANSACTIONS_COLUMNS, COLUMN_PRODUCT_ID + " = ? AND " + COLUMN_STATE + " = ?", new String[]{productId, String.valueOf(PurchaseState.PURCHASED.ordinal())}, null, null, null);
		}

		@NotNull
		@Override
		public Integer retrieveData(@NotNull Cursor cursor) {
			return cursor.getCount();
		}
	}

	static class TransactionsByProductId implements DbQuery<List<Transaction>> {

		@Nullable
		private final String productId;

		public TransactionsByProductId(@Nullable String productId) {
			this.productId = productId;
		}

		@NotNull
		@Override
		public Cursor createCursor(@NotNull SQLiteDatabase db) {
			if (productId != null) {
				return db.query(TABLE_TRANSACTIONS, TABLE_TRANSACTIONS_COLUMNS, COLUMN_PRODUCT_ID + " = ?", new String[]{productId}, null, null, null);
			} else {
				return db.query(TABLE_TRANSACTIONS, TABLE_TRANSACTIONS_COLUMNS, null, null, null, null, null);
			}
		}

		@NotNull
		@Override
		public List<Transaction> retrieveData(@NotNull Cursor cursor) {
			return getTransactionsFromCursor(cursor);
		}
	}

	private class DatabaseHelper extends SQLiteOpenHelper {

		public DatabaseHelper(@NotNull Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(@NotNull SQLiteDatabase db) {
			createTransactionsTable(db);
		}

		private void createTransactionsTable(@NotNull SQLiteDatabase db) {
			db.execSQL("CREATE TABLE " + TABLE_TRANSACTIONS + "(" +
					COLUMN_ID + " TEXT PRIMARY KEY, " +
					COLUMN_PRODUCT_ID + " INTEGER, " +
					COLUMN_STATE + " TEXT, " +
					COLUMN_PURCHASE_TIME + " TEXT, " +
					COLUMN_DEVELOPER_PAYLOAD + " INTEGER)");
		}

		@Override
		public void onUpgrade(@NotNull SQLiteDatabase db, int oldVersion, int newVersion) {
		}
	}

	@NotNull
	public DatabaseHelper getDatabaseHelper() {
		return databaseHelper;
	}

	static class InsertTransaction implements DbExec {

		@NotNull
		private final Transaction transaction;

		InsertTransaction(@NotNull Transaction transaction) {
			this.transaction = transaction;
		}

		@Override
		public void exec(@NotNull SQLiteDatabase db) {
			final ContentValues values = new ContentValues();

			values.put(COLUMN_ID, transaction.orderId);
			values.put(COLUMN_PRODUCT_ID, transaction.productId);
			values.put(COLUMN_STATE, transaction.purchaseState.ordinal());
			values.put(COLUMN_PURCHASE_TIME, transaction.purchaseTime);
			values.put(COLUMN_DEVELOPER_PAYLOAD, transaction.developerPayload);

			db.replace(TABLE_TRANSACTIONS, null /* nullColumnHack */, values);
		}
	}
}
