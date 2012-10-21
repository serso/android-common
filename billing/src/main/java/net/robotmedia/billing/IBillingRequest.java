package net.robotmedia.billing;

import android.os.RemoteException;
import com.android.vending.billing.IMarketBillingService;
import org.jetbrains.annotations.NotNull;

/**
 * User: serso
 * Date: 1/17/12
 * Time: 1:06 PM
 */
interface IBillingRequest {

	long run(@NotNull IMarketBillingService service) throws RemoteException;

	@NotNull
	BillingRequestType getRequestType();

	boolean hasNonce();

	boolean isSuccess();

	long getNonce();

	void onResponseCode(@NotNull ResponseCode response);

	int getStartId();
}
