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

import android.content.Context;
import org.solovyev.android.db.AndroidDbUtils;

import javax.annotation.Nonnull;
import java.util.List;

public class TransactionManager {

	public synchronized static void dropDatabase(@Nonnull Context context) {
		context.deleteDatabase(BillingDB.DATABASE_NAME);
	}

	public synchronized static void addTransaction(@Nonnull Transaction transaction) {
		BillingDB.getInstance().insert(transaction);
	}

	public synchronized static boolean isPurchased(@Nonnull String productId) {
		return countPurchases(productId) > 0;
	}

	public synchronized static int countPurchases(@Nonnull String productId) {
		return AndroidDbUtils.doDbQuery(BillingDB.getInstance().getDatabaseHelper(), new BillingDB.CountPurchases(productId));
	}

	@Nonnull
	public synchronized static List<Transaction> getTransactions() {
		return AndroidDbUtils.doDbQuery(BillingDB.getInstance().getDatabaseHelper(), new BillingDB.TransactionsByProductId(null));
	}

	@Nonnull
	public synchronized static List<Transaction> getTransactions(@Nonnull String productId) {
		return AndroidDbUtils.doDbQuery(BillingDB.getInstance().getDatabaseHelper(), new BillingDB.TransactionsByProductId(productId));
	}


}
