package org.solovyev.android.security;

import org.jetbrains.annotations.NotNull;
import org.solovyev.android.security.base64.ABase64StringDecoder;
import org.solovyev.android.security.base64.ABase64StringEncoder;
import org.solovyev.common.security.*;
import org.solovyev.common.text.StringDecoder;
import org.solovyev.common.text.StringEncoder;

public final class ASecurity extends Security {

    private ASecurity() {
        throw new AssertionError();
    }

    @NotNull
    public static Cipherer<byte[], byte[]> newAndroidAesByteCipherer() {
        return Security.newAndroidAesCipherer();
    }

    @NotNull
    public static SecretKeyProvider newAndroidAesSecretKeyProvider() {
        return Security.newAndroidSecretKeyProvider();
    }

    @NotNull
    public static Cipherer<String, String> newAndroidAesStringCipherer() {
        return TypedCipherer.newInstance(newAndroidAesByteCipherer(), StringDecoder.getInstance(), StringEncoder.getInstance(), ABase64StringDecoder.getInstance(), ABase64StringEncoder.getInstance());
    }
}
