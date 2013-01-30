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
