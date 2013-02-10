package org.solovyev.android.security;

import org.jetbrains.annotations.NotNull;
import org.solovyev.android.security.base64.ABase64StringDecoder;
import org.solovyev.android.security.base64.ABase64StringEncoder;
import org.solovyev.common.security.*;
import org.solovyev.common.text.StringDecoder;
import org.solovyev.common.text.StringEncoder;

public final class ASecurity extends Security {

    private static final String PROVIDER = "BC";
    private static final String CIPHERER_ALGORITHM = "AES";
    private static final String CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";

    private static final int PBE_ITERATION_COUNT = 1024;
    private static final String PBE_ALGORITHM = "PBEWITHSHAAND256BITAES-CBC-BC";
    private static final int PBE_KEY_LENGTH = 256;
    private static final int PBE_SALT_LENGTH = 20;
    private static final String HASH_ALGORITHM = "SHA-512";
    private static final int IV_LENGTH = 16;
    private static final int SALT_LENGTH = 20;
    private static final String RANDOM_ALGORITHM = "SHA1PRNG";

    private ASecurity() {
        throw new AssertionError();
    }

    @NotNull
    public static Cipherer<byte[], byte[]> newAndroidAesByteCipherer() {
        return Security.newCipherer(CIPHER_ALGORITHM, PROVIDER, InitialVectorDef.newSha1PrngRandom(IV_LENGTH));
    }

    @NotNull
    public static Cipherer<byte[], byte[]> newAndroidAesByteCipherer(final byte[] initialVector) {
        return Security.newCipherer(CIPHER_ALGORITHM, PROVIDER, InitialVectorDef.newPredefined(initialVector));
    }

    @NotNull
    public static SecretKeyProvider newAndroidAesSecretKeyProvider() {
        return Security.newPbeSecretKeyProvider(PBE_ITERATION_COUNT, PBE_ALGORITHM, CIPHERER_ALGORITHM, PROVIDER, PBE_KEY_LENGTH, PBE_SALT_LENGTH);
    }

    @NotNull
    public static HashProvider<byte[], byte[]> newAndroidSha512ByteHashProvider() {
        return Security.newHashProvider(HASH_ALGORITHM, PROVIDER);
    }

    @NotNull
    public static HashProvider<String, String> newAndroidSha512StringHashProvider() {
        return TypedHashProvider.newInstance(newAndroidSha512ByteHashProvider(), StringDecoder.getInstance(), ABase64StringEncoder.getInstance());
    }

    @NotNull
    public static SaltGenerator newAndroidSaltGenerator() {
        return Security.newSaltGenerator(RANDOM_ALGORITHM, SALT_LENGTH);
    }

    @NotNull
    public static Cipherer<String, String> newAndroidAesStringCipherer() {
        return TypedCipherer.newInstance(newAndroidAesByteCipherer(), StringDecoder.getInstance(), StringEncoder.getInstance(), ABase64StringDecoder.getInstance(), ABase64StringEncoder.getInstance());
    }

    @NotNull
    public static Cipherer<String, String> newAndroidAesStringCipherer(final byte[] initialVector) {
        return TypedCipherer.newInstance(newAndroidAesByteCipherer(initialVector), StringDecoder.getInstance(), StringEncoder.getInstance(), ABase64StringDecoder.getInstance(), ABase64StringEncoder.getInstance());
    }

    @NotNull
    public static SecurityService<byte[], byte[], byte[]> newAndroidAesByteSecurityService() {
        return newSecurityService(newAndroidAesByteCipherer(), newAndroidAesSecretKeyProvider(), newAndroidSaltGenerator(), newAndroidSha512ByteHashProvider());
    }

    @NotNull
    public static SecurityService<byte[], byte[], byte[]> newAndroidAesByteSecurityService(final byte[] initialVector) {
        return newSecurityService(newAndroidAesByteCipherer(initialVector), newAndroidAesSecretKeyProvider(), newAndroidSaltGenerator(), newAndroidSha512ByteHashProvider());
    }

    @NotNull
    public static SecurityService<String, String, String> newAndroidAesStringSecurityService() {
        return newSecurityService(newAndroidAesStringCipherer(), newAndroidAesSecretKeyProvider(), newAndroidSaltGenerator(), newAndroidSha512StringHashProvider());
    }
}
