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

package net.robotmedia.billing;

import android.app.PendingIntent;
import net.robotmedia.billing.model.Transaction;
import javax.annotation.Nonnull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * User: serso
 * Date: 1/18/12
 * Time: 11:49 AM
 */

/**
 * This class is used for storing all the registered observers of billing service.
 * Use this class to notify all the net.robotmedia.billing.IBillingObserver observers
 */
class BillingObserverRegistry {

	// synchronized field
	@Nonnull
	private static final Set<IBillingObserver> observers = new HashSet<IBillingObserver>();

	static void onCheckBillingSupportedResponse(boolean supported) {
		for (IBillingObserver o : getSynchronizedObservers()) {
			o.onCheckBillingSupportedResponse(supported);
		}
	}

	/**
	 * Called after the response to a
	 * {@link net.robotmedia.billing.BillingRequest.Purchase} request is
	 * received.
	 *
	 * @param productId		 id of the item whose purchase was requested.
	 * @param purchaseIntent intent to purchase the item.
	 */
	static void onPurchaseIntent(@Nonnull String productId, @Nonnull PendingIntent purchaseIntent) {
		for (IBillingObserver o : getSynchronizedObservers()) {
			o.onPurchaseIntentOK(productId, purchaseIntent);
		}
	}

	static void onTransactionsRestored() {
		for (IBillingObserver o : getSynchronizedObservers()) {
			o.onTransactionsRestored();
		}
	}

	/**
	 * Registers the specified billing observer.
	 *
	 * @param observer the billing observer to add.
	 * @return true if the observer wasn't previously registered, false
	 *         otherwise.
	 * @see #unregisterObserver(IBillingObserver)
	 */
	static boolean registerObserver(@Nonnull IBillingObserver observer) {
		synchronized (observers) {
			return observers.add(observer);
		}
	}

	/**
	 * Unregisters the specified billing observer.
	 *
	 * @param observer the billing observer to unregister.
	 * @return true if the billing observer was unregistered, false otherwise.
	 * @see #registerObserver(IBillingObserver)
	 */
	static boolean unregisterObserver(@Nonnull IBillingObserver observer) {
		synchronized (observers) {
			return observers.remove(observer);
		}
	}

	/**
	 * Notifies observers of the purchase state change of the specified item.
	 *
	 * @param productId id of the item whose purchase state has changed.
	 * @param state  new purchase state of the item.
	 */
	static void notifyPurchaseStateChange(@Nonnull String productId, @Nonnull Transaction.PurchaseState state) {
		for (IBillingObserver o : getSynchronizedObservers()) {
			o.onPurchaseStateChanged(productId, state);
		}
	}

	// method gets a synchronized copy of list
	@Nonnull
	private static List<IBillingObserver> getSynchronizedObservers() {
		final List<IBillingObserver> result;
		synchronized (observers) {
			result = new ArrayList<IBillingObserver>(observers);
		}
		return result;
	}

	static void onRequestPurchaseResponse(@Nonnull String productId, @Nonnull ResponseCode response) {
		for (IBillingObserver o : getSynchronizedObservers()) {
			o.onRequestPurchaseResponse(productId, response);
		}
	}

	public static void onPurchaseIntentFailure(@Nonnull String productId, @Nonnull ResponseCode responseCode) {
		for (IBillingObserver o : getSynchronizedObservers()) {
			o.onPurchaseIntentFailure(productId, responseCode);
		}
	}

	public static void onErrorRestoreTransactions(@Nonnull ResponseCode response) {
		for (IBillingObserver o : getSynchronizedObservers()) {
			o.onErrorRestoreTransactions(response);
		}
	}
}
