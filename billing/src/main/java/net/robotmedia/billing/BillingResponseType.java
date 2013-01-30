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
 * Date: 1/18/12
 * Time: 11:05 AM
 */
enum BillingResponseType {

	IN_APP_NOTIFY {
		@Override
		protected void doAction(@NotNull Context context, @NotNull Intent intent) {
			String notifyId = intent.getStringExtra(EXTRA_NOTIFICATION_ID);
			BillingController.onNotify(context, notifyId);
		}
	},

	RESPONSE_CODE {
		@Override
		protected void doAction(@NotNull Context context, @NotNull Intent intent) {
			final long requestId = intent.getLongExtra(EXTRA_REQUEST_ID, -1);
			final int responseCode = intent.getIntExtra(EXTRA_RESPONSE_CODE, 0);
			BillingController.onResponseCode(requestId, responseCode);
		}
	},

	PURCHASE_STATE_CHANGED {
		@Override
		protected void doAction(@NotNull Context context, @NotNull Intent intent) {
			final String signedData = intent.getStringExtra(EXTRA_INAPP_SIGNED_DATA);
			final String signature = intent.getStringExtra(EXTRA_INAPP_SIGNATURE);
			BillingController.onPurchaseStateChanged(context, signedData, signature);
		}
	};

	static final String EXTRA_NOTIFICATION_ID = "notification_id";
	static final String EXTRA_INAPP_SIGNED_DATA = "inapp_signed_data";
	static final String EXTRA_INAPP_SIGNATURE = "inapp_signature";
	static final String EXTRA_REQUEST_ID = "request_id";
	static final String EXTRA_RESPONSE_CODE = "response_code";

	protected abstract void doAction(@NotNull Context context, @NotNull Intent intent);

	@NotNull
	String toIntentAction() {
		return "com.android.vending.billing." + this.name();
	}

	@Nullable
	static BillingResponseType fromIntentAction(@NotNull Intent intent) {
		final String actionString = intent.getAction();
		if (actionString == null) {
			return null;
		}

		final String[] split = actionString.split("\\.");
		if (split.length <= 0) {
			return null;
		}

		try {
			return BillingResponseType.valueOf(split[split.length - 1]);
		} catch (IllegalArgumentException e) {
			// unexpected response
			return null;
		}
	}

}
