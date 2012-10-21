package net.robotmedia.billing;

import android.content.Context;
import android.content.Intent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * User: serso
 * Date: 1/17/12
 * Time: 1:23 PM
 */
enum BillingRequestType {

	CHECK_BILLING_SUPPORTED {
		@NotNull
		@Override
		protected BillingRequest getBillingRequest(@NotNull String packageName, @NotNull Intent intent, int startId) {
			return new BillingRequest.CheckBillingSupported(packageName, startId);
		}
	},

	CONFIRM_NOTIFICATIONS {
		@NotNull
		@Override
		protected BillingRequest getBillingRequest(@NotNull String packageName, @NotNull Intent intent, int startId) {
			final String[] notifyIds = intent.getStringArrayExtra(EXTRA_NOTIFY_IDS);
			return new BillingRequest.ConfirmNotifications(packageName, startId, notifyIds);
		}
	},

	GET_PURCHASE_INFORMATION {
		@NotNull
		@Override
		protected BillingRequest getBillingRequest(@NotNull String packageName, @NotNull Intent intent, int startId) {
			final long nonce = intent.getLongExtra(EXTRA_NONCE, 0);
			final String[] notifyIds = intent.getStringArrayExtra(EXTRA_NOTIFY_IDS);
			return new BillingRequest.GetPurchaseInformation(packageName, startId, notifyIds, nonce);
		}
	},

	REQUEST_PURCHASE {
		@NotNull
		@Override
		protected BillingRequest getBillingRequest(@NotNull String packageName, @NotNull Intent intent, int startId) {
			final String productId = intent.getStringExtra(EXTRA_ITEM_ID);
			final String developerPayload = intent.getStringExtra(EXTRA_DEVELOPER_PAYLOAD);
			return new BillingRequest.Purchase(packageName, startId, productId, developerPayload);
		}
	},

	RESTORE_TRANSACTIONS {
		@NotNull
		@Override
		protected BillingRequest getBillingRequest(@NotNull String packageName, @NotNull Intent intent, int startId) {
			final long nonce = intent.getLongExtra(EXTRA_NONCE, 0);
			return new BillingRequest.RestoreTransactions(packageName, startId, nonce);
		}
	};

	public static final String EXTRA_ITEM_ID = "ITEM_ID";
	public static final String EXTRA_NONCE = "EXTRA_NONCE";
	public static final String EXTRA_NOTIFY_IDS = "NOTIFY_IDS";
	public static final String EXTRA_DEVELOPER_PAYLOAD = "DEVELOPER_PAYLOAD";

	final void doAction(@NotNull IBillingService service, @NotNull Intent intent, int startId) {
		service.runRequestOrQueue(getBillingRequest(service.getPackageName(), intent, startId));
	}

	@NotNull
	protected abstract BillingRequest getBillingRequest(@NotNull String packageName, @NotNull Intent intent, int startId);

	@NotNull
	String toIntentAction(@NotNull Context context) {
		return context.getPackageName() + "." + this.name();
	}

	@Nullable
	static BillingRequestType fromIntentAction(@NotNull Intent intent) {
		final String actionString = intent.getAction();
		if (actionString == null) {
			return null;
		}

		final String[] split = actionString.split("\\.");
		if (split.length <= 0) {
			return null;
		}

		return BillingRequestType.valueOf(split[split.length - 1]);
	}
}
