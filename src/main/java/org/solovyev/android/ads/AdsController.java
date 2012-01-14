package org.solovyev.android.ads;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.ViewGroup;
import com.google.ads.AdView;
import net.robotmedia.billing.BillingController;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.solovyev.android.AndroidUtils;

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

	private boolean transactionsRestored = false;

	public void init(@NotNull String admobUserId, @NotNull String adFreeProductId) {
		this.admobUserId = admobUserId;
		this.adFreeProductId = adFreeProductId;

		this.initialized = true;
	}

	private boolean isAdFreePurchased(@NotNull Context context) {
		return BillingController.isPurchased(context.getApplicationContext(), adFreeProductId);
	}

	public boolean isAdFree(@NotNull Context context) {
		// check if user already bought this product
		boolean purchased = isAdFreePurchased(context);
		if (!purchased && !transactionsRestored) {
			// we must to restore all transactions done by user to guarantee that product was purchased or not
			BillingController.restoreTransactions(context);

			transactionsRestored = true;

			// todo serso: may be call net.robotmedia.billing.BillingController.restoreTransactions() always before first check and get rid of second check
			// check the billing one more time
			purchased = isAdFreePurchased(context);
		}
		return purchased;
	}

	@Nullable
	public AdView inflateAd(@NotNull Activity activity, @Nullable ViewGroup parentView, int parentViewId) {
		AdView result = null;
		if (!isAdFree(activity)) {
			Log.d(activity.getClass().getName(), "Application is not ad free - inflating ad!");
			final List<String> keywords = Collections.emptyList();
			result = AndroidUtils.createAndInflateAdView(activity, admobUserId, parentView, parentViewId, keywords);
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
