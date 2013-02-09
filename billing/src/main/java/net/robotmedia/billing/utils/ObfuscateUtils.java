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
import org.solovyev.android.security.ASecurity;
import org.solovyev.common.security.Cipherer;
import org.solovyev.common.security.CiphererException;
import org.solovyev.common.security.SecurityService;
import org.solovyev.common.security.TypedCipherer;
import org.solovyev.common.text.StringDecoder;
import org.solovyev.common.text.StringEncoder;

import javax.crypto.SecretKey;
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

    @NotNull
    public static Cipherer<Transaction, Transaction> getObfuscator() {
        final Cipherer<byte[], byte[]> byteCipherer = ASecurity.newAndroidAesByteCipherer(AESObfuscator.IV);
        final Cipherer<String, String> stringCipherer = TypedCipherer.newInstance(byteCipherer, StringDecoder.getInstance(), StringEncoder.getInstance(), BillingBase64StringDecoder.getInstance(), BillingBase64StringEncoder.getInstance());
        return TransactionObfuscator.newInstance(PrefixStringObfuscator.newInstance(AESObfuscator.header, stringCipherer));
    }

    @NotNull
    public static SecurityService<Transaction, Transaction> getObfuscationSecurityService() {
        return ASecurity.newSecurityService(getObfuscator(), ASecurity.newAndroidAesSecretKeyProvider(), ASecurity.newAndroidSaltGenerator(), ASecurity.newAndroidSha512HashProvider());
    }

    private static class TransactionObfuscator implements Cipherer<Transaction, Transaction> {

        @NotNull
        private Cipherer<String, String> stringCipherer;

        private TransactionObfuscator(@NotNull Cipherer<String, String> stringCipherer) {
            this.stringCipherer = stringCipherer;
        }

        @NotNull
        private static Cipherer<Transaction, Transaction> newInstance(@NotNull Cipherer<String, String> stringCipherer) {
            return new TransactionObfuscator(stringCipherer);
        }

        @NotNull
        @Override
        public Transaction encrypt(@NotNull SecretKey secret, @NotNull Transaction decrypted) throws CiphererException {
            decrypted.orderId = stringCipherer.encrypt(secret, decrypted.orderId);
            decrypted.productId = stringCipherer.encrypt(secret, decrypted.productId);
            decrypted.developerPayload = stringCipherer.encrypt(secret, decrypted.developerPayload);
            return decrypted;
        }

        @NotNull
        @Override
        public Transaction decrypt(@NotNull SecretKey secret, @NotNull Transaction encrypted) throws CiphererException {
            encrypted.orderId = stringCipherer.decrypt(secret, encrypted.orderId);
            encrypted.productId = stringCipherer.decrypt(secret, encrypted.productId);
            encrypted.developerPayload = stringCipherer.decrypt(secret, encrypted.developerPayload);
            return encrypted;
        }
    }

    private static class PrefixStringObfuscator implements Cipherer<String, String> {

        @NotNull
        private final String prefix;

        @NotNull
        private Cipherer<String, String> stringCipherer;

        private PrefixStringObfuscator(@NotNull String prefix, @NotNull Cipherer<String, String> stringCipherer) {
            this.prefix = prefix;
            this.stringCipherer = stringCipherer;
        }

        private static PrefixStringObfuscator newInstance(@NotNull String prefix, @NotNull Cipherer<String, String> stringCipherer) {
            return new PrefixStringObfuscator(prefix, stringCipherer);
        }

        @NotNull
        @Override
        public String encrypt(@NotNull SecretKey secret, @NotNull String decrypted) throws CiphererException {
            return stringCipherer.encrypt(secret, prefix + decrypted);
        }

        @NotNull
        @Override
        public String decrypt(@NotNull SecretKey secret, @NotNull String encrypted) throws CiphererException {
            String decrypted = stringCipherer.decrypt(secret, encrypted);
            return decrypted.substring(prefix.length(), decrypted.length());
        }
    }
}
