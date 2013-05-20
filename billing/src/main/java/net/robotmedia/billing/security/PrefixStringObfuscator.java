package net.robotmedia.billing.security;

import org.solovyev.common.security.Cipherer;
import org.solovyev.common.security.CiphererException;

import javax.annotation.Nonnull;
import javax.crypto.SecretKey;

/**
 * User: serso
 * Date: 2/10/13
 * Time: 6:54 PM
 */
class PrefixStringObfuscator implements Cipherer<String, String> {

	@Nonnull
	private final String securityPrefix;

	@Nonnull
	private Cipherer<String, String> stringCipherer;

	private PrefixStringObfuscator(@Nonnull String securityPrefix, @Nonnull Cipherer<String, String> stringCipherer) {
		this.securityPrefix = securityPrefix;
		this.stringCipherer = stringCipherer;
	}

	@Nonnull
	static Cipherer<String, String> newInstance(@Nonnull String prefix, @Nonnull Cipherer<String, String> stringCipherer) {
		return new PrefixStringObfuscator(prefix, stringCipherer);
	}

	@Nonnull
	@Override
	public String encrypt(@Nonnull SecretKey secret, @Nonnull String decrypted) throws CiphererException {
		return stringCipherer.encrypt(secret, securityPrefix + decrypted);
	}

	@Nonnull
	@Override
	public String decrypt(@Nonnull SecretKey secret, @Nonnull String encrypted) throws CiphererException {
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
