package net.robotmedia.billing.security;

import android.content.Context;
import android.provider.Settings;
import net.robotmedia.billing.model.Transaction;
import net.robotmedia.billing.utils.BillingBase64StringDecoder;
import net.robotmedia.billing.utils.BillingBase64StringEncoder;
import net.robotmedia.billing.utils.Installation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.solovyev.android.security.ASecurity;
import org.solovyev.common.security.*;
import org.solovyev.common.text.StringDecoder;
import org.solovyev.common.text.StringEncoder;

/**
 * User: serso
 * Date: 2/10/13
 * Time: 6:52 PM
 */
public final class BillingSecurity {

    private BillingSecurity() {
        throw new AssertionError();
    }

    @NotNull
    private static Cipherer<Transaction, Transaction> newTransactionObfuscator(@NotNull byte[] initialVector, @Nullable String securityPrefix) {
        final Cipherer<byte[], byte[]> byteCipherer = ASecurity.newAndroidAesByteCipherer(initialVector);
        Cipherer<String, String> stringCipherer = TypedCipherer.newInstance(byteCipherer, StringDecoder.getInstance(), StringEncoder.getInstance(), BillingBase64StringDecoder.getInstance(), BillingBase64StringEncoder.getInstance());

        if (securityPrefix != null) {
            stringCipherer = PrefixStringObfuscator.newInstance(securityPrefix, stringCipherer);
        }

        return TransactionObfuscator.newInstance(stringCipherer);
    }

    @NotNull
    public static SecurityService<Transaction, Transaction, byte[]> getObfuscationSecurityService(byte[] initialVector, @Nullable String securityPrefix) {
        return ASecurity.newSecurityService(newTransactionObfuscator(initialVector, securityPrefix), ASecurity.newAndroidAesSecretKeyProvider(), ASecurity.newAndroidSaltGenerator(), getHashProvider());
    }

    @NotNull
    private static HashProvider<Transaction, byte[]> getHashProvider() {
        final HashProvider<byte[], byte[]> hashProvider = ASecurity.newAndroidSha512ByteHashProvider();
        return TypedHashProvider.newByteHashCodeInstance(hashProvider);
    }

    @NotNull
    public static String generatePassword(@NotNull Context context) {
        final String installationId = Installation.id(context);
        final String deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        return installationId + deviceId + context.getPackageName();
    }
}
