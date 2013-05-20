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

package net.robotmedia.billing.model;

import org.json.JSONException;
import org.json.JSONObject;
import org.solovyev.common.JCloneable;
import org.solovyev.common.JObject;

import javax.annotation.Nonnull;
import java.util.Date;

public class Transaction extends JObject implements JCloneable<Transaction> {

	public static enum PurchaseState {
		// Responses to requestPurchase or restoreTransactions.

		// 0: User was charged for the order.
		PURCHASED(0),

		// 1: The charge failed on the server.
		CANCELLED(1),

		// 2: User received a refund for the order.
		REFUNDED(2);


		private final int id;

		PurchaseState(int id) {
			this.id = id;
		}

		public static PurchaseState valueOf(int id) {
			for (PurchaseState purchaseState : values()) {
				if (purchaseState.id == id) {
					return purchaseState;
				}
			}
			return CANCELLED;
		}
	}

	static final String DEVELOPER_PAYLOAD = "developerPayload";
	static final String NOTIFICATION_ID = "notificationId";
	static final String ORDER_ID = "orderId";
	static final String PACKAGE_NAME = "packageName";
	static final String PRODUCT_ID = "productId";
	static final String PURCHASE_STATE = "purchaseState";

	static final String PURCHASE_TIME = "purchaseTime";

	@Nonnull
	public static Transaction newInstance(@Nonnull JSONObject json) throws JSONException {
		final Transaction transaction = new Transaction();

		final int response = json.getInt(PURCHASE_STATE);
		transaction.purchaseState = PurchaseState.valueOf(response);
		transaction.productId = json.getString(PRODUCT_ID);
		transaction.packageName = json.getString(PACKAGE_NAME);
		transaction.purchaseTime = json.getLong(PURCHASE_TIME);
		transaction.orderId = json.optString(ORDER_ID, null);
		transaction.notificationId = json.optString(NOTIFICATION_ID, null);
		transaction.developerPayload = json.optString(DEVELOPER_PAYLOAD, null);

		return transaction;
	}

	public String developerPayload;
	public String notificationId;
	public String orderId;
	public String packageName;
	public String productId;
	public PurchaseState purchaseState;
	public long purchaseTime;

	public Transaction() {
	}

	public Transaction(String orderId,
					   String productId,
					   String packageName,
					   PurchaseState purchaseState,
					   String notificationId,
					   long purchaseTime,
					   String developerPayload) {
		this.orderId = orderId;
		this.productId = productId;
		this.packageName = packageName;
		this.purchaseState = purchaseState;
		this.notificationId = notificationId;
		this.purchaseTime = purchaseTime;
		this.developerPayload = developerPayload;
	}

	@Nonnull
	public JSONObject toJson() throws JSONException {
		final JSONObject json = new JSONObject();

		json.put(PURCHASE_STATE, this.purchaseState.id);
		json.put(PRODUCT_ID, this.productId);
		json.put(PACKAGE_NAME, this.packageName);
		json.put(PURCHASE_TIME, this.purchaseTime);
		json.put(ORDER_ID, this.orderId);
		json.put(NOTIFICATION_ID, this.notificationId);
		json.put(DEVELOPER_PAYLOAD, this.developerPayload);


		return json;
	}

	@Nonnull
	@Override
	public Transaction clone() {
		return (Transaction) super.clone();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Transaction that = (Transaction) o;

		if (purchaseTime != that.purchaseTime) return false;
		if (developerPayload != null ? !developerPayload.equals(that.developerPayload) : that.developerPayload != null)
			return false;
		if (notificationId != null ? !notificationId.equals(that.notificationId) : that.notificationId != null)
			return false;
		if (orderId != null ? !orderId.equals(that.orderId) : that.orderId != null) return false;
		if (packageName != null ? !packageName.equals(that.packageName) : that.packageName != null) return false;
		if (productId != null ? !productId.equals(that.productId) : that.productId != null) return false;
		if (purchaseState != that.purchaseState) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = developerPayload != null ? developerPayload.hashCode() : 0;
		result = 31 * result + (notificationId != null ? notificationId.hashCode() : 0);
		result = 31 * result + (orderId != null ? orderId.hashCode() : 0);
		result = 31 * result + (packageName != null ? packageName.hashCode() : 0);
		result = 31 * result + (productId != null ? productId.hashCode() : 0);
		result = 31 * result + (purchaseState != null ? purchaseState.hashCode() : 0);
		result = 31 * result + (int) (purchaseTime ^ (purchaseTime >>> 32));
		return result;
	}

	@Override
	public String toString() {
		return "Transaction{" +
				"orderId='" + orderId + '\'' +
				", productId='" + productId + '\'' +
				", purchaseTime=" + new Date(purchaseTime) +
				", purchaseState=" + purchaseState +
				'}';
	}
}
