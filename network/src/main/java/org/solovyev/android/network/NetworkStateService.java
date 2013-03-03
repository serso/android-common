package org.solovyev.android.network;

import android.content.Context;
import javax.annotation.Nonnull;

public interface NetworkStateService {

    static final String TAG = "NetworkStateService";

    /**
     * This method starts listening for network connectivity state changes.
     *
     * @param context context
     */
    void startListening(@Nonnull Context context);

    /**
     * This method stops this class from listening for network changes.
     */
    void stopListening();

    boolean addListener(@Nonnull NetworkStateListener listener);

    boolean removeListener(@Nonnull NetworkStateListener listener);

    @Nonnull
    NetworkData getNetworkData();
}
