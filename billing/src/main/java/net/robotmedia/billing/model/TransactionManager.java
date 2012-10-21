/*   Copyright 2011 Robot Media SL (http://www.robotmedia.net)
*
*   Licensed under the Apache License, Version 2.0 (the "License");
*   you may not use this file except in compliance with the License.
*   You may obtain a copy of the License at
*
*       http://www.apache.org/licenses/LICENSE-2.0
*
*   Unless required by applicable law or agreed to in writing, software
*   distributed under the License is distributed on an "AS IS" BASIS,
*   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*   See the License for the specific language governing permissions and
*   limitations under the License.
*/

package net.robotmedia.billing.model;

import android.content.Context;
import org.jetbrains.annotations.NotNull;
import org.solovyev.android.db.AndroidDbUtils;

import java.util.List;

public class TransactionManager {

	public synchronized static void dropDatabase(@NotNull Context context) {
		context.deleteDatabase(BillingDB.DATABASE_NAME);
	}

	public synchronized static void addTransaction(@NotNull Transaction transaction) {
		BillingDB.getInstance().insert(transaction);
	}

	public synchronized static boolean isPurchased(@NotNull String productId) {
		return countPurchases(productId) > 0;
	}

	public synchronized static int countPurchases(@NotNull String productId) {
		return AndroidDbUtils.doDbQuery(BillingDB.getInstance().getDatabaseHelper(), new BillingDB.CountPurchases(productId));
	}

	@NotNull
	public synchronized static List<Transaction> getTransactions() {
		return AndroidDbUtils.doDbQuery(BillingDB.getInstance().getDatabaseHelper(), new BillingDB.TransactionsByProductId(null));
	}

	@NotNull
	public synchronized static List<Transaction> getTransactions(@NotNull String productId) {
		return AndroidDbUtils.doDbQuery(BillingDB.getInstance().getDatabaseHelper(), new BillingDB.TransactionsByProductId(productId));
	}


}
