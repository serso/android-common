package net.robotmedia.billing.security;

import net.robotmedia.billing.model.Transaction;
import javax.annotation.Nonnull;
import org.solovyev.common.security.Cipherer;
import org.solovyev.common.security.CiphererException;

import javax.crypto.SecretKey;

/**
* User: serso
* Date: 2/10/13
* Time: 6:55 PM
*/
class TransactionObfuscator implements Cipherer<Transaction, Transaction> {

    @Nonnull
    private Cipherer<String, String> stringCipherer;

    private TransactionObfuscator(@Nonnull Cipherer<String, String> stringCipherer) {
        this.stringCipherer = stringCipherer;
    }

    @Nonnull
    static Cipherer<Transaction, Transaction> newInstance(@Nonnull Cipherer<String, String> stringCipherer) {
        return new TransactionObfuscator(stringCipherer);
    }

    @Nonnull
    @Override
    public Transaction encrypt(@Nonnull SecretKey secret, @Nonnull Transaction decrypted) throws CiphererException {
        decrypted.orderId = stringCipherer.encrypt(secret, decrypted.orderId);
        decrypted.productId = stringCipherer.encrypt(secret, decrypted.productId);
        decrypted.developerPayload = stringCipherer.encrypt(secret, decrypted.developerPayload);
        return decrypted;
    }

    @Nonnull
    @Override
    public Transaction decrypt(@Nonnull SecretKey secret, @Nonnull Transaction encrypted) throws CiphererException {
        encrypted.orderId = stringCipherer.decrypt(secret, encrypted.orderId);
        encrypted.productId = stringCipherer.decrypt(secret, encrypted.productId);
        encrypted.developerPayload = stringCipherer.decrypt(secret, encrypted.developerPayload);
        return encrypted;
    }
}
