package net.robotmedia.billing.helper;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import net.robotmedia.billing.IBillingObserver;
import net.robotmedia.billing.ResponseCode;
import net.robotmedia.billing.model.Transaction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * User: serso
 * Date: 5/14/12
 * Time: 4:16 PM
 */
public class DefaultBillingObserver extends AbstractBillingObserver {

    @Nullable
    private final IBillingObserver nestedBillingObserver;

    public DefaultBillingObserver(@NotNull Context context, @Nullable IBillingObserver nestedBillingObserver) {
        super(context);
        this.nestedBillingObserver = nestedBillingObserver;
    }

    @Override
    public void onCheckBillingSupportedResponse(boolean supported) {
        if (nestedBillingObserver != null) {
            nestedBillingObserver.onCheckBillingSupportedResponse(supported);
        }
    }

    @Override
    public void onPurchaseIntentOK(@NotNull String productId, @NotNull PendingIntent purchaseIntent) {
        super.onPurchaseIntentOK(productId, purchaseIntent);
        if (nestedBillingObserver != null) {
            nestedBillingObserver.onPurchaseIntentOK(productId, purchaseIntent);
        }
    }

    @Override
    public void onPurchaseIntentFailure(@NotNull String productId, @NotNull ResponseCode responseCode) {
        if (nestedBillingObserver != null) {
            nestedBillingObserver.onPurchaseIntentFailure(productId, responseCode);
        }
    }

    @Override
    public void onPurchaseStateChanged(@NotNull String productId, @NotNull Transaction.PurchaseState state) {
        if (nestedBillingObserver != null) {
            nestedBillingObserver.onPurchaseStateChanged(productId, state);
        }
    }

    @Override
    public void onRequestPurchaseResponse(@NotNull String productId, @NotNull ResponseCode response) {
        if (nestedBillingObserver != null) {
            nestedBillingObserver.onRequestPurchaseResponse(productId, response);
        }
    }

    @Override
    public void onTransactionsRestored() {
        super.onTransactionsRestored();
        if (nestedBillingObserver != null) {
            nestedBillingObserver.onTransactionsRestored();
        }
    }

    @Override
    public void onErrorRestoreTransactions(@NotNull ResponseCode responseCode) {
        super.onErrorRestoreTransactions(responseCode);
        if (nestedBillingObserver != null) {
            nestedBillingObserver.onErrorRestoreTransactions(responseCode);
        }
    }
}
