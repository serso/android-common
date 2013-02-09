package org.solovyev.android.security;

import android.test.AndroidTestCase;
import junit.framework.Assert;
import org.solovyev.common.security.Cipherer;
import org.solovyev.common.security.CiphererException;
import org.solovyev.common.security.SecretKeyProvider;
import org.solovyev.common.security.SecurityService;
import org.solovyev.common.text.Strings;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Random;

public class AndroidStringCiphererTest extends AndroidTestCase {

    public void testEncryptDecrypt() throws Exception {
        final SecurityService<String, String> securityService = ASecurity.newAndroidAesStringSecurityService();

        doRandomCiphererTest(securityService.getSecretKeyProvider(), securityService.getCipherer());
    }

    protected static <E> void doRandomCiphererTest(SecretKeyProvider secretKeyProvider, Cipherer<E, String> cipherer) throws CiphererException {
        final Random r = new Random(new Date().getTime());
        for ( int i = 0; i < 100; i++ ) {

            final String expected = Strings.generateRandomString(r.nextInt(1000));

            final SecretKey sk = secretKeyProvider.getSecretKey(Strings.generateRandomString(10), Strings.generateRandomString(10));

            final E encrypted = cipherer.encrypt(sk, expected);
            final String decrypted = cipherer.decrypt(sk, encrypted);

            Assert.assertEquals(expected, decrypted);
        }
    }
}
