/*   Copyright 2011 Robot Media SL (http://www.robotmedia.net)
*
*   Licensed under the Apache License, Version 2.0 (the "License");
*   you may not use this file except in compliance with the License.
*   You may obtain a copy of the License at
*
*       http://www.apache.org/licenses/LICENSE-2.0
*
*   Unless required by applicable law or agreed to in writing, software
*   distributed under the License is distributed on an "AS IS" BASIS,
*   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*   See the License for the specific language governing permissions and
*   limitations under the License.
*/

package net.robotmedia.billing;

import android.app.PendingIntent;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;

import com.android.vending.billing.IMarketBillingService;
import org.jetbrains.annotations.NotNull;

abstract class BillingRequest implements IBillingRequest {

	private static final String KEY_BILLING_REQUEST = "BILLING_REQUEST";

	private static final String KEY_API_VERSION = "API_VERSION";
	private static final String KEY_PACKAGE_NAME = "PACKAGE_NAME";
	private static final String KEY_RESPONSE_CODE = "RESPONSE_CODE";

	protected static final String KEY_REQUEST_ID = "REQUEST_ID";

	private static final String KEY_NONCE = "NONCE";

	public static final long IGNORE_REQUEST_ID = -1;

	@NotNull
	private String packageName;

	private int startId;
	private boolean success;
	private long nonce;

	public BillingRequest(@NotNull String packageName, int startId) {
		this.packageName = packageName;
		this.startId = startId;
	}

	public BillingRequest(@NotNull String packageName, int startId, long nonce) {
		this.packageName = packageName;
		this.startId = startId;
		this.nonce = nonce;
	}

	protected void addParams(@NotNull Bundle request) {
		// Do nothing by default
	}

	@Override
	public long getNonce() {
		return nonce;
	}

	@Override
	public boolean hasNonce() {
		return false;
	}

	@Override
	public boolean isSuccess() {
		return success;
	}

	@NotNull
	private Bundle makeRequestBundle() {
		final Bundle request = new Bundle();
		request.putString(KEY_BILLING_REQUEST, getRequestType().name());
		request.putInt(KEY_API_VERSION, 1);
		request.putString(KEY_PACKAGE_NAME, packageName);
		if (hasNonce()) {
			request.putLong(KEY_NONCE, nonce);
		}
		return request;
	}

	@Override
	public void onResponseCode(@NotNull ResponseCode response) {
		// Do nothing by default
	}

	protected void processOkResponse(@NotNull Bundle response) {
		// Do nothing by default
	}

	protected void processNotOkResponse(@NotNull Bundle response, @NotNull ResponseCode responseCode) {
		// Do nothing by default
	}

	@Override
	public final long run(@NotNull IMarketBillingService service) throws RemoteException {
		final Bundle request = makeRequestBundle();
		addParams(request);

		final Bundle response = service.sendBillingRequest(request);
		if (validateResponse(response)) {
			processOkResponse(response);
			return response.getLong(KEY_REQUEST_ID, IGNORE_REQUEST_ID);
		} else {
			processNotOkResponse(response, ResponseCode.valueOf(response.getInt(KEY_RESPONSE_CODE)));
			return IGNORE_REQUEST_ID;
		}
	}

	public void setNonce(long nonce) {
		this.nonce = nonce;
	}

	private boolean validateResponse(@NotNull Bundle response) {
		final int responseCode = response.getInt(KEY_RESPONSE_CODE);
		success = ResponseCode.isOk(responseCode);
		if (!success) {
			Log.w(this.getClass().getSimpleName(), "Error with response code " + ResponseCode.valueOf(responseCode));
		}
		return success;
	}

	@Override
	public int getStartId() {
		return startId;
	}

	/**
	* User: serso
	* Date: 1/17/12
	* Time: 12:45 PM
	*/
	static class CheckBillingSupported extends BillingRequest {

		public CheckBillingSupported(String packageName, int startId) {
			super(packageName, startId);
		}

		@NotNull
		@Override
		public BillingRequestType getRequestType() {
			return BillingRequestType.CHECK_BILLING_SUPPORTED;
		}

		@Override
		protected void processOkResponse(@NotNull Bundle response) {
			final boolean supported = this.isSuccess();
			BillingController.onCheckBillingSupportedResponse(supported);
		}
	}

	/**
	* User: serso
	* Date: 1/17/12
	* Time: 12:45 PM
	*/
	static class ConfirmNotifications extends BillingRequest {

		@NotNull
		private final String[] notifyIds;

		private static final String KEY_NOTIFY_IDS = "NOTIFY_IDS";

		public ConfirmNotifications(@NotNull String packageName, int startId, @NotNull String[] notifyIds) {
			super(packageName, startId);
			this.notifyIds = notifyIds;
		}

		@Override
		protected void addParams(@NotNull Bundle request) {
			request.putStringArray(KEY_NOTIFY_IDS, notifyIds);
		}

		@NotNull
		@Override
		public BillingRequestType getRequestType() {
			return BillingRequestType.CONFIRM_NOTIFICATIONS;
		}

	}

	/**
	* User: serso
	* Date: 1/17/12
	* Time: 12:45 PM
	*/
	static class GetPurchaseInformation extends BillingRequest {

		private String[] notifyIds;

		private static final String KEY_NOTIFY_IDS = "NOTIFY_IDS";

		public GetPurchaseInformation(String packageName, int startId, String[] notifyIds, long nonce) {
			super(packageName,startId, nonce);
			this.notifyIds = notifyIds;
		}

		@Override
		protected void addParams(@NotNull Bundle request) {
			request.putStringArray(KEY_NOTIFY_IDS, notifyIds);
		}

		@NotNull
		@Override
		public BillingRequestType getRequestType() {
			return BillingRequestType.GET_PURCHASE_INFORMATION;
		}

		@Override public boolean hasNonce() { return true; }

	}

	/**
	* User: serso
	* Date: 1/17/12
	* Time: 12:45 PM
	*/
	static class Purchase extends BillingRequest {

		private String productId;
		private String developerPayload;

		private static final String KEY_ITEM_ID = "ITEM_ID";
		private static final String KEY_DEVELOPER_PAYLOAD = "DEVELOPER_PAYLOAD";
		private static final String KEY_PURCHASE_INTENT = "PURCHASE_INTENT";

		public Purchase(String packageName, int startId, String productId, String developerPayload) {
			super(packageName, startId);
			this.productId = productId;
			this.developerPayload = developerPayload;
		}

		@Override
		protected void addParams(@NotNull Bundle request) {
			request.putString(KEY_ITEM_ID, productId);
			if (developerPayload != null) {
				request.putString(KEY_DEVELOPER_PAYLOAD, developerPayload);
			}
		}

		@NotNull
		@Override
		public BillingRequestType getRequestType() {
			return BillingRequestType.REQUEST_PURCHASE;
		}

		@Override
		public void onResponseCode(@NotNull ResponseCode response) {
			super.onResponseCode(response);
			BillingController.onRequestPurchaseResponse(productId, response);
		}

		@Override
		protected void processOkResponse(@NotNull Bundle response) {
			final PendingIntent purchaseIntent = response.getParcelable(KEY_PURCHASE_INTENT);
			BillingController.onPurchaseIntent(productId, purchaseIntent);
		}

		@Override
		protected void processNotOkResponse(@NotNull Bundle response, @NotNull ResponseCode responseCode) {
			BillingController.onPurchaseIntentFailure(productId, responseCode);
		}
	}

	/**
	* User: serso
	* Date: 1/17/12
	* Time: 12:45 PM
	*/
	static class RestoreTransactions extends BillingRequest {

		public RestoreTransactions(String packageName, int startId, long nonce) {
			super(packageName, startId, nonce);
		}

		@NotNull
		@Override
		public BillingRequestType getRequestType() {
			return BillingRequestType.RESTORE_TRANSACTIONS;
		}

		@Override public boolean hasNonce() { return true; }

		@Override
		public void onResponseCode(@NotNull ResponseCode response) {
			super.onResponseCode(response);

			if (response == ResponseCode.RESULT_OK) {
    			BillingController.onTransactionsRestored();
    		} else {
    			BillingController.onErrorRestoreTransactions(response);
    		}
		}

	}
}