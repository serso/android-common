package net.robotmedia.billing;

import org.jetbrains.annotations.NotNull;

/**
 * User: serso
 * Date: 1/17/12
 * Time: 12:39 PM
 */
interface IBillingService {

	void runRequestOrQueue(@NotNull IBillingRequest request);

	@NotNull
	String getPackageName();

}
