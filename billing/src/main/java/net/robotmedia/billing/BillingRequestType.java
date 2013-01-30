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
