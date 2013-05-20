package org.solovyev.android.security;

import org.solovyev.android.security.base64.ABase64StringDecoder;
import org.solovyev.android.security.base64.ABase64StringEncoder;
import org.solovyev.common.security.*;
import org.solovyev.common.text.StringDecoder;
import org.solovyev.common.text.StringEncoder;

import javax.annotation.Nonnull;

public final class Security extends org.solovyev.common.security.Security {

	private static final String PROVIDER = "BC";
	private static final String CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";

	private static final int PBE_ITERATION_COUNT = 1024;
	private static final String PBE_ALGORITHM = "PBEWITHSHAAND256BITAES-CBC-BC";
	private static final int PBE_KEY_LENGTH = 256;

	private static final String HASH_ALGORITHM = "SHA-512";

	private static final int SALT_LENGTH = 20;

	// Initial vector
	private static final int IV_LENGTH = 16;
	private static final String IV_RANDOM_ALGORITHM = "SHA1PRNG";

	private Security() {
		throw new AssertionError();
	}

	@Nonnull
	public static Cipherer<byte[], byte[]> newAndroidAesByteCipherer() {
		return org.solovyev.common.security.Security.newCipherer(CIPHER_ALGORITHM, PROVIDER, InitialVectorDef.newRandom(IV_RANDOM_ALGORITHM, IV_LENGTH));
	}

	@Nonnull
	public static Cipherer<byte[], byte[]> newAndroidAesByteCipherer(final byte[] initialVector) {
		return org.solovyev.common.security.Security.newCipherer(CIPHER_ALGORITHM, PROVIDER, InitialVectorDef.newPredefined(initialVector));
	}

	@Nonnull
	public static SecretKeyProvider newAndroidAesSecretKeyProvider() {
		return org.solovyev.common.security.Security.newPbeSecretKeyProvider(PBE_ITERATION_COUNT, PBE_ALGORITHM, CIPHERER_ALGORITHM_AES, PROVIDER, PBE_KEY_LENGTH, SALT_LENGTH);
	}

	@Nonnull
	public static HashProvider<byte[], byte[]> newAndroidSha512ByteHashProvider() {
		return org.solovyev.common.security.Security.newHashProvider(HASH_ALGORITHM, PROVIDER);
	}

	@Nonnull
	public static HashProvider<String, String> newAndroidSha512StringHashProvider() {
		return TypedHashProvider.newInstance(newAndroidSha512ByteHashProvider(), StringDecoder.getInstance(), ABase64StringEncoder.getInstance());
	}

	@Nonnull
	public static SaltGenerator newAndroidSaltGenerator() {
		return org.solovyev.common.security.Security.newSaltGenerator(IV_RANDOM_ALGORITHM, SALT_LENGTH);
	}

	@Nonnull
	public static Cipherer<String, String> newAndroidAesStringCipherer() {
		return TypedCipherer.newInstance(newAndroidAesByteCipherer(), StringDecoder.getInstance(), StringEncoder.getInstance(), ABase64StringDecoder.getInstance(), ABase64StringEncoder.getInstance());
	}

	@Nonnull
	public static Cipherer<String, String> newAndroidAesStringCipherer(final byte[] initialVector) {
		return TypedCipherer.newInstance(newAndroidAesByteCipherer(initialVector), StringDecoder.getInstance(), StringEncoder.getInstance(), ABase64StringDecoder.getInstance(), ABase64StringEncoder.getInstance());
	}

	@Nonnull
	public static SecurityService<byte[], byte[], byte[]> newAndroidAesByteSecurityService() {
		return newSecurityService(newAndroidAesByteCipherer(), newAndroidAesSecretKeyProvider(), newAndroidSaltGenerator(), newAndroidSha512ByteHashProvider());
	}

	@Nonnull
	public static SecurityService<byte[], byte[], byte[]> newAndroidAesByteSecurityService(final byte[] initialVector) {
		return newSecurityService(newAndroidAesByteCipherer(initialVector), newAndroidAesSecretKeyProvider(), newAndroidSaltGenerator(), newAndroidSha512ByteHashProvider());
	}

	@Nonnull
	public static SecurityService<String, String, String> newAndroidAesStringSecurityService() {
		return newSecurityService(newAndroidAesStringCipherer(), newAndroidAesSecretKeyProvider(), newAndroidSaltGenerator(), newAndroidSha512StringHashProvider());
	}

	@Nonnull
	public static SecurityService<String, String, String> newAndroidStringSecurityService(@Nonnull SecurityService<byte[], byte[], byte[]> securityService) {
		return SecurityServiceConverter.wrap(securityService, StringDecoder.getInstance(), StringEncoder.getInstance(), ABase64StringDecoder.getInstance(), ABase64StringEncoder.getInstance());
	}
}
