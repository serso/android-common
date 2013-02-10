package net.robotmedia.billing.security;

import org.jetbrains.annotations.NotNull;
import org.solovyev.common.security.Cipherer;
import org.solovyev.common.security.CiphererException;

import javax.crypto.SecretKey;

/**
* User: serso
* Date: 2/10/13
* Time: 6:54 PM
*/
class PrefixStringObfuscator implements Cipherer<String, String> {

    @NotNull
    private final String securityPrefix;

    @NotNull
    private Cipherer<String, String> stringCipherer;

    private PrefixStringObfuscator(@NotNull String securityPrefix, @NotNull Cipherer<String, String> stringCipherer) {
        this.securityPrefix = securityPrefix;
        this.stringCipherer = stringCipherer;
    }

    @NotNull
    static Cipherer<String, String> newInstance(@NotNull String prefix, @NotNull Cipherer<String, String> stringCipherer) {
        return new PrefixStringObfuscator(prefix, stringCipherer);
    }

    @NotNull
    @Override
    public String encrypt(@NotNull SecretKey secret, @NotNull String decrypted) throws CiphererException {
        return stringCipherer.encrypt(secret, securityPrefix + decrypted);
    }

    @NotNull
    @Override
    public String decrypt(@NotNull SecretKey secret, @NotNull String encrypted) throws CiphererException {
        String decrypted = stringCipherer.decrypt(secret, encrypted);

        // Check for presence of header. This serves as a final integrity check, for cases
        // where the block size is correct during decryption.
        final int securityIndex = decrypted.indexOf(securityPrefix);
        if (securityIndex != 0) {
            throw new CiphererException("Security prefix not found (invalid data or key), prefix:" + securityPrefix);
        }

        return decrypted.substring(securityPrefix.length(), decrypted.length());
    }
}
