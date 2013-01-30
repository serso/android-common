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

package net.robotmedia.billing.utils;

import android.content.Context;
import net.robotmedia.billing.model.Transaction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * User: serso
 * Date: 1/21/12
 * Time: 5:28 PM
 */
public final class ObfuscateUtils {

	private ObfuscateUtils() {
		throw new AssertionError();
	}

	public static void unobfuscate(@NotNull Context context, @NotNull List<Transaction> transactions, @Nullable byte[] salt) {
		for (Transaction p : transactions) {
			unobfuscate(context, p, salt);
		}
	}

	/**
	 * Obfuscates the specified purchase. Only the order id, product id and
	 * developer payload are obfuscated.
	 *
	 * @param context context
	 * @param t	   purchase to be obfuscated.
	 * @param salt	salt
	 * @see #unobfuscate(android.content.Context, net.robotmedia.billing.model.Transaction, byte[])
	 */
	public static void obfuscate(@NotNull Context context, @NotNull Transaction t, @Nullable byte[] salt) {
		if (salt == null) {
			return;
		}
		t.orderId = Security.obfuscate(context, salt, t.orderId);
		t.productId = Security.obfuscate(context, salt, t.productId);
		t.developerPayload = Security.obfuscate(context, salt, t.developerPayload);
	}

	/**
	 * Unobfuscate the specified purchase.
	 *
	 * @param context context
	 * @param t	   purchase to unobfuscate.
	 * @param salt	salt
	 * @see #obfuscate(android.content.Context, net.robotmedia.billing.model.Transaction, byte[])
	 */
	public static void unobfuscate(@NotNull Context context, @NotNull Transaction t, @Nullable byte[] salt) {
		t.orderId = Security.unobfuscate(context, salt, t.orderId);
		t.productId = Security.unobfuscate(context, salt, t.productId);
		t.developerPayload = Security.unobfuscate(context, salt, t.developerPayload);
	}
}
