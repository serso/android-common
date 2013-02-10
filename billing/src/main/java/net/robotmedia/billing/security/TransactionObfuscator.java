package net.robotmedia.billing.security;

import net.robotmedia.billing.model.Transaction;
import org.jetbrains.annotations.NotNull;
import org.solovyev.common.security.Cipherer;
import org.solovyev.common.security.CiphererException;

import javax.crypto.SecretKey;

/**
* User: serso
* Date: 2/10/13
* Time: 6:55 PM
*/
class TransactionObfuscator implements Cipherer<Transaction, Transaction> {

    @NotNull
    private Cipherer<String, String> stringCipherer;

    private TransactionObfuscator(@NotNull Cipherer<String, String> stringCipherer) {
        this.stringCipherer = stringCipherer;
    }

    @NotNull
    static Cipherer<Transaction, Transaction> newInstance(@NotNull Cipherer<String, String> stringCipherer) {
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
