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
import net.robotmedia.billing.IBillingObserver;
import net.robotmedia.billing.ResponseCode;
import net.robotmedia.billing.model.Transaction;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * User: serso
 * Date: 5/14/12
 * Time: 4:16 PM
 */
public class DefaultBillingObserver extends AbstractBillingObserver {

	@Nullable
	private final IBillingObserver nestedBillingObserver;

	public DefaultBillingObserver(@Nonnull Context context, @Nullable IBillingObserver nestedBillingObserver) {
		super(context);
		this.nestedBillingObserver = nestedBillingObserver;
	}

	@Override
	public void onCheckBillingSupportedResponse(boolean supported) {
		if (nestedBillingObserver != null) {
			nestedBillingObserver.onCheckBillingSupportedResponse(supported);
		}
	}

	@Override
	public void onPurchaseIntentOK(@Nonnull String productId, @Nonnull PendingIntent purchaseIntent) {
		super.onPurchaseIntentOK(productId, purchaseIntent);
		if (nestedBillingObserver != null) {
			nestedBillingObserver.onPurchaseIntentOK(productId, purchaseIntent);
		}
	}

	@Override
	public void onPurchaseIntentFailure(@Nonnull String productId, @Nonnull ResponseCode responseCode) {
		if (nestedBillingObserver != null) {
			nestedBillingObserver.onPurchaseIntentFailure(productId, responseCode);
		}
	}

	@Override
	public void onPurchaseStateChanged(@Nonnull String productId, @Nonnull Transaction.PurchaseState state) {
		if (nestedBillingObserver != null) {
			nestedBillingObserver.onPurchaseStateChanged(productId, state);
		}
	}

	@Override
	public void onRequestPurchaseResponse(@Nonnull String productId, @Nonnull ResponseCode response) {
		if (nestedBillingObserver != null) {
			nestedBillingObserver.onRequestPurchaseResponse(productId, response);
		}
	}

	@Override
	public void onTransactionsRestored() {
		super.onTransactionsRestored();
		if (nestedBillingObserver != null) {
			nestedBillingObserver.onTransactionsRestored();
		}
	}

	@Override
	public void onErrorRestoreTransactions(@Nonnull ResponseCode responseCode) {
		super.onErrorRestoreTransactions(responseCode);
		if (nestedBillingObserver != null) {
			nestedBillingObserver.onErrorRestoreTransactions(responseCode);
		}
	}
}
