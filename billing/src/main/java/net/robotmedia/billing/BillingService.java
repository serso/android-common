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

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import com.android.vending.billing.IMarketBillingService;
import net.robotmedia.billing.utils.Compatibility;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class BillingService extends Service implements ServiceConnection, IBillingService {

    @NotNull
    private static final String TAG = BillingService.class.getSimpleName();

	private static final String ACTION_MARKET_BILLING_SERVICE = "com.android.vending.billing.MarketBillingService.BIND";

	// all operations must be synchronized
	@NotNull
	private static final List<IBillingRequest> pendingRequests = new LinkedList<IBillingRequest>();

	private static final int MAX_RETRIES = 3;

	@Nullable
	private static IMarketBillingService service;

	static void checkBillingSupported(@NotNull Context context) {
		context.startService(createIntent(context, BillingRequestType.CHECK_BILLING_SUPPORTED));
	}

	static void confirmNotifications(@NotNull Context context, @NotNull String[] notifyIds) {
		final Intent intent = createIntent(context, BillingRequestType.CONFIRM_NOTIFICATIONS);
		intent.putExtra(BillingRequestType.EXTRA_NOTIFY_IDS, notifyIds);
		context.startService(intent);
	}

	static void confirmNotifications(@NotNull Context context, @NotNull Collection<String> notifyIds) {
		confirmNotifications(context, notifyIds.toArray(new String[notifyIds.size()]));
	}

	static void getPurchaseInformation(@NotNull Context context, @NotNull Collection<String> notifyIds, long nonce) {
		getPurchaseInformation(context, notifyIds.toArray(new String[notifyIds.size()]), nonce);
	}

	static void getPurchaseInformation(@NotNull Context context, @NotNull String[] notifyIds, long nonce) {
		final Intent intent = createIntent(context, BillingRequestType.GET_PURCHASE_INFORMATION);
		intent.putExtra(BillingRequestType.EXTRA_NOTIFY_IDS, notifyIds);
		intent.putExtra(BillingRequestType.EXTRA_NONCE, nonce);
		context.startService(intent);
	}

	static void requestPurchase(@NotNull Context context, @NotNull String productId, @Nullable String developerPayload) {
		final Intent intent = createIntent(context, BillingRequestType.REQUEST_PURCHASE);
		intent.putExtra(BillingRequestType.EXTRA_ITEM_ID, productId);
		intent.putExtra(BillingRequestType.EXTRA_DEVELOPER_PAYLOAD, developerPayload);
		context.startService(intent);
	}

	static void restoreTransactions(@NotNull Context context, long nonce) {
		final Intent intent = createIntent(context, BillingRequestType.RESTORE_TRANSACTIONS);
		intent.setClass(context, BillingService.class);
		intent.putExtra(BillingRequestType.EXTRA_NONCE, nonce);
		context.startService(intent);
	}

	@NotNull
	private static Intent createIntent(@NotNull Context context, @NotNull BillingRequestType action) {
		final Intent result = new Intent(action.toIntentAction(context));

		result.setClass(context, BillingService.class);

		return result;
	}

	private void bindMarketBillingService() {
		try {
			final boolean bindResult = bindService(new Intent(ACTION_MARKET_BILLING_SERVICE), this, Context.BIND_AUTO_CREATE);
			if (!bindResult) {
				Log.e(this.getClass().getSimpleName(), "Could not bind to MarketBillingService");
			}
		} catch (SecurityException e) {
			Log.e(this.getClass().getSimpleName(), "Could not bind to MarketBillingService", e);
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onServiceConnected(ComponentName name, IBinder service) {
		BillingService.service = IMarketBillingService.Stub.asInterface(service);
		runPendingRequests();
	}

	@Override
	public void onServiceDisconnected(ComponentName name) {
		service = null;
	}

	// This is the old onStart method that will be called on the pre-2.0
	// platform.  On 2.0 or later we override onStartCommand() so this
	// method will not be called.
	@Override
	public void onStart(Intent intent, int startId) {
		handleCommand(intent, startId);
	}

	// @Override // Avoid compile errors on pre-2.0
	public int onStartCommand(Intent intent, int flags, int startId) {
		handleCommand(intent, startId);
		return Compatibility.START_NOT_STICKY;
	}

	private void handleCommand(@NotNull Intent intent, int startId) {
		final BillingRequestType action = BillingRequestType.fromIntentAction(intent);
		if (action != null) {
			action.doAction(this, intent, startId);
		}
	}

	/**
	 * @return 'true' if all requests (on the moment of start) were processed during current procedure. False is possible only if service was disconnected during processing
	 */
	private boolean runPendingRequests() {
		int maxStartId = -1;

		synchronized (pendingRequests) {
			for (Iterator<IBillingRequest> it = pendingRequests.iterator(); it.hasNext(); ) {
				final IBillingRequest request = it.next();

				// if service was disconnected during procedure => connect
				if (service != null) {
					runRequest(service, request, 1);
					it.remove();

					maxStartId = Math.max(maxStartId, request.getStartId());
				} else {
					bindMarketBillingService();
					return false;
				}
			}

		}

		if (maxStartId >= 0) {
			stopSelf(maxStartId);
		}

		return true;
	}

	private boolean runRequest(@NotNull IMarketBillingService service, @NotNull IBillingRequest request, int counter) {
		try {
            BillingController.debug("Running request: " + request.getRequestType());

			final long requestId = request.run(service);

			BillingController.onRequestSent(requestId, request);

			return true;
		} catch (RemoteException e) {
            BillingController.debug("Remote exception: " + e.getMessage());

			Log.w(this.getClass().getSimpleName(), "Remote billing service crashed");
			if (counter < MAX_RETRIES) {
				return runRequest(service, request, counter + 1);
			} else {
				return false;
			}
		}
	}

	@Override
	public void runRequestOrQueue(@NotNull IBillingRequest request) {
		synchronized (pendingRequests) {
			pendingRequests.add(request);
		}

		if (service == null) {
			bindMarketBillingService();
		} else {
			runPendingRequests();
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// Ensure we're not leaking Android Market billing service
		if (service != null) {
			try {
				unbindService(this);
			} catch (IllegalArgumentException e) {
				// This might happen if the service was disconnected
			}
		}
	}

}
