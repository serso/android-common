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

import net.robotmedia.billing.model.Transaction.PurchaseState;
import android.app.PendingIntent;
import javax.annotation.Nonnull;

public interface IBillingObserver {

	/**
	 * Called each time
	 *
	 * @param supported if true, in-app billing is supported. Otherwise, it isn't.
	 * @see BillingController#checkBillingSupported(android.content.Context)
	 */
	public void onCheckBillingSupportedResponse(boolean supported);

	/**
	 * Called after requesting the purchase of the specified item.
	 *
	 * @param productId	  id of the item whose purchase was requested.
	 * @param purchaseIntent a purchase pending intent for the specified item.
	 * @see BillingController#requestPurchase(android.content.Context, String,
	 *	  boolean)
	 */
	public void onPurchaseIntentOK(@Nonnull String productId, @Nonnull PendingIntent purchaseIntent);

	/**
	 * Called when purchase intent was not sent due to billing service error
	 *
	 * @param productId	id of the item whose purchase was requested
	 * @param responseCode one of the failures response codes from billing service
	 */
	void onPurchaseIntentFailure(@Nonnull String productId, @Nonnull ResponseCode responseCode);

	/**
	 * Called when the specified item is purchased, cancelled or refunded.
	 *
	 * @param productId id of the item whose purchase state has changed.
	 * @param state	 purchase state of the specified item.
	 */
	public void onPurchaseStateChanged(@Nonnull String productId, @Nonnull PurchaseState state);

	/**
	 * Called with the response for the purchase request of the specified item.
	 * This is used for reporting various errors, or if the user backed out and
	 * didn't purchase the item.
	 *
	 * @param productId id of the item whose purchase was requested
	 * @param response  response of the purchase request
	 */
	public void onRequestPurchaseResponse(@Nonnull String productId, @Nonnull ResponseCode response);

	/**
	 * Called when a restore transactions request has been successfully
	 * received by the server.
	 */
	public void onTransactionsRestored();

	/**
	 * Called when a restore transactions request ended with a server error.
	 * @param responseCode response code
	 */
	public void onErrorRestoreTransactions(@Nonnull ResponseCode responseCode);
}
