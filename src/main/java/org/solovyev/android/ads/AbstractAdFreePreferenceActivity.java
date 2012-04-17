package org.solovyev.android.ads;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;
import net.robotmedia.billing.BillingController;
import net.robotmedia.billing.IBillingObserver;
import net.robotmedia.billing.ResponseCode;
import net.robotmedia.billing.helper.AbstractBillingObserver;
import net.robotmedia.billing.model.Transaction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.solovyev.android.AndroidUtils;
import org.solovyev.android.R;

/**
 * User: serso
 * Date: 4/18/12
 * Time: 12:14 AM
 */
public abstract class AbstractAdFreePreferenceActivity extends PreferenceActivity implements IBillingObserver {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(getPreferencesResourceId());

        final Preference adFreePreference = findPreference(getAdFreePreferenceId());
        adFreePreference.setEnabled(false);

        // observer must be set before net.robotmedia.billing.BillingController.checkBillingSupported()
        BillingController.registerObserver(this);

        BillingController.checkBillingSupported(AbstractAdFreePreferenceActivity.this);

        final String clearBillingDataPreferenceId = getClearBillingDataPreferenceId();
        if (clearBillingDataPreferenceId != null) {
            final Preference clearBillingInfoPreference = findPreference(clearBillingDataPreferenceId);
            if (clearBillingInfoPreference != null) {
                clearBillingInfoPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {

                        Toast.makeText(AbstractAdFreePreferenceActivity.this, R.string.c_billing_clearing, Toast.LENGTH_SHORT).show();

                        removeBillingInformation(AbstractAdFreePreferenceActivity.this, PreferenceManager.getDefaultSharedPreferences(AbstractAdFreePreferenceActivity.this));

                        return true;
                    }
                });
            }
        }
    }

    protected abstract int getPreferencesResourceId();

    @Nullable
    protected abstract String getClearBillingDataPreferenceId();

    @NotNull
    protected abstract String getAdFreeProductId();

    @NotNull
    protected abstract String getAdFreePreferenceId();

    public static void removeBillingInformation(@NotNull Context context, @NotNull SharedPreferences preferences) {
        final SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(AbstractBillingObserver.KEY_TRANSACTIONS_RESTORED, false);
        editor.commit();

        BillingController.dropBillingData(context);
    }

    private void setAdFreeAction() {
        final Preference adFreePreference = findPreference(getAdFreePreferenceId());

        if (!AdsController.getInstance().isAdFree(this)) {
            Log.d(AbstractAdFreePreferenceActivity.class.getName(), "Ad free is not purchased - enable preference!");

            adFreePreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference preference) {

                    // check billing availability
                    if (BillingController.checkBillingSupported(AbstractAdFreePreferenceActivity.this) != BillingController.BillingStatus.SUPPORTED) {
                        Log.d(AbstractAdFreePreferenceActivity.class.getName(), "Billing is not supported - warn user!");
                        // warn about not supported billing
                        new AlertDialog.Builder(AbstractAdFreePreferenceActivity.this).setTitle(R.string.c_error).setMessage(R.string.c_billing_error).create().show();
                    } else {
                        Log.d(AbstractAdFreePreferenceActivity.class.getName(), "Billing is supported - continue!");
                        if (!AdsController.getInstance().isAdFree(AbstractAdFreePreferenceActivity.this)) {
                            Log.d(AbstractAdFreePreferenceActivity.class.getName(), "Item not purchased - try to purchase!");

                            // not purchased => purchasing
                            Toast.makeText(AbstractAdFreePreferenceActivity.this, R.string.c_billing_purchasing, Toast.LENGTH_SHORT).show();

                            // show purchase window for user
                            BillingController.requestPurchase(AbstractAdFreePreferenceActivity.this, getAdFreeProductId(), true);
                        } else {
                            // disable preference
                            adFreePreference.setEnabled(false);
                            // and show message to user
                            Toast.makeText(AbstractAdFreePreferenceActivity.this, R.string.c_billing_already_purchased, Toast.LENGTH_SHORT).show();
                        }
                    }

                    return true;
                }
            });
            adFreePreference.setEnabled(true);
        } else {
            Log.d(AbstractAdFreePreferenceActivity.class.getName(), "Ad free is not purchased - disable preference!");
            adFreePreference.setEnabled(false);
        }
    }

    @Override
    protected void onDestroy() {
        BillingController.unregisterObserver(this);
        super.onDestroy();
    }

    @Override
    public void onCheckBillingSupportedResponse(boolean supported) {
        if (supported) {
            setAdFreeAction();
        } else {
            final Preference adFreePreference = findPreference(getAdFreePreferenceId());
            adFreePreference.setEnabled(false);
            Log.d(AbstractAdFreePreferenceActivity.class.getName(), "Billing is not supported!");
        }
    }

    @Override
    public void onPurchaseIntentOK(@NotNull String productId, @NotNull PendingIntent purchaseIntent) {
        // do nothing
    }

    @Override
    public void onPurchaseIntentFailure(@NotNull String productId, @NotNull ResponseCode responseCode) {
        // do nothing
    }

    @Override
    public void onPurchaseStateChanged(@NotNull String itemId, @NotNull Transaction.PurchaseState state) {
        if (getAdFreeProductId().equals(itemId)) {
            final Preference adFreePreference = findPreference(getAdFreePreferenceId());
            if (adFreePreference != null) {
                switch (state) {
                    case PURCHASED:
                        adFreePreference.setEnabled(false);
                        // restart activity to disable ads
                        AndroidUtils.restartActivity(this);
                        break;
                    case CANCELLED:
                        adFreePreference.setEnabled(true);
                        break;
                    case REFUNDED:
                        adFreePreference.setEnabled(true);
                        break;
                }
            } else {
            }
        }
    }

    @Override
    public void onRequestPurchaseResponse(@NotNull String itemId, @NotNull ResponseCode response) {
        // do nothing
    }

    @Override
    public void onTransactionsRestored() {
        // do nothing
    }

    @Override
    public void onErrorRestoreTransactions(@NotNull ResponseCode responseCode) {
        // do nothing
    }
}

