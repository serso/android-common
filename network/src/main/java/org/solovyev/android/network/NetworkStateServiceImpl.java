package org.solovyev.android.network;

/**
 * User: serso
 * Date: 7/26/12
 * Time: 5:15 PM
 */


/*
 * Copyright (C) 2006 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.util.Log;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.solovyev.common.listeners.JListeners;
import org.solovyev.common.listeners.Listeners;

public class NetworkStateServiceImpl implements NetworkStateService {

    private static final boolean DEBUG = false;

    @Nullable
    private Context context;

    @NotNull
    private final JListeners<NetworkStateListener> listeners = Listeners.newHardRefListeners();

    @NotNull
    private NetworkData networkData;

    @NotNull
    private BroadcastReceiver receiver;

    private class ConnectivityBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(@NotNull Context context, @NotNull Intent intent) {
            final String action = intent.getAction();

            if (!action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                return;
            }

            final NetworkData localNetworkData = NetworkDataImpl.fromIntent(intent);

            networkData = localNetworkData;

            if (DEBUG) {
                Log.d(TAG, "onReceive(): " + localNetworkData);
            }

            // notify listeners
            for (NetworkStateListener localListener : listeners.getListeners()) {
                localListener.onNetworkEvent(localNetworkData);
            }
        }
    }

    /**
     * Create a new NetworkConnectivityListener.
     */
    public NetworkStateServiceImpl() {
        networkData = NetworkDataImpl.newUnknownNetworkData();
        receiver = new ConnectivityBroadcastReceiver();
    }

    @Override
    public synchronized void startListening(@NotNull Context context) {
        this.context = context.getApplicationContext();

        final IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        context.registerReceiver(receiver, filter);
    }

    @Override
    public boolean addListener(@NotNull NetworkStateListener listener) {
        return listeners.addListener(listener);
    }

    @Override
    public boolean removeListener(@NotNull NetworkStateListener listener) {
        return listeners.removeListener(listener);
    }

    @Override
    public synchronized void stopListening() {
        if (context != null) {
            context.unregisterReceiver(receiver);
        }
        context = null;
        networkData = NetworkDataImpl.newUnknownNetworkData();
    }

    @Override
    @NotNull
    public NetworkData getNetworkData() {
        return networkData;
    }
}