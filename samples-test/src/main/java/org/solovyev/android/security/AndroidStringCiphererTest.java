package org.solovyev.android.security;

import android.test.AndroidTestCase;
import junit.framework.Assert;
import org.solovyev.common.security.*;
import org.solovyev.common.text.Strings;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Random;

public class AndroidStringCiphererTest extends AndroidTestCase {

    public void testEncryptDecrypt() throws Exception {
        final SecurityService<String, String, String> securityService = ASecurity.newAndroidAesStringSecurityService();

        final Cipherer<String, String> cipherer = securityService.getCipherer();
        final SecretKeyProvider secretKeyProvider = securityService.getSecretKeyProvider();
        final SaltGenerator saltGenerator = securityService.getSaltGenerator();
        final HashProvider<String, String> hashProvider = securityService.getHashProvider();

        final Random r = new Random(new Date().getTime());
        for ( int i = 0; i < 100; i++ ) {

            final String expected = Strings.generateRandomString(r.nextInt(1000));

            final SecretKey sk = secretKeyProvider.getSecretKey(Strings.generateRandomString(10), saltGenerator.generateSalt());

            final String encrypted = cipherer.encrypt(sk, expected);
            final String decrypted = cipherer.decrypt(sk, encrypted);

            Assert.assertEquals(expected, decrypted);

            final byte[] hashSalt = saltGenerator.generateSalt();
            final String decryptedHash = hashProvider.getHash(decrypted, hashSalt);
            final String expectedHash = hashProvider.getHash(expected, hashSalt);

            Assert.assertEquals(expectedHash, decryptedHash);
        }
    }
}
