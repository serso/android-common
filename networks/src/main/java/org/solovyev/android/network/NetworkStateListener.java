package org.solovyev.android.network;

import javax.annotation.Nonnull;

/**
 * User: serso
 * Date: 7/26/12
 * Time: 5:26 PM
 */
public interface NetworkStateListener {

	void onNetworkEvent(@Nonnull NetworkData networkData);
}
