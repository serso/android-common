package net.robotmedia.billing.utils;

import android.test.AndroidTestCase;
import junit.framework.Assert;
import net.robotmedia.billing.model.Transaction;
import org.jetbrains.annotations.NotNull;
import org.solovyev.common.Bytes;
import org.solovyev.common.security.SecurityService;
import org.solovyev.common.text.Strings;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Random;

/**
 * User: serso
 * Date: 2/9/13
 * Time: 6:19 PM
 */
public class ObfuscateUtilsTest extends AndroidTestCase {

    private static int TEST_COUNT = 1000;

    @NotNull
    private Random random;

    private byte[] salt = new byte[]{81, -114, 32, -127, -32, -104, -40, -15, -47, 57, -13, -41, -33, 67, -114, 7, -11, 53, 126, 82};

    @Override
    public void setUp() throws Exception {
        super.setUp();
        random = new Random(new Date().getTime());
    }

    public void testObfuscation() throws Exception {
        final SecurityService<Transaction, Transaction> ss = ObfuscateUtils.getObfuscationSecurityService();

        final String password = Security.generatePassword(getContext());
        final String hexSalt = Bytes.toHex(salt);

        final SecretKey sk = ss.getSecretKeyProvider().getSecretKey(password, hexSalt);

        for (int i = 0; i < TEST_COUNT; i++) {
            final Transaction transaction = generateRandomTransaction();

            final Transaction obfuscated1 = transaction.clone();
            ObfuscateUtils.obfuscate(getContext(), obfuscated1, salt);

            Assert.assertFalse(transaction.equals(obfuscated1));

            final Transaction unobfuscated1 = obfuscated1.clone();
            ObfuscateUtils.unobfuscate(getContext(), unobfuscated1, salt);
            Assert.assertEquals(transaction, unobfuscated1);

            Transaction obfuscated2 = transaction.clone();
            obfuscated2 = ss.getCipherer().encrypt(sk, obfuscated2);

            final Transaction unobfuscated3 = obfuscated2.clone();
            ObfuscateUtils.unobfuscate(getContext(), unobfuscated3, salt);
            Assert.assertEquals(transaction, unobfuscated3);

            Transaction unobfuscated2 = obfuscated1.clone();
            unobfuscated2 = ss.getCipherer().decrypt(sk, unobfuscated2);
            Assert.assertEquals(unobfuscated1, unobfuscated2);

        }


    }

    @NotNull
    private Transaction generateRandomTransaction() {
        final Transaction transaction = new Transaction();

        transaction.orderId = Strings.generateRandomString(random.nextInt(100));
        transaction.developerPayload = Strings.generateRandomString(random.nextInt(100));
        transaction.notificationId = Strings.generateRandomString(random.nextInt(100));
        transaction.packageName = Strings.generateRandomString(random.nextInt(100));
        transaction.productId = Strings.generateRandomString(random.nextInt(100));
        transaction.purchaseTime = random.nextLong();
        transaction.purchaseState = Transaction.PurchaseState.valueOf(random.nextInt(3));

        return transaction;
    }
}
