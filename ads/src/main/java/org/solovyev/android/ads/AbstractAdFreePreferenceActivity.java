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

package org.solovyev.android.ads;

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
import net.robotmedia.billing.helper.DefaultBillingObserver;
import net.robotmedia.billing.model.Transaction;
import org.solovyev.android.Activities;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * User: serso
 * Date: 4/18/12
 * Time: 12:14 AM
 */
public abstract class AbstractAdFreePreferenceActivity extends PreferenceActivity implements IBillingObserver {

    @Nonnull
    private final IBillingObserver defaultBillingObserver = new DefaultBillingObserver(this, this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(getPreferencesResourceId());

        final Preference adFreePreference = findPreference(getAdFreePreferenceId());
        adFreePreference.setEnabled(false);

        // observer must be set before net.robotmedia.billing.BillingController.checkBillingSupported()
        BillingController.registerObserver(defaultBillingObserver);

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

    @Nonnull
    protected abstract String getAdFreeProductId();

    @Nonnull
    protected abstract String getAdFreePreferenceId();

    public static void removeBillingInformation(@Nonnull Context context, @Nonnull SharedPreferences preferences) {
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
                        Toast.makeText(AbstractAdFreePreferenceActivity.this, R.string.c_billing_error, Toast.LENGTH_LONG).show();
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
        BillingController.unregisterObserver(defaultBillingObserver);
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
    public void onPurchaseIntentOK(@Nonnull String productId, @Nonnull PendingIntent purchaseIntent) {
        // do nothing
    }

    @Override
    public void onPurchaseIntentFailure(@Nonnull String productId, @Nonnull ResponseCode responseCode) {
        // do nothing
    }

    @Override
    public void onPurchaseStateChanged(@Nonnull String itemId, @Nonnull Transaction.PurchaseState state) {
        if (getAdFreeProductId().equals(itemId)) {
            final Preference adFreePreference = findPreference(getAdFreePreferenceId());
            if (adFreePreference != null) {
                switch (state) {
                    case PURCHASED:
                        adFreePreference.setEnabled(false);
                        // restart activity to disable ads
                        Activities.restartActivity(this);
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
    public void onRequestPurchaseResponse(@Nonnull String itemId, @Nonnull ResponseCode response) {
        // do nothing
    }

    @Override
    public void onTransactionsRestored() {
        // do nothing
    }

    @Override
    public void onErrorRestoreTransactions(@Nonnull ResponseCode responseCode) {
        // do nothing
    }
}

