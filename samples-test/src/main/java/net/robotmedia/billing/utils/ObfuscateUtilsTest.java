package net.robotmedia.billing.utils;

import android.test.AndroidTestCase;
import junit.framework.Assert;
import net.robotmedia.billing.model.Transaction;
import net.robotmedia.billing.security.BillingSecurity;
import org.solovyev.common.security.SecurityService;
import org.solovyev.common.text.Strings;

import javax.annotation.Nonnull;
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

	@Nonnull
	private Random random;

	private byte[] salt = new byte[]{81, -114, 32, -127, -32, -104, -40, -15, -47, 57, -13, -41, -33, 67, -114, 7, -11, 53, 126, 82};

	@Override
	public void setUp() throws Exception {
		super.setUp();
		random = new Random(new Date().getTime());
	}

	public void testObfuscation() throws Exception {
		final SecurityService<Transaction, Transaction, byte[]> ss = BillingSecurity.getObfuscationSecurityService(AESObfuscator.IV, AESObfuscator.SECURITY_PREFIX);

		final String password = BillingSecurity.generatePassword(getContext());

		final SecretKey sk = ss.getSecretKeyProvider().getSecretKey(password, salt);

		for (int i = 0; i < TEST_COUNT; i++) {
			final Transaction transaction = generateRandomTransaction();

			// old obfuscation
			final Transaction obfuscated1 = transaction.clone();
			ObfuscateUtils.obfuscate(getContext(), obfuscated1, salt);
			Assert.assertFalse(transaction.equals(obfuscated1));

			// old deobfuscation
			final Transaction deobfuscated1 = obfuscated1.clone();
			ObfuscateUtils.unobfuscate(getContext(), deobfuscated1, salt);
			Assert.assertEquals(transaction, deobfuscated1);

			// new deobfuscation on old obfuscation
			Transaction deobfuscated2 = obfuscated1.clone();
			deobfuscated2 = ss.getCipherer().decrypt(sk, deobfuscated2);
			Assert.assertEquals(transaction, deobfuscated2);

			// new obfuscation
			Transaction obfuscated2 = transaction.clone();
			obfuscated2 = ss.getCipherer().encrypt(sk, obfuscated2);
			Assert.assertFalse(transaction.equals(obfuscated2));
			Assert.assertEquals(obfuscated1, obfuscated2);

			// new deobfuscation on new obfuscation
			Transaction deobfuscated3 = obfuscated2.clone();
			deobfuscated3 = ss.getCipherer().decrypt(sk, deobfuscated3);
			Assert.assertEquals(transaction, deobfuscated3);
		}


	}

	@Nonnull
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
