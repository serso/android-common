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

package org.solovyev.android.samples.ads;

import android.app.Activity;
import android.app.PendingIntent;
import android.os.Bundle;
import android.view.*;
import net.robotmedia.billing.BillingController;
import net.robotmedia.billing.IBillingObserver;
import net.robotmedia.billing.ResponseCode;
import net.robotmedia.billing.helper.AbstractBillingObserver;
import net.robotmedia.billing.model.Transaction;

import javax.annotation.Nonnull;

import org.solovyev.android.Activities;
import org.solovyev.android.ads.AdsController;
import org.solovyev.android.samples.R;
import org.solovyev.android.samples.SamplesApplication;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static org.solovyev.android.samples.SamplesApplication.ADS_FREE_PRODUCT;

/**
 * User: serso
 * Date: 8/10/12
 * Time: 1:11 PM
 */
public class SamplesAdsActivity extends Activity {

	@Nonnull
	private final SamplesBillingObserver billingObserver = new SamplesBillingObserver();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.acl_ads_layout);
		final ViewGroup views = (ViewGroup) findViewById(R.id.acl_ads_linearlayout);
		AdsController.getInstance().inflateAd(this, views, R.id.acl_ads_advertisement_framelayout);

		BillingController.registerObserver(billingObserver);
		final View removeAdsButton = views.findViewById(R.id.acl_ads_remove_advertisement_button);
		if (AdsController.getInstance().isAdFree(this)) {
			removeAdsButton.setEnabled(false);
		}
		removeAdsButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				BillingController.requestPurchase(SamplesAdsActivity.this, ADS_FREE_PRODUCT);
			}
		});
	}

	@Override
	protected void onDestroy() {
		BillingController.unregisterObserver(billingObserver);
		super.onDestroy();
	}

	private final class SamplesBillingObserver implements IBillingObserver {

		@Override
		public void onCheckBillingSupportedResponse(boolean supported) {

		}

		@Override
		public void onPurchaseIntentOK(@Nonnull String productId, @Nonnull PendingIntent purchaseIntent) {

		}

		@Override
		public void onPurchaseIntentFailure(@Nonnull String productId, @Nonnull ResponseCode responseCode) {

		}

		@Override
		public void onPurchaseStateChanged(@Nonnull String productId, @Nonnull Transaction.PurchaseState state) {
			if (ADS_FREE_PRODUCT.equals(productId)) {
				switch (state) {
					case PURCHASED:
					case CANCELLED:
					case REFUNDED:
						Activities.restartActivity(SamplesAdsActivity.this);
						break;
				}
			}
		}

		@Override
		public void onRequestPurchaseResponse(@Nonnull String productId, @Nonnull ResponseCode response) {

		}

		@Override
		public void onTransactionsRestored() {

		}

		@Override
		public void onErrorRestoreTransactions(@Nonnull ResponseCode responseCode) {

		}
	}
}
