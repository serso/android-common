package org.solovyev.android.security;

import javax.annotation.Nonnull;
import org.solovyev.android.security.base64.ABase64StringDecoder;
import org.solovyev.android.security.base64.ABase64StringEncoder;
import org.solovyev.common.security.*;
import org.solovyev.common.text.StringDecoder;
import org.solovyev.common.text.StringEncoder;

public final class ASecurity extends Security {

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

    private ASecurity() {
        throw new AssertionError();
    }

    @Nonnull
    public static Cipherer<byte[], byte[]> newAndroidAesByteCipherer() {
        return Security.newCipherer(CIPHER_ALGORITHM, PROVIDER, InitialVectorDef.newRandom(IV_RANDOM_ALGORITHM, IV_LENGTH));
    }

    @Nonnull
    public static Cipherer<byte[], byte[]> newAndroidAesByteCipherer(final byte[] initialVector) {
        return Security.newCipherer(CIPHER_ALGORITHM, PROVIDER, InitialVectorDef.newPredefined(initialVector));
    }

    @Nonnull
    public static SecretKeyProvider newAndroidAesSecretKeyProvider() {
        return Security.newPbeSecretKeyProvider(PBE_ITERATION_COUNT, PBE_ALGORITHM, CIPHERER_ALGORITHM_AES, PROVIDER, PBE_KEY_LENGTH, SALT_LENGTH);
    }

    @Nonnull
    public static HashProvider<byte[], byte[]> newAndroidSha512ByteHashProvider() {
        return Security.newHashProvider(HASH_ALGORITHM, PROVIDER);
    }

    @Nonnull
    public static HashProvider<String, String> newAndroidSha512StringHashProvider() {
        return TypedHashProvider.newInstance(newAndroidSha512ByteHashProvider(), StringDecoder.getInstance(), ABase64StringEncoder.getInstance());
    }

    @Nonnull
    public static SaltGenerator newAndroidSaltGenerator() {
        return Security.newSaltGenerator(IV_RANDOM_ALGORITHM, SALT_LENGTH);
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
}
