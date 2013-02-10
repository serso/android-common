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

import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import net.robotmedia.billing.model.Transaction;
import net.robotmedia.billing.model.TransactionManager;
import net.robotmedia.billing.security.BillingSecurity;
import net.robotmedia.billing.security.DefaultSignatureValidator;
import net.robotmedia.billing.security.ISignatureValidator;
import net.robotmedia.billing.utils.AESObfuscator;
import net.robotmedia.billing.utils.Compatibility;
import net.robotmedia.billing.utils.ObfuscateUtils;
import net.robotmedia.billing.utils.Security;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.solovyev.common.security.CiphererException;
import org.solovyev.common.security.SecurityService;

import javax.crypto.SecretKey;
import java.util.*;

public class BillingController {

    @Nullable
    private static SecretKey secretKey;

    @NotNull
    public static SecretKey getSecretKey(@NotNull Context context) throws CiphererException {
        if ( secretKey == null ) {
            final byte[] salt = getSalt();
            final String password = BillingSecurity.generatePassword(context);
            secretKey = getTransactionObfuscator().getSecretKeyProvider().getSecretKey(password, salt);
        }
        return secretKey;
    }

    public static enum BillingStatus {
		UNKNOWN,
		SUPPORTED,
		UNSUPPORTED
	}

	/**
	 * Used to provide on-demand values to the billing controller.
	 */
	public static interface IConfiguration {

		/**
		 * Returns a salt for the obfuscation of purchases in local memory.
		 * NOTE: this array must be the same during different application starts
		 * or user must call the net.robotmedia.billing.BillingController#restoreTransactions(android.content.Context) method to get all transaction from market
		 *
		 * @return array of 20 random bytes.
		 */
		public byte[] getObfuscationSalt();

		/**
		 * Returns the public key used to verify the signature of responses of
		 * the Market Billing service.
		 *
		 * @return Base64 encoded public key.
		 */
		public String getPublicKey();
	}

	private static final String JSON_NONCE = "nonce";
	private static final String JSON_ORDERS = "orders";

	public static final String LOG_TAG = "Billing";

	@NotNull
	private static BillingStatus status = BillingStatus.UNKNOWN;

	private static IConfiguration configuration = null;
	private static boolean debug = false;

	@Nullable
	private static ISignatureValidator validator = null;

	// todo serso: we only queue to this list and never remove (probably we should do it inside net.robotmedia.billing.BillingController.onPurchaseStateChanged )
	// synchronized field
	// value: product id with automatic confirmation
	@NotNull
	private static final Set<String> automaticConfirmations = new HashSet<String>();

	// todo serso: we only queue to this list and never remove (probably we should do it inside net.robotmedia.billing.BillingController.confirmNotifications )
	// synchronized field
	@NotNull
	private static final Map<String, Set<String>> manualConfirmations = new HashMap<String, Set<String>>();

	// synchronized field
	@NotNull
	private static final Map<Long, IBillingRequest> pendingRequests = new HashMap<Long, IBillingRequest>();

    @Nullable
    private static SecurityService<Transaction, Transaction, byte[]> transactionObfuscator;

	/**
	 * Adds the specified notification to the set of manual confirmations of the
	 * specified item.
	 *
	 * @param productId	  id of the item.
	 * @param notificationId id of the notification.
	 */
	private static void addManualConfirmation(@NotNull String productId, @NotNull String notificationId) {
		synchronized (manualConfirmations) {
			Set<String> notifications = manualConfirmations.get(productId);
			if (notifications == null) {
				notifications = new HashSet<String>();
				manualConfirmations.put(productId, notifications);
			}
			notifications.add(notificationId);
		}
	}

	/**
	 * Returns the current billing status. NOTE: current billing status may be not the same as the actual status needs some time to update (but nevertheless it can be used as first approach)
	 * This method calls billing service to determine the exact status and notify listeners through IBillingObserver#onCheckBillingSupportedResponse(boolean) method
	 *
	 * @param context context
	 *
	 * @return the current billing status (unknown, supported or unsupported)
	 *
	 * @see IBillingObserver#onCheckBillingSupportedResponse(boolean)
	 */
	@NotNull
	public static BillingStatus checkBillingSupported(@NotNull Context context) {
		BillingService.checkBillingSupported(context);
		return status;
	}

	/**
	 * Called after the response to a
	 * {@link net.robotmedia.billing.BillingRequest.CheckBillingSupported} request is
	 * received.
	 *
	 * @param supported billing supported
	 */
	static void onCheckBillingSupportedResponse(boolean supported) {
		status = supported ? BillingStatus.SUPPORTED : BillingStatus.UNSUPPORTED;
		BillingObserverRegistry.onCheckBillingSupportedResponse(supported);
	}


	/**
	 * Requests to confirm all pending MANUAL notifications for the specified item.
	 *
	 * @param context   context
	 * @param productId id of the item whose purchase must be confirmed.
	 * @return true if pending notifications for this item were found, false
	 *         otherwise.
	 */
	public static boolean confirmNotifications(@NotNull Context context, @NotNull String productId) {
		synchronized (manualConfirmations) {
			final Set<String> notifications = manualConfirmations.get(productId);
			if (notifications != null) {
				confirmNotifications(context, notifications);
				return true;
			} else {
				return false;
			}
		}
	}

	/**
	 * Requests to confirm all specified notifications.
	 *
	 * @param context   context
	 * @param notifyIds array with the ids of all the notifications to confirm.
	 */
	private static void confirmNotifications(@NotNull Context context, @NotNull String[] notifyIds) {
		BillingService.confirmNotifications(context, notifyIds);
	}

	/**
	 * Requests to confirm all specified notifications.
	 *
	 * @param context   context
	 * @param notifyIds array with the ids of all the notifications to confirm.
	 */
	private static void confirmNotifications(@NotNull Context context, @NotNull Collection<String> notifyIds) {
		BillingService.confirmNotifications(context, notifyIds);
	}

	/**
	 * Returns the number of purchases for the specified item. Only transactions with state PURCHASED are counted
	 *
	 * @param context   context
	 * @param productId id of the item whose purchases will be counted.
	 * @return number of purchases for the specified item.
	 */
	public static int countPurchases(@NotNull Context context, @NotNull String productId) {
		final String obfuscatedItemId = Security.obfuscate(context, getSalt(), productId);

		// item id != null => obfuscatedItemId != null
		assert obfuscatedItemId != null;
		return TransactionManager.countPurchases(obfuscatedItemId);
	}

	protected static void debug(@Nullable String message) {
		if (debug && message != null) {
			Log.d(LOG_TAG, message);
		}
	}

	/**
	 * Requests purchase information for the specified notification. Immediately
	 * followed by a call to
	 * {@link #onPurchaseStateChanged(android.content.Context, String, String)}, if the request
	 * is successful.
	 *
	 * @param context  context
	 * @param notifyId id of the notification whose purchase information is
	 *                 requested.
	 */
	private static void getPurchaseInformation(@NotNull Context context, @NotNull String notifyId) {
		final long nonce = Security.generateNonce();
		BillingService.getPurchaseInformation(context, new String[]{notifyId}, nonce);
	}

	/**
	 * Gets the salt from the configuration and logs a warning if it's null.
	 *
	 * @return salt.
	 */
	@Nullable
	private static byte[] getSalt() {
		byte[] salt = null;
		if (configuration == null || ((salt = configuration.getObfuscationSalt()) == null)) {
			Log.w(LOG_TAG, "Can't (un)obfuscate purchases without salt");
		}
		return salt;
	}

	/**
	 * Lists all transactions stored locally, including cancellations and
	 * refunds.
	 *
	 * @param context context
	 * @return list of transactions.
	 */
	@NotNull
	public static List<Transaction> getTransactions(@NotNull Context context) {
		final List<Transaction> transactions = TransactionManager.getTransactions();
		ObfuscateUtils.unobfuscate(context, transactions, getSalt());
		return transactions;
	}

    /**
	 * Lists all transactions of the specified item, stored locally.
	 *
	 * @param context   context
	 * @param productId id of the item whose transactions will be returned.
	 * @return list of transactions.
	 */
	@NotNull
	public static List<Transaction> getTransactions(@NotNull Context context, @NotNull String productId) {
		byte[] salt = getSalt();

		final String obfuscatedItemId = Security.obfuscate(context, getSalt(), productId);

		assert obfuscatedItemId != null;

		final List<Transaction> transactions = TransactionManager.getTransactions(obfuscatedItemId);
		ObfuscateUtils.unobfuscate(context, transactions, salt);

		return transactions;
	}

	/**
	 * Returns true if the specified item has been registered as purchased in
	 * local memory. Note that if the item was later canceled or refunded this
	 * will still return true. Also note that the item might have been purchased
	 * in another installation, but not yet registered in this one.
	 *
	 * @param context   context
	 * @param productId item id.
	 * @return true if the specified item is purchased, false otherwise.
	 */
	public static boolean isPurchased(@NotNull Context context, @NotNull String productId) {
		final byte[] salt = getSalt();
		final String obfuscatedItemId = Security.obfuscate(context, salt, productId);

		assert obfuscatedItemId != null;
		return TransactionManager.isPurchased(obfuscatedItemId);
	}

	/**
	 * Called when an IN_APP_NOTIFY message is received.
	 *
	 * @param context  context
	 * @param notifyId notification id.
	 */
	protected static void onNotify(@NotNull Context context, @NotNull String notifyId) {
		debug("Notification " + notifyId + " available");

		getPurchaseInformation(context, notifyId);
	}

	/**
	 * Called after the response to a
	 * {@link net.robotmedia.billing.BillingRequest.GetPurchaseInformation} request is
	 * received. Registers all transactions in local memory and confirms those
	 * who can be confirmed automatically.
	 *
	 * @param context	context
	 * @param signedData signed JSON data received from the Market Billing service.
	 * @param signature  data signature.
	 */
	protected static void onPurchaseStateChanged(@NotNull Context context, @Nullable String signedData, @Nullable String signature) {
		debug("Purchase state changed");

		if (TextUtils.isEmpty(signedData)) {
			Log.w(LOG_TAG, "Signed data is empty");
			return;
		}

		if (!debug) {
			if (TextUtils.isEmpty(signature)) {
				Log.w(LOG_TAG, "Empty signature requires debug mode");
				return;
			}

			final ISignatureValidator validator = getSignatureValidator();
			if (!validator.validate(signedData, signature)) {
				Log.w(LOG_TAG, "Signature does not match data.");
				return;
			}
		}

		List<Transaction> transactions;
		try {
			final JSONObject jObject = new JSONObject(signedData);
			if (!verifyNonce(jObject)) {
				Log.w(LOG_TAG, "Invalid nonce");
				return;
			}
			transactions = parseTransactions(jObject);
		} catch (JSONException e) {
			Log.e(LOG_TAG, "JSON exception: ", e);
			return;
		}

		final List<String> confirmations = new ArrayList<String>();
		for (Transaction transaction : transactions) {

			if (transaction.notificationId != null) {
				synchronized (automaticConfirmations) {
					if (automaticConfirmations.contains(transaction.productId)) {
						confirmations.add(transaction.notificationId);
					} else {
						// TODO: Discriminate between purchases, cancellations and refunds.
						addManualConfirmation(transaction.productId, transaction.notificationId);
					}
				}
			}

			storeTransaction(context, transaction);
			BillingObserverRegistry.notifyPurchaseStateChange(transaction.productId, transaction.purchaseState);
		}

		if (!confirmations.isEmpty()) {
			final String[] notifyIds = confirmations.toArray(new String[confirmations.size()]);
			confirmNotifications(context, notifyIds);
		}
	}

	/**
	 * Called after a {@link BillingRequest} is
	 * sent.
	 *
	 * @param requestId the id the request.
	 * @param request   the billing request.
	 */
	protected static void onRequestSent(long requestId, @NotNull IBillingRequest request) {
		debug("Request " + requestId + " of type " + request.getRequestType() + " sent");

		if (request.isSuccess()) {
			synchronized (pendingRequests) {
				pendingRequests.put(requestId, request);
			}
		} else if (request.hasNonce()) {
			// in case of unsuccessful request with nonce we shall unregister nonce
			Security.removeNonce(request.getNonce());
		}
	}

	/**
	 * Called after a {@link BillingRequest} is
	 * sent.
	 *
	 * @param requestId	the id of the request.
	 * @param responseCode the response code.
	 * @see ResponseCode
	 */
	protected static void onResponseCode(long requestId, int responseCode) {
		final ResponseCode response = ResponseCode.valueOf(responseCode);
		debug("Request " + requestId + " received response " + response);

		synchronized (pendingRequests) {
			final IBillingRequest request = pendingRequests.get(requestId);
			if (request != null) {
				pendingRequests.remove(requestId);
				request.onResponseCode(response);
			}
		}
	}

	/**
	 * Parse all purchases from the JSON data received from the Market Billing
	 * service.
	 *
	 * @param data JSON data received from the Market Billing service.
	 * @return list of purchases.
	 * @throws org.json.JSONException if the data couldn't be properly parsed.
	 */
	@NotNull
	private static List<Transaction> parseTransactions(@NotNull JSONObject data) throws JSONException {
		final List<Transaction> result = new ArrayList<Transaction>();

		final JSONArray orders = data.optJSONArray(JSON_ORDERS);
		if (orders != null) {
			for (int i = 0; i < orders.length(); i++) {
				final JSONObject jElement = orders.getJSONObject(i);
				result.add(Transaction.newInstance(jElement));
			}
		}

		return result;
	}

	/**
	 * Requests the purchase of the specified item. The transaction will not be
	 * confirmed automatically.
	 *
	 * @param context   context
	 * @param productId id of the item to be purchased.
	 * @see #requestPurchase(android.content.Context, String, boolean)
	 */
	public static void requestPurchase(@NotNull Context context, @NotNull String productId) {
		requestPurchase(context, productId, false);
	}

	/**
	 * Requests the purchase of the specified item with optional automatic
	 * confirmation.
	 *
	 * @param context		  context
	 * @param productId		id of the item to be purchased.
	 * @param autoConfirmation if true, the transaction will be confirmed automatically. If
	 *                         false, the transaction will have to be confirmed with a call
	 *                         to {@link #confirmNotifications(android.content.Context, String)}.
	 * @see IBillingObserver#onPurchaseIntentOK(String, android.app.PendingIntent)
	 */
	public static void requestPurchase(@NotNull Context context,
									   @NotNull String productId,
									   boolean autoConfirmation) {
		if (autoConfirmation) {
			synchronized (automaticConfirmations) {
				automaticConfirmations.add(productId);
			}
		}

		BillingService.requestPurchase(context, productId, null);
	}

	/**
	 * Requests to restore all transactions.
	 *
	 * @param context context
	 */
	public static void restoreTransactions(@NotNull Context context) {
		Log.d(BillingController.class.getSimpleName(), "Restoring transactions...");

		final long nonce = Security.generateNonce();
		BillingService.restoreTransactions(context, nonce);
	}

	/**
	 * Sets the configuration instance of the controller.
	 *
	 * @param config configuration instance.
	 */
	public static void setConfiguration(IConfiguration config) {
		configuration = config;
	}

	/**
	 * Sets debug mode.
	 *
	 * @param debug debug
	 */
	public static void setDebug(boolean debug) {
		BillingController.debug = debug;
	}

    public static boolean isDebug() {
        return debug;
    }

    /**
	 * Sets a custom signature validator. If no custom signature validator is
	 * provided,
	 * {@link net.robotmedia.billing.security.DefaultSignatureValidator} will
	 * be used.
	 *
	 * @param validator signature validator instance.
	 */
	@SuppressWarnings({"UnusedDeclaration"})
	public static void setSignatureValidator(ISignatureValidator validator) {
		BillingController.validator = validator;
	}

	@NotNull
	static ISignatureValidator getSignatureValidator() {
		return BillingController.validator != null ? BillingController.validator : new DefaultSignatureValidator(BillingController.configuration);
	}

	/**
	 * Starts the specified purchase intent with the specified activity.
	 *
	 * @param context	   context
	 * @param purchaseIntent purchase intent.
	 * @param intent		 intent
	 */
	public static void startPurchaseIntent(@NotNull Context context,
										   @NotNull PendingIntent purchaseIntent,
										   @Nullable Intent intent) {
		if (Compatibility.isStartIntentSenderSupported(context)) {
			// This is on Android 2.0 and beyond. The in-app buy page activity
			// must be on the activity stack of the application.
			Compatibility.startIntentSender(context, purchaseIntent.getIntentSender(), intent);
		} else {
			// This is on Android version 1.6. The in-app buy page activity must
			// be on its own separate activity stack instead of on the activity
			// stack of the application.
			try {
				purchaseIntent.send(context, 0 /* code */, intent);
			} catch (CanceledException e) {
				Log.e(LOG_TAG, "Error starting purchase intent", e);
			}
		}
	}

	static void storeTransaction(@NotNull Context context, @NotNull Transaction t) {
		final Transaction clone = t.clone();
		ObfuscateUtils.obfuscate(context, clone, getSalt());
		TransactionManager.addTransaction(clone);
	}

	private static boolean verifyNonce(@NotNull JSONObject data) {
		long nonce = data.optLong(JSON_NONCE);
		if (Security.isNonceKnown(nonce)) {
			Security.removeNonce(nonce);
			return true;
		} else {
			return false;
		}
	}

	public static void dropBillingData(@NotNull Context context) {
		Log.d(BillingController.class.getSimpleName(), "Dropping billing database...");
		TransactionManager.dropDatabase(context);
	}

	static void onRequestPurchaseResponse(@NotNull String productId, @NotNull ResponseCode response) {
		BillingObserverRegistry.onRequestPurchaseResponse(productId, response);
	}

	static void onPurchaseIntent(@NotNull String productId, @NotNull PendingIntent purchaseIntent) {
		BillingObserverRegistry.onPurchaseIntent(productId, purchaseIntent);
	}

	static void onPurchaseIntentFailure(@NotNull String productId, @NotNull ResponseCode responseCode) {
		BillingObserverRegistry.onPurchaseIntentFailure(productId, responseCode);
	}

	static void onTransactionsRestored() {
		BillingObserverRegistry.onTransactionsRestored();
	}

	static void onErrorRestoreTransactions(@NotNull ResponseCode response) {
		BillingObserverRegistry.onErrorRestoreTransactions(response);
	}

	public static void registerObserver(@NotNull IBillingObserver billingObserver) {
		BillingObserverRegistry.registerObserver(billingObserver);
	}

	public static void unregisterObserver(@NotNull IBillingObserver billingObserver) {
		BillingObserverRegistry.unregisterObserver(billingObserver);
	}

    @NotNull
    static SecurityService<Transaction, Transaction, byte[]> getTransactionObfuscator() {
        if ( transactionObfuscator == null ) {
            transactionObfuscator = BillingSecurity.getObfuscationSecurityService(AESObfuscator.IV, AESObfuscator.SECURITY_PREFIX);
        }
        return transactionObfuscator;
    }
}
