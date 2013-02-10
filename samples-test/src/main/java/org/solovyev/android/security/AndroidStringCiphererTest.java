package org.solovyev.android.security;

import android.test.AndroidTestCase;
import junit.framework.Assert;
import org.solovyev.common.security.*;
import org.solovyev.common.text.Strings;

import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Date;
import java.util.Random;

public class AndroidStringCiphererTest extends AndroidTestCase {

    public void testEncryptDecrypt() throws Exception {
        final SecurityService<String, String, String> securityService = ASecurity.newAndroidAesStringSecurityService();

        doRandomCiphererTest(securityService.getSecretKeyProvider(), securityService.getCipherer(), securityService.getSaltGenerator());
    }

    protected static <E> void doRandomCiphererTest(SecretKeyProvider secretKeyProvider, Cipherer<E, String> cipherer, SaltGenerator saltGenerator) throws CiphererException, NoSuchProviderException, NoSuchAlgorithmException {
        final Random r = new Random(new Date().getTime());
        for ( int i = 0; i < 100; i++ ) {

            final String expected = Strings.generateRandomString(r.nextInt(1000));

            final SecretKey sk = secretKeyProvider.getSecretKey(Strings.generateRandomString(10), saltGenerator.generateSalt());

            final E encrypted = cipherer.encrypt(sk, expected);
            final String decrypted = cipherer.decrypt(sk, encrypted);

            Assert.assertEquals(expected, decrypted);
        }
    }
}
