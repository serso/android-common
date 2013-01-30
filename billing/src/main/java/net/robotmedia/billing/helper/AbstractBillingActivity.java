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

import net.robotmedia.billing.BillingController;
import net.robotmedia.billing.BillingController.BillingStatus;
import net.robotmedia.billing.ResponseCode;
import net.robotmedia.billing.model.Transaction.PurchaseState;
import android.app.Activity;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractBillingActivity extends Activity implements BillingController.IConfiguration {

	protected AbstractBillingObserver mBillingObserver;

	/**
	 * Returns the billing status. If it's currently unknown, requests to check
	 * if billing is supported and
	 * {@link net.robotmedia.billing.helper.AbstractBillingActivity#onBillingChecked(boolean)} should be
	 * called later with the result.
	 * 
	 * @return the current billing status (unknown, supported or unsupported).
	 * @see net.robotmedia.billing.helper.AbstractBillingActivity#onBillingChecked(boolean)
	 */
	public BillingStatus checkBillingSupported() {
		return BillingController.checkBillingSupported(this);
	}

	public abstract void onBillingChecked(boolean supported);

	@Override
	protected void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mBillingObserver = new AbstractBillingObserver(this) {

			@Override
			public void onCheckBillingSupportedResponse(boolean supported) {
				AbstractBillingActivity.this.onBillingChecked(supported);
			}

			@Override
			public void onPurchaseIntentFailure(@NotNull String productId, @NotNull ResponseCode responseCode) {
			}

			@Override
			public void onPurchaseStateChanged(@NotNull String productId, @NotNull PurchaseState state) {
				AbstractBillingActivity.this.onPurchaseStateChanged(productId, state);
			}

			@Override
			public void onRequestPurchaseResponse(@NotNull String productId, @NotNull ResponseCode response) {
				AbstractBillingActivity.this.onRequestPurchaseResponse(productId, response);
			}

			@Override
			public void onErrorRestoreTransactions(@NotNull ResponseCode responseCode) {
				// ignore errors when restoring transactions
			}
		};
		BillingController.registerObserver(mBillingObserver);
		BillingController.setConfiguration(this); // This activity will provide
		// the public key and salt
		this.checkBillingSupported();
		if (!mBillingObserver.isTransactionsRestored()) {
			BillingController.restoreTransactions(this);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		BillingController.unregisterObserver(mBillingObserver); // Avoid
																// receiving
		// notifications after
		// destroy
		BillingController.setConfiguration(null);
	}

	public abstract void onPurchaseStateChanged(String productId, PurchaseState state);;

	public abstract void onRequestPurchaseResponse(String productId, ResponseCode response);

	/**
	 * Requests the purchase of the specified item. The transaction will not be
	 * confirmed automatically; such confirmation could be handled in
	 * {@link net.robotmedia.billing.helper.AbstractBillingActivity#onPurchaseExecuted(String)}. If automatic
	 * confirmation is preferred use
	 * {@link BillingController#requestPurchase(android.content.Context, String, boolean)}
	 * instead.
	 * 
	 * @param productId
	 *            id of the item to be purchased.
	 */
	public void requestPurchase(String productId) {
		BillingController.requestPurchase(this, productId);
	}

	/**
	 * Requests to restore all transactions.
	 */
	public void restoreTransactions() {
		BillingController.restoreTransactions(this);
	}

}
