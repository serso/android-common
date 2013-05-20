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

package net.robotmedia.billing.helper;

import android.app.PendingIntent;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import net.robotmedia.billing.BillingController;
import net.robotmedia.billing.IBillingObserver;
import net.robotmedia.billing.ResponseCode;

import javax.annotation.Nonnull;

/**
 * Abstract subclass of IBillingObserver that provides default implementations
 * for {@link IBillingObserver#onPurchaseIntentOK(String, android.app.PendingIntent)} and
 * {@link IBillingObserver#onTransactionsRestored()}.
 */
public abstract class AbstractBillingObserver implements IBillingObserver {

	public static final String KEY_TRANSACTIONS_RESTORED = "net.robotmedia.billing.transactionsRestored";

	protected Context context;

	public AbstractBillingObserver(Context context) {
		this.context = context;
	}

	public boolean isTransactionsRestored() {
		return isTransactionsRestored(context);
	}

	public static boolean isTransactionsRestored(@Nonnull Context context) {
		final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		return preferences.getBoolean(KEY_TRANSACTIONS_RESTORED, false);
	}

	/**
	 * Called after requesting the purchase of the specified item. The default
	 * implementation simply starts the pending intent.
	 *
	 * @param productId      id of the item whose purchase was requested.
	 * @param purchaseIntent a purchase pending intent for the specified item.
	 */
	@Override
	public void onPurchaseIntentOK(@Nonnull String productId, @Nonnull PendingIntent purchaseIntent) {
		BillingController.startPurchaseIntent(context, purchaseIntent, null);
	}

	@Override
	public void onTransactionsRestored() {
		final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		final Editor editor = preferences.edit();
		editor.putBoolean(KEY_TRANSACTIONS_RESTORED, true);
		editor.commit();
	}

	@Override
	public void onErrorRestoreTransactions(@Nonnull ResponseCode responseCode) {
		// ignore errors when restoring transactions
	}
}
