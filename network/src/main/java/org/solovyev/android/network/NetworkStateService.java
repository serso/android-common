package org.solovyev.android.network;

import android.content.Context;
import org.jetbrains.annotations.NotNull;

public interface NetworkStateService {

    static final String TAG = "NetworkStateService";

    /**
     * This method starts listening for network connectivity state changes.
     *
     * @param context context
     */
    void startListening(@NotNull Context context);

    /**
     * This method stops this class from listening for network changes.
     */
    void stopListening();

    boolean addListener(@NotNull NetworkStateListener listener);

    boolean removeListener(@NotNull NetworkStateListener listener);

    @NotNull
    NetworkData getNetworkData();
}
