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
 * ---------------------------------------------------------------------
 * Contact details
 *
 * Email: se.solovyev@gmail.com
 * Site:  http://se.solovyev.org
 */

package org.solovyev.android.ads;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.ViewGroup;
import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;
import net.robotmedia.billing.BillingController;
import net.robotmedia.billing.helper.AbstractBillingObserver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

/**
 * User: serso
 * Date: 1/14/12
 * Time: 9:18 PM
 */
public final class AdsController {

	private static final AdsController instance = new AdsController();

	private AdsController() {
	}

	@NotNull
	public static AdsController getInstance() {
		return instance;
	}

	@NotNull
	private String admobUserId;

	@NotNull
	private String adFreeProductId;

	private boolean initialized = false;

	@NotNull
	public AdView createAndInflateAdView(@NotNull Activity activity,
												@NotNull String admobAccountId,
												@Nullable ViewGroup parentView,
												int layoutId,
												@NotNull List<String> keywords) {
		final ViewGroup layout = parentView != null ? parentView : (ViewGroup) activity.findViewById(layoutId);

		// Create the adView
		final AdView adView = new AdView(activity, AdSize.BANNER, admobAccountId);

		// Add the adView to it
		layout.addView(adView);

		// Initiate a generic request to load it with an ad
		final AdRequest adRequest = new AdRequest();

		// todo serso: revert - only for tests
		//adRequest.addTestDevice(AdRequest.TEST_EMULATOR);
		//adRequest.addTestDevice("DB3C2F605A1296971898F0E60224A927");

		for (String keyword : keywords) {
			adRequest.addKeyword(keyword);
		}
		adView.loadAd(adRequest);

		return adView;
	}

	public void init(@NotNull String admobUserId, @NotNull String adFreeProductId, @NotNull BillingController.IConfiguration configuration) {
		this.admobUserId = admobUserId;
		this.adFreeProductId = adFreeProductId;

		//BillingController.setDebug(true);
		BillingController.setConfiguration(configuration);

		this.initialized = true;
	}

	private boolean isAdFreePurchased(@NotNull Context context) {
        // todo serso: this piece of code is shared between two branches in Android Calculator project (Calculator++) don't forget to change code below for correct work
        // Blackberry playbook doesn't support billing => this it the only one point where we can stop application to communicate with billing service
		//return true;
        return BillingController.isPurchased(context.getApplicationContext(), adFreeProductId);
	}

	public boolean isAdFree(@NotNull Context context) {
		// check if user already bought this product
		boolean purchased = isAdFreePurchased(context);
		if ( !purchased ) {
			if (!AbstractBillingObserver.isTransactionsRestored(context)) {
				// we must to restore all transactions done by user to guarantee that product was purchased or not
				BillingController.restoreTransactions(context);

				// todo serso: may be call net.robotmedia.billing.BillingController.restoreTransactions() always before first check and get rid of second check
				// check the billing one more time
				purchased = isAdFreePurchased(context);
			}
		}
		return purchased;
	}

	@Nullable
	public AdView inflateAd(@NotNull Activity activity, @Nullable ViewGroup parentView, int parentViewId) {
		AdView result = null;
		if (!isAdFree(activity)) {
			Log.d(activity.getClass().getName(), "Application is not ad free - inflating ad!");
			final List<String> keywords = Collections.emptyList();
			result = createAndInflateAdView(activity, admobUserId, parentView, parentViewId, keywords);
		} else {
			Log.d(activity.getClass().getName(), "Application is ad free - no ads!");
		}

		return result;
	}

	@Nullable
	public AdView inflateAd(@NotNull Activity activity) {
		return inflateAd(activity, null, R.id.ad_parent_view);
	}

	private void checkState() {
		if (!initialized) {
			throw new IllegalStateException(AdsController.class.getName() + " must be initialized before usage!");
		}
	}
}
